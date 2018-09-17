package ru.develop_for_android.taifun.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;

import ru.develop_for_android.taifun.AppExecutors;
import ru.develop_for_android.taifun.R;
import ru.develop_for_android.taifun.data.AppDatabase;
import ru.develop_for_android.taifun.data.FoodEntry;

public class FoodListWidget extends AppWidgetProvider {
    private static List<FoodEntry> food;

    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        // Here we setup the intent which points to the StackViewService which will
        // provide the views for this collection.
        AppExecutors.getInstance().diskIO().execute(() -> {
            food = AppDatabase.getInstance(context).foodDao().getRandomFood();
            Intent intent = new Intent(context, WidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            ArrayList<String> uris = new ArrayList<>();
            for (FoodEntry foodEntry : food) {
                uris.add(foodEntry.getImageAddressNetwork());
            }

            intent.putExtra(WidgetService.WidgetViewsFactory.KEY_FILES, uris);
            // When intents are compared, the extras are ignored, so we need to embed the extras
            // into the data so that the extras will not be ignored.
            //intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.food_list_widget);
            rv.setRemoteAdapter(R.id.stack_view, intent);

            // The empty view is displayed when the collection has no items. It should be a sibling
            // of the collection view.
            rv.setEmptyView(R.id.stack_view, R.id.empty_view);

            // Here we setup the a pending intent template. Individuals items of a collection
            // cannot setup their own pending intents, instead, the collection as a whole can
            // setup a pending intent template, and the individual items can set a fillInIntent
            // to create unique before on an item to item basis.
            appWidgetManager.updateAppWidget(appWidgetId, rv);
        });
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
}
