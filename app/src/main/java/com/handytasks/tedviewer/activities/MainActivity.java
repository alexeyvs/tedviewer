package com.handytasks.tedviewer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.handytasks.tedviewer.R;
import com.handytasks.tedviewer.Video;
import com.handytasks.tedviewer.fragments.BrowserFragment;
import com.handytasks.tedviewer.fragments.PlayerFragment;


public class MainActivity extends FragmentActivity
        implements BrowserFragment.OnFragmentInteractionListener
{

    private static final String TAG = "MainActivity";
    public static final String VIDEO_ARG = "video";
    private BrowserFragment mBrowserFragment;
    private PlayerFragment mPlayerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setup layout and fragments depending on screen size
        if ( getResources().getBoolean(R.bool.multipane) ) {
            setContentView(R.layout.activity_main_multipane);
            setupPlayerFragment();
        } else {
            setContentView(R.layout.activity_main);
        }

        setupBrowserFragment();
    }

    private void setupPlayerFragment () {
        android.app.FragmentManager fm = getFragmentManager();
        mPlayerFragment = (PlayerFragment) fm.findFragmentByTag("player");
        if (null == mPlayerFragment) {
            mPlayerFragment = PlayerFragment.newInstance();
            android.app.FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.player_container, mPlayerFragment, "player");
            ft.commit();
        }
    }

    private void setupBrowserFragment ()
    {
        android.app.FragmentManager fm = getFragmentManager();
        mBrowserFragment = (BrowserFragment) fm.findFragmentByTag("browser");
        if ( null == mBrowserFragment ) {
            mBrowserFragment = BrowserFragment.newInstance();
            android.app.FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.browser_container, mBrowserFragment, "browser");
            ft.commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBrowserFragment.refresh(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                mBrowserFragment.refresh(true);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnPlay(Video video) {
        if (getResources().getBoolean(R.bool.multipane) && mPlayerFragment != null) {
            mPlayerFragment.play(video, 0);
        } else {
            Intent intent = new Intent(this, PlayerActivity.class);
            // clone object without thumbnail to avoid TransactionTooLargeException
            Video arg = new Video(video);
            arg.removeThumbnail();
            intent.putExtra(VIDEO_ARG, arg);
            startActivity(intent);
        }
    }
}
