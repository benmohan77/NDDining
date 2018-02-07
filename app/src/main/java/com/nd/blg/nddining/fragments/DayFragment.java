package com.nd.blg.nddining.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.nd.blg.nddining.R;
import com.nd.blg.nddining.adapters.LocationPagerAdapter;
import com.nd.blg.nddining.objects.Day;
import com.nd.blg.nddining.objects.FavoritesList;
import com.nd.blg.nddining.objects.Item;

public class DayFragment extends Fragment implements LocationListFragment.OnLocationFragmentInteractionListener {


    private OnFragmentInteractionListener mListener;
    private  FavoritesList favoritesList;
    private Day day;
    private ViewPager mViewPager;
    private TabLayout tabs;
    private LocationPagerAdapter adapter;


    public DayFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DayFragment newInstance(Day day) {
        DayFragment fragment = new DayFragment();
        Bundle args = new Bundle();
        Gson gson = new Gson();
        args.putString("day", gson.toJson(day));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String obj = getArguments().getString("day", "");
        if(favoritesList == null){
            favoritesList = new FavoritesList();
        }
        Gson gson = new Gson();
        day = gson.fromJson(obj, Day.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        getActivity().setTitle("Bison Dining");
        if(day != null){
            getActivity().setTitle(day.getFriendlyName());
            mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
            tabs = (TabLayout) view.findViewById(R.id.tab_layout);
            adapter = new LocationPagerAdapter(getChildFragmentManager(), day, this.favoritesList);
            mViewPager.setAdapter(adapter);
            tabs.setupWithViewPager(mViewPager);
        }


        return view;
    }


    public void setFavorites(FavoritesList favoritesList){
        if(favoritesList != null){
            this.favoritesList = favoritesList;
        }
        if(adapter != null){
            adapter.notifyDataSetChanged();
        }
        if(mViewPager != null){
            mViewPager.getAdapter().notifyDataSetChanged();
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void addFavoriteFromLocation(Item item) {
        adapter.update();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
