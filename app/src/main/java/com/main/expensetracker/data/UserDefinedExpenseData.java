/**
 * 
 */
package com.main.expensetracker.data;

import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

import com.main.expensetracker.utility.AppUtil;

/**
 * @author MaRoy
 *
 */
public class UserDefinedExpenseData{

	
	private String expenseName;

	private Float expenseValue;

	private Date dateOfExpense;
	
	private String expenseType;

	private String expenseCategory;
	
	private String accountingType;

	public UserDefinedExpenseData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserDefinedExpenseData(String expenseName, Float expenseValue,
			Date dateOfExpense, String expenseType, String expenseCategory, String accountingType) {
		super();
		this.expenseName = expenseName;
		this.expenseValue = expenseValue;
		this.dateOfExpense = dateOfExpense;
		this.expenseType = expenseType;
		this.expenseCategory = expenseCategory;
		this.accountingType = accountingType;
	}

	/**
	 * @return the expenseName
	 */
	public String getExpenseName() {
		return expenseName;
	}

	/**
	 * @param expenseName the expenseName to set
	 */
	public void setExpenseName(String expenseName) {
		this.expenseName = expenseName;
	}

	/**
	 * @return the expenseValue
	 */
	public Float getExpenseValue() {
		return expenseValue;
	}

	/**
	 * @param expenseValue the expenseValue to set
	 */
	public void setExpenseValue(Float expenseValue) {
		this.expenseValue = expenseValue;
	}

	/**
	 * @return the dataOfExpense
	 */
	public Date getDateOfExpense() {
		return dateOfExpense;
	}

	/**
	 * @param dataOfExpense the dataOfExpense to set
	 */
	public void setDateOfExpense(Date dateOfExpense) {
		this.dateOfExpense = dateOfExpense;
	}
	
	/**
	 * @return the expenseType
	 */
	public String getExpenseType() {
		return expenseType;
	}

	/**
	 * @param expenseType the expenseType to set
	 */
	public void setExpenseType(String expenseType) {
		this.expenseType = expenseType;
	}
	
	/**
	 * @return the expenseCategory
	 */
	public String getExpenseCategory() {
		return expenseCategory;
	}

	/**
	 * @param expenseCategory the expenseCategory to set
	 */
	public void setExpenseCategory(String expenseCategory) {
		this.expenseCategory = expenseCategory;
	}

	/**
	 * @return the accountingType
	 */
	public String getAccountingType() {
		return accountingType;
	}

	/**
	 * @param accountingType the accountingType to set
	 */
	public void setAccountingType(String accountingType) {
		this.accountingType = accountingType;
	}
	
	

}
