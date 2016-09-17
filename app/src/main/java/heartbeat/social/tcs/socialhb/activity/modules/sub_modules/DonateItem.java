package heartbeat.social.tcs.socialhb.activity.modules.sub_modules;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import heartbeat.social.tcs.socialhb.R;
import heartbeat.social.tcs.socialhb.adapter.ViewPagerAdapter;
import heartbeat.social.tcs.socialhb.fragment.DonateBooksFragment;
import heartbeat.social.tcs.socialhb.fragment.DonateClothFragment;
import heartbeat.social.tcs.socialhb.fragment.DonateEWasteFragment;
import heartbeat.social.tcs.socialhb.fragment.DonateFoodFragment;
import heartbeat.social.tcs.socialhb.fragment.DonateFragment;
import heartbeat.social.tcs.socialhb.fragment.ReuseFragment;

public class DonateItem extends AppCompatActivity {


    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_item);

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
        getSupportActionBar().setTitle("Donate Item");

        tabLayout = (TabLayout)findViewById(R.id.tabLayout2);
        viewPager = (ViewPager)findViewById(R.id.viewPager2);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new DonateBooksFragment(), "Books");
        viewPagerAdapter.addFragments(new DonateClothFragment(), "Cloths");
        viewPagerAdapter.addFragments(new DonateFoodFragment(), "Food");
        viewPagerAdapter.addFragments(new DonateEWasteFragment(), "EWaste");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}
