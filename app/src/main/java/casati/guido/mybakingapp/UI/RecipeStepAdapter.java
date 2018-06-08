package casati.guido.mybakingapp.UI;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import casati.guido.mybakingapp.Data.Step;
import casati.guido.mybakingapp.R;

/**
 * Created by guido on 27/05/2018.
 */

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.RecipeDetailViewHolder> {

    private static final String TAG = RecipeStepAdapter.class.getSimpleName();
    private List<Step> lRecipeSteps;
    /**
     * An on-click handler defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final RecipeDetailAdapterOnClickHandler mClickHandler;

    public RecipeStepAdapter(List<Step> lRecipeSteps, RecipeDetailAdapterOnClickHandler onClickHandler) {
        this.lRecipeSteps = lRecipeSteps;
        this.mClickHandler = onClickHandler;
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface RecipeDetailAdapterOnClickHandler {
        void onClick(int step);
    }
    /**
     * Called when RecyclerView needs a new {@link RecipeDetailViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * onBindViewHolder(RecipeDetailViewHolder, int, List). Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(RecipeDetailViewHolder, int)
     */
    @Override
    public RecipeDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType)  {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_recipe_detail, parent, false);
        return new RecipeDetailViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link RecipeDetailViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link RecipeDetailViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override onBindViewHolder(RecipeDetailViewHolder, int, List) instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(RecipeDetailViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: triggered");
        Step stepItem = lRecipeSteps.get(position);
        holder.tvRecipeDetailStep.setText(stepItem.getmShortDescription());
    }


    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        if (lRecipeSteps == null) return 0;
        else return lRecipeSteps.size();
    }

    public class RecipeDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.tv_recipe_detail_step)
        TextView tvRecipeDetailStep;

        public RecipeDetailViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }
}
