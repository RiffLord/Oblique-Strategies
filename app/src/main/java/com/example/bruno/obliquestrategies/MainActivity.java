package com.example.bruno.obliquestrategies;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

//  TODO: CODE CLEANUP

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

                drawCard(v);

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

    private void drawCard(View view) {
        Log.i(TAG, "Drawing a new card...");

        //  TODO: keep an array of drawn card indexes
        AssetManager assets = view.getContext().getAssets();

            //  Counts the cards in the deck file
        int nCards = 0;
        try {
            Log.i(TAG, "Opening file to count cards...");
            InputStream deckStream = assets.open("deck");
            BufferedReader deckReader = new BufferedReader(new InputStreamReader(deckStream));

            while (deckReader.readLine() != null) {
                nCards++;
            }
            deckReader.close();

            Log.i(TAG, "Number of cards in deck: " + nCards);
        } catch (IOException e) {
            Log.e(TAG, "Error opening deck file...");
            e.printStackTrace();
        }

        //  Iterates through the file up to the desired index and and prints the corresponding card
        String cardContent = new String();
        try {
            Log.i(TAG, "Opening file to draw a random card...");
            InputStream deckStream = assets.open("deck");
            BufferedReader deckReader = new BufferedReader(new InputStreamReader(deckStream));

            Random cardToDraw = new Random();
            cardToDraw.setSeed(SystemClock.currentThreadTimeMillis());

            for (int i = 0; i < cardToDraw.nextInt(nCards); i++)
                cardContent = deckReader.readLine();

            Log.i(TAG, cardContent);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        final TextView subtitle = findViewById(R.id.subtitle);
        subtitle.setVisibility(View.INVISIBLE);

        final TextView card = findViewById(R.id.title);

        if (cardContent != null)
            card.setText(cardContent);

        else card.setText("New card drawn!");
    }
}
