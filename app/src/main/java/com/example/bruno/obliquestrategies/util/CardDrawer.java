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
public class CardDrawer {
    private static final String TAG = "CardDrawer";

    private final AssetManager mAssets;

    private static int mCardCount;
    private Random mCardToDraw;

    //  The indexes of drawn cards are added to this list
    private static ArrayList<String> mDrawnCardsList;

    //  Sets everything up to draw new cards
    public CardDrawer(View view) {
        mCardCount = 0;
        mDrawnCardsList = new ArrayList<>();

        //  Sets up pseudo-random number generation
        mCardToDraw = new Random();
        mCardToDraw.setSeed(SystemClock.currentThreadTimeMillis());

        //  Obtains a reference to the assets directory
        mAssets = view.getContext().getAssets();

        try {
            //  Opens the deck file & BufferedReader...
            InputStream deckStream = mAssets.open("deck");
            BufferedReader deckReader = new BufferedReader(new InputStreamReader(deckStream));

            //  Counts the cards in the deck file
            while (deckReader.readLine() != null) {
                mCardCount++;
            }
            //  ...BufferedReader is closed
            deckReader.close();
        } catch (IOException e) {
            Log.e(TAG, "Error opening deck file...");
            e.printStackTrace();
        }
    }

    //  Returns a random integer representing the position of a card within the deck
    private int shuffle() {
        //  Generates a random number, no bigger than the size of the deck
        int nNextCard = mCardToDraw.nextInt(mCardCount);
        Log.i(TAG, "New card index: " + nNextCard);

        if (mDrawnCardsList.size() == mCardCount)
            mDrawnCardsList.clear();    //  Clears the list allowing the user to continue drawing cards

        return nNextCard;
    }

    //  Accesses the deck file and draws a random card from it, returning it as a String
    public String drawCard() {
        String cardText = "";
        try {
            //  Opens the file & reader...
            InputStream deckStream = mAssets.open("deck");
            BufferedReader deckReader = new BufferedReader(new InputStreamReader(deckStream));

            //  ...obtains a number...
            int cardPosition = shuffle();
            //  ...and iterates through the file up to the specified position
            for (int i = 0; i < cardPosition; i++) cardText = deckReader.readLine();

            if (mDrawnCardsList.contains(cardText)) drawCard();  //  Obtains a new card index in case the current card was previously drawn

            deckReader.close();

            Log.d(TAG, cardPosition + ": " + cardText);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        return formatText(cardText);
    }

    //  Returns a formatted String, ready to be displayed on-screen
    private String formatText(String sCard) {
        String formattedCard = "";

        if (sCard.contains(" -")) {
            formattedCard = sCard.replaceAll(" -", "\n");
            sCard = formattedCard;
        }

        if (sCard.contains(" _")) {
            formattedCard = sCard.replaceAll(" _", "\n\n");
            sCard = formattedCard;
        }

        return sCard;
    }
}
