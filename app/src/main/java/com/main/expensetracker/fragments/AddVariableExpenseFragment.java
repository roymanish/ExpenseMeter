/**
 * 
 */
package com.main.expensetracker.fragments;

import android.app.DialogFragment;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.main.expensetracker.IExpenseDataProcessor;
import com.main.expensetracker.activities.R;
import com.main.expensetracker.constants.AppConstants;
import com.main.expensetracker.data.UserDefinedExpenseData;
import com.main.expensetracker.processor.ExpenseDataProcessor;
import com.main.expensetracker.utility.AppUtil;
import com.main.expensetracker.utility.DatePickerDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author MaRoy
 *
 */
public class AddVariableExpenseFragment extends Fragment implements OnClickListener{

	static final int DATE_DIALOG_ID = 1;
	private EditText dateValueView;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// The last two arguments ensure LayoutParams are inflated
		// properly.
		View rootView = inflater.inflate(
				R.layout.fragment_add_variable_expense, container, false);
		
		Button myButton = (Button) rootView.findViewById(R.id.saveButton);
        myButton.setOnClickListener(this);

		TextView dateLabelView = (TextView)rootView.findViewById(R.id.dateLabel);
		dateValueView = (EditText)rootView.findViewById(R.id.dateValue);
		ImageView datePickerView = (ImageView)rootView.findViewById(R.id.datePicker);

		dateLabelView.setVisibility(View.VISIBLE);
		dateValueView.setVisibility(View.VISIBLE);
		datePickerView.setVisibility(View.VISIBLE);
		
		datePickerView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				DialogFragment df = new DatePickerDialogFragment();
				df.show(getActivity().getFragmentManager(), "Date Picker");
			}
		});

		Spinner categorySpinner = (Spinner)rootView.findViewById(R.id.categorySpinner);
		categorySpinner.setAdapter(getSpinnerAdapter());
		
		return rootView;
	}
	
	public void onClick(View view) {

		EditText key = (EditText) view.getRootView().findViewById(R.id.vExpNameValue);
		EditText value = (EditText) view.getRootView().findViewById(R.id.vExpAmtValue);
		EditText dateValue = (EditText) view.getRootView().findViewById(R.id.dateValue);
		
		Spinner categorySpinner = (Spinner)view.getRootView().findViewById(R.id.categorySpinner);
		String expenseCat = (String)categorySpinner.getSelectedItem();
		
		RadioGroup debitCreditRadio = (RadioGroup)view.getRootView().findViewById(R.id.radioAccountingType);
		int selectedId = debitCreditRadio.getCheckedRadioButtonId();
		RadioButton selectedRadio = (RadioButton)view.getRootView().findViewById(selectedId);
		String accountingType = (String)selectedRadio.getText();

		if(key.getText().length() != 0 && value.getText().length() != 0){
			
			String expenseName = key.getText().toString();
			Float expenseValue = Float.valueOf(value.getText().toString());
			Date expenseDate = AppUtil.getDateFromString(dateValue.getText().toString(), "MM/dd/yy");
			UserDefinedExpenseData expenseData = new UserDefinedExpenseData(expenseName, expenseValue, expenseDate, AppConstants.EXPENSE_TYPE_VARIABLE,expenseCat,accountingType);
			
			//Save data to file
			IExpenseDataProcessor dataProcessor = ExpenseDataProcessor.getInstance();
			dataProcessor.saveDataToFile(getString(R.string.preference_file_key_Variable), expenseName, dataProcessor.getGson().toJson(expenseData), getActivity());
			
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
