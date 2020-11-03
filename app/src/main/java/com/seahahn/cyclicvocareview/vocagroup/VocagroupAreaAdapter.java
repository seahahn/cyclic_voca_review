package com.seahahn.cyclicvocareview.vocagroup;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.seahahn.cyclicvocareview.R;

import java.util.ArrayList;

public class VocagroupAreaAdapter extends RecyclerView.Adapter<VocagroupAreaAdapter.VocagroupAreaViewHolder> {

    Context context;
    private ArrayList<VocagroupArea> mData;

    public VocagroupAreaAdapter(Context context, ArrayList<VocagroupArea> mData) {
        this.context = context;
        this.mData = mData;
    }
    
    public class VocagroupAreaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView TextView_vocagroupAdd_vocagroupArea;
        EditText EditText_vocagroupAdd_vocagroupAreaInput;
        ImageButton ImageButton_vocagroupAdd_areaDelete;
        Switch Switch_vocagroupAdd_areaSwitch;

        MyCustomEditTextListener myCustomEditTextListener;
        MyOnCheckedChanged myOnCheckedChanged;

        public VocagroupAreaViewHolder(@NonNull View itemView, MyCustomEditTextListener myCustomEditTextListener, MyOnCheckedChanged myOnCheckedChanged) {
            super(itemView);
            TextView_vocagroupAdd_vocagroupArea = itemView.findViewById(R.id.TextView_vocagroupAdd_vocagroupArea);
            EditText_vocagroupAdd_vocagroupAreaInput = itemView.findViewById(R.id.EditText_vocagroupAdd_vocagroupAreaInput);
            ImageButton_vocagroupAdd_areaDelete = itemView.findViewById(R.id.ImageButton_vocagroupAdd_areaDelete);
            Switch_vocagroupAdd_areaSwitch = itemView.findViewById(R.id.Switch_vocagroupAdd_areaSwitch);

            this.myCustomEditTextListener = myCustomEditTextListener;
            this.EditText_vocagroupAdd_vocagroupAreaInput.addTextChangedListener(myCustomEditTextListener);

            this.myOnCheckedChanged = myOnCheckedChanged;
            this.Switch_vocagroupAdd_areaSwitch.setOnCheckedChangeListener(myOnCheckedChanged);
        }

        public void onBind(VocagroupArea vocagroupArea){
            TextView_vocagroupAdd_vocagroupArea.setText(vocagroupArea.getVocagroupAreaNumber()+" "+(getAdapterPosition()+1));
            ImageButton_vocagroupAdd_areaDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v == ImageButton_vocagroupAdd_areaDelete){
                mData.remove(getAdapterPosition());
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public VocagroupAreaAdapter.VocagroupAreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vocagroup_area, parent, false);
        VocagroupAreaViewHolder vocagroupAreaViewHolder = new VocagroupAreaViewHolder(view, new MyCustomEditTextListener(), new MyOnCheckedChanged());

        return vocagroupAreaViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VocagroupAreaAdapter.VocagroupAreaViewHolder holder, final int position) {
        holder.onBind(mData.get(position));

        holder.myCustomEditTextListener.updatePosition(holder.getAdapterPosition());
        holder.EditText_vocagroupAdd_vocagroupAreaInput.setText(mData.get(holder.getAdapterPosition()).getEditText_vocagroupAdd_vocagroupAreaInput());

        holder.myOnCheckedChanged.updatePosition(holder.getAdapterPosition());
        holder.Switch_vocagroupAdd_areaSwitch.setChecked(mData.get(holder.getAdapterPosition()).isSwitch_vocagroupAdd_areaSwitch());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public ArrayList<VocagroupArea> getData(){
        return mData;
    }

    public boolean isVocagroupAreaEmpty(){
        boolean isEmpty;
        boolean emptyCheck = false;

        for(int i = 0; i < mData.size(); i++){
            isEmpty = mData.get(i).getEditText_vocagroupAdd_vocagroupAreaInput().isEmpty();
            if(isEmpty){
                emptyCheck = true;
            }
        }
        return emptyCheck;
    }


    public void addItem(VocagroupArea vocagroupArea){
        mData.add(vocagroupArea);
        notifyDataSetChanged();
    }

    public void setItem(int position, ArrayList<VocagroupArea> mData, VocagroupArea vocagroupArea){
        mData.get(position).setEditText_vocagroupAdd_vocagroupAreaInput(vocagroupArea.getEditText_vocagroupAdd_vocagroupAreaInput());
        notifyDataSetChanged();
    }

    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            mData.get(position).setEditText_vocagroupAdd_vocagroupAreaInput(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }

    private class MyOnCheckedChanged implements CompoundButton.OnCheckedChangeListener{

        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                mData.get(position).setSwitch_vocagroupAdd_areaSwitch(isChecked);
            } else {
                mData.get(position).setSwitch_vocagroupAdd_areaSwitch(isChecked);
            }
        }
    }
}


