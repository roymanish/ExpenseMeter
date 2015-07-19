/**
 * 
 */
package com.main.expensetracker.utility;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import com.main.expensetracker.activities.R;

import java.util.Calendar;
import java.util.Date;

/**
 * @author MaRoy
 *
 */
public class DatePickerDialogFragment extends DialogFragment implements
DatePickerDialog.OnDateSetListener{

	private EditText dateValueView;
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		dateValueView = (EditText)getActivity().findViewById(R.id.dateValue);
		Date recordDate = AppUtil.getDateFromString(dateValueView.getText().toString(), "MM/dd/yy HH:mm");
		
		final Calendar c = Calendar.getInstance();
		recordDate = recordDate == null ? c.getTime() : recordDate;
		c.setTime(recordDate);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}
	@Override
	public void onDateSet(DatePicker view, int yr, int monthOfYear,
			int dayOfMonth) {

		//updateDate(yr, monthOfYear + 1, dayOfMonth);
		dateValueView.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + yr);

	}
	
}
