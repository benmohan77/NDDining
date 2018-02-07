package com.nd.blg.nddining.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nd.blg.nddining.R;
import com.nd.blg.nddining.adapters.ItemAdapter;
import com.nd.blg.nddining.objects.FavoritesList;
import com.nd.blg.nddining.objects.Item;


public class FavoritesFragment extends ListFragment {

    private FavoritesList favoritesList;
    private ItemAdapter adapter;

    private OnFavoritesFragmentInteractionListener mListener;

    public FavoritesFragment() {
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
    public static FavoritesFragment newInstance() {
        FavoritesFragment fragment = new FavoritesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String obj = getArguments().getString("favorites", "");
        System.out.println(obj);

        adapter = new ItemAdapter(getContext(), R.id.list, favoritesList.getFavorites(), favoritesList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Favorite Items");
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id){
        super.onListItemClick(l, v, pos, id);
        if(mListener != null){
            mListener.addFavoriteFromFavoritesList(favoritesList.getFavorites().get(pos));
        }
        adapter.notifyDataSetChanged();
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
        setListAdapter(adapter);
    }


    public interface OnFavoritesFragmentInteractionListener {
        void addFavoriteFromFavoritesList(Item item);
    }
}
