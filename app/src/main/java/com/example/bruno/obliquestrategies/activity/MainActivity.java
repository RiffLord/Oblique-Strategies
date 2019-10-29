package com.example.bruno.obliquestrategies.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bruno.obliquestrategies.R;
import com.example.bruno.obliquestrategies.util.Deck;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Eno";

    private Deck mDeck;

    //  UI Elements
    private View mScreenView;
    private TextView mCard;
    private TextView mSubtitle;

    private static int m_nClickCount = 0;

    /** TODO:
     * Full screen
     **/

    private void changeLayout(int n) {
        if (n % 2 == 0) {
            mScreenView.setBackgroundColor(getResources().getColor(R.color.backgroundDark));
            mCard.setTextColor(Color.WHITE);
        } else {
            mScreenView.setBackgroundColor(Color.WHITE);
            mCard.setTextColor(getResources().getColor(R.color.backgroundDark));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  Sets up the views for the Activity's graphical elements
        mScreenView = findViewById(R.id.layout);
        mCard = findViewById(R.id.title);
        mSubtitle = findViewById(R.id.subtitle);

        mDeck = new Deck(mScreenView);

        if (savedInstanceState != null) {
            mCard.setText(savedInstanceState.getString("card"));

            if (mCard.getText().toString().equals(getResources().getString(R.string.app_name)))
                mSubtitle.setVisibility(View.VISIBLE);

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

                //  Handles the touch by calling the drawCard method
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
