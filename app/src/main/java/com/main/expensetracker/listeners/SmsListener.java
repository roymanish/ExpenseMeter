package com.main.expensetracker.listeners;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;

import com.main.expensetracker.activities.R;
import com.main.expensetracker.data.ExpenseData;
import com.main.expensetracker.data.MonthWiseExpenseData;
import com.main.expensetracker.processor.ExpenseDataProcessor;
import com.main.expensetracker.processor.TransactionMessageProcessor;
import com.main.expensetracker.utility.AppUtil;

import java.util.Date;
import java.util.List;

public class SmsListener extends BroadcastReceiver {

    static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    public SmsListener() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (ACTION.equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] messages = new SmsMessage[pdus.length];

                for (int i = 0; i < pdus.length; i++)
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

                for (SmsMessage message : messages) {

                    if(TransactionMessageProcessor.getInstance().isTransactionMessage(message.getMessageBody()))
                    {
                        Double amount = TransactionMessageProcessor.getInstance().extractExpenseAmountFromMsg(message.getMessageBody());
                        Date smsDate = AppUtil.getDateFromTimestamp(message.getTimestampMillis());
                        String where = message.getOriginatingAddress();

                        ExpenseData sms = new ExpenseData(where, amount.toString(), AppUtil.convertDateToString(smsDate));
                        List<MonthWiseExpenseData> monthWiseList = ExpenseDataProcessor.getInstance().getMonthWiseList(context);
                        TransactionMessageProcessor.getInstance().createMonthWiseData(sms, monthWiseList, context,message.getMessageBody());

                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(context)
                                        .setSmallIcon(R.drawable.msg_notification)
                                        .setContentTitle("New Expense")
                                        .setContentText("Rs."+sms.getAmount());

                        // Gets an instance of the NotificationManager service
                        NotificationManager mNotifyMgr =
                                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                        // Builds the notification and issues it.
                        mNotifyMgr.notify(001, mBuilder.build());
                    }
                }
            }
        }
    }
}

