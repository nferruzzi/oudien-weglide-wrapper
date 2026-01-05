package org.weglide.copilot;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class MainActivity extends Activity {

    private static final String URL = "https://copilot.weglide.org";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open in system browser (which supports Service Workers)
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
        startActivity(intent);

        // Close this activity
        finish();
    }
}
