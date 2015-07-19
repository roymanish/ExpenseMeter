/**
 * 
 */
package com.main.expensetracker.utility;

import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.widget.ListView;

import com.main.expensetracker.adapter.MonthWiseExpenseAdapter;
import com.main.expensetracker.data.MonthWiseExpenseData;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

/**
 * @author MaRoy
 *
 */
public class ListActivityCardView extends ListActivity{

	
	public void viewCards(){
		
		this.applyCardView();

	}
	
	private void applyCardView(){
		
		ListView lv = getListView();
		if(lv != null){
			lv.setDivider(new ColorDrawable(Color.TRANSPARENT));
			lv.setDividerHeight(20);
		    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) lv.getLayoutParams();
		    params.setMargins(20, 0, 20, 0);
		    lv.setLayoutParams(params);
		}
			
	}
	
	public void applySwipeInAnimation(Activity activity, List<MonthWiseExpenseData> monthWiselist){
		
		ListView lv = getListView();
		
		MonthWiseExpenseAdapter mAdapter = new MonthWiseExpenseAdapter(activity, monthWiselist);
		SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(mAdapter);
		animationAdapter.setAbsListView(lv);
		lv.setAdapter(animationAdapter);
	}
	
}
