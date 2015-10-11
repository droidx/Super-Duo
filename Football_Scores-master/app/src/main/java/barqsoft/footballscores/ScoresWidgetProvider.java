package barqsoft.footballscores;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import barqsoft.footballscores.service.ListWidgetService;

/**
 * Created by priteshsankhe on 10/10/15.
 */
public class ScoresWidgetProvider extends AppWidgetProvider {

    private static final String LOG_TAG = ScoresWidgetProvider.class.getSimpleName();
    public static final String ITEM_SELECTED_ACTION = "barqsoft.footballscores.ITEM_SELECTED_ACTION";
    public static final String EXTRA_ITEM = "barqsoft.footballscores.EXTRA_ITEM";

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(ITEM_SELECTED_ACTION)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);
            Toast.makeText(context, "Touched view " + viewIndex, Toast.LENGTH_SHORT).show();
        }
        super.onReceive(context, intent);
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(LOG_TAG, "onUpdate()");
        // To prevent any ANR timeouts, we perform the update in a service
        for (int i = 0; i < appWidgetIds.length; ++i) {
            Intent intent = new Intent(context, UpdateService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            context.startService(intent);
        }
    }

    public static class UpdateService extends Service {
        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Log.d(LOG_TAG, "onStartCommand() called with: " + "intent = [" + intent + "], flags = [" + flags + "], startId = [" + startId + "]");
            buildUpdate(this);
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            Intent widgetServiceIntent = new Intent(getApplicationContext(), ListWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews updateViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.scores_widget);
            updateViews.setRemoteAdapter(appWidgetId, R.id.widget_match_list, widgetServiceIntent);
            updateViews.setEmptyView(R.id.widget_match_list, R.id.empty_view);

            Intent toastIntent = new Intent(getApplicationContext(), MainActivity.class);
            toastIntent.setAction(ScoresWidgetProvider.ITEM_SELECTED_ACTION);
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent toastPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, toastIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            updateViews.setPendingIntentTemplate(R.id.widget_match_list, toastPendingIntent);
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            manager.updateAppWidget(appWidgetId, updateViews);
            return super.onStartCommand(intent, flags, startId);
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        /**
         * Build a widget update to show the todays football matches
         */
        public void buildUpdate(Context context) {
            // fetch data : Currently fetching for all the five days
            // a good optimization would be just to fetch for today?
            Log.d(LOG_TAG, "buildUpdate() called with: " + "context = [" + context + "]");
            Utilies.getData(getApplicationContext(), "n2");
            Utilies.getData(getApplicationContext(), "p2");
        }
    }
}
