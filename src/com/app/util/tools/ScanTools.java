package com.app.util.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 扫描工具类
 **/
public class ScanTools 
{
	private String basePath;

	private List<Class<?>> classeList = new ArrayList<Class<?>>();

	public ScanTools() 
	{
		basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	}

	/**
	 * 加载class文件
	 * @param basePackage 包名
	 **/
	public void loadClassFile(String basePackage) throws Exception
	{
		File file = new File(basePath + basePackage.replaceAll("\\.", "/"));

		for (File itemFile : file.listFiles()) {
			// 如果是class文件
			if (itemFile.isFile() && itemFile.getName().indexOf(".class") != -1)
				classeList.add(Class.forName(basePackage + "." + itemFile.getName().replace(".class", "")));

			// 如果是文件夹继续向下扫描
			if (itemFile.isDirectory())
				loadClassFile(basePackage + "." + itemFile.getName());
		}
	}

	public List<Class<?>> getClasseList()
	{
		return classeList;
	}
}