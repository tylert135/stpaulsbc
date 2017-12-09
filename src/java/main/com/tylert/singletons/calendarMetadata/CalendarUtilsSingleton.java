package com.tylert.singletons.calendarMetadata;

import java.util.HashMap;

public class CalendarUtilsSingleton 
{
	public static String EVENTS_PATH = "metaData/events/";
	private static HashMap<String, CalendarEventsUtils> xmlCrudHashMap = new HashMap<String, CalendarEventsUtils>();
	
	public static synchronized CalendarCrudBase getInstance(String crudName)
	{
		if (xmlCrudHashMap.get(crudName) == null)
		{
			CalendarEventsUtils xmlCrud = new CalendarEventsUtils();
			xmlCrud.setName(crudName);
			xmlCrudHashMap.put(crudName, xmlCrud);
			return xmlCrud;
		}
		return xmlCrudHashMap.get(crudName);
	}

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Clone is not allowed.");
    }
    
}
