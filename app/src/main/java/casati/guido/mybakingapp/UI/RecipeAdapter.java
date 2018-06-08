package casati.guido.mybakingapp.UI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import casati.guido.mybakingapp.Data.Recipe;
import casati.guido.mybakingapp.R;

/**
 * Created by guido on 26/05/2018.
 * inspired by Udacity's Sunshine project
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private List<Recipe> lRecipeList;
    private static final String TAG = RecipeAdapter.class.getSimpleName();

    /**
     * An on-click handler defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final RecipeAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface RecipeAdapterOnClickHandler {
        void onClick(Recipe recipeClicked);
    }

    /** getters and setters
     *
     * @return Recipe Data object
     */
    public List<Recipe> getlRecipeList() {
        return lRecipeList;
    }

    /**
     * This method is used to set the recipe data on a RecipeAdapter
     * @param recipeData The new recipe data to be displayed.
     */
    public void setRecipeData(@NonNull List<Recipe> recipeData) {
        lRecipeList = recipeData;
        notifyDataSetChanged();
    }

    /**
     * Creates a RecipeAdapter
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public RecipeAdapter(RecipeAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    /**
     * Cache of the children views for a recipe list item.
     */
    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.recipe_icon) ImageView recipeIcon;
        @BindView(R.id.tv_recipe_name) TextView recipeName;
        @BindView(R.id.tv_recipe_servings) TextView recipeServings;

        public RecipeAdapterViewHolder(View view) {
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
            mClickHandler.onClick(lRecipeList.get(adapterPosition));
        }
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  use this viewType integer to provide a different layout
     * @return A new RecipeAdapterViewHolder that holds the View for each list item
     */
    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_recipe, viewGroup, false);
        return new RecipeAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the recipe
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param recipeAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder recipeAdapterViewHolder, int position) {
        Recipe recipeItem = lRecipeList.get(position);
        recipeAdapterViewHolder.recipeName.setText(recipeItem.getsRecipeName());
        recipeAdapterViewHolder.recipeServings.setText("Servings: " + String.valueOf(recipeItem.getsServings()));
    }

    /**
     * This method simply returns the number of items to display
     * @return The number of items available in our recipe list
     */
    @Override
    public int getItemCount() {
        if (lRecipeList == null) return 0;
        return lRecipeList.size();
    }


}
