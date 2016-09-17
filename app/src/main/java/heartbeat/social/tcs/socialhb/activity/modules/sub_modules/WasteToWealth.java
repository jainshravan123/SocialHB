package heartbeat.social.tcs.socialhb.activity.modules.sub_modules;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import heartbeat.social.tcs.socialhb.R;
import heartbeat.social.tcs.socialhb.adapter.ViewPagerAdapter;
import heartbeat.social.tcs.socialhb.fragment.DonateFragment;
import heartbeat.social.tcs.socialhb.fragment.ReuseFragment;

public class WasteToWealth extends AppCompatActivity {


    Toolbar toolbar;
    TabLayout tabLayout2;
    ViewPager viewPager2;
    ViewPagerAdapter viewPagerAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waste_to_wealth);

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
        getSupportActionBar().setTitle("WasteToWealth");


        tabLayout2 = (TabLayout)findViewById(R.id.tabLayout2);
        viewPager2 = (ViewPager)findViewById(R.id.viewPager2);
        viewPagerAdapter2 = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter2.addFragments(new DonateFragment(), "Donate");
        viewPagerAdapter2.addFragments(new ReuseFragment(), "Reuse");
        viewPager2.setAdapter(viewPagerAdapter2);
        tabLayout2.setupWithViewPager(viewPager2);


    }
}
