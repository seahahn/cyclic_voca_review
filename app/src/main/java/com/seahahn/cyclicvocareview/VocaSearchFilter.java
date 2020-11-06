package com.seahahn.cyclicvocareview;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;

import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seahahn.cyclicvocareview.vocagroup.Vocagroup;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.seahahn.cyclicvocareview.MainActivity.userID;

public class VocaSearchFilter extends AppCompatActivity {

    TextView TextView_close;

//    private ArrayList<ArrayList<VocaShowItem>> list = new ArrayList<>(); // 데이터를 넣은 리스트변수
//    private ArrayList<ArrayList<VocaShowItem>> arraylist;
    private ArrayList<String> list = new ArrayList<>(); // 데이터를 넣은 리스트변수
    private ArrayList<String> arraylist;

    EditText EditText_vocaSearchFilter_editSearch; // 검색어를 입력할 Input 창
    VocaSearchFilterAdapter vocaSearchFilterAdapter = new VocaSearchFilterAdapter(this, list); // 리사이클러뷰에 연결할 아답터
    RecyclerView ListView_vocaSearchFilter_listView; // 검색을 보여줄 리스트변수
    ArrayList<ArrayList<VocaShowItem>> vocaList = new ArrayList<>();
    ArrayList<ArrayList<VocaShowItem>> vocaListTotal = new ArrayList<>(); // 데이터를 넣은 리스트변수
    ArrayList<Vocagroup> vocagroupList = new ArrayList<>();
    ArrayList<String> vocaCurrentCycleList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this); // 레이아웃 매니저 초기화

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_voca_search_filter);

        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;

        // 화면 우측 상단 '닫기' 텍스트뷰
        TextView_close = findViewById(R.id.TextView_close);
        TextView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        EditText_vocaSearchFilter_editSearch = findViewById(R.id.EditText_vocaSearchFilter_editSearch);
        ListView_vocaSearchFilter_listView = findViewById(R.id.ListView_vocaSearchFilter_listView);
        ListView_vocaSearchFilter_listView.setLayoutManager(linearLayoutManager);

        // 검색에 사용할 데이터을 미리 저장한다.
        settingList();

        // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
        arraylist = new ArrayList<>();
        arraylist.addAll(list);

        // 리사이클러뷰에 아답터를 연결한다.
        ListView_vocaSearchFilter_listView.setAdapter(vocaSearchFilterAdapter);

        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        EditText_vocaSearchFilter_editSearch.addTextChangedListener(new TextWatcher() {
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
                String text = EditText_vocaSearchFilter_editSearch.getText().toString();
                search(text);
            }
        });


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
                if (arraylist.get(i).contains(charText)) {
                    // 검색된 데이터를 리스트에 추가한다.
                    list.add(arraylist.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        vocaSearchFilterAdapter.notifyDataSetChanged();
    }

    // 검색에 사용될 데이터를 리스트에 추가한다.
    private void settingList(){
        vocaCurrentCycleList.add(getResources().getString(R.string.searchFilterVocaCycleListWrong));
        vocaCurrentCycleList.add(getResources().getString(R.string.searchFilterVocaCycleListNew));
        vocaCurrentCycleList.add(getResources().getString(R.string.searchFilterVocaCycleListReview));

        list.clear();
        list.add(getResources().getString(R.string.searchFilterNone));

        SharedPreferences sharedPreferences = getSharedPreferences(userID, MODE_PRIVATE);
        Gson gson = new Gson();
        String vocagroupListJson = sharedPreferences.getString("VocagroupList", null);
        Type vocagroupListType = new TypeToken<ArrayList<Vocagroup>>(){}.getType();
        if(gson.fromJson(vocagroupListJson, vocagroupListType) != null && gson.fromJson(vocagroupListJson, vocagroupListType).toString().length() >2){
            vocagroupList = gson.fromJson(vocagroupListJson, vocagroupListType); // 기존에 저장된 단어장 있으면 데이터 가져옴
        }

        for(int i = 0; i < vocagroupList.size(); i++){
            list.add(vocagroupList.get(i).getVocagroupName());
        }
        list.addAll(vocaCurrentCycleList);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();

            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);

                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
}