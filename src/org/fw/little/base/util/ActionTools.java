package org.fw.little.base.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fw.little.base.annotation.Controller;
import org.fw.little.base.annotation.RequestMapping;
import org.fw.little.base.core.LittleAction;
import org.fw.little.base.core.LittleController;

/**
 * action工具类
 * **/
public class ActionTools 
{

	/**
	 * 加载全部控制器中的action
	 * **/
	@SuppressWarnings("unchecked")
	public Map<String,LittleAction> getActions(List<Class<?>> classes)throws Exception
	{
		Map<String,LittleAction> actions = new HashMap<String, LittleAction>();
		Controller con                   = null;
		RequestMapping req               = null;
		LittleAction action              = null;
		
		for(Class<?> itemClass : classes)
		{
			con = itemClass.getDeclaredAnnotation(Controller.class);
			if(null != con)
			{
				for(Method method : itemClass.getDeclaredMethods())
				{
					req = method.getDeclaredAnnotation(RequestMapping.class);
					if(null != req)
					{
						action = new LittleAction(req.url(), (Class<? extends LittleController>)itemClass, method);
						actions.put(req.url(), action);
					}
				}
			}
		}
		
		return actions;
	}
}
