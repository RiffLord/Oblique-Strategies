package com.example.bruno.obliquestrategies.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bruno.obliquestrategies.R;
import com.example.bruno.obliquestrategies.fragment.CardFragment;
import com.example.bruno.obliquestrategies.util.DepthPageTransformer;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private View mScreenView;
    public static Animation mFade;

    private View mDecorView;

    private GestureDetector mGestureDetector;

    //  Number of pages to display
    private static final int NUM_PAGES = 173;

    private ViewPager2 mViewPager;
    private FragmentStateAdapter mPagerAdapter;

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            return new CardFragment();
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //  Instantiate a ViewPager2 and a PagerAdapter
        mViewPager = findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(this);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setPageTransformer(new DepthPageTransformer());

        //  Sets up the views for the Activity's graphical elements
        mScreenView = findViewById(R.id.layout);
        mFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        mScreenView.startAnimation(mFade);

        mDecorView = getWindow().getDecorView();

        //  Removing this makes status bar fully transparent, however the card intersects with the Notch
        hideStatusBar(mDecorView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mGestureDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }
            @Override
            public void onShowPress(MotionEvent e) {}
            @Override
            public boolean onSingleTapUp(MotionEvent e) { return false; }
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) { return false; }
            @Override
            public void onLongPress(MotionEvent e) {}

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        hideStatusBar(mDecorView);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                displayAboutDialog();
                break;
            case R.id.theme:
                displayThemeDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayAboutDialog() {
        MaterialAlertDialogBuilder aboutDialogBuilder = new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme);
        aboutDialogBuilder.setTitle(getResources().getString(R.string.app_name));
        aboutDialogBuilder.setMessage(getResources().getString(R.string.about));
        aboutDialogBuilder.setNegativeButton(getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog aboutDialog = aboutDialogBuilder.create();
        aboutDialog.show();
    }

    private void displayThemeDialog() {
        MaterialAlertDialogBuilder themeDialogBuilder = new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme);
        themeDialogBuilder.setTitle(getResources().getString(R.string.theme_title));
        themeDialogBuilder.setMessage(getResources().getString(R.string.theme_dialog_msg));
        themeDialogBuilder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //  Save changes here
            }
        });
        themeDialogBuilder.setNegativeButton(getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog themeDialog = themeDialogBuilder.create();
        themeDialog.show();
    }

    private void hideStatusBar(final View decorView) {
        // Hide the status bar.
        /*
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    hideStatusBar(decorView);
                }
            }
        });

         */
    }
}
