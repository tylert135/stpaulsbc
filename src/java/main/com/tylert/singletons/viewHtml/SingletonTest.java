package com.tylert.singletons.viewHtml;

public class SingletonTest
{
	public static void main(String[] args) {
		IViewHtmlCrudBase instance1 = ViewHtmlSingleton.getInstance("File1");
		System.out.println(instance1.getName()+ "  "+instance1);
		IViewHtmlCrudBase instance2 = ViewHtmlSingleton.getInstance("File2");
		System.out.println(instance2.getName()+ "  "+instance2);
	}
}
