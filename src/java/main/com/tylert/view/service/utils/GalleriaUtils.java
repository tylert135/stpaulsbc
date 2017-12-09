package com.tylert.view.service.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;

import com.tylert.domain.Gallery;
import com.tylert.singletons.galleriaMetadata.GalleriaMetadataEventsUtils;
import com.tylert.singletons.galleriaMetadata.GalleriaMetadataUtilsSingleton;
import com.tylert.singletons.viewHtml.IViewHtmlCrudBase;
import com.tylert.singletons.viewHtml.ViewHtmlSingleton;
import com.tylert.singletons.viewHtml.ViewHtmlUtils;
import com.tylert.xmlBeans.galleria.GalleriaDocument.Galleria;
import com.tylert.xmlBeans.galleria.PhotoDocument;
import com.tylert.xmlBeans.galleria.PhotoDocument.Photo;
import com.tylert.xmlBeans.galleria.PhotoGalleryDocument;
import com.tylert.xmlBeans.galleria.PhotoGalleryDocument.PhotoGallery;

public class GalleriaUtils
{
	public static Gallery getGalleria(String path, int id)
			throws FileNotFoundException, IOException
	{
		GalleriaMetadataUtilsSingleton galleriaInstance = GalleriaMetadataUtilsSingleton
				.getInstance();
		List<Gallery> gList = galleriaInstance.getGalleriaList(path);
		for (Gallery g : gList)
		{
			if (g.getId() == id)
				return g;
		}
		return null;
	}

	public static List<String> generateGalleryLI(List<Gallery> gList)
	{
		List<String> aList = new ArrayList<String>();
		for (Gallery g : gList)
		{
			aList.add("<a href=\"photoGallery.htm?galleryId=" + g.getId()
					+ "\">" + g.getGalleryName() + "</a>");
		}
		return aList;
	}

	public static void generateGalleryInclude(String realPath, int id,
			Galleria g, HttpServletRequest request) throws IOException
	{
		IViewHtmlCrudBase instance = ViewHtmlSingleton.getInstance("gallery"
				+ id);
		String template = instance.readHtmlFile(realPath
				+ GalleriaMetadataEventsUtils.GALLERY_TEMPLATE_FILE);
		String photoJsp = createPhotoJsp(template, id, g);
		instance.saveHtmlFile(realPath, "WEB-INF/jsp/photoGallery/gallery" + id
				+ ".jsp", photoJsp, request);
	}

	private static String createPhotoJsp(String template, int id, Galleria g)
	{
		String photoJsp = "";

		for (Photo p : g.getPhotoArray())
		{
			String pSrc = "./assets/images/galleria/" + id + "/"
					+ p.getSource();
			String tmpStr = template.replaceAll("IMAGE_SOURCE", pSrc);
			tmpStr = tmpStr.replace("TITLE", p.getTitle());
			tmpStr = tmpStr.replace("DESCRIPTION", p.getDescription());
			tmpStr = tmpStr.replace("GALLERY_ID", Integer.toString(id));

			photoJsp = photoJsp + "\n" + tmpStr;
		}
		return photoJsp;
	}

	/**
	 * 
	 * @param argv
	 */
	public static void main(String[] argv)
	{
		String path = null;
		try {
			path = new File(".").getAbsolutePath().replace(".", "");
			String propFilename = path + "build.properties";
			System.out.println(propFilename);
			Properties props = CommonUtils.getProps(propFilename);
			System.out.println(props.getProperty("appserver.home"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		int galleryId = 108;
		String photoDirStr = path + "webapp/assets/images/galleria/" + galleryId;
		String webapp = path + "webapp/";

		String galleryTemplateFile = webapp+GalleriaMetadataEventsUtils.GALLERY_TEMPLATE_FILE;
		File photoDir = new File(photoDirStr);
		String[] photoFiles = photoDir.list();

		String galleryXml = webapp+GalleriaMetadataEventsUtils.GALLERY_XML;
		File f = new File(galleryXml);

		int ctr=0;

		System.out.println("Creating gallery " + galleryId);
		try
		{
			String galleryTemplate = ViewHtmlUtils.read(galleryTemplateFile);
			PhotoGalleryDocument pDoc = PhotoGalleryDocument.Factory.parse(f);
			PhotoGallery pg = pDoc.getPhotoGallery();
			Galleria[] galleries = pg.getGalleriaArray();
			int j = 0;
			for (j = 0; j < galleries.length; j++)
			{
				// remove old photos
				if (galleries[j].getId().intValue() == galleryId)
				{
					PhotoDocument.Photo[] galleriaArray = galleries[j]
							.getPhotoArray();
					for (int i = galleriaArray.length-1; i >= 0; i--)
					{
						//Photo pTest = galleries[j].getPhotoArray(i);
						galleries[j].removePhoto(i);
					}
					break;
				}
			}

			String galleryJsp = "";
			for (String filename : photoFiles)
			{
				Photo p = galleries[j].addNewPhoto();
				p.setSource(filename);
				p.setTitle("");
				p.setDescription("");
				System.out.println("\tFile:  " + filename);
				String template = new String(galleryTemplate);
				String pSrc = "./assets/images/galleria/" + galleryId + "/"
				+ p.getSource();
				String tmpStr = template.replaceAll("IMAGE_SOURCE", pSrc);
				tmpStr = tmpStr.replace("TITLE", p.getTitle());
				tmpStr = tmpStr.replace("DESCRIPTION", p.getDescription());
				tmpStr = tmpStr.replace("GALLERY_ID", Integer.toString(galleryId));
				galleryJsp = galleryJsp + "\n" + tmpStr;
				ctr++;
			}
			XmlOptions xmlOptions = new XmlOptions();
			xmlOptions.setSavePrettyPrint();
			xmlOptions.setSavePrettyPrintIndent(4);
			pDoc.save(f, xmlOptions);
			
			FileWriter fileWriter = null;
			PrintWriter printWriter = null;
			// output new (html) section
			fileWriter = new FileWriter(webapp+"WEB-INF/jsp/photoGallery/gallery"+galleryId+".jsp");
			printWriter = new PrintWriter(fileWriter);
			printWriter.println(galleryJsp);
			printWriter.close();

		}
		catch (XmlException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Gallery " + galleryId + " created. (" + ctr + " images)");
	}

}
