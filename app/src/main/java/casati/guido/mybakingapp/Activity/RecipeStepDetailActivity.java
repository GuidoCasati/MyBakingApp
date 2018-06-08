package casati.guido.mybakingapp.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import casati.guido.mybakingapp.Data.Step;
import casati.guido.mybakingapp.R;
import casati.guido.mybakingapp.UI.RecipeStepDetailFragment;
import casati.guido.mybakingapp.UI.StepPagerAdapter;

/**
 * Created by guido on 27/05/2018.
 */

public class RecipeStepDetailActivity extends AppCompatActivity {

    private static final String TAG = RecipeStepDetailActivity.class.getSimpleName();
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private boolean bTwoPane;
    private String sRecipeName;
    private Step step;
    private int iIndex;
    private ArrayList<Step> stepArrayList;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("Steps", stepArrayList);
        outState.putInt("Index", iIndex);
        outState.putString("Name", sRecipeName);
        outState.putSerializable("Step", step);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);
        ButterKnife.bind(this);
        //check orientation
        int iOrientation = getResources().getConfiguration().orientation;
        //check screen size
        int minSize = getResources().getConfiguration().smallestScreenWidthDp;
        if (minSize >= 600)
            bTwoPane = true;


        if (savedInstanceState == null) {
            // retrieve intent
            Intent intent = getIntent();
            if (intent != null) {
                step = (Step) intent.getSerializableExtra("Step");
                sRecipeName = intent.getStringExtra("Name");
                stepArrayList = intent.getParcelableArrayListExtra("Steps");
                iIndex = step.getmStepId();
                Log.d(TAG, "onCreate: " + step.getmDescription() + sRecipeName);
            }
        } else {
            stepArrayList = savedInstanceState.getParcelableArrayList("Steps");
            step = (Step) savedInstanceState.getSerializable("Step");
            iIndex = savedInstanceState.getInt("Index");
            sRecipeName = savedInstanceState.getString("Name");
        }
        getSupportActionBar().setTitle(sRecipeName);

        if (bTwoPane) {
            RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
            recipeStepDetailFragment.setStep(step);
            recipeStepDetailFragment.setlSteps(stepArrayList);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_detail_fragment_container, recipeStepDetailFragment)
                    .commit();
        } else {
            Log.d(TAG, "onCreate: new stepPagerAdapter");
                    /*viewPager.setAdapter(new StepPagerAdapter(getSupportFragmentManager(),stepArrayList));*/
            StepPagerAdapter adapter = new StepPagerAdapter(getSupportFragmentManager(), stepArrayList);

            for (int i = 0; i < stepArrayList.size(); i++) {
                adapter.addFragment(RecipeStepDetailFragment.newInstance(
                        stepArrayList.get(i).getmDescription(),
                        stepArrayList.get(i).getmVideoUrl(),
                        stepArrayList.get(i).getmThumbnailUrl()), "Step: " + stepArrayList.get(i).getmStepId());
                //tabLayout.addTab(tabLayout.newTab().setText("Step: " + stepArrayList.get(i).getmStepId()));
            }

            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    viewPager.setCurrentItem(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
            //setup viewPager
            viewPager.setCurrentItem(iIndex);
            tabLayout.setupWithViewPager(viewPager);
            if (iOrientation == Configuration.ORIENTATION_LANDSCAPE)
                tabLayout.setVisibility(View.GONE);
        }
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}





