package com.tylert.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.tylert.security.UserUtils;
import com.tylert.stats.WebStatisticsUtils;

@Controller
public class UtilController {
	
	@RequestMapping(value = "/getRoles.htm", method = RequestMethod.GET)
	protected void getRoless(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{

		WebStatisticsUtils.logSessionMsg(request.getSession(), "Get Roles" );
		JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("roles", (new Gson()).toJson(UserUtils.getRoles()));
        }
        catch(Exception pe)
        {
            pe.printStackTrace();
        }
        PrintWriter writer = response.getWriter();
        response.setContentType("text/html");
        writer.println(jsonObject);

	}

	

}
