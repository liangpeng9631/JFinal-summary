package com.app.util.mongo;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import com.app.conf.AppConfig;
import com.app.util.tools.SYS;
import com.app.util.tools.SYS_BASE;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoDatabase;

/**
 * mongodb工具类
 * **/
public class MongoUtil 
{
	private MongoClient client;
	
	/**
	 * 
	 * **/
	public MongoUtil(String host,Map<String,String> conf)
	{
		MongoClientOptions.Builder build = new MongoClientOptions.Builder();
								   build.connectionsPerHost(Integer.parseInt(conf.get("connectionsPerHost")));
								   build.threadsAllowedToBlockForConnectionMultiplier(Integer.parseInt(conf.get("threadsAllowedToBlockForConnectionMultiplier")));
								   build.maxWaitTime(Integer.parseInt(conf.get("maxWaitTime")));
								   build.connectTimeout(Integer.parseInt(conf.get("connectTimeout")));
		
		client = new MongoClient(host,build.build());
	}
	
	/**
	 * 获取mongo客户端
	 * **/
	public MongoClient getClient()
	{
		return client;
	}
	
	/**
	 * 关闭连接
	 * **/
	public void close()
	{
		client.close();
	}
	
	/**
	 * 保存异常
	 * @param summary 简述
	 * @param message 明细
	 * **/
	public void saveException(String summary,String message)
	{		
		//发送报警短信
		MongoClient client = AppConfig.getAppConfig().getMongoUtil().getClient();
		MongoDatabase db   = client.getDatabase("ykb_log");
		
		Map<String,Object> map = new HashMap<String,Object>();
						   map.put("OD", SYS_BASE.longTimeToStr(SYS_BASE.nowTime(), SYS.DATE_FORMAT_DF));
						   map.put("TravelType","机票");
						   map.put("SeverityLevel","3");
						   map.put("Summary",summary);
						   map.put("Content",message);
						   map.put("Status","0");
						   
		Document document = new Document(map);
		
		db.getCollection("UrgencyErrorLog").insertOne(document);
	}
}