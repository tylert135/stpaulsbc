package com.tylert.singletons.galleriaMetadata;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.tylert.domain.Gallery;

public interface GalleriaMetadataCrudBase
{
	public void replacePhoto(String realPath, Gallery g, HttpServletRequest request) throws FileNotFoundException, IOException;

	public List<Gallery> getPhotoObjectList(String realPath, Gallery g) throws FileNotFoundException, IOException;

	public List<Gallery> getGalleriaList(String realPath) throws FileNotFoundException, IOException;

	public String getName();

	public void setName(String name);
}
