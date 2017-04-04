package com.dav.cityweather.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;

/**
 * Created by dav on 03.04.17.
 */


public class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
    private ArrayList mResultList;
    private AutoCompleteRequest mAutoCompleteRequest;

    public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        mAutoCompleteRequest= new AutoCompleteRequest(context);
    }

    @Override
    public int getCount() {
        return mResultList.size();
    }

    @Override
    public Object getItem(int index) {
        return mResultList.get(index);
    }

    @Override
    @NonNull
    public Filter getFilter() {
         return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    mResultList = mAutoCompleteRequest.autocomplete(constraint.toString());
                    filterResults.values = mResultList;
                    filterResults.count = mResultList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}