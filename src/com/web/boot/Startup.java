package com.web.boot;

import org.fw.little.base.boot.JettyStart;

import com.app.conf.info.Conf;
import com.jfinal.core.JFinalFilter;

public class Startup
{
	public static void main(String[] args)
	{
		JettyStart start = new JettyStart();
		start.setFilter(new JFinalFilter());
		start.setConfClass("com.app.conf.AppConfig");
		start.setPort(Integer.parseInt(System.getProperty("serverPort")));
		start.setPub(System.getProperty("user.dir")+Conf.www);
		start.setWelcomeFiles(new String[]{"index.html"});
		start.run();
	}	
}