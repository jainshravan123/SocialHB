package heartbeat.social.tcs.socialhb.adapter;

/**
 * Created by admin on 27/07/16.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments   = new ArrayList<>();
    ArrayList<String>   tabTitles   = new ArrayList<>();

    public void addFragments(Fragment fragments,String titles)
    {
        this.fragments.add(fragments);
        this.tabTitles.add(titles);

    }


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }
}

