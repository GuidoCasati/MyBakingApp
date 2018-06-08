package casati.guido.mybakingapp.Widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import casati.guido.mybakingapp.R;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String sRecipe) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

        //pending intent
        Intent intent = new Intent(context, IngredientsService.class);
        views.setRemoteAdapter(R.id.lv_widget, intent);
        //views.setTextViewText(R.id.appwidget_text, sRecipe);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //get recipe name
        //String sRecipe = MainActivity.sRecipe;
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPreferences",
                Context.MODE_PRIVATE);
        String sRecipe = sharedPreferences.getString("recipe", null);

        Log.d("sRecipe", "onUpdate: "+sRecipe);
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, sRecipe);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

