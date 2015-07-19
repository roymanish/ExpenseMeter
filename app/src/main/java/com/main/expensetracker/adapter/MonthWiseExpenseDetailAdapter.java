/**
 * 
 */
package com.main.expensetracker.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.main.expensetracker.activities.EditExpenseRecordActivity;
import com.main.expensetracker.activities.R;
import com.main.expensetracker.constants.AppConstants;
import com.main.expensetracker.data.ExpenseData;
import com.main.expensetracker.processor.ExpenseDataProcessor;
import com.main.expensetracker.utility.AppUtil;

/**
 * @author MaRoy
 *
 */
public class MonthWiseExpenseDetailAdapter extends ArrayAdapter<ExpenseData>{

	// List context
	private final Context context;
	// List values
	private final ArrayList<ExpenseData> smsDataList;

	public MonthWiseExpenseDetailAdapter(Context context, ArrayList<ExpenseData> smsDataList) {
		super(context, R.layout.activity_monthly_expense_detail, smsDataList);
		this.context = context;
		this.smsDataList = smsDataList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final int pos = position;

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		final View rowView = inflater.inflate(R.layout.activity_monthly_expense_detail, parent, false);

		TextView amountView = (TextView) rowView.findViewById(R.id.amount);
		amountView.setText(getSmsList().get(position).getAmount().toString());

		TextView transactionTypeView = (TextView) rowView.findViewById(R.id.transactionTypeText);
		transactionTypeView.setText(getSmsList().get(position).getAccountingType()+":");

		TextView dateView = (TextView) rowView.findViewById(R.id.date);
		dateView.setText(getSmsList().get(position).getDate().toString());

		TextView iconTextView = (TextView)rowView.findViewById(R.id.icon);

		if(AppConstants.ACCOUNTING_TYPE_CREDIT.equalsIgnoreCase(getSmsList().get(position).getAccountingType()))
			iconTextView.setText("C");
		else if(AppConstants.ACCOUNTING_TYPE_DEBIT.equalsIgnoreCase(getSmsList().get(position).getAccountingType()))
			iconTextView.setText("D");

		TextView iconView = (TextView)rowView.findViewById(R.id.iconBckgrnd);
		iconView.setBackground(AppUtil.randomColorPicker(context, position));
		
		TextView expenseCatView = (TextView)rowView.findViewById(R.id.expensCategory);
		expenseCatView.setText(getSmsList().get(position).getCategory());

		final ImageView editButton = (ImageView)rowView.findViewById(R.id.editButton);

		editButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				ExpenseData data  = getSmsList().get(pos);
				((View) v.getParent()).setBackgroundColor(Color.parseColor("#42b4e6"));
				Intent i = new Intent(context, EditExpenseRecordActivity.class);
				i.putExtra(AppConstants.EDIT_AMOUNT, data.getAmount().toString());
				i.putExtra(AppConstants.EDIT_DATE, data.getDate());
				i.putExtra(AppConstants.EDIT_EXPENSE_CATEGORY, data.getCategory());
				i.putExtra(AppConstants.ITEM_POSITION, pos);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				((Activity)context).startActivityForResult(i, 4);

			}
		});

		return rowView;
	}

	public void onActivityResult(int requestCode, Intent data){

		if(requestCode==4 && data != null)
		{  
			String editAmount = data.getStringExtra(AppConstants.EDIT_AMOUNT);
			String editDate = data.getStringExtra(AppConstants.EDIT_DATE);
			String category = data.getStringExtra(AppConstants.EDIT_EXPENSE_CATEGORY);
			Integer position = new Integer(data.getStringExtra(AppConstants.ITEM_POSITION));
			
			updateExpenseDetailsRecord(editAmount, editDate, category, position);
			
		} 

	}

	public ArrayList<ExpenseData> getSmsList(){
		return smsDataList;
	}

	public void updateExpenseDetailsRecord(String amount, String date, String category, int pos){

		ExpenseData data = getSmsList().get(pos);
		data.setAmount(amount);
		data.setDate(date);
		data.setCategory(category);
		ExpenseDataProcessor.getInstance().updateMonthlyDataInFile(getSmsList(), context);
		notifyDataSetChanged();
	}
}
