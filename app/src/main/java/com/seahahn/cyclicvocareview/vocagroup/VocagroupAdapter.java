package com.seahahn.cyclicvocareview.vocagroup;
import android.view.*;
import android.widget.Toast;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.seahahn.cyclicvocareview.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;
import static com.seahahn.cyclicvocareview.MainActivity.userID;

public class VocagroupAdapter extends RecyclerView.Adapter<VocagroupAdapter.VocagroupViewHolder> implements ItemTouchHelperListener {

    private static final int REQUEST_VOCAGROUP_MODIFY = 2;
    private static final int REQUEST_VOCA_UPLOAD = 3;
    private static final String TAG = "VocagroupAdapter";

    Context context;
    private ArrayList<Vocagroup> mData;

    private ArrayList<VocaArea> vocaArea = new ArrayList<>();
    private VocaAreaAdapter vocaAreaAdapter;

    public VocagroupAdapter(Context context, ArrayList<Vocagroup> mData) {
        this.context = context;
        this.mData = mData;
    }

    public class VocagroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        Button vocagroup_name;

        public VocagroupViewHolder(@NonNull View itemView) {
            super(itemView);
            vocagroup_name = itemView.findViewById(R.id.vocagroup_name);

        }

        public void onBind(Vocagroup vocagroup){
            vocagroup_name.setText(vocagroup.getVocagroupName());
            vocagroup_name.setOnClickListener(this);
            vocagroup_name.setOnLongClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            if(v == vocagroup_name){

                PopupMenu popup = new PopupMenu(context , vocagroup_name);
                MenuInflater inf = popup.getMenuInflater();
                inf.inflate(R.menu.menu_main_vocagroupclick, popup.getMenu()); // 버튼 눌렀을 때 띄울 팝업 형태 정해주기
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // 메뉴 내 각각의 아이템 클릭 시 어떻게 할지 정하기
                        switch (item.getItemId()){
                            case R.id.menu_main_vocagroupstudy:
                                Date date = new Date();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HH:mm");

                                Intent intent = new Intent(context, VocaShow.class);
//                intent.putExtra("vocagroupName", mData.get(getAdapterPosition()).getVocagroupName());
//                intent.putExtra("단어장 포지션", getAdapterPosition());

                                String vocagroupName = mData.get(getAdapterPosition()).getVocagroupName() + " vocagroupName";
                                SharedPreferences sharedPreferences = context.getSharedPreferences(userID, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                Gson gson = new Gson();
                                String vocagroupJson = gson.toJson(mData.get(getAdapterPosition()));
                                editor.putString("vocagroupName", vocagroupName);
                                editor.putInt("단어장 포지션", getAdapterPosition());
                                editor.putString(vocagroupName, vocagroupJson); // 저장할 값 입력하기
                                editor.commit();

                                context.startActivity(intent);

                                break;
                            case R.id.menu_main_vocagroupupload:

                                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                builder.setTitle("단어 대량 추가하기");
                                builder.setMessage("엑셀 파일을 불러옵니다.");
                                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                        intent.putExtra("단어장 포지션", getAdapterPosition());
                                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                                        intent.setType("application/*");
                                        ((Activity)context).startActivityForResult(intent, REQUEST_VOCA_UPLOAD);

                                        // 엑셀 파일 이용한 대량의 단어 데이터 추가 시작
                                        // 선택한 단어장의 정보 저장
////                String vocagroupName = mData.get(getAdapterPosition()).getVocagroupName();
                                        SharedPreferences sharedPreferences = context.getSharedPreferences(userID, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        Gson gson = new Gson();
//                        String vocagroupJson = gson.toJson(mData.get(getAdapterPosition()));
                                        editor.putInt("단어장 포지션", getAdapterPosition());
                                        editor.commit();

                                    }
                                });
                                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(context, "엑셀 파일 불러오기 취소", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();

                                break;
                        }
                        return false;
                    }
                });
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(v == vocagroup_name){

//                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
//                builder.setTitle("단어 대량 추가하기");
//                builder.setMessage("엑셀 파일을 불러옵니다.");
//                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
////                        intent.putExtra("단어장 포지션", getAdapterPosition());
//                        intent.addCategory(Intent.CATEGORY_OPENABLE);
//                        intent.setType("application/*");
//                        ((Activity)context).startActivityForResult(intent, REQUEST_VOCA_UPLOAD);
//
//                        // 엑셀 파일 이용한 대량의 단어 데이터 추가 시작
//                        // 선택한 단어장의 정보 저장
//////                String vocagroupName = mData.get(getAdapterPosition()).getVocagroupName();
//                        SharedPreferences sharedPreferences = context.getSharedPreferences(userID, MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
////                        Gson gson = new Gson();
////                        String vocagroupJson = gson.toJson(mData.get(getAdapterPosition()));
//                editor.putInt("단어장 포지션", getAdapterPosition());
//                editor.commit();
//
//                    }
//                });
//                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(context, "엑셀 파일 불러오기 취소", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
            }
            return false;
        }
    }

    @Override
    public VocagroupAdapter.VocagroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vocagroup, parent, false);
        return new VocagroupAdapter.VocagroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VocagroupAdapter.VocagroupViewHolder holder, int position) {
        holder.onBind(mData.get(position));
    }

    @Override
    public int getItemCount() {
//        return mData.size();
        return (null != mData ? mData.size() : 0);
    }

    public ArrayList<Vocagroup> getData(){
        return mData;
    }

    public void setData(ArrayList<Vocagroup> data){
        mData = data;
    }

    public void addItem(Vocagroup vocagroup){
        mData.add(vocagroup);
        notifyDataSetChanged();
    }

    public void removeAll(ArrayList<Vocagroup> data){
        mData.removeAll(data);
    }

    @Override
    public boolean onItemMove(int from_position, int to_position) {
        //이동할 객체 저장
         Vocagroup vocagroup = mData.get(from_position);
         //이동할 객체 삭제
        mData.remove(from_position);
         //이동하고 싶은 position에 추가
        mData.add(to_position, vocagroup);
         //Adapter에 데이터 이동알림
         notifyItemMoved(from_position,to_position);

        SharedPreferences sharedPreferences = context.getSharedPreferences(userID, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String vocagroupListJson = gson.toJson(getData());
        editor.putString("VocagroupList", vocagroupListJson); // 저장할 값 입력하기
        editor.commit();

         return true;
    }

    @Override
    public void onItemSwipe(int position) {
//        mData.remove(position);
//        notifyItemRemoved(position);
    }

    //왼쪽 버튼 누르면 단어장 수정 화면으로 전환
    @Override
    public void onLeftClick(int position, RecyclerView.ViewHolder viewHolder) {

        boolean vocaLearningCycleExist = false;
//        SharedPreferences sharedPreferencesM = context.getSharedPreferences("VocaLearningCycle", MODE_PRIVATE);
        SharedPreferences sharedPreferencesM = context.getSharedPreferences(userID, MODE_PRIVATE);
        Gson gsonM = new Gson();
        String vocaLearningCycleListJson = sharedPreferencesM.getString("VocaLearningCycleList", null);
        Type vocaLearningCycleListType = new TypeToken<ArrayList<VocaLearningCycle>>(){}.getType();
        ArrayList<VocaLearningCycle> check = gsonM.fromJson(vocaLearningCycleListJson, vocaLearningCycleListType);

        if(check != null && !check.toString().equals("[]")){
            vocaLearningCycleExist = true;
        } else {
            vocaLearningCycleExist = false;
        }

        if(vocaLearningCycleExist){
            String vocagroupName = mData.get(position).getVocagroupName() + " vocagroupName";

            Intent intentModify = new Intent(context, VocagroupModify.class);
            intentModify.putExtra("vocagroupName", vocagroupName);
//        intentModify.putExtra("단어장 제목", mData.get(position).getVocagroupName());
//        intentModify.putExtra("단어장 학습 주기", mData.get(position).getVocaLearningCycle());
//        intentModify.putExtra("단어장 영역1", mData.get(position).getVocagroupArea1());
//        intentModify.putExtra("단어장 영역2", mData.get(position).getVocagroupArea2());
//        intentModify.putExtra("단어장 영역1 스위치", mData.get(position).isVocagroupAreaSwitch1());
//        intentModify.putExtra("단어장 영역2 스위치", mData.get(position).isVocagroupAreaSwitch2());
//        intentModify.putExtra("단어장 영역 리스트", mData.get(position).getVocagroupAreaList());
            intentModify.putExtra("단어장 포지션", position);

            SharedPreferences sharedPreferences = context.getSharedPreferences(userID, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String vocagroupJsonSave = gson.toJson(mData.get(position));
            editor.putString(vocagroupName, vocagroupJsonSave); // 저장할 값 입력하기
            editor.commit();

            ((Activity) context).startActivityForResult(intentModify, REQUEST_VOCAGROUP_MODIFY);
        } else {
            Toast.makeText(context.getApplicationContext(), "단어 학습 주기를 먼저 생성해주세요.",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRightClick(int position, RecyclerView.ViewHolder viewHolder) {
        // 단어장과 함께 단어장에 저장되어 있던 단어 데이터를 삭제시킨다
        String vocagroupName = mData.get(position).getVocagroupName() + " vocagroupName";
        String vocaListKey = vocagroupName + " vocaList";
        SharedPreferences sharedPreferences = context.getSharedPreferences(userID, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(vocagroupName);
        editor.remove(vocaListKey);
        editor.commit();

        // 메인의 단어장 목록과 정보를 삭제시킨다
        mData.remove(position);
        notifyItemRemoved(position);
//        notifyDataSetChanged();
    }

    public void onFinish(int position, Vocagroup vocagroup) {
        mData.set(position, vocagroup);
        notifyItemChanged(position);
    }

    public void modifyItem(int position, Vocagroup vocagroup){
        mData.set(position, vocagroup);
        notifyDataSetChanged();
    }


}
