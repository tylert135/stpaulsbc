package com.tylert.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.tylert.domain.Gallery;
import com.tylert.domain.UploadItem;
import com.tylert.singletons.galleriaMetadata.GalleriaMetadataUtilsSingleton;
import com.tylert.view.service.utils.GalleriaUtils;
import com.tylert.view.service.utils.ViewUtils;

@Controller
@SessionAttributes(value={"uploadItem"})
public class UploadController implements HandlerExceptionResolver
{
	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String getUploadForm(ModelMap model, HttpServletRequest request) throws FileNotFoundException, IOException
	{
		UploadItem uploadItem = new UploadItem();
		String path = request.getSession().getServletContext().getRealPath("/");
		GalleriaMetadataUtilsSingleton galleriaInstance = GalleriaMetadataUtilsSingleton.getInstance();
		uploadItem.setGalleryList(galleriaInstance.getGalleriaList(path));

		model.addAttribute("uploadItem", uploadItem);
		ViewUtils.addLandingPageAttributes(model);
		return "uploadForm";
	}

	@RequestMapping(value = "/uploadSuccess", method = RequestMethod.GET)
	public String uploadSuccess(@ModelAttribute("uploadItem") UploadItem uploadItem, Model model, HttpServletRequest request) 
		throws FileNotFoundException, IOException
	{
		String realPath = request.getSession().getServletContext().getRealPath("/");
		model.addAttribute("uploadItem", uploadItem);
		// get gallery based on Id, this will get me the photo gallery name
		Gallery g = GalleriaUtils.getGalleria(realPath, uploadItem.getGallery().getId());

		// set some fields not entered by form
		uploadItem.getGallery().setSource(uploadItem.getFileData().getOriginalFilename());
		uploadItem.getGallery().setGalleryName(g.getGalleryName());
		
		// now that all the fields are populated, now save the new image data
		GalleriaMetadataUtilsSingleton galleriaInstance = GalleriaMetadataUtilsSingleton.getInstance();
		galleriaInstance.replacePhoto(realPath, uploadItem.getGallery(), request);
		return "uploadSuccess";
	}

	@RequestMapping(value = "/uploadFailure", method = RequestMethod.GET)
	public String uploadFailure(Model model)
	{
		model.addAttribute(new UploadItem());
		return "uploadFailure";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String create(ModelAndView mav, @ModelAttribute("uploadItem") UploadItem uploadItem,
			BindingResult result, HttpServletRequest request)
	{
		if (result.hasErrors())
		{
			for (ObjectError error : result.getAllErrors())
			{
				System.err.println("Error: " + error.getCode() + " - "
						+ error.getDefaultMessage());
			}
			return "uploadForm";
		}

		// Some type of file processing...
		System.err.println("-------------------------------------------");
		System.err.println("Test upload: " + uploadItem.getGallery().getId());
		System.err.println("Test upload: "
				+ uploadItem.getFileData().getOriginalFilename());
		System.err.println("-------------------------------------------");
		String path = request.getSession().getServletContext().getRealPath("/");

		try
		{
			uploadItem.getFileData().transferTo(
					new File(path + "/assets/images/galleria/" + uploadItem.getGallery().getId() + "/"
							+ uploadItem.getFileData().getOriginalFilename()));
		} catch (IOException e)
		{
			System.out.println(e.toString());
		}

		return "redirect:uploadSuccess.htm";
	}

	/*** Trap Exceptions during the upload and show errors back in view form ***/ 
    public ModelAndView resolveException(HttpServletRequest request, 
            HttpServletResponse response, Object handler, Exception exception) 
    {         
        Map<String, Object> model = new HashMap<String, Object>(); 
        if (exception instanceof MaxUploadSizeExceededException) 
        { 
        	System.out.println(exception.toString());
            model.put("errors", exception.getMessage()); 
        } else 
        { 
        	System.out.println(exception.toString());
            model.put("errors", "Unexpected error: " + exception.getMessage()); 
        } 
        model.put("uploadedFile", new UploadItem()); 
        return new ModelAndView("uploadFailure", (Map<String, Object>) model); 
    } 
 

	
	
}
