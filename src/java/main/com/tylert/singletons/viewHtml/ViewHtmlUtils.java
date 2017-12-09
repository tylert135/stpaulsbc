package com.tylert.singletons.viewHtml;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import com.tylert.domain.ViewItem;
import com.tylert.domain.calendar.WebStatisticData;
import com.tylert.stats.WebStatisticsUtils;
import com.tylert.view.service.utils.Mail;

public class ViewHtmlUtils implements IViewHtmlCrudBase
{
	private String name;

	public void saveHtmlFile(String realPath, ViewItem viewItem,
			HttpServletRequest request) throws IOException
	{
		saveHtmlFile(realPath,
				viewItem.getPath() + "/" + viewItem.getId() + ".html",
				viewItem.getHtml(), request);
	}

	public void saveHtmlFile(String realPath, String filename, String html,
			HttpServletRequest request) throws IOException
	{
		String origHtml = read(realPath + filename); // save original HTML 
		// makes sure file changed
		if (!StringUtils.trimAllWhitespace(origHtml).equalsIgnoreCase(
				StringUtils.trimAllWhitespace(html)))
		{
			logChange(realPath, filename, html, origHtml, request);
			write(realPath + filename, html);
		}
	}

	private void write(String filename, String html) throws IOException
	{

		FileWriter fileWriter = null;
		PrintWriter printWriter = null;
		try
		{
			// output new (html) section
			fileWriter = new FileWriter(filename);
			printWriter = new PrintWriter(fileWriter);
			printWriter.println(html);
			printWriter.close();
		}
		catch (IOException e)
		{
			System.out.println(e.toString());
			throw new IOException(e);
		}
	}

	public String readHtmlFile(String realPath, ViewItem viewItem)
			throws IOException
	{
		String filename = realPath + viewItem.getPath() + "/"
				+ viewItem.getId() + ".html";
		return read(filename);
	}

	public String readHtmlFile(String filename) throws IOException
	{
		return read(filename);
	}

	/**
	 * This method will read a HTML section and return its contents in a String.
	 * 
	 * @param filename - filename and path
	 * @return
	 * @throws IOException
	 */
	public static String read(String filename) throws IOException
	{
		InputStream in = null;
		String htmlText = "";
		try
		{

			try
			{
				// if a new html section then no input file found
				in = new BufferedInputStream(new FileInputStream(filename));
			}
			catch (FileNotFoundException e)
			{
				return "";
			}
			int ch;
			while ((ch = in.read()) != -1)
			{
				htmlText += (char) ch;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.toString());
		}
		finally
		{
			if (in != null)
				in.close(); // very important
		}
		return htmlText;
	}

	private void logChange(String realPath, String filename, String html,
			String oldHtml, HttpServletRequest request) throws IOException
	{
		try
		{
			if (!request.getUserPrincipal().getName().equals("tim"))
			{
				Mail.send("stpaulsbc.log@gmail.com", "Web Update",
						"*** New: ***\n" + html);
			}
			WebStatisticData webStats = WebStatisticsUtils
					.setBasicWebStats(request);
			webStats.setType(WebStatisticData.SECTION_CHANGE);
			webStats.setOldXml(oldHtml);
			webStats.setNewXml(html);
			WebStatisticsUtils.saveMetadataStatistics(webStats);

			// FileWriter out = new
			// FileWriter(realPath+"sections/changeLog.html", true);
			// BufferedWriter writer = new BufferedWriter(out);
			// writer.write(String.format(
			// "<TABLE>" +
			// "<TR>" +
			// "<TD>" +
			// "<BR/>%3$s" +
			// "<H3>Changed: &nbsp;&nbsp;%1$tB %1$te, %1$tY %1$tl:%1$tM%1$tp</H3>"
			// +
			// "File: %4$s<BR/>" +
			// "IP Address: %2$s<BR/>" +
			// "</TD>" +
			// "</TR>" +
			// "<TR>" +
			// "<TD>" +
			// "<H3>*** Old: ***</H3><BR/>" +
			// "%5$s<BR/>" +
			// "</TD>" +
			// "</TR>" +
			// "<TR>" +
			// "<TD>" +
			// "<H3>*** New: ***</H3><BR/>" +
			// "%6$s<BR/>" +
			// "%3$s<BR/>" +
			// "</TD>" +
			// "</TR>" +
			// "</TABLE>",
			// cal, request.getRemoteAddr(), seperator,
			// filename, oldHtml, html));
			// writer.close();
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
			throw new IOException(e);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}

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
