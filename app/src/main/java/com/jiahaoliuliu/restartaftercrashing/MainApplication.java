package com.jiahaoliuliu.restartaftercrashing;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.io.PrintWriter;
import java.io.StringWriter;
import android.os.Process;

/**
 * Example from StackOverFlow
 * http://stackoverflow.com/questions/12560590/android-app-restarts-upon-crash-force-close
 * http://stackoverflow.com/questions/2681499/android-how-to-auto-restart-application-after-its-been-force-closed
 * Created by jiahao on 03/11/16.
 */
public class MainApplication extends Application {

    public static final String INTENT_KEY_UNCAUGHT_EXCEPTION = "UncaughtException";
    public static final String INTENT_KEY_STACK_TRACE = "StackTrace";

    private static final long WAITING_TIME = 2000; // 2seconds

    private Context mContext;

    private Thread.UncaughtExceptionHandler mDefaultUEH;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        // Init the variables
        mContext = this;

        // Save the default uncaught exception handler for Crashlytics
        // This won't avoid the system info screen to appear. Sometimes it says
        // stops the app and sometimes it says restart the app. This option will
        // affect the correct start of the app.
        mDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(mCaughtExceptionHandler);
    }

    private Thread.UncaughtExceptionHandler mCaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable exception) {

            StringWriter stackTrace = new StringWriter();
            exception.printStackTrace(new PrintWriter(stackTrace));
            System.err.println(stackTrace);// You can use LogCat too
            Intent intent = new Intent(mContext, MainActivity.class);
            String s = stackTrace.toString();
            //you can use this String to know what caused the exception and in which Activity
            intent.putExtra(INTENT_KEY_UNCAUGHT_EXCEPTION,
                    "Exception is: " + stackTrace.toString());
            intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
            intent.putExtra(INTENT_KEY_STACK_TRACE, s);

            // Start the activity after 2 seconds
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + WAITING_TIME, pendingIntent);

            // This will make Crashlytics do its job
            mDefaultUEH.uncaughtException(thread, exception);
        }
    };
}
