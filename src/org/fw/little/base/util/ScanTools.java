package org.fw.little.base.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.tree.AnnotationNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;

/**
 * 扫描工具类
 * **/
public class ScanTools
{
	/**
	 * 根路径
	 * **/
	private String basePath;
	
	/**
	 * class列表
	 * **/
	private List<String> classeList = new ArrayList<String>();
	
	/**
	 * 控制器列表
	 * **/
	private List<Class<?>> controllerList = new ArrayList<Class<?>>();
	
	
	/**
	 * 业务模块列表 
	 * **/
	
	
	public ScanTools()
	{
		basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	}
	
	/**
	 * 加载class文件
	 * @param basePackage 包名
	 * **/
	public void loadClassFile(String basePackage) throws Exception
	{
		File file = new File(basePath+basePackage.replaceAll("\\.", "/"));
		
		for(File itemFile : file.listFiles())
		{
			//如果是class文件
			if(itemFile.isFile() && itemFile.getName().indexOf(".class") != -1)
			{
				classeList.add(basePackage+"."+itemFile.getName().replace(".class", ""));
			}

			//如果是文件夹继续向下扫描
			if(itemFile.isDirectory())
				loadClassFile(basePackage+"."+itemFile.getName());
		}
	}
	
	public List<String> getClasseList()
	{
		return classeList;
	}
	
	/**
	 * 获取controller列表
	 * @param clzz 控制器注释类
	 * **/
	public List<Class<?>> getControllerList(Class<?> clzz) throws Exception
	{
		ClassNode cn      = null;
		ClassReader read  = null;
		String controller = clzz.getName();
		
		for(String item : classeList)
		{
			cn   = new ClassNode(Opcodes.ASM5);
			read = new ClassReader(item);
			read.accept(cn, 0);
			
			//过滤@Controller
			if(null != cn.visibleAnnotations)
			{
				for(AnnotationNode vitem :cn.visibleAnnotations)
				{
					if(controller.equals(Type.getType(vitem.desc).getClassName()))
					{
						controllerList.add(Class.forName(item));
					}
				}
			}
		}
		return controllerList;
	}

}