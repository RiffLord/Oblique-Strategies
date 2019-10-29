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

//  Utility class which accesses the deck file using an AssetManager and draws random cards
public class Deck {
    private static final String TAG = "Deck";

    private AssetManager mAssets;

    private static int m_nCards = 0;
    private Random mCardToDraw;

    //  The indexes of drawn cards are added to this list
    private ArrayList<Integer> mUsedCardIndexes;

    //  Sets everything up to draw new cards
    public Deck(View view) {
        //  Sets up pseudo-random number generation
        mCardToDraw = new Random();
        mCardToDraw.setSeed(SystemClock.currentThreadTimeMillis());

        mUsedCardIndexes = new ArrayList<>();

        //  Obtains a reference to the assets directory
        mAssets = view.getContext().getAssets();

        try {
            //  Opens the deck file & BufferedReader...
            InputStream deckStream = mAssets.open("deck");
            BufferedReader deckReader = new BufferedReader(new InputStreamReader(deckStream));

            //  Counts the cards in the deck file
            while (deckReader.readLine() != null) {
                m_nCards++;
            }
            //  ...BufferedReader is closed
            deckReader.close();

            Log.i(TAG, "Cards: " + m_nCards);
        } catch (IOException e) {
            Log.e(TAG, "Error opening deck file...");
            e.printStackTrace();
        }
    }

    //  Returns a random integer representing the position of a card within the deck
    private int shuffle() {
        //  Generates a random number, no bigger than the size of the deck
        int nNextCard = mCardToDraw.nextInt(m_nCards);
        Log.i(TAG, "New card index: " + nNextCard);

        if (mUsedCardIndexes.isEmpty()) {
            //  Adds the index to the list of drawn card indexes
            mUsedCardIndexes.add(nNextCard);
        } else if (mUsedCardIndexes.size() == m_nCards) {   //  If the size of the drawn card list is equal to the size of the deck
            //  Clears the list allowing the user to continue drawing cards
            mUsedCardIndexes.clear();
            mUsedCardIndexes.add(nNextCard);
        } else {
            if (!(mUsedCardIndexes.contains(nNextCard)))
                mUsedCardIndexes.add(nNextCard);

            //  If the number generated is already in the list
            //  the method calls itself to generate a new number
            else shuffle();
        }

        return nNextCard;
    }

    //  Accesses the deck file and draws a random card from it, returning it as a String
    public String drawCard() {
        //  Creates a String instance to which the drawn card's text will be assigned
        String cardContent = new String();
        try {
            //  Opens the file & reader...
            InputStream deckStream = mAssets.open("deck");
            BufferedReader deckReader = new BufferedReader(new InputStreamReader(deckStream));

            //  ...obtains a number...
            int cardPosition = shuffle();
            //  ...and iterates through the file up to the specified position
            for (int i = 0; i < cardPosition; i++) {
                cardContent = deckReader.readLine();
            }

            deckReader.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        if (cardContent != null)
            return cardContent;

        //  TODO: fix this
        //  If the [blank] card was drawn, hides the TextView
        if (cardContent == "[blank]\n")
            return "blank";

        else return "Card missing...";
    }
}
