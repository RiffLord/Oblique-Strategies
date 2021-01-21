package com.example.bruno.obliquestrategies.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;

import com.example.bruno.obliquestrategies.R;
import com.example.bruno.obliquestrategies.util.CardDrawer;
import com.google.android.material.textview.MaterialTextView;

public class MainActivity extends AppCompatActivity {
    private CardDrawer mCardDrawer;

    //  UI Elements
    private View mScreenView;
    public static Animation mFade;             //  FIND SOLUTION FOR THIS
    private MaterialTextView mCard;
    private int mUiOptions;
    private View mDecorView;

    private Handler mHandler;
    //  Keeps track of the number of times
    //  a user has clicked on the screen in
    //  order to alternate between light and
    //  dark layouts
    private static int mClickCount = 0;

    //  Alternates between a dark background and white text & white background and dark text
    private void changeLayout(int n) {
        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        mFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        mScreenView.startAnimation(mFade);

        int navBarSettings = mDecorView.getSystemUiVisibility();

        if (n % 2 == 0) {
            mScreenView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            mCard.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryLight));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
            navBarSettings &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        } else {
            mScreenView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryLight));
            mCard.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryLight));
            navBarSettings |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        }
        mDecorView.setSystemUiVisibility(navBarSettings);
        mFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        mScreenView.startAnimation(mFade);
    }

    private void hideStatusBar(final View decorView, final Context context) {
        // Hide the status bar.
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                |    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    hideStatusBar(decorView, context);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  Sets up the views for the Activity's graphical elements
        mScreenView = findViewById(R.id.layout);
        mFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        mScreenView.startAnimation(mFade);
        mCard = findViewById(R.id.card);

        mDecorView = getWindow().getDecorView();

        hideStatusBar(mDecorView, this);


        //  Should prevent reinitializing the CardDrawer to make sure each card will be drawn at least once
        if (mCardDrawer == null) {
            mCardDrawer = new CardDrawer(mScreenView);
            mCard.setText(mCardDrawer.drawCard());
        }

        //  Restores the state if the Activity is restarted
        if (savedInstanceState != null) {
            mCard.setText(savedInstanceState.getString("card"));


            changeLayout(savedInstanceState.getInt("layout"));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //  Sets up a listener which waits for a touch event
        mScreenView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                mClickCount++;

                mCard.setText("");
                //  Obtains a card from the deck and displays it on-screen
                mCard.setText(mCardDrawer.drawCard());

                changeLayout(mClickCount);

                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideStatusBar(mDecorView, this);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("card", mCard.getText().toString());

        //  Saves the click counter state to set the correct
        //  layout colours when restarting the Activity
        outState.putInt("layout", mClickCount);
        mFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
    }
}
