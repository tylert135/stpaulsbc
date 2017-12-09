package com.tylert.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tylert.domain.UserReportRequest;
import com.tylert.domain.calendar.WebStatisticData;
import com.tylert.stats.WebStatisticsUtils;
import com.tylert.view.service.utils.Mail;

@Controller
//@SessionAttributes("userReportRequest")
@RequestMapping(value = "/userReportRequest.htm")
public class UserReportController
{
	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(@ModelAttribute("userReportRequest") UserReportRequest userReportRequest, ModelMap model, 
			HttpServletRequest request)
	{
		try
		{
			String message = String.format("Name: %1$s %2$s\nEmail: %3$s\n\nRequest/Comment:\n%4$s", 
					userReportRequest.getFname(), userReportRequest.getLname(),
					userReportRequest.getEmail(), userReportRequest.getComment());
			WebStatisticData webStats = WebStatisticsUtils.setBasicWebStats(request);
			webStats.setType(WebStatisticData.REQUEST);
			webStats.setText(message);
			WebStatisticsUtils.saveMetadataStatistics(webStats);
			Mail.send("stpaulsbcweb@gmail.com", "User Request",message);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return "redirect:"+userReportRequest.getReturnURL();
	}
	
	
}
