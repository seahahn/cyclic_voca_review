package com.example.vocacyclicreview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.vocacyclicreview.R;
import com.example.vocacyclicreview.SearchAdapter;

import java.util.ArrayList;
import java.util.List;

public class VocaSearch extends AppCompatActivity {

    ImageButton ImageButton_vocaSearch_goback;

    private List<String> list;          // 데이터를 넣은 리스트변수
    private ListView ListView_vocaSearch_listView;          // 검색을 보여줄 리스트변수
    private EditText EditText_vocaSearch_editSearch;        // 검색어를 입력할 Input 창
    private SearchAdapter adapter;      // 리스트뷰에 연결할 아답터
    private ArrayList<String> arraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca_search);

        ImageButton_vocaSearch_goback = findViewById(R.id.ImageButton_vocaSearch_goback);
        ImageButton_vocaSearch_goback.setClickable(true);
        ImageButton_vocaSearch_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(VocaSearch.this, MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });


        EditText_vocaSearch_editSearch = (EditText) findViewById(R.id.EditText_vocaSearch_editSearch);
        ListView_vocaSearch_listView = (ListView) findViewById(R.id.ListView_vocaSearch_listView);

        // 리스트를 생성한다.
        list = new ArrayList<String>();

        // 검색에 사용할 데이터을 미리 저장한다.
        settingList();

        // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
        arraylist = new ArrayList<String>();
        arraylist.addAll(list);

        // 리스트에 연동될 아답터를 생성한다.
        adapter = new SearchAdapter(list, this);

        // 리스트뷰에 아답터를 연결한다.
        ListView_vocaSearch_listView.setAdapter(adapter);

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


    // 검색을 수행하는 메소드
    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            list.addAll(arraylist);
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < arraylist.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (arraylist.get(i).toLowerCase().contains(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    list.add(arraylist.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }


    // 검색에 사용될 데이터를 리스트에 추가한다.
    private void settingList(){
        list.add("apple");
        list.add("interest");
        list.add("computer");
        list.add("mind");
        list.add("for");
        list.add("and");

    }
}