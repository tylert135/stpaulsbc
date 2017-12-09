package com.tylert.domain;

import java.util.ArrayList;
import java.util.List;

public class ViewItem {
	
    public static String SAVE = "Save";
    public static String NEW = "New";
    public static String DELETE = "Delete";
    public static String PATH = "sections";
    public static String CHANGE_LOG = "changeLog";
    public static String ANONYMOUS_USER_ROLE = "anonymousUser";
    public static String SUPER_ROLE = "SUPER";
    public static String VIEW_PATH = "metaData/view/";
    public static String VIEW_METADATA_FILE = VIEW_PATH+"viewData.xml";
    public static String VIEW_FILE_EXTENSION = ".html";
	
	private String name;
	private String type;	// custom or default view item
	private String id;
	private String html;
	private String description = "";
	private String path;
	private String action;
	private List<String> roles = new ArrayList<String>();
	private List<ViewItem> subView = new ArrayList<ViewItem>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	public String getAction()
	{
		return action;
	}
	public void setAction(String action)
	{
		this.action = action;
	}
	public List<ViewItem> getSubView()
	{
		return subView;
	}
	public void setSubView(List<ViewItem> subView)
	{
		this.subView = subView;
	}
}
