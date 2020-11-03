package com.seahahn.cyclicvocareview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

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
                    .inflate(R.layout.item_spinner, parent, false);
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
                    .inflate(R.layout.item_spinner_dropdown, parent, false);
        }
        String text = mData.get(position).getVocaLearningCycleName();
        ((TextView) convertView.findViewById(R.id.spinner_item)).setText(text);

        return convertView;
    }

    public void setData(ArrayList<VocaLearningCycle> data){
        mData = data;
    }
}
