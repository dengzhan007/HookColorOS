package com.wuxianlin.hookcoloros;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !checkLSPosed(this)) {
            SettingsManager.getInstance(this).fixFolderPermissionsAsync();
        }
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private static boolean checkResult = false;
    private static boolean checked = false;

    @SuppressLint("WorldReadableFiles")
    public static boolean checkLSPosed(Context context) {
        if (checked)
            return checkResult;
        try {
            context.getSharedPreferences(context.getPackageName() + "_preferences",
                    Context.MODE_WORLD_READABLE);
            checkResult = true;
            checked = true;
        } catch (SecurityException exception) {
            Toast.makeText(context, "LuckyHooker Settings may not work", Toast.LENGTH_LONG).show();
            checkResult = false;
            checked = true;
        }
        return checkResult;
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                getPreferenceManager().setSharedPreferencesMode(Context.MODE_WORLD_READABLE);
            } else if (!checkLSPosed(getContext())) {
                getPreferenceManager().setStorageDeviceProtected();
            }
        }
    }
}