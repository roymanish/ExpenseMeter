/**
 * 
 */
package com.main.expensetracker.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.main.expensetracker.constants.AppConstants;

/**
 * @author MaRoy
 *
 */
public class WeeklyExpenseData {

	private String week;
	private Double debitAmount;
	private Double creditAmount;
	private ArrayList<ExpenseData> smsList;
	/**
	 * @return the week
	 */
	public String getWeek() {
		return week;
	}
	/**
	 * @param week the week to set
	 */
	public void setWeek(String week) {
		this.week = week;
	}
	/**
	 * @return the debitAmount
	 */
	public Double getDebitAmount() {
		return debitAmount;
	}
	/**
	 * @param debitAmount the debitAmount to set
	 */
	public void setDebitAmount(Double debitAmount) {
		this.debitAmount = debitAmount;
	}
	/**
	 * @return the creditAmount
	 */
	public Double getCreditAmount() {
		return creditAmount;
	}
	/**
	 * @param creditAmount the creditAmount to set
	 */
	public void setCreditAmount(Double creditAmount) {
		this.creditAmount = creditAmount;
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

	public void add(ExpenseData data){

		if(this.smsList == null)
			this.smsList = new ArrayList<ExpenseData>();
		this.smsList.add(data);
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
