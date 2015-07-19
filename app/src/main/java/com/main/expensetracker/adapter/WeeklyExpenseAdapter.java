/**
 * 
 */
package com.main.expensetracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.main.expensetracker.IExpenseDataProcessor;
import com.main.expensetracker.activities.R;
import com.main.expensetracker.constants.AppConstants;
import com.main.expensetracker.data.ChartData;
import com.main.expensetracker.data.ExpenseData;
import com.main.expensetracker.data.WeeklyExpenseData;
import com.main.expensetracker.processor.ExpenseDataProcessor;
import com.main.expensetracker.utility.AppUtil;
import com.main.expensetracker.utility.GChartProgressBar;
import com.main.expensetracker.utility.GChartWebViewClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author MaRoy
 *
 */
public class WeeklyExpenseAdapter extends ArrayAdapter<WeeklyExpenseData>{

	// List context
	private final Context context;
	// List values
	private final List<WeeklyExpenseData> weeklyExpenseDataList;

	private IExpenseDataProcessor dataProcessor;

    private View rowView;

    static class ViewHolder{
        public TextView iconTextView;
        public TextView iconView;
        public TextView creditView;
        public TextView debitView;
        public GChartProgressBar progressBarView;
        public TextView progressBarText;
        public WebView chartWebView;
    }

	public WeeklyExpenseAdapter(Context context, List<WeeklyExpenseData> weeklyExpenseDataList) {
		super(context, R.layout.activity_monthly_expense, weeklyExpenseDataList);
		this.context = context;
		this.weeklyExpenseDataList = weeklyExpenseDataList;
		this.dataProcessor = ExpenseDataProcessor.getInstance();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

       this.rowView = convertView;

        if(rowView == null){

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            rowView = inflater.inflate(R.layout.activity_monthly_expense, parent, false);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.iconTextView = (TextView) rowView.findViewById(R.id.monthName);
            viewHolder.iconView = (TextView) rowView.findViewById(R.id.monthNameLabel);
            viewHolder.creditView = (TextView) rowView.findViewById(R.id.creditAmount);
            viewHolder.debitView = (TextView) rowView.findViewById(R.id.debitAmount);
            viewHolder.progressBarView = (GChartProgressBar)rowView.findViewById(R.id.progressBar);
            viewHolder.progressBarText = (TextView)rowView.findViewById(R.id.progressBarText);
            viewHolder.chartWebView = (WebView)rowView.findViewById(R.id.chart_webview);
            rowView.setTag(viewHolder);


        }

        //Fill Data in View
        ViewHolder holder = (ViewHolder)rowView.getTag();

		final WeeklyExpenseData weeklyData = getWeeklyExpenseDataList().get(position);

		Double totalFixedExpense = "W1".equalsIgnoreCase(weeklyData.getWeek()) ? dataProcessor.getTotalOfAllFixedExpenses(context) : 0D;

		Double totalVariableExpenseCredit = dataProcessor.getTotalOfAllVariableExpenseForWeekByAccountingType(context, weeklyData.getWeek(), AppConstants.ACCOUNTING_TYPE_CREDIT);
		Double totalVariableExpenseDebit = dataProcessor.getTotalOfAllVariableExpenseForWeekByAccountingType(context, weeklyData.getWeek(), AppConstants.ACCOUNTING_TYPE_DEBIT);

		Double totalCreditAmount = weeklyData.getCreditAmount()+totalVariableExpenseCredit;
		Double totalDebitAmount = weeklyData.getDebitAmount()+totalVariableExpenseDebit+totalFixedExpense;


        holder.iconTextView.setText(weeklyData.getWeek());

        holder.iconView.setBackground(AppUtil.randomColorPicker(context,position));

        holder.creditView.setText(totalCreditAmount.toString());

        holder.debitView.setText(totalDebitAmount.toString());

        holder.chartWebView.getSettings().setJavaScriptEnabled(true);
        holder.chartWebView.getSettings().setDomStorageEnabled(true);
        WebViewJavaScriptInterface jsInterface = new WebViewJavaScriptInterface(weeklyData);
        holder.chartWebView.addJavascriptInterface(jsInterface,"app");
        holder.chartWebView.loadUrl("file:///android_asset/ExpenseChart.html");
        holder.chartWebView.setWebViewClient(new GChartWebViewClient(holder.progressBarView, holder.progressBarText));

		return rowView;
	}

	public List<WeeklyExpenseData> getWeeklyExpenseDataList(){
		return weeklyExpenseDataList;
	}

	public ArrayList<ExpenseData> getSmsListForWeek(String month){

		for(WeeklyExpenseData weeklyData : this.weeklyExpenseDataList){

			if(month.equalsIgnoreCase(weeklyData.getWeek()))
				return weeklyData.getSmsList();
		}
		return null;
	}

    /*
     * JavaScript Interface. Web code can access methods in here
     * (as long as they have the @JavascriptInterface annotation)
     */
    public class WebViewJavaScriptInterface{

        private WeeklyExpenseData weeklyData;

        /*
         * Need a reference to the context in order to sent a post message
         */
        public WebViewJavaScriptInterface(WeeklyExpenseData weeklyData){
            this.weeklyData = weeklyData;
        }

        /*
         * This method can be called from Android. @JavascriptInterface
         * required after SDK version 17.
         */
        @JavascriptInterface
        public String getChartData(){
            ArrayList<ChartData> pieHelperArrayList = new ArrayList<ChartData>();

            Float creditAmtFrmTranData = Float.valueOf(weeklyData.getAmountByAccountingType(AppConstants.ACCOUNTING_TYPE_CREDIT));
            Double creditAmtFrmVarExp = dataProcessor.getTotalOfAllVariableExpenseForWeekByAccountingType(context, weeklyData.getWeek(), AppConstants.ACCOUNTING_TYPE_CREDIT);
            Float totalCredit = creditAmtFrmTranData+Float.valueOf(creditAmtFrmVarExp.toString());
            Float totalDebit = 0f;

            Map<String, String> expenseByCategory = dataProcessor.getCategorizedWeeklyExpenses(weeklyData, context);

            for(String expenseCategory : expenseByCategory.keySet()){

                Float expenseAmount = Float.valueOf(expenseByCategory.get(expenseCategory));
                totalDebit = totalDebit+expenseAmount;
                pieHelperArrayList.add(new ChartData(expenseAmount.toString(), expenseCategory));
            }
            if(totalCredit>totalDebit) {
                Float balanceAmount = totalCredit-totalDebit;
                pieHelperArrayList.add(new ChartData(balanceAmount.toString(), AppConstants.CATEGORY.BALANCE.toString()));
            }
            String chartDataJson = ExpenseDataProcessor.getInstance().getGson().toJson(pieHelperArrayList);
            return chartDataJson;

        }
    }

}
