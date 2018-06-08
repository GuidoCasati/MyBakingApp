package casati.guido.mybakingapp.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import casati.guido.mybakingapp.Data.Recipe;
import casati.guido.mybakingapp.R;
import casati.guido.mybakingapp.Utils.RecipeAPIClient;
import casati.guido.mybakingapp.Utils.RecipeApiInterface;
import casati.guido.mybakingapp.Utils.SimpleIdlingResource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by guido on 25/05/2018.
 */

public class MainFragment extends Fragment implements RecipeAdapter.RecipeAdapterOnClickHandler {

    // static variables
    private static final String TAG = MainFragment.class.getSimpleName();

    //global variables
    private OnRecipeClickListener onRecipeClickListener;
    private RecipeAdapter recipeAdapter;

    //binding views to layout
    @BindView(R.id.rv_recipes)
    RecyclerView recyclerViewRecipes;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.error_textView)
    TextView errorTextView;

    //espresso
    private SimpleIdlingResource simpleIdlingResource = null;

    public MainFragment() {
    }

    @Override
    public void onClick(Recipe recipeClicked) {
        onRecipeClickListener.onRecipeClicked(recipeClicked);
        Log.d(TAG, "onClick: recipe clicked " + recipeClicked.getsRecipeName());
    }

    public interface OnRecipeClickListener {
        void onRecipeClicked(Recipe recipe);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onRecipeClickListener = (OnRecipeClickListener) context;
        } catch (Exception e) {
            Log.d(TAG, "An exception occurred: " + e.getMessage() + " perhaps the click handler is not implemented?");
        }
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //binding views
        ButterKnife.bind(this, rootView);

        // Attach the layout manager to the recycler view
        //retrieve screen orientation from device configuration
        int iOrientation = getResources().getConfiguration().orientation;
        if (iOrientation == 2) {
            //landscape mode
            GridLayoutManager gridLayoutManager = new GridLayoutManager(rootView.getContext(), 2);
            recyclerViewRecipes.setLayoutManager(gridLayoutManager);
        } else if (iOrientation == 1) {
            //portrait mode
            LinearLayoutManager linearLayoutManager =
                    new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerViewRecipes.setLayoutManager(linearLayoutManager);
        }

        recyclerViewRecipes.setHasFixedSize(true);

        // recipeAdapter that will link the recipe details with the views displaying the data
        recipeAdapter = new RecipeAdapter(this);

        // Attaching the adapter to the RecyclerView
        recyclerViewRecipes.setAdapter(recipeAdapter);

        //IdlingResource
        if (simpleIdlingResource != null)
            simpleIdlingResource.setIdleState(false);

        loadRecipes();

        return rootView;
    }


    /**
     * handle for the RecipeApiInterface
     * to Get the list of Recipes
     **/
    private void loadRecipes() {

        final RecipeApiInterface recipeApiInterface = RecipeAPIClient.getClient().create(RecipeApiInterface.class);
        Call<List<Recipe>> call = recipeApiInterface.doGetListRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                //set progress bar to invisible
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onResponse: " + response.code());
                List<Recipe> responseRecipes = response.body();

                //debug
                for(int i = 0; i < responseRecipes.size(); i++) {
                    Log.d(TAG, "onResponse: " + responseRecipes.get(i).getsRecipeName());
                }

                if (responseRecipes != null) {
                    recipeAdapter.setRecipeData(responseRecipes);
                    errorTextView.setVisibility(View.GONE);
                    if (simpleIdlingResource != null)
                        simpleIdlingResource.setIdleState(true);
                } else {
                    errorTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                progressBar.setVisibility(View.VISIBLE);
                Log.e(TAG, "onFailure: An error occurred while loading: " + Arrays.toString(t.getStackTrace()));
                //call.cancel();
            }
        });
    }
}
