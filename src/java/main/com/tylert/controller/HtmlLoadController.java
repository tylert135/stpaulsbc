package com.tylert.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.tylert.domain.ViewItem;
import com.tylert.domain.WebLog;
import com.tylert.domain.calendar.WebStatisticData;
import com.tylert.singletons.viewHtml.IViewHtmlCrudBase;
import com.tylert.singletons.viewHtml.ViewHtmlSingleton;
import com.tylert.stats.WebStatisticsUtils;
import com.tylert.view.service.utils.ViewUtils;

/**
 * Class that loads HTML code for insert into landing page.
 * 
 * @author Tim
 *
 */
@Controller
public class HtmlLoadController {
	
	/**
	 * Load a section of HTML code and pass it back to tylerMenu.js.html
	 * 
	 * @param id
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/getHtml.htm", method = RequestMethod.GET)
	protected void getHtmlFileData(@RequestParam String id, 
			HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{

		String realPath = request.getSession().getServletContext().getRealPath("/");
		WebStatisticsUtils.logSessionMsg(request.getSession(), "Get HTML File (" + id + ")" );
//		if (id.equals("changeLog"))
//			WebStatisticsUtils.createChangeLogSection(realPath);
			

		ViewItem vi = ViewUtils.getViewItemById(realPath, id);
		IViewHtmlCrudBase instance = ViewHtmlSingleton.getInstance(id);
		String filename = null;
		if (vi != null)
		{
			filename = realPath + "/" + vi.getPath() + "/" + id + ".html";
			vi.setHtml(instance.readHtmlFile(filename));
		}
		else
		{
			vi = new ViewItem();
			vi.setType("custom");
			vi.setAction(ViewItem.NEW);
			vi.setName("New");
			vi.setId(id);
			vi.setPath(ViewItem.PATH);
			filename = realPath + "/" + vi.getPath() + "/sectionTemplate.html";
			vi.setHtml(instance.readHtmlFile(filename));
		}
		JSONObject jsonObject = null;

		try
		{
			jsonObject = new JSONObject();
			String json = new Gson().toJson(vi);
			jsonObject.put("viewItem", json);
		}
		catch (Exception pe)
		{
			pe.printStackTrace();
		}
		if (!id.startsWith("box"))
		{
			WebStatisticData webStats = WebStatisticsUtils.setBasicWebStats(request);
			webStats.setType(WebStatisticData.NAVIGATE);
			webStats.setText(filename);
			WebStatisticsUtils.saveMetadataStatistics(webStats);
		}
		PrintWriter writer = response.getWriter();
		writer.println(jsonObject);

	}

	/**
	 * @param id
	 * @param logCtr
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/getChangeLog.htm", method = RequestMethod.GET)
	protected void getChangeLogData(@RequestParam String id, @RequestParam String logCtr, 
			HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{

		int logCounter = Integer.parseInt(logCtr);
		WebLog wl =  null;
		if (logCounter == 0)
			wl =  WebStatisticsUtils.getInitialLogData(request.getSession().getServletContext().getRealPath("/"));

		WebLog sendWL = new WebLog();
		sendWL.setHeaderRow(wl.getHeaderRow());
		sendWL.setFooterRow(wl.getFooterRow());
		sendWL.setRowCtr(logCounter+25);
		sendWL.setTimeStamp(wl.getTimeStamp());
		sendWL.setVisitCount(wl.getVisitCount());
		sendWL.setMore(wl.isMore());
		for (int i=logCounter; i <=logCounter+25; i++)
		{
			sendWL.getTableRow().put(new Integer(i), wl.getTableRow().get(new Integer(i)));
		}
		
		JSONObject jsonObject = null;

		try
		{
			jsonObject = new JSONObject();
			String json2 = new Gson().toJson(sendWL);
			jsonObject.put("webLog", json2);
		}
		catch (Exception pe)
		{
			pe.printStackTrace();
		}
		PrintWriter writer = response.getWriter();
		writer.println(jsonObject);
	}
	
	/**
	 * @param logCtr
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/getMoreChangeLog.htm", method = RequestMethod.GET)
	protected void getMoreChangeLogData(@RequestParam String logCtr,
			HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{

		int logCounter = Integer.parseInt(logCtr);
		WebLog wl =  WebStatisticsUtils.getMoreLogData(request.getSession().getServletContext().getRealPath("/"), 
				logCounter);

		JSONObject jsonObject = null;

		try
		{
			jsonObject = new JSONObject();
			String json2 = new Gson().toJson(wl);
			String json = new Gson().toJson(logCounter);
			jsonObject.put("oldLogCtr", json);
			jsonObject.put("webLog", json2);
		}
		catch (Exception pe)
		{
			pe.printStackTrace();
		}
		PrintWriter writer = response.getWriter();
		writer.println(jsonObject);
	}
}
