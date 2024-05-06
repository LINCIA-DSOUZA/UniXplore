package com.example.unixplore;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MySpinnerAdapter extends BaseAdapter {

    private final ArrayList<String> data; // Now contains individual options

    public MySpinnerAdapter(ArrayList<String> data) {
        ArrayList<String> options = new ArrayList<>();
        for (String line : data) {
            String[] values = line.split(",");
            for (String value : values) {
                options.add(value.trim());
            }
        }
        this.data = options;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) convertView;
        if (textView == null) {
            textView = new TextView(parent.getContext());
        }
        textView.setText(data.get(position));
        return textView;
    }
    public int getPosition(String pref) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).equals(pref)) {
                return i;
            }
        }
        return -1;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


}
