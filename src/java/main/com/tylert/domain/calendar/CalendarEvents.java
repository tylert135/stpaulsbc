package com.tylert.domain.calendar;

public class CalendarEvents extends AbstractCalendarEvents
{

	private String agendaId;
	private String title;
	private String startDate;
	private String endDate;
	private String allDay;
	private String data;
	private String startHrs;
	private String endHrs;
	private String startMins;
	private String endMins;
	private String action;
	private String startAmPm;
	private String endAmPm;
	
	private DisplayProp displayProp;
	public static class DisplayProp
	{
		private String backgroundColor;
		private String foregroundColor;
		public String getBackgroundColor()
		{
			return backgroundColor;
		}
		public void setBackgroundColor(String backgroundColor)
		{
			this.backgroundColor = backgroundColor;
		}
		public String getForegroundColor()
		{
			return foregroundColor;
		}
		public void setForegroundColor(String foregroundColor)
		{
			this.foregroundColor = foregroundColor;
		}
	}
	
	public CalendarEvents()
	{
		this.displayProp = new DisplayProp();
	}
	
	public String getAgendaId()
	{
		return agendaId;
	}
	public void setAgendaId(String agendaId)
	{
		this.agendaId = agendaId;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getStartDate()
	{
		return startDate;
	}
	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}
	public String getEndDate()
	{
		return endDate;
	}
	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}
	public String getAllDay()
	{
		return allDay;
	}
	public void setAllDay(String allDay)
	{
		this.allDay = allDay;
	}
	public String getData()
	{
		return data;
	}
	public void setData(String data)
	{
		this.data = data;
	}
	public String getStartHrs()
	{
		return startHrs;
	}
	public void setStartHrs(String startHrs)
	{
		this.startHrs = startHrs;
	}
	public String getEndHrs()
	{
		return endHrs;
	}
	public void setEndHrs(String endHrs)
	{
		this.endHrs = endHrs;
	}
	public String getStartMins()
	{
		return startMins;
	}
	public void setStartMins(String startMins)
	{
		this.startMins = startMins;
	}
	public String getEndMins()
	{
		return endMins;
	}
	public void setEndMins(String endMins)
	{
		this.endMins = endMins;
	}
	public DisplayProp getDisplayProp()
	{
		return displayProp;
	}
	public void setDisplayProp(DisplayProp displayProp)
	{
		this.displayProp = displayProp;
	}
	public String getAction()
	{
		return action;
	}
	public void setAction(String action)
	{
		this.action = action;
	}
	public String getStartAmPm()
	{
		return startAmPm;
	}
	public void setStartAmPm(String startAmPm)
	{
		this.startAmPm = startAmPm;
	}
	public String getEndAmPm()
	{
		return endAmPm;
	}
	public void setEndAmPm(String endAmPm)
	{
		this.endAmPm = endAmPm;
	}
	@Override
	public String getBgColor()
	{
		return getDisplayProp().getBackgroundColor();
	}
	@Override
	public String getFgColor()
	{
		return getDisplayProp().getForegroundColor();
	}
}
