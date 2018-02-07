package com.nd.blg.nddining.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.nd.blg.nddining.R;
import com.nd.blg.nddining.adapters.ItemAdapter;
import com.nd.blg.nddining.objects.AllList;
import com.nd.blg.nddining.objects.FavoritesList;
import com.nd.blg.nddining.objects.Item;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AllListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AllListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllListFragment extends ListFragment {

    private AllList allList;
    private  FavoritesList favoritesList = new FavoritesList();

    private OnFragmentInteractionListener mListener;
    private ItemAdapter adapter;

    public AllListFragment() {
        // Required empty public constructora
    }


    public static AllListFragment newInstance(AllList allList) {
        AllListFragment fragment = new AllListFragment();
        Bundle args = new Bundle();
        Gson gson = new Gson();
        args.putString("list", gson.toJson(allList));
        fragment.setArguments(args);
        return fragment;
    }

    public void setFavoritesList(FavoritesList favoritesList){
        if(favoritesList != null){
            this.favoritesList = favoritesList;
        }
        if(adapter != null){
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(favoritesList == null){
            favoritesList = new FavoritesList();
        }
        Gson gson = new Gson();
        String obj = getArguments().getString("list", "");
        System.out.println(obj);
        allList = gson.fromJson(obj, AllList.class);
        adapter = new ItemAdapter(getContext(), R.id.list, allList.getAllItems(), favoritesList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("All Items");
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id){
        super.onListItemClick(l, v, pos, id);
            if(mListener != null){
                mListener.addFavoriteFromAllList(allList.getAllItems().get(pos));
            }
            adapter.notifyDataSetChanged();
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
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        setListAdapter(adapter);
    }


    public interface OnFragmentInteractionListener {
        void addFavoriteFromAllList(Item item);
    }
}
