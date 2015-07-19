/**
 * 
 */
package com.main.expensetracker.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.main.expensetracker.constants.AppConstants;
import com.main.expensetracker.data.ExpenseData;
import com.main.expensetracker.utility.DatePickerDialogFragment;

/**
 * @author MaRoy
 *
 */
public class EditExpenseRecordActivity extends Activity{

	private Integer position;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_expense);
		
		Bundle i = this.getIntent().getExtras();
		position = (Integer)i.get(AppConstants.ITEM_POSITION);
		String amount = i.getString(AppConstants.EDIT_AMOUNT);
		String date = i.getString(AppConstants.EDIT_DATE);
		String category = i.getString(AppConstants.EDIT_EXPENSE_CATEGORY);
		
		EditText editAmountText = (EditText)findViewById(R.id.editAmountValue);
		editAmountText.setText(amount);
		
		EditText datePickerView = (EditText)findViewById(R.id.dateValue);
		if(date != null)
			datePickerView.setText(date);
		
		final Activity activity = this;
		datePickerView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				
				if(hasFocus){
					DialogFragment df = new DatePickerDialogFragment();
					df.show(activity.getFragmentManager(), "Date Picker");
				}
			}
		});
		
		Spinner categorySpinner = (Spinner)findViewById(R.id.editSpinnerCategory);
		ArrayAdapter<String> spinnerAdapter = getSpinnerAdapter();
		categorySpinner.setAdapter(spinnerAdapter);
		categorySpinner.setSelection(spinnerAdapter.getPosition(category));
	}
	
	public void onSaveEdit(View v){
		
		EditText amountView = (EditText)v.getRootView().findViewById(R.id.editAmountValue);
		String editAmount = amountView.getText().toString();
		
		EditText dateView = (EditText)v.getRootView().findViewById(R.id.dateValue);
		String editDate = dateView.getText().toString();
		
		Spinner editSpinner = (Spinner)v.getRootView().findViewById(R.id.editSpinnerCategory);
		String expenseCategory = (String)editSpinner.getSelectedItem();
		
		Intent intent=new Intent();
		intent.putExtra(AppConstants.EDIT_AMOUNT, editAmount);
		intent.putExtra(AppConstants.EDIT_DATE, editDate);
		intent.putExtra(AppConstants.EDIT_EXPENSE_CATEGORY, expenseCategory);
		intent.putExtra(AppConstants.ITEM_POSITION, position.toString());
		setResult(4, intent);
		finish();
	}
	
	public void onCancelEdit(View v){
		
		EditExpenseRecordActivity.this.finish();
	}
	
	public ArrayAdapter<String> getSpinnerAdapter(){

		List<String> categoryList = new ArrayList<String>();
		List<AppConstants.CATEGORY> list = Arrays.asList(AppConstants.CATEGORY.values());
		
		for(AppConstants.CATEGORY c : list){
			categoryList.add(c.toString());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, categoryList);
		return adapter;
	}
}
