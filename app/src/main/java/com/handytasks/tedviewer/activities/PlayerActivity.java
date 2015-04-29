package com.handytasks.tedviewer.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.os.Bundle;
import android.view.MenuItem;

import com.handytasks.tedviewer.R;
import com.handytasks.tedviewer.Video;
import com.handytasks.tedviewer.fragments.PlayerFragment;

public class PlayerActivity extends FragmentActivity {

    private PlayerFragment mPlayerFragment;
    private int mPostition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        // getActionBar().setDisplayHomeAsUpEnabled(true);

        if ( null == savedInstanceState ) {
            mPostition = 0;
            android.app.FragmentManager fm = getFragmentManager();
            android.app.FragmentTransaction ft = fm.beginTransaction();
            mPlayerFragment = PlayerFragment.newInstance();
            ft.replace(R.id.standalone_player_container, mPlayerFragment, "player");
            ft.commit();
        } else {
            android.app.FragmentManager fm = getFragmentManager();
            mPlayerFragment = (PlayerFragment) fm.findFragmentByTag("player");
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPostition = savedInstanceState.getInt("position");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // TODO: get real playback position
        outState.putInt("position", mPlayerFragment.getPlaybackPosition());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Video video = getIntent().getParcelableExtra(MainActivity.VIDEO_ARG);
        if ( null != mPlayerFragment ) {
            mPlayerFragment.play(video, mPostition);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
