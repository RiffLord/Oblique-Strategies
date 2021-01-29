package com.example.bruno.obliquestrategies.util;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class DepthPageTransformer implements ViewPager2.PageTransformer {
    private static final float MIN_SCALE = 0.75f;

    @Override
    public void transformPage(@NonNull View page, float position) {
        int pageWidth = page.getWidth();

        if (position < -1) { // [-Infinity, -1)
            page.setAlpha(0f);
        } else if (position <= 0) { //  [-1, 0]
            // Use the default slide transition when moving to the left page
            page.setAlpha(1f);
            page.setTranslationX(0f);
            page.setTranslationZ(0f);
            page.setScaleX(1f);
            page.setScaleY(1f);
        } else if (position <= 1) { //  (0, 1]
            //  Fade the page out
            page.setAlpha(1 - position);

            //  Counteract the default slide transition
            page.setTranslationX(pageWidth * -position);
            //  Move it behind the left page
            page.setTranslationZ(-1f);

            //  Scale the page down between MIN_SCALE and 1
            float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
        } else page.setAlpha(0f);   //  (1, Infinity]
    }
}
