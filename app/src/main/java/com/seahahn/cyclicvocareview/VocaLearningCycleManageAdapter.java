package com.seahahn.cyclicvocareview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.seahahn.cyclicvocareview.MainActivity.userID;

public class VocaLearningCycleManageAdapter extends RecyclerView.Adapter<VocaLearningCycleManageAdapter.VocaLearningCycleManageViewHolder> implements ItemTouchHelperListener{

    private static final int REQUEST_VOCALEARNINGCYCLE_MODIFY = 4;
    Context context;
    private ArrayList<VocaLearningCycle> mData;

    public VocaLearningCycleManageAdapter(Context context, ArrayList<VocaLearningCycle> mData) {
        this.context = context;
        this.mData = mData;
    }

    public class VocaLearningCycleManageViewHolder extends RecyclerView.ViewHolder {
        TextView vocaLearningCycle_name;

        public VocaLearningCycleManageViewHolder(@NonNull View itemView) {
            super(itemView);
            vocaLearningCycle_name = itemView.findViewById(R.id.vocaLearningCycle_name);
        }

        public void onBind(VocaLearningCycle vocaLearningCycle){
//            Log.d("VLCManageAdapter onBind 확인", vocaLearningCycle.getVocaLearningCycleName());
            vocaLearningCycle_name.setText(vocaLearningCycle.getVocaLearningCycleName());
        }
    }

    @Override
    public VocaLearningCycleManageAdapter.VocaLearningCycleManageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vocalearningcycle_manage, parent, false);
        return new VocaLearningCycleManageAdapter.VocaLearningCycleManageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VocaLearningCycleManageAdapter.VocaLearningCycleManageViewHolder holder, int position) {
        holder.onBind(mData.get(position));
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public ArrayList<VocaLearningCycle> getData(){
        return mData;
    }

    public void setData(ArrayList<VocaLearningCycle> data){
        mData = data;
    }

    public void addItem(VocaLearningCycle vocaLearningCycle){
        mData.add(vocaLearningCycle);
        notifyDataSetChanged();
    }

    public void removeAll(ArrayList<VocaLearningCycle> data){
        mData.removeAll(data);
    }

    @Override
    public boolean onItemMove(int from_position, int to_position) {
        //이동할 객체 저장
        VocaLearningCycle vocaLearningCycle = mData.get(from_position);
        //이동할 객체 삭제
        mData.remove(from_position);
        //이동하고 싶은 position에 추가
        mData.add(to_position, vocaLearningCycle);
        //Adapter에 데이터 이동알림
        notifyItemMoved(from_position, to_position);

//        SharedPreferences sharedPreferences = context.getSharedPreferences("VocaLearningCycle", MODE_PRIVATE);
        SharedPreferences sharedPreferences = context.getSharedPreferences(userID, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String vocaLearningCycleListJson = gson.toJson(getData());
        editor.putString("VocaLearningCycleList", vocaLearningCycleListJson); // 저장할 값 입력하기
        editor.commit();

        return true;
    }

    @Override
    public void onItemSwipe(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onLeftClick(int position, RecyclerView.ViewHolder viewHolder) {
        String vocagroupVLC = mData.get(position).getVocaLearningCycleName() + " vocaLearningCycle";
        Intent intentModify = new Intent(context, VocaLearningCycleModify.class);
        intentModify.putExtra("vocagroupVLC", vocagroupVLC);
        intentModify.putExtra("단어학습주기 포지션", position);

//        SharedPreferences sharedPreferences = context.getSharedPreferences("VocaLearningCycle", MODE_PRIVATE);
        SharedPreferences sharedPreferences = context.getSharedPreferences(userID, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String vocaLearningCycleJsonSave = gson.toJson(mData.get(position));
        editor.putString(vocagroupVLC, vocaLearningCycleJsonSave); // 저장할 값 입력하기
        editor.commit();

        ((Activity) context).startActivityForResult(intentModify, REQUEST_VOCALEARNINGCYCLE_MODIFY);
    }

    @Override
    public void onRightClick(int position, RecyclerView.ViewHolder viewHolder) {
        mData.remove(position);
        notifyItemRemoved(position);
    }
}
