/**
 * Copyright (C) 2013 Johannes Schnatterer
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This file is part of nusic.
 *
 * nusic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * nusic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with nusic.  If not, see <http://www.gnu.org/licenses/>.
 */
package info.schnatterer.nusic.android.activities;

import info.schnatterer.nusic.android.util.TextUtil;
import info.schnatterer.nusic.ui.R;
import roboguice.activity.RoboActionBarActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Activity that loads a website from an URL and displays it in a text view.
 *
 * The URI to the website that is displayed can be passed to the activity using
 * {@link Intent#putExtra(String, String)} with {@link #EXTRA_URL} as key.<br/>
 * <br/>
 * This will also work for email links (<code>mailto:</code>).URLs like<br/>
 * <code>mailto:x@y.z?subject=@string/someResource&amp;body=Your message here</code>
 * <br/>
 * will result in an intent {@link Intent#ACTION_SENDTO}, i.e. the user's
 * preferred email app is initialized with the parameters passed to this
 * activity. Note that any <code>@string/</code> parameters are localized before
 * starting the new activity.
 * <br/>
 * In addition a title for the share button can be added using
 * {@link Intent#putExtra(String, String)} with {@link #EXTRA_SUBJECT} as key.
 */
public class NusicWebViewActivity extends RoboActionBarActivity {

    /**
     * ID of the URL that is shown by this acivity. Its a {@link String} extra passed to this
     * activity via {@link Intent}.
     */
    public static final String EXTRA_URL = NusicWebViewActivity.class.getCanonicalName() + ".url";
    /**
     * ID of the subject that is added to the EXTRA_URL parameter when pressing the share button in
     * this activity. Its a {@link String} extra passed to this activity via {@link Intent}.
     */
    public static final String EXTRA_SUBJECT = NusicWebViewActivity.class.getCanonicalName() + ".title";

    /** "Protocol" prefix of a link for E-mails. */
    private static final String MAILTO_LINK = "mailto:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        WebView webView = (WebView) findViewById(R.id.webview);

        // Display the back arrow in the header (left of the icon)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String url = getExtraOrEmpty(EXTRA_URL);
        if (url.startsWith(MAILTO_LINK)) {
            Intent send = new Intent(Intent.ACTION_SENDTO);
            Uri uri = Uri.parse(TextUtil.replaceResourceStrings(this, url));

            send.setData(uri);
            startActivity(send);
            finish();
        } else {
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setBuiltInZoomControls(true);

            webView.loadUrl(url);

            /* Prevent WebView from Opening the Browser on external links */
            webView.setWebViewClient(new InsideWebViewClient());
        }
    }

    /* Class that prevents opening the Browser */
    private class InsideWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.webview, menu);

        MenuItem shareItem = menu.findItem(R.id.action_share);
        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        shareActionProvider.setShareIntent(createShareIntent());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // When the back arrow in the header (left of the icon) is clicked,  "go back one activity"
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Creates a share intent for the URL of the view for a specific {@code menuItem}.
     */
    @VisibleForTesting
    Intent createShareIntent() {
           return new Intent(Intent.ACTION_SEND)
                .setType("text/plain")
                .putExtra(Intent.EXTRA_SUBJECT, getExtraOrEmpty(EXTRA_SUBJECT))
                .putExtra(Intent.EXTRA_TEXT, createShareText());
    }

    @NonNull
    private String createShareText() {
        String url = getExtraOrEmpty(EXTRA_URL);
        if (!"".equals(url)) {
            url += "\n" + getString(R.string.NusicWebViewActivity_sharedViaNusic);
        }
        return url;
    }

    @NonNull
    private String getExtraOrEmpty(String extra) {
        String stringExtra = getIntent().getStringExtra(extra);
        if (stringExtra != null) {
            return stringExtra;
        } else {
            return "";
        }
    }
}
