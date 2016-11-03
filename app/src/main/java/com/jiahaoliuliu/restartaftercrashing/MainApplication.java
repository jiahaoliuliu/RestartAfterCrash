package com.jiahaoliuliu.restartaftercrashing;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import java.io.PrintWriter;
import java.io.StringWriter;
import android.os.Process;

/**
 * Created by jiahao on 03/11/16.
 */

public class MainApplication extends Application {

    public static final String INTENT_KEY_UNCAUGHT_EXCEPTION = "UncaughtException";
    public static final String INTENT_KEY_STACK_TRACE = "StackTrace";

    @Override
    public void onCreate() {
        super.onCreate();

        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this,
                MainActivity.class));
    }

    public class MyExceptionHandler implements
            java.lang.Thread.UncaughtExceptionHandler {
        private final Context myContext;
        private final Class<?> myActivityClass;

        public MyExceptionHandler(Context context, Class<?> c) {

            myContext = context;
            myActivityClass = c;
        }

        public void uncaughtException(Thread thread, Throwable exception) {

            StringWriter stackTrace = new StringWriter();
            exception.printStackTrace(new PrintWriter(stackTrace));
            System.err.println(stackTrace);// You can use LogCat too
            Intent intent = new Intent(myContext, myActivityClass);
            String s = stackTrace.toString();
            //you can use this String to know what caused the exception and in which Activity
            intent.putExtra(INTENT_KEY_UNCAUGHT_EXCEPTION,
                    "Exception is: " + stackTrace.toString());
            intent.putExtra(INTENT_KEY_STACK_TRACE, s);
            myContext.startActivity(intent);
            //for restarting the Activity
            Process.killProcess(Process.myPid());
            System.exit(0);
        }
    }
}
