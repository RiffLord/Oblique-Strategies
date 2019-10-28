package com.example.bruno.obliquestrategies.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bruno.obliquestrategies.R;
import com.example.bruno.obliquestrategies.util.Deck;

public class MainActivity extends AppCompatActivity {
    //  Tag for log messages
    private static final String TAG = "Eno";

    //  View of the screen
    private View mScreenView;

    private Deck mDeck;

    /** TODO:
     * onPause saves the currently displayed card and
     * onRestart displays the currently stored card, if any
     *
     * Support portrait and landscape mode
     * Full screen
     **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "onCreate... Instantiating layout.");

        //  Obtains a reference to the activity_main.xml layout
        mScreenView = findViewById(R.id.layout);

        mDeck = new Deck(mScreenView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart... Touch listener set up.");

        //  Sets up a listener which waits for a touch event
        mScreenView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i(TAG, "Received a Touch event...");

                //  Obtains a reference to the title TextView of the layout;
                //  the TextView will be used to display the card
                final TextView card = findViewById(R.id.title);

                //  Obtains a reference to the subtitle TextView and hides it from the screen
                final TextView subtitle = findViewById(R.id.subtitle);
                subtitle.setVisibility(View.INVISIBLE);

                //  Handles the touch by calling the drawCard method
                card.setText(mDeck.drawCard());

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
}
