package com.tylert.singletons.galleriaMetadata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;

import com.tylert.xmlBeans.galleria.GalleriaDocument.Galleria;
import com.tylert.xmlBeans.galleria.PhotoDocument.Photo;
import com.tylert.xmlBeans.galleria.PhotoGalleryDocument;
import com.tylert.xmlBeans.galleria.PhotoGalleryDocument.PhotoGallery;

import com.tylert.domain.Gallery;
import com.tylert.view.service.utils.GalleriaUtils;

public class GalleriaMetadataEventsUtils implements GalleriaMetadataCrudBase
{
	private String name;

	public static String GALLERY_XML = "metaData/gallery/galleria.xml";
	public static String GALLERY_TEMPLATE_FILE = "metaData/gallery/galleriaTemplate.jsp";

	public List<Gallery> getGalleriaList(String realPath)
			throws FileNotFoundException, IOException
	{
		File f = new File(realPath + GALLERY_XML);

		List<Gallery> gList = new ArrayList<Gallery>();
		try
		{
			PhotoGalleryDocument gDoc = PhotoGalleryDocument.Factory.parse(f);
			PhotoGallery pg = gDoc.getPhotoGallery();
			Galleria[] galleries = pg.getGalleriaArray();
			for (int j = 0; j < galleries.length; j++)
			{
				Gallery g = new Gallery();
				g.setGalleryName(galleries[j].getLink());
				g.setId(galleries[j].getId().intValue());
				gList.add(g);
			}
		} catch (XmlException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gList;
	}

	public List<Gallery> getPhotoObjectList(String realPath, Gallery g)
			throws FileNotFoundException, IOException
	{
		File f = new File(realPath + GALLERY_XML);

		List<Gallery> pList = new ArrayList<Gallery>();
		try
		{
			PhotoGalleryDocument pDoc = PhotoGalleryDocument.Factory.parse(f);
			PhotoGallery pg = pDoc.getPhotoGallery();
			Galleria[] galleries = pg.getGalleriaArray();
			for (int j = 0; j < galleries.length; j++)
			{
				if (galleries[j].getId().intValue() == g.getId())
				{
					Photo[] photos = galleries[j].getPhotoArray();
					for (int i = 0; i < photos.length; i++)
					{
						Gallery newGallery = new Gallery();
						newGallery.setId(galleries[j].getId().intValue());
						newGallery.setGalleryName(galleries[j].getLink());
						newGallery.setSource(photos[i].getSource());
						newGallery.setTitle(photos[i].getTitle());
						newGallery.setDescription(photos[i].getDescription());
						pList.add(newGallery);
					}
				}
			}
		} catch (XmlException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pList;
	}

	public void replacePhoto(String realPath, Gallery g, HttpServletRequest request)
			throws FileNotFoundException, IOException
	{
		File f = new File(realPath + GALLERY_XML);

		try
		{
			PhotoGalleryDocument pDoc = PhotoGalleryDocument.Factory.parse(f);
			PhotoGallery pg = pDoc.getPhotoGallery();
			Galleria[] galleries = pg.getGalleriaArray();
			for (int j = 0; j < galleries.length; j++)
			{
				if (galleries[j].getId().intValue() == g.getId())
				{
					// add new photo
					Photo p = galleries[j].addNewPhoto();
					p.setDescription(g.getDescription());
					p.setSource(g.getSource());
					p.setTitle(g.getTitle());
					XmlOptions xmlOptions = new XmlOptions();
					xmlOptions.setSavePrettyPrint();
					xmlOptions.setSavePrettyPrintIndent(4);

					pDoc.save(f, xmlOptions);
					// create new jsp based on xml
					GalleriaUtils.generateGalleryInclude(realPath, galleries[j].getId()
							.intValue(), galleries[j], request);
					break;
				}
			}
		} catch (XmlException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
