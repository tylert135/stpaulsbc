package com.tylert.view.service.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;

public class CommonUtils
{
	public static String APP_PROPERTIES = "WEB-INF/config/app.properties";
	
	public static Properties getProps(String filename)
			throws FileNotFoundException, IOException
	{
		Properties props = new Properties();
		props.load(new FileInputStream(filename));
		return props;
	}	
	
	public static boolean isGoodIp(String ip)
	{
//		int i=0;if (i==0) return true;
		if (!ip.equals("0:0:0:0:0:0:0:1"))
		{
			StringTokenizer stok = new StringTokenizer(ip,".");
			String[] addr = new String[4];
			addr[0] = stok.nextToken();
			addr[1] = stok.nextToken();
			addr[2] = stok.nextToken();
			addr[3] = stok.nextToken();
			// make sure it's not connected locally
			if (   !(addr[0].equals("192") && addr[1].equals("168") && addr[2].equals("0")) //Home
					&& !(addr[0].equals("156") && addr[1].equals("70") 
							&& addr[2].equals("222") && addr[2].equals("30"))
					&& !(addr[0].equals("72") && addr[1].equals("92") 
							&& addr[2].equals("79") && addr[2].equals("217"))
					&& !(addr[0].equals("68") && addr[1].equals("32") 
							&& addr[2].equals("38") && addr[2].equals("129"))
					&& !(addr[0].equals("162") && addr[1].equals("111") 
							&& addr[2].equals("195"))
					&& !(addr[0].equals("127") && addr[1].equals("0") 
							&& addr[2].equals("0"))
				)
				return true;
		}
		return false;
	}
}
