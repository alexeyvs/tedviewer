package com.handytasks.tedviewer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by avsho_000 on 4/8/2015.
 */
public class VideosAdapter extends ArrayAdapter<Video> {
    public VideosAdapter (Activity activity, List<Video> videos) {
        super(activity, 0, videos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Activity activity = (Activity) getContext();
        LayoutInflater inflater = activity.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.video_item, parent, true);
        Video item = getItem(position);

        TextView textView = (TextView) rowView.findViewById(R.id.title);
        textView.setText(item.getTitle());

        ImageView thumbnailImage = (ImageView) rowView.findViewById(R.id.thumbnail);
        thumbnailImage.setImageBitmap(item.getThumbnailImage());

        return rowView;
    }
}
