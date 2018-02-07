package com.nd.blg.nddining.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nd.blg.nddining.R;
import com.nd.blg.nddining.adapters.WeekAdapter;
import com.nd.blg.nddining.objects.Day;
import com.nd.blg.nddining.objects.FavoritesList;
import com.nd.blg.nddining.objects.Item;

import java.util.ArrayList;
import java.util.List;


public class WeekFragment extends ListFragment {

    private FavoritesList favoritesList;
    private List<Day> days;
    private WeekAdapter adapter;
    private DayFragment fragment;

    private OnFavoritesFragmentInteractionListener mListener;

    public WeekFragment() {
        // Required empty public constructor
    }

    public void setFavoritesList(FavoritesList favoritesList){
        if(this.favoritesList == null){
            this.favoritesList = favoritesList;
        }
        if(adapter != null){
            adapter.notifyDataSetChanged();
        }
    }


    // TODO: Rename and change types and number of parameters
    public static WeekFragment newInstance(List<Day> days) {
        WeekFragment fragment = new WeekFragment();
        Bundle args = new Bundle();
        Gson gson = new Gson();
        args.putString("days", gson.toJson(days));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String obj = getArguments().getString("days", "");
        Gson gson = new Gson();
        days = gson.fromJson(obj, new TypeToken< ArrayList<Day>>(){}.getType());
        adapter = new WeekAdapter(getContext(), R.id.list, days);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Upcoming Days");
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id){
        super.onListItemClick(l, v, pos, id);
        fragment = DayFragment.newInstance(days.get(pos));
        fragment.setFavorites(favoritesList);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("week").commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFavoritesFragmentInteractionListener) {
            mListener = (OnFavoritesFragmentInteractionListener) context;
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
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        if(days != null){
            setListAdapter(adapter);
        }
    }

    public interface OnFavoritesFragmentInteractionListener {
        void onFragmentInteraction(Item item);
    }
}
