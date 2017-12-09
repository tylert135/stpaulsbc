package com.tylert.view.service.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.ui.ModelMap;

import com.tylert.domain.Gallery;
import com.tylert.domain.ViewItem;
import com.tylert.domain.UserReportRequest;
import com.tylert.security.UserUtils;
import com.tylert.singletons.viewMetadata.ViewMetadataUtilsSingleton;

public class ViewUtils
{

	public static ViewItem getViewItemById(String realPath, String Id)
		throws IOException
	{
		List<ViewItem> miList = getAllViewItems(realPath);
		for (ViewItem mi : miList)
		{
			if (mi.getSubView() == null || mi.getSubView().size() == 0)
			{
				if (mi.getId().equals(Id))
					return mi;
			}
			else
			{
				for (ViewItem suMi : mi.getSubView())
				{
					if (suMi.getId().equals(Id))
						return suMi;
				}
			}
		}
		
		return null;
	}


	public static List<ViewItem> getAllViewItems(String path)
		throws FileNotFoundException, IOException
	{
		ViewMetadataUtilsSingleton viewMetaDataUtils = ViewMetadataUtilsSingleton.getInstance();;
		return viewMetaDataUtils.getRequestedViewType(path, null);
	}

	public static List<String> generateNewLandingPageGalleryLI(List<Gallery> gList)
	{
		List<String> liList = new ArrayList<String>();
		String li;
		String liSub = "<li><ul class=\"navsub\">";
		for (Gallery g : gList)
		{
			li = (new StringBuilder()).append("<li class=\"navdd\"><span onclick='javascript: window.location=\"photoGallery.htm?galleryId=")
			.append(g.getId())
			.append("\";'>")
			.append(g.getGalleryName())
			.append("</span></li>")
			.toString();
			liSub += li;
		}				
		li = "</ul><span class=\"navspan\">Photo Gallery</span></li>";
		liSub += li;
		liList.add(liSub);
		return liList;
	}
	public static List<String> generateNewLandingPageViewLI(List<ViewItem> miList, List<String> roles)
	{
		List<String> liList = new ArrayList<String>();
		String li;
		for (ViewItem vi : miList)
		{
			if (!UserUtils.isUserAuthorizedForView(vi, roles))
				continue;
				
			if (ViewItem.CHANGE_LOG.equals(vi.getId()))
			{
				li = (new StringBuilder()).append("<li class=\"navli\"><span id=\"")
				.append(vi.getId())
				.append("\" onclick=\"getChangeLog('")
				.append(vi.getId())
				.append("', this,0)\">")
				.append(vi.getName())
				.append("</span></li>")
				.toString();
				liList.add(li);
			}
			else if (vi.getSubView()==null || vi.getSubView().size() == 0)
			{
				li = (new StringBuilder()).append("<li class=\"navli\"><span id=\"")
				.append(vi.getId())
				.append("\" onclick=\"menuClicked('")
				.append(vi.getId())
				.append("', this)\">")
				.append(vi.getName())
				.append("</span></li>")
				.toString();
				liList.add(li);
			}
			else
			{
				String liSub = "<li><ul class=\"navsub\">";
				for (ViewItem subVi : vi.getSubView())
				{
					if (!UserUtils.isUserAuthorizedForView(subVi, roles))
						continue;
						
					li = (new StringBuilder()).append("<li class=\"navdd\"><span onclick=\"menuClicked('")
					.append(subVi.getId())
					.append("', this)\">")
					.append(subVi.getName())
					.append("</span></li>")
					.toString();
					liSub += li;
				}				
				li = (new StringBuilder()).append("</ul><span class=\"navspan\">")
				.append(vi.getName())
				.append("</span></li>")
				.toString();
				liSub += li;
				liList.add(liSub);
			}
			
		}

		return liList;
	}

	public static String generateNewAdminMenuLI(ViewItem vi)
	{
		String li = (new StringBuilder()).append(
				"<li><a href=\"#\" onclick=\"createRTE('").append(vi.getId())
				.append("', '").append(vi.getName()).append(" Section','")
				.append(vi.getType()).append("');\">").append(vi.getName())
				.append("</a></li>").toString();
		return li;
	}
	public static void addLandingPageAttributes	(ModelMap model)
	{
		UserReportRequest userReportRequest = new UserReportRequest();
		userReportRequest.setFname("");
		userReportRequest.setReturnURL("");
		model.addAttribute(userReportRequest);
	}


}
