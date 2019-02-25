package org.fw.little.base.boot;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyStart 
{
	private String confClass;
	
	/**
	 * 端口
	 * **/
	private int port;
	
	/**
	 * 站点个根目录
	 * **/
	private String pub;
	
	/**
	 * 过滤器
	 * **/
	private Filter filter;
	
	/**
	 * 欢迎页面
	 * **/
	private String[] welcomeFiles;
	
	public JettyStart(){}

	/**
	 * 设置配置文件
	 * **/
	public void setConfClass(String confClass) 
	{
		this.confClass = confClass;
	}

	/**
	 * 端口
	 * **/
	public void setPort(int port)
	{
		this.port = port;
	}

	/**
	 * 站点根目录
	 * **/
	public void setPub(String pub)
	{
		this.pub = pub;
	}
	
	/**
	 * 欢迎页列表
	 * **/
	public void setWelcomeFiles(String[] welcomeFiles)
	{
		this.welcomeFiles = welcomeFiles;
	}
	
	/**
	 * 设置过滤器
	 * **/
	public void setFilter(Filter filter)
	{
		this.filter = filter;
	}

	public void run()
	{
		EnumSet<DispatcherType> all = EnumSet.of(DispatcherType.ASYNC, 
								      			 DispatcherType.ERROR, 
								      			 DispatcherType.FORWARD,
								      			 DispatcherType.INCLUDE, 
								      			 DispatcherType.REQUEST);

		final Server server = new Server(port);
		
		try
		{
			FilterHolder filterHolder = new FilterHolder(filter);
			filterHolder.setInitParameter("configClass", confClass);
			
			WebAppContext context = new WebAppContext(pub,"/");
			context.addFilter(filterHolder, "/*", all);
			context.setWelcomeFiles(welcomeFiles);
			context.setDefaultsDescriptor("webdefault.xml");
			
			server.setHandler(context);
			server.setStopAtShutdown(true);
			server.start();
			server.join();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}