package com.stilldre.moovify.activity;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.stilldre.moovify.BuildConfig;
import com.stilldre.moovify.R;
import com.stilldre.moovify.database.FavoriteHelper;
import com.stilldre.moovify.model.Movie;
import com.stilldre.moovify.repository.MovieRepository;
import com.stilldre.moovify.service.network.callback.OnGetDetailCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static String MOVIE_ID = BuildConfig.TMDB_MOVIE_ID;
    private static String IMAGE_BASE_URL = BuildConfig.TMDB_IMAGE_BASE_URL_780;
    FavoriteHelper favoriteHelper;
    private Movie movie;

    @BindView(R.id.movieDetailsBackdrop)
    ImageView ivBackdrop;
    @BindView(R.id.movieDetailsTitle)
    TextView tvTitle;
    @BindView(R.id.movieDetailsOverview)
    TextView tvOverview;
    @BindView(R.id.overviewLabel)
    TextView tvOverviewLabel;
    @BindView(R.id.movieDetailsReleaseDate)
    TextView tvReleaseDate;
    @BindView(R.id.movieDetailsRating)
    RatingBar rbRating;
    @BindView(R.id.detailToolbar)
    Toolbar toolbar;

    private MovieRepository movieRepository;
    private boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        movie = getIntent().getParcelableExtra("movie");

        movieRepository = MovieRepository.getInstance();

        favoriteHelper = new FavoriteHelper(this);
        favoriteHelper.open();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.black));

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setTitle(null);
        }
        getMovieDetail();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (!favoriteHelper.getMovieById(movie.getId())) {
            isFavorite = false;
            menu.findItem(R.id.action_favorite).setIcon(R.drawable.ic_favorite);
        } else if (favoriteHelper.getMovieById(movie.getId())) {
            isFavorite = true;
            menu.findItem(R.id.action_favorite).setIcon(R.drawable.ic_favorite_red);
        }

        MenuItem item = menu.findItem(R.id.action_favorite);
        item.setOnMenuItemClickListener(view -> {
            if (!isFavorite) {
                addFav(movie);
                favoriteAdded();
                menu.findItem(R.id.action_favorite).setIcon(R.drawable.ic_favorite_red);
                isFavorite = true;
            } else if (isFavorite) {
                removeFav(movie.getId());
                isFavorite = false;
                menu.findItem(R.id.action_favorite).setIcon(R.drawable.ic_favorite);
                favoriteRemoved();
            }

            return true;
        });
        return super.onPrepareOptionsMenu(menu);
    }


    private void getMovieDetail() {
        movieRepository.getDetail(movie.getId(), new OnGetDetailCallback() {
            @Override
            public void onSuccess(Movie movie) {
                tvTitle.setText(movie.getTitle());
                tvOverviewLabel.setVisibility(View.VISIBLE);
                tvOverview.setText(movie.getOverview());
                rbRating.setVisibility(View.VISIBLE);
                rbRating.setRating(movie.getRating() / 2);
                tvReleaseDate.setText(movie.getReleaseDate());

                if (!isFinishing()) {
                    Glide.with(DetailActivity.this)
                            .load(IMAGE_BASE_URL + movie.getBackdropPath())
                            .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                            .into(ivBackdrop);
                }
            }

            @Override
            public void onError() {
                finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar_detail, menu);
        return true;
    }

    private void favoriteAdded() {
        Toast.makeText(DetailActivity.this, "Favorite Added", Toast.LENGTH_SHORT).show();
    }

    private void favoriteRemoved() {
        Toast.makeText(DetailActivity.this, "Favorite Removed", Toast.LENGTH_SHORT).show();
    }

    public void addFav(Movie movie) {
        favoriteHelper.insertMovie(movie);
    }

    public void removeFav(String id) {
        favoriteHelper.deleteFavorite(id);
    }
}
