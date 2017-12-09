package com.tylert.domain.calendar;

import java.util.Calendar;

public class WebStatisticData
{
	public static String VISIT = "visit";
	public static String SECTION_CHANGE = "sectionChange";
	public static String REQUEST = "request";
	public static String NAVIGATE = "navigate";
	public static String CALENDAR = "calendar";
	public static String PHOTO = "photoGallery";
	public static String OTHER = "other";

	Calendar date;
	String ipAddress;
	String sessionId;
	String username;
	String role;
	String realPath;
	String webpage;
	
	
	String type;
	String oldXml;
	String newXml;
	String text;
	
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public Calendar getDate()
	{
		return date;
	}
	public void setDate(Calendar date)
	{
		this.date = date;
	}
	public String getOldXml()
	{
		return oldXml;
	}
	public void setOldXml(String oldXml)
	{
		this.oldXml = oldXml;
	}
	public String getNewXml()
	{
		return newXml;
	}
	public void setNewXml(String newXml)
	{
		this.newXml = newXml;
	}
	public String getText()
	{
		return text;
	}
	public void setText(String text)
	{
		this.text = text;
	}
	public String getIpAddress()
	{
		return ipAddress;
	}
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}
	public String getSessionId()
	{
		return sessionId;
	}
	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}
	public String getUsername()
	{
		return username;
	}
	public void setUsername(String username)
	{
		this.username = username;
	}
	public String getRole()
	{
		return role;
	}
	public void setRole(String role)
	{
		this.role = role;
	}
	public String getWebpage()
	{
		return webpage;
	}
	public void setWebpage(String webpage)
	{
		this.webpage = webpage;
	}
	public String getRealPath()
	{
		return realPath;
	}
	public void setRealPath(String realPath)
	{
		this.realPath = realPath;
	}
}
