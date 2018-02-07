package com.nd.blg.nddining.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nd.blg.nddining.R;
import com.nd.blg.nddining.adapters.ObjectListAdapter;
import com.nd.blg.nddining.objects.FavoritesList;
import com.nd.blg.nddining.objects.Item;
import com.nd.blg.nddining.objects.Location;

import static android.content.ContentValues.TAG;

public class LocationListFragment extends Fragment {

    private Location location;
    private ObjectListAdapter adapter;
    private FavoritesList favoritesList;
    private OnLocationFragmentInteractionListener mListener;
    private OnLocationFragmentInteractionListener fListener;

    public LocationListFragment() {
        // Required empty public constructor
    }


    public static LocationListFragment newInstance(Location location) {
        LocationListFragment fragment = new LocationListFragment();
        Bundle args = new Bundle();
        Gson gson = new Gson();
        args.putString("location", gson.toJson(location));
        fragment.setArguments(args);
        return fragment;
    }

    public void setFavoritesList(FavoritesList favoritesList){
        this.favoritesList = favoritesList;
        if(adapter != null){
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String obj = getArguments().getString("location", "");
        if(favoritesList == null){
            favoritesList = new FavoritesList();
        }
        Gson gson = new Gson();
        location = gson.fromJson(obj, Location.class);
        adapter = new ObjectListAdapter(getContext(), location.getAllList(), this.favoritesList);
        onAttachToParentFragment(getParentFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(!this.location.getAllItems().isEmpty()){
            // Inflate the layout for this fragment
            View rootView = inflater.inflate(R.layout.location_list_fragment, container, false);

            ListView lst = (ListView) rootView.findViewById(R.id.list);
            lst.setAdapter(adapter);
            lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(location.getAllList().get(position) instanceof Item){
                        if(mListener != null){
                            mListener.addFavoriteFromLocation((Item) location.getAllList().get(position));
                            fListener.addFavoriteFromLocation((Item) location.getAllList().get(position));
                        }
                    }
                    update();
                }
            });
            return rootView;
        } else {
            View rootView = inflater.inflate(R.layout.message, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.message_text);
            textView.setText(location.getMessage());
            return rootView;
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    public void update(){
        if(adapter != null){
            adapter.notifyDataSetChanged();
        }
    }
//
//    @Override
//    public void onListItemClick(ListView l, View v, int pos, long id){
//        super.onListItemClick(l, v, pos, id);
//
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLocationFragmentInteractionListener) {
            mListener = (OnLocationFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void onAttachToParentFragment(Fragment fragment){
        try{
            fListener = (OnLocationFragmentInteractionListener)fragment;
        } catch(ClassCastException e){
            throw new ClassCastException(fragment.toString() + "must implement on location fragment interaction listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnLocationFragmentInteractionListener {
        void addFavoriteFromLocation(Item item);
    }
}
