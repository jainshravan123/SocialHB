package heartbeat.social.tcs.socialhb.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import heartbeat.social.tcs.socialhb.R;

/**
 * Created by admin on 27/07/16.
 */
public class ReuseFragment extends Fragment
{

    public ReuseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_reuse, container, false);
        return rootView;
    }

}
