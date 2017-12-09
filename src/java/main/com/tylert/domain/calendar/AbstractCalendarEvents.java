package com.tylert.domain.calendar;

public abstract class AbstractCalendarEvents
{
    public static String FALSE = "false";
    public static String TRUE = "true";
    
    public static String SAVE = "Save";
    public static String NEW = "New";
    public static String DELETE = "Delete";
	
    abstract public String getBgColor();

    abstract public String getFgColor();

    abstract public String getTitle();

    abstract public String getStartDate();

    abstract public String getStartHrs();

    abstract public String getStartMins();

    abstract public String getAllDay();

    abstract public String getEndDate();

    abstract public String getEndHrs();

    abstract public String getEndMins();

    abstract public String getAction();

    abstract public String getStartAmPm();

    abstract public String getEndAmPm();

}
