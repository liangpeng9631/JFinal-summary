package com.app.util.tools;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 * **/
public class IoTools
{

	/**
	 * 输入流转字符串读取,不对输入流进行关闭操作
	 * @param InputStream in 输入流
	 * @throws Exception
	 * @return String
	 * **/
	public static String InputStreatToString(InputStream in,String charset)throws Exception
	{
		return new String(IoTools.InputStreatToBytes(in),charset).trim();
	}
	
	/**
	 * 输入流转字节数组,不对输入流进行关闭操作
	 * @param InputStream in 输入流
	 * @throws Exception
	 * @return byte[]
	 * **/
	public static byte[] InputStreatToBytes(InputStream in)throws Exception
	{
		
		ByteArrayOutputStream outArray = new ByteArrayOutputStream();
		byte[] tempArray               = new byte[512];
		int len                        = 0;
		byte[] data                    = null;

		//批量读取数据
		while((len = in.read(tempArray)) != -1){
			outArray.write(tempArray, 0, len);
			tempArray = new byte[512];
		}
		
		//排除空数据
		len = 0;
		for(byte bte : tempArray){
			if(bte == 0)
				break;
			else
				len ++;
		}
		
		outArray.write(tempArray,0,len);
		data  = outArray.toByteArray();
		outArray.close();
		return data;
	}
	
	/**
	 * 提取字符串
	 * **/
	public static String loadStrByRegx(String regx,String str) throws Exception
	{
		Pattern pattern = Pattern.compile(regx);
		Matcher matcher = pattern.matcher(str);
		if(matcher.find())
			return str.substring(matcher.start(), matcher.end());
		else
			return "";
	}

}