package com.jiahaoliuliu.restartaftercrashing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String crashThisApp;

    // Views
    private TextView mCrashReportTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // link the views
        mCrashReportTextView = (TextView) findViewById(R.id.crash_report_text_view);

        // If the app has crashed before, show it
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            StringBuilder stringBuilder = new StringBuilder();

            if (extras.containsKey(MainApplication.INTENT_KEY_UNCAUGHT_EXCEPTION)) {
                stringBuilder.append(extras.getString(MainApplication.INTENT_KEY_UNCAUGHT_EXCEPTION));
            }

            if (extras.containsKey(MainApplication.INTENT_KEY_STACK_TRACE)) {
                stringBuilder.append(extras.getString(MainApplication.INTENT_KEY_STACK_TRACE));
            }

            mCrashReportTextView.setText(stringBuilder.toString());
        }
    }

    /**
     * Method used to crash the app
     * @param view
     */
    public void crash(View view) {
        // Launch null pointer exception
        crashThisApp.charAt(0);
    }
}
