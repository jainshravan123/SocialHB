package heartbeat.social.tcs.socialhb.activity.nav_drawer_activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;
import heartbeat.social.tcs.socialhb.R;
import heartbeat.social.tcs.socialhb.activity.Dashboard;
import heartbeat.social.tcs.socialhb.bean.User;
import heartbeat.social.tcs.socialhb.bean.UserProfile;
import heartbeat.social.tcs.socialhb.bean.Web_API_Config;
import heartbeat.social.tcs.socialhb.network.CheckInternetConnection;
import heartbeat.social.tcs.socialhb.session.SessionManager;
import heartbeat.social.tcs.socialhb.sqliteDb.DBHelper;
import heartbeat.social.tcs.socialhb.sqliteDb.ProfileDBHelper;

public class Profile extends AppCompatActivity {


    private AppBarLayout app_bar;
    private NestedScrollView nested_scroll;
    private ImageButton user_profile_photo;
    private FloatingActionButton floatingActionButton;
    private ProgressBar prgBar1;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private ImageView profile_back;
    private TextView  emp_id, first_name, last_name, home_contact_number, home_country_code, work_contact_number, work_country_code,  official_email,
            personal_email, offc_add, resedential_add;
    Toolbar toolbar;

    int s_id, s_user_id;
    String s_emp_id, s_first_name, s_last_name, s_home_contact_number, s_home_country_code, s_work_contact_number, s_work_country_code, s_official_email, s_personal_email, s_offc_add, s_resedential_add, s_profile_image, s_profile_back_image;

    private EditText profile_edit_input_first_name, profile_edit_input_last_name, profile_edit_home_contact_number, profile_edit_work_contact_number, profile_edit_personal_email, profile_edit_home_address;
    private Button   profile_edit_submit_btn;
    private SweetAlertDialog profileUpdateDialg, warningProfileUpdateDialog, profileUpdatedSuccessfully;
    String updated_first_name, updated_last_name, updated_home_contact_number, updated_home_country_code, updated_work_contact_number, updated_work_country_code, updated_personal_email, updated_home_address;
    int PICK_IMAGE_REQUEST = 1001;

    Bitmap bitmap;
    ImageView updatedImageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        app_bar = (AppBarLayout) findViewById(R.id.app_bar);
        profile_back = (ImageView) findViewById(R.id.profile_back);
        nested_scroll = (NestedScrollView) findViewById(R.id.nested_scroll);
        user_profile_photo = (ImageButton) findViewById(R.id.user_profile_photo);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        prgBar1 = (ProgressBar) findViewById(R.id.prgBar1);


        home_contact_number = (TextView) findViewById(R.id.tvNumber1);
        work_contact_number = (TextView) findViewById(R.id.tvNumber2);
        personal_email      = (TextView) findViewById(R.id.tvNumber3);
        official_email      = (TextView) findViewById(R.id.tvNumber4);
        resedential_add     = (TextView) findViewById(R.id.tvNumber5);
        offc_add            = (TextView) findViewById(R.id.tvNumber6);
        emp_id              = (TextView) findViewById(R.id.tvNumber7);



        toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //getSupportActionBar().setTitle("Name");


        user_profile_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                openProfilePicDialog();
            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfileEditDialog();
               // Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_LONG).show();
            }
        });
        getProfileInformation();
    }



    private void getProfileInformation(){


        DBHelper dbHelper = new DBHelper(getApplicationContext());
        String id = dbHelper.getUserID();

        String url = Web_API_Config.user_profile_api+id;



        //Create JSONObjectRequest for Volley
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!

                            prgBar1.setVisibility(View.GONE);
                            app_bar.setVisibility(View.VISIBLE);
                            nested_scroll.setVisibility(View.VISIBLE);
                            user_profile_photo.setVisibility(View.VISIBLE);
                            floatingActionButton.setVisibility(View.VISIBLE);


                        try {
                             s_id                     = response.getInt("id");
                             s_user_id                = response.getInt("user_id");
                             s_emp_id              = response.getString("emp_id");
                             s_first_name          = response.getString("first_name");
                             s_last_name           = response.getString("last_name");
                             s_home_contact_number = response.getString("home_contact_number");
                             s_home_country_code   = response.getString("home_country_code");
                             s_work_contact_number = response.getString("work_contact_number");
                             s_work_country_code   = response.getString("work_country_code");
                             s_official_email      =  response.getString("official_email");
                             s_personal_email      = response.getString("personal_email");
                             s_offc_add            = response.getString("offc_add");
                             s_resedential_add     = response.getString("resedential_add");
                             s_profile_image       = response.getString("profile_image");
                             s_profile_back_image  = response.getString("profile_back_image");


                            if(checkEmptyField(s_home_contact_number)){
                                home_contact_number.setText("("+s_home_country_code+")"+" "+s_home_contact_number);
                            }
                            else{
                                home_contact_number.setText("Not Avaialable");
                            }
                            if(checkEmptyField(s_work_contact_number)){
                                work_contact_number.setText("("+s_work_country_code+")"+" "+s_work_contact_number);
                            }
                            else{
                                work_contact_number.setText("Not Avaialable");
                            }
                            if(checkEmptyField(s_personal_email)){
                                personal_email.setText(s_personal_email);
                            }
                            else{
                                personal_email.setText("Not Avaialable");
                            }
                            if(checkEmptyField(s_official_email)){
                                official_email.setText(s_official_email);
                            }
                            else{
                                official_email.setText("Not Avaialable");
                            }
                            if(checkEmptyField(s_resedential_add)){
                                resedential_add.setText(s_resedential_add);
                            }
                            else{
                                resedential_add.setText("Not Avaialable");
                            }
                            if(checkEmptyField(s_offc_add)){
                                offc_add.setText(s_offc_add);
                            }
                            else{
                                offc_add.setText("Not Avaialable");
                            }
                            if(checkEmptyField(s_emp_id)){
                                emp_id.setText(s_emp_id);
                            }
                            else{
                                emp_id.setText("Not Avaialable");
                            }

                            collapsingToolbarLayout.setTitle(s_first_name);
                            collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBarPlus1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);

    }



    //Opening SignUp Custom Dialog
    public void openProfileEditDialog(){

        MaterialDialog.Builder builder = new MaterialDialog.Builder(Profile.this)
                .title("Edit Profile")
                .titleColor(Color.BLACK)
                .customView(R.layout.profile_edit_layout, true)
                .negativeText("Cancel")
                .positiveColorRes(R.color.black_color)
                .negativeColorRes(R.color.gray_btn_bg_color)
                .canceledOnTouchOutside(false)
                .autoDismiss(false);

        final MaterialDialog materialDialog = builder.build();
        materialDialog.show();

        View view1 = materialDialog.getCustomView();


       profile_edit_input_first_name          = (EditText) view1.findViewById(R.id.input_first_name);
       profile_edit_input_last_name           = (EditText) view1.findViewById(R.id.input_last_name);
       profile_edit_home_contact_number       = (EditText) view1.findViewById(R.id.input_home_contact_number);
       profile_edit_work_contact_number       = (EditText) view1.findViewById(R.id.input_work_contact_number);
       profile_edit_personal_email            = (EditText) view1.findViewById(R.id.input_personal_email);
       profile_edit_home_address              = (EditText) view1.findViewById(R.id.input_home_address);
       profile_edit_submit_btn                = (Button)   view1.findViewById(R.id.profileUpdateSubmitBtn);


        profile_edit_input_first_name.setText(s_first_name);
        profile_edit_input_last_name.setText(s_last_name);
        profile_edit_home_contact_number.setText(s_home_contact_number);
        profile_edit_work_contact_number.setText(s_home_contact_number);
        profile_edit_personal_email.setText(s_personal_email);
        profile_edit_home_address.setText(s_resedential_add);


        profile_edit_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                warningProfileUpdateDialog = new SweetAlertDialog(Profile.this, SweetAlertDialog.WARNING_TYPE);
                warningProfileUpdateDialog.setTitleText("Are you sure?");
                warningProfileUpdateDialog.setContentText("Do you really want to update your profile?");
                warningProfileUpdateDialog.setCancelText("No,cancel please!");
                warningProfileUpdateDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        warningProfileUpdateDialog.cancel();
                    }
                });
                warningProfileUpdateDialog.setConfirmText("Update It!");
                warningProfileUpdateDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        warningProfileUpdateDialog.cancel();

                        profileUpdateDialg = new SweetAlertDialog(Profile.this, SweetAlertDialog.PROGRESS_TYPE);
                        profileUpdateDialg.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        profileUpdateDialg.setTitleText("Updating...");
                        profileUpdateDialg.setCancelable(false);
                        profileUpdateDialg.show();

                        //updating profile data
                        updateProfileData();


                    }
                });

                warningProfileUpdateDialog.show();


            }
        });


        View negative  = materialDialog.getActionButton(DialogAction.NEGATIVE);
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });
    }


    public void updateProfileData(){


        updated_first_name          = profile_edit_input_first_name.getText().toString();
        updated_last_name           = profile_edit_input_last_name.getText().toString();
        updated_home_contact_number = profile_edit_home_contact_number.getText().toString();
        updated_home_country_code   = "+91";
        updated_work_contact_number = profile_edit_work_contact_number.getText().toString();
        updated_work_country_code   = "+91";
        updated_personal_email      = profile_edit_personal_email.getText().toString();
        updated_home_address        = profile_edit_home_address.getText().toString();



        String url = Web_API_Config.user_profile_update_api;

        DBHelper dbHelper = new DBHelper(getApplicationContext());


        JSONObject profileObject = new JSONObject();
        try {
            profileObject.put("id", dbHelper.getUserID());
            profileObject.put("user_id", dbHelper.getUserID());
            profileObject.put("emp_id", dbHelper.getEmpID());
            profileObject.put("first_name", updated_first_name);
            profileObject.put("last_name", updated_last_name);
            profileObject.put("home_contact_number", updated_home_contact_number);
            profileObject.put("home_country_code", updated_home_country_code);
            profileObject.put("work_contact_number", updated_work_contact_number);
            profileObject.put("work_country_code", updated_work_country_code);
            profileObject.put("personal_email", updated_personal_email);
            profileObject.put("resedential_add", updated_home_address);
            profileObject.put("offc_add", null);
            profileObject.put("official_email", null);
            profileObject.put("profile_back_image", null);
            profileObject.put("profile_image", null);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Create JSONObjectRequest for Volley
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.PUT, url, profileObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        profileUpdateDialg.dismiss();

                       Log.e("Update Profile Data - ", response.toString());


                        profileUpdatedSuccessfully = new SweetAlertDialog(Profile.this, SweetAlertDialog.SUCCESS_TYPE);
                        profileUpdatedSuccessfully.setTitleText("Profile Updated Successfully");
                        profileUpdatedSuccessfully.setContentText("Check Profile Now");
                        profileUpdatedSuccessfully.show();

                        profileUpdatedSuccessfully.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Intent dashboardIntent = new Intent(getApplicationContext(), Profile.class);
                                startActivity(dashboardIntent);
                                finish();
                            }
                        });
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);

    }

    //Checking Whether the field is empty or not
    public boolean checkEmptyField(String data){
        if(data.equals("")  || data.equals("NA") || data == null)
        {
            return false;
        }
        return true;
    }

    public void openProfilePicDialog()
    {

        MaterialDialog.Builder builder = new MaterialDialog.Builder(Profile.this)
                .title("Profile Picture")
                .titleColor(Color.BLACK)
                .customView(R.layout.profile_pic_dialog, true)
                .negativeText("Cancel")
                .positiveColorRes(R.color.black_color)
                .negativeColorRes(R.color.gray_btn_bg_color)
                .canceledOnTouchOutside(false)
                .autoDismiss(false);

        final MaterialDialog materialDialog = builder.build();
        materialDialog.show();

        View view1 = materialDialog.getCustomView();

        ImageView imageView      = (ImageView) view1.findViewById(R.id.profilePicImageView);
        Button    changeImageBtn = (Button) view1.findViewById(R.id.profilePicChangeBtn);

        if(s_profile_image.equals("NA")){
            imageView.setImageResource(R.drawable.student);
        }



        View negative  = materialDialog.getActionButton(DialogAction.NEGATIVE);
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });


        changeImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
                showFileChooser();
            }
        });
    }


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                openUpdatedProfilePivDialog();
                updatedImageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void openUpdatedProfilePivDialog(){
        MaterialDialog.Builder builder = new MaterialDialog.Builder(Profile.this)
                .title("New Picture")
                .titleColor(Color.BLACK)
                .customView(R.layout.profile_pic_update_dialog, true)
                .negativeText("Cancel")
                .positiveColorRes(R.color.black_color)
                .negativeColorRes(R.color.gray_btn_bg_color)
                .canceledOnTouchOutside(false)
                .autoDismiss(false);

        final MaterialDialog materialDialog = builder.build();
        materialDialog.show();

        View view1 = materialDialog.getCustomView();

        updatedImageView         = (ImageView) view1.findViewById(R.id.updatedProfilePicImageView);
        Button    changeImageBtn = (Button) view1.findViewById(R.id.profilePicUpdateBtn);

        changeImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        View negative  = materialDialog.getActionButton(DialogAction.NEGATIVE);
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });
    }


    public void imageUpdationProcesss()
    {

        String url = Web_API_Config.user_profile_pic_update_api;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] array = byteArrayOutputStream.toByteArray();


        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        UUID uniqueKey = UUID.randomUUID();


        String encoded_string = Base64.encodeToString(array, 0);

        String image_name     = android_id + String.valueOf(uniqueKey);



    }

}
