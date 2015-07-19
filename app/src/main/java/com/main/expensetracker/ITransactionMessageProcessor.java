/**
 * 
 */
package com.main.expensetracker;

import android.content.Context;
import android.database.Cursor;

import com.main.expensetracker.data.MonthWiseExpenseData;

import java.util.List;

/**
 * @author MaRoy
 *
 */
public interface ITransactionMessageProcessor {

	//public List<MonthWiseExpenseData> processTransactionMessages(Cursor c, Context ctx, boolean isCursorValid, boolean isReloadFromDisk);
	public List<MonthWiseExpenseData> processTransactionMessagesNew(Cursor c, Context ctx, boolean isFirstCall);
}
