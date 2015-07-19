/**
 * 
 */
package com.main.expensetracker.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author MaRoy
 *
 */
public class ExpenseData implements Parcelable{

	// Number from witch the sms was send
	private String number;
	// SMS text body
	private String amount;
	
	private String accountingType;
	
	private String transactionType;
	
	private String date;
	
	private String category;
	
	public String getNumber() {
		return number;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	
	public String getAmount() {
		return amount;
	}
	
	public void setAmount(String amount) {
		this.amount = amount;
	}

	/**
	 * @return the transactionType
	 */
	public String getAccountingType() {
		return accountingType;
	}

	/**
	 * @param transactionType the transactionType to set
	 */
	public void setAccountingType(String accountingType) {
		this.accountingType = accountingType;
	}

	
	/**
	 * @return the transactionType
	 */
	public String getTransactionType() {
		return transactionType;
	}

	/**
	 * @param transactionType the transactionType to set
	 */
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	public ExpenseData() {

	}
	public ExpenseData(String number, String amount, String date) {
		this.number = number;
		this.amount = amount;
		this.date = date;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(number);
		dest.writeString(amount);
		dest.writeString(accountingType);
		dest.writeString(transactionType);
		dest.writeString(date);
		dest.writeString(category);
	}
	
	public static final Parcelable.Creator<ExpenseData> CREATOR = new Parcelable.Creator<ExpenseData>() { 
		public ExpenseData createFromParcel(Parcel in) {
			ExpenseData smsData = new ExpenseData();
			smsData.number = in.readString();
			smsData.amount = in.readString();
			smsData.accountingType = in.readString();
			smsData.transactionType = in.readString();
			smsData.date = in.readString();
			smsData.category = in.readString();
			return smsData;
		}

		@Override
		public ExpenseData[] newArray(int size) {
			return new ExpenseData[size];
		}
	};
	
}
