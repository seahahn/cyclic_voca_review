package com.seahahn.cyclicvocareview;

import android.Manifest;
import android.content.*;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.widget.Toast;

import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.*;
import com.seahahn.cyclicvocareview.vocagroup.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.*;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    private static final int WRITE_REQUEST_CODE = 30;
    private static final int READ_REQUEST_CODE = 31;
    public static String userID; // 사용자가 로그인한 경우, 사용자 식별을 위한 고유값을 여기에 지정해줌
    public static String userEmail; // 사용자가 로그인한 경우, 사용자의 이메일 주소를 여기에 지정해줌

    private static final int REQUEST_VOCAGROUP_ADD = 1;
    private static final int REQUEST_VOCAGROUP_MODIFY = 2;
//    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 99;
    private static final int REQUEST_VOCA_UPLOAD = 3;
    private static final String TAG = "MainActivity";
    ImageButton ImageButton_main_help;
    ImageButton ImageButton_main_search;
    ImageButton ImageButton_main_setting;
    Button Button_main_profiles;
    Button Button_main_vocagroupmanage;
    Button Button_main_vocaupload;

    // 메인 단어장 목록 출력을 위한 요소들 초기화 시작
    ArrayList<Vocagroup> vocagroupList = new ArrayList<>(); // 메인에서 보여줄 단어장 리스트
    VocagroupAdapter vocagroupAdapter = new VocagroupAdapter(this, vocagroupList); // 리스트를 보여주기 위한 어댑터
    RecyclerView ListView_main_listview; // 메인의 리사이클러뷰 초기화
    ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelperCallback(vocagroupAdapter)); // 좌우 스와이프 모션을 위한 아이템터치헬퍼 초기화 후 어댑터에 연결
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this); // 레이아웃 매니저 초기화

    private ArrayList<VocaArea> vocaArea = new ArrayList<>();
    private VocaAreaAdapter vocaAreaAdapter;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체 초기화
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null){
            userID = "default";
            userEmail = "default";
        } else {
            userID = user.getUid();
            userEmail = user.getEmail();
        }

        vocagroupAdapter.notifyDataSetChanged();

//        permissionCheck();

//        // 인터넷 사용 권한 허용
//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                .permitDiskReads()
//                .permitDiskWrites()
//                .permitNetwork().build());

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck == PackageManager.PERMISSION_DENIED){
//            // 권한 없음
            PermissionListener permissionlistener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    Toast.makeText(MainActivity.this, "권한 허가", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                    Toast.makeText(MainActivity.this, "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                }
            };
            TedPermission.with(this)
                    .setPermissionListener(permissionlistener)
                    .setRationaleMessage("엑셀 파일을 이용한 단어 업로드를 위해서 파일 읽기/쓰기 권한이 필요해요.")
                    .setDeniedMessage("단어 추가...편하게 하시지...\n나중에 [설정] > [권한] 에서 권한을 허용할 수 있어요.")
                    .setPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
                    .check();
        }else{
//            // 권한 있음
        }



        // 도움말 버튼 세팅
        ImageButton_main_help = findViewById(R.id.ImageButton_main_help);
        ImageButton_main_help.setClickable(true);
        ImageButton_main_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Help.class);
                startActivity(intent);
            }
        });

        // 단어 검색 버튼 세팅
        ImageButton_main_search = findViewById(R.id.ImageButton_main_search);
        ImageButton_main_search.setClickable(true);
        ImageButton_main_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VocaSearch.class);
                startActivity(intent);
            }
        });

        // 설정 버튼 세팅
        ImageButton_main_setting = findViewById(R.id.ImageButton_main_setting);
        ImageButton_main_setting.setClickable(true);
        ImageButton_main_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Setting.class);
                startActivity(intent);
            }
        });

        // 단어장 관리 버튼 세팅
        Button_main_vocagroupmanage = findViewById(R.id.Button_main_vocagroupmanage);
        Button_main_vocagroupmanage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this , Button_main_vocagroupmanage);
                MenuInflater inf = popup.getMenuInflater();
                inf.inflate(R.menu.menu_main_vocagroupmanage, popup.getMenu());
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu_main_vocagroupAdd:

                                boolean vocaLearningCycleExist = false;
//                                SharedPreferences sharedPreferences = getSharedPreferences("VocaLearningCycle", MODE_PRIVATE);
                                SharedPreferences sharedPreferences = getSharedPreferences(userID, MODE_PRIVATE);

                                Gson gson = new Gson();
                                String vocaLearningCycleListJson = sharedPreferences.getString("VocaLearningCycleList", null);
                                Type vocaLearningCycleListType = new TypeToken<ArrayList<VocaLearningCycle>>(){}.getType();
                                ArrayList<VocaLearningCycle> check = gson.fromJson(vocaLearningCycleListJson, vocaLearningCycleListType);

                                if(check != null && !check.toString().equals("[]")){
                                    vocaLearningCycleExist = true;
                                } else {
                                    vocaLearningCycleExist = false;
                                }

                                if(vocaLearningCycleExist){
                                    Intent intentAdd = new Intent(MainActivity.this, VocagroupAdd.class);
                                    startActivityForResult(intentAdd, REQUEST_VOCAGROUP_ADD);
                                    break;
                                } else {
                                    Toast.makeText(getApplicationContext(), "단어 학습 주기를 먼저 생성해주세요.",Toast.LENGTH_SHORT).show();
                                    break;
                                }

                            case R.id.menu_main_vocaLearningCycleManage:
                                Intent intentCycleModify = new Intent(MainActivity.this, VocaLearningCycleManage.class);
                                startActivity(intentCycleModify);
                                break;
                        }
                        return false;
                    }
                });
            }
        });

        // 단어 업로드 버튼 세팅
        Button_main_vocaupload = findViewById(R.id.Button_main_vocaupload);
        Button_main_vocaupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this , Button_main_vocaupload);
                MenuInflater inf = popup.getMenuInflater();
                inf.inflate(R.menu.menu_main_backup, popup.getMenu()); // 버튼 눌렀을 때 띄울 팝업 형태 정해주기
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // 메뉴 내 각각의 아이템 클릭 시 어떻게 할지 정하기
                        switch (item.getItemId()){
                            case R.id.menu_main_backupWrite:
                                // 백업파일 내보내기
                                long now = System.currentTimeMillis(); // 단어 학습 주기에 따른 단어 출력 날짜를 지정하기 위해 단어를 생성한 시간을 단어 데이터에 포함시킴
                                Date date = new Date(now);
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", new Locale("ko", "KR"));
                                String getTime = format.format(date);

                                // 현재 시각 + 사용자 이메일 주소 + .txt 로 파일 제목 만들어서 생성
                                String fileName = getTime+"_"+userEmail+".txt";

                                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                intent.setType("text/plain");
                                intent.putExtra(Intent.EXTRA_TITLE,fileName);

                                // onActivityResult 에서 파일 내용 넣은 후에 작성 완료할 것
                                startActivityForResult(intent, WRITE_REQUEST_CODE);

                                break;
                            case R.id.menu_main_backupRead:
                                // 백업파일 불러오기
                                Intent intent2 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                intent2.addCategory(Intent.CATEGORY_OPENABLE);
                                intent2.setType("text/plain");
                                startActivityForResult(intent2, READ_REQUEST_CODE);
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
            }
        });

        // 메인 단어장 목록 출력하기
        ListView_main_listview = findViewById(R.id.ListView_main_listview);
        ListView_main_listview.setLayoutManager(linearLayoutManager); // 리사이클러뷰에 레이아웃 매니저 연결
        helper.attachToRecyclerView(ListView_main_listview); // 리사이클러뷰에 아이템터치헬퍼 연결
        ListView_main_listview.setAdapter(vocagroupAdapter); // 리사이클러뷰에 어댑터 연결
    }

    @Override
    protected void onResume() {
        super.onResume();
        vocagroupAdapter.notifyDataSetChanged();

        // 메인 단어장 목록을 담은 데이터 불러오기
        SharedPreferences sharedPreferences = getSharedPreferences(userID, MODE_PRIVATE);
        Gson gson = new Gson();
        String vocagroupListJson = sharedPreferences.getString("VocagroupList", null);
        Type vocagroupListType = new TypeToken<ArrayList<Vocagroup>>(){}.getType();
        if(gson.fromJson(vocagroupListJson, vocagroupListType) != null && gson.fromJson(vocagroupListJson, vocagroupListType).toString().length() >2){
            vocagroupList = gson.fromJson(vocagroupListJson, vocagroupListType); // 기존에 저장된 단어장 있으면 데이터 가져옴
//            Log.d(TAG, "vocagroupList : "+vocagroupList);
        } else {
            vocagroupAdapter.removeAll(vocagroupList);
        }
        if(vocagroupList != null) {
            if (vocagroupList.size() > 0) {
                vocagroupAdapter.setData(vocagroupList);
//                Log.d(TAG, "vocagroupAdapter : "+vocagroupAdapter.getData());
            } else {
                vocagroupAdapter.removeAll(vocagroupList);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // 메인 단어장 목록 변경사항 있는 경우(순서 바뀜, 수정, 삭제 등) 대비해서 저장하기
        SharedPreferences sharedPreferences = getSharedPreferences(userID, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String vocagroupListJson = gson.toJson(vocagroupAdapter.getData());
        editor.putString("VocagroupList", vocagroupListJson); // 저장할 값 입력하기
        editor.commit();
    }

    private void permissionCheck(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
//                Log.d("권한 설정", "완료");
            } else {
//                Log.d("권한 설정", "요청");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        boolean fromBackup = false;
        // 백업으로 파일 불러온 경우에는 true로 바뀜
        // 이를 통해 빈 단어장 목록 혹은 기존에 있던 단어장 목록으로 다시 돌아오는 것을 방지함함
        // 단어장 추가 요청 시 결과 받는 부분
        if (requestCode == REQUEST_VOCAGROUP_ADD && resultCode == RESULT_OK) {
            assert data != null;
            String vocagroupName = data.getStringExtra("vocagroupName");

            SharedPreferences sharedPreferences = getSharedPreferences(userID, MODE_PRIVATE);
            Gson gson = new Gson();
            String vocagroupJsonLoad = sharedPreferences.getString(vocagroupName, null);
            Vocagroup vocagroup = gson.fromJson(vocagroupJsonLoad, Vocagroup.class);
            vocagroupAdapter.addItem(vocagroup);
        } else if (requestCode == REQUEST_VOCAGROUP_ADD && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "단어장 추가 취소", Toast.LENGTH_SHORT).show();
        }

        // 단어장 수정 요청 시 결과 받는 부분
        if (requestCode == REQUEST_VOCAGROUP_MODIFY && resultCode == RESULT_OK) {

            assert data != null;
            String vocagroupName = data.getStringExtra("vocagroupName");
            int vocagroupPosition = data.getIntExtra("단어장 포지션", -1);

            SharedPreferences sharedPreferences = getSharedPreferences(userID, MODE_PRIVATE);
            Gson gson = new Gson();
            String vocagroupJsonLoad = sharedPreferences.getString(vocagroupName, null);
            Vocagroup vocagroup = gson.fromJson(vocagroupJsonLoad, Vocagroup.class);
            vocagroupList.set(vocagroupPosition, vocagroup);
            vocagroupAdapter.notifyDataSetChanged();
        } else if (requestCode == REQUEST_VOCAGROUP_MODIFY && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "단어장 수정 취소", Toast.LENGTH_SHORT).show();
        }

        // 엑셀 파일로 단어 업로드 시 결과 받는 부분
        if (requestCode == REQUEST_VOCA_UPLOAD && resultCode == RESULT_OK) {
            // 엑셀 파일 이용한 대량의 단어 데이터 추가 시작

            // 선택한 단어장의 정보 저장
            SharedPreferences sharedPreferences = getSharedPreferences(userID, MODE_PRIVATE);
            int position = sharedPreferences.getInt("단어장 포지션", 33);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
//            String vocagroupJson = gson.toJson(vocagroupAdapter.getData().get(position));

            // 단어장 데이터 불러오기
            Vocagroup vocagroup = vocagroupAdapter.getData().get(position);
            // 단어장의 추가 영역 데이터 불러오기
            vocaAreaAdapter = new VocaAreaAdapter(this, vocaArea);
            for (int i = 0; i < vocagroup.getVocagroupAreaList().size(); i++) {
                vocaAreaAdapter.addItem(new VocaArea(vocagroup.getVocagroupAreaList().get(i).getEditText_vocagroupAdd_vocagroupAreaInput(), "",
                        vocagroup.getVocagroupAreaList().get(i).isSwitch_vocagroupAdd_areaSwitch(), null, null));
            }
            // ArrayList<VocaShowItem> voca 만들어서 영역 1, 2, 추가영역 각각에 voca.add(new VocaShowItem(...)) 한다
            long now = System.currentTimeMillis(); // 단어 학습 주기에 따른 단어 출력 날짜를 지정하기 위해 단어를 생성한 시간을 단어 데이터에 포함시킴
            Date date = new Date(now);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm", new Locale("ko", "KR"));
            String getTime = format.format(date);

            String vocagroupName = vocagroup.getVocagroupName() + " vocagroupName";

            // 내용 없는 단어 객체 만들기
            ArrayList<VocaShowItem> voca = new ArrayList<>();
            voca.add(new VocaShowItem(vocagroup.isVocagroupAreaSwitch1(), "", null, getTime, vocagroupName));
            voca.add(new VocaShowItem(vocagroup.isVocagroupAreaSwitch2(), "", null, Integer.toString(0), null));
            for (int i = 0; i < vocaAreaAdapter.getItemCount(); i++) {
                voca.add(new VocaShowItem(vocaAreaAdapter.getItemSide(i), "", null, null, null));
            }

            // ArrayList<ArrayList<VocaShowItem>> vocaList 만든 후, vocaList에 선택한 단어장에 담긴 단어 목록을 불러온다
            String vocaListKey = vocagroupName + " vocaList";
            ArrayList<ArrayList<VocaShowItem>> vocaList = new ArrayList<>();
            String vocaListJson = sharedPreferences.getString(vocaListKey, null);
            Type vocaListType = new TypeToken<ArrayList<ArrayList<VocaShowItem>>>() {
            }.getType();
            if (gson.fromJson(vocaListJson, vocaListType) != null) {
                vocaList = gson.fromJson(vocaListJson, vocaListType);
            }

            // 엑셀 파일 불러와서 추가하기
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
//                InputStream is = getBaseContext().getResources().getAssets().open("my_excel.xls");
                Workbook wb = Workbook.getWorkbook(inputStream);

                if (wb != null) {
                    Sheet sheet = wb.getSheet(0);   // 시트 불러오기
                    if (sheet != null) {
                        int colTotal = voca.size();
//                        sheet.getColumns();    // 전체 컬럼 (단어장의 영역 수와 일치해야 함)
                        int rowIndexStart = 1;                  // row 인덱스 시작 (둘째줄부터)
                        int rowTotal = sheet.getColumn(colTotal - 1).length;

                        StringBuilder sb;
                        for (int row = rowIndexStart; row < rowTotal; row++) {

                            // 내용 없는 단어 객체 만들기
                            voca = new ArrayList<>();
                            voca.add(new VocaShowItem(vocagroup.isVocagroupAreaSwitch1(), "", null, getTime, vocagroupName));
                            voca.add(new VocaShowItem(vocagroup.isVocagroupAreaSwitch2(), "", null, Integer.toString(0), null));
                            for (int i = 0; i < vocaAreaAdapter.getItemCount(); i++) {
                                voca.add(new VocaShowItem(vocaAreaAdapter.getItemSide(i), "", null, null, null));
                            }

                            ArrayList<String> vocaEachArea = new ArrayList<>();
//                            sb = new StringBuilder();
                            for (int col = 0; col < colTotal; col++) {
                                String contents = sheet.getCell(col, row).getContents();
//                                sb.append("col" + col + " : " + contents + " , ");
                                vocaEachArea.add(contents);
                            }
//                            Log.i("test", sb.toString());
                            // 내용 없는 단어 객체에 내용 채워넣기
                            voca.set(0, new VocaShowItem(vocagroup.isVocagroupAreaSwitch1(), vocaEachArea.get(0), null, getTime, vocagroupName));
                            voca.set(1, new VocaShowItem(vocagroup.isVocagroupAreaSwitch2(), vocaEachArea.get(1), null, Integer.toString(0), null));
                            for(int i = 0; i < vocaAreaAdapter.getItemCount(); i++){
                                voca.set(i+2, new VocaShowItem(vocaAreaAdapter.getItemSide(i), vocaEachArea.get(i+2), null, null, null));
                            }
                            vocaList.add(voca);
//                            Log.d(TAG, "vocaList : "+vocaList);
                            vocaEachArea.clear();
//                            voca.clear();
//                            Log.d(TAG, "vocaList : "+vocaList);
                        }
                    }
                }
            } catch (IOException | BiffException e) {
                e.printStackTrace();
            }
            // 바뀐 vocaList를 다시 같은 SF Key에 저장한다
            String vocaListJsonSave = gson.toJson(vocaList);
            editor.putString(vocaListKey, vocaListJsonSave);
            editor.commit();

        } else if (requestCode == REQUEST_VOCA_UPLOAD && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "단어 업로드 취소", Toast.LENGTH_SHORT).show();
        }

        // 백업 파일 생성 후 저장한 결과 작성한 부분
        if (requestCode == WRITE_REQUEST_CODE && resultCode == RESULT_OK) {
            // 텍스트 파일 생성
            Uri uri = data.getData();
            addTextFile(uri);

            // 저장된 사용자 데이터 가져오기
            SharedPreferences sharedPreferences = getSharedPreferences(userID, MODE_PRIVATE);
            Gson gson = new Gson();
            String backupDataString = gson.toJson(sharedPreferences.getAll(), Map.class); // 저장된 데이터를 String 형태로 변환
            try {
                writeString(backupDataString); // String 형태로 변환한 데이터를 텍스트 파일에 작성
                FinishRecord(); // 작성 완료
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == WRITE_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "백업파일 내보내기 취소", Toast.LENGTH_SHORT).show();
        }

        // 백업 파일 불러온 결과 작성한 부분
        if (requestCode == READ_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData(); // 텍스트 파일 가져옴
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri); // 가져온 텍스트 파일 열기

                StringBuilder backupDataString = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    backupDataString.append(line).append("\n"); // 가져온 텍스트 파일의 내용을 StringBuilder에 넣음
                }

                SharedPreferences sharedPreferences = getSharedPreferences(userID, MODE_PRIVATE);
                Gson gson = new Gson();
                Map shared = gson.fromJson(backupDataString.toString(), Map.class); // StringBuilder에 넣은 내용을 Map으로 바꿈

                // Iterator 사용하여 불러온 백업 파일에 저장된
                Iterator<String> keys = shared.keySet().iterator(); // 맵으로 만든 데이터의 키, 값쌍을 하나씩 SharedPreferences에 추가함
                while (keys.hasNext()){
                    String key = keys.next();
                    String value = shared.get(key).toString();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(key, value);
                    editor.commit();
                }
                fromBackup = true;

                String test = sharedPreferences.getString("VocagroupList", null);
//                Log.d(TAG, "vocagroupListJson : "+test);

                Toast.makeText(getApplicationContext(), "백업 파일 불러오기 완료", Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else if (requestCode == READ_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "백업파일 불러오기 취소", Toast.LENGTH_SHORT).show();
        }

        if(!fromBackup){
            SharedPreferences sharedPreferences = getSharedPreferences(userID, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String vocagroupListJson = gson.toJson(vocagroupAdapter.getData());
            editor.putString("VocagroupList", vocagroupListJson); // 저장할 값 입력하기
            editor.commit();

//            Log.d(TAG, "vocagroupListJson : "+vocagroupListJson);
        }
        fromBackup = false;

    }

    private ParcelFileDescriptor pfd;
    private FileOutputStream fileOutputStream;
    private FileInputStream fileInputStream;

    public void addTextFile(Uri uri){
        try {
            pfd = this.getContentResolver().openFileDescriptor(uri, "w");
            fileOutputStream = new FileOutputStream(pfd.getFileDescriptor());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void getTextFile(Uri uri){
        try {
            pfd = this.getContentResolver().openFileDescriptor(uri, "w");
            fileInputStream = new FileInputStream(pfd.getFileDescriptor());
//            Log.d(TAG, "fileInputStream : "+fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void writeString(String st) throws IOException {
        if(fileOutputStream!=null) fileOutputStream.write(st.getBytes());
    }

    public void readString() throws IOException {
        if(fileInputStream!=null) fileInputStream.read();
//        Log.d(TAG, "fileInputStream : "+fileInputStream);
    }

    public void FinishRecord() throws IOException {
        Toast.makeText(getApplicationContext(), "백업 파일 저장 완료", Toast.LENGTH_LONG).show();
        fileOutputStream.close();
        pfd.close();
    }

    public void FinishReading() throws IOException {
        Toast.makeText(getApplicationContext(), "백업 파일 불러오기 완료", Toast.LENGTH_LONG).show();
        fileInputStream.close();
        pfd.close();
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

}