package com.tylert.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.Gson;
import com.tylert.domain.ViewItem;
import com.tylert.security.UserUtils;
import com.tylert.service.IViewItemService;
import com.tylert.singletons.viewMetadata.ViewMetadataUtilsSingleton;
import com.tylert.stats.WebStatisticsUtils;
import com.tylert.view.service.utils.ViewUtils;

@Controller
@SessionAttributes("viewItem")
public class AdminController
{

	@RequestMapping(value="/viewSuccess", method = RequestMethod.GET)
	public String showUserForm(@ModelAttribute("viewItem") ViewItem viewItem, 
			ModelMap model, HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		model.addAttribute(viewItem);
		return "viewSuccess";
	}

	@RequestMapping(value="/showMaintenancePage.htm", method = RequestMethod.GET)
	public String redirect(ModelMap model)
	{
        WebStatisticsUtils.logMsg((new StringBuilder()).append("User: ").
        		append(UserUtils.getUsername()).
        		append(" Role: ").append(UserUtils.getRoles()).
        		toString());
		ViewItem viewItem = new ViewItem();
		model.addAttribute(viewItem);
		ViewUtils.addLandingPageAttributes(model);
		return "adminMaintenance";
    }
	
	/**
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/getAdminMenuItems", method = RequestMethod.GET)
	public void getViewItems(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		String realPath = request.getSession().getServletContext().getRealPath("/");

		ViewMetadataUtilsSingleton viewMetaDataUtils = ViewMetadataUtilsSingleton.getInstance();
		List<ViewItem> viewList = viewMetaDataUtils.getRequestedViewType(realPath, "custom");
		List<String> newCustomList = adminViewList(viewList);
		
		viewList = viewMetaDataUtils.getRequestedViewType(realPath, "default");
		List<String> newDefaultList = adminViewList(viewList);
		
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("newCustomList", (new Gson()).toJson(newCustomList));
            jsonObject.put("newDefaultList", (new Gson()).toJson(newDefaultList));
            jsonObject.put("viewItem", (new Gson()).toJson(viewList.get(0)));
            jsonObject.put("roles", (new Gson()).toJson(UserUtils.getRoles()));
        }
        catch(Exception pe)
        {
            pe.printStackTrace();
        }
        WebStatisticsUtils.logSessionMsg(request.getSession(), "MaintenanceLoginSuccess:getMenuItems");
        PrintWriter writer = response.getWriter();
        response.setContentType("text/html");
        writer.println(jsonObject);
	}

	/** 
	 * Generates menu list for administrator screen
	 * 
	 * @param viList
	 * @return
	 */
	private List<String> adminViewList(List<ViewItem> viList)
	{
		List<String> liList = new ArrayList<String>();

		for (ViewItem mi : viList)
		{
			if (mi.getSubView() == null || mi.getSubView().size() == 0)
			{
				if (UserUtils.isUserAuthorizedForView(mi, UserUtils.getRoles()))
				{
					liList.add(ViewUtils.generateNewAdminMenuLI(mi));
				}
			}
			else
			{
				for (ViewItem subMi : mi.getSubView())
				{
					if (UserUtils.isUserAuthorizedForView(subMi, UserUtils.getRoles()))
					{
						liList.add(ViewUtils.generateNewAdminMenuLI(subMi));
					}
				}
			}
		}
		return liList;

	}
	
	private IViewItemService viewItemService;

	@Autowired
	public void setViewItemService(IViewItemService viewItemService) {
		this.viewItemService = viewItemService;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(@ModelAttribute("viewItem") ViewItem viewItem, ModelMap model, 
			HttpServletRequest request)
	{
		String realPath = request.getSession().getServletContext().getRealPath("/");
		try
		{
			if (ViewItem.DELETE.equals(viewItem.getAction()))
				viewItemService.delete(realPath, viewItem, request);
			else
				viewItemService.save(realPath, viewItem, request);
		} catch (ServletException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute(viewItem);
		WebStatisticsUtils.logSessionMsg(request.getSession(), "Save View Data (" + viewItem.getId() + ")");
		return "redirect:viewSuccess.htm";
	}
	
	
}
