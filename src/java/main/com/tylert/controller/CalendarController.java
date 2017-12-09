package com.tylert.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.tylert.domain.calendar.AbstractCalendarEvents;
import com.tylert.domain.calendar.CalendarEvents;
import com.tylert.domain.calendar.CalendarEventsJSONAdapter;
import com.tylert.domain.calendar.WebStatisticData;
import com.tylert.singletons.calendarMetadata.CalendarCrudBase;
import com.tylert.singletons.calendarMetadata.CalendarUtilsSingleton;
import com.tylert.stats.WebStatisticsUtils;

@Controller
public class CalendarController {
	
	@RequestMapping(value = "/getEvents.htm", method = RequestMethod.GET)
	protected void getCalendarEvents(@RequestParam String calDate, 
			HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{

		WebStatisticsUtils.logSessionMsg(request.getSession(), "Get Calendar Events (" + calDate + ")" );
		
		String realPath = request.getSession().getServletContext().getRealPath("/");

		CalendarCrudBase calEvents = CalendarUtilsSingleton.getInstance(calDate);
		List<CalendarEvents> ce = calEvents.getCalendarEvents(realPath, calDate);
		JSONObject jsonObject = new JSONObject();

		try
		{
			String json = new Gson().toJson(ce);
			jsonObject.put("calendarEvents", json);
		}
		catch (Exception pe)
		{
			pe.printStackTrace();
		}

		WebStatisticData webStats = WebStatisticsUtils.setBasicWebStats(request);
		webStats.setType(WebStatisticData.CALENDAR);
		webStats.setText("Month: "+calDate);
		WebStatisticsUtils.saveMetadataStatistics(webStats);
		PrintWriter writer = response.getWriter();
		writer.println(jsonObject);

	}

	@RequestMapping(value = "/saveEvents.htm", method = RequestMethod.GET)
	protected void saveEvents(
			@RequestParam  String what,
			@RequestParam  String startDateObj,
			@RequestParam  String startHour,
			@RequestParam  String startMinute,
			@RequestParam  String startAmPm,
			@RequestParam  String endDateObj,
			@RequestParam  String endHour,
			@RequestParam  String endMinute,
			@RequestParam  String endAmPm,
			@RequestParam  String bgColor,
			@RequestParam  String fgColor,
			@RequestParam  String action,
			@RequestParam  String origEvent,
			@RequestParam  String origStartDate,
			@RequestParam  String origEndDate,
			HttpServletRequest request, 
			HttpServletResponse response)
	throws ServletException, IOException
	{

		WebStatisticsUtils.logSessionMsg(request.getSession(), "!!!!!! saveEvents (" + what + ": " + action + ")" );

		CalendarEvents cejson = null;
		CalendarEventsJSONAdapter origCe = null;
		
		CalendarEvents ce =  new CalendarEvents();
		ce.setAction(action);
		ce.setTitle(what);
		ce.setStartDate(startDateObj);
		ce.setStartHrs(startHour);
		ce.setStartMins(startMinute);
		ce.setStartAmPm(startAmPm);
		ce.setEndDate(endDateObj);
		ce.setEndHrs(endHour);
		ce.setEndMins(endMinute);
		ce.setEndAmPm(endAmPm);
		ce.getDisplayProp().setForegroundColor(fgColor);
		ce.getDisplayProp().setBackgroundColor(bgColor);
		
		// verify dates
		StringTokenizer stok = new StringTokenizer(startDateObj, "-");
		int yr = Integer.parseInt(stok.nextToken());
		int mon = Integer.parseInt(stok.nextToken());
		int day = Integer.parseInt(stok.nextToken());
		Calendar startDate = new GregorianCalendar(yr, mon, day, getIntHour(startHour,startAmPm), Integer.parseInt(startMinute));
		
		stok = new StringTokenizer(endDateObj, "-");
		yr = Integer.parseInt(stok.nextToken());
		mon = Integer.parseInt(stok.nextToken());
		day = Integer.parseInt(stok.nextToken());
		Calendar endDate = new GregorianCalendar(yr, mon, day, getIntHour(endHour,endAmPm), Integer.parseInt(endMinute));

		if (endDate.before(startDate))
			return;

		WebStatisticData webStats = WebStatisticsUtils.setBasicWebStats(request);
		webStats.setType(WebStatisticData.CALENDAR);
		if (AbstractCalendarEvents.NEW.equals(action))
		{
			webStats.setText("New");
		}
		else
		{
			// if not a new event then setup original event
			cejson = new Gson().fromJson(origEvent, CalendarEvents.class);
			origCe = new CalendarEventsJSONAdapter(cejson, origStartDate, origEndDate);
			if (AbstractCalendarEvents.DELETE.equals(action))
			{
				webStats.setText("Delete");
			}
			else
			{
				webStats.setText("Save");
			}
		}
		webStats.setOldXml(what + ": " + startDateObj + " " + startHour + ":" + startMinute + startAmPm);
		webStats.setNewXml(what + ": " + endDateObj + " " + endHour + ":" + endMinute + endAmPm);
		WebStatisticsUtils.saveMetadataStatistics(webStats);
		
		stok = new StringTokenizer(ce.getStartDate(),"-");
		String calYear = stok.nextToken();
		String calMonth = stok.nextToken();
		String calDay = stok.nextToken();
		String realPath = request.getSession().getServletContext().getRealPath("/");
		CalendarCrudBase calUtils = CalendarUtilsSingleton.getInstance(calYear);
		
		String filename = realPath+CalendarUtilsSingleton.EVENTS_PATH+calYear+"-"+calMonth+".xml";
		calUtils.saveEventData(filename, ce, AbstractCalendarEvents.NEW.equals(action)?null:origCe);
		
		Calendar cal = new GregorianCalendar(Integer.parseInt(calYear), Integer.parseInt(calMonth), 
				Integer.parseInt(calDay));
		if (cal.get(Calendar.DAY_OF_MONTH) < 7 
				&& !(cal.get(Calendar.DAY_OF_MONTH) == 1 
						&& cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY))
		{
			cal.add(Calendar.DAY_OF_MONTH, -7);
			filename = realPath+CalendarUtilsSingleton.EVENTS_PATH+cal.get(Calendar.YEAR)+"-"+cal.get(Calendar.MONTH)+".xml";
			calUtils.saveEventData(filename, ce, AbstractCalendarEvents.NEW.equals(action)?null:origCe);
		}
		else if ((cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH) - cal.get(Calendar.DAY_OF_MONTH)) < 7)
		{
			cal.add(Calendar.DAY_OF_MONTH, 7);
			filename = realPath+CalendarUtilsSingleton.EVENTS_PATH+cal.get(Calendar.YEAR)+"-"+cal.get(Calendar.MONTH)+".xml";
			calUtils.saveEventData(filename, ce, AbstractCalendarEvents.NEW.equals(action)?null:origCe);
		}

	}	
	
	private int getIntHour(String hr, String amPm)
	{
		int intHr = Integer.parseInt(hr);
		if ("PM".equals(amPm))
		{
			if (intHr!=12)
				intHr = intHr+12;
		}
		else
		{
			if (intHr==12)
				intHr = intHr-12;
		}
		return intHr;
	}

}
