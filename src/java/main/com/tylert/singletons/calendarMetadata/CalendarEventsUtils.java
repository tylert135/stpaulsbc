package com.tylert.singletons.calendarMetadata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;

import com.tylert.xmlBeans.eventCalendar.DayDocument;
import com.tylert.xmlBeans.eventCalendar.EventCalendarDocument;
import com.tylert.xmlBeans.eventCalendar.EventCalendarDocument.EventCalendar;

import com.tylert.domain.calendar.AbstractCalendarEvents;
import com.tylert.domain.calendar.CalendarEvents;
import com.tylert.domain.calendar.CalendarEventsJSONAdapter;
import com.tylert.stats.WebStatisticsUtils;

public class CalendarEventsUtils implements CalendarCrudBase
{
	public static String EVENTS_PATH = "metaData/events/";
	private String name;

	public List<CalendarEvents> getCalendarEvents(String realPath,
			String calDate) throws FileNotFoundException, IOException
	{
		List<CalendarEvents> ceList = new ArrayList<CalendarEvents>();
		File f = new File((new StringBuilder()).append(realPath)
				.append("/metaData/events/").append(calDate).append(".xml")
				.toString());

		try
		{
			EventCalendarDocument eventCalDoc = EventCalendarDocument.Factory
					.parse(f);

			EventCalendar eventCal = eventCalDoc.getEventCalendar();
			DayDocument.Day dArray[] = eventCal.getEvents().getDayArray();

			for (int j = 0; j < dArray.length; j++)
			{
				if (dArray[j].getWhat() != null)
				{
					CalendarEvents ce = new CalendarEvents();
					ce.getDisplayProp().setBackgroundColor(
							dArray[j].getBgcolor());
					ce.getDisplayProp().setForegroundColor(
							dArray[j].getFgcolor());
					ce.setTitle(dArray[j].getWhat());
					ce.setStartDate(dArray[j].getStartDate());
					ce.setStartHrs(dArray[j].getStartHour().toString());
					ce.setStartMins(dArray[j].getStartMinute().toString());
					ce.setAllDay(dArray[j].getAllDay() ? AbstractCalendarEvents.TRUE
							: AbstractCalendarEvents.FALSE);
					ce.setEndDate(dArray[j].getEndDate());
					ce.setEndHrs(dArray[j].getEndHour().toString());
					ce.setEndMins(dArray[j].getEndMinute().toString());
					ce.setStartAmPm(dArray[j].getStartAmPm());
					ce.setEndAmPm(dArray[j].getEndAmPm());
					ceList.add(ce);
				}
			}

		}
		catch (FileNotFoundException e)
		{
			WebStatisticsUtils.logMsg("No calendar month found, " + calDate
					+ ".xml. Therefore no events saved.");
		}
		catch (XmlException e)
		{
			e.printStackTrace();
		}
		return ceList;
	}

	public void saveEventData(String realPath, CalendarEvents ce,
			CalendarEventsJSONAdapter origCe) throws IOException
	{
		File f = new File(realPath);

		try
		{
			int i = 0;
			EventCalendarDocument eventCalDoc = null;
			DayDocument.Day d = null;
			EventCalendar eventCal = null;
			if (f.exists())
			{
				eventCalDoc = EventCalendarDocument.Factory.parse(f);
				eventCal = eventCalDoc.getEventCalendar();

				if (!AbstractCalendarEvents.NEW.equals(ce.getAction()))
				{
					// find original from file
					DayDocument.Day dArray[] = eventCal.getEvents()
							.getDayArray();
					for (i = 0; i < dArray.length; i++)
					{
						d = dArray[i];
						if (d.getId() == null)
						{
							d = null;
							continue;
						}
						// find calendar event
						if (origCe.getTitle().equals(d.getWhat())
								&& d.getStartDate().equals(
										origCe.getStartDate())
								&& d.getEndDate().equals(origCe.getEndDate())
								&& d.getStartHour().toString()
										.equals(origCe.getStartHrs())
								&& d.getStartMinute().toString()
										.equals(origCe.getStartMins())
								&& d.getEndHour().toString()
										.equals(origCe.getEndHrs())
								&& d.getEndMinute().toString()
										.equals(origCe.getEndMins()))
						{
							// found
							break;
						}
						d = null;
					}
				}
				if (d != null)
				{
					// remove old event
					eventCal.getEvents().removeDay(i);
				}
				d = eventCal.getEvents().addNewDay();
			}
			else
			{
				eventCalDoc = EventCalendarDocument.Factory.newInstance();
				eventCal = eventCalDoc.addNewEventCalendar();
				d = eventCal.addNewEvents().addNewDay();
			}
			if (!AbstractCalendarEvents.DELETE.equals(ce.getAction()))
			{
				updateXmlbeansDay(d, ce);
			}
			XmlOptions xmlOptions = new XmlOptions();
			xmlOptions.setSavePrettyPrint();
			xmlOptions.setSavePrettyPrintIndent(4);
			eventCalDoc.save(f, xmlOptions);
		}
		catch (XmlException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return;
	}

	private void updateXmlbeansDay(DayDocument.Day d, CalendarEvents ce)
	{
		Random generator = new Random();
		int r = generator.nextInt();
		d.setId(new BigInteger(Integer.toString(r)));
		if (ce.getAllDay() != null)
			d.setAllDay(ce.getAllDay() == AbstractCalendarEvents.TRUE ? true
					: false);
		d.setWhat(ce.getTitle());
		d.setBgcolor(ce.getDisplayProp().getBackgroundColor());
		d.setFgcolor(ce.getDisplayProp().getForegroundColor());

		d.setStartDate(ce.getStartDate());
		if (ce.getStartAmPm().equals("PM"))
		{
			if (!ce.getStartHrs().equals("12"))
				d.setStartHour(new BigInteger(ce.getStartHrs())
						.add(new BigInteger("12")));
			else
				d.setStartHour(new BigInteger(ce.getStartHrs()));
		}
		else
		{
			if (ce.getStartHrs().equals("12"))
				d.setStartHour(new BigInteger("0"));
			else
				d.setStartHour(new BigInteger(ce.getStartHrs()));
		}
		d.setStartMinute(new BigInteger(ce.getStartMins()));
		d.setStartAmPm(ce.getStartAmPm());

		if (ce.getEndAmPm().equals("PM"))
		{
			if (!ce.getEndHrs().equals("12"))
				d.setEndHour(new BigInteger(ce.getEndHrs()).add(new BigInteger(
						"12")));
			else
				d.setEndHour(new BigInteger(ce.getEndHrs()));
		}
		else
		{
			if (ce.getEndHrs().equals("12"))
				d.setEndHour(new BigInteger("0"));
			else
				d.setEndHour(new BigInteger(ce.getEndHrs()));
		}
		d.setEndDate(ce.getEndDate());
		d.setEndMinute(new BigInteger(ce.getEndMins()));
		d.setEndAmPm(ce.getEndAmPm());

	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
