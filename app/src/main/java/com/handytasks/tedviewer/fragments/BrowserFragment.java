package com.handytasks.tedviewer.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handytasks.tedviewer.R;
import com.handytasks.tedviewer.RssHandler;
import com.handytasks.tedviewer.Video;
import com.handytasks.tedviewer.VideosAdapter;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BrowserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BrowserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrowserFragment extends Fragment {
    private VideosAdapter mAdapter;

    private ArrayList <Video> mVideosList;
    private View mView;
    private OnFragmentInteractionListener mListener;
    private ProgressDialog mLoadingProgress;

    public static BrowserFragment newInstance() {
        return new BrowserFragment();
    }

    public BrowserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ( null == savedInstanceState ) {
            mVideosList = new ArrayList<>();
        } else {
            mVideosList = savedInstanceState.getParcelableArrayList("videos");
        }
        mAdapter = new VideosAdapter(getActivity(), mVideosList);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // store loaded videos info
        outState.putParcelableArrayList("videos", mVideosList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_browser, container, false);

        ListView listView = (ListView) mView.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ( null != mListener ) {
                    mListener.OnPlay((Video)parent.getAdapter().getItem(position));
                }
            }
        });

        ((ListView)mView.findViewById(R.id.listView)).setAdapter(mAdapter);
        return mView;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void OnPlay (Video video);
    }

    private void doRefresh () {
        new ReadFeedsTask().execute();
    }

    public void refresh(boolean force) {
        if ( false == force ) {
            if ( mVideosList.size() == 0 ) {
                doRefresh();
            }
        } else {
            doRefresh();
        }
    }

    private class ReadFeedsTask extends AsyncTask<Void, Integer, ArrayList<Video>> {
        private final String RSS_ADDRESS = "http://www.ted.com/talks/rss";

        @Override
        protected ArrayList<Video> doInBackground(Void... params) {
            // TODO: handle errors
            URL url = null;
            try {
                url = new URL(RSS_ADDRESS);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = null;
            try {
                sp = spf.newSAXParser();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            assert sp != null;
            XMLReader xr = null;
            try {
                xr = sp.getXMLReader();
            } catch (SAXException e) {
                e.printStackTrace();
            }

            final int[] i = {1};
            RssHandler rh = new RssHandler();
            rh.setProgressHandler(new RssHandler.IReportProgress() {
                @Override
                public void reportProgress() {
                    publishProgress(i[0]++);
                }
            });

            xr.setContentHandler(rh);
            assert url != null;
            try {
                xr.parse(new InputSource(url.openStream()));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }

            return rh.getVideoList();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mLoadingProgress.setProgress(values[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingProgress = new ProgressDialog(getActivity());
            mLoadingProgress.setTitle(getActivity().getString(R.string.loading_feed));
            mLoadingProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mLoadingProgress.setIndeterminate(false);
            mLoadingProgress.setMax(RssHandler.ARTICLES_LIMIT);
            mLoadingProgress.show();
        }

        @Override
        protected void onPostExecute(ArrayList<Video> res) {
            super.onPostExecute(res);
            // new Toast(getApplicationContext()).makeText(getApplicationContext(), String.format("%d articles fetched", res.size()), Toast.LENGTH_LONG).show();
            mVideosList = res;
            if ( getActivity() != null ) {
                mAdapter = new VideosAdapter(BrowserFragment.this.getActivity(), mVideosList);
                ((ListView) mView.findViewById(R.id.listView)).setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
            mLoadingProgress.dismiss();
        }
    }
}
