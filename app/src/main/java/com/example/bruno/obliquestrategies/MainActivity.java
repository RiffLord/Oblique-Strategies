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

public class MainActivity extends AppCompatActivity {
    //  Tag for log messages
    private static final String TAG = "Eno";

    //  View of the screen
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

        //  Obtains a reference to the activity_main.xml layout
        mScreenView = findViewById(R.id.layout);
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

                //  Handles the touch by calling the drawCard method
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

    //  Accesses the deck file and draws a random card from it
    private void drawCard(View view) {
        Log.i(TAG, "Drawing a new card...");

        //  Obtains a reference to the subtitle TextView and hides it from the screen
        final TextView subtitle = findViewById(R.id.subtitle);
        subtitle.setVisibility(View.INVISIBLE);

        //  Obtains a reference to the title TextView of the layout;
        //  the TextView will be used to display the card
        final TextView card = findViewById(R.id.title);

        //  TODO: keep an array of drawn card indexes

        //  Obtains a reference to the assets directory
        AssetManager assets = view.getContext().getAssets();

        int nCards = 0;
        try {
            Log.i(TAG, "Opening file to count cards...");

            //  Opens the deck file & BufferedReader...
            InputStream deckStream = assets.open("deck");
            BufferedReader deckReader = new BufferedReader(new InputStreamReader(deckStream));

            //  Counts the cards in the deck file
            while (deckReader.readLine() != null) {
                nCards++;
            }
            //  ...BufferedReader is closed
            deckReader.close();

            Log.i(TAG, "Number of cards in deck: " + nCards);
        } catch (IOException e) {
            Log.e(TAG, "Error opening deck file...");
            e.printStackTrace();
        }

        //  Creates a String instance to which the drawn card's text will be assigned
        String cardContent = new String();
        try {
            Log.i(TAG, "Opening file to draw a random card...");

            //  Reopens the file & reader...
            InputStream deckStream = assets.open("deck");
            BufferedReader deckReader = new BufferedReader(new InputStreamReader(deckStream));

            //  Used for pseudo-random number generation
            Random cardToDraw = new Random();
            cardToDraw.setSeed(SystemClock.currentThreadTimeMillis());

            //  Generates a random number, no bigger than the value of nCards
            //  and iterates through the file up to that number
            for (int i = 0; i < cardToDraw.nextInt(nCards); i++)
                cardContent = deckReader.readLine();

            //  ...reader is finally closed
            deckReader.close();

            Log.i(TAG, cardContent);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        //  Sets the text to the value obtained from the deck
        if (cardContent != null)
            card.setText(cardContent);

        //  If the [blank] card was drawn, hides the TextView
        else if (cardContent.equals("[blank]"))
            card.setVisibility(View.INVISIBLE);

        else card.setText(R.string.card_missing);
    }
}
