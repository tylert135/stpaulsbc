
package com.tylert.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.tylert.domain.Gallery;
import com.tylert.domain.ViewItem;
import com.tylert.domain.calendar.WebStatisticData;
import com.tylert.security.UserUtils;
import com.tylert.singletons.galleriaMetadata.GalleriaMetadataUtilsSingleton;
import com.tylert.singletons.viewMetadata.ViewMetadataEventsUtils;
import com.tylert.singletons.viewMetadata.ViewMetadataUtilsSingleton;
import com.tylert.stats.WebStatisticsUtils;
import com.tylert.view.service.utils.CommonUtils;
import com.tylert.view.service.utils.Mail;
import com.tylert.view.service.utils.ViewUtils;

@Controller
public class LandingPageController
{

	
	/**
	 * This is the starting point, when a user enters the web site this method set user request object and 
	 * web visits email.
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = {"", "/", "/landingPage.htm"}, method = RequestMethod.GET)
	public String showLandingPage(ModelMap model, HttpServletRequest request)
	{
		ViewItem viewItem = new ViewItem();
		model.addAttribute(viewItem);
		String visitor = request.getParameter("visitor");
		String ip = request.getRemoteAddr();
		if ((visitor == null || !visitor.equals("0"))
				&& CommonUtils.isGoodIp(ip)) // make sure it's not using localhost, skip email and stats
		{
			try
			{
				WebStatisticData webStats = WebStatisticsUtils.setBasicWebStats(request);
				webStats.setType(WebStatisticData.VISIT);
				WebStatisticsUtils.saveMetadataStatistics(webStats);
				Mail.send("stpaulsbcweb@gmail.com", "Web Visit", String.format("IP: %1$s\nSession: %2$s", 
						request.getRemoteAddr(), request.getSession().getId()) );
			}
			catch (Exception e)
			{
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		}
		ViewUtils.addLandingPageAttributes(model);
		return "landingPage";
	}

	/**
	 * When the login menu item is clicked.
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/login.htm", method = RequestMethod.GET)
	public String loginPage(ModelMap model)
	{
		ViewItem viewItem = new ViewItem();
		model.addAttribute(viewItem);
		return "login";
	}

	/**
	 * 
	 * Retrieves menu items for the landing page. This is accomplished by
	 * loading the view metadata xml file.
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/landingMenuSetup", method = RequestMethod.GET)
	public void getLandingMenuItems(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		String realPath = request.getSession().getServletContext().getRealPath("/");
		
		boolean photoGallery = "NONE".equals(CommonUtils.getProps(realPath+CommonUtils.APP_PROPERTIES).getProperty("photo.gallery"))?false:true;
		photoGallery = false; // Remove photo gallery
		List<Gallery> gList = new ArrayList<Gallery>();
		if (photoGallery)
		{
			GalleriaMetadataUtilsSingleton galleriaInstance = GalleriaMetadataUtilsSingleton.getInstance();
			gList = galleriaInstance.getGalleriaList(realPath);		
		}
		
		ViewMetadataEventsUtils viewMetaDataUtils = new ViewMetadataEventsUtils();
		List<ViewItem> viewItemList = viewMetaDataUtils.getRequestedViewType(realPath, "custom");
		
		JSONObject jsonObject = null;
		try
		{
			jsonObject = new JSONObject();
			List<String> roles = UserUtils.getRoles();

			
			
			String json = null;
			List<String> galleryStrList = new ArrayList<String>();
			if (photoGallery)
			{
				galleryStrList = ViewUtils.generateNewLandingPageGalleryLI(gList);
			}
			json = new Gson().toJson(galleryStrList);

			List<String> strList = ViewUtils.generateNewLandingPageViewLI(viewItemList, roles);
			String json2 = new Gson().toJson(strList);
			
			jsonObject.put("viewList", json2);
			jsonObject.put("galleriaLi", json);
			jsonObject.put("roles", (new Gson()).toJson(roles));
			
		} catch (Exception pe)
		{
			pe.printStackTrace();
		}

		PrintWriter writer = response.getWriter();
		writer.println(jsonObject);
	}

	/**
	 * Get the default sections of the landing page.
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/getDefaultSections.htm", method = RequestMethod.GET)
	public void returnDefaultSections(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		String realPath = request.getSession().getServletContext().getRealPath("/");

		ViewMetadataUtilsSingleton viewInstance = ViewMetadataUtilsSingleton.getInstance();
		List<ViewItem> viList = viewInstance.getRequestedViewType(realPath, "default");

		JSONObject jsonObject = null;
		try
		{
			jsonObject = new JSONObject();
			String json = new Gson().toJson(viList);
			jsonObject.put("miList", json);
		} catch (Exception pe)
		{
			pe.printStackTrace();
		}

		PrintWriter writer = response.getWriter();
		writer.println(jsonObject);

	}

}
