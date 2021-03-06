package degree.nano.udacity.abidhasan.com.popularmoviesstageone.MVP_INTERFACES;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import degree.nano.udacity.abidhasan.com.popularmoviesstageone.model.PopularTopRatedMovieModels.MovieGridItem;
import degree.nano.udacity.abidhasan.com.popularmoviesstageone.view.MovieViewHolder;

/**
 * Created by abidhasan on 2/26/17.
 */

public class PopularMoviesMVP {

    /**
     * Required View methods available to Presenter.
     * A passive layer, responsible to show data
     * and receive user interactions
     * (presenter -> view)
     */
    public interface RequiredViewOps{

        Context getAppContext();
        Context getActivityContext();

        ProgressDialog getProgressDialog();
        void showProgressDialog();
        void hideProgressDialog();
        void showToast(Toast toast);
        void showAlert(AlertDialog alertDialog);
        void goToDetailActivity(MovieGridItem item);

        RecyclerView getRecyclrView();
    }


    /**
     * Required View methods available to Presenter.
     * A passive layer, responsible to show data
     * and receive user interactions
     * (presenter -> view)
     */
    public interface ProvidedPresenterOps{

        void onDestroy(boolean isChangingConfigurations);
        void onConfigurationChanged(RequiredViewOps view);
        void setView(RequiredViewOps view);
        boolean onCreateOptionMenu(Menu menu);


        MovieViewHolder createPopularViewHolder(ViewGroup parent , int viewType);
        void bindPopularViewHolder(MovieViewHolder holder , int position);
        int getPopularItemCount();

        void initializeRecyclerView();
        MovieViewHolder createTopRatedViewHolder(ViewGroup parent , int viewType);
        void bindTopratedViewHolder(MovieViewHolder holder , int position);
        int getTopRatedItemCount();

        MovieViewHolder createFavoriteViewHolder(ViewGroup parent , int viewType);
        void bindFavoriteViewHolder(MovieViewHolder holder , int position);
        int getFavoriteItemCount();

        void setProgressDialogMsg(String msg , ProgressDialog progressDialog);
        void loadPopularMovies();
        void loadTopRatedMovies();
        void loadFavoriteMovies();


    }


    /**
     * required Presenter operation available
     * to model (model -> presenter)
     */
    public interface RequiredPresenterOps{
        Context getAppContext();
        Context getActivityContext();
    }

    /**
     * Operations offered to model to communicate with presenter
     * Handles all data business logic
     * (presenter -> model)
     */
    public interface ProvidedModelOps{

        void onDestroy(boolean isConfigurationChanging);
        void loadPopularMovies();
        void loadTopRatedMovies();

        int getPopularMoviesListSize();
        int getTopRatedMoviesListSize();
        int getFavedMovieListSize();

        List<MovieGridItem> generatePopularMovieGridItems();
        List<MovieGridItem> generateTopRatedMovieGridItems();
        boolean loadFavedMovies();
        MovieGridItem getAMovieItem(int position);
    }
}
