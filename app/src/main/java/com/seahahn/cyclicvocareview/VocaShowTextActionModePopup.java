package com.seahahn.cyclicvocareview;
import android.os.*;

import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class VocaShowTextActionModePopup extends AppCompatActivity {

    private static final String TAG = "VocaShowTextActionModePopup";
    TextView TextView_input;
    TextView TextView_output;

    Spinner spinner;

    RadioGroup radioGroup1;
    RadioGroup radioGroup2;
    RadioGroup radioGroup3;
    RadioButton RadioButton_en;
    RadioButton RadioButton_zhCN;
    RadioButton RadioButton_zhTW;
    RadioButton RadioButton_ja;
    RadioButton RadioButton_es;
    RadioButton RadioButton_fr;
    RadioButton RadioButton_ru;
    RadioButton RadioButton_de;
    RadioButton RadioButton_it;
    RadioButton RadioButton_vi;
    RadioButton RadioButton_id;
    RadioButton RadioButton_th;

    Button Button_webSearch;
    TextView TextView_close;

    String input;
    String langElement;
    String targetLang;
    String output;

    String[] languagesKey = {"한국어", "영어", "중국어-간체", "중국어-번체", "일본어", "스페인어", "프랑스어", "러시아어", "독일어", "이탈리아어", "베트남어", "인도네시아어", "태국어"};
    String[] languagesValue = {"ko", "en", "zh-CN", "zh-TW", "ja", "es", "fr", "ru", "de", "it", "vi", "id", "th"};
    HashMap<String, String> languages = new HashMap<>();
    boolean initLangElementToSpinner = false;
    boolean spinnerItemSelectable = false;
    boolean spinnerItemSelected = false;

    TranslationThread translationThread = new TranslationThread();
    boolean threadOn = false;


    String clientId = "3VGQteSJL9ZLikB83dYi";//애플리케이션 클라이언트 아이디값";
    String clientSecret = "ptcy05qSPE";//애플리케이션 클라이언트 시크릿값";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_voca_show_text_action_mode_popup);

        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;

        Intent intent = getIntent();
        input = intent.getStringExtra("input"); // 사용자가 선택한 텍스트 값 가져오기

        // 입력된 텍스트(input)와 출력될 텍스트(output) 초기화
        TextView_input = findViewById(R.id.TextView_input);
        TextView_output = findViewById(R.id.TextView_output);
        // 가져온 텍스트 값 보여주기
        TextView_input.setText(input);

        threadOn = true;
        translationThread.start();

        // 사용자가 원하는 언어로 출력할 수 있게 언어 선택할 수 있는 라디오버튼
        radioGroup1 = findViewById(R.id.radioGroup1);
        radioGroup2 = findViewById(R.id.radioGroup2);
        radioGroup3 = findViewById(R.id.radioGroup3);
        RadioButton_en = findViewById(R.id.RadioButton_en);
        RadioButton_zhCN = findViewById(R.id.RadioButton_zhCN);
        RadioButton_zhTW = findViewById(R.id.RadioButton_zhTW);
        RadioButton_ja = findViewById(R.id.RadioButton_ja);
        RadioButton_es = findViewById(R.id.RadioButton_es);
        RadioButton_fr = findViewById(R.id.RadioButton_fr);
        RadioButton_ru = findViewById(R.id.RadioButton_ru);
        RadioButton_de = findViewById(R.id.RadioButton_de);
        RadioButton_it = findViewById(R.id.RadioButton_it);
        RadioButton_vi = findViewById(R.id.RadioButton_vi);
        RadioButton_id = findViewById(R.id.RadioButton_id);
        RadioButton_th = findViewById(R.id.RadioButton_th);

        // 하단의 '웹 검색' 버튼 초기화
        Button_webSearch = findViewById(R.id.Button_webSearch);

        // 라디오그룹에 라디오 버튼 선택 시 만들어낼 결과 구현
        // 3개의 열로 만들기 위해서 세 개의 라디오그룹으로 나누었음
        // 라디오버튼 선택 시 모든 라디오그룹의 라디오버튼 선택이 초기화되어 1개만 선택하는 효과를 줌
        radioGroup1.clearCheck();
        radioGroup2.clearCheck();
        radioGroup3.clearCheck();
        radioGroup1.setOnCheckedChangeListener(listener1);
        radioGroup2.setOnCheckedChangeListener(listener2);
        radioGroup3.setOnCheckedChangeListener(listener3);

        // 입력한 텍스트의 언어 선택을 위한 스피너 초기화
        initLangElementToSpinner = true;
        spinner = findViewById(R.id.spinner);
        for(int i = 0; i < languagesKey.length; i++){
            languages.put(languagesKey[i], languagesValue[i]);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languagesKey); // 스피너 목록에는 언어명(languagesKey)을 출력함
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        // getIndex : 사용자가 입력한 텍스트의 언어를 감지한 초기값을 언어 선택 스피너에 세팅해주기 위한 메소드
        // 스피너의 아이템 중에서 감지한 언어와 동일한 문자열을 찾아서 그 문자열의 포지션을 리턴함
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!initLangElementToSpinner && spinnerItemSelectable){
                    langElement = languagesValue[spinner.getSelectedItemPosition()];
                    spinnerItemSelected = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        TextView_close = findViewById(R.id.TextView_close);
        TextView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        initLangElementToSpinner = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        threadOn = false;
        translationThread.interrupt();
    }

    // 사용자가 입력한 텍스트의 언어를 감지한 초기값을 언어 선택 스피너에 세팅해주기 위한 메소드
    // 스피너의 아이템 중에서 감지한 언어와 동일한 문자열을 찾아서 그 문자열의 포지션을 리턴함
    private int getIndex(String[] languagesValue){
        System.out.println("getIndex langElement : "+langElement);
        for(int i = 0; i < languagesValue.length; i++){
            if(languagesValue[i].equals(langElement)){
                return i;
            }
        }
        return 0;
    }


    private RadioGroup.OnCheckedChangeListener listener1 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // 다른 라디오그룹의 선택 초기화
            radioGroup2.setOnCheckedChangeListener(null);
            radioGroup3.setOnCheckedChangeListener(null);
            radioGroup2.clearCheck();
            radioGroup3.clearCheck();
            radioGroup2.setOnCheckedChangeListener(listener2);
            radioGroup3.setOnCheckedChangeListener(listener3);

            switch(checkedId){
                case R.id.RadioButton_en: // 영어
                    targetLang = "en";
                    translationThread.interrupt();
                    break;
                case R.id.RadioButton_ja: // 일본어
                    targetLang = "ja";
                    translationThread.interrupt();
                    break;
                case R.id.RadioButton_ru: // 러시아어
                    targetLang = "ru";
                    translationThread.interrupt();
                    break;
                case R.id.RadioButton_vi: // 베트남어
                    targetLang = "vi";
                    translationThread.interrupt();
                    break;
            }
        }
    };
    private RadioGroup.OnCheckedChangeListener listener2 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // 다른 라디오그룹의 선택 초기화
            radioGroup1.setOnCheckedChangeListener(null);
            radioGroup3.setOnCheckedChangeListener(null);
            radioGroup1.clearCheck();
            radioGroup3.clearCheck();
            radioGroup1.setOnCheckedChangeListener(listener1);
            radioGroup3.setOnCheckedChangeListener(listener3);

            switch(checkedId){
                case R.id.RadioButton_zhCN: // 중국어-간체
                    targetLang = "zh-CN";
                    translationThread.interrupt();
                    break;
                case R.id.RadioButton_es: // 스페인어
                    targetLang = "es";
                    translationThread.interrupt();
                    break;
                case R.id.RadioButton_de: // 독일어
                    targetLang = "de";
                    translationThread.interrupt();
                    break;
                case R.id.RadioButton_id: // 인도네시아어
                    targetLang = "id";
                    translationThread.interrupt();
                    break;
            }
        }
    };
    private RadioGroup.OnCheckedChangeListener listener3 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // 다른 라디오그룹의 선택 초기화
            radioGroup1.setOnCheckedChangeListener(null);
            radioGroup2.setOnCheckedChangeListener(null);
            radioGroup1.clearCheck();
            radioGroup2.clearCheck();
            radioGroup1.setOnCheckedChangeListener(listener1);
            radioGroup2.setOnCheckedChangeListener(listener2);

            switch(checkedId){
                case R.id.RadioButton_zhTW: // 중국어-번체
                    targetLang = "zh-TW";
                    translationThread.interrupt();
                    break;
                case R.id.RadioButton_fr: // 프랑스어
                    targetLang = "fr";
                    translationThread.interrupt();
                    break;
                case R.id.RadioButton_it: // 이탈리아어
                    targetLang = "it";
                    translationThread.interrupt();
                    break;
                case R.id.RadioButton_th: // 태국어
                    targetLang = "th";
                    translationThread.interrupt();
                    break;
            }
        }
    };

    private static String post(String apiUrl, Map<String, String> requestHeaders, String text){
        HttpURLConnection con = connect(apiUrl);
        String postParams =  "query="  + text; //원본언어: 한국어 (ko) -> 목적언어: 영어 (en)
        try {
            con.setRequestMethod("POST");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postParams.getBytes());
                wr.flush();
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 응답
                return readBody(con.getInputStream());
            } else {  // 에러 응답
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }

    private class TranslationThread extends Thread {

        @Override
        public void run() {
            while(threadOn){
                try{
                    // 네이버 파파고 언어감지 API 적용
                    String query;
                    try {
                        query = URLEncoder.encode(input, "UTF-8"); // 사용자가 선택한 텍스트를 언어 감지에 입력
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException("인코딩 실패", e);
                    }
                    String langScanApiURL = "https://openapi.naver.com/v1/papago/detectLangs";

                    Map<String, String> requestHeaders = new HashMap<>();
                    requestHeaders.put("X-Naver-Client-Id", clientId);
                    requestHeaders.put("X-Naver-Client-Secret", clientSecret);

                    String responseBody = post(langScanApiURL, requestHeaders, query);

                    // Json 형식으로 받아온 감지 결과를 String으로 변환하여 번역 API에 전달해줌
                    if(!spinnerItemSelected){
                        // 사용자가 스피너로 언어 선택을 따로 하지 않았을 경우에만 자동 언어 감지
                        langElement = JsonParser.parseString(responseBody).getAsJsonObject().get("langCode").getAsString();
                    }

                    // 네이버 파파고 번역 API 적용
                    try {
                        String text = URLEncoder.encode(input, "UTF-8");
                        String translationApiURL = "https://openapi.naver.com/v1/papago/n2mt";
                        URL url = new URL(translationApiURL);
                        HttpURLConnection con = (HttpURLConnection)url.openConnection();
                        con.setRequestMethod("POST");
                        con.setRequestProperty("X-Naver-Client-Id", clientId);
                        con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                        // post request
                        // 언어 감지 결과와 사용자가 선택한 언어를 여기서 적용함
                        // source 가 감지한 언어(input), target 이 사용자가 보길 원하는 언어(output)
                        if(targetLang == null){
                            targetLang = "ko";
                        }
                        String postParams = "source="+langElement+"&target="+targetLang+"&text=" + text;
                        System.out.println(postParams);
                        con.setDoOutput(true);
                        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                        wr.writeBytes(postParams);
                        wr.flush();
                        wr.close();
                        int responseCode = con.getResponseCode();
                        BufferedReader br;
                        if(responseCode==200) { // 정상 호출
                            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        } else {  // 에러 발생
                            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                        }
                        String inputLine;
                        StringBuffer response = new StringBuffer();
                        while ((inputLine = br.readLine()) != null) {
                            response.append(inputLine);
                        }
                        br.close();

                        // Json 형식으로 받아온 번역 결과를 String으로 변환
                        String result = JsonParser.parseString(String.valueOf(response)).getAsJsonObject().get("message").getAsJsonObject().get("result").getAsJsonObject().get("translatedText").getAsString();

                        output = result;
                        translationHandler.sendEmptyMessage(0);

                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }

                while(initLangElementToSpinner){
                    translationHandler.sendEmptyMessage(1);
                    initLangElementToSpinner = false;
                }

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    continue;
                }
            }
        }
    }

    Handler translationHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    TextView_output.setText(output);
                    break;
                case 1:
                    spinner.setSelection(getIndex(languagesValue));
                    spinnerItemSelectable = true;
                    break;
                default:
                    break;
            }
        }
    };

}