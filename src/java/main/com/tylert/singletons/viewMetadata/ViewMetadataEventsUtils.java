package com.tylert.singletons.viewMetadata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.springframework.util.StringUtils;

import com.tylert.xmlBeans.viewData.LinkDocument.Link;
import com.tylert.xmlBeans.viewData.ViewItemsDocument;
import com.tylert.xmlBeans.viewData.ViewItemsDocument.ViewItems;
import com.tylert.xmlBeans.viewData.SublinkDocument.Sublink;
import com.tylert.xmlBeans.viewData.ViewDocument.View;

import com.tylert.domain.ViewItem;
import com.tylert.security.UserUtils;
import com.tylert.stats.WebStatisticsUtils;

public class ViewMetadataEventsUtils implements IViewMetadataCrudBase
{
	public List<ViewItem> getRequestedViewType(String realPath,
			String viewType) throws FileNotFoundException, IOException
	{
		WebStatisticsUtils.logMsg("getRequestedMenuItem");
		List<ViewItem> menuList = new ArrayList<ViewItem>();

		File f = new File(realPath + ViewItem.VIEW_METADATA_FILE);

		try
		{
			ViewItemsDocument viewItemsDoc = ViewItemsDocument.Factory
					.parse(f);
			ViewItems mItems = viewItemsDoc.getViewItems();
			View viewArray[] = mItems.getViewArray();

			for (int j = 0; j < viewArray.length; j++)
				if (viewType == null
						|| viewArray[j].getType().equals(viewType))
				{
					Link linkArray[] = viewArray[j].getLinkArray();
					for (int i = 0; i < linkArray.length; i++)
					{
						if (!UserUtils.isInRole(UserUtils.getRoles(),
								ViewItem.SUPER_ROLE)
								&& "changeLog".equals(linkArray[i].getId()))
							continue;

						menuList.add(setViewItem(linkArray[i],
								viewArray[j].getType(), mItems.getPath()));
					}

				}

		}
		catch (XmlException e)
		{
			WebStatisticsUtils.logMsg(e.toString());
			e.printStackTrace();
		}
		WebStatisticsUtils.logMsg("Return menu items");
		return menuList;
	}

	private ViewItem setViewItem(Link link, String type, String path)
	{
		ViewItem vi = new ViewItem();
		vi.setId(link.getId());
		vi.setName(link.getName());
		vi.setDescription(link.getDescription());
		vi.setPath(path);
		vi.setType(type);
		String roleArray[] = link.getRoleArray();

		for (int k = 0; k < roleArray.length; k++)
			vi.getRoles().add(roleArray[k]);

		Sublink subLinkArray[] = link.getSublinkArray();
		List<ViewItem> subList = new ArrayList<ViewItem>();
		if (subLinkArray != null && subLinkArray.length > 0)
			for (int i = 0; i < subLinkArray.length; i++)
				subList.add(setSubViewItem(subLinkArray[i], type, path));
		vi.setSubView(subList);
		return vi;
	}

	private ViewItem setSubViewItem(Sublink link, String type, String path)
	{
		ViewItem vi = new ViewItem();
		vi.setName(link.getName());
		vi.setId(link.getId());
		vi.setDescription(link.getDescription());
		vi.setPath(path);
		vi.setType(type);
		String roleArray[] = link.getRoleArray();
		for (int k = 0; k < roleArray.length; k++)
			vi.getRoles().add(roleArray[k]);
		return vi;
	}

	public void saveViewData(String realPath, ViewItem viewItem)
			throws IOException
	{

		File f = new File((new StringBuilder()).append(realPath)
				.append(ViewItem.VIEW_METADATA_FILE).toString());
		ViewItemsDocument viewItemsDoc;
		try
		{
			viewItemsDoc = ViewItemsDocument.Factory.parse(f);
			ViewItemsDocument.ViewItems vItems = viewItemsDoc
					.getViewItems();
			View[] viewArray = vItems.getViewArray();

			int i = 0;
			int j = 0;
			Link l = null;
			Sublink sl = null;
			boolean isUpdated = false;
			for (i = 0; i < viewArray.length; i++)
			{
				// for same link types
				if (viewArray[i].getType().equals(viewItem.getType()))
				{

					//check link
					if (ViewItem.NEW.equals(viewItem.getAction()))
					{
						l = viewArray[i].addNewLink();
						updateXmlbeansLink(l, viewItem);
						isUpdated = true;
						break;
					}

					Link linkArray[] = viewArray[i].getLinkArray();
					for (j = 0; j < linkArray.length; j++)
					{
						if (linkArray[j].getId().equals(viewItem.getId()))
						{

							if (ViewItem.DELETE.equals(viewItem.getAction()))
								viewArray[i].removeLink(j);
							else
							{
								l = linkArray[j];
								updateXmlbeansLink(l, viewItem);
							}
							isUpdated = true;
							break;
						}
						
						//now check sublinks
						Sublink subLinkArray[] = linkArray[j].getSublinkArray();
						
						if (subLinkArray == null || subLinkArray.length == 0)
							continue;
						else
						{
							for (int k=0; k<subLinkArray.length; k++)
							{
								if (ViewItem.NEW.equals(viewItem.getAction()))
								{
								
									sl = linkArray[j].addNewSublink();
									updateXmlbeansSublink(sl, viewItem);
									isUpdated = true;
									break;
								}
		
								if (subLinkArray[k].getId().equals(viewItem.getId()))
								{
									if (ViewItem.DELETE.equals(viewItem.getAction()))
									{
										linkArray[j].removeSublink(k);
									}
									else
									{
										sl = subLinkArray[k];
										updateXmlbeansSublink(sl, viewItem);
									}
									isUpdated = true;
									break;
								}								
							}
						}
					}
				}
				if (isUpdated)
					break;
			}
			XmlOptions xmlOptions = new XmlOptions();
			xmlOptions.setSavePrettyPrint();
			xmlOptions.setSavePrettyPrintIndent(4);
			viewItemsDoc.save(f, xmlOptions);
		}
		catch (XmlException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}

	private void updateXmlbeansLink(Link l, ViewItem newVi)
	{
		l.setId(newVi.getId());
		l.setName(newVi.getName());
		l.setDescription(newVi.getDescription());
		
		String[] roleArray = new String[0];
		if(l.getRoleArray().length == 0)
		{
			roleArray = StringUtils.addStringToArray(roleArray, ViewItem.SUPER_ROLE);
			roleArray = StringUtils.addStringToArray(roleArray, ViewItem.ANONYMOUS_USER_ROLE);
		}
		else
		{			
			for (String s : newVi.getRoles())
				roleArray = StringUtils.addStringToArray(roleArray, s);
		}
		l.setRoleArray(roleArray);
		
		
		
		Sublink[] subLinkArray = newVi.getSubView() == null || newVi.getSubView().size() == 0? 
				new Sublink[0] : 
				new Sublink[newVi.getSubView().size()]; //create sublink of correct size
		if (subLinkArray.length > 0)
		{
			//subLinkArray = new Sublink[newVi.getSubView().size()];
			for (int i=0; i < newVi.getSubView().size(); i++)
			{
				Sublink sl = Sublink.Factory.newInstance();
				sl.setId(newVi.getSubView().get(i).getId());
				sl.setName(newVi.getSubView().get(i).getName());
				sl.setDescription(newVi.getSubView().get(i).getDescription());
				

				roleArray = new String[0];
				if(sl.getRoleArray().length == 0)
				{
					roleArray = StringUtils.addStringToArray(roleArray, ViewItem.SUPER_ROLE);
					roleArray = StringUtils.addStringToArray(roleArray, ViewItem.ANONYMOUS_USER_ROLE);
				}
				else
				{			
					for (String s : newVi.getRoles())
						roleArray = StringUtils.addStringToArray(roleArray, s);
				}
				sl.setRoleArray(roleArray);				
			}
			l.setSublinkArray(subLinkArray); //set sublinks
		}
		
		
	}

	private void updateXmlbeansSublink(Sublink sl, ViewItem newVi)
	{
		sl.setId(newVi.getId());
		sl.setName(newVi.getName());
		sl.setDescription(newVi.getDescription());
		
		String[] roleArray = new String[0];
		if(sl.getRoleArray().length == 0)
		{
			roleArray = StringUtils.addStringToArray(roleArray, ViewItem.SUPER_ROLE);
			roleArray = StringUtils.addStringToArray(roleArray, ViewItem.ANONYMOUS_USER_ROLE);
		}
		else
		{			
			for (String s : newVi.getRoles())
				roleArray = StringUtils.addStringToArray(roleArray, s);
		}
		sl.setRoleArray(roleArray);
	}

}
