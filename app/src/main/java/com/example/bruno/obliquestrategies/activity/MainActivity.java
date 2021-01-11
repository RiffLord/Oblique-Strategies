package com.example.bruno.obliquestrategies.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.bruno.obliquestrategies.R;
import com.example.bruno.obliquestrategies.util.Deck;

public class MainActivity extends AppCompatActivity {
    private Deck mDeck;

    //  UI Elements
    private View mScreenView;
    private Animation mFade;
    private TextView mCard;
    private TextView mSubtitle;

    //  Keeps track of the number of times
    //  a user has clicked on the screen in
    //  order to alternate between light and
    //  dark layouts
    private static int m_nClickCount = 0;

    //  Alternates between a dark background and white text & white background and dark text
    private void changeLayout(int n) {
        mFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out_dark);
        mScreenView.startAnimation(mFade);
        if (n % 2 == 0) {
            mScreenView.setBackgroundColor(ContextCompat.getColor(this, R.color.dark));
            mCard.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else {
            mScreenView.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            mCard.setTextColor(ContextCompat.getColor(this, R.color.dark));
        }
        mFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_dark);
        mScreenView.startAnimation(mFade);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  Sets up the views for the Activity's graphical elements
        mScreenView = findViewById(R.id.layout);
        mFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_dark);
        mScreenView.startAnimation(mFade);
        mCard = findViewById(R.id.title);
        mSubtitle = findViewById(R.id.subtitle);

        mDeck = new Deck(mScreenView);

        //  Restores the state if the Activity is restarted
        if (savedInstanceState != null) {
            mCard.setText(savedInstanceState.getString("card"));

            //  If the screen was rotated before a card was drawn, the subtitle is still visible
            if (mCard.getText().toString().equals(getResources().getString(R.string.app_name)))
                mSubtitle.setVisibility(View.VISIBLE);

            //  If a card was drawn, the subtitle is hidden
            else mSubtitle.setVisibility(View.INVISIBLE);

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
                //  Hides the subtitle
                mSubtitle.setVisibility(View.INVISIBLE);

                m_nClickCount++;
                changeLayout(m_nClickCount);

                //  Obtains a card from the deck and displays it on-screen
                mCard.setText(mDeck.drawCard());

                return false;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("card", mCard.getText().toString());

        //  Saves the click counter state to set the correct
        //  layout colours when restarting the Activity
        outState.putInt("layout", m_nClickCount);
    }
}
