package casati.guido.mybakingapp.UI;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import casati.guido.mybakingapp.Data.Step;
import casati.guido.mybakingapp.R;

/**
 * Created by guido on 27/05/2018.
 */

public class RecipeStepDetailFragment extends Fragment {

    private Step step;
    private String sDescription, sVideoURL, sThumbURL;
    private List<Step> lSteps;
    private SimpleExoPlayer simpleExoPlayer;
    private String TAG = RecipeStepDetailFragment.class.getSimpleName();
    @BindView(R.id.tv_step_description)
    TextView tv_step_description;
    @BindView(R.id.simple_exoPlayer)
    SimpleExoPlayerView simpleExoPlayerView;
    @BindView(R.id.iv_thumbnail)
    ImageView iv_thumbNail;
    boolean bTwoPane;
    int iOrientation;
    Uri videoURI;
    long playerPosition;
    boolean isPlayWhenReady = true;
    /**
     * Called to do initial creation of a fragment.  This is called after
     * {@link #onAttach(Activity)} and before
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * <p>
     * <p>Note that this can be called while the fragment's activity is
     * still in the process of being created.  As such, you can not rely
     * on things like the activity's content view hierarchy being initialized
     * at this point.  If you want to do work once the activity itself is
     * created, see {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>Any restored child fragments will be created before the base
     * <code>Fragment.onCreate</code> method returns.</p>
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() !=null){
            sDescription = getArguments().getString("Desc");
            sThumbURL = getArguments().getString("ImageURL");
            sVideoURL = getArguments().getString("VideoURL");
        }
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();
        if (videoURI != null)
            initializePlayer(videoURI);
    }

    /**
     * Called when the Fragment is no longer resumed.  This is generally
     * tied to {@link Activity#onPause() Activity.onPause} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onPause() {
        super.onPause();
        if (simpleExoPlayer != null) {
            playerPosition = simpleExoPlayer.getCurrentPosition();
            isPlayWhenReady = simpleExoPlayer.getPlayWhenReady();
            releasePlayer();
        }
    }

    /**
     * Called when the Fragment is no longer started.  This is generally
     * tied to {@link Activity#onStop() Activity.onStop} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStop() {
        super.onStop();
        if (simpleExoPlayer != null){
            isPlayWhenReady = simpleExoPlayer.getPlayWhenReady();
            playerPosition = simpleExoPlayer.getCurrentPosition();
            releasePlayer();
        }
    }

    /**
     * Called to ask the fragment to save its current dynamic state, so it
     * can later be reconstructed in a new instance of its process is
     * restarted.  If a new instance of the fragment later needs to be
     * created, the data you place in the Bundle here will be available
     * in the Bundle given to {@link #onCreate(Bundle)},
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}, and
     * {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>This corresponds to {@link Activity#onSaveInstanceState(Bundle)
     * Activity.onSaveInstanceState(Bundle)} and most of the discussion there
     * applies here as well.  Note however: <em>this method may be called
     * at any time before {@link #onDestroy()}</em>.  There are many situations
     * where a fragment may be mostly torn down (such as when placed on the
     * back stack with no UI showing), but its state will not be saved until
     * its owning activity actually needs to save its state.
     *
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("Step", step);
        if(simpleExoPlayer!=null){
            playerPosition = simpleExoPlayer.getCurrentPosition();
            isPlayWhenReady = simpleExoPlayer.getPlayWhenReady();
        }
        outState.putBoolean("PlayState", isPlayWhenReady);
        outState.putLong("playerPosition",playerPosition);

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
     *                           UI should be attached to.
     *                           The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
        ButterKnife.bind(this, rootView);
        playerPosition = C.TIME_UNSET;

        Log.d(TAG, "onCreateView: triggered");
        //get orientation
        iOrientation = getResources().getConfiguration().orientation;
        int minSize = getResources().getConfiguration().smallestScreenWidthDp;
        if (minSize >= 600)
            bTwoPane = true;

        if (savedInstanceState != null) {
            Log.d(TAG, "onCreateView: savedInstanceState triggered");
            step = (Step) savedInstanceState.getSerializable("Step");
            playerPosition = savedInstanceState.getLong("playerPosition", C.TIME_UNSET);
            isPlayWhenReady = savedInstanceState.getBoolean("PlayState");
        } else if (savedInstanceState == null) {
        }

        if (step!=null){
            sDescription = step.getmDescription();
            sThumbURL = step.getmThumbnailUrl();
            sVideoURL = step.getmVideoUrl();
            Log.d(TAG, "onCreateView: CHECK II");
        }
        // set description
        tv_step_description.setText(sDescription);
        Log.d(TAG, "onCreateView: sDescription CHECK III " + sDescription);
        //setup exoplayer
        simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (isNetworkAvailable(getContext())) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        else{
            Toast toast = Toast.makeText(getContext(), "Network unavailable", Toast.LENGTH_LONG);
            toast.show();
       }
        if (StringUtils.isNotBlank(sVideoURL) && StringUtils.isNotEmpty(sVideoURL) && networkInfo != null && networkInfo.isConnected()) {
            videoURI = Uri.parse(sVideoURL);
            iv_thumbNail.setVisibility(View.GONE);
            simpleExoPlayerView.setVisibility(View.VISIBLE);
            initializePlayer(videoURI);
            if (iOrientation == Configuration.ORIENTATION_LANDSCAPE && !bTwoPane) {
                //video full screen
                simpleExoPlayerView.getLayoutParams().height = LayoutParams.MATCH_PARENT;
                simpleExoPlayerView.getLayoutParams().width = LayoutParams.MATCH_PARENT;
                simpleExoPlayerView.setPadding(0,0,0,0);
                simpleExoPlayerView.hideController();
                tv_step_description.setVisibility(View.GONE);
            }
        } else {
            simpleExoPlayerView.setVisibility(View.GONE);
            iv_thumbNail.setVisibility(View.VISIBLE);
            if (StringUtils.isNotEmpty(sThumbURL) && StringUtils.isNotBlank(sThumbURL)) {
                Log.d(TAG, "onCreateView: triggered // thumbnail " + sThumbURL);
                //Uri uri=Uri.parse(step.getmThumbnailUrl()).buildUpon().build();
                Picasso.with(getContext()).load(sThumbURL).into(iv_thumbNail);
            }
        }
        return rootView;
    }


    /**
     * Returns true if network is available or about to become available
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
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
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public void setlSteps(List<Step> lSteps) {
        this.lSteps = lSteps;
    }

    private void initializePlayer(Uri mediaUri) {
        if (simpleExoPlayer == null) {
            // new instance
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(simpleExoPlayer);
            //setup mediaSource
            String userAgent = Util.getUserAgent(getActivity(), "My Baking App");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            if (playerPosition != C.TIME_UNSET)
                simpleExoPlayer.seekTo(playerPosition);
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(isPlayWhenReady);
        }
    }

    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
        }
    }

    /**
     * Called when the view previously created by {@link #onCreateView} has
     * been detached from the fragment.  The next time the fragment needs
     * to be displayed, a new view will be created.  This is called
     * after {@link #onStop()} and before {@link #onDestroy()}.  It is called
     * <em>regardless</em> of whether {@link #onCreateView} returned a
     * non-null view.  Internally it is called after the view's state has
     * been saved but before it has been removed from its parent.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
        simpleExoPlayer = null;
    }

    /**
     * Called when the fragment is no longer attached to its activity.  This
     * is called after {@link #onDestroy()}.
     */
    @Override
    public void onDetach() {
        super.onDetach();
    }

    public static RecipeStepDetailFragment newInstance(String description, String videoUrl,
                                                       String imageUrl) {
        String TAG = RecipeStepDetailFragment.class.getSimpleName();
        Log.d(TAG, "newInstance: triggered");
        Bundle args = new Bundle();
        args.putString("Desc", description);
        args.putString("VideoURL", videoUrl);
        args.putString("ImageURL", imageUrl);
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }



}
