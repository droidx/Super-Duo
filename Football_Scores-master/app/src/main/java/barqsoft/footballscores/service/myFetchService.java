package barqsoft.footballscores.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import barqsoft.footballscores.Utilies;

/**
 * Created by yehya khaled on 3/2/2015.
 */

// Refactored and renamed this file and class from myFetchService to MyFetchService - following java's naming convention
public class MyFetchService extends IntentService
{
    public static final String LOG_TAG = "MyFetchService";
    public MyFetchService()
    {
        super("MyFetchService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        Log.d(LOG_TAG, "onHandleIntent() called with: " + "intent = [" + intent + "]");
        Utilies.getData(getApplicationContext(), "n2");
        Utilies.getData(getApplicationContext(), "p2");
        return;
    }
}

