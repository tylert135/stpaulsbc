package com.tylert.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class WebLog
{
	private HashMap<Integer, String> tableRow = new HashMap<Integer, String>();
	private String headerRow;
	private String footerRow;
	private  int visitCount;
	private  int rowCtr;
	private  Calendar timeStamp;
	private List<String> logFiles = new ArrayList<String>();
	private boolean isMore;
	
	/********************************************************************************/
	public HashMap<Integer, String> getTableRow()
	{
		return tableRow;
	}
	public void setTableRow(HashMap<Integer, String> tableRow)
	{
		this.tableRow = tableRow;
	}
	public String getHeaderRow()
	{
		return headerRow;
	}
	public void setHeaderRow(String headerRow)
	{
		this.headerRow = headerRow;
	}
	public int getVisitCount()
	{
		return visitCount;
	}
	public void setVisitCount(int visitCount)
	{
		this.visitCount = visitCount;
	}
	public int getRowCtr()
	{
		return rowCtr;
	}
	public void setRowCtr(int rowCtr)
	{
		this.rowCtr = rowCtr;
	}
	public Calendar getTimeStamp()
	{
		return timeStamp;
	}
	public void setTimeStamp(Calendar timeStamp)
	{
		this.timeStamp = timeStamp;
	}
	public List<String> getLogFiles()
	{
		return logFiles;
	}
	public void setLogFiles(List<String> logFiles)
	{
		this.logFiles = logFiles;
	}
	public boolean isMore()
	{
		return isMore;
	}
	public void setMore(boolean isMore)
	{
		this.isMore = isMore;
	}
	public String getFooterRow()
	{
		return footerRow;
	}
	public void setFooterRow(String footerRow)
	{
		this.footerRow = footerRow;
	}
	
}
