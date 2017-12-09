package com.tylert.singletons.viewMetadata;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.tylert.domain.ViewItem;

public interface IViewMetadataCrudBase
{
	public List<ViewItem> getRequestedViewType(String realPath,
		String viewType) throws FileNotFoundException, IOException;
	
	public void saveViewData(String realPath, ViewItem viewItem)
		throws IOException;
}
