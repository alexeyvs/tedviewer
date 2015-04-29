package com.handytasks.tedviewer;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RssHandler extends DefaultHandler {

    private static final String ITEM_TAG = "item";

    private Video currentVideo = new Video();
    private final ArrayList<Video> VideoList = new ArrayList<>();
    private int videosParsed = 0;

    // Number of articles to download
    public static final int ARTICLES_LIMIT = 30;

    private StringBuffer mElementValue = new StringBuffer();
    private IReportProgress mProgressHandler;

    public ArrayList<Video> getVideoList() {
        return VideoList;
    }

    public interface IReportProgress {
        public void reportProgress();
    }
    public void setProgressHandler (IReportProgress handler) {
        mProgressHandler = handler;
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) {
        mElementValue = new StringBuffer();
        if (qName.equalsIgnoreCase("media:thumbnail")) {
            currentVideo.setThumbnailURL(atts.getValue("url"));
        } else if (qName.equalsIgnoreCase("media:content")) {
            currentVideo.setVideoURL(atts.getValue("url"));
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (localName.equalsIgnoreCase("title")){
            currentVideo.setTitle(mElementValue.toString());
        } else if (localName.equalsIgnoreCase("description")){
            currentVideo.setDescription(mElementValue.toString());
        } else if (localName.equals(ITEM_TAG)) {
            if ( mProgressHandler != null ) {
                mProgressHandler.reportProgress();
            }
        }

        // Check if looking for article, and if article is complete
        if (true == localName.equals(ITEM_TAG)) {

            VideoList.add(currentVideo);

            currentVideo = new Video();

            videosParsed++;

            if (videosParsed >= ARTICLES_LIMIT)
            {
                throw new SAXException();
            }
        }
    }

    public void characters(char ch[], int start, int length) {
        mElementValue.append(new String(ch, start, length));
    }
}