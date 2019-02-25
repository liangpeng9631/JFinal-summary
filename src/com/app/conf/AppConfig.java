package com.app.conf;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.app.util.tools.RouterTools;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;

public class AppConfig extends JFinalConfig 
{

	private static volatile AppConfig appConfig = null;
	
	/**
	 * 其他扩展配置
	 * **/
	private Map<String,String> extendsConfig = null;
	
	/**
	 * json工具类
	 * **/
	private ObjectMapper jsonMapper = new ObjectMapper();
	
	/**
	 * 日志
	 * **/
	private Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
	
	
	public AppConfig()
	{
		appConfig = this;
	}
	
	public static AppConfig getAppConfig()
	{
		return appConfig;
	}
	
	/**
	 * 常量配置
	 * 
	 * 路由信息
	 * 数据库视图信息
	 * 数据库连接信息
	 * 加载其他的拓展信息
	 * **/
	@Override
	public void configConstant(Constants constants)
	{
		//加载拓展配置

	}

	/**
	 * 
	 * **/
	@Override
	public void configHandler(Handlers handlers) 
	{

	}

	/**
	 * 注册系统拦截器
	 * **/
	@Override
	public void configInterceptor(Interceptors interceptors) 
	{

	}

	/**
	 * 注册插件
	 * **/
	@Override
	public void configPlugin(Plugins plugins) 
	{	

	}

	/**
	 * 注册路由,将URL与对应类匹配
	 * **/
	@Override
	public void configRoute(Routes routes) 
	{
		new RouterTools().loadController(routes, "com");
	}
	
	
	/**
	 * 获取其他拓展配置信息(短信,支付宝等第三方的接口)
	 * **/
	public Map<String, String> getExtendsConfig() 
	{
		return extendsConfig;
	}

	/**
	 * json操作类
	 * **/
	public ObjectMapper getJsonMapper() 
	{
		return jsonMapper;
	}
	
	/**
	 * jfinal启动调用
	 * **/
	@Override
	public void afterJFinalStart() 
	{
		
	}

	
	@Override
	public void beforeJFinalStop() 
	{
		
	}
	
	/**
	 * 加载log组件
	 * **/
	public Logger getLogger()
	{
		return logger;
	}

}