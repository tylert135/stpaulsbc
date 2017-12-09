package com.tylert.context;

import org.springframework.context.ApplicationContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContextAware;

public class AppContext implements ApplicationContextAware
{
	private static ApplicationContext applicationContext;

	/**
	 * Loaded during Spring initialization.
	 */
	@Override
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException
	{
		applicationContext = ctx;
	}

	/**
	 * Get access to the Spring ApplicationContext from anywhere in application.
	 * 
	 * @return Spring ApplicationContext
	 */
	public static ApplicationContext getApplicationContext()
	{
		return applicationContext;
	}
}