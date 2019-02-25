package com.app.controller;

import com.app.annotation.UrlMapper;
import com.jfinal.core.Controller;

@UrlMapper(val="/")
public class IndexController extends Controller
{
	public void index()
	{
		renderText("JFinal->IndexController->IndexAction");
	}	
}
