/**
 * 
 */
package com.main.expensetracker.activities;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.main.expensetracker.adapter.WeeklyExpenseAdapter;
import com.main.expensetracker.constants.AppConstants;
import com.main.expensetracker.data.MonthWiseExpenseData;
import com.main.expensetracker.data.WeeklyExpenseData;
import com.main.expensetracker.processor.ExpenseDataProcessor;
import com.main.expensetracker.utility.AppUtil;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

/**
 * @author MaRoy
 *
 */
public class WeeklyExpenseActivity extends ListActivity implements OnItemSelectedListener{

	ExpenseDataProcessor dataProcessor;
	Spinner spinner;
	String monthName;
	MonthWiseExpenseData monthWiseData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_viewpager_fragment);

		monthName = AppUtil.getMonthFromDate(Calendar.getInstance().getTime(), AppConstants.MONTH_NAME_SHORT);
		this.populateAdapter();

	}
	
	/* (non-Javadoc)
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l,View v, int position, long id){

		Intent i = new Intent(getApplicationContext(), WeeklyExpenseDetailActivity.class);

		String weekNbr = ((TextView)v.findViewById(R.id.monthName)).getText().toString();
		SwingBottomInAnimationAdapter animationAdapter = (SwingBottomInAnimationAdapter)l.getAdapter();
		WeeklyExpenseAdapter baseAdapter = ((WeeklyExpenseAdapter)animationAdapter.getDecoratedBaseAdapter());
		Bundle bundle = new Bundle();
		bundle.putString(AppConstants.WEEK_NBR, weekNbr);
		bundle.putParcelableArrayList(AppConstants.WEEKLY_LIST, baseAdapter.getSmsListForWeek(weekNbr));
		i.putExtras(bundle);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_action_button, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		MenuItem item = menu.findItem(R.id.action_change_month);
		item.setVisible(true);
		spinner = (Spinner)item.getActionView();
		spinner.setAdapter(getSpinnerAdapterForMonthList());
		spinner.setOnItemSelectedListener(this);
		return true;
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(this, LandingPageActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		WeeklyExpenseActivity.this.finish();
	}

	public ArrayAdapter<String> getSpinnerAdapterForMonthList(){

		List<String> monthNameList = Arrays.asList(getResources().getStringArray(R.array.month_array));
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, monthNameList);
		return adapter;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		
		((TextView)parent.getChildAt(0)).setTextColor(Color.WHITE);
		spinner.setSelection(position);

		int selMonth = spinner.getSelectedItemPosition();
		Date anyDate;
		switch(selMonth){

		case 0:
			anyDate = this.getAnyDateOfMonth(Calendar.JANUARY);
			monthName = AppUtil.getMonthFromDate(anyDate, AppConstants.MONTH_NAME_SHORT);
			this.populateAdapter();
			break;
		case 1:
			anyDate = this.getAnyDateOfMonth(Calendar.FEBRUARY);
			monthName = AppUtil.getMonthFromDate(anyDate, AppConstants.MONTH_NAME_SHORT);
			this.populateAdapter();
			break;
		case 2:
			anyDate = this.getAnyDateOfMonth(Calendar.MARCH);
			monthName = AppUtil.getMonthFromDate(anyDate, AppConstants.MONTH_NAME_SHORT);
			this.populateAdapter();
			break;
		case 3:
			anyDate = this.getAnyDateOfMonth(Calendar.APRIL);
			monthName = AppUtil.getMonthFromDate(anyDate, AppConstants.MONTH_NAME_SHORT);
			this.populateAdapter();
			break;
		case 4:
			anyDate = this.getAnyDateOfMonth(Calendar.MAY);
			monthName = AppUtil.getMonthFromDate(anyDate, AppConstants.MONTH_NAME_SHORT);
			this.populateAdapter();
			break;
		case 5:
			anyDate = this.getAnyDateOfMonth(Calendar.JUNE);
			monthName = AppUtil.getMonthFromDate(anyDate, AppConstants.MONTH_NAME_SHORT);
			this.populateAdapter();
			break;
		case 6:
			anyDate = this.getAnyDateOfMonth(Calendar.JULY);
			monthName = AppUtil.getMonthFromDate(anyDate, AppConstants.MONTH_NAME_SHORT);
			this.populateAdapter();
			break;
		case 7:
			anyDate = this.getAnyDateOfMonth(Calendar.AUGUST);
			monthName = AppUtil.getMonthFromDate(anyDate, AppConstants.MONTH_NAME_SHORT);
			this.populateAdapter();
			break;
		case 8:
			anyDate = this.getAnyDateOfMonth(Calendar.SEPTEMBER);
			monthName = AppUtil.getMonthFromDate(anyDate, AppConstants.MONTH_NAME_SHORT);
			this.populateAdapter();
			break;
		case 9:
			anyDate = this.getAnyDateOfMonth(Calendar.OCTOBER);
			monthName = AppUtil.getMonthFromDate(anyDate, AppConstants.MONTH_NAME_SHORT);
			this.populateAdapter();
			break;
		case 10:
			anyDate = this.getAnyDateOfMonth(Calendar.NOVEMBER);
			monthName = AppUtil.getMonthFromDate(anyDate, AppConstants.MONTH_NAME_SHORT);
			this.populateAdapter();
			break;
		case 11:
			anyDate = this.getAnyDateOfMonth(Calendar.DECEMBER);
			monthName = AppUtil.getMonthFromDate(anyDate, AppConstants.MONTH_NAME_SHORT);
			this.populateAdapter();
			break;
		default :
			break;

		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

	private void populateAdapter(){

		dataProcessor = ExpenseDataProcessor.getInstance();
		monthWiseData = dataProcessor.readTransactionDataFromFileForMonth(AppConstants.TRANSACTION_STORAGE_FILE, AppConstants.MONTHLY_LIST, getApplicationContext(),monthName);

		if(monthWiseData == null){
			Toast.makeText(getApplicationContext(), "No data for "+monthName, Toast.LENGTH_SHORT).show();
			return;
		}
		List<WeeklyExpenseData> weeklyExpenseDataList = monthWiseData.getWeeklyExpenseData();
		
		ListView lv = getListView();

		WeeklyExpenseAdapter mAdapter = new WeeklyExpenseAdapter(this, weeklyExpenseDataList);
		SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(mAdapter);
		animationAdapter.setAbsListView(lv);
		lv.setAdapter(animationAdapter);
	}

	private Date getAnyDateOfMonth(int month){

		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, month);
		return c.getTime();
	}
}
