package heartbeat.social.tcs.socialhb.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import heartbeat.social.tcs.socialhb.R;

public class Trial extends AppCompatActivity
{
    Toolbar toolbar;
    RadioGroup first_radio_group;
    AppCompatButton getSelectedRadioTextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial);


        first_radio_group         = (RadioGroup) findViewById(R.id.first_radio_group);
        getSelectedRadioTextBtn   = (AppCompatButton) findViewById(R.id.getSelectedRadioTextBtn);
        toolbar                   = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle("Trial");


        for (int i = 0; i< 9; i++)
        {
            AppCompatRadioButton appCompatRadioButton = new AppCompatRadioButton(this);
            appCompatRadioButton.setId(i);
            appCompatRadioButton.setText("Radio Button Text");
            appCompatRadioButton.setChecked(false);
            //appCompatRadioButton.setBackgroundResource(R.drawable.item_selector); // This is a custom button drawable, defined in XML
            first_radio_group.addView(appCompatRadioButton);
        }

        getSelectedRadioTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(), "radio Btn ID : " + String.valueOf(first_radio_group.getCheckedRadioButtonId()), Toast.LENGTH_LONG).show();
            }
        });

    }
}
