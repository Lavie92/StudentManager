package com.example.btcn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.btcn.models.Faculty;

import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<Faculty> {

    public CustomArrayAdapter(Context context, int resource, List<Faculty> faculties) {
        super(context, resource, faculties);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        Faculty faculty = getItem(position);
        if (faculty != null) {
            TextView textView = view.findViewById(android.R.id.text1);
            textView.setText(faculty.getName());
        }

        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        Faculty faculty = getItem(position);
        if (faculty != null) {
            TextView textView = view.findViewById(android.R.id.text1);
            textView.setText(faculty.getName());
        }

        return view;
    }
}
