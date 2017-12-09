package com.tylert.stats;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;

import com.tylert.xmlBeans.webStatistics.StatDataDocument.StatData;
import com.tylert.xmlBeans.webStatistics.TypeDocument.Type;
import com.tylert.xmlBeans.webStatistics.WebStatisticsDocument;
import com.tylert.xmlBeans.webStatistics.WebStatisticsDocument.WebStatistics;

import com.tylert.domain.WebLog;
import com.tylert.domain.calendar.WebStatisticData;
import com.tylert.security.UserUtils;
import com.tylert.view.service.utils.CommonUtils;

public class WebStatisticsUtils
{

	public static String WEB_STATISTICS_PATH = "/metaData/webStatistics/";
	public static boolean SHOW_NAVIGATE = true;
	public static WebLog webLog;

	public static void logSessionMsg(HttpSession session, String serviceName)

	{
		out("Session: " + session.getId() + "\n  * User: "
				+ UserUtils.getUsername() + "\n  * " + serviceName);
	}

	public static void logMsg(String str)
	{
		out(str);
	}

	private static void out(String str)
	{
		System.out.print("************************************** ");
		System.out.println(str);
	}

	public static void saveMetadataStatistics(WebStatisticData webStats)
	{

		if (!CommonUtils.isGoodIp(webStats.getIpAddress()))
			return;

		Calendar cal = new GregorianCalendar();
		String filename = webStats.getRealPath()
				+ WebStatisticsUtils.WEB_STATISTICS_PATH
				+ String.format("%1$tY", cal) + "-" + String.format("%1$tm", cal)
				+ "-stats.xml";

		File f = new File(filename);

		try
		{
			WebStatisticsDocument webStatisticsDoc = null;
			WebStatistics webStatistics = null;
			StatData statData = null;
			int visitCount = 0;
			if (f.exists())
			{
				webStatisticsDoc = WebStatisticsDocument.Factory.parse(f);
				webStatistics = webStatisticsDoc.getWebStatistics();
				statData = webStatistics.getStatData();
				visitCount = webStatistics.getVisitCounter();
			}
			else
			{
				// get visit counter from prior month
				Calendar priorMonth = (Calendar)cal.clone();
				priorMonth.add(Calendar.MONTH, -1);
				File fPrior = new File( webStats.getRealPath()
						+ WebStatisticsUtils.WEB_STATISTICS_PATH
						+ String.format("%1$tY", priorMonth) + "-" + String.format("%1$tm", priorMonth)
						+ "-stats.xml");
				WebStatisticsDocument webStatisticsDocPrior = WebStatisticsDocument.Factory.parse(fPrior);
				WebStatistics webStatisticsPrior = webStatisticsDocPrior.getWebStatistics();
				visitCount = 0;
				
				webStatisticsDoc = WebStatisticsDocument.Factory.newInstance();
				webStatistics = webStatisticsDoc.addNewWebStatistics();
				statData = webStatistics.addNewStatData();
				webStatistics.setVisitCounter(0);
				webStatistics.setVisitCounterTotal(webStatisticsPrior.getVisitCounter()+ webStatisticsPrior.getVisitCounterTotal());
			}
			if (WebStatisticData.VISIT.equals(webStats.getType()))
			{
				visitCount++;
				webStatistics.setVisitCounter(visitCount);
			}
			Type type = statData.addNewType();
			WebStatisticsUtils.updateXmlbeansWebStatistic(type, webStats);

			XmlOptions xmlOptions = new XmlOptions();
			xmlOptions.setSavePrettyPrint();
			xmlOptions.setSavePrettyPrintIndent(4);
			webStatisticsDoc.save(f, xmlOptions);

			// webStatisticsDoc.save(f);
		}
		catch (XmlException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static WebLog getInitialLogData(String realPath)
	{
		webLog = new WebLog();
		File dir = new File(realPath + WEB_STATISTICS_PATH);
		File[] files = dir.listFiles();

		for (int j = files.length - 1; j >= 0; j--)
		{
			if (!files[j].getName().startsWith("~")
					&& files[j].getName().endsWith("-stats.xml"))
			{
				webLog.getLogFiles().add(files[j].getName());
			}
		}
		Collections.sort(webLog.getLogFiles(), Collections.reverseOrder()); // sort filenames
		WebStatistics webStats = getWebStatistics(realPath, webLog.getLogFiles().get(0));
		int totalVisits = webStats.getVisitCounterTotal()+webStats.getVisitCounter();
		String headerRow = new StringBuffer("")
				.append("<H2 style='LINE-HEIGHT: 120%'>Web Statistics</H2>")
				.append("<B>Visit Counter: ")
				.append(totalVisits)
				.append("</B><BR/><BR/><DIV  width=\"90%\" id=\"changeLogDiv\" style=\"overflow-x:auto;\"><TABLE id=\"changeLogTable\">")
				//.append("</B><BR/><BR/><TABLE id=\"changeLogTable\">")
				.append("<TR>")
				.append("<TD width='175px'>").append("<B>DATE</B></TD>")

				.append("<TD style='margin-left: 10px;'>")
				.append("<B>TIME</B>").append("</TD>")

				.append("<TD style='margin-left: 10px;'>")
				.append("<B>IP</B>").append("</TD>")

				.append("<TD style='margin-left: 10px;'>")
				.append("<B>USER</B>").append("</TD>")

				.append("<TD style='margin-left: 10px;'>")
				.append("<B>INFO</B>").append("</TD>")

				.append("<TD style='margin-left: 10px;'>")
				.append("<B>SESSION</B>").append("</TD>")

				.append("<TD style='margin-left: 10px;'>")
				.append("<B>ACTION</B>").append("</TD>")

				.append("<TD style='margin-left: 10px;'>")
				.append("<B>ROLE</B>").append("</TD>")

				.append("<TD style='margin-left: 10px;'>")
				.append("<B>WEB PAGE</B>").append("</TD>")

				.append("<TD style='margin-left: 10px;'>")
				.append("<B>OLD SECTION</B>").append("</TD>")

				.append("<TD style='margin-left: 10px;'>")
				.append("<B>NEW SECTION</B>")
				.append("</TD></TR>").toString();
		webLog.setHeaderRow(headerRow);

		
		HashMap<Integer, String> tableRow = getTableRowData(webStats, 0);
		webLog.setTableRow(tableRow);
		webLog.setVisitCount(webStats.getVisitCounterTotal()+webStats.getVisitCounter());
		webLog.setTimeStamp(new GregorianCalendar());
		webLog.setRowCtr(tableRow.size());
		webLog.getLogFiles().remove(0);
		webLog.setMore(true);
		// if we don't have at least 25 rows then get more data
		if (webLog.getTableRow().size() < 25)
		{
			while (webLog.getTableRow().size() < 25)
			{
				if (webLog.getLogFiles().size() == 0)
				{
					break;
				}
				webStats = getWebStatistics(realPath, webLog.getLogFiles().get(0));
				tableRow = getTableRowData(webStats, tableRow.size());
				webLog.getTableRow().putAll(tableRow);
				webLog.setRowCtr(tableRow.size());
				webLog.getLogFiles().remove(0);
			}
			if (webLog.getTableRow().size()<=25 && webLog.getLogFiles().size() == 0)
				webLog.setMore(false);
		}
		webLog.setFooterRow("</TABLE><CENTER><BUTTON id='moreLogData' onclick=\"getMoreChangeLog(logCtr)\">More</BUTTON></CENTER></DIV>");
		//webLog.setFooterRow("</TABLE><CENTER><BUTTON id='moreLogData' onclick=\"getMoreChangeLog(logCtr)\">More</BUTTON></CENTER>");

		return webLog;
	}
	
	public static WebLog getMoreLogData(String realPath, int logCtr)
	{
		if (webLog.getTableRow().size() < logCtr+25)
		{
			while (webLog.getTableRow().size() < logCtr+25)
			{
				if (webLog.getLogFiles().size() == 0)
				{
					break;
				}
				WebStatistics webStats = getWebStatistics(realPath, webLog.getLogFiles().get(0));
				HashMap<Integer, String> tableRow = getTableRowData(webStats, webLog.getTableRow().size());
				webLog.getTableRow().putAll(tableRow);
				webLog.setRowCtr(webLog.getTableRow().size());
				webLog.getLogFiles().remove(0);
			}
		}
		WebLog wl = new WebLog();
		if (webLog.getTableRow().size()<logCtr+25 && webLog.getLogFiles().size() == 0)
			webLog.setMore(false);
		else
			webLog.setMore(true);
		int j=logCtr+1;
		for (int i=j; i <=logCtr+25 && i <= webLog.getTableRow().size(); i++)
		{
			wl.getTableRow().put(new Integer(i), webLog.getTableRow().get(new Integer(i)));
			j++;
		}
		wl.setRowCtr(j-1);
		wl.setMore(webLog.isMore());
		return wl;
	}

	private static HashMap<Integer, String> getTableRowData(WebStatistics webStats, int currentRowCtr)
	{

		HashMap<Integer, String> tableRow = new HashMap<Integer, String>();
		int newRowCtr=0;
		
		if (webStats != null)
		{
			System.out.println("Visit Count: "
					+ webStats.getVisitCounter());
			List<WebStatisticData> WebStatisticDataList = getWebStatisticDataList(webStats);
			for (WebStatisticData webStatisticData : WebStatisticDataList)
			{
				String text = null;
				int textOffset = 0;
				int pageOffset = webStatisticData.getWebpage()
				.lastIndexOf('/') + 1;
				String page = webStatisticData.getWebpage().substring(
						pageOffset);
				if (webStatisticData.getText() == null)
					text = "";
				else if (webStatisticData.getText().lastIndexOf("/sections/") == -1)
					text = webStatisticData.getText();
				else
				{
					textOffset = webStatisticData.getText().lastIndexOf("/sections/") + 10;
					text = webStatisticData.getText().substring(textOffset);
				}
				
				String ipAddress = webStatisticData.getIpAddress() == null ? "*"
						: webStatisticData.getIpAddress();
				String username = webStatisticData.getUsername() == null ? "*"
						: webStatisticData.getUsername();
				String type = webStatisticData.getType() == null ? "*"
						: webStatisticData.getType();
				String sessionId = webStatisticData.getSessionId() == null ? "*"
						: webStatisticData.getSessionId();
				String role = webStatisticData.getRole() == null ? "*"
						: webStatisticData.getRole();
				String oldXml = webStatisticData.getOldXml() == null ? "*"
						: webStatisticData.getOldXml();
				String newXml = webStatisticData.getNewXml() == null ? "*"
						: webStatisticData.getNewXml();
				String date = String.format("%1$ta. %1$tB %1$te, %1$tY",webStatisticData.getDate());
				String time = String.format("%1$tl:%1$tM%1$tp",webStatisticData.getDate());
				String s = "<TR>" + "<TD style='margin-left: 10px;'>" + date + "</TD>" +
				
				"<TD  style='margin-left: 10px;'>" + time + "</TD>" +
				
				"<TD  style='margin-left: 10px;'>" + ipAddress + "</TD>"
				+
				
				"<TD  style='margin-left: 10px;'>" + username
				+ "</TD>" +
				
				"<TD  style='margin-left: 10px;'>" + text
				+ "</TD>" +
				
				"<TD  style='margin-left: 10px;'>" + sessionId
				+ "</TD>" +
				
				"<TD  style='margin-left: 10px;'>" + type
				+ "</TD>" +
				
				"<TD  style='margin-left: 10px;'>" + role
				+ "</TD>" +
				
				"<TD  style='margin-left: 10px;'>" + page
				+ "</TD>" +
				
				"<TD  style='margin-left: 10px;'>" + oldXml
				+ "</TD>" +
				
				"<TD  style='margin-left: 10px;'>\n" + newXml
				+ "</TD>" +
				
				"<TR>\n";
				newRowCtr++;
				tableRow.put(new Integer(currentRowCtr+newRowCtr), s);
			}
		}
		return tableRow;
	}
	
	public static List<WebStatisticData> getWebStatisticDataList(WebStatistics webStatistics)
	{
		StatData statData = null;

		List<WebStatisticData> wstatList = new ArrayList<WebStatisticData>();

		if (webStatistics != null && webStatistics.getStatData() != null)
		{
			statData = webStatistics.getStatData();
			Type[] webStatArray = statData.getTypeArray();
			for (int i = webStatArray.length - 1; i >= 0; i--)
			{
				int mon, yr, day, hr, min;

				StringTokenizer stok = new StringTokenizer(
						webStatArray[i].getDate(), "-");
				yr = Integer.parseInt(stok.nextToken());
				mon = Integer.parseInt(stok.nextToken()) - 1;
				day = Integer.parseInt(stok.nextToken());

				stok = new StringTokenizer(webStatArray[i].getTime(), ":");
				hr = Integer.parseInt(stok.nextToken());
				min = Integer.parseInt(stok.nextToken());
				WebStatisticData data = new WebStatisticData();
				data.setDate(new GregorianCalendar(yr, mon, day, hr, min));
				data.setIpAddress(webStatArray[i].getIpAddress());
				data.setText(webStatArray[i].getText());
				data.setNewXml(webStatArray[i].getNewSection());
				data.setOldXml(webStatArray[i].getOldSection());
				data.setRole(webStatArray[i].getRole());
				data.setSessionId(webStatArray[i].getSessionId());
				data.setType(webStatArray[i].getName());
				data.setUsername(webStatArray[i].getUsername());
				data.setWebpage(webStatArray[i].getWebpage());
				wstatList.add(data);
			}
		}
		return wstatList;
	}

	private static WebStatistics getWebStatistics(String realPath, String filename)
	{
		File f = new File(realPath + WebStatisticsUtils.WEB_STATISTICS_PATH
				+ filename);
		WebStatisticsDocument webStatisticsDoc = null;
		WebStatistics webStatistics = null;
		try
		{
			if (f.exists())
			{
				webStatisticsDoc = WebStatisticsDocument.Factory.parse(f);
				webStatistics = webStatisticsDoc.getWebStatistics();
			}
		}
		catch (XmlException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return webStatistics;
	}

	public static void updateXmlbeansWebStatistic(Type t, WebStatisticData webStats)
	{
		t.setDate(String.format("%1$tY-%1$tm-%1$te", webStats.getDate()));
		t.setTime(String.format("%1$tk:%1$tM", webStats.getDate()));
		t.setOldSection(webStats.getOldXml());
		t.setNewSection(webStats.getNewXml());
		t.setIpAddress(webStats.getIpAddress());
		t.setText(webStats.getText());
		t.setName(webStats.getType());
		t.setRole(webStats.getRole());
		t.setSessionId(webStats.getSessionId());
		t.setUsername(webStats.getUsername());
		t.setWebpage(webStats.getWebpage());
	}

	public static WebStatisticData setBasicWebStats(HttpServletRequest request)
	{
		WebStatisticData webStats = new WebStatisticData();
		webStats.setDate(new GregorianCalendar());
		webStats.setIpAddress(request.getRemoteAddr());
		webStats.setSessionId(request.getSession().getId());
		webStats.setRole(UserUtils.getRoles().get(0));
		webStats.setUsername(UserUtils.getUsername());
		webStats.setRealPath(request.getSession().getServletContext()
				.getRealPath("/"));
		webStats.setWebpage(request.getRequestURL().toString());
		return webStats;
	}

}
