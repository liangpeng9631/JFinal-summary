package com.app.controller;

import org.apache.logging.log4j.Logger;

import com.app.annotation.UrlMapper;
import com.app.conf.AppConfig;
import com.jfinal.core.Controller;

@UrlMapper(val="/logs")
public class LogsController extends Controller
{
	private Logger logger = AppConfig.getAppConfig().getLogger();
	
	public void index()
	{
		renderText("LogsController->IndexAction");
	}
	
	/**
	 * 信息
	 * **/
	public void info()throws Exception
	{
		String name = this.getPara("name","none");
		String logs = this.getPara("logs","{}");
		
		logger.info(name+"#"+logs);
		
		renderText("SUCCESS");
	}
	
	/**
	 * 错误
	 * **/
	public void error()throws Exception
	{
		String name = this.getPara("name","none");
		String logs = this.getPara("logs","{}");
		
		logger.error(name+"#"+logs);
		
		renderText("SUCCESS");
	}
	
	/**
	 * 调试
	 * **/
	public void debug()throws Exception
	{
		String name = this.getPara("name","none");
		String logs = this.getPara("logs","{}");
		
		logger.debug(name+"#"+logs);
		
		renderText("SUCCESS");
	}
	
	/**
	 * 跟踪
	 * **/
	public void trace()throws Exception
	{
		String name = this.getPara("name","none");
		String logs = this.getPara("logs","{}");
		
		logger.trace(name+"#"+logs);
		
		renderText("SUCCESS");
	}
	
	/**
	 * 严重
	 * **/
	public void fatal()throws Exception
	{
		String name = this.getPara("name","none");
		String logs = this.getPara("logs","{}");
		
		logger.fatal(name+"#"+logs);
		
		renderText("SUCCESS");
	}
	
	/**
	 * 警告
	 * **/
	public void warn()throws Exception
	{
		String name = this.getPara("name","none");
		String logs = this.getPara("logs","{}");
		
		logger.warn(name+"#"+logs);
		
		renderText("SUCCESS");
	}
	
}