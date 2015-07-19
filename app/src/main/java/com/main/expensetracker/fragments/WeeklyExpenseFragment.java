/**
 * 
 */
package com.main.expensetracker.fragments;

import java.util.List;

import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.ListFragment;
import android.widget.ListView;

import com.main.expensetracker.adapter.WeeklyExpenseAdapter;
import com.main.expensetracker.data.WeeklyExpenseData;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

/**
 * @author MaRoy
 *
 */
public class WeeklyExpenseFragment extends ListFragment{

	List<WeeklyExpenseData> weeklyDataList;
	ListView lv;
	Context context;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		lv = getListView();

		context = getActivity();

		WeeklyExpenseAdapter mAdapter = new WeeklyExpenseAdapter(context, weeklyDataList);
		SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(mAdapter);
		animationAdapter.setAbsListView(lv);
		lv.setAdapter(animationAdapter);
	}
	/**
	 * @return the weeklyDataList
	 */
	public List<WeeklyExpenseData> getWeeklyDataList() {
		return weeklyDataList;
	}
	/**
	 * @param weeklyDataList the weeklyDataList to set
	 */
	public void setWeeklyDataList(List<WeeklyExpenseData> weeklyDataList) {
		this.weeklyDataList = weeklyDataList;
	}
	
	
}
