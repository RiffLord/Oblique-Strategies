package com.example.bruno.obliquestrategies;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Eno";

    private ConstraintLayout mLayout;
    private View mScreenView;
    private TextView mTitle, mCard;

    /** TODO:
     * onStart draws a card after user input
     * onPause saves the currently displayed card and
     * onRestart displayes the currently stored card, if any
     *
     * Support portrait and landscape mode
     * Full screen
     **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "onCreate... Instantiating layout.");

        //  Instantiates the layout and obtains a reference to it in order to add a listener
        mLayout = new ConstraintLayout(this);
        mScreenView = findViewById(R.id.layout);

        /*
        //  Setting up title TextView
        mTitle = new TextView(this);
        mTitle.findViewById(R.id.title);
        mTitle.setVisibility(View.VISIBLE);

        //  Card not drawn yet, set to invisible
        mCard = new TextView(this);
        mCard.findViewById(R.id.card);
        mCard.setVisibility(View.INVISIBLE);
         */
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart... Touch listener set up.");
        mScreenView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //  Handles the touch and calls the draw card method
                Log.i(TAG, "Received a Touch event...");

                drawCard();

                return false;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause...");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart... Calling onStart.");
    }

    private void drawCard() {
        Log.i(TAG, "Drawing a new card...");
    }
}
