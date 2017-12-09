package com.tylert.domain;

public class UserReportRequest
{
	private String fname;
	private String lname;
	private String email;
	private String address;
	private String city;
	private String state;
	private String zip;
	private String Comment;
	private String returnURL;
	
	public String getFname()
	{
		return fname;
	}
	public void setFname(String fname)
	{
		this.fname = fname;
	}
	public String getLname()
	{
		return lname;
	}
	public void setLname(String lname)
	{
		this.lname = lname;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getAddress()
	{
		return address;
	}
	public void setAddress(String address)
	{
		this.address = address;
	}
	public String getCity()
	{
		return city;
	}
	public void setCity(String city)
	{
		this.city = city;
	}
	public String getState()
	{
		return state;
	}
	public void setState(String state)
	{
		this.state = state;
	}
	public String getZip()
	{
		return zip;
	}
	public void setZip(String zip)
	{
		this.zip = zip;
	}
	public String getComment()
	{
		return Comment;
	}
	public void setComment(String comment)
	{
		Comment = comment;
	}
	public String getReturnURL()
	{
		return returnURL;
	}
	public void setReturnURL(String returnURL)
	{
		this.returnURL = returnURL;
	}
}
