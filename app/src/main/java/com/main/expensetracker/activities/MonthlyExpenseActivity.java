package com.main.expensetracker.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.main.expensetracker.ITransactionMessageProcessor;
import com.main.expensetracker.adapter.MonthWiseExpenseAdapter;
import com.main.expensetracker.constants.AppConstants;
import com.main.expensetracker.data.ExpenseData;
import com.main.expensetracker.data.MonthWiseExpenseData;
import com.main.expensetracker.processor.ExpenseDataProcessor;
import com.main.expensetracker.processor.TransactionMessageProcessor;
import com.main.expensetracker.utility.AppUtil;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MonthlyExpenseActivity extends NavigationDrawerActivity implements AdapterView.OnItemClickListener{

	private Uri uri;
	private ITransactionMessageProcessor processor;
	private ListView lv;
	private Context context;
	private ProgressDialog progress;

    private List<MonthWiseExpenseData>  monthWiselist;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;


	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        setNavigationDrawer();
        View rootView = getLayoutInflater().inflate(R.layout.activity_main, frameLayout, true);

		uri = Uri.parse(AppConstants.INBOX_URI);
		Cursor c= getContentResolver().query(uri, null, null, null, null);
		startManagingCursor(c);

		processor = TransactionMessageProcessor.getInstance();

		String isFirstCall = ExpenseDataProcessor.getInstance().readDataFromFile(AppConstants.CONSTANTS_STORAGE_FILE, AppConstants.IS_FIRST_CALL, getApplicationContext());

		PackageInfo pInfo = null;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(),0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		if((isFirstCall == null || isFirstCall.isEmpty())) {
			if(pInfo.versionCode == 11) {
				ExpenseDataProcessor.getInstance().clearFileData(AppConstants.CONSTANTS_STORAGE_FILE, getApplicationContext());
				ExpenseDataProcessor.getInstance().saveDataToFile(AppConstants.CONSTANTS_STORAGE_FILE, AppConstants.IS_FIRST_CALL, AppConstants.IS_FIRST_CALL, getApplicationContext());
				ExpenseDataProcessor.getInstance().clearFileData(AppConstants.TRANSACTION_STORAGE_FILE, getApplicationContext());
			}
			monthWiselist = processor.processTransactionMessagesNew(c,getApplicationContext(),true);
			ExpenseDataProcessor.getInstance().saveDataToFile(AppConstants.CONSTANTS_STORAGE_FILE, AppConstants.IS_FIRST_CALL, AppConstants.IS_FIRST_CALL, getApplicationContext());
		}
		else
			monthWiselist = processor.processTransactionMessagesNew(c,getApplicationContext(),false);

		lv = (ListView)rootView.findViewById(R.id.list_main);
        lv.setOnItemClickListener(this);

		context = this;

		Collections.sort(monthWiselist, new MonthsComparator());

		MonthWiseExpenseAdapter mAdapter = new MonthWiseExpenseAdapter(context, monthWiselist);
		SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(mAdapter);
		animationAdapter.setAbsListView(lv);
		lv.setAdapter(animationAdapter);

	}


	/* (non-Javadoc)
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l,View v, int position, long id){

		Intent i = new Intent(getApplicationContext(), MonthlyExpenseDetailActivity.class);

		String month = ((TextView)v.findViewById(R.id.monthName)).getText().toString();
		SwingBottomInAnimationAdapter animationAdapter = (SwingBottomInAnimationAdapter)l.getAdapter();
		MonthWiseExpenseAdapter baseAdapter = ((MonthWiseExpenseAdapter)animationAdapter.getDecoratedBaseAdapter());
		List<ExpenseData> smsList = baseAdapter.getSmsListForMonth(month);
		Bundle bundle = new Bundle();
		bundle.putString(AppConstants.MONTH_NAME, AppUtil.getMonthFromStringDate(smsList.get(0).getDate(), AppConstants.MONTH_NAME_LONG));
		bundle.putParcelableArrayList(AppConstants.MONTHLY_LIST, baseAdapter.getSmsListForMonth(month));
		i.putExtras(bundle);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_action_button, menu);
		return true;
	}

    @Override
	public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

		MenuItem item = menu.findItem(R.id.action_refresh);
		item.setVisible(true);
		item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {

				final MenuItem itm = item;
				itm.setIcon(R.drawable.refresh_pressed);
				ExpenseDataProcessor.getInstance().clearFileData(AppConstants.CONSTANTS_STORAGE_FILE, getApplicationContext());
				ExpenseDataProcessor.getInstance().saveDataToFile(AppConstants.CONSTANTS_STORAGE_FILE, AppConstants.IS_FIRST_CALL, AppConstants.IS_FIRST_CALL, getApplicationContext());
				ExpenseDataProcessor.getInstance().clearFileData(AppConstants.TRANSACTION_STORAGE_FILE, getApplicationContext());

				final Cursor c= getContentResolver().query(uri, null, null ,null,null);

				progress = ProgressDialog.show(context, "Expense Meter",
						"Loading...!!", true);

				new Thread(new Runnable() {
					@Override
					public void run()
					{
						monthWiselist = processor.processTransactionMessagesNew(c, getApplicationContext(), true);

						runOnUiThread(new Runnable() {
							@Override
							public void run()
							{
								progress.dismiss();
								itm.setIcon(R.drawable.refresh);
							}
						});
					}
				}).start();

				Collections.sort(monthWiselist, new MonthsComparator());

				// Set smsList in the ListAdapter
				MonthWiseExpenseAdapter mAdapter = new MonthWiseExpenseAdapter(context, monthWiselist);
				SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(mAdapter);
				animationAdapter.setAbsListView(lv);
				lv.setAdapter(animationAdapter);


				return true;
			}
		}); 
		return true;
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		Intent i = new Intent(this, LandingPageActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		MonthlyExpenseActivity.this.finish();
	}

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Intent intent = new Intent(getApplicationContext(), MonthlyExpenseDetailActivity.class);

        String month = ((TextView)view.findViewById(R.id.monthName)).getText().toString();
        SwingBottomInAnimationAdapter animationAdapter = (SwingBottomInAnimationAdapter)lv.getAdapter();
        MonthWiseExpenseAdapter baseAdapter = ((MonthWiseExpenseAdapter)animationAdapter.getDecoratedBaseAdapter());
        List<ExpenseData> smsList = baseAdapter.getSmsListForMonth(month);
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.MONTH_NAME, AppUtil.getMonthFromStringDate(smsList.get(0).getDate(), AppConstants.MONTH_NAME_LONG));
        bundle.putParcelableArrayList(AppConstants.MONTHLY_LIST, baseAdapter.getSmsListForMonth(month));
        intent.putExtras(bundle);
        startActivity(intent);
    }

	private class MonthsComparator implements Comparator<MonthWiseExpenseData>{

		@Override
		public int compare(MonthWiseExpenseData m1, MonthWiseExpenseData m2) {

			if(m1.getFirstDateOfMonth() != null && m2.getFirstDateOfMonth() != null) {
				if (m1.getFirstDateOfMonth().before(m2.getFirstDateOfMonth()))
					return 1;
				else if (m1.getFirstDateOfMonth().after(m2.getFirstDateOfMonth()))
					return -1;
				else
					return 0;
			}
			else
				return 0;
		}
	}
}
