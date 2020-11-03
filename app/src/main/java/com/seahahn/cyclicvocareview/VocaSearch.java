package com.seahahn.cyclicvocareview;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seahahn.cyclicvocareview.R;
import com.seahahn.cyclicvocareview.vocagroup.Vocagroup;

import java.lang.reflect.Type;
import java.util.*;

import static com.seahahn.cyclicvocareview.MainActivity.userID;

public class VocaSearch extends AppCompatActivity {

    private static final String TAG = "VocaSearch";

    ImageButton ImageButton_vocaSearch_goback;
    Button Button_vocaSearch_filter;

    private ArrayList<ArrayList<VocaShowItem>> list = new ArrayList<>(); // 데이터를 넣은 리스트변수
    private ArrayList<ArrayList<VocaShowItem>> arraylist;

    EditText EditText_vocaSearch_editSearch; // 검색어를 입력할 Input 창
    VocaSearchAdapter vocaSearchAdapter = new VocaSearchAdapter(this, list); // 리사이클러뷰에 연결할 아답터
    RecyclerView ListView_vocaSearch_listView; // 검색을 보여줄 리스트변수
    ArrayList<ArrayList<VocaShowItem>> vocaList = new ArrayList<>();
    ArrayList<ArrayList<VocaShowItem>> vocaListTotal = new ArrayList<>(); // 데이터를 넣은 리스트변수
    ArrayList<Vocagroup> vocagroupList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this); // 레이아웃 매니저 초기화

    String filter;
    String filterWrongVoca;
    String filterNewVoca;
    String filterReviewVoca;
    String filterNone;
    int filterVocaCycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca_search);

        // 화면 좌측 상단 좌향 화살표 이미지버튼 - 뒤로가기 기능
        ImageButton_vocaSearch_goback = findViewById(R.id.ImageButton_vocaSearch_goback);
        ImageButton_vocaSearch_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VocaSearch.this, MainActivity.class);
                startActivity(intent);
//                finish();
            }
        });

        // 화면 우측 '필터' 버튼
        Button_vocaSearch_filter = findViewById(R.id.Button_vocaSearch_filter);
        Button_vocaSearch_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(getApplicationContext(), VocaSearchFilter.class);
                startActivity(mIntent);
            }
        });

        EditText_vocaSearch_editSearch = findViewById(R.id.EditText_vocaSearch_editSearch);
        ListView_vocaSearch_listView = findViewById(R.id.ListView_vocaSearch_listView);
        ListView_vocaSearch_listView.setLayoutManager(linearLayoutManager);


        // 리스트를 생성한다.
//        list = new ArrayList<String>();

        // 검색에 사용할 데이터을 미리 저장한다.
        settingList();

        // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
        arraylist = new ArrayList<>();
        arraylist.addAll(list);
//        Log.d(TAG, "arraylist : "+arraylist);

        // 리사이클러뷰에 아답터를 연결한다.
        ListView_vocaSearch_listView.setAdapter(vocaSearchAdapter);

        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        EditText_vocaSearch_editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                String text = EditText_vocaSearch_editSearch.getText().toString();
                search(text);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        settingList();
        vocaSearchAdapter.notifyDataSetChanged();
    }


    // 검색을 수행하는 메소드
    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            list.addAll(arraylist);
        }
        // 문자 입력을 할때..
        else {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < arraylist.size(); i++) {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (arraylist.get(i).get(0).getVocaShowText().contains(charText)) {
                    // 검색된 데이터를 리스트에 추가한다.
                    list.add(arraylist.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        vocaSearchAdapter.notifyDataSetChanged();
    }


    // 검색에 사용될 데이터를 리스트에 추가한다.
    private void settingList(){

        list.clear();
        vocaListTotal.clear();

        SharedPreferences sharedPreferences = getSharedPreferences(userID, MODE_PRIVATE);
        Gson gson = new Gson();
        String vocagroupListJson = sharedPreferences.getString("VocagroupList", null);
        Type vocagroupListType = new TypeToken<ArrayList<Vocagroup>>(){}.getType();
        if(gson.fromJson(vocagroupListJson, vocagroupListType) != null && gson.fromJson(vocagroupListJson, vocagroupListType).toString().length() >2){
            vocagroupList = gson.fromJson(vocagroupListJson, vocagroupListType); // 기존에 저장된 단어장 있으면 데이터 가져옴
        }

        // 사용자가 선택한 필터값 불러오기
        filter = sharedPreferences.getString("VocaSearchFilter", getResources().getString(R.string.searchFilterNone));
        filterWrongVoca = getResources().getString(R.string.searchFilterVocaCycleListWrong);
        filterNewVoca = getResources().getString(R.string.searchFilterVocaCycleListNew);
        filterReviewVoca = getResources().getString(R.string.searchFilterVocaCycleListReview);
        filterNone = getResources().getString(R.string.searchFilterNone);
        if(filter.equals(filterWrongVoca)){
            filterVocaCycle = -2;
        } else if(filter.equals(filterNewVoca)){
            filterVocaCycle = 0;
        } else if(filter.equals(filterReviewVoca)){
            filterVocaCycle = -1;
        }

        for(int i = 0; i < vocagroupList.size(); i++){
            if(filter.equals(vocagroupList.get(i).getVocagroupName())){
                // 필터로 단어장 이름을 선택한 경우
                String vocagroupName = vocagroupList.get(i).getVocagroupName()+" vocagroupName";
                String vocaListKey = vocagroupName+" vocaList";
                String vocaListJson = sharedPreferences.getString(vocaListKey, null);
                Type vocaListType = new TypeToken<ArrayList<ArrayList<VocaShowItem>>>(){}.getType();
                if(gson.fromJson(vocaListJson, vocaListType) != null){
                    vocaList = gson.fromJson(vocaListJson, vocaListType);
                }
                vocaListTotal.addAll(vocaList);
                Log.d(TAG, "단어장 이름");
            }else if(filter.equals(filterWrongVoca)){
                // 필터로 '틀린 단어' 선택한 경우
                String vocagroupName = vocagroupList.get(i).getVocagroupName()+" vocagroupName";
                String vocaListKey = vocagroupName+" vocaList";
                String vocaListJson = sharedPreferences.getString(vocaListKey, null);
                Type vocaListType = new TypeToken<ArrayList<ArrayList<VocaShowItem>>>(){}.getType();
                if(gson.fromJson(vocaListJson, vocaListType) != null){
                    vocaList = gson.fromJson(vocaListJson, vocaListType);
                }
                for(int j = 0; j < vocaList.size(); j++){
                    if(vocaList.get(j).get(1).getAddedDate().equals(String.valueOf(filterVocaCycle))){
                        vocaListTotal.add(vocaList.get(j));
                    }
                }
                Log.d(TAG, "틀린 단어");
            } else if(filter.equals(filterNewVoca)){
                // 필터로 '새 단어' 선택한 경우
                String vocagroupName = vocagroupList.get(i).getVocagroupName()+" vocagroupName";
                String vocaListKey = vocagroupName+" vocaList";
                String vocaListJson = sharedPreferences.getString(vocaListKey, null);
                Type vocaListType = new TypeToken<ArrayList<ArrayList<VocaShowItem>>>(){}.getType();
                if(gson.fromJson(vocaListJson, vocaListType) != null){
                    vocaList = gson.fromJson(vocaListJson, vocaListType);
                }
                for(int j = 0; j < vocaList.size(); j++){
                    if(vocaList.get(j).get(1).getAddedDate().equals(String.valueOf(filterVocaCycle))){
                        vocaListTotal.add(vocaList.get(j));
                        Log.d(TAG, "새 단어");
                    }
                }
            } else if(filter.equals(filterReviewVoca)){
                // 필터로 '복습할 단어' 선택한 경우
                String vocagroupName = vocagroupList.get(i).getVocagroupName()+" vocagroupName";
                String vocaListKey = vocagroupName+" vocaList";
                String vocaListJson = sharedPreferences.getString(vocaListKey, null);
                Type vocaListType = new TypeToken<ArrayList<ArrayList<VocaShowItem>>>(){}.getType();
                if(gson.fromJson(vocaListJson, vocaListType) != null){
                    vocaList = gson.fromJson(vocaListJson, vocaListType);
                }
                for(int j = 0; j < vocaList.size(); j++){
                    if(vocaList.get(j).get(1).getAddedDate().equals(String.valueOf(filterVocaCycle))
                    || Integer.parseInt(vocaList.get(j).get(1).getAddedDate()) > 0){
                        vocaListTotal.add(vocaList.get(j));
                        Log.d(TAG, "복습할 단어");
                    }
                }
            } else if(filter.equals(filterNone)){
                // '필터 없음'인 경우
                // 모든 단어 그대로 다 리스트에 올림
                String vocagroupName = vocagroupList.get(i).getVocagroupName()+" vocagroupName";
                String vocaListKey = vocagroupName+" vocaList";
                String vocaListJson = sharedPreferences.getString(vocaListKey, null);
                Type vocaListType = new TypeToken<ArrayList<ArrayList<VocaShowItem>>>(){}.getType();
                if(gson.fromJson(vocaListJson, vocaListType) != null){
                    vocaList = gson.fromJson(vocaListJson, vocaListType);
                }
                vocaListTotal.addAll(vocaList);
                Log.d(TAG, "필터 없음");
            }
        }
        list.addAll(vocaListTotal);
    }
}