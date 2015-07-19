/**
 * 
 */
package com.main.expensetracker;

import android.content.Context;

import com.google.gson.Gson;
import com.main.expensetracker.data.ExpenseData;
import com.main.expensetracker.data.MonthWiseExpenseData;
import com.main.expensetracker.data.UserDefinedExpenseData;
import com.main.expensetracker.data.WeeklyExpenseData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author MaRoy
 *
 */
public interface IExpenseDataProcessor {

	public void saveDataToFile(String fileName, String key, String json, Context ctx);
	
	public String readDataFromFile(String fileName,String key,Context ctx);
	
	public void deleteDataFromFile(String fileName, Context ctx, String key);
	
	public void clearFileData(String fileName, Context ctx);
	
	public List<UserDefinedExpenseData> readUserDefinedDataFromFile(String fileName, Context ctx);
	
	public List<UserDefinedExpenseData> readUserDefinedDataFromFileForMonth(String fileName, Context ctx, String monthName);
	
	public List<UserDefinedExpenseData> readUserDefinedDataFromFileForWeek(String fileName, Context ctx, String weekNbr);
	
	public MonthWiseExpenseData readTransactionDataFromFileForMonth(String fileName,String key, Context ctx, String monthName);
	
	public List<UserDefinedExpenseData> readFixedExpenseDataFromFile(Context ctx);
	
	public List<UserDefinedExpenseData> readVariableExpenseDataFromFileForMonth(Context ctx, String monthName);
	
	public Double getTotalOfAllFixedExpenses(Context ctx);
	
	public Double getTotalOfAllVariableExpenseForMonthByAccountingType(Context ctx, String monthName, String accountingType);
	
	public Double getTotalOfAllVariableExpenseForWeekByAccountingType(Context ctx, String weekNbr, String accountingType);
	
	public Map<String, String> getCategorizedMonthlyExpenses(MonthWiseExpenseData monthWiseData, Context context);
	
	public Map<String, String> getCategorizedWeeklyExpenses(WeeklyExpenseData weeklyData, Context context);
	
	public void updateMonthlyDataInFile(ArrayList<ExpenseData> smsList, Context ctx);
	
	public void updateWeeklyDataInFile(ArrayList<ExpenseData> smsList, Context ctx);
	
	public Gson getGson();
	
	public List<MonthWiseExpenseData> getMonthWiseList(Context ctx);
	
	public void setMonthWiseList(List<MonthWiseExpenseData> monthWiseList);

    public String readAssetContentAsString(Context ctx,String filename);
	
	public void saveDataInFile();
	
	public void saveDataInDB();
}
