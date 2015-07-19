/**
 * 
 */
package com.main.expensetracker.data;

import com.main.expensetracker.constants.AppConstants;
import com.main.expensetracker.utility.AppUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MaRoy
 *
 */
public class MonthWiseExpenseData {

	private String month;
	
	private Double amount;
	
	private Double creditAmount;

	private Date firstDateOfMonth;
	
	private Double debitAmount;

	private Double carryForwardAmount;
	
	private ArrayList<ExpenseData> smsList;


	public MonthWiseExpenseData() {
	}

	/**
	 * @return the month
	 */

	public String getMonth() {
		return month;
	}
	/**
	 * @param month the month to set
	 */
	public void setMonth(String month) {
		this.month = month;
	}
	/**
	 * @return the amount
	 */
	public Double getAmount() {
		return this.getCreditAmount() - this.getDebitAmount();
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	/**
	 * @return the creditAmount
	 */
	public Double getCreditAmount() {
		
		if(this.creditAmount != null)
			return creditAmount;
		return 0D;
	}
	/**
	 * @param creditAmount the creditAmount to set
	 */
	public void setCreditAmount(Double creditAmount) {
		this.creditAmount = creditAmount;
	}
	/**
	 * @return the debitAmount
	 */
	public Double getDebitAmount() {
		
		if(this.debitAmount != null)
			return debitAmount;
		return 0D;
	}
	/**
	 * @param debitAmount the debitAmount to set
	 */
	public void setDebitAmount(Double debitAmount) {
		this.debitAmount = debitAmount;
	}
	/**
	 * @return the smsList
	 */
	public ArrayList<ExpenseData> getSmsList() {
		return smsList;
	}
	/**
	 * @param smsList the smsList to set
	 */
	public void setSmsList(ArrayList<ExpenseData> smsList) {
		this.smsList = smsList;
	}


	public Double getCarryForwardAmount() {
		return carryForwardAmount;
	}

	public void setCarryForwardAmount(Double carryForwardAmount) {
		this.carryForwardAmount = carryForwardAmount;
	}

	public Date getFirstDateOfMonth() {
		return firstDateOfMonth;
	}

	public void setFirstDateOfMonth(Date firstDateOfMonth) {
		this.firstDateOfMonth = firstDateOfMonth;
	}

	public void addCarryForward(Date smsDate){

		Date firstDateOfMonth = AppUtil.getFirstDateOfMonth(smsDate);
		this.setFirstDateOfMonth(firstDateOfMonth);

		ExpenseData data = new ExpenseData();
		data.setNumber("Carry Forward");
		data.setCategory(AppConstants.CATEGORY.CARRY_FORWARD.toString());
		data.setDate(AppUtil.convertDateToString(firstDateOfMonth));
		data.setAmount(this.getCarryForwardAmount().toString());
		data.setAccountingType(AppConstants.ACCOUNTING_TYPE_CREDIT);
		this.add(data);
	}
	public void add(ExpenseData data){
		
		if(this.smsList == null){
			
			this.smsList = new ArrayList<ExpenseData>();
			this.smsList.add(data);
		}
		else {
			this.smsList.add(data);
		}
	}
	
	public String getAmountByTransactionType(String transactionType){
		
		Double amount = 0D;
		for(ExpenseData smsData : this.smsList){
			
			if(transactionType.equalsIgnoreCase(smsData.getTransactionType())){
				
				amount = amount + Double.valueOf(smsData.getAmount());
			}
		}
		return amount.toString();
	}
	
	public String getAmountByAccountingType(String accountingType){
		
		Double amount = 0D;
		for(ExpenseData smsData : this.smsList){
			
			if(accountingType.equalsIgnoreCase(smsData.getAccountingType())){
				
				amount = amount + Double.valueOf(smsData.getAmount());
			}
		}
		return amount.toString();
	}
	
	public List<WeeklyExpenseData> getWeeklyExpenseData(){
		
		List<WeeklyExpenseData> weeklySmsList = new ArrayList<WeeklyExpenseData>();
		
		for(ExpenseData data: this.smsList){

			//Getting week number for date
			Date date = AppUtil.getDateFromString(data.getDate(), "MM/dd/yy HH:mm");
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int weekNbr = cal.get(Calendar.WEEK_OF_MONTH);
			WeeklyExpenseData weekData = this.getWeeklyDataFromList(weekNbr, weeklySmsList);
			if(weekData != null )
			{
				
				if(AppConstants.ACCOUNTING_TYPE_DEBIT.equalsIgnoreCase(data.getAccountingType()))
					weekData.setDebitAmount(weekData.getDebitAmount()+Double.valueOf(data.getAmount()));
				if(AppConstants.ACCOUNTING_TYPE_CREDIT.equalsIgnoreCase(data.getAccountingType()))
					weekData.setCreditAmount(weekData.getCreditAmount()+Double.valueOf(data.getAmount()));
				weekData.add(data);
				
			}
			else{
				
				weekData = new WeeklyExpenseData();
				weekData.setWeek("W"+weekNbr);
				if(AppConstants.ACCOUNTING_TYPE_DEBIT.equalsIgnoreCase(data.getAccountingType())){
					weekData.setDebitAmount(Double.valueOf(data.getAmount()));
					weekData.setCreditAmount(0D);
				}
				if(AppConstants.ACCOUNTING_TYPE_CREDIT.equalsIgnoreCase(data.getAccountingType())){
					weekData.setCreditAmount(Double.valueOf(data.getAmount()));
					weekData.setDebitAmount(0D);
				}
				weekData.add(data);
				weeklySmsList.add(weekData);
			}
		}
		return weeklySmsList;
	}
	
	public WeeklyExpenseData getWeeklyDataFromList(int week, List<WeeklyExpenseData> weeklySmsList){
		
		for(WeeklyExpenseData data : weeklySmsList){
			if(data.getWeek().equalsIgnoreCase("W"+week))
				return data;
		}
		return null;
	}
	
	public Map<String, String> getCategorizedDebitAmtFromTranData(){
		
		Map<String, String> expenseByCategory = new HashMap<String, String>();
		
		for(ExpenseData data : this.smsList){
			
			if(AppConstants.ACCOUNTING_TYPE_DEBIT.equalsIgnoreCase(data.getAccountingType())){
				
				if(expenseByCategory.containsKey(data.getCategory())){
					Double expenseAmt = Double.valueOf(expenseByCategory.get(data.getCategory()))+Double.valueOf(data.getAmount());
					expenseByCategory.put(data.getCategory(), expenseAmt.toString());
				}
				else{
					expenseByCategory.put(data.getCategory(), data.getAmount());
				}
			}
		}
		return expenseByCategory;
	}
	
	
}
