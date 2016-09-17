package heartbeat.social.tcs.socialhb.network;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import heartbeat.social.tcs.socialhb.R;

/**
 * Created by admin on 18/07/16.
 */
public class CheckInternetConnection {

    public boolean checkingInternetConnection(Context activityContext){


        ConnectivityManager connectivity = (ConnectivityManager) activityContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }


    public void showSnackBar(Context context, Activity activity)
    {
       /* final Snackbar snackbar = Snackbar.make(activity.findViewById(R.id.drawer_layout), "No Internet Connection", Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
        snackbar.setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });*/

        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
    }


    public void showNetworkIdentifier(Context context, Activity activity){
        if(!(checkingInternetConnection(context))){
            showSnackBar(context, activity);
        }
    }

}
