package com.app.util.tools;

import com.app.annotation.UrlMapper;
import com.jfinal.config.Routes;

public class RouterTools 
{
	@SuppressWarnings("unchecked")
	public void loadController(Routes routes,String basePackage)
	{
		try 
		{
			ScanTools scan = new ScanTools();
			scan.loadClassFile(basePackage);
			UrlMapper con = null;
			
			for(Class<?> clzz : scan.getClasseList())
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
