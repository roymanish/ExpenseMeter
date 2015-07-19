package com.main.expensetracker.utility;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by MaRoy on 3/31/2015.
 */
public class GChartProgressBar extends ProgressBar{

    private int progressStatus;
    private Handler handler = new Handler();
    public GChartProgressBar(Context context) {
        super(context);
    }

    public GChartProgressBar(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    public GChartProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context,attrs,defStyle);
    }

    public void showProgressBar(final TextView progressText){

        final ProgressBar progressBar = this;
        progressStatus = 0;

        progressBar.setVisibility(View.VISIBLE);
        progressText.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 5;
                    // Update the progress bar and display the

                    //current value in the text view
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                            progressText.setText("Loading Chart.. "+progressStatus+"%");
                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.

                        //Just to display the progress slowly
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
