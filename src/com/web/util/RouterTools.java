package com.web.util;

import com.web.annotation.UrlMapper;

import org.fw.little.base.util.ScanTools;

import com.jfinal.config.Routes;

public class RouterTools 
{
	@SuppressWarnings("unchecked")
	public void loadController(Routes routes,String basePackage)
	{		
		ScanTools scanTools = new ScanTools();
		UrlMapper con       = null;
		
		try
		{
			scanTools.loadClassFile(basePackage);
			for(Class<?> clzz : scanTools.getControllerList(UrlMapper.class))
			{
				con = clzz.getDeclaredAnnotation(UrlMapper.class);
				if(null != con)
				{
					routes.add(con.val(), (Class<? extends com.jfinal.core.Controller>)clzz);
				}
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}