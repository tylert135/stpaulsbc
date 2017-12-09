package com.tylert.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.tylert.domain.ViewItem;

public interface IViewItemService
{
	public void save(String realPath, ViewItem viewItem, HttpServletRequest request) throws ServletException, IOException;
	public void delete(String realPath, ViewItem viewItem, HttpServletRequest request) throws ServletException, IOException;
}
