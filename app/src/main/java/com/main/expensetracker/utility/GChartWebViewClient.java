package com.main.expensetracker.utility;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by MaRoy on 3/30/2015.
 */
public class GChartWebViewClient extends WebViewClient {

    private ProgressBar progressBar;
    private TextView textView;

    public GChartWebViewClient(final GChartProgressBar progressBar, final TextView textView) {

        this.progressBar = progressBar;
        this.textView = textView;
        progressBar.showProgressBar(textView);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        view.setVisibility(View.VISIBLE);
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        // TODO Auto-generated method stub
        super.onPageFinished(view, url);
        textView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
    }
}
