package com.nd.blg.nddining.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.nd.blg.nddining.objects.Day;
import com.nd.blg.nddining.objects.FavoritesList;
import com.nd.blg.nddining.fragments.LocationListFragment;

/**
 * Created by Ben on 7/23/2017.
 */

public class LocationPagerAdapter extends FragmentPagerAdapter {


    SparseArray<Fragment> registeredFragments = new SparseArray<>();
    private Day day;
    private  FavoritesList favoritesList;

    public LocationPagerAdapter(FragmentManager fm, Day day, FavoritesList favoritesList) {
        super(fm);
        this.favoritesList = favoritesList;
        this.day = day;
    }

    @Override
    public Fragment getItem(int position) {
        LocationListFragment fragment = LocationListFragment.newInstance(day.getLocations().get(position));
        fragment.setFavoritesList(favoritesList);
        return fragment;
    }

    public void update(){
        for(int i = 0; i < registeredFragments.size(); i++){
            ((LocationListFragment)registeredFragments.valueAt(i)).update();
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return  day.getLocations().size();
    }

    @Override
    public CharSequence getPageTitle(int position){
        return day.getLocations().get(position).getName();
    }
}
