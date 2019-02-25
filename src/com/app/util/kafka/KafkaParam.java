package com.app.util.kafka;

import java.lang.reflect.Method;

/**
 * 异步任务参数
 * **/
public class KafkaParam
{

	//队列名称
	private String name;
	
	//线程数
	private int num;
	
	//对应的方法
	private Method method;
	
	//实体类
	private Object obj;
	
	//类
	private Class<?> clazz;
	
	public Class<?> getClazz()
	{
		return clazz;
	}

	public void setClazz(Class<?> clazz)
	{
		this.clazz = clazz;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public int getNum()
	{
		return num;
	}

	public void setNum(int num) 
	{
		this.num = num;
	}

	public Method getMethod()
	{
		return method;
	}

	public void setMethod(Method method)
	{
		this.method = method;
	}

	public Object getObj()
	{
		return obj;
	}

	public void setObj(Object obj)
	{
		this.obj = obj;
	}
	
}
