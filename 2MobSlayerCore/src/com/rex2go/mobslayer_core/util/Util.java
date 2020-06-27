package com.rex2go.mobslayer_core.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Util {

	public static String getTimeStamp() {
		return new SimpleDateFormat("dd.MM.yyyy HH:mm:s").format(new Date());
	}
	
	public ArrayList<Object> convertStringToArraylist(String str) {
	    ArrayList<Object> charList = new ArrayList<Object>();      
	    for(int i = 0; i<str.length();i++){
	        charList.add(str.charAt(i));
	    }
	    return charList;
	}
}
