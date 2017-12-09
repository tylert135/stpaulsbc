package com.tylert.singletons.calendarMetadata;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.tylert.domain.calendar.CalendarEvents;
import com.tylert.domain.calendar.CalendarEventsJSONAdapter;

public interface CalendarCrudBase
{
	public List<CalendarEvents> getCalendarEvents(String realPath, String calDate) throws FileNotFoundException, IOException;
	
	public void saveEventData(String realPath, CalendarEvents ce, 
			CalendarEventsJSONAdapter origCe) throws IOException;
	
	public String getName();
	public void setName(String name);
}
