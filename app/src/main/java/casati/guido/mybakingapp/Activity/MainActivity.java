package casati.guido.mybakingapp.Activity;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

import casati.guido.mybakingapp.Data.Ingredient;
import casati.guido.mybakingapp.Data.Recipe;
import casati.guido.mybakingapp.Data.Step;
import casati.guido.mybakingapp.R;
import casati.guido.mybakingapp.UI.MainFragment;
import casati.guido.mybakingapp.Widget.BakingAppWidgetProvider;

public class MainActivity extends AppCompatActivity implements MainFragment.OnRecipeClickListener {

    private final String TAG = MainActivity.class.getSimpleName();
    public static ArrayList<Ingredient> alIngredients = new ArrayList<>();
    public static String sRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.main_fragment_container, new MainFragment()).commit();
        }
    }

    @Override
    public void onRecipeClicked(Recipe recipe) {
        Log.d(TAG, "onRecipeClicked: " + recipe.getsRecipeName());
        //arrayLIst for intent
        ArrayList<Step> stepArrayList = new ArrayList<>();
        stepArrayList.addAll(recipe.getlSteps());
        //update ingredients list
        sRecipe = recipe.getsRecipeName();
        alIngredients.clear();
        alIngredients.addAll(recipe.getlIngredients());

        //update shared preferences
        SharedPreferences sharedPreferences = this.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("recipe", sRecipe);
        editor.apply();

        //intent
        Intent intent = new Intent(this, RecipeStepActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("recipe", recipe);
        bundle.putParcelableArrayList("steps", stepArrayList);
        bundle.putParcelableArrayList("ingredients", alIngredients);
        intent.putExtras(bundle);
        startActivity(intent);

        //update widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getApplicationContext(), BakingAppWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_widget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_text);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
