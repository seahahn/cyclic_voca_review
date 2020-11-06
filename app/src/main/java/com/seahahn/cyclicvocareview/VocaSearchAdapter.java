package com.seahahn.cyclicvocareview;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seahahn.cyclicvocareview.vocagroup.Vocagroup;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.seahahn.cyclicvocareview.MainActivity.userID;

public class VocaSearchAdapter extends RecyclerView.Adapter<VocaSearchAdapter.VocaSearchViewHolder> {

    private static final String TAG = "VocaSearchAdapter";
    private Context context;
     private ArrayList<ArrayList<VocaShowItem>> mData;

    String vocagroupName;
    ArrayList<ArrayList<VocaShowItem>> vocaList = new ArrayList<>();

     public VocaSearchAdapter(Context context, ArrayList<ArrayList<VocaShowItem>> mData) {
        this.context = context;
        this.mData = mData;
     }

     public void setItems(ArrayList<ArrayList<VocaShowItem>> items) {
         this.mData = items;
         notifyDataSetChanged();
     }

     @NonNull
     @Override
     public VocaSearchAdapter.VocaSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_listview, parent, false);
         return new VocaSearchAdapter.VocaSearchViewHolder(view);
     }

     @Override
     public void onBindViewHolder(@NonNull VocaSearchAdapter.VocaSearchViewHolder holder, int position) {
         holder.onBind(mData.get(position));
     }

     @Override
     public int getItemCount() {
         return mData.size();
     }

     public class VocaSearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

         TextView TextView_vocaSearch;

         public VocaSearchViewHolder(@NonNull View itemView) {
             super(itemView);
             TextView_vocaSearch = itemView.findViewById(R.id.TextView_vocaSearch);
         }

         public void onBind(ArrayList<VocaShowItem> arrayListVocaShowItem){
             TextView_vocaSearch.setText(arrayListVocaShowItem.get(0).getVocaShowText());
             TextView_vocaSearch.setOnClickListener(this);
         }

         @Override
         public void onClick(View v) {
             if(v == TextView_vocaSearch){

                 SharedPreferences sharedPreferences = context.getSharedPreferences(userID, MODE_PRIVATE);
                 SharedPreferences.Editor editor = sharedPreferences.edit();
                 Gson gson = new Gson();

                 // 사용자가 선택한 단어가 속한 단어장 정보(제목, 영역 구성) 가져오기
                 String vocagroupName = mData.get(getAdapterPosition()).get(0).getVocagroupName(); // 단어장 제목에 +" vocagroupName" 한 것
                 String vocagroupJson = sharedPreferences.getString(vocagroupName, null); // 단어장 데이터 JSON 불러오기
                 Vocagroup vocagroup = gson.fromJson(vocagroupJson, Vocagroup.class); // 단어장 형식에 맞춘 데이터로 변환

                 // 사용자가 선택한 단어가 속한 단어장에 있는 단어 목록 가져오기
                 String vocaListKey = vocagroupName + " vocaList";
                 String vocaListJson = sharedPreferences.getString(vocaListKey, null);
                 Type vocaListType = new TypeToken<ArrayList<ArrayList<VocaShowItem>>>(){}.getType();
                 vocaList = gson.fromJson(vocaListJson, vocaListType);

                 // 사용자가 선택한 단어장 정보(제목, 영역 구성) 보내기
                 String vocagroupJsonM = gson.toJson(vocagroup);
                 editor.putString(vocagroupName, vocagroupJsonM); // 저장할 값 입력하기

                 // 사용자가 선택한 단어장의 단어 목록 보내기
                 String vocaListKeyM = vocagroupName + " vocaList";
                 String vocaListJsonM = gson.toJson(vocaList);
                 editor.putString(vocaListKeyM, vocaListJsonM);

                 // 사용자가 현재 보고 있는 단어 데이터 보내기
                 String vocaJson = gson.toJson(mData.get(getAdapterPosition()));
                 editor.putString("vocagroupName", vocagroupName);
                 editor.putString("vocaModify", vocaJson);
                 editor.putInt("vocaModifyPosition", getAdapterPosition());

                 editor.commit();

                 Intent mIntent = new Intent(context, VocaModify.class);
//                 mIntent.putExtra("vocagroupName", vocagroupName);
                 mIntent.putExtra("fromSearch", true);
                 context.startActivity(mIntent);

             }
         }
     }
}
