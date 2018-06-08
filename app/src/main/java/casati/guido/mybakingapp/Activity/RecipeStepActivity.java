package casati.guido.mybakingapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import casati.guido.mybakingapp.Data.Ingredient;
import casati.guido.mybakingapp.Data.Recipe;
import casati.guido.mybakingapp.Data.Step;
import casati.guido.mybakingapp.R;
import casati.guido.mybakingapp.UI.RecipeStepDetailFragment;
import casati.guido.mybakingapp.UI.RecipeStepFragment;

/**
 * Created by guido on 25/05/2018.
 */

public class RecipeStepActivity extends AppCompatActivity implements RecipeStepFragment.onRecipeStepClickHandler {

    private boolean bTwoPane;
    private Recipe recipe;
    private String sRecipeName;
    private List<Ingredient> lIngredients;
    private List<Step> lSteps;
    private FragmentManager fragmentManager;
    final static private String TAG = RecipeStepActivity.class.getSimpleName();
    @BindView(R.id.tv_ingredients)
    TextView textViewIngredients;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Step> stepArrayList = new ArrayList<>();
        stepArrayList.addAll(lSteps);
        outState.putParcelableArrayList("Steps", stepArrayList);
        outState.putSerializable("Recipe", recipe);
        outState.putString("Name", sRecipeName);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);
        ButterKnife.bind(this);
        //retrieve intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        //retrieve ingredient
        if (bundle != null) {
            lIngredients = bundle.getParcelableArrayList("ingredients");
        }
        //set ingredients tv
        StringBuilder stringBuilder = new StringBuilder("Ingredients:\n");
        for (int i = 0; i < lIngredients.size(); i++) {
            stringBuilder.append("• " + lIngredients.get(i).getmQuantity() + " "
                    + lIngredients.get(i).getmMeasure()
                    + " " + lIngredients.get(i).getmIngredient() + "\n");
        }
        textViewIngredients.setText(stringBuilder.toString());

        //check on the device, based on the size of the screen
        int minSize = getResources().getConfiguration().smallestScreenWidthDp;
        if (minSize >= 600)
            bTwoPane = true;

        if (savedInstanceState == null) {
            //retrieve recipes list
            recipe = (Recipe) bundle.getSerializable("recipe");
            sRecipeName = recipe.getsRecipeName();
            //Log.d(TAG, "onCreate:getsRecipeName " + sRecipeName); //debug
            //retrieve recipe steps
            lSteps = bundle.getParcelableArrayList("steps");

            if (bTwoPane) {
                RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
                recipeStepDetailFragment.setStep(lSteps.get(0));
                getSupportFragmentManager().beginTransaction().
                        add(R.id.step_detail_fragment_container, recipeStepDetailFragment)
                        .commit();
            }
        } else if (savedInstanceState != null) {
            Log.d(TAG, "onCreate: savedInstanceState triggered");
            recipe = (Recipe) savedInstanceState.getSerializable("Recipe");
            lSteps = savedInstanceState.getParcelableArrayList("Steps");
            sRecipeName = savedInstanceState.getString("Name");
        }
        //set title to the recipe name
        getSupportActionBar().setTitle(sRecipeName);
        //setup fragment
        RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
        recipeStepFragment.setlSteps(lSteps);
        getSupportFragmentManager().beginTransaction().
                add(R.id.recipe_step_fragment_container, recipeStepFragment)
                .commit();
    }


    @Override
    public void onStepClicked(int position) {

        Step step = lSteps.get(position);
        Log.d(TAG, "onStepClicked: " + step.getmDescription());

        if (!bTwoPane) {
            ArrayList<Step> stepArrayList = new ArrayList<>();
            stepArrayList.addAll(lSteps);
            //intent
            Intent intent = new Intent(this, RecipeStepDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Step", step);
            bundle.putString("Name", sRecipeName);
            bundle.putParcelableArrayList("Steps", stepArrayList);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
            recipeStepDetailFragment.setStep(lSteps.get(position));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_fragment_container, recipeStepDetailFragment)
                    .commit();
        }
    }
}
