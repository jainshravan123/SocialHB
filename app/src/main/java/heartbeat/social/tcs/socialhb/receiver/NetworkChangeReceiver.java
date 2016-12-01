package heartbeat.social.tcs.socialhb.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;
import heartbeat.social.tcs.socialhb.activity.SignIn;
import heartbeat.social.tcs.socialhb.network.NetworkChangeUtil;

/**
 * Created by admin on 08/10/16.
 */
public class NetworkChangeReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {

        String status = NetworkChangeUtil.getConnectivityStatusString(context);

        new SweetAlertDialog(context.getApplicationContext(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText(status)
                .setContentText("Try Again")
                .show();
    }
}
