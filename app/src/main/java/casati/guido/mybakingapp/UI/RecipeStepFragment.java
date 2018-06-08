package casati.guido.mybakingapp.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import casati.guido.mybakingapp.Data.Step;
import casati.guido.mybakingapp.R;

/**
 * Created by guido on 26/05/2018.
 */

public class RecipeStepFragment extends Fragment implements RecipeStepAdapter.RecipeDetailAdapterOnClickHandler {

    private onRecipeStepClickHandler onRecipeStepClickHandler;
    private static final String TAG = RecipeStepFragment.class.getSimpleName();
    private List<Step> lSteps;
    @BindView(R.id.rv_recipe_detail)
    RecyclerView rvRecipeStep;

    public void setlSteps(List<Step> lSteps) {
        this.lSteps = lSteps;
    }

    @Override
    public void onClick(int position) {
        onRecipeStepClickHandler.onStepClicked(position);
    }

    public interface onRecipeStepClickHandler {
        void onStepClicked(int position);
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

        View rootView =inflater.inflate(R.layout.fragment_recipe_step,container,false);
        ButterKnife.bind(this, rootView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false);
        rvRecipeStep.setLayoutManager(layoutManager);
        rvRecipeStep.setHasFixedSize(true);
        RecipeStepAdapter recipeStepAdapter = new RecipeStepAdapter(lSteps,this );
        rvRecipeStep.setAdapter(recipeStepAdapter);

        return rootView;
    }

    /**
     * Called when a fragment is first attached to its context.
     * {@link #onCreate(Bundle)} will be called after this.
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onRecipeStepClickHandler = (onRecipeStepClickHandler) context;
        } catch (ClassCastException e) {
            Log.d(TAG, "An exception occurred: " + e.getMessage() + " perhaps the click handler is not implemented?");
        }
    }

}
