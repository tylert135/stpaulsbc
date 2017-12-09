package com.tylert.domain;

import java.util.List;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class UploadItem
{
	private Gallery gallery;
	private List<Gallery> galleryList;
	private CommonsMultipartFile fileData;
	public Gallery getGallery()
	{
		return gallery;
	}
	public void setGallery(Gallery gallery)
	{
		this.gallery = gallery;
	}
	public List<Gallery> getGalleryList()
	{
		return galleryList;
	}
	public void setGalleryList(List<Gallery> galleryList)
	{
		this.galleryList = galleryList;
	}
	public CommonsMultipartFile getFileData()
	{
		return fileData;
	}
	public void setFileData(CommonsMultipartFile fileData)
	{
		this.fileData = fileData;
	}
	
}
