package com.seahahn.cyclicvocareview;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.seahahn.cyclicvocareview.vocagroup.Vocagroup;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.seahahn.cyclicvocareview.MainActivity.userID;

public class VocaModify extends AppCompatActivity {

    final String TAG = "VocaModify";

    ImageButton ImageButton_vocaModify_goback;
    Button Button_vocaModify_modify;
    
    TextView TextView_vocaModify_textBold;
    TextView TextView_vocaModify_textItalic;
    
    String mCurrentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE1 = 1;
    static final int REQUEST_IMAGE_CAPTURE2 = 2;
    static final int REQUEST_IMAGE_CAPTURE_ITEM = 3;
    static final int REQUEST_PICTURE1 = 4;
    static final int REQUEST_PICTURE2 = 5;
    static final int REQUEST_PICTURE_ITEM = 6;

    TextView TextView_vocaModify_vocagroupInput; // '단어장 :' 우측에 단어장 이름 보여줄 텍스트뷰
    TextView TextView_vocaModify_vocagroupArea1;
    TextView TextView_vocaModify_vocagroupArea2;
    Switch Switch_vocaModify_area1Switch;
    Switch Switch_vocaModify_area2Switch;
    EditText EditText_vocaModify_vocagroupArea1Input;
    EditText EditText_vocaModify_vocagroupArea2Input;
    ImageButton ImageButton_vocaModify_gallery1;
    ImageButton ImageButton_vocaModify_gallery2;
    ImageView ImageView_vocaModify_imageView1;
    ImageView ImageView_vocaModify_imageView2;

    TextView TextView_vocaDelete;

    private ArrayList<VocaArea> vocaArea = new ArrayList<>();
    private VocaAreaAdapter vocaAreaAdapter;
    private RecyclerView ListView_vocaModify_listview;

    ArrayList<ArrayList<VocaShowItem>> vocaList = new ArrayList<>();

    String vocagroupName;
    int vocagroupPosition;

    boolean fromSearch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca_modify);

        Intent intent = getIntent();
        fromSearch = intent.getBooleanExtra("fromSearch", false);


        // 단어 학습 액티비티(VocaShow)에서 사용자가 선택한 단어장 정보(제목, 영역 구성) 받아오기
//        Intent intent = getIntent();
//        vocagroupName = intent.getStringExtra("vocagroupName");
//        vocagroupPosition = intent.getIntExtra("단어장 포지션", -1);

        SharedPreferences sharedPreferencesM = getSharedPreferences(userID, MODE_PRIVATE);
        Gson gsonM = new Gson();
        vocagroupName = sharedPreferencesM.getString("vocagroupName", null);
        vocagroupPosition = sharedPreferencesM.getInt("단어장 포지션", 100);
        String vocagroupJson = sharedPreferencesM.getString(vocagroupName, null);
        final Vocagroup vocagroup = gsonM.fromJson(vocagroupJson, Vocagroup.class);

        // 사용자가 선택한 단어장의 단어 목록 받아오기
        String vocaListKey = vocagroupName + " vocaList";
        Log.d(TAG, "vocaListKey : "+vocaListKey);
        String vocaListJson = sharedPreferencesM.getString(vocaListKey, null);
        Log.d(TAG, "vocaListJson : "+vocaListJson);
        Type vocaListType = new TypeToken<ArrayList<ArrayList<VocaShowItem>>>(){}.getType();
        vocaList = gsonM.fromJson(vocaListJson, vocaListType);

        
        // 사용자가 보고 있었던 단어 데이터 받아오기
        ArrayList<VocaShowItem> voca = new ArrayList<>();
        final int vocaPosition = sharedPreferencesM.getInt("vocaModifyPosition", 0);
        String vocaJson = sharedPreferencesM.getString("vocaModify", "");
        Type vocaType = new TypeToken<ArrayList<VocaShowItem>>(){}.getType();
        voca = gsonM.fromJson(vocaJson, vocaType);
        
        permissionCheck();

        // 화면 좌측 상단 좌향 화살표 이미지버튼 - 뒤로 가기(바로 이전 액티비티로 돌아감)
        ImageButton_vocaModify_goback = findViewById(R.id.ImageButton_vocaModify_goback);
        ImageButton_vocaModify_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(VocaModify.this, MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });

        // 두 번째 영역 밑의 추가 영역 출력하는 리사이클러뷰와 어댑터, 레이아웃 매니저 초기화
        ListView_vocaModify_listview = findViewById(R.id.ListView_vocaModify_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        ListView_vocaModify_listview.setLayoutManager(linearLayoutManager);
        vocaAreaAdapter = new VocaAreaAdapter(this, vocaArea);
        ListView_vocaModify_listview.setAdapter(vocaAreaAdapter);

        // '단어장 :' 우측의 단어장 제목, 영역명(ex. 단어, 의미, ...), 각 영역별 입력칸(EditText),
        // 입력칸 우측 이미지 추가 버튼, 입력칸 아래 이미지뷰(이미지 넣기 전엔 안 보임) 초기화
        TextView_vocaModify_vocagroupInput = findViewById(R.id.TextView_vocaModify_vocagroupInput);
        TextView_vocaModify_vocagroupArea1 = findViewById(R.id.TextView_vocaModify_vocagroupArea1);
        TextView_vocaModify_vocagroupArea2 = findViewById(R.id.TextView_vocaModify_vocagroupArea2);
        Switch_vocaModify_area1Switch = findViewById(R.id.Switch_vocaModify_area1Switch);
        Switch_vocaModify_area2Switch = findViewById(R.id.Switch_vocaModify_area2Switch);
        EditText_vocaModify_vocagroupArea1Input = findViewById(R.id.EditText_vocaModify_vocagroupArea1Input);
        EditText_vocaModify_vocagroupArea2Input = findViewById(R.id.EditText_vocaModify_vocagroupArea2Input);
        ImageButton_vocaModify_gallery1 = findViewById(R.id.ImageButton_vocaModify_gallery1);
        ImageButton_vocaModify_gallery2 = findViewById(R.id.ImageButton_vocaModify_gallery2);
        ImageView_vocaModify_imageView1 = findViewById(R.id.ImageView_vocaModify_imageView1);
        ImageView_vocaModify_imageView2 = findViewById(R.id.ImageView_vocaModify_imageView2);

        // 가져온 단어장 정보에 맞게 단어 수정 액티비티의 텍스트뷰와 스위치(앞면, 뒷면 여부) 값 세팅
        // & 가져온 단어 데이터에 맞게 수정 가능한 데이터값 세팅해놓기
        TextView_vocaModify_vocagroupInput.setText(vocagroup.getVocagroupName());
        TextView_vocaModify_vocagroupArea1.setText(vocagroup.getVocagroupArea1());
        TextView_vocaModify_vocagroupArea2.setText(vocagroup.getVocagroupArea2());
        Switch_vocaModify_area1Switch.setChecked(vocagroup.isVocagroupAreaSwitch1());
        Switch_vocaModify_area2Switch.setChecked(vocagroup.isVocagroupAreaSwitch2());
        EditText_vocaModify_vocagroupArea1Input.setText(voca.get(0).getVocaShowText());
        EditText_vocaModify_vocagroupArea2Input.setText(voca.get(1).getVocaShowText());
        ImageView_vocaModify_imageView1.setImageBitmap(BitmapFactory.decodeFile(voca.get(0).getImage()));
        ImageView_vocaModify_imageView2.setImageBitmap(BitmapFactory.decodeFile(voca.get(1).getImage()));
        ImageView_vocaModify_imageView1.setTag(voca.get(0).getImage());
        ImageView_vocaModify_imageView2.setTag(voca.get(1).getImage());

        for(int i = 0; i < vocagroup.getVocagroupAreaList().size(); i++){
            // 추가 영역 쪽 값 세팅
            // 텍스트와 이미지의 경우, voca 자체가 ArrayList이므로 추가 영역에 해당하는 포지션인 2부터 값 넣도록 만듦
            vocaAreaAdapter.addItem(new VocaArea(vocagroup.getVocagroupAreaList().get(i).getEditText_vocagroupAdd_vocagroupAreaInput(), "",
                    vocagroup.getVocagroupAreaList().get(i).isSwitch_vocagroupAdd_areaSwitch(), null, null));
            vocaAreaAdapter.notifyItemInserted(i);
            vocaAreaAdapter.setItem(i, vocaArea, voca.get(i+2).getVocaShowText(), voca.get(i+2).getImage());
            vocaAreaAdapter.notifyItemChanged(i);
        }

        // 이미지 추가 버튼에 카메라, 갤러리 호출하는 기능 부여
        ImageButton_vocaModify_gallery1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("이미지 가져오기");
                builder.setItems(R.array.takePhoto, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // 카메라 호출
                                dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE1);
                                break;
                            case 1: // 갤러리 호출
                                galleryPhoto(REQUEST_PICTURE1);
                                break;
                            case 2: // 사진 제거
                                ImageView_vocaModify_imageView1.setImageBitmap(null);
                                ImageView_vocaModify_imageView1.setTag(null);
                                break;
                            default:
                                break;
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        ImageButton_vocaModify_gallery2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("이미지 가져오기");
                builder.setItems(R.array.takePhoto, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // 카메라 호출
                                dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE2);
                                break;
                            case 1: // 갤러리 호출
                                galleryPhoto(REQUEST_PICTURE2);
                                break;
                            case 2: // 사진 제거
                                ImageView_vocaModify_imageView2.setImageBitmap(null);
                                ImageView_vocaModify_imageView2.setTag(null);
                            default:
                                break;
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        
        
        

        // 화면 우측 상단 버튼 - 단어 수정 기능 구현 (사용자가 VocaShow에서 보고 있었던 단어 데이터가 수정됨)
        Button_vocaModify_modify = findViewById(R.id.Button_vocaModify_modify);
        Button_vocaModify_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EditText_vocaModify_vocagroupArea1Input.getText().toString().isEmpty() &&
                        EditText_vocaModify_vocagroupArea2Input.getText().toString().isEmpty() &&
                        vocaAreaAdapter.isVocaAreaEmpty() == 0) {
                    // 전부 빈 영역인 경우 토스트 메시지 출력
                    // 최소 2개의 영역에는 값이 있어야 함
                    Toast.makeText(getApplicationContext(), "영역 1, 2의 텍스트를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if(EditText_vocaModify_vocagroupArea1Input.getText().toString().isEmpty() ||
                        EditText_vocaModify_vocagroupArea2Input.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "영역 1, 2의 텍스트를 입력해주세요.", Toast.LENGTH_SHORT).show();

                } else {
                    // 1개 이상의 영역에 값이 있는 경우 데이터 저장됨

                    // VocaShowItem 형식에 맞추어 단어 정보를 담은 ArrayList 객체 생성
                    ArrayList<VocaShowItem> voca = new ArrayList<>();
                    voca.add(new VocaShowItem(vocagroup.isVocagroupAreaSwitch1(), EditText_vocaModify_vocagroupArea1Input.getText().toString(), (String)ImageView_vocaModify_imageView1.getTag(), vocaList.get(vocaPosition).get(0).getAddedDate(), vocagroupName));
                    voca.add(new VocaShowItem(vocagroup.isVocagroupAreaSwitch2(), EditText_vocaModify_vocagroupArea2Input.getText().toString(), (String)ImageView_vocaModify_imageView2.getTag(), vocaList.get(vocaPosition).get(1).getAddedDate(), null));
                    for(int i = 0; i < vocaAreaAdapter.getItemCount(); i++){
                        voca.add(new VocaShowItem(vocaAreaAdapter.getItemSide(i), vocaAreaAdapter.getItemText(i), vocaAreaAdapter.getImageTag(i), null, null));
                    }


                    String vocaListKey = vocagroupName + " vocaList"; // sharedPreferences에 저장하기 위해 고유한 키값 만들기
//
//                    // 수정한 단어 정보를 기존에 다른 단어들이 포함된 ArrayList에 추가함
//                    ArrayList<ArrayList<VocaShowItem>> vocaList = new ArrayList<>();
                    SharedPreferences sharedPreferences = getSharedPreferences(userID, MODE_PRIVATE);
                    Gson gson = new Gson();
                    String vocaListJson = sharedPreferences.getString(vocaListKey, null);
                    Type vocaListType = new TypeToken<ArrayList<ArrayList<VocaShowItem>>>(){}.getType();
                    if(gson.fromJson(vocaListJson, vocaListType) != null){
                        vocaList = gson.fromJson(vocaListJson, vocaListType);
                    }

                    vocaList.set(vocaPosition, voca);
//                    Log.d(TAG, "vocaList.set(vocaPosition, voca) : "+vocaList.set(vocaPosition, voca));

                    // 단어 수정 후, 바뀐 단어 목록 데이터를 다시 저장함
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    // '저장할_객체'를 '저장할_객체_클래스명'의 형식에 맞추어 String으로 변환함
                    String vocaListJsonSave = gson.toJson(vocaList);
                    // '쉐어드이름' xml 파일 안에 'String_식별값'이라는 이름으로 '스트링_변수명'의 내용을 저장함
                    editor.putString(vocaListKey, vocaListJsonSave);

//                    editor.putInt("intentRefreshAction", 11);

                    editor.commit();

                    if(fromSearch){
                        // 단어 검색(VocaSearch)에서 수정으로 온 경우
                        Intent intent = new Intent(VocaModify.this, VocaSearch.class);
                        startActivity(intent);
                    } else {
                        // 단어 학습(VocaShow)에서 수정으로 온 경우
                        Intent intentRefreshAction = new Intent(VocaModify.this, VocaShow.class);
                        intentRefreshAction.putExtra("intentRefreshAction", 11);
                        intentRefreshAction.putExtra("vocagroupName", vocagroupName);

                        startActivity(intentRefreshAction);
                    }
                }
            }
        });

        // 화면 우측 상단 '단어 삭제' 텍스트뷰 - 단어 삭제 기능
        TextView_vocaDelete = findViewById(R.id.TextView_vocaDelete);
        TextView_vocaDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("단어 삭제");
                builder.setMessage("삭제하시겠어요?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String vocaListKey = vocagroupName + " vocaList"; // sharedPreferences에 저장하기 위해 고유한 키값 만들기

                        SharedPreferences sharedPreferences = getSharedPreferences(userID, MODE_PRIVATE);
                        Gson gson = new Gson();

                        vocaList.remove(vocaPosition);

                        // 단어 수정 후, 바뀐 단어 목록 데이터를 다시 저장함
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        // '저장할_객체'를 '저장할_객체_클래스명'의 형식에 맞추어 String으로 변환함
                        String vocaListJsonSave = gson.toJson(vocaList);
                        // '쉐어드이름' xml 파일 안에 'String_식별값'이라는 이름으로 '스트링_변수명'의 내용을 저장함
                        editor.putString(vocaListKey, vocaListJsonSave);

//                        editor.putInt("intentRefreshAction", 11);

                        editor.commit();

                        if(fromSearch){
                            // 단어 검색(VocaSearch)에서 수정으로 온 경우
//                            Intent intent = new Intent(VocaModify.this, VocaSearch.class);
//                            startActivity(intent);
                            finish();
                        } else {
                            // 단어 학습(VocaShow)에서 수정으로 온 경우
                            Intent intentRefreshAction = new Intent(VocaModify.this, VocaShow.class);
                            intentRefreshAction.putExtra("intentRefreshAction", 11);

                            startActivity(intentRefreshAction);
                        }

                    }
                });
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

//                String vocaListKey = vocagroupName + " vocaList"; // sharedPreferences에 저장하기 위해 고유한 키값 만들기
//
//                SharedPreferences sharedPreferences = getSharedPreferences(userID, MODE_PRIVATE);
//                Gson gson = new Gson();
//
//                vocaList.remove(vocaPosition);
//
//                // 단어 수정 후, 바뀐 단어 목록 데이터를 다시 저장함
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                // '저장할_객체'를 '저장할_객체_클래스명'의 형식에 맞추어 String으로 변환함
//                String vocaListJsonSave = gson.toJson(vocaList);
//                // '쉐어드이름' xml 파일 안에 'String_식별값'이라는 이름으로 '스트링_변수명'의 내용을 저장함
//                editor.putString(vocaListKey, vocaListJsonSave);
//                editor.commit();
//
//                finish();
            }
        });
    }



    private void permissionCheck(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
//                Log.d("권한 설정", "완료");
            } else {
//                Log.d("권한 설정", "요청");
                ActivityCompat.requestPermissions(VocaModify.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 영역 1 사진 불러오는 부분
        if(requestCode == REQUEST_IMAGE_CAPTURE1 && resultCode == RESULT_OK){
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//
//            ImageView_vocaModify_imageView1.setImageBitmap(imageBitmap);

            File file = new File(mCurrentPhotoPath);
            Bitmap bitmap;
            if(Build.VERSION.SDK_INT >= 29){
                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), Uri.fromFile(file));
                try{
                    bitmap = ImageDecoder.decodeBitmap(source);
                    if(bitmap != null) {
                        Drawable drawable = new BitmapDrawable(getResources(), bitmap);

                        String imagePath = mCurrentPhotoPath;
                        ImageView_vocaModify_imageView1.setTag(imagePath);
//                        Log.d("area1 imagePath", imagePath.toString());
                        ImageView_vocaModify_imageView1.setImageBitmap(bitmap);
                    }
                }catch(IOException e){
                }
            } else {
                try{
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
                    if(bitmap != null){
                        Drawable drawable = new BitmapDrawable(getResources(), bitmap);

                        String imagePath = mCurrentPhotoPath;
                        ImageView_vocaModify_imageView1.setTag(imagePath);
//                        Log.d("area1 imagePath", imagePath.toString());
                        ImageView_vocaModify_imageView1.setImageBitmap(bitmap);
                    }
                }catch(IOException e){
                }
            }
        } else if(requestCode == REQUEST_IMAGE_CAPTURE1 && resultCode == RESULT_CANCELED){
            Toast.makeText(this, "카메라 작동 안됨1", Toast.LENGTH_SHORT).show();
        }
        if(requestCode == REQUEST_PICTURE1 && resultCode == RESULT_OK){
            try{
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();

                FileOutputStream out = new FileOutputStream(mCurrentPhotoPath);
                img.compress(Bitmap.CompressFormat.PNG, 70, out);
                out.close();

                Drawable drawable = new BitmapDrawable(getResources(), img);

                String imagePath = mCurrentPhotoPath;
                ImageView_vocaModify_imageView1.setTag(imagePath);
//                Log.d("area1 imagePath", imagePath.toString());
                ImageView_vocaModify_imageView1.setImageBitmap(img);
            }catch(Exception e){
            }
        } else if(resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_SHORT).show();
        }

        // 영역 2 사진 불러오는 부분
        if(requestCode == REQUEST_IMAGE_CAPTURE2 && resultCode == RESULT_OK){
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//
//            ImageView_vocaModify_imageView2.setImageBitmap(imageBitmap);
            File file = new File(mCurrentPhotoPath);
            Bitmap bitmap;
            if(Build.VERSION.SDK_INT >= 29){
                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), Uri.fromFile(file));
                try{
                    bitmap = ImageDecoder.decodeBitmap(source);
                    if(bitmap != null) {
                        Drawable drawable = new BitmapDrawable(getResources(), bitmap);

                        String imagePath = mCurrentPhotoPath;
                        ImageView_vocaModify_imageView2.setTag(imagePath);
//                        Log.d("area2 imagePath", imagePath);
                        ImageView_vocaModify_imageView2.setImageBitmap(bitmap);
                    }
                }catch(IOException e){
                }
            } else {
                try{
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
                    if(bitmap != null){
                        Drawable drawable = new BitmapDrawable(getResources(), bitmap);

                        String imagePath = mCurrentPhotoPath;
                        ImageView_vocaModify_imageView2.setTag(imagePath);
//                        Log.d("area2 imagePath", imagePath);
                        ImageView_vocaModify_imageView2.setImageBitmap(bitmap);
                    }
                }catch(IOException e){
                }
            }
        } else if(requestCode == REQUEST_IMAGE_CAPTURE2 && resultCode == RESULT_CANCELED){
            Toast.makeText(this, "카메라 작동 안됨2", Toast.LENGTH_SHORT).show();
        }
        if(requestCode == REQUEST_PICTURE2 && resultCode == RESULT_OK){
            File file = new File(mCurrentPhotoPath);
            try{
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();

                FileOutputStream out = new FileOutputStream(mCurrentPhotoPath);
                img.compress(Bitmap.CompressFormat.PNG, 70, out);
                out.close();

                Drawable drawable = new BitmapDrawable(getResources(), img);

                String imagePath = mCurrentPhotoPath;
                ImageView_vocaModify_imageView2.setTag(imagePath);
//                Log.d("area2 imagePath", imagePath);
                ImageView_vocaModify_imageView2.setImageBitmap(img);
            }catch(Exception e){
            }
        } else if(resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_SHORT).show();
        }

        // 추가 영역 사진 불러오는 부분
        if(requestCode == REQUEST_IMAGE_CAPTURE_ITEM && resultCode == RESULT_OK){
            // 이미지버튼 누르면 어댑터에서 설정한 대로 값을 Shared로 저장한 후에 여기서 가져옴
            SharedPreferences sharedPreferences = getSharedPreferences("photo", MODE_PRIVATE);
            mCurrentPhotoPath = sharedPreferences.getString("mCurrentPhotoPathInAdapter", "");
            int position = sharedPreferences.getInt("photoPosition", 55);

//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//
//            vocaAreaAdapter.setBitmap(position, imageBitmap);

            File file = new File(mCurrentPhotoPath);
            Bitmap bitmap;
            if(Build.VERSION.SDK_INT >= 29){
                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), Uri.fromFile(file));
                try{
                    bitmap = ImageDecoder.decodeBitmap(source);
                    if(bitmap != null) {
                        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
//                        Log.d("추가 영역 사진 불러오기", "카메라 "+drawable);

                        String imagePath = mCurrentPhotoPath;
                        vocaAreaAdapter.setImageTag(position, imagePath);
//                        Log.d("areaItem imagePath", imagePath);
                        vocaAreaAdapter.setBitmap(position, bitmap);
                    }
                }catch(IOException e){
                }
            } else {
                try{
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
                    if(bitmap != null){
                        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
//                        Log.d("추가 영역 사진 불러오기", "카메라 "+drawable);

                        String imagePath = mCurrentPhotoPath;
                        vocaAreaAdapter.setImageTag(position, imagePath);
//                        Log.d("areaItem imagePath", imagePath);
                        vocaAreaAdapter.setBitmap(position, bitmap);
                    }
                }catch(IOException e){
                }
            }
        } else if(requestCode == REQUEST_IMAGE_CAPTURE_ITEM && resultCode == RESULT_CANCELED){
            Toast.makeText(this, "카메라 작동 안됨3", Toast.LENGTH_SHORT).show();
        }
        if(requestCode == REQUEST_PICTURE_ITEM && resultCode == RESULT_OK){
            File file = new File(mCurrentPhotoPath);
            SharedPreferences sharedPreferences = getSharedPreferences("photo", MODE_PRIVATE);
            int position = sharedPreferences.getInt("photoPosition", 55);

            try{
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();

//                FileOutputStream out = new FileOutputStream(mCurrentPhotoPath);
//                img.compress(Bitmap.CompressFormat.PNG, 70, out);
//                out.close();

                Drawable drawable = new BitmapDrawable(getResources(), img);
//                Log.d("추가 영역 사진 불러오기", "갤러리 "+drawable);

                String imagePath = mCurrentPhotoPath;
                vocaAreaAdapter.setImageTag(position, imagePath);
//                Log.d("areaItem imagePath", imagePath);
                vocaAreaAdapter.setBitmap(position, img);
            }catch(Exception e){
            }
        } else if(resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", new Locale("ko", "KR")).format(new Date());
        String imageFileName = "PNG_"+timeStamp+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".png", storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent(int requestCode){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;

            try{
                photoFile = createImageFile();
            }catch(IOException e){
            }
            if (photoFile != null){
                Uri photoURI = FileProvider.getUriForFile(this, "com.seahahn.cyclicvocareview.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, requestCode);
            }
//        }
    }

    private void galleryPhoto(int requestCode){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

//        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;

            try{
                photoFile = createImageFile();
            }catch(IOException e){
            }
            if (photoFile != null){
                Uri photoURI = FileProvider.getUriForFile(this, "com.seahahn.cyclicvocareview.fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, requestCode);
            }
//        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("area1", EditText_vocaModify_vocagroupArea1Input.getText().toString()); // 상태를 저장하는 코드. "데이터 이름"은 상수로 선언해주면 좋음
        outState.putString("area2", EditText_vocaModify_vocagroupArea2Input.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        
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