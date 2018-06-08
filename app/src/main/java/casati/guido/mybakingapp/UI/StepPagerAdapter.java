package casati.guido.mybakingapp.UI;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import casati.guido.mybakingapp.Data.Step;

/**
 * Created by guido on 02/06/2018.
 * credits to:
 * https://www.journaldev.com/12958/android-tablayout-viewpager
 * https://guides.codepath.com/android/google-play-style-tabs-using-tablayout
 */

public class StepPagerAdapter extends FragmentPagerAdapter {

    private List<Step> lSteps;
    private final List <RecipeStepDetailFragment> lRecipeStepDetailFragments = new ArrayList< >();
    private final List <String> lsFragmentTitleList = new ArrayList < >();

    public StepPagerAdapter (FragmentManager fragmentManager, List<Step> lSteps) {
        super(fragmentManager);
        setlSteps(lSteps);
    }

    public void setlSteps(@NonNull List<Step> lSteps) {
        this.lSteps = lSteps;
        notifyDataSetChanged();
    }


    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        Log.d(StepPagerAdapter.class.getSimpleName(), "getCount: " + lSteps.size());
        Log.d(StepPagerAdapter.class.getSimpleName(), "lRecipeStepDetailFragments.size(): " + lRecipeStepDetailFragments.size());
        return lRecipeStepDetailFragments.size();
    }

    /**
     * This method may be called by the ViewPager to obtain a title string
     * to describe the specified page. This method may return null
     * indicating no title for this page. The default implementation returns
     * null.
     *
     * @param position The position of the title requested
     * @return A title for the requested page
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return lsFragmentTitleList.get(position);
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        Log.d("StepPagerAdapter", "getItem: triggered position: " + position);
        return lRecipeStepDetailFragments.get(position);
    }

    public void addFragment(RecipeStepDetailFragment fragment, String title) {
        lRecipeStepDetailFragments.add(fragment);
        lsFragmentTitleList.add(title);
    }

}
