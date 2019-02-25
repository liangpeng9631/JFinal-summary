package com.app.util.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.app.util.io.IoTools;

/**
 * http通信工具
 * **/
public class HttpClient 
{
	
	private int getCode;

	private int postCode;

	private RequestConfig config;
	
	private final static String pool = "http";
	
	private Header[] responseHeads;
	
	private Map<String,String> requestHeads;
	
	private LayeredConnectionSocketFactory ssf;
	
	/**
	 * 获取get响应码
	 * **/
	public int getGetCode() 
	{
		return getCode;
	}

	/**
	 * 获取post响应码
	 * **/
	public int getPostCode() 
	{
		return postCode;
	}

	public HttpClient()
	{
		 this.config = RequestConfig.custom()
				  .setSocketTimeout(30000)
				  .setConnectTimeout(30000)
				  .setConnectionRequestTimeout(30000)
				  .build();
	}
	
	/**
	 * 支持代理的客户端
	 * @param 代理服务器地址
	 * @param 端口
	 * **/
	public HttpClient(String poxy,int port)
	{
		 this.config = RequestConfig.custom()
				  .setSocketTimeout(10000)
				  .setConnectTimeout(10000)
				  .setConnectionRequestTimeout(10000)
				  .setProxy(new HttpHost(poxy, port, HttpClient.pool))
				  .build();
	}
	
	/**
	 * 设置请求头
	 * @param requestHeads 请求头数组
	 * **/
	public void setRequestHeads(Map<String,String> requestHeads)
	{
		this.requestHeads = requestHeads;
	}
	
	/**
	 * 获取响应头
	 * 
	 * **/
	public Header[] getResponseAllHeads()
	{
		return this.responseHeads;
	}
	
	/**
	 * 设置ssl
	 * **/
	public void setSsf(LayeredConnectionSocketFactory ssf) 
	{
		this.ssf = ssf;
	}
	
	/**
	 * 根据目录加载ssl证书
	 * **/
	public void loadSSL(String filePath)throws Exception 
	{

		FileInputStream instream = new FileInputStream(filePath);
		SSLContext sslContext    = SSLContext.getInstance("SSL");
		
		KeyStore keyStore = KeyStore.getInstance("JKS");
		         keyStore.load(instream, "changeit".toCharArray());
		
		instream.close();
		
		//构建TrustManager
		TrustManagerFactory  trustManagerFactory = TrustManagerFactory.getInstance("SunX509");   
		                     trustManagerFactory.init(keyStore);   
		TrustManager[] tms = trustManagerFactory.getTrustManagers();     
		 
		//构建Key Manager
		KeyManagerFactory  keyManagerFactory = KeyManagerFactory.getInstance("SunX509");   
		                   keyManagerFactory.init(keyStore, "changeit".toCharArray());           
		KeyManager[] kms = keyManagerFactory.getKeyManagers();
		        
		sslContext.init(kms, tms, null);
		
		this.ssf = new SSLConnectionSocketFactory(sslContext);
	}
	
	/**
	 * 私有证书
	 * **/
	public void loadSSLToPrivate()throws Exception 
	{

		SSLContext sslContext    = SSLContext.getInstance("TLS");
		sslContext.init(null, new TrustManager[] { new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] chain, String authType) {}
			public void checkServerTrusted(X509Certificate[] chain, String authType) {}
			public X509Certificate[] getAcceptedIssuers() {return null;}
		} }, new SecureRandom());
		
		this.ssf = new SSLConnectionSocketFactory(sslContext,new HostnameVerifier(){

			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
			
		});
	}
	
	
	/**
	 * get请求
	 * @param url 请求地址
	 * **/
	public String get(String url,String charset)throws Exception
	{
		
		CloseableHttpClient httpClient;
		
		if(null != ssf){
			httpClient = HttpClients.custom().setSSLSocketFactory(this.ssf).setDefaultRequestConfig(config).build();
		}else{
			httpClient = HttpClients.custom().setDefaultRequestConfig(config).build();
		}
		
		HttpGet httpget                = new HttpGet(url);
		
		//设置请求头
		setHeader(httpget, this.requestHeads);
		
		
		CloseableHttpResponse httpResponse = httpClient.execute(httpget);
        this.getCode                       = httpResponse.getStatusLine().getStatusCode();
        HttpEntity responseEntity          = httpResponse.getEntity();
        
		//获取响应头
		this.responseHeads = httpResponse.getAllHeaders();

		InputStream in                     = responseEntity.getContent();
		String response                    = IoTools.InputStreatToString(in,charset);
		
		//资源回收
		in.close();
		httpget.abort();
		httpResponse.close();
		return response;
	}
	
	
	/**
	 * get请求下载文件
	 * @param url 请求地址
	 * **/
	public byte[] getFileToBytes(String url)throws Exception
	{
		
		CloseableHttpClient httpClient;
		
		if(null != ssf){
			httpClient = HttpClients.custom().setSSLSocketFactory(this.ssf).setDefaultRequestConfig(config).build();
		}else{
			httpClient = HttpClients.custom().setDefaultRequestConfig(config).build();
		}
		
		HttpGet httpget                    = new HttpGet(url);
		
		//设置请求头
		setHeader(httpget, this.requestHeads);
		
        CloseableHttpResponse httpResponse = httpClient.execute(httpget);
        this.getCode                       = httpResponse.getStatusLine().getStatusCode();
        HttpEntity responseEntity          = httpResponse.getEntity();
        
		//获取响应头
		this.responseHeads = httpResponse.getAllHeaders();
		
		InputStream in                     = responseEntity.getContent();
		byte[] response                    = IoTools.InputStreatToBytes(in);
		
		//资源回收
		in.close();
		httpget.abort();
		httpResponse.close();
		return response;
	}
	
	public InputStream getFileToInputStream(String url)throws Exception
	{
		
		CloseableHttpClient httpClient     = HttpClients.custom().setDefaultRequestConfig(config).build();
		HttpGet httpget                    = new HttpGet(url);
        CloseableHttpResponse httpResponse = httpClient.execute(httpget);
        this.getCode                       = httpResponse.getStatusLine().getStatusCode();
        HttpEntity responseEntity          = httpResponse.getEntity();
		return responseEntity.getContent();
	}
	
	/**
	 * post请求
	 * @param url    地址
	 * @param params 参数
	 * List<NameValuePair> params = new ArrayList<NameValuePair>();
	 * params.add(new BasicNameValuePair("name","snake"));
	 * params.add(new BasicNameValuePair("pwd","123"));
	 * params.add(new BasicNameValuePair("html","<hr/>"));
	 * **/
	public String post(String url,List<NameValuePair> params,String charset)throws Exception
	{
		
		CloseableHttpClient httpClient;

		if(null != ssf){
			httpClient = HttpClients.custom().setSSLSocketFactory(this.ssf).setDefaultRequestConfig(config).build();
		}else{
			httpClient = HttpClients.custom().setDefaultRequestConfig(config).build();
		}
		
		HttpPost post   = new HttpPost(url);
		HttpEntity data = new UrlEncodedFormEntity(params, "UTF-8");
		
		//设置请求头
		setHeader(post, this.requestHeads);
			
		//设置提交数据
		post.setEntity(data);
		
		CloseableHttpResponse httpResponse = httpClient.execute(post);
		 this.postCode                     = httpResponse.getStatusLine().getStatusCode();
		HttpEntity responseEntity          = httpResponse.getEntity();
		
		//获取响应头
		this.responseHeads = httpResponse.getAllHeaders();
		
		InputStream in                     = responseEntity.getContent();
		String response                    = IoTools.InputStreatToString(in,charset);		
		//资源回收
		in.close();
		post.abort();
		httpResponse.close();
		
		return response;
	}
	
	/**
	 * post请求
	 * @param url     Sting  地址
	 * @param body    String 数据
	 * @param charset String 字符编码
	 * **/
	public String post(String url,String body,String charset)throws Exception
	{
		
		CloseableHttpClient httpClient;

		if(null != ssf){
			httpClient = HttpClients.custom().setSSLSocketFactory(this.ssf).setDefaultRequestConfig(config).build();
		}else{
			httpClient = HttpClients.custom().setDefaultRequestConfig(config).build();
		}
		
		HttpPost post   = new HttpPost(url);
		
		//设置请求头
		setHeader(post, this.requestHeads);
		
		//设置请求数据
		post.setEntity(new StringEntity(body,Charset.forName(charset)));
		
		CloseableHttpResponse httpResponse = httpClient.execute(post);
		this.postCode                      = httpResponse.getStatusLine().getStatusCode();
		HttpEntity responseEntity          = httpResponse.getEntity();
		
		//获取响应头
		this.responseHeads = httpResponse.getAllHeaders();
		
		InputStream in                     = responseEntity.getContent();
		String response                    = IoTools.InputStreatToString(in,charset);		
		//资源回收
		in.close();
		post.abort();
		httpResponse.close();
		
		return response;
	}
	
	public String postFile(String url,String name,File file,String charset)throws Exception
	{
		
		CloseableHttpClient httpClient;

		if(null != ssf){
			httpClient = HttpClients.custom().setSSLSocketFactory(this.ssf).setDefaultRequestConfig(config).build();
		}else{
			httpClient = HttpClients.custom().setDefaultRequestConfig(config).build();
		}
		
		HttpPost post   = new HttpPost(url);
		
		//设置请求头
		setHeader(post, this.requestHeads);
		
		//设置上传文件
		MultipartEntityBuilder mult = MultipartEntityBuilder.create();

		mult.addBinaryBody(name, file);
		
		//设置请求数据
		post.setEntity(mult.build());
		
		CloseableHttpResponse httpResponse = httpClient.execute(post);
		this.postCode                      = httpResponse.getStatusLine().getStatusCode();
		HttpEntity responseEntity          = httpResponse.getEntity();
		
		//获取响应头
		this.responseHeads = httpResponse.getAllHeaders();
		
		InputStream in                     = responseEntity.getContent();
		String response                    = IoTools.InputStreatToString(in,charset);		
		//资源回收
		in.close();
		post.abort();
		httpResponse.close();
		
		return response;
	}
	
	/**
	 * 设置请求头
	 * @param base    HttpRequestBase     请求方法
	 * @param headers Map<String, String> 请求头
	 * **/
	private void setHeader(HttpRequestBase base , Map<String, String> headers)
	{
		if(null != headers)
		{
			for(String key:requestHeads.keySet())
			{
				base.setHeader(key, requestHeads.get(key));
			}
		}
	}
	
	
	/**
	 * 
	 * @param url
	 * @param querys
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public String get(String url,Map<String, String> querys,String charset)throws Exception
	{
		
		CloseableHttpClient httpClient;
		
		if(null != ssf){
			httpClient = HttpClients.custom().setSSLSocketFactory(this.ssf).setDefaultRequestConfig(config).build();
		}else{
			httpClient = HttpClients.custom().setDefaultRequestConfig(config).build();
		}
		
		HttpGet httpget                = new HttpGet(buildUrl(url, querys));
		
		//设置请求头
		setHeader(httpget, this.requestHeads);
		
		
		CloseableHttpResponse httpResponse = httpClient.execute(httpget);
        this.getCode                       = httpResponse.getStatusLine().getStatusCode();
        HttpEntity responseEntity          = httpResponse.getEntity();
        
		//获取响应头
		this.responseHeads = httpResponse.getAllHeaders();

		InputStream in                     = responseEntity.getContent();
		String response                    = IoTools.InputStreatToString(in,charset);
		
		//资源回收
		in.close();
		httpget.abort();
		httpResponse.close();
		return response;
	}
	
	/**
	 * 组合参数
	 * @param url
	 * @param querys
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static String buildUrl(String url, Map<String, String> querys) throws UnsupportedEncodingException {
    	StringBuilder sbUrl = new StringBuilder();
    	sbUrl.append(url);
    	if (null != querys) {
    		StringBuilder sbQuery = new StringBuilder();
        	for (Map.Entry<String, String> query : querys.entrySet()) {
        		if (0 < sbQuery.length()) {
        			sbQuery.append("&");
        		}
        		if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
        			sbQuery.append(query.getValue());
                }
        		if (!StringUtils.isBlank(query.getKey())) {
        			sbQuery.append(query.getKey());
        			if (!StringUtils.isBlank(query.getValue())) {
        				sbQuery.append("=");
        				sbQuery.append(URLEncoder.encode(query.getValue(), "utf-8"));
        			}        			
                }
        	}
        	if (0 < sbQuery.length()) {
        		sbUrl.append("?").append(sbQuery);
        	}
        }
    	
    	return sbUrl.toString();
    }

}