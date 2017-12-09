package com.tylert.singletons.viewHtml;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.tylert.domain.ViewItem;

public interface IViewHtmlCrudBase
{
	public void saveHtmlFile(String realPath, ViewItem viewItem, HttpServletRequest request) throws IOException;
	public void saveHtmlFile(String realPath, String filename, String html, HttpServletRequest request) throws IOException;
	public String readHtmlFile(String realPath, ViewItem viewItem) throws IOException;
	public String readHtmlFile(String filename) throws IOException;
	
	public String getName();
	public void setName(String name);
}
