package heartbeat.social.tcs.socialhb.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import heartbeat.social.tcs.socialhb.R;
import heartbeat.social.tcs.socialhb.activity.modules.sub_modules.ViewStatus;
import heartbeat.social.tcs.socialhb.bean.Web_API_Config;
import heartbeat.social.tcs.socialhb.sqliteDb.DBHelper;

/**
 * Created by admin on 27/07/16.
 */
public class DonateClothFragment extends Fragment
{
    Button btnSubmit;
    EditText editText;
    NumberPicker numberPicker;

    String book_desc;
    int    num_of_books;

    SweetAlertDialog donateItemGetConfirmation, donateItemConfirmation, donateItemSuccessDialog;

    public DonateClothFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_donate_cloth, container, false);

        editText     = (EditText) rootView.findViewById(R.id.edtBook);
        numberPicker = (NumberPicker) rootView.findViewById(R.id.bookNumberPicker);
        btnSubmit    = (Button) rootView.findViewById(R.id.btnSubmit);

        numberPicker.setMaxValue(100);
        numberPicker.setMinValue(1);
        numberPicker.setWrapSelectorWheel(false);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                book_desc = editText.getText().toString();
                showWarningDialog();
            }
        });

        return rootView;
    }


    public void showWarningDialog(){

        donateItemGetConfirmation = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
        donateItemGetConfirmation.setTitleText("Are you sure?");
        donateItemGetConfirmation.setContentText("Do you want to donate "+numberPicker.getValue() + " cloths ?");
        donateItemGetConfirmation.setCancelText("No,cancel please!");
        donateItemGetConfirmation.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                donateItemGetConfirmation.cancel();
            }
        });
        donateItemGetConfirmation.setConfirmText("Proceed!");
        donateItemGetConfirmation.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {

                donateItemGetConfirmation.cancel();
                showDialogForPostingData();

            }
        });

        donateItemGetConfirmation.show();
    }



    public void showDialogForPostingData(){
        donateItemConfirmation = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        donateItemConfirmation.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        donateItemConfirmation.setTitleText("Loading...");
        donateItemConfirmation.setCancelable(false);
        donateItemConfirmation.show();


        try {
            //posting data to server
            postingDataToServer();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void postingDataToServer() throws JSONException {
        String url = Web_API_Config.donate_item;

        DBHelper dbHelper = new DBHelper(getActivity());
        String user_id    = dbHelper.getUserID();
        num_of_books      = numberPicker.getValue();
        JSONObject jsonObject  = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        JSONObject user_jsonObject = new JSONObject();
        user_jsonObject.put("id", user_id);

        jsonObject1.put("id", 2);
        jsonObject1.put("category", null);

        jsonObject2.put("id", 1);
        jsonObject2.put("status", null);


        jsonObject.put("user", user_jsonObject);
        jsonObject.put("donateCategory", jsonObject1);
        jsonObject.put("noOfItems", num_of_books);
        jsonObject.put("donateItemStatus", jsonObject2);
        jsonObject.put("description", book_desc);


        //Create JSONObjectRequest for Volley
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!

                        donateItemConfirmation.dismiss();
                        Log.e("DonateBookFragment", response.toString());
                        showSuccessDialog();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(getActivity()).add(jsonRequest);
    }

    public void showSuccessDialog(){
        donateItemSuccessDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
        donateItemSuccessDialog.setTitleText("You Have Successfully Done");
        donateItemSuccessDialog.setContentText("See Status of donated Items");
        donateItemSuccessDialog.show();

        donateItemSuccessDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Intent dashboardIntent = new Intent(getActivity(), ViewStatus.class);
                startActivity(dashboardIntent);
            }
        });

    }
}
