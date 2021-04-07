package com.pch.ete.ui.service_request.adapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pch.ete.ui.service_request.model.DictionaryModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pl.sly.dynamicautocomplete.DynamicAutocompleteProvider;

public class SimpleDictionaryProvider implements DynamicAutocompleteProvider<String> {

    String[] mVendorNamesArray;

    public SimpleDictionaryProvider(String[] vendorNames) {
        this.mVendorNamesArray = vendorNames;
    }

    @Override
    public Collection<String> provideDynamicAutocompleteItems(String constraint) {
        List<String> result = new ArrayList<>();
        for(String name: mVendorNamesArray){
            if(name.contains(constraint)){
                result.add(name);
            }
        }
        return result;
    }
}

