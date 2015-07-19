/**
 * 
 */
package com.main.expensetracker.utility;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.main.expensetracker.activities.R;
import com.main.expensetracker.data.MonthWiseExpenseData;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author MaRoy
 *
 */
public class AppUtil {

	/**
	 * @param str
	 * @param open
	 * @param close
	 * @return
	 */
	public static String substringBetween(final String str, final String open, final String close) {
        if (str == null || open == null || close == null) {
            return null;
        }
        final int start = str.indexOf(open);
        if (start != -1) {

            int i=start+open.length();
            while(i<str.length()){

                if(close.equalsIgnoreCase(String.valueOf(str.charAt(i))))
                    break;
                i++;
            }
            return removeSpecialCharacters(str.substring(start+open.length(), i));
        }
        return "0";
	}

	/**
	 * @param timestamp
	 * @return
	 */
	public static Date getDateFromTimestamp(Long timestamp){

		if(timestamp == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		return calendar.getTime();
	}
	
	/**
	 * @param strDate
	 * @return
	 */
	public static Date getDateFromString(String strDate, String format){

		Date date = null;
		DateFormat formatter = new SimpleDateFormat(format);
		if(strDate != null && !strDate.isEmpty()){
			try {
				date = formatter.parse(strDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return date;
	}
	
	/**
	 * @param date
	 * @return
	 */
	public static String convertDateToString(Date date){
		
		String dateStr = null;
		DateFormat formatter = new SimpleDateFormat();
		dateStr = formatter.format(date);
		return dateStr;
	}

	/**
	 * @param date
	 * @return
	 */
	public static String getMonthFromDate(Date date, String style){

		Format formatter = new SimpleDateFormat(style); 
	    String s = formatter.format(date);

	    return s;
	}
	
	public static String getWeekFromDate(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return "W"+c.get(Calendar.WEEK_OF_MONTH);
	}
	
	
	public static String getMonthFromStringDate(String strDate, String style){
		
		Date date = AppUtil.getDateFromString(strDate, "MM/dd/yy HH:mm");
        if(date == null){
            date = AppUtil.getDateFromString(strDate, "MM/dd/yy");
        }

		return AppUtil.getMonthFromDate(date, style);
		
	}
	
	public static String getWeekFromStringDate(String strDate){
		
		Date date = AppUtil.getDateFromString(strDate, "MM/dd/yy HH:mm");
		
		return AppUtil.getWeekFromDate(date);
	}
	
	public static int getWeekCountForMonth(Calendar c){
		
		//Getting first date of month
		c.set(Calendar.DATE, 1);
		int start = c.get(Calendar.WEEK_OF_MONTH);
		
		//Getting last date of month
		c.add(Calendar.MONTH, 1);
		c.add(Calendar.DATE, -1);
		int end = c.get(Calendar.WEEK_OF_MONTH);
		
		return end-start+1;
	}

	public static Date getFirstDateOfMonth(Date smsDate){

		Calendar cal = Calendar.getInstance();
		cal.setTime(smsDate);
		cal.set(Calendar.DAY_OF_MONTH,1);
		return cal.getTime();

	}

	public static Date subtractMonthFromDate(Date date, int noOfMonths){

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, -noOfMonths);
		return cal.getTime();
	}

	/**
	 * @param month
	 * @param monthWiseList
	 * @return
	 */
	public static MonthWiseExpenseData getMonthWiseSmsData(String month, List<MonthWiseExpenseData> monthWiseList){

		for(MonthWiseExpenseData smsData : monthWiseList){

			if(month.equalsIgnoreCase(smsData.getMonth())){
				return smsData;
			}
		}
		return null;
	}

	/**
	 * @param value
	 * @return
	 */
	public static boolean isNumeric(String value){

		try{
			Double.parseDouble(value);
			return true;
		}
		catch(Exception e){
			return false;
		}
	}

	/**
	 * @param value
	 * @return
	 */
	public static String removeSpecialCharacters(String value){

		String[] value1 = value.split(",");
		if(value1.length > 1){
			value = "";
			for(int i = 0;i<value1.length;i++){
				value = value + value1[i];
			}
		}
        if(AppUtil.isNumeric(value))
            return value;
        else
            return "0";
	}
	
	/**
	 * @param num
	 * @return
	 */
	public static boolean isOdd(int num) {
		  int i = 0;
		  boolean odd = false;

		  while (i != num) {
		    odd = !odd;
		    i = i + 1;
		  }

		  return odd;
		}
	
	/**
	 * @param context
	 * @param position
	 * @return
	 */
	public static Drawable randomColorPicker(Context context, int position){
		
		if(position > 4)
			position = position%4;
		
		Resources res = context.getResources();
        TypedArray rounded_icons = res.obtainTypedArray(R.array.rounded_icons);
        Drawable drawable = rounded_icons.getDrawable(position);
        
		return drawable;
	}

    public static float round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    public static String getRandomColor(){
        Random rand = new Random();
        Integer randomColor =  Color.rgb(rand.nextInt(), rand.nextInt(), rand.nextInt());
        return randomColor.toString();
    }
}
