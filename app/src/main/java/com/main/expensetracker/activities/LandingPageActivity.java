/**
 * 
 */
package com.main.expensetracker.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * @author MaRoy
 *
 */
public class LandingPageActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.landing_page_menu);
	}
	
	public void onAddNewExpenseClick(View v){
		Intent i = new Intent(getApplicationContext(), AddNewExpenseActivity.class);
		startActivity(i);
	}
	
	public void onMonthlyAnalysisClick(View v){
		Intent i = new Intent(getApplicationContext(), MonthlyExpenseActivity.class);
		startActivity(i);
	}
	
	public void onWeeklyAnalysisClick(View v){
		Intent i = new Intent(getApplicationContext(), WeeklyExpenseActivity.class);
		startActivity(i);
	}
	
	@Override
	public void onBackPressed() {

		LandingPageActivity.this.finish();
	}
}
