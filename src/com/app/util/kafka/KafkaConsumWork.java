package com.app.util.kafka;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.app.conf.AppConfig;

import kafka.consumer.ConsumerIterator;

/**
 * 消息队列消费工具类,用于异步执行任务
 * **/
public class KafkaConsumWork implements Runnable
{

	private ConsumerIterator<String, String> it;
	
	private KafkaParam param;
	
	public KafkaConsumWork(ConsumerIterator<String, String> it,KafkaParam param)
	{
		this.it    = it;
		this.param = param;
	}
	
	public void run() 
	{
		while(it.hasNext())
		{
			try
			{
				//调用对应逻辑
				param.getMethod().invoke(param.getObj(), it.next().message());
			}
			catch(Exception exe)
			{
				exe.printStackTrace();
				
				StringWriter sw = new StringWriter();
				exe.printStackTrace(new PrintWriter(sw, true));
				
				//写异常日志
				AppConfig.getAppConfig().getLogger().info("异常:"+sw.toString());
				
				//异常信息写入mongo
				AppConfig.getAppConfig().getMongoUtil().saveException(exe.getMessage(),sw.toString());
				
				try
				{
					sw.close();
				} 
				catch (IOException e)
				{
					AppConfig.getAppConfig().getLogger().info(e.getMessage());
				}
				
			}

		}
	}	
}