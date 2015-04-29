package com.handytasks.tedviewer.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.handytasks.tedviewer.R;
import com.handytasks.tedviewer.Video;

public class PlayerFragment extends Fragment {
    private VideoView mVideoView;

    private View mView;

    public static PlayerFragment newInstance() {
        return new PlayerFragment();
    }

    public PlayerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_player, container, false);
        mView.findViewById(R.id.no_video_selected_layout).setVisibility(View.VISIBLE);
        return mView;
    }

    public int getPlaybackPosition() {
        return mVideoView.getCurrentPosition();
    }

    public void play(Video video, int postition) {
        ((TextView)mView.findViewById(R.id.title)).setText(video.getTitle());
        ((TextView)mView.findViewById(R.id.description)).setText(video.getDescription());
        // hide select video layout
        mView.findViewById(R.id.no_video_selected_layout).setVisibility(View.GONE);

        mVideoView = (VideoView)mView.findViewById(R.id.video_view);
        mVideoView.setMediaController(new MediaController(getActivity()));
        mVideoView.setVideoURI(video.getVideoUri());
        mVideoView.requestFocus();

        mVideoView.start();
        mVideoView.seekTo(postition);
    }

}
