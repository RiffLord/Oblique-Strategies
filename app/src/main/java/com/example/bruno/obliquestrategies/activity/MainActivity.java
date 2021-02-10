package com.example.bruno.obliquestrategies.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bruno.obliquestrategies.R;
import com.example.bruno.obliquestrategies.fragment.CardFragment;
import com.example.bruno.obliquestrategies.util.DepthPageTransformer;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class MainActivity extends AppCompatActivity {
    private static final String PREF_THEME = "theme";
    private SharedPreferences mSettings;

    private static final int NUM_PAGES = 173;   //  Number of pages to display

    private ViewPager2 mViewPager;
    private FragmentStateAdapter mPagerAdapter;

    /**
     * A simple pager adapter that represents a series of ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) { super(fa); }

        @NonNull
        @Override
        public Fragment createFragment(int position) { return new CardFragment(); }

        @Override
        public int getItemCount() { return NUM_PAGES; }
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //  Instantiate a ViewPager2 and a PagerAdapter
        mViewPager = findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(this);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setPageTransformer(new DepthPageTransformer());

        //  Sets up the views for the Activity's graphical elements
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setNavigationBarButtonsColor(getWindow().getNavigationBarColor());

        mSettings = getSharedPreferences(PREF_THEME, MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.about) displayAboutDialog();
        //  This will be added when dark mode is correctly implemented
        //if (item.getItemId() == R.id.theme) displayThemeDialog();
        return super.onOptionsItemSelected(item);
    }

    private void displayAboutDialog() {
        MaterialAlertDialogBuilder aboutDialogBuilder = new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme);
        aboutDialogBuilder.setTitle(getResources().getString(R.string.app_name));
        aboutDialogBuilder.setMessage(getResources().getString(R.string.about));
        aboutDialogBuilder.setNegativeButton(getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog aboutDialog = aboutDialogBuilder.create();
        aboutDialog.show();
    }

    private void displayThemeDialog() {
        MaterialAlertDialogBuilder themeDialogBuilder = new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme);
        themeDialogBuilder.setTitle(getResources().getString(R.string.theme_title));
        themeDialogBuilder.setView(getSwitchView());
        themeDialogBuilder.setNegativeButton(getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog themeDialog = themeDialogBuilder.create();
        themeDialog.show();
    }

    private View getSwitchView() {
        View switchView = View.inflate(this, R.layout.theme_switch, null);
        final SwitchMaterial switchMaterial = (SwitchMaterial) switchView.findViewById(R.id.switch_dark_mode);
        switchMaterial.setChecked(loadSettings());
        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //  CHANGE APP THEME
                saveSettings(isChecked);
            }
        });
        return switchView;
    }

    private void saveSettings(boolean settings) {
        mSettings = getSharedPreferences(PREF_THEME, MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = mSettings.edit();
        prefEditor.putBoolean(PREF_THEME, settings).apply();
    }

    private boolean loadSettings() {
        return mSettings.getBoolean(PREF_THEME, false);
    }

    //  Makes navigation bar buttons dark if the background is light and vice versa
    private void setNavigationBarButtonsColor(int navigationBarColor) {
            View decorView = getWindow().getDecorView();
            int flags = decorView.getSystemUiVisibility();
            if (isColorLight(navigationBarColor)) {
                flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            } else {
                flags &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            }
            decorView.setSystemUiVisibility(flags);
    }

    private boolean isColorLight(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness < 0.5;
    }
}
