package com.luminousmossboss.luminous.adapter;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luminousmossboss.luminous.R;
import com.luminousmossboss.luminous.model.DataItem;
import com.luminousmossboss.luminous.model.FilterItem;
import com.luminousmossboss.luminous.model.ListItem;

import java.util.ArrayList;

/**
 *
 * Created by Brian on 2/2/2015.
 */

public class FilterListAdapter extends FragListAdapter {

    //private Context context;
    //private ArrayList<ListItem> listItems;

    public FilterListAdapter(Context context, ArrayList<ListItem> listItems) {
        //this.context = context;
        //this.listItems = listItems;
        super(context, listItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_filter, null);
        }
        convertView = basicView(position, convertView);
        return convertView;
    }

}