package com.example.vocacyclicreview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vocacyclicreview.vocagroup.Vocagroup;
import com.example.vocacyclicreview.vocagroup.VocagroupModify;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class VocaLearningCycleAdapter extends BaseAdapter{

    Context context;
    private ArrayList<VocaLearningCycle> mData;

    public VocaLearningCycleAdapter(Context context, ArrayList<VocaLearningCycle> mData) {
        this.context = context;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
//        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_vocalearningcycle, parent, false);
        }
        if(mData != null){
            String text = mData.get(position).getVocaLearningCycleName();
            ((TextView) convertView.findViewById(R.id.spinner_item)).setText(text);
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_vocalearningcycle_dropdown, parent, false);
        }
        String text = mData.get(position).getVocaLearningCycleName();
        ((TextView) convertView.findViewById(R.id.spinner_item)).setText(text);

        return convertView;
    }

    public void setData(ArrayList<VocaLearningCycle> data){
        mData = data;
    }
}
