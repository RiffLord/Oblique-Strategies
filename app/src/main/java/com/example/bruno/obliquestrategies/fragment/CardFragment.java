package com.example.bruno.obliquestrategies.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bruno.obliquestrategies.R;
import com.example.bruno.obliquestrategies.util.CardDrawer;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardFragment extends Fragment {

    private CardDrawer mCardDrawer;

    private MaterialTextView mCard;
    private MaterialCardView mCardView;

    private View mLayoutView;

    public CardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CardFragment.
     */
    public static CardFragment newInstance(String param1, String param2) {
        CardFragment fragment = new CardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("card", mCard.getText().toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.white_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLayoutView = Objects.requireNonNull(getView()).findViewById(R.id.card_layout_white);
        mCardView = mLayoutView.findViewById(R.id.card);
        mCard = getView().findViewById(R.id.card_text);;

        //  Should prevent reinitializing the CardDrawer to make sure each card will be drawn at least once
        if (mCardDrawer == null) {
            mCardDrawer = new CardDrawer(mLayoutView);
            mCard.setText(mCardDrawer.drawCard());
        }

        //  Restores the state if the Activity is restarted
        if (savedInstanceState != null) mCard.setText(savedInstanceState.getString("card"));
    }
}