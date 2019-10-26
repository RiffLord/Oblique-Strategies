package com.example.bruno.obliquestrategies;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Eno";

    private View mScreenView;

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

        //  Instantiates the layout and obtains a reference to it in order to add a listener
        mScreenView = findViewById(R.id.layout);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart... Touch listener set up.");
        mScreenView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //  Handles the touch by calling the drawCard method
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

        final TextView card = findViewById(R.id.title);
        card.setText("New card drawn!");
    }
}
