package barqsoft.footballscores.service;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.ScoresWidgetProvider;

/**
 * Created by priteshsankhe on 11/10/15.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(getApplicationContext(), intent);
    }

    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private final String TAG = ListRemoteViewsFactory.class.getSimpleName();

        private Context context;
        private Cursor cursor;
        private int appWidgetId;

        public ListRemoteViewsFactory(Context context, Intent intent) {
            this.context = context;
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            Date today = new Date(System.currentTimeMillis());
            SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
            final Uri queryUri = DatabaseContract.scores_table.buildScoreWithDate();
            cursor = getContentResolver().query(queryUri, null, null, new String[]{mformat.format(today)}, null);
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return cursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            cursor.moveToPosition(position);
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_item);
            final String home = cursor.getString(cursor.getColumnIndex(DatabaseContract.scores_table.HOME_COL));
            final String away = cursor.getString(cursor.getColumnIndex(DatabaseContract.scores_table.AWAY_COL));
            final String homeGoals = cursor.getString(cursor.getColumnIndex(DatabaseContract.scores_table.HOME_GOALS_COL));
            final String awayGoals = cursor.getString(cursor.getColumnIndex(DatabaseContract.scores_table.AWAY_GOALS_COL));
            Log.d(TAG, "getViewAt() called with: " + "position = [" + position + "]" + " home : " + home);
            Log.d(TAG, "getViewAt() called with: " + "position = [" + position + "]" + " home goals : " + homeGoals + "\t away goals : " + awayGoals);
            if ("-1".equalsIgnoreCase(homeGoals)) {
                rv.setTextViewText(R.id.widget_item_title, home + " v/s " + away);
            } else {
                rv.setTextViewText(R.id.widget_item_title, home + " v/s " + away + "\n" + homeGoals + " - " + awayGoals);
            }
            Bundle extras = new Bundle();
            extras.putInt(ScoresWidgetProvider.EXTRA_ITEM, position);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            rv.setOnClickFillInIntent(R.id.widget_item_title, fillInIntent);
            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
