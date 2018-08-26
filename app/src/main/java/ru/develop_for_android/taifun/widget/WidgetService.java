package ru.develop_for_android.taifun.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ru.develop_for_android.taifun.R;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetViewsFactory(this.getApplicationContext(), intent);
    }

    class WidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        public static final String KEY_FILES = "urls";

        private static final int mCount = 10;
        private ArrayList<String> fileAddresses;
        private Context mContext;
        private int mAppWidgetId;

        public WidgetViewsFactory(Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            fileAddresses = intent.getStringArrayListExtra(KEY_FILES);
            Log.i("WIDGET", "creating widget from factory with " + fileAddresses.size());
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return fileAddresses.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.list_item_food_in_widget);
            try {
                Bitmap bitmap = Glide.with(getBaseContext())
                        .asBitmap()
                        .load(fileAddresses.get(position))
                        .submit(600, 400)
                        .get();

                rv.setImageViewBitmap(R.id.food_in_order_image, bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
