/**
 * 
 */
package com.main.expensetracker.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.main.expensetracker.fragments.AddFixedExpenseFragment;
import com.main.expensetracker.fragments.AddVariableExpenseFragment;

/**
 * @author MaRoy
 *
 */
public class AddNewExpenseActivity extends FragmentActivity{

	ViewPager pager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewpager_fragment);
		
		final ActionBar actionBar = getActionBar();

	    // Specify that tabs should be displayed in the action bar.
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		pager = (ViewPager) findViewById(R.id.vpPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        
        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
        	
        	@Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                // When the tab is selected, switch to the
                // corresponding page in the ViewPager.
        		pager.setCurrentItem(tab.getPosition());
            }

			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				
			}

        };
        
     // Add 3 tabs, specifying the tab's text and TabListener
     actionBar.addTab( actionBar.newTab().setText("Fixed Expense").setTabListener(tabListener));
     actionBar.addTab( actionBar.newTab().setText("Variable Expense").setTabListener(tabListener));
     
     pager.setOnPageChangeListener(
             new ViewPager.SimpleOnPageChangeListener() {
                 @Override
                 public void onPageSelected(int position) {
                     // When swiping between pages, select the
                     // corresponding tab.
                     getActionBar().setSelectedNavigationItem(position);
                 }
             });




	}
	
	private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
        	
        	Fragment fm = null;
            switch(pos) {

            case 0: 
            	fm = new AddFixedExpenseFragment();
            	break;
            case 1: 
            	fm = new AddVariableExpenseFragment();
            	break;
            default: 
            	fm = new AddFixedExpenseFragment();
            	break;
            }
            return fm;
        }

        @Override
        public int getCount() {
            return 2;
        }       
    }

	public void onViewClick(View view){

		Intent i = new Intent(this, FixedExpensesDetailActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		AddNewExpenseActivity.this.finish();
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(this, LandingPageActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		AddNewExpenseActivity.this.finish();
	}
}
