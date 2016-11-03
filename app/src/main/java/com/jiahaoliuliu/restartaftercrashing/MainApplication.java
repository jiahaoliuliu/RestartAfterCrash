package com.jiahaoliuliu.restartaftercrashing;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

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

    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        // Init the variables
        mContext = this;

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
            mContext.startActivity(intent);
            //for restarting the Activity
            Process.killProcess(Process.myPid());
            System.exit(0);
        }
    };
}
