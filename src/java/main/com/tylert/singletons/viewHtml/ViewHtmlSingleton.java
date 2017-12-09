package com.tylert.singletons.viewHtml;

import java.util.HashMap;

public class ViewHtmlSingleton
{
	private static HashMap<String, IViewHtmlCrudBase> xmlCrudHashMap = new HashMap<String, IViewHtmlCrudBase>();
	
	public static synchronized IViewHtmlCrudBase getInstance(String crudName)
	{
		if (xmlCrudHashMap.get(crudName) == null)
		{
			IViewHtmlCrudBase xmlCrud = new ViewHtmlUtils();
			xmlCrud.setName(crudName);
			xmlCrudHashMap.put(crudName, xmlCrud);
			return xmlCrud;
		}
		return xmlCrudHashMap.get(crudName);
	}

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Clone is not allowed.");
    }
}
