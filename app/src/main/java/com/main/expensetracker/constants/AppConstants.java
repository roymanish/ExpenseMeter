/**
 * 
 */
package com.main.expensetracker.constants;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MaRoy
 *
 */
public class AppConstants {
	
	public static String INBOX_URI = "content://sms/inbox";
	
	public static String AMOUNT_REGEX = "INR \\d+|Rs.\\d+|INR\\d+";
	
	public static String CREDIT_REGEX = ".*\\bcredited.*";
	
	public static String DEBIT_REGIX = ".*\\bdebited.*|.*\\bDebit Card.*|.*\\bdebit card.*";
	
	public static String CASH_WITHDRAWAL_REGIX = ".*\\bWDL\\b.*|.*\\bwithdrawn\\b.*";
	
	public static String CREDIT_CARD_REGIX = ".*\\bCredit\\bCard\\b.*";
	
	public static String INBOX_BODY = "body";
	
	public static String INBOX_DATE = "date";
	
	public static String INBOX_ADDRESS = "address";

	public static String MONTHLY_LIST = "MONTHLY_LIST";
	
	public static String WEEKLY_LIST = "Weekly_List";
	
	public static String ACCOUNTING_TYPE_CREDIT = "CREDIT";
	
	public static String ACCOUNTING_TYPE_DEBIT = "DEBIT";
	
	public static String YTD = "YTD";
	
	public static String TRANSACTION_TYPE_CASH_WDL = "ATM WDL";
	
	public static String MONTH_NAME = "MonthName";
	
	public static String WEEK_NBR = "Week_Nbr";
	
	public static String CONSTANTS_STORAGE_FILE = "App_Constants_Storage_File";
	
	public static String LAST_UPDATED_DATE = "LAST_UPDATE_DATE";

	public static String IS_FIRST_CALL = "Is_First_Call";
	
	public static String TRANSACTION_STORAGE_FILE = "Transaction_Storage_File";
	
	public static String MONTH_NAME_SHORT = "MMM";
	
	public static String MONTH_NAME_LONG = "MMMM";
	
	public static String ITEM_POSITION = "position";
	
	public static String EDIT_AMOUNT = "amount";
	
	public static String EDIT_DATE = "date";
	
	public static String EDIT_EXPENSE_CATEGORY = "expenseCategory";
	
	public static String EXPENSE_TYPE_FIXED = "Fixed Expense";
	
	public static String EXPENSE_TYPE_VARIABLE = "Variable Expense";

	public static Map<String, Integer> monthMap = new HashMap<String, Integer>();
	static {
		monthMap.put("JAN", Calendar.JANUARY);
		monthMap.put("FEB",Calendar.FEBRUARY);
		monthMap.put("MAR",Calendar.MARCH);
		monthMap.put("APR",Calendar.APRIL);
		monthMap.put("MAY",Calendar.MAY);
		monthMap.put("JUN",Calendar.JUNE);
		monthMap.put("JUL",Calendar.JULY);
		monthMap.put("AUG",Calendar.AUGUST);
		monthMap.put("SEP",Calendar.SEPTEMBER);
		monthMap.put("OCT",Calendar.OCTOBER);
		monthMap.put("NOV",Calendar.NOVEMBER);
		monthMap.put("DEC",Calendar.DECEMBER);
	}
	
	public static enum CATEGORY{
		
		FOOD{
			public String toString(){
				return "Food";
			}
		},
		
		RENT{
			public String toString(){
				return "Rent";
			}
		},
		
		SAVINGS{
			public String toString(){
				return "Savings";
			}
		},
		
		INVESTMENT{
			public String toString(){
				return "Investment";
			}
		},
		
		CASH{
			public String toString(){
				return "Cash";
			}
		},
		
		CREDIT_CARD{
			public String toString(){
				return "Credit Card";
			}
		},

        BALANCE{
            public String toString(){ return "Balance"; }
        },
		CARRY_FORWARD{
			@Override
			public String toString() {
				return "Carry Forward";
			}
		}
	}

}
