package org.weglide.copilot;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.browser.customtabs.CustomTabsIntent;

public class MainActivity extends Activity {

    private static final String URL = "https://copilot.weglide.org";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use Chrome Custom Tabs - uses system browser with Service Worker support
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
            .setShowTitle(false)
            .setUrlBarHidingEnabled(true)
            .build();

        // Try Custom Tabs first
        try {
            customTabsIntent.launchUrl(this, Uri.parse(URL));
        } catch (Exception e) {
            // Fallback to regular browser intent
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
            startActivity(intent);
        }

        finish();
    }
}
