/**
 * 
 */
package com.main.expensetracker.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.main.expensetracker.IExpenseDataProcessor;
import com.main.expensetracker.adapter.FixedExpenseDetailAdapter;
import com.main.expensetracker.constants.AppConstants;
import com.main.expensetracker.data.UserDefinedExpenseData;
import com.main.expensetracker.processor.ExpenseDataProcessor;
import com.main.expensetracker.utility.SwipeDismissList;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author MaRoy
 *
 */
public class FixedExpensesDetailActivity extends ListActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		IExpenseDataProcessor dataProcessor = ExpenseDataProcessor.getInstance();
		String fixedExpenseListJson = dataProcessor.readDataFromFile(getString(R.string.preference_file_key_Fixed), null, getApplicationContext());
		Type type = new TypeToken<List<UserDefinedExpenseData>>(){}.getType();
		List<UserDefinedExpenseData> fixedExpenseList = dataProcessor.getGson().fromJson(fixedExpenseListJson, type);
		List<UserDefinedExpenseData> variableExpenseList = dataProcessor.readUserDefinedDataFromFileForMonth(getString(R.string.preference_file_key_Variable), getApplicationContext(), null);

		if(variableExpenseList != null)
			fixedExpenseList.addAll(variableExpenseList);

		ListView lv = getListView();

		final FixedExpenseDetailAdapter mAdapter = new FixedExpenseDetailAdapter(this, fixedExpenseList);
		SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(mAdapter);
		animationAdapter.setAbsListView(lv);
		lv.setAdapter(animationAdapter);

		SwipeDismissList swipeList = new SwipeDismissList(lv,
				new SwipeDismissList.OnDismissCallback() {
			// Gets called whenever the user deletes an item.
			public SwipeDismissList.Undoable onDismiss(AbsListView listView, final int position) {
				// Get your item from the adapter (mAdapter being an adapter for MyItem objects)
				final UserDefinedExpenseData deletedItem = mAdapter.getItem(position);
				// Delete item from adapter
				mAdapter.remove(deletedItem);
				// Return an Undoable implementing every method
				return new SwipeDismissList.Undoable() {

					// Method is called when user undoes this deletion
					public void undo() {
						// Reinsert item to list
						mAdapter.insert(deletedItem, position);
					}

					// Return an undo message for that item
					public String getTitle() {
						return deletedItem.getExpenseName() + " deleted";
					}

					// Called when user cannot undo the action anymore
					public void discard() {
						// Use this place to e.g. delete the item from database
                        String fileName = "";
                        if(AppConstants.EXPENSE_TYPE_FIXED.equalsIgnoreCase(deletedItem.getExpenseType())){
                            fileName = getApplicationContext().getString(R.string.preference_file_key_Fixed);
                        }
                        else
                            fileName = getApplicationContext().getString(R.string.preference_file_key_Variable);

						ExpenseDataProcessor.getInstance().deleteDataFromFile(fileName, getApplicationContext(), deletedItem.getExpenseName());
						mAdapter.remove(deletedItem);
						mAdapter.notifyDataSetChanged();
					}
				};
			}
		});
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(this, AddNewExpenseActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		FixedExpensesDetailActivity.this.finish();
	}

	@Override  
	protected void onActivityResult(int requestCode, int resultCode, Intent data)  
	{  
		super.onActivityResult(requestCode, resultCode, data); 

		SwingBottomInAnimationAdapter animationAdapter = (SwingBottomInAnimationAdapter)getListView().getAdapter();
		FixedExpenseDetailAdapter baseAdapter = ((FixedExpenseDetailAdapter)animationAdapter.getDecoratedBaseAdapter());
		baseAdapter.onActivityResult(requestCode, data);

	} 
}
