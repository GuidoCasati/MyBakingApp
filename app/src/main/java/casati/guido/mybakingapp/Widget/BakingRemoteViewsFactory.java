package casati.guido.mybakingapp.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import casati.guido.mybakingapp.Activity.MainActivity;
import casati.guido.mybakingapp.Data.Ingredient;
import casati.guido.mybakingapp.R;

/**
 * Created by guido on 06/06/2018.
 */

public class BakingRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private ArrayList<Ingredient> alIngredients;

    /**
     * Called when your factory is first constructed. The same factory may be shared across
     * multiple RemoteViewAdapters depending on the intent passed.
     */
    @Override
    public void onCreate() {

    }

    public BakingRemoteViewsFactory(Context context) {
        this.context = context;
    }

    /**
     * Called when notifyDataSetChanged() is triggered on the remote adapter. This allows a
     * RemoteViewsFactory to respond to data changes by updating any internal references.
     * <p>
     * Note: expensive tasks can be safely performed synchronously within this method. In the
     * interim, the old data will be displayed within the widget.
     *
     * @see AppWidgetManager#notifyAppWidgetViewDataChanged(int[], int)
     */
    @Override
    public void onDataSetChanged() {
        // update widget
        alIngredients = MainActivity.alIngredients;
    }

    /**
     * Called when the last RemoteViewsAdapter that is associated with this factory is
     * unbound.
     */
    @Override
    public void onDestroy() {

    }

    /**
     * See {@link Adapter#getCount()}
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return alIngredients.size();
    }

    /**
     * See {@link Adapter#getView(int, View, ViewGroup)}.
     * <p>
     * Note: expensive tasks can be safely performed synchronously within this method, and a
     * loading view will be displayed in the interim. See {@link #getLoadingView()}.
     *
     * @param position The position of the item within the Factory's data set of the item whose
     *                 view we want.
     * @return A RemoteViews object corresponding to the data at the specified position.
     */
    @Override
    public RemoteViews getViewAt(int position) {
        Ingredient ingredient = alIngredients.get(position);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.list_item_ingredient_widget);
        remoteViews.setTextViewText(R.id.tv_ingredientName, ingredient.getmIngredient());
        remoteViews.setTextViewText(R.id.tv_quantityMeasure, ingredient.getmQuantity() + " " + ingredient.getmMeasure());
        return remoteViews;
    }

    /**
     * This allows for the use of a custom loading view which appears between the time that
     * {@link #getViewAt(int)} is called and returns. If null is returned, a default loading
     * view will be used.
     *
     * @return The RemoteViews representing the desired loading view.
     */
    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    /**
     * See {@link Adapter#getViewTypeCount()}.
     *
     * @return The number of types of Views that will be returned by this factory.
     */
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    /**
     * See {@link Adapter#getItemId(int)}.
     *
     * @param position The position of the item within the data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * See {@link Adapter#hasStableIds()}.
     *
     * @return True if the same id always refers to the same object.
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }
}
