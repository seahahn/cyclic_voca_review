package com.example.vocacyclicreview;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vocacyclicreview.vocagroup.VocagroupArea;
import com.example.vocacyclicreview.vocagroup.VocagroupAreaAdapter;

import java.util.ArrayList;
import java.util.List;

public class VocaLearningCycleAreaAdapter extends RecyclerView.Adapter<VocaLearningCycleAreaAdapter.VocaLearningCycleAreaViewHolder> {

    Context context;
    private ArrayList<VocaLearningCycleArea> mData;

    public VocaLearningCycleAreaAdapter(Context context, ArrayList<VocaLearningCycleArea> mData) {
        this.context = context;
        this.mData = mData;
    }

    public class VocaLearningCycleAreaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView TextView_vocaLearningCycle_vocaLearningCycleArea;
        EditText EditText_vocaLearningCycle_vocaLearningCycleAreaInput;
        ImageButton ImageButton_vocaLearningCycle_areaDelete;

        MyCustomEditTextListener myCustomEditTextListener;

        public VocaLearningCycleAreaViewHolder(@NonNull View itemView, MyCustomEditTextListener myCustomEditTextListener) {
            super(itemView);
            TextView_vocaLearningCycle_vocaLearningCycleArea = itemView.findViewById(R.id.TextView_vocaLearningCycle_vocaLearningCycleArea);
            EditText_vocaLearningCycle_vocaLearningCycleAreaInput = itemView.findViewById(R.id.EditText_vocaLearningCycle_vocaLearningCycleAreaInput);
            ImageButton_vocaLearningCycle_areaDelete = itemView.findViewById(R.id.ImageButton_vocaLearningCycle_areaDelete);

            this.myCustomEditTextListener = myCustomEditTextListener;
            this.EditText_vocaLearningCycle_vocaLearningCycleAreaInput.addTextChangedListener(myCustomEditTextListener);
        }

        public void onBind(VocaLearningCycleArea vocaLearningCycleArea){
            TextView_vocaLearningCycle_vocaLearningCycleArea.setText(vocaLearningCycleArea.getVocaLearningCycleAreaNumber()+" "+(getAdapterPosition()+1));
            ImageButton_vocaLearningCycle_areaDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v == ImageButton_vocaLearningCycle_areaDelete){
                mData.remove(getAdapterPosition());
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public VocaLearningCycleAreaAdapter.VocaLearningCycleAreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vocalearningcycle_area, parent, false);
        VocaLearningCycleAreaViewHolder vocaLearningCycleAreaViewHolder = new VocaLearningCycleAreaViewHolder(view, new MyCustomEditTextListener());

        return vocaLearningCycleAreaViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VocaLearningCycleAreaAdapter.VocaLearningCycleAreaViewHolder holder, int position) {
        holder.onBind(mData.get(position));

        holder.myCustomEditTextListener.updatePosition(holder.getAdapterPosition());
        holder.EditText_vocaLearningCycle_vocaLearningCycleAreaInput.setText(mData.get(holder.getAdapterPosition()).getEditText_vocaLearningCycle_vocaLearningCycleAreaInput());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public ArrayList<VocaLearningCycleArea> getData(){
        return mData;
    }


    public void addItem(VocaLearningCycleArea vocaLearningCycleArea){
        mData.add(vocaLearningCycleArea);
        notifyDataSetChanged();
    }

    public void setItem(int position, ArrayList<VocaLearningCycleArea> mData, VocaLearningCycleArea vocaLearningCycleArea){
        mData.get(position).setEditText_vocaLearningCycle_vocaLearningCycleAreaInput(vocaLearningCycleArea.getEditText_vocaLearningCycle_vocaLearningCycleAreaInput());
        notifyDataSetChanged();
    }

    public boolean isSAreaEmpty(){
        boolean isEmpty;
        boolean emptyCheck = false;

        for(int i = 0; i < mData.size(); i++){
            isEmpty = mData.get(i).getEditText_vocaLearningCycle_vocaLearningCycleAreaInput().isEmpty();
            if(isEmpty){
                emptyCheck = true;
            }
        }
        return emptyCheck;
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
            mData.get(position).setEditText_vocaLearningCycle_vocaLearningCycleAreaInput(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }
}
