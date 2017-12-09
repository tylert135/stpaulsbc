package com.tylert.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.tylert.domain.Gallery;
import com.tylert.domain.calendar.WebStatisticData;
import com.tylert.singletons.galleriaMetadata.GalleriaMetadataUtilsSingleton;
import com.tylert.stats.WebStatisticsUtils;
import com.tylert.view.service.utils.GalleriaUtils;
import com.tylert.view.service.utils.ViewUtils;

@Controller
public class MenuController {

	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logout(ModelMap model)
		throws ServletException, IOException
	{
		ViewUtils.addLandingPageAttributes(model);
		return "landingPage";
	}

	@RequestMapping(value="/maxLogin", method = RequestMethod.GET)
	public String maxLogin(ModelMap model)
		throws ServletException, IOException
	{
		ViewUtils.addLandingPageAttributes(model);
		return "maxLogin";
	}

	/**
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/photoMenuSetup", method = RequestMethod.GET)
	public void getPhotoMenuItems(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		String path = request.getSession().getServletContext().getRealPath("/");
		
		GalleriaMetadataUtilsSingleton galleriaInstance = GalleriaMetadataUtilsSingleton.getInstance();
		List<Gallery> gList = galleriaInstance.getGalleriaList(path);

		JSONObject jsonObject = null;
		try
		{
			jsonObject = new JSONObject();
			
			String json = new Gson().toJson(GalleriaUtils.generateGalleryLI(gList));
			jsonObject.put("galleriaLi", json);
		}
		catch (Exception pe)
		{
			pe.printStackTrace();
		}

		PrintWriter writer = response.getWriter();
		writer.println(jsonObject);

	}

	@RequestMapping(value = "/photoGallery", method = RequestMethod.GET)
	public ModelAndView gallery(@RequestParam String galleryId, HttpServletRequest request, ModelMap model)
	throws ServletException, IOException
	{
		ModelAndView mav = new ModelAndView();
		String path = request.getSession().getServletContext().getRealPath("/");
	    
	    String galleryCall="/WEB-INF/jsp/photoGallery/gallery"+galleryId+".jsp";
	    WebStatisticsUtils.logSessionMsg(request.getSession(), "View Gallery (" + galleryCall + ")" );
	    
	    request.setAttribute("galleryName", GalleriaUtils.getGalleria(path, Integer.parseInt(galleryId)).getGalleryName());
	    request.setAttribute("galleryCall", galleryCall);
    
	    ViewUtils.addLandingPageAttributes(model);
		mav.setViewName("photoGallery");
		
		WebStatisticData webStats = WebStatisticsUtils.setBasicWebStats(request);
		webStats.setType(WebStatisticData.PHOTO);
		webStats.setText("Gallery ID: "+galleryId);
		WebStatisticsUtils.saveMetadataStatistics(webStats);
	    return mav;
	}

	@RequestMapping(value = "/calendarAdmin", method = RequestMethod.GET)
	public ModelAndView calendarAdmin(HttpServletRequest request, ModelMap model)
	throws ServletException, IOException
	{
		ModelAndView mav = new ModelAndView();    
		mav.setViewName("calendarAdmin");
		WebStatisticData webStats = WebStatisticsUtils.setBasicWebStats(request);
		webStats.setType(WebStatisticData.CALENDAR);
		WebStatisticsUtils.saveMetadataStatistics(webStats);
		ViewUtils.addLandingPageAttributes(model);
	    return mav;
	}

	@RequestMapping(value = "/callGallery", method = RequestMethod.GET)
	public ModelAndView callGallery(@RequestParam String galleryId, HttpServletRequest request, ModelMap model)
	throws ServletException, IOException
	{
	    ModelAndView mav = new ModelAndView();
	    mav.setViewName("photoGallery/photoGallery"+galleryId);
	    ViewUtils.addLandingPageAttributes(model);
	    return mav;
	}


}
