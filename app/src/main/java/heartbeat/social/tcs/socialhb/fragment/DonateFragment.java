package heartbeat.social.tcs.socialhb.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import heartbeat.social.tcs.socialhb.activity.modules.sub_modules.DonateItem;
import heartbeat.social.tcs.socialhb.R;
import heartbeat.social.tcs.socialhb.activity.modules.sub_modules.ViewStatus;

/**
 * Created by admin on 27/07/16.
 */
public class DonateFragment extends Fragment
{

    Button btnDonateItem, btnViewStatus;


    public DonateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_donate, container, false);

        btnDonateItem = (Button) rootView.findViewById(R.id.donateItem);
        btnViewStatus = (Button) rootView.findViewById(R.id.viewStatus);

        btnDonateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(getActivity(), DonateItem.class);
                startActivity(intent1);
            }
        });

        btnViewStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), ViewStatus.class);
                startActivity(intent1);
            }
        });

        return rootView;
    }

}
