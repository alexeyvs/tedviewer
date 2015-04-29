package com.handytasks.tedviewer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Video implements Parcelable {
    private String title;
    private String description;
    private String thumbnailURL;
    private String videoURL;
    private Bitmap mThumbnail;

    public Video (Video in) {
        title = in.title;
        description = in.description;
        thumbnailURL = in.thumbnailURL;
        videoURL = in.videoURL;
        mThumbnail = in.mThumbnail;
    }

    private Video(Parcel in) {
        title = in.readString();
        description = in.readString();
        thumbnailURL = in.readString();
        videoURL = in.readString();
        if (in.dataAvail() > 0 ) {
            mThumbnail = Bitmap.CREATOR.createFromParcel(in);
        }
    }

    public Video() {

    }

    public void removeThumbnail () {
        mThumbnail = null;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = getCData(description);
    }
    public String getDescription() {
        return description;
    }
    private String getCData(String data){
        return data.replaceAll("<!\\[CDATA\\[", "").replaceAll("\\]\\]>", "");
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        //TODO: handle errors
        //TODO: load async
        this.thumbnailURL = thumbnailURL;
        URL url = null;
        try {
            url = new URL(thumbnailURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        assert url != null;
        try {
            mThumbnail = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Uri getVideoUri() {
        return Uri.parse(videoURL);
    }
    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }
    public Bitmap getThumbnailImage() {
        return mThumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(thumbnailURL);
        dest.writeString(videoURL);
        if ( null != mThumbnail ) {
            mThumbnail.writeToParcel(dest, flags);
        }
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

}