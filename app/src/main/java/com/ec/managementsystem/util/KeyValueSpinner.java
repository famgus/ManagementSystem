package com.ec.managementsystem.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.ec.managementsystem.R;

import java.util.ArrayList;



@SuppressLint("ViewHolder")
public class KeyValueSpinner implements SpinnerAdapter {
    Context context;
    ArrayList<KeyValue> items;

    public KeyValueSpinner(Context context, ArrayList<KeyValue> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    //Note:-Create this two method getIDFromIndex and getIndexByID
    public int getIdFromIndex(int Index) {
        return items.get(Index).getId();
    }

    public int getIndexById(int Id) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == Id) {
                return i;
            }
        }
        return -1;
    }

    public int getIndexByValue(String value) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getValue().equals(value)) {
                return i;
            }
        }
        return -1;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView textview = (TextView) inflater.inflate(R.layout.custom_spinner_layout, null);
        textview.setText(items.get(position).getValue());
        return textview;
    }


    @Override
    public int getViewTypeCount() {
        //return android.R.layout.simple_spinner_dropdown_item;
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @SuppressLint("InflateParams")
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView textview = (TextView) inflater.inflate(R.layout.custom_spinner_layout, null);
        textview.setText(items.get(position).getValue());
        return textview;
    }

}
