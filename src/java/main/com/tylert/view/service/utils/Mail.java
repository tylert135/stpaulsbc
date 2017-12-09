package com.tylert.view.service.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.springframework.context.ApplicationContext;

import com.tylert.context.AppContext;
import com.tylert.mail.MailService;

public class Mail
{
	public static void send(String sendTo, String subject, String message) throws Exception
	{
		try
		{
			Calendar cal = new GregorianCalendar();
			ApplicationContext ctx = AppContext.getApplicationContext();
			MailService mailService = (MailService)ctx.getBean("mailService");
			mailService.sendMail("stpaulsbc@gmail.com", sendTo, subject,
					String.format("Date: %1$tB %1$te, %1$tY %1$tl:%1$tM:%1$tS%1$tp \n%2$s", cal, message));
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
			throw new Exception(e);
		}
		
	}
	
}
