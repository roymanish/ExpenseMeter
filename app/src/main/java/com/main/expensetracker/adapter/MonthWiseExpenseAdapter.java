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
import com.main.expensetracker.data.MonthWiseExpenseData;
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
public class MonthWiseExpenseAdapter extends ArrayAdapter<MonthWiseExpenseData>{

	// List context
	private final Context context;
	// List values
	private final List<MonthWiseExpenseData> monthWiseSmsDataList;
	
	private IExpenseDataProcessor dataProcessor;

    static class ViewHolder{
        public TextView iconTextView;
        public TextView iconView;
        public TextView creditView;
        public TextView debitView;
        public GChartProgressBar progressBarView;
        public TextView progressBarText;
        public WebView chartWebView;
    }

	public MonthWiseExpenseAdapter(Context context, List<MonthWiseExpenseData> monthWiseSmsDataList) {
		super(context, R.layout.activity_monthly_expense, monthWiseSmsDataList);
		this.context = context;
		this.monthWiseSmsDataList = monthWiseSmsDataList;
		this.dataProcessor = ExpenseDataProcessor.getInstance();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

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

		final MonthWiseExpenseData monthWiseData = getMonthWiseSmsDataList().get(position);
		
		Double totalFixedExpense = dataProcessor.getTotalOfAllFixedExpenses(context);
		
		Double totalVariableExpenseDebit = dataProcessor.getTotalOfAllVariableExpenseForMonthByAccountingType(context, monthWiseData.getMonth(), AppConstants.ACCOUNTING_TYPE_DEBIT);
		Double totalVariableExpenseCredit = dataProcessor.getTotalOfAllVariableExpenseForMonthByAccountingType(context, monthWiseData.getMonth(), AppConstants.ACCOUNTING_TYPE_CREDIT);
		
		Double totalCreditAmount = monthWiseData.getCreditAmount()+totalVariableExpenseCredit+monthWiseData.getCarryForwardAmount();
		Double totalDebitAmount = monthWiseData.getDebitAmount()+totalFixedExpense+totalVariableExpenseDebit;

        holder.iconTextView.setText(monthWiseData.getMonth());

        holder.iconView.setBackground(AppUtil.randomColorPicker(context,position));

        holder.creditView.setText(totalCreditAmount.toString());

        holder.debitView.setText(totalDebitAmount.toString());

        holder.chartWebView.getSettings().setJavaScriptEnabled(true);
        holder.chartWebView.getSettings().setDomStorageEnabled(true);
        WebViewJavaScriptInterface jsInterface = new WebViewJavaScriptInterface(monthWiseData);
        holder.chartWebView.addJavascriptInterface(jsInterface,"app");
        holder.chartWebView.loadUrl("file:///android_asset/ExpenseChart.html");
        holder.chartWebView.setWebViewClient(new GChartWebViewClient(holder.progressBarView, holder.progressBarText));

		return rowView;
	}

	public List<MonthWiseExpenseData> getMonthWiseSmsDataList() {
		return monthWiseSmsDataList;
	}

	public ArrayList<ExpenseData> getSmsListForMonth(String month){

		for(MonthWiseExpenseData monthWiseData : this.monthWiseSmsDataList){

			if(month.equalsIgnoreCase(monthWiseData.getMonth()))
				return monthWiseData.getSmsList();
		}
		return null;
	}

    /*
     * JavaScript Interface. Web code can access methods in here
     * (as long as they have the @JavascriptInterface annotation)
     */
    public class WebViewJavaScriptInterface{

        private MonthWiseExpenseData monthWiseData;

        /*
         * Need a reference to the context in order to sent a post message
         */
        public WebViewJavaScriptInterface(MonthWiseExpenseData monthWiseData){
            this.monthWiseData = monthWiseData;
        }

        /*
         * This method can be called from Android. @JavascriptInterface
         * required after SDK version 17.
         */
        @JavascriptInterface
        public String getChartData(){
            ArrayList<ChartData> pieHelperArrayList = new ArrayList<ChartData>();

            Float creditAmtFrmTranData = Float.valueOf(this.monthWiseData.getAmountByAccountingType(AppConstants.ACCOUNTING_TYPE_CREDIT));
            Double creditAmtFrmVarExp = dataProcessor.getTotalOfAllVariableExpenseForMonthByAccountingType(context, this.monthWiseData.getMonth(), AppConstants.ACCOUNTING_TYPE_CREDIT);
            Float totalCredit = creditAmtFrmTranData+Float.valueOf(creditAmtFrmVarExp.toString());
            Float totalDebit = 0f;

            Map<String, String> expenseByCategory = dataProcessor.getCategorizedMonthlyExpenses(this.monthWiseData, context);

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
