package com.main.expensetracker.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;


public class FeedBackActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        Button sendFeedback = (Button)findViewById(R.id.sendFeedbackBtn);
        sendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                String aEmailList[] = {"manishroy007@gmail.com"};
                String aEmailCCList[] = {""};
                EditText sndrNameView = (EditText)findViewById(R.id.senderName);
                String senderName = sndrNameView.getText().toString();

                EditText senderEmailView = (EditText)findViewById(R.id.senderEmail);
                String senderEmail = senderEmailView.getText().toString();

                RatingBar ratingView = (RatingBar)findViewById(R.id.ratingBar);
                String rating = String.valueOf(ratingView.getRating());

                EditText commentView = (EditText)findViewById(R.id.feedBackBody);
                String comment = commentView.getText().toString();
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
                emailIntent.putExtra(android.content.Intent.EXTRA_CC, aEmailCCList);
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, senderName+" sent feedback with rating "+rating);
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, comment);
                startActivity(emailIntent);
                sndrNameView.setText("");
                senderEmailView.setText("");
                ratingView.setRating(0.0f);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feed_back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
