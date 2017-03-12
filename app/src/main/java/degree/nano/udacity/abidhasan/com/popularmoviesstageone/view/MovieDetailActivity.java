package degree.nano.udacity.abidhasan.com.popularmoviesstageone.view;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import degree.nano.udacity.abidhasan.com.popularmoviesstageone.Common.ActivityFragmentStatemaintainer;
import degree.nano.udacity.abidhasan.com.popularmoviesstageone.MVP_INTERFACES.MovieDetailMVP;
import degree.nano.udacity.abidhasan.com.popularmoviesstageone.R;
import degree.nano.udacity.abidhasan.com.popularmoviesstageone.model.MovieDetailModel;
import degree.nano.udacity.abidhasan.com.popularmoviesstageone.model.PopularTopRatedMovieModels.MovieGridItem;
import degree.nano.udacity.abidhasan.com.popularmoviesstageone.presenter.MovieDetailPresenter;

import static degree.nano.udacity.abidhasan.com.popularmoviesstageone.R.styleable.Toolbar;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailMVP.RequiredViewOps {

    private MovieDetailMVP.ProviedPresenterOps mPresenter;
    private ProgressDialog pDialog;
    private Toolbar mToolbar;

    private ImageView backDropIv, posterIv;
    public TextView titleTv , ratingTv , releaseDateTv , overViewTv;

    // Responsible to maintain the object's integrity
    // during configurations change
    private ActivityFragmentStatemaintainer mStateMaintainer
            = new ActivityFragmentStatemaintainer(getFragmentManager(), getClass().getName());

    private MovieGridItem selectedMovieItem;
    private String toolbarTitle , moviegridItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpMVP();
        setContentView(R.layout.activity_movie_detail);

        Bundle extras = getIntent().getExtras();
        selectedMovieItem = getSelectedMovieItem(extras);
        Log.d(getClass().getSimpleName() , "selected movie "+ selectedMovieItem.getMovieId());
        setUpViews();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(getClass().getSimpleName() , "lifecycle_event :onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(getClass().getSimpleName() , "lifecycle_event :onResume()");
        showMovieDetails();
    }

    /**
     * tell presenter to fetch
     * movie detail for the movie
     */
    private void showMovieDetails() {
        mPresenter.loadMovieDetail(selectedMovieItem);
    }

    private void setUpViews() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        backDropIv = (ImageView) findViewById(R.id.movie_backdrop);
        posterIv = (ImageView) findViewById(R.id.movie_detail_poster_iv);
        titleTv = (TextView) findViewById(R.id.movie_title_tv_detail);
        ratingTv = (TextView) findViewById(R.id.vote_avg_tv_detail);
        releaseDateTv = (TextView) findViewById(R.id.releaseDate_tv);
        overViewTv = (TextView) findViewById(R.id.overView_tv);


        /*if(mToolbar !=null)
            setSupportActionBar(mToolbar);

        if(selectedMovieItem != null)
            getSupportActionBar().setTitle(selectedMovieItem.getMovieTitle());*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(getClass().getSimpleName() , "lifecycle_event :onPause()");

        if(pDialog != null)
            pDialog.dismiss();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(getClass().getSimpleName() , "lifecycle_event :onStop()");

        mPresenter.onConfigurationChanged(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(getClass().getSimpleName() , "lifecycle_event :onDestroy()");
        mPresenter.onDestroy(isChangingConfigurations());
    }


    /**
     * Setup Model View Presenter pattern.
     * Use a {@link ActivityFragmentStatemaintainer} to maintain the
     * Presenter and Model instances between configuration changes.
     * Could be done differently,
     * using a dependency injection for example.
     */
    private void setUpMVP(){
        try{
            if(mStateMaintainer.isFirstTimeIn())
                initialize(this);
            else
                reInitialize(this);
        }catch (InstantiationException | IllegalAccessException e){
            Log.e(getClass().getSimpleName() , "onCreate()"+e.getMessage());
            throw  new RuntimeException(e);
        }
    }

    /**
     * Initialize relevant MVP Objects.
     * Creates a Presenter instance, saves the presenter in {@link ActivityFragmentStatemaintainer}
     *
     * @param view
     */
    private void initialize(MovieDetailMVP.RequiredViewOps view)
        throws InstantiationException , IllegalAccessException{

        //create the presenter
        MovieDetailPresenter presenter = new MovieDetailPresenter(this);

        //create the model
        MovieDetailModel model = new MovieDetailModel(presenter);

        //set model for presenter
        presenter.setModel(model);

        //save presenter
        /**
         * save object {@link ActivityFragmentStatemaintainer}
         */
        mStateMaintainer.put(MovieDetailMVP.ProviedPresenterOps.class.getSimpleName() , presenter);
        mStateMaintainer.put(MovieDetailMVP.ProviedPresenterOps.class.getSimpleName() ,  model);

        //set the presenter as a interface
        //to limit communication with it
        mPresenter = presenter;
    }


    /**
     * Recovers Presenter and informs Presenter that occurred a config change.
     * If Presenter has been lost, recreates a instance
     */
    private void reInitialize(MovieDetailMVP.RequiredViewOps view)
        throws  InstantiationException , IllegalAccessException{

        mPresenter = mStateMaintainer.get(MovieDetailMVP.ProviedPresenterOps.class.getSimpleName());

        if(mPresenter == null){
            Log.e(getClass().getSimpleName() , "didn't get presenter from stateMaintainer");
            initialize(view);
        }else {
            Log.d(getClass().getSimpleName() , "reInitializing view ");
            mPresenter.onConfigurationChanged(view);
        }
    }

    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }

    @Override
    public Context getActivityContext() {
        return MovieDetailActivity.this;
    }

    @Override
    public ProgressDialog getProgressDialog() {

        if (pDialog == null)
            return pDialog = createProgressDialog() ;

        return pDialog;
    }

    @Override
    public void showProgressDialog() {
        pDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        pDialog.hide();
    }

    @Override
    public void showToast(Toast toast) {
        toast.show();
    }

    @Override
    public void showAlert(AlertDialog alertDialog) {
        alertDialog.show();
    }

    @Override
    public ImageView getBackdropImageView() {

        return backDropIv;
    }

    @Override
    public ImageView getPosterImageView() {
        return posterIv;
    }

    @Override
    public TextView getTitleTv() {
        return titleTv;
    }

    @Override
    public TextView getRatingTv() {
        return ratingTv;
    }

    @Override
    public TextView getRelaseDateTv() {
        return releaseDateTv;
    }

    @Override
    public TextView getOverViewTv() {
        return overViewTv;
    }


    private MovieGridItem getSelectedMovieItem(Bundle b) {

        if(b != null)
            moviegridItem = b.getString("movie_item");

        return new Gson().fromJson(moviegridItem , MovieGridItem.class);
    }

    public ProgressDialog createProgressDialog() {

        pDialog = new ProgressDialog(getActivityContext()
                , R.style.AppTheme_Dark_Dialog);

        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);

        return pDialog;
    }

}
