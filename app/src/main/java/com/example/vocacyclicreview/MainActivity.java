package com.example.vocacyclicreview;
import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.Toast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vocacyclicreview.vocagroup.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class MainActivity extends AppCompatActivity {


    private static final int REQUEST_VOCAGROUP_ADD = 1;
    private static final int REQUEST_VOCAGROUP_MODIFY = 2;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 99;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vocagroupAdapter.notifyDataSetChanged();

//        permissionCheck();

//        // 인터넷 사용 권한 허용
//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                .permitDiskReads()
//                .permitDiskWrites()
//                .permitNetwork().build());

//        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if(permissionCheck == PackageManager.PERMISSION_DENIED){
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
//        }else{
//            // 권한 있음
//        }



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
        Button_main_vocagroupmanage = (Button) findViewById(R.id.Button_main_vocagroupmanage);
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
                                SharedPreferences sharedPreferences = getSharedPreferences("VocaLearningCycle", MODE_PRIVATE);
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
        SharedPreferences sharedPreferences = getSharedPreferences("Vocagroup", MODE_PRIVATE);
        Gson gson = new Gson();
        String vocagroupListJson = sharedPreferences.getString("VocagroupList", null);

        Type vocagroupListType = new TypeToken<ArrayList<Vocagroup>>(){}.getType();
        if(gson.fromJson(vocagroupListJson, vocagroupListType) != null && gson.fromJson(vocagroupListJson, vocagroupListType).toString().length() >2){
            vocagroupList = gson.fromJson(vocagroupListJson, vocagroupListType); // 기존에 저장된 단어장 있으면 데이터 가져옴
        } else {
            vocagroupAdapter.removeAll(vocagroupList);
        }
        if(vocagroupList != null) {
            if (vocagroupList.size() > 0) {
                vocagroupAdapter.setData(vocagroupList);
            } else {
                vocagroupAdapter.removeAll(vocagroupList);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // 메인 단어장 목록 변경사항 있는 경우(순서 바뀜, 수정, 삭제 등) 대비해서 저장하기
        SharedPreferences sharedPreferences = getSharedPreferences("Vocagroup", MODE_PRIVATE);
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
                Log.d("권한 설정", "완료");
            } else {
                Log.d("권한 설정", "요청");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 단어장 추가 요청 시 결과 받는 부분
        if(requestCode == REQUEST_VOCAGROUP_ADD && resultCode == RESULT_OK){

            String vocagroupName = data.getStringExtra("vocagroupName");

            SharedPreferences sharedPreferences = getSharedPreferences("Vocagroup", MODE_PRIVATE);
            Gson gson = new Gson();
            String vocagroupJsonLoad = sharedPreferences.getString(vocagroupName, null);
            Vocagroup vocagroup = gson.fromJson(vocagroupJsonLoad, Vocagroup.class);
            vocagroupAdapter.addItem(vocagroup);
//            vocagroupList.add(vocagroup);
            vocagroupAdapter.notifyDataSetChanged();
//            vocagroupList.add(vocagroup);

//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            String vocagroupListJson = gson.toJson(vocagroupAdapter.getData());
//            editor.putString("vocagroupList", vocagroupListJson); // 저장할 값 입력하기
//            editor.commit();
        } else if(resultCode == RESULT_CANCELED){
            Toast.makeText(this, "단어장 추가 취소", Toast.LENGTH_SHORT).show();
        }

        // 단어장 수정 요청 시 결과 받는 부분
        if(requestCode == REQUEST_VOCAGROUP_MODIFY && resultCode == RESULT_OK){

            String vocagroupName = data.getStringExtra("vocagroupName");
            int vocagroupPosition = data.getIntExtra("단어장 포지션", -1);

            SharedPreferences sharedPreferences = getSharedPreferences("Vocagroup", MODE_PRIVATE);
            Gson gson = new Gson();
            String vocagroupJsonLoad = sharedPreferences.getString(vocagroupName, null);
            Vocagroup vocagroup = gson.fromJson(vocagroupJsonLoad, Vocagroup.class);
            vocagroupList.set(vocagroupPosition, vocagroup);
            vocagroupAdapter.notifyDataSetChanged();
        } else if(resultCode == RESULT_CANCELED){
            Toast.makeText(this, "단어장 수정 취소", Toast.LENGTH_SHORT).show();
        }

        // 엑셀 파일로 단어 업로드 시 결과 받는 부분
        if(requestCode == REQUEST_VOCA_UPLOAD && resultCode == RESULT_OK){
            // 엑셀 파일 이용한 대량의 단어 데이터 추가 시작
            // 선택한 단어장의 정보 저장
//                String vocagroupName = mData.get(getAdapterPosition()).getVocagroupName();
            SharedPreferences sharedPreferences = getSharedPreferences("Vocagroup", MODE_PRIVATE);
            int position = sharedPreferences.getInt("단어장 포지션", 33);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String vocagroupJson = gson.toJson(vocagroupAdapter.getData().get(position));
//                editor.putString("vocagroupName", vocagroupName);
//                editor.putString(vocagroupName, vocagroupJson); // 저장할 값 입력하기
//                editor.commit();
//
//                vocagroupName = sharedPreferences.getString("vocagroupName", null);
//
//                String vocaListKey = vocagroupName + " vocaList";

            // 단어장 데이터 불러오기
            Vocagroup vocagroup = vocagroupAdapter.getData().get(position);
            // 단어장의 추가 영역 데이터 불러오기
            vocaAreaAdapter = new VocaAreaAdapter(this, vocaArea);
            for(int i = 0; i < vocagroup.getVocagroupAreaList().size(); i++){
                vocaAreaAdapter.addItem(new VocaArea(vocagroup.getVocagroupAreaList().get(i).getEditText_vocagroupAdd_vocagroupAreaInput(), "",
                        vocagroup.getVocagroupAreaList().get(i).isSwitch_vocagroupAdd_areaSwitch(), null, null));
//                    vocaAreaAdapter.notifyItemInserted(i);
            }
            // ArrayList<VocaShowItem> voca 만들어서 영역 1, 2, 추가영역 각각에 voca.add(new VocaShowItem(...)) 한다
            long now = System.currentTimeMillis(); // 단어 학습 주기에 따른 단어 출력 날짜를 지정하기 위해 단어를 생성한 시간을 단어 데이터에 포함시킴
            Date date = new Date(now);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm", new Locale("ko", "KR"));
            String getTime = format.format(date);

            // 내용 없는 단어 객체 만들기
            ArrayList<VocaShowItem> voca = new ArrayList<>();
            voca.add(new VocaShowItem(vocagroup.isVocagroupAreaSwitch1(), "", null, getTime));
            voca.add(new VocaShowItem(vocagroup.isVocagroupAreaSwitch2(), "", null, Integer.toString(0)));
            for(int i = 0; i < vocaAreaAdapter.getItemCount(); i++){
                voca.add(new VocaShowItem(vocaAreaAdapter.getItemSide(i), "", null, null));
            }

            // ArrayList<ArrayList<VocaShowItem>> vocaList 만든 후, vocaList에 선택한 단어장에 담긴 단어 목록을 불러온다
            String vocaListKey = vocagroupJson + " vocaList";
            ArrayList<ArrayList<VocaShowItem>> vocaList = new ArrayList<>();
            String vocaListJson = sharedPreferences.getString(vocaListKey, null);
            Type vocaListType = new TypeToken<ArrayList<ArrayList<VocaShowItem>>>(){}.getType();
            if(gson.fromJson(vocaListJson, vocaListType) != null){
                vocaList = gson.fromJson(vocaListJson, vocaListType);
            }

            // 엑셀 파일 불러와서 추가하기
            try {
//                            AssetManager assetManager = context.getAssets();
//                            AssetFileDescriptor fileDescriptor = assetManager.openFd("test.xlsx");

//                            File file = new File(context.getFilesDir() + "/app/src/main/assets/test.xlsx");
//                            File file = new File();
//                Log.d(TAG, "엑셀 결과 data ; "+data);
//                Log.d(TAG, "엑셀 결과 data.getData() ; "+data.getData());
//                String path = getRealPathFromURI2(data.getData());
//                Log.d(TAG, "엑셀 결과 path ; "+path);
//                Uri imgUri = data.getData();
//                String imgPath = getPath(getApplicationContext(), imgUri);
//                File localImgFile = null;
//                if(imgUri != null && imgPath != null){
//
//                    InputStream in = getContentResolver().openInputStream(imgUri);//src
//
//                    String extension = imgPath.substring(imgPath.lastIndexOf("."));
//                    localImgFile = new File(getApplicationContext().getFilesDir(), "localImgFile"+extension);
//
//                    if(in != null) {
//                        try {
//                            OutputStream out = new FileOutputStream("/storage/emulated/0/Download/test.xlsx");//dst
//                            try {
//                                // Transfer bytes from in to out
//                                byte[] buf = new byte[1024];
//                                int len;
//                                while ((len = in.read(buf)) > 0) {
//                                    out.write(buf, 0, len);
//                                }
//                            } finally {
//                                out.close();
//                            }
//                        } finally {
//                            in.close();
//                        }
//                    }
//
//                    //InternalStorage로 복사된 localImgFile을 통하여 File에 접근가능
//                }
                FileInputStream fileInputStream = new FileInputStream("/storage/emulated/0/Download/test.xlsx");
//                            FileInputStream fileInputStream = context.getAssets().open("test.xlsx");
//                            FileInputStream file = fileDescriptor.createInputStream();
                XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);

                int rowindex = 0;
                int columnindex = 0;

                //시트 수 (첫번째에만 존재하므로 0을 준다)
                //만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다
                XSSFSheet sheet = workbook.getSheetAt(0);

                //행의 수
                int rows = sheet.getPhysicalNumberOfRows();
                for (rowindex = 1; rowindex < rows; rowindex++) {
                    XSSFRow row = sheet.getRow(rowindex);
                    ArrayList<String> vocaEachArea = new ArrayList<>();
                    if (row != null) {
                        //셀의 수
                        int cells = row.getPhysicalNumberOfCells();
                        for (columnindex = 0; columnindex <= cells; columnindex++) {
                            //셀값을 읽는다
                            XSSFCell cell = row.getCell(columnindex);
                            String value = "";
                            //셀이 빈값일경우를 위한 널체크
                            if (cell == null) {
                                continue;
                            } else {
                                //타입별로 내용 읽기
                                switch (cell.getCellType()) {
                                    case FORMULA:
                                        value = cell.getCellFormula();
                                        break;
                                    case NUMERIC:
                                        value = cell.getNumericCellValue() + "";
                                        break;
                                    case STRING:
                                        value = cell.getStringCellValue() + "";
                                        break;
                                    case BLANK:
                                        value = cell.getBooleanCellValue() + "";
                                        break;
                                    case ERROR:
                                        value = cell.getErrorCellValue() + "";
                                        break;
                                }
                            }
                            vocaEachArea.add(value);
                        }
                        // 내용 없는 단어 객체에 내용 채워넣기
                        voca.set(0, new VocaShowItem(vocagroup.isVocagroupAreaSwitch1(), vocaEachArea.get(0), null, getTime));
                        voca.set(1, new VocaShowItem(vocagroup.isVocagroupAreaSwitch2(), vocaEachArea.get(1), null, Integer.toString(0)));
                        for(int i = 0; i < vocaAreaAdapter.getItemCount(); i++){
                            voca.set(i+2, new VocaShowItem(vocaAreaAdapter.getItemSide(i), vocaEachArea.get(i+2), null, null));
                        }
                        vocaList.add(voca);
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }

            // 바뀐 vocaList를 다시 같은 SF Key에 저장한다
            String vocaListJsonSave = gson.toJson(vocaList);
            editor.putString(vocaListKey, vocaListJsonSave);
            editor.commit();
        }

        SharedPreferences sharedPreferences = getSharedPreferences("Vocagroup", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String vocagroupListJson = gson.toJson(vocagroupAdapter.getData());
        editor.putString("VocagroupList", vocagroupListJson); // 저장할 값 입력하기
        editor.commit();
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}