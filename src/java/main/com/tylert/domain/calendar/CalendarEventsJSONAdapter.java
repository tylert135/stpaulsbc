package com.tylert.domain.calendar;

import java.util.StringTokenizer;

public class CalendarEventsJSONAdapter extends AbstractCalendarEvents
{
	private CalendarEvents cejson;
	
	public CalendarEventsJSONAdapter (CalendarEvents cejson, String origStartDate, String origEndDate)
	{
		if (cejson == null)
			return;
		
		this.cejson = cejson;
		
		//2010-12-04T13:00:00
		
		// start date and time
		StringTokenizer stok = new StringTokenizer(origStartDate,"T");
		cejson.setStartDate(stok.nextToken());
		stok = new StringTokenizer(stok.nextToken(),":");
		cejson.setStartHrs(stok.nextToken());
		cejson.setStartMins(stok.nextToken());

		// end date and time
		stok = new StringTokenizer(origEndDate,"T");
		cejson.setEndDate(stok.nextToken());
		stok = new StringTokenizer(stok.nextToken(),":");
		cejson.setEndHrs(stok.nextToken());
		cejson.setEndMins(stok.nextToken());
	}
	@Override
	public String getBgColor()
	{
		return cejson.getDisplayProp().getBackgroundColor();
	}
	@Override
	public String getFgColor()
	{
		return cejson.getDisplayProp().getForegroundColor();
	}
	@Override
	public String getTitle()
	{
		return cejson.getTitle();
	}
	@Override
	public String getStartDate()
	{
		return cejson.getStartDate();
	}
	@Override
	public String getStartHrs()
	{
		return cejson.getStartHrs();
	}
	@Override
	public String getStartMins()
	{
		return cejson.getStartMins();
	}
	@Override
	public String getAllDay()
	{
		return cejson.getAllDay();
	}
	@Override
	public String getEndDate()
	{
		return cejson.getEndDate();
	}
	@Override
	public String getEndHrs()
	{
		return cejson.getEndHrs();
	}
	@Override
	public String getEndMins()
	{
		return cejson.getEndMins();
	}
	@Override
	public String getAction()
	{
		return "N/A";
	}
	@Override
	public String getStartAmPm()
	{
		int hr = Integer.parseInt(cejson.getStartHrs());
		if (hr >= 12)
			return "PM";
		else
			return "AM";
	}
	@Override
	public String getEndAmPm()
	{
		int hr = Integer.parseInt(cejson.getEndHrs());
		if (hr >= 12)
			return "PM";
		else
			return "AM";
	}
}
