/**
 * 
 */
package com.main.expensetracker.processor;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.main.expensetracker.IExpenseDataProcessor;
import com.main.expensetracker.activities.R;
import com.main.expensetracker.constants.AppConstants;
import com.main.expensetracker.data.ExpenseData;
import com.main.expensetracker.data.MonthWiseExpenseData;
import com.main.expensetracker.data.UserDefinedExpenseData;
import com.main.expensetracker.data.WeeklyExpenseData;
import com.main.expensetracker.utility.AppUtil;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** A Singleton data processor for all the processing related to data in local storage files.
 * @author MaRoy
 *
 */
public class ExpenseDataProcessor implements IExpenseDataProcessor{

	private static ExpenseDataProcessor dataProcessor;
	private Gson gson;
	private List<MonthWiseExpenseData> monthWiseList;

	private ExpenseDataProcessor(){
		gson = new Gson();
	}

	public static ExpenseDataProcessor getInstance(){

		if(dataProcessor == null)
			dataProcessor = new ExpenseDataProcessor();
		return dataProcessor;
	}

	/**
	 * @return the gson
	 */
	public Gson getGson() {
		return gson;
	}


	/**
	 * @return the monthWiseList
	 */
	public List<MonthWiseExpenseData> getMonthWiseList(Context ctx) {

		String tranExpenseListJson = dataProcessor.readDataFromFile(AppConstants.TRANSACTION_STORAGE_FILE, AppConstants.MONTHLY_LIST, ctx);
		Type type = new TypeToken<List<MonthWiseExpenseData>>(){}.getType();
		List<MonthWiseExpenseData> monthWiselist = dataProcessor.getGson().fromJson(tranExpenseListJson, type);
		this.monthWiseList = monthWiselist == null ? new ArrayList<MonthWiseExpenseData>() : monthWiselist;

		return monthWiseList;
	}

	/**
	 * @param monthWiseList the monthWiseList to set
	 */
	public void setMonthWiseList(List<MonthWiseExpenseData> monthWiseList) {
		this.monthWiseList = monthWiseList;
	}

	/* Saves json data to the mentioned file in local storage. It expects a key to save data so that it is easy to retrieve data from the file.
	 * (non-Javadoc)
	 * @see com.main.expensetracker.IExpenseDataProcessor#saveDataToFile(java.lang.String, java.lang.String, java.lang.String, android.content.Context)
	 */
	@Override
	public void saveDataToFile(String fileName, String key, String json, Context ctx) {

		SharedPreferences sharedPref = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		
		if(sharedPref.contains(key)){
			editor.remove(key);
			editor.putString(key, json);
		}
		else{
			editor.putString(key, json);
		}
		editor.commit();

	}

	/* Reads data from specified file based on given key. If key is null then it will fetch all the data from the file.
	 * (non-Javadoc)
	 * @see com.main.expensetracker.IExpenseDataProcessor#readDataFromFile(java.lang.String, java.lang.String, android.content.Context)
	 */
	@Override
	public String readDataFromFile(String fileName,String key,Context ctx) {

		SharedPreferences sharedPref = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		String jsonString = "";

		if(key == null || key.isEmpty()){
			Map<String,String> allPreferences = (Map<String, String>) sharedPref.getAll();
			jsonString = allPreferences.values().toString();
		}
		else{
			jsonString = sharedPref.getString(key, "");
		}
		return jsonString;
	}

	/* Deletes data from file for the given key.
	 * (non-Javadoc)
	 * @see com.main.expensetracker.IExpenseDataProcessor#deleteDataFromFile(java.lang.String, android.content.Context, java.lang.String)
	 */
	public void deleteDataFromFile(String fileName, Context ctx, String key){

		SharedPreferences sharedPref = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = sharedPref.edit();
		editor.remove(key);
		editor.commit();
	}

	/* Delete all data from file
	 * (non-Javadoc)
	 * @see com.main.expensetracker.IExpenseDataProcessor#clearFileData(java.lang.String, android.content.Context)
	 */
	public void clearFileData(String fileName, Context ctx){

		SharedPreferences sharedPref = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = sharedPref.edit();
		editor.clear();
		editor.commit();
	}

	/* Reads user created data from the specified file.
	 * (non-Javadoc)
	 * @see com.main.expensetracker.IExpenseDataProcessor#readUserDefinedDataFromFile(java.lang.String, android.content.Context)
	 */
	public List<UserDefinedExpenseData> readUserDefinedDataFromFile(String fileName, Context ctx){

		String listJson = this.readDataFromFile(fileName,null, ctx);
		Type type = new TypeToken<List<UserDefinedExpenseData>>(){}.getType();
		List<UserDefinedExpenseData> expenseDataList = this.getGson().fromJson(listJson, type);
		return expenseDataList;
	}

	/* Reads user created data from the specified file for a given month.
	 * (non-Javadoc)
	 * @see com.main.expensetracker.IExpenseDataProcessor#readUserDefinedDataFromFileForMonth(java.lang.String, android.content.Context, java.lang.String)
	 */
	public List<UserDefinedExpenseData> readUserDefinedDataFromFileForMonth(String fileName, Context ctx, String monthName){

		List<UserDefinedExpenseData> userDefinedExpenseDataList = new ArrayList<UserDefinedExpenseData>();

		List<UserDefinedExpenseData> expenseDataList = this.readUserDefinedDataFromFile(fileName, ctx);

		if(monthName != null && !monthName.isEmpty()){
			for(UserDefinedExpenseData ude : expenseDataList){

				if(monthName.equalsIgnoreCase(AppUtil.getMonthFromDate(ude.getDateOfExpense(), AppConstants.MONTH_NAME_SHORT)))
					userDefinedExpenseDataList.add(ude);
			}
		}
		else{
			userDefinedExpenseDataList.addAll(expenseDataList);
		}


		return userDefinedExpenseDataList;
	}

	/* Reads user created data from the specified file for a given week.
	 * (non-Javadoc)
	 * @see com.main.expensetracker.IExpenseDataProcessor#readUserDefinedDataFromFileForWeek(java.lang.String, android.content.Context, java.lang.String)
	 */
	public List<UserDefinedExpenseData> readUserDefinedDataFromFileForWeek(String fileName, Context ctx, String weekNbr){

		List<UserDefinedExpenseData> variableExpenseDataList = new ArrayList<UserDefinedExpenseData>();

		List<UserDefinedExpenseData> expenseDataList = this.readUserDefinedDataFromFile(fileName, ctx);

		if(weekNbr != null && !weekNbr.isEmpty()){
			for(UserDefinedExpenseData ude : expenseDataList){

				if(weekNbr.equalsIgnoreCase(AppUtil.getWeekFromDate(ude.getDateOfExpense())))
					variableExpenseDataList.add(ude);
			}
		}
		else{
			variableExpenseDataList.addAll(expenseDataList);
		}
		return variableExpenseDataList;
	}

	/* Reads transaction data(Data pulled from sms) from file for a given month.
	 * (non-Javadoc)
	 * @see com.main.expensetracker.IExpenseDataProcessor#readTransactionDataFromFileForMonth(java.lang.String, java.lang.String, android.content.Context, java.lang.String)
	 */
	public MonthWiseExpenseData readTransactionDataFromFileForMonth(String fileName,String key, Context ctx, String monthName,String year){


		String listJson = this.readDataFromFile(fileName,key, ctx);
		Type type = new TypeToken<List<MonthWiseExpenseData>>(){}.getType();
		List<MonthWiseExpenseData> monthwiseList = this.getGson().fromJson(listJson, type);

        if(monthwiseList != null && !monthwiseList.isEmpty()) {
            for (MonthWiseExpenseData data : monthwiseList) {

                if (monthName.equalsIgnoreCase(data.getMonth()) && year.equalsIgnoreCase(AppUtil.getYearFromDate(data.getFirstDateOfMonth())))
                    return data;
            }
        }
		return null;
	}

	/* Reads user defined data from file which are fixed for all the months.
	 * (non-Javadoc)
	 * @see com.main.expensetracker.IExpenseDataProcessor#readFixedExpenseDataFromFile(android.content.Context)
	 */
	public List<UserDefinedExpenseData> readFixedExpenseDataFromFile(Context ctx){

		List<UserDefinedExpenseData> fixedExpenseList = this.readUserDefinedDataFromFile(ctx.getString(R.string.preference_file_key_Fixed), ctx);

		return fixedExpenseList;
	}

	/* Reads user defined data from file which is specific to the given month.
	 * (non-Javadoc)
	 * @see com.main.expensetracker.IExpenseDataProcessor#readVariableExpenseDataFromFileForMonth(android.content.Context, java.lang.String)
	 */
	public List<UserDefinedExpenseData> readVariableExpenseDataFromFileForMonth(Context ctx, String monthName){

		List<UserDefinedExpenseData> expenseListToReturn = new ArrayList<UserDefinedExpenseData>();
		List<UserDefinedExpenseData> variableExpenseList = this.readUserDefinedDataFromFile(ctx.getString(R.string.preference_file_key_Variable), ctx);

		for(UserDefinedExpenseData data : variableExpenseList){

			if(monthName.equalsIgnoreCase(AppUtil.getMonthFromDate(data.getDateOfExpense(), AppConstants.MONTH_NAME_SHORT)))
				expenseListToReturn.add(data);
		}
		return expenseListToReturn;
	}

	/* Returns sum of all the fixed expenses.
	 * (non-Javadoc)
	 * @see com.main.expensetracker.IExpenseDataProcessor#getTotalOfAllFixedExpenses(android.content.Context)
	 */
	public Double getTotalOfAllFixedExpenses(Context ctx){

		Double total = 0D;
		List<UserDefinedExpenseData> fixedExpenseList = this.readFixedExpenseDataFromFile(ctx);

		for(UserDefinedExpenseData data : fixedExpenseList){

			total = total + data.getExpenseValue();
		}
		return total;
	}

	/* Returns sum of all the variable expenses for a given month and accounting type.
	 * (non-Javadoc)
	 * @see com.main.expensetracker.IExpenseDataProcessor#getTotalOfAllVariableExpenseForMonthByAccountingType(android.content.Context, java.lang.String, java.lang.String)
	 */
	public Double getTotalOfAllVariableExpenseForMonthByAccountingType(Context ctx, String monthName, String accountingType){

		Double total = 0D;
		List<UserDefinedExpenseData> expenseList = this.readVariableExpenseDataFromFileForMonth(ctx, monthName);

		for(UserDefinedExpenseData data : expenseList){

			if(accountingType.equalsIgnoreCase(data.getAccountingType()))
				total = total + data.getExpenseValue();
		}
		return total;
	}

	/* Returns sum of all the variable expenses for a given week and accounting type.
	 * (non-Javadoc)
	 * @see com.main.expensetracker.IExpenseDataProcessor#getTotalOfAllVariableExpenseForWeekByAccountingType(android.content.Context, java.lang.String, java.lang.String)
	 */
	public Double getTotalOfAllVariableExpenseForWeekByAccountingType(Context ctx, String weekNbr, String accountingType){

		Double total = 0D;
		List<UserDefinedExpenseData> expenseList = this.readUserDefinedDataFromFileForWeek(ctx.getString(R.string.preference_file_key_Variable), ctx, weekNbr);

		for(UserDefinedExpenseData data : expenseList){

			if(accountingType.equalsIgnoreCase(data.getAccountingType()))
				total = total + data.getExpenseValue();
		}
		return total;
	}

	/** Creates a categorized map of all types of expense for a month.Used to create charts.
	 * @param monthWiseData
	 * @param context
	 * @return
	 */
	public Map<String, String> getCategorizedMonthlyExpenses(MonthWiseExpenseData monthWiseData, Context context){

		Map<String, String> expenseByCategory = monthWiseData.getCategorizedDebitAmtFromTranData();

		//Get categorized Fixed Expense
		List<UserDefinedExpenseData> fixedExpenseList = dataProcessor.readFixedExpenseDataFromFile(context);

		for(UserDefinedExpenseData data : fixedExpenseList){

			if(expenseByCategory.containsKey(data.getExpenseCategory())){

				Double expenseAmount = Double.valueOf(expenseByCategory.get(data.getExpenseCategory()))+data.getExpenseValue();
				expenseByCategory.put(data.getExpenseCategory(), expenseAmount.toString());
			}
			else{
				expenseByCategory.put(data.getExpenseCategory(), data.getExpenseValue().toString());
			}
		}

		//Get categorized variable expense
		List<UserDefinedExpenseData> variableExpenseList = dataProcessor.readVariableExpenseDataFromFileForMonth(context, monthWiseData.getMonth());

		for(UserDefinedExpenseData data : variableExpenseList){

			if(AppConstants.ACCOUNTING_TYPE_DEBIT.equalsIgnoreCase(data.getAccountingType())){
				if(expenseByCategory.containsKey(data.getExpenseCategory())){

					Double expenseAmount = Double.valueOf(expenseByCategory.get(data.getExpenseCategory()))+data.getExpenseValue();
					expenseByCategory.put(data.getExpenseCategory(), expenseAmount.toString());
				}
				else{
					expenseByCategory.put(data.getExpenseCategory(), data.getExpenseValue().toString());
				}
			}
		}
		return expenseByCategory;
	}

	/** Creates a categorized map of all types of expense for a month.Used to create charts.
	 * @param weeklyData
	 * @param context
	 * @return
	 */
	public Map<String, String> getCategorizedWeeklyExpenses(WeeklyExpenseData weeklyData, Context context){

		Map<String, String> expenseByCategory = weeklyData.getCategorizedDebitAmtFromTranData();

		//Get categorized Fixed Expense
		if("W1".equalsIgnoreCase(weeklyData.getWeek())){
			List<UserDefinedExpenseData> fixedExpenseList = dataProcessor.readFixedExpenseDataFromFile(context);

			for(UserDefinedExpenseData data : fixedExpenseList){

				if(expenseByCategory.containsKey(data.getExpenseCategory())){

					Double expenseAmount = Double.valueOf(expenseByCategory.get(data.getExpenseCategory()))+data.getExpenseValue();
					expenseByCategory.put(data.getExpenseCategory(), expenseAmount.toString());
				}
				else{
					expenseByCategory.put(data.getExpenseCategory(), data.getExpenseValue().toString());
				}
			}
		}

		//Get categorized variable expense
		List<UserDefinedExpenseData> variableExpenseList = dataProcessor.readUserDefinedDataFromFileForWeek(context.getString(R.string.preference_file_key_Variable), context, weeklyData.getWeek());

		for(UserDefinedExpenseData data : variableExpenseList){

			if(AppConstants.ACCOUNTING_TYPE_DEBIT.equalsIgnoreCase(data.getAccountingType())){
				if(expenseByCategory.containsKey(data.getExpenseCategory())){

					Double expenseAmount = Double.valueOf(expenseByCategory.get(data.getExpenseCategory()))+data.getExpenseValue();
					expenseByCategory.put(data.getExpenseCategory(), expenseAmount.toString());
				}
				else{
					expenseByCategory.put(data.getExpenseCategory(), data.getExpenseValue().toString());
				}
			}
		}
		return expenseByCategory;
	}

	/* Updates data for given month to the file.
	 * (non-Javadoc)
	 * @see com.main.expensetracker.IExpenseDataProcessor#updateMonthlyDataInFile(java.util.ArrayList, android.content.Context)
	 */
	public void updateMonthlyDataInFile(ArrayList<ExpenseData> smsList, Context ctx){

		this.getMonthWiseList(ctx);

		String monthName = AppUtil.getMonthFromStringDate(smsList.get(0).getDate(), AppConstants.MONTH_NAME_SHORT);

		Double creditAmount = 0D;
		Double debitAmount = 0D;
		for(MonthWiseExpenseData data : this.monthWiseList){

			if(monthName.equalsIgnoreCase(data.getMonth())){
				data.setSmsList(smsList);
				for(ExpenseData sms : smsList){

					if(AppConstants.ACCOUNTING_TYPE_CREDIT.equalsIgnoreCase(sms.getAccountingType())
							&& !AppConstants.CATEGORY.CARRY_FORWARD.toString().equalsIgnoreCase(sms.getCategory()))
						creditAmount = creditAmount + Double.valueOf(sms.getAmount());
					else if(AppConstants.ACCOUNTING_TYPE_DEBIT.equalsIgnoreCase(sms.getAccountingType()))
						debitAmount = debitAmount + Double.valueOf(sms.getAmount());
				}
				data.setCreditAmount(creditAmount);
				data.setDebitAmount(debitAmount);
				this.saveDataToFile(AppConstants.TRANSACTION_STORAGE_FILE, AppConstants.MONTHLY_LIST, dataProcessor.getGson().toJson(this.monthWiseList), ctx);
			}
		}

	}

	/* Updates data for given week to the file.
	 * (non-Javadoc)
	 * @see com.main.expensetracker.IExpenseDataProcessor#updateWeeklyDataInFile(java.util.ArrayList, android.content.Context)
	 */
	public void updateWeeklyDataInFile(ArrayList<ExpenseData> smsList, Context ctx){

		if(this.monthWiseList == null){
			String tranExpenseListJson = dataProcessor.readDataFromFile(AppConstants.TRANSACTION_STORAGE_FILE, AppConstants.MONTHLY_LIST, ctx);
			Type type = new TypeToken<List<MonthWiseExpenseData>>(){}.getType();
			this.monthWiseList = dataProcessor.getGson().fromJson(tranExpenseListJson, type);
		}

		String monthName = AppUtil.getMonthFromStringDate(smsList.get(0).getDate(), AppConstants.MONTH_NAME_SHORT);

		ArrayList<ExpenseData> monthlySmsList = new ArrayList<ExpenseData>();
		Double creditAmount = 0D;
		Double debitAmount = 0D;
		for(MonthWiseExpenseData data : this.monthWiseList){

			if(monthName.equalsIgnoreCase(data.getMonth())){

				List<WeeklyExpenseData> weeklyList = data.getWeeklyExpenseData();

				for(WeeklyExpenseData weeklyData : weeklyList){


					if(AppUtil.getWeekFromStringDate(smsList.get(0).getDate()).equalsIgnoreCase(weeklyData.getWeek())){
						monthlySmsList.addAll(smsList);
					}
					else
						monthlySmsList.addAll(weeklyData.getSmsList());
				}

				data.setSmsList(monthlySmsList);
				for(ExpenseData sms : monthlySmsList){

					if(AppConstants.ACCOUNTING_TYPE_CREDIT.equalsIgnoreCase(sms.getAccountingType()))
						creditAmount = creditAmount + Double.valueOf(sms.getAmount());
					else
						debitAmount = debitAmount + Double.valueOf(sms.getAmount());
				}

				data.setCreditAmount(creditAmount);
				data.setDebitAmount(debitAmount);
				this.saveDataToFile(AppConstants.TRANSACTION_STORAGE_FILE, AppConstants.MONTHLY_LIST, dataProcessor.getGson().toJson(this.monthWiseList), ctx);
			}
		}

	}

    public String readAssetContentAsString(Context ctx,String filename){

        AssetManager assetManager = ctx.getAssets();
        InputStream input;
        String text = "";

        try {
            input = assetManager.open(filename);

            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();

            // byte buffer into a string
            text = new String(buffer);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return text;
    }
	@Override
	public void saveDataInFile() {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveDataInDB() {
		// TODO Auto-generated method stub

	}

}
