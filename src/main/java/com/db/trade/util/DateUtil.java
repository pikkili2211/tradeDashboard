package com.db.trade.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static boolean isvalidDate(String strDate) throws Exception {

		if (null != strDate) {
			Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
			if (date1.before(new Date()))
				return false;
			return true;
		}
		return false;
	}

	public static Date stringToDateConverter(String str) throws Exception {
		  return new SimpleDateFormat("yyyy-MM-dd").parse(str);
	}

	public static String dateToStringConversion(Date inputDate) {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String strDate = dateFormat.format(inputDate);
		System.out.println("Converted String: " + strDate);
		return strDate;
	}

}
