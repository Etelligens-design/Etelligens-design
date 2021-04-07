package com.pch.ete.ui.service_request.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pl.sly.dynamicautocomplete.DynamicAutocompleteFilter;
import pl.sly.dynamicautocomplete.OnDynamicAutocompleteFilterListener;

public class SimpleDictionaryAdapter extends ArrayAdapter<String> implements
        OnDynamicAutocompleteFilterListener<String> {
    private List<String> mListItems;
    private SimpleDictionaryProvider mSimpleDictionaryProvider;
    private DynamicAutocompleteFilter<String> mDictionaryAutocompleteFilter;
    private LayoutInflater mLayoutInflater;
    private int mLayoutId;

    public SimpleDictionaryAdapter(Context context, int textViewResourceId, String[] vendorNames) {
        super(context, textViewResourceId);
        mSimpleDictionaryProvider = new SimpleDictionaryProvider(vendorNames);
        mDictionaryAutocompleteFilter = new DynamicAutocompleteFilter<>(mSimpleDictionaryProvider, this);
        mDictionaryAutocompleteFilter.useCache(true);
        mListItems = new ArrayList<>();
        mLayoutId = textViewResourceId;
        mLayoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void onDynamicAutocompleteFilterResult(Collection<String> result) {
        mListItems.clear();
        mListItems.addAll(result);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return mDictionaryAutocompleteFilter;
    }

    @Override
    public int getCount() {
        return mListItems.size();
    }

    @Override
    public void clear() {
        mListItems.clear();
    }

    @Override
    public String getItem(int position) {
        return mListItems.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(mLayoutId, null);

            viewHolder = new ViewHolder();
            viewHolder.word = (TextView) convertView
                    .findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.word.setText(getItem(position));

        return convertView;
    }

    private static class ViewHolder {
        private TextView word;
    }
}
