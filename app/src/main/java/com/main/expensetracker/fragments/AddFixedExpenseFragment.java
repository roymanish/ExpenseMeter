/**
 * 
 */
package com.main.expensetracker.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.main.expensetracker.IExpenseDataProcessor;
import com.main.expensetracker.activities.R;
import com.main.expensetracker.constants.AppConstants;
import com.main.expensetracker.data.UserDefinedExpenseData;
import com.main.expensetracker.processor.ExpenseDataProcessor;
import com.main.expensetracker.utility.AppUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author MaRoy
 *
 */
public class AddFixedExpenseFragment extends Fragment implements OnClickListener{

	 @Override
	    public View onCreateView(LayoutInflater inflater,
	            ViewGroup container, Bundle savedInstanceState) {
	        // The last two arguments ensure LayoutParams are inflated
	        // properly.
	        View rootView = inflater.inflate(
	                R.layout.fragment_add_fixed_expense, container, false);
	        Button myButton = (Button) rootView.findViewById(R.id.saveButton);
	        myButton.setOnClickListener(this);
	        
	        Spinner categorySpinner = (Spinner)rootView.findViewById(R.id.categorySpinner);
			categorySpinner.setAdapter(getSpinnerAdapter());
			
	        return rootView;
	    }

	@Override
	public void onClick(View view) {

		EditText key = (EditText) view.getRootView().findViewById(R.id.expNameValue);
		EditText value = (EditText) view.getRootView().findViewById(R.id.expAmtValue);
		Spinner categorySpinner = (Spinner)view.getRootView().findViewById(R.id.categorySpinner);
		String expenseCat = (String)categorySpinner.getSelectedItem();

		if(key.getText().length() != 0 && value.getText().length() != 0){
			String expenseName = key.getText().toString();
			Float expenseValue = Float.valueOf(value.getText().toString());
			UserDefinedExpenseData expenseData = new UserDefinedExpenseData(expenseName, expenseValue, null, AppConstants.EXPENSE_TYPE_FIXED, expenseCat, AppConstants.ACCOUNTING_TYPE_DEBIT);
			
			//Save data to file
			IExpenseDataProcessor dataProcessor = ExpenseDataProcessor.getInstance();
			dataProcessor.saveDataToFile(getString(R.string.preference_file_key_Fixed), expenseName, dataProcessor.getGson().toJson(expenseData), getActivity());
			
			//Return to main page
			getActivity().onBackPressed();
		}
		else{
			Toast.makeText(getActivity(), "Please Enter Name/Amount!", Toast.LENGTH_LONG).show();
		}
		
	}
	
	public ArrayAdapter<String> getSpinnerAdapter(){

		List<String> categoryList = new ArrayList<String>();
		List<AppConstants.CATEGORY> list = Arrays.asList(AppConstants.CATEGORY.values());
		
		for(AppConstants.CATEGORY c : list){
			categoryList.add(c.toString());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
				android.R.layout.simple_spinner_dropdown_item, categoryList);
		return adapter;
	}

}
