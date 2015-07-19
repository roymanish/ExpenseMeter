/**
 * 
 */
package com.main.expensetracker.adapter;

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

import com.main.expensetracker.IExpenseDataProcessor;
import com.main.expensetracker.activities.EditExpenseRecordActivity;
import com.main.expensetracker.activities.R;
import com.main.expensetracker.constants.AppConstants;
import com.main.expensetracker.data.UserDefinedExpenseData;
import com.main.expensetracker.processor.ExpenseDataProcessor;
import com.main.expensetracker.utility.AppUtil;

import java.util.List;

/**
 * @author MaRoy
 *
 */
public class FixedExpenseDetailAdapter extends ArrayAdapter<UserDefinedExpenseData>{

	// List context
	private final Context context;
	// List values
	private final List<UserDefinedExpenseData> fixedExpenseList;

	public FixedExpenseDetailAdapter(Context context, List<UserDefinedExpenseData> fixedExpenseList) {
		super(context, R.layout.activity_fixed_expenses, fixedExpenseList);
		this.context = context;
		this.fixedExpenseList = fixedExpenseList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		final int pos = position;
		View rowView = inflater.inflate(R.layout.activity_fixed_expenses, parent, false);

		TextView expenseName = (TextView) rowView.findViewById(R.id.expenseName);
		expenseName.setText(getFixedExpenseList().get(position).getExpenseName());

		TextView expenseValue = (TextView) rowView.findViewById(R.id.expenseValue);
		expenseValue.setText(getFixedExpenseList().get(position).getExpenseValue().toString());

		TextView expenseType = (TextView)rowView.findViewById(R.id.expensType);
		expenseType.setText(getFixedExpenseList().get(position).getExpenseType());

		if(getFixedExpenseList().get(position).getExpenseCategory() != null){
			TextView expenseCategory = (TextView)rowView.findViewById(R.id.usrDefExpCategory);
			expenseCategory.setText(getFixedExpenseList().get(position).getExpenseCategory());
		}

		final ImageView editButton = (ImageView)rowView.findViewById(R.id.editButton);

		editButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				UserDefinedExpenseData data = getFixedExpenseList().get(pos);
				((View) v.getParent()).setBackgroundColor(Color.parseColor("#42b4e6"));
				Intent i = new Intent(context, EditExpenseRecordActivity.class);
				i.putExtra(AppConstants.EDIT_AMOUNT, data.getExpenseValue().toString());
				if(data.getDateOfExpense() != null)
					i.putExtra(AppConstants.EDIT_DATE, AppUtil.convertDateToString(data.getDateOfExpense()));
				i.putExtra(AppConstants.EDIT_EXPENSE_CATEGORY, data.getExpenseCategory());
				i.putExtra(AppConstants.ITEM_POSITION, pos);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				((Activity)context).startActivityForResult(i, 3);

			}
		});

		/*ImageView deleteExpense = (ImageView)rowView.findViewById(R.id.deleteExpense); 

        deleteExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

        	    String key = ((TextView)view.getRootView().findViewById(R.id.expenseName)).getText().toString();
        	    ExpenseDataProcessor.getInstance().deleteDataFromFile(getContext().getString(R.string.preference_file_key_Fixed), getContext(), key);
        	    remove(getItem(pos));
        	    notifyDataSetChanged();
            }
        });*/
		return rowView;
	}

	public List<UserDefinedExpenseData> getFixedExpenseList() {
		return fixedExpenseList;
	}

	public void onActivityResult(int requestCode, Intent data){

		if(requestCode==3 && data != null)
		{  
			String editAmount = data.getStringExtra(AppConstants.EDIT_AMOUNT);
			String editCategory = data.getStringExtra(AppConstants.EDIT_EXPENSE_CATEGORY);
			Integer position = new Integer(data.getStringExtra(AppConstants.ITEM_POSITION));

			updateFixedExpenseDetailsRecord(editAmount, editCategory, position);

		} 

	}

	public void updateFixedExpenseDetailsRecord(String amount, String expenseCategory,int pos){

		UserDefinedExpenseData data = getFixedExpenseList().get(pos);
		data.setExpenseValue(Float.valueOf(amount));
		data.setExpenseCategory(expenseCategory);

		//Save in file
		IExpenseDataProcessor dataProcessor = ExpenseDataProcessor.getInstance();
        String fileName = "";
        if(AppConstants.EXPENSE_TYPE_FIXED.equalsIgnoreCase(data.getExpenseType())){
            fileName = context.getString(R.string.preference_file_key_Fixed);
        }
        else
            fileName = context.getString(R.string.preference_file_key_Variable);

		dataProcessor.saveDataToFile(fileName, data.getExpenseName(), dataProcessor.getGson().toJson(data), context);

		//Refresh the screen
		notifyDataSetChanged();
	}
}
