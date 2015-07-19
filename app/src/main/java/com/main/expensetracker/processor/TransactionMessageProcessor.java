/**
 *
 */
package com.main.expensetracker.processor;

import android.content.Context;
import android.database.Cursor;

import com.main.expensetracker.ITransactionMessageProcessor;
import com.main.expensetracker.constants.AppConstants;
import com.main.expensetracker.data.ExpenseData;
import com.main.expensetracker.data.MonthWiseExpenseData;
import com.main.expensetracker.utility.AppUtil;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author MaRoy
 *
 */
public class TransactionMessageProcessor implements ITransactionMessageProcessor{

	private static TransactionMessageProcessor transactionMessageProcessor;
	private ExpenseDataProcessor dataProcessor;
	private TransactionMessageProcessor(){
		dataProcessor = ExpenseDataProcessor.getInstance();
	}

	public static TransactionMessageProcessor getInstance(){

		if(transactionMessageProcessor == null)
			transactionMessageProcessor = new TransactionMessageProcessor();
		return transactionMessageProcessor;
	}
	/**
	 * @param c
	 * @param ctx
	 * @return
	 */
	/*public List<MonthWiseExpenseData> processTransactionMessages(Cursor c, Context ctx, boolean isCursorValid, boolean isReloadFromDisk) {

		*//**Pattern to identify the amount spent*//*
		Pattern creditPattern = Pattern.compile(AppConstants.CREDIT_REGEX,Pattern.CASE_INSENSITIVE);
		Pattern debitPattern = Pattern.compile(AppConstants.DEBIT_REGIX,Pattern.CASE_INSENSITIVE);
		Pattern cashWdlPattern = Pattern.compile(AppConstants.CASH_WITHDRAWAL_REGIX,Pattern.CASE_INSENSITIVE);
		Pattern creditCardPatter = Pattern.compile(AppConstants.CREDIT_CARD_REGIX,Pattern.CASE_INSENSITIVE);
	    Pattern r = Pattern.compile(AppConstants.AMOUNT_REGEX);


		boolean isNewUpdate = false;

		String lastUpdatedDateStr = ExpenseDataProcessor.getInstance().readDataFromFile(AppConstants.CONSTANTS_STORAGE_FILE, AppConstants.LAST_UPDATED_DATE, ctx);
		Date lastUpdateDate = (lastUpdatedDateStr != null && !lastUpdatedDateStr.isEmpty()) ? AppUtil.getDateFromTimestamp(Long.valueOf(lastUpdatedDateStr)) : null;
		IExpenseDataProcessor dataProcessor = ExpenseDataProcessor.getInstance();
		String tranExpenseListJson = dataProcessor.readDataFromFile(AppConstants.TRANSACTION_STORAGE_FILE, AppConstants.MONTHLY_LIST, ctx);
		Type type = new TypeToken<List<MonthWiseExpenseData>>(){}.getType();
		List<MonthWiseExpenseData> monthWiselist = dataProcessor.getGson().fromJson(tranExpenseListJson, type);
		monthWiselist = monthWiselist == null ? new ArrayList<MonthWiseExpenseData>() : monthWiselist;

		if(isCursorValid) {

			int startPos = isReloadFromDisk ? c.getCount()-1 : 0;
			int endPos = isReloadFromDisk ? 0 : c.getCount()-1;

			int i = startPos;
			boolean loopCntr = true;

			while(loopCntr) {

				if(i == endPos)
					loopCntr = false;

				String smsBody = c.getString(c.getColumnIndexOrThrow(AppConstants.INBOX_BODY)).toString();

				String timeStamp =  c.getString(c.getColumnIndexOrThrow(AppConstants.INBOX_DATE)).toString();
		    	Date smsDate = AppUtil.getDateFromTimestamp(Long.valueOf(timeStamp));

		    	if(lastUpdateDate != null && (smsDate.before(lastUpdateDate)||smsDate.compareTo(lastUpdateDate) == 0)){
		    		break;
		    	}

                String where = c.getString(c.getColumnIndexOrThrow(AppConstants.INBOX_ADDRESS)).toString();

				if(!isNewUpdate && c.isFirst()) {
						ExpenseDataProcessor.getInstance().saveDataToFile(AppConstants.CONSTANTS_STORAGE_FILE, AppConstants.LAST_UPDATED_DATE, String.valueOf(smsDate.getTime()), ctx);
						isNewUpdate = true;
				}

			    Matcher m = r.matcher(smsBody);
			    Matcher creditMatcher = creditPattern.matcher(smsBody);
			    Matcher debitMatcher = debitPattern.matcher(smsBody);
			    Matcher cashWdlMatcher = cashWdlPattern.matcher(smsBody);
			    //Matcher ccMatcher = creditCardPatter.matcher(smsBody);

			    if(m.find() && isTransactionMessage(smsBody)){

			    	Double amount = extractExpenseAmountFromMsg(smsBody);
					if(amount==0){
						i = isReloadFromDisk ? i-1 : i+1;
						c.moveToPosition(i);
						continue;
					}


					ExpenseData sms = new ExpenseData(where, amount.toString(), AppUtil.convertDateToString(smsDate));
			    	String month = AppUtil.getMonthFromDate(smsDate, AppConstants.MONTH_NAME_SHORT);
			    	MonthWiseExpenseData monthWiseData = AppUtil.getMonthWiseSmsData(month,monthWiselist);

			    	if(monthWiseData != null){

				    	if(creditMatcher.find()){
				    		monthWiseData.setCreditAmount(monthWiseData.getCreditAmount()+amount);
				    		sms.setAccountingType(AppConstants.ACCOUNTING_TYPE_CREDIT);
				    		monthWiseData.add(sms);
				    	}
				    	else if(debitMatcher.find() || cashWdlMatcher.find()){
				    		monthWiseData.setDebitAmount(monthWiseData.getDebitAmount()+amount);
				    		sms.setAccountingType(AppConstants.ACCOUNTING_TYPE_DEBIT);
				    		if(cashWdlMatcher.find()){
				    			sms.setTransactionType(AppConstants.TRANSACTION_TYPE_CASH_WDL);
				    			sms.setCategory(AppConstants.CATEGORY.CASH.toString());
				    		}
				    		monthWiseData.add(sms);
				    	}
			    	}
			    	else{

						//Start of new month. Calculating carry forward amount from previous month
						String pervMonth = AppUtil.getMonthFromDate(AppUtil.subtractMonthFromDate(smsDate,1), AppConstants.MONTH_NAME_SHORT);
						MonthWiseExpenseData prevMonthWiseData = AppUtil.getMonthWiseSmsData(pervMonth, monthWiselist);
						Double carryForwardAmount = prevMonthWiseData == null ? 0D : (prevMonthWiseData.getCreditAmount()+prevMonthWiseData.getCarryForwardAmount() - prevMonthWiseData.getDebitAmount());

						//Creating new month data
			    		monthWiseData = new MonthWiseExpenseData();
						monthWiseData.setCarryForwardAmount(carryForwardAmount);
						monthWiseData.addCarryForward(smsDate);
			    		monthWiseData.setMonth(month.toUpperCase());

				    	if(creditMatcher.find()){
				    		monthWiseData.setCreditAmount(amount);
				    		sms.setAccountingType(AppConstants.ACCOUNTING_TYPE_CREDIT);
				    		monthWiseData.add(sms);
					    	monthWiselist.add(monthWiseData);
				    	}
				    	else if(debitMatcher.find() || cashWdlMatcher.find()){
				    		monthWiseData.setDebitAmount(amount);
				    		sms.setAccountingType(AppConstants.ACCOUNTING_TYPE_DEBIT);
				    		if(cashWdlMatcher.find()){
				    			sms.setTransactionType(AppConstants.TRANSACTION_TYPE_CASH_WDL);
				    			sms.setCategory(AppConstants.CATEGORY.CASH.toString());
				    		}
				    		monthWiseData.add(sms);
					    	monthWiselist.add(monthWiseData);
				    	}
			    	}
			    }

				i = isReloadFromDisk ? i-1 : i+1;
				c.moveToPosition(i);
			}
		}

		//Save data to file
		dataProcessor.setMonthWiseList(monthWiselist);
		dataProcessor.saveDataToFile(AppConstants.TRANSACTION_STORAGE_FILE, AppConstants.MONTHLY_LIST, dataProcessor.getGson().toJson(monthWiselist), ctx);

		return monthWiselist;
	}*/

	public List<MonthWiseExpenseData> processTransactionMessagesNew(Cursor c, Context ctx, boolean isFirstCall){

		List<MonthWiseExpenseData> monthWiselist = dataProcessor.getMonthWiseList(ctx);

		int position = c.getCount()-1;
		if(c.moveToLast() && isFirstCall) {

			while (position >= 0) {

				String smsBody = c.getString(c.getColumnIndexOrThrow(AppConstants.INBOX_BODY)).toString();

				String timeStamp = c.getString(c.getColumnIndexOrThrow(AppConstants.INBOX_DATE)).toString();
				Date smsDate = AppUtil.getDateFromTimestamp(Long.valueOf(timeStamp));

				String where = c.getString(c.getColumnIndexOrThrow(AppConstants.INBOX_ADDRESS)).toString();

				if (isTransactionMessage(smsBody)) {

					Double amount = extractExpenseAmountFromMsg(smsBody);
					if (amount == 0) {

						position--;
						c.moveToPosition(position);
						continue;
					}

					ExpenseData sms = new ExpenseData(where, amount.toString(), AppUtil.convertDateToString(smsDate));
					createMonthWiseData(sms,monthWiselist,ctx,smsBody);
				}
				position--;
				c.moveToPosition(position);
			}
		}

		return monthWiselist;
	}

	public void createMonthWiseData(ExpenseData sms, List<MonthWiseExpenseData> monthWiselist, Context ctx, String smsBody){

		Pattern creditPattern = Pattern.compile(AppConstants.CREDIT_REGEX,Pattern.CASE_INSENSITIVE);
		Pattern debitPattern = Pattern.compile(AppConstants.DEBIT_REGIX,Pattern.CASE_INSENSITIVE);
		Pattern cashWdlPattern = Pattern.compile(AppConstants.CASH_WITHDRAWAL_REGIX,Pattern.CASE_INSENSITIVE);

		Matcher creditMatcher = creditPattern.matcher(smsBody);
		Matcher debitMatcher = debitPattern.matcher(smsBody);
		Matcher cashWdlMatcher = cashWdlPattern.matcher(smsBody);

		String month = AppUtil.getMonthFromStringDate(sms.getDate(), AppConstants.MONTH_NAME_SHORT);
		MonthWiseExpenseData monthWiseData = AppUtil.getMonthWiseSmsData(month, monthWiselist);
		Date smsDate  = AppUtil.getDateFromString(sms.getDate(), "MM/dd/yy HH:mm");

		if(monthWiseData != null){

			if(creditMatcher.find()){
				monthWiseData.setCreditAmount(monthWiseData.getCreditAmount()+Double.valueOf(sms.getAmount()));
				sms.setAccountingType(AppConstants.ACCOUNTING_TYPE_CREDIT);
				monthWiseData.add(sms);
			}
			else if(debitMatcher.find() || cashWdlMatcher.find()){
				monthWiseData.setDebitAmount(monthWiseData.getDebitAmount()+Double.valueOf(sms.getAmount()));
				sms.setAccountingType(AppConstants.ACCOUNTING_TYPE_DEBIT);
				if(cashWdlMatcher.find()){
					sms.setTransactionType(AppConstants.TRANSACTION_TYPE_CASH_WDL);
					sms.setCategory(AppConstants.CATEGORY.CASH.toString());
				}
				monthWiseData.add(sms);
			}
		}
		else{

			//Start of new month. Calculating carry forward amount from previous month
			String pervMonth = AppUtil.getMonthFromDate(AppUtil.subtractMonthFromDate(smsDate,1), AppConstants.MONTH_NAME_SHORT);
			MonthWiseExpenseData prevMonthWiseData = AppUtil.getMonthWiseSmsData(pervMonth, monthWiselist);
			Double carryForwardAmount = prevMonthWiseData == null ? 0D : (prevMonthWiseData.getCreditAmount()+prevMonthWiseData.getCarryForwardAmount() - prevMonthWiseData.getDebitAmount());

			//Creating new month data
			monthWiseData = new MonthWiseExpenseData();
			monthWiseData.setCarryForwardAmount(carryForwardAmount);
			monthWiseData.addCarryForward(smsDate);
			monthWiseData.setMonth(month.toUpperCase());

			if(creditMatcher.find()){
				monthWiseData.setCreditAmount(Double.valueOf(sms.getAmount()));
				sms.setAccountingType(AppConstants.ACCOUNTING_TYPE_CREDIT);
				monthWiseData.add(sms);
				monthWiselist.add(monthWiseData);
			}
			else if(debitMatcher.find() || cashWdlMatcher.find()){
				monthWiseData.setDebitAmount(Double.valueOf(sms.getAmount()));
				sms.setAccountingType(AppConstants.ACCOUNTING_TYPE_DEBIT);
				if(cashWdlMatcher.find()){
					sms.setTransactionType(AppConstants.TRANSACTION_TYPE_CASH_WDL);
					sms.setCategory(AppConstants.CATEGORY.CASH.toString());
				}
				monthWiseData.add(sms);
				monthWiselist.add(monthWiseData);
			}
		}

		//Save data to file
		dataProcessor.setMonthWiseList(monthWiselist);
		dataProcessor.saveDataToFile(AppConstants.TRANSACTION_STORAGE_FILE, AppConstants.MONTHLY_LIST, dataProcessor.getGson().toJson(monthWiselist), ctx);
	}
	public boolean isTransactionMessage(String smsBody){

		Pattern r = Pattern.compile(AppConstants.AMOUNT_REGEX);
		Matcher m = r.matcher(smsBody);
		if(m.find() && (AppUtil.isNumeric(AppUtil.substringBetween(smsBody,"INR",".")) || AppUtil.isNumeric(AppUtil.substringBetween(smsBody,"Rs.","."))))
			return true;
		return false;
	}

	public Double extractExpenseAmountFromMsg(String smsBody){

		Double amount = Double.valueOf(AppUtil.substringBetween(smsBody,"INR","."));
		if(amount==0){
			amount = Double.valueOf(AppUtil.substringBetween(smsBody,"Rs.","."));
		}
		amount = amount != null ? amount : 0;
		return amount;
	}
}
