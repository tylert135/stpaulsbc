package com.tylert.service.impl;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.tylert.domain.ViewItem;
import com.tylert.service.IViewItemService;
import com.tylert.singletons.viewHtml.IViewHtmlCrudBase;
import com.tylert.singletons.viewHtml.ViewHtmlSingleton;
import com.tylert.singletons.viewMetadata.ViewMetadataUtilsSingleton;

/**
 * Save the HTML section.
 * 
 * @author Tim
 *
 */
public class ViewItemServiceImpl implements IViewItemService
{
	public void save(String realPath, ViewItem viewItem,
			HttpServletRequest request) throws IOException
	{
		// this will force the SUPER user access
		boolean isSuper = false;
		if (viewItem.getRoles() != null)
		{
			for (String role : viewItem.getRoles())
			{
				if (ViewItem.SUPER_ROLE.equals(role))
				{
					isSuper = true;
					break;
				}
			}
		}
		else
			viewItem.setRoles(new ArrayList<String>());
		if (!isSuper)
			viewItem.getRoles().add(ViewItem.SUPER_ROLE);

		if (viewItem.getId().length() > 0 && viewItem.getHtml().length() > 0)
		{
			//saves HTML data
			IViewHtmlCrudBase htmlInstance = ViewHtmlSingleton.getInstance(viewItem.getId());
			htmlInstance.saveHtmlFile(realPath, viewItem, request);
		}

		//save metaData
		ViewMetadataUtilsSingleton viewMetaDataUtils = ViewMetadataUtilsSingleton.getInstance();
		viewMetaDataUtils.saveViewData(realPath, viewItem);
	}

	/* (non-Javadoc)
	 * @see com.tylert.service.IViewItemService#delete(java.lang.String, com.tylert.domain.ViewItem, javax.servlet.http.HttpServletRequest)
	 */
	public void delete(String realPath, ViewItem viewItem,
			HttpServletRequest request) throws ServletException, IOException
	{
		ViewMetadataUtilsSingleton viewMetaDataUtils = ViewMetadataUtilsSingleton.getInstance();
		viewMetaDataUtils.saveViewData(realPath, viewItem);
	}

	/**
	 * @param realPath
	 * @param viewItem
	 * @param request
	 * @throws IOException
	 */
	public void saveViewMetaData(String realPath, ViewItem viewItem,
			HttpServletRequest request) throws IOException
	{
		// this will force the SUPER user access
		boolean isSuper = false;
		if (viewItem.getRoles() != null)
		{
			for (String role : viewItem.getRoles())
			{
				if ("SUPER".equals(role))
				{
					isSuper = true;
					break;
				}
			}
		}
		else
			viewItem.setRoles(new ArrayList<String>());
		if (!isSuper)
			viewItem.getRoles().add(ViewItem.SUPER_ROLE);

		IViewHtmlCrudBase htmlInstance = ViewHtmlSingleton.getInstance(viewItem
				.getId());
		htmlInstance.saveHtmlFile(realPath, viewItem, request);

		ViewMetadataUtilsSingleton viewMetaDataUtils = ViewMetadataUtilsSingleton.getInstance();
		viewMetaDataUtils.saveViewData(realPath, viewItem);
	}

	/**
	 * @param realPath
	 * @param viewItem
	 * @param request
	 * @throws ServletException
	 * @throws IOException
	 */
	public void deleteViewMetaData(String realPath, ViewItem viewItem,
			HttpServletRequest request) throws ServletException, IOException
	{
		ViewMetadataUtilsSingleton viewMetaDataUtils = ViewMetadataUtilsSingleton.getInstance();
		viewMetaDataUtils.saveViewData(realPath, viewItem);
	}
}