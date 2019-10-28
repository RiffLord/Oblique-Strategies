package com.example.bruno.obliquestrategies.util;

import android.content.res.AssetManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

//  TODO: CODE CLEANUP

public class Deck {
    private static final String TAG = "Deck";

    private AssetManager mAssets;
    private static int m_nCards = 0;
    private Random mCardToDraw;

    //  Any drawn card will be added to this list
    //  its contents will be compared each time to
    //  the next card drawn in order to ensure that
    //  it wasn't already drawn in the current session
    private ArrayList<Integer> mUsedCardIndexes;

    public Deck(View view) {
        mCardToDraw = new Random();
        //  Used for pseudo-random number generation
        mCardToDraw.setSeed(SystemClock.currentThreadTimeMillis());

        mUsedCardIndexes = new ArrayList<Integer>();
        Log.i(TAG, "mUsedCardIndexes.isEmpty(): " + mUsedCardIndexes.isEmpty());

        //  Obtains a reference to the mAssets directory
        mAssets = view.getContext().getAssets();

        try {
            Log.i(TAG, "Opening file to count cards...");

            //  Opens the deck file & BufferedReader...
            InputStream deckStream = mAssets.open("deck");
            BufferedReader deckReader = new BufferedReader(new InputStreamReader(deckStream));

            //  Counts the cards in the deck file
            while (deckReader.readLine() != null) {
                m_nCards++;
            }
            //  ...BufferedReader is closed
            deckReader.close();

            Log.i(TAG, "Number of cards in deck: " + m_nCards);
        } catch (IOException e) {
            Log.e(TAG, "Error opening deck file...");
            e.printStackTrace();
        }
    }

    private int shuffle() {
        //  Generates a random number, no bigger than the value of m_nCards
        int nNextCard = mCardToDraw.nextInt(m_nCards);
        Log.i(TAG, "New card index: " + nNextCard);

        if (mUsedCardIndexes.isEmpty()) {
            //  Adds the index to the list of drawn card indexes
            mUsedCardIndexes.add(nNextCard);
        } else {
            if (!(mUsedCardIndexes.contains(nNextCard)))
                mUsedCardIndexes.add(nNextCard);

            else shuffle();
        }

        return nNextCard;
    }

    //  Accesses the deck file and draws a random card from it
    public String drawCard() {
        Log.i(TAG, "Drawing a new card...");

        //  Creates a String instance to which the drawn card's text will be assigned
        String cardContent = new String();
        try {
            Log.i(TAG, "Opening file to draw a random card...");

            //  Reopens the file & reader...
            InputStream deckStream = mAssets.open("deck");
            BufferedReader deckReader = new BufferedReader(new InputStreamReader(deckStream));

            //  and iterates through the file up to that number
            int cardPosition = shuffle();
            for (int i = 0; i < cardPosition; i++) {
                cardContent = deckReader.readLine();
            }

            //  ...reader is finally closed
            deckReader.close();

            Log.i(TAG, cardContent);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        //  Sets the text to the value obtained from the deck
        if (cardContent != null)
            return cardContent;

            //  If the [blank] card was drawn, hides the TextView
        else if (cardContent.equals("[blank]"))
            return "";

        else return "Card missing...";
    }
}
