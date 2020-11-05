package com.seahahn.cyclicvocareview;
import android.widget.Toast;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.Typeface;
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

import java.io.*;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.seahahn.cyclicvocareview.MainActivity.userID;

public class VocaAdd extends AppCompatActivity {

    final String TAG = "VocaAdd";

    ImageButton ImageButton_vocaAdd_goback;
    Button Button_vocaAdd_add;

    TextView TextView_vocaAdd_textBold;
    TextView TextView_vocaAdd_textItalic;

    String mCurrentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE1 = 1;
    static final int REQUEST_IMAGE_CAPTURE2 = 2;
    static final int REQUEST_IMAGE_CAPTURE_ITEM = 3;
    static final int REQUEST_PICTURE1 = 4;
    static final int REQUEST_PICTURE2 = 5;
    static final int REQUEST_PICTURE_ITEM = 6;

    TextView TextView_vocaAdd_vocagroupInput; // '단어장 :' 우측에 단어장 이름 보여줄 텍스트뷰
    TextView TextView_vocaAdd_vocagroupArea1;
    TextView TextView_vocaAdd_vocagroupArea2;
    Switch Switch_vocaAdd_area1Switch;
    Switch Switch_vocaAdd_area2Switch;
    EditText EditText_vocaAdd_vocagroupArea1Input;
    EditText EditText_vocaAdd_vocagroupArea2Input;
    ImageButton ImageButton_vocaAdd_gallery1;
    ImageButton ImageButton_vocaAdd_gallery2;
    ImageView ImageView_vocaAdd_imageView1;
    ImageView ImageView_vocaAdd_imageView2;


    private ArrayList<VocaArea> vocaArea = new ArrayList<>();
    private VocaAreaAdapter vocaAreaAdapter;
    private RecyclerView ListView_vocaAdd_listview;

    String vocagroupName;
    int vocagroupPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca_add);


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

        Log.d(TAG, "vocagroupName : "+vocagroupName);

        permissionCheck();

        // 화면 좌측 상단 좌향 화살표 이미지버튼 - 뒤로 가기(바로 이전 액티비티로 돌아감)
        ImageButton_vocaAdd_goback = findViewById(R.id.ImageButton_vocaAdd_goback);
        ImageButton_vocaAdd_goback.setClickable(true);
        ImageButton_vocaAdd_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(VocaAdd.this, MainActivity.class);
//                startActivity(intent);

//                SharedPreferences sharedPreferences = getSharedPreferences(userID, MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                Gson gson = new Gson();
//                String vocagroupJson = gson.toJson(vocagroup);
//                editor.putString(vocagroupName, vocagroupJson); // 저장할 값 입력하기
//                editor.commit();

                Intent intentRefreshAction = new Intent(VocaAdd.this, VocaShow.class);
                intentRefreshAction.putExtra("intentRefreshAction", 10);

                startActivity(intentRefreshAction);

//                finish();
            }
        });

        // 상단 두번째 툴바 B 모양 텍스트뷰 - 텍스트 블럭 지정 후 누르면 선택한 텍스트의 글씨체 굵게(Bold) 만들어주는 기능
        TextView_vocaAdd_textBold = findViewById(R.id.TextView_vocaAdd_textBold);
        TextView_vocaAdd_textBold.setClickable(true);
        TextView_vocaAdd_textBold.setOnClickListener(new View.OnClickListener() {
            boolean textBold;
            @Override
            public void onClick(View v) {
                if(!textBold){
//                    EditText_vocaAdd_vocagroupArea1Input.setPaintFlags(EditText_vocaAdd_vocagroupArea1Input.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
                    EditText_vocaAdd_vocagroupArea1Input.setTypeface(null, Typeface.BOLD);
                    textBold = true;
                } else {
//                    EditText_vocaAdd_vocagroupArea1Input.setPaintFlags(EditText_vocaAdd_vocagroupArea1Input.getPaintFlags() &~ Paint.FAKE_BOLD_TEXT_FLAG);
                    EditText_vocaAdd_vocagroupArea1Input.setTypeface(null, Typeface.NORMAL);
                    textBold = false;
                }
            }
        });

        // 상단 두번째 툴바 기울어진 I 모양 텍스트뷰 - 텍스트 블럭 지정 후 누르면 선택한 텍스트의 글씨체 기울임(Italic체) 만들어주는 기능
        TextView_vocaAdd_textItalic = findViewById(R.id.TextView_vocaAdd_textItalic);
        TextView_vocaAdd_textItalic.setClickable(true);
        TextView_vocaAdd_textItalic.setOnClickListener(new View.OnClickListener() {
            boolean textItalic;
            @Override
            public void onClick(View v) {
                if(!textItalic){
                    EditText_vocaAdd_vocagroupArea1Input.setTypeface(null, Typeface.ITALIC);
                    textItalic = true;
                } else {
                    EditText_vocaAdd_vocagroupArea1Input.setTypeface(null, Typeface.NORMAL);
                    textItalic = false;
                }
            }
        });

        // 액티비티 스크롤뷰 내의 리니어 레이아웃 초기화
        // 카메라, 갤러리에서 사진 불러온 후 레이아웃 하단에 추가하기 위해서 초기화함
        // dispatchTakePictureIntent(카메라 호출 후 사진 가져오는 메소드)와 ACTION_GET_CONTENT 인텐트 결과로 받은 이미지는 레이아웃 최하단에 위치하게 됨
//        LinearLayout_vocaAdd = findViewById(R.id.LinearLayout_vocaAdd);

        // 상단 두번째 툴바 가운데 카메라 모양 이미지버튼 - 카메라 불러오기 기능. 사진 찍으면 찍은 이미지 가져와서 추가함
//        ImageButton_vocaAdd_camera = findViewById(R.id.ImageButton_vocaAdd_camera);
//        ImageButton_vocaAdd_camera.setClickable(true);
//        ImageButton_vocaAdd_camera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dispatchTakePictureIntent();
//            }
//        });

        // 상단 두번째 툴바 그림 모양 이미지버튼 - 갤러리에서 이미지 불러오기 기능. 가져온 이미지는 레이아웃 최하단에 추가됨
//        ImageButton_vocaAdd_gallery = findViewById(R.id.ImageButton_vocaAdd_gallery);
//        ImageButton_vocaAdd_gallery.setClickable(true);
//        ImageButton_vocaAdd_gallery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent, REQUEST_PICTURE);
//            }
//        });

        // 상단 두번째 툴바 마이크 모양 이미지버튼 - 음성녹음 버튼 (미완성)

//        ImageButton_vocaAdd_voiceRecord = findViewById(R.id.ImageButton_vocaAdd_voiceRecord);
//        ImageButton_vocaAdd_voiceRecord.setClickable(true);
//        ImageButton_vocaAdd_voiceRecord.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String recordOnOff;
//                if(recorder == null){
//                    recordOnOff = getString(R.string.recordStart);
//                }else{
//                    recordOnOff = getString(R.string.recordEnd);
//                }
//                final AlertDialog.Builder builder = new AlertDialog.Builder(VocaAdd.this);
//                builder.setTitle("음성 녹음").setMessage("녹음하시려면 '시작'을 눌러주세요.");
//                builder.setPositiveButton(recordOnOff, null);
//                builder.setNegativeButton("종료", null);
//                final AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//
//                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if(recorder == null){
//                            recordStart();
//                        }else{
//                            recordStop();
//                        }
//                    }
//                });
//                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        alertDialog.dismiss();
//                    }
//                });
//
//            }
//        });

        // 두 번째 영역 밑의 추가 영역 출력하는 리사이클러뷰와 어댑터, 레이아웃 매니저 초기화
        ListView_vocaAdd_listview = findViewById(R.id.ListView_vocaAdd_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        ListView_vocaAdd_listview.setLayoutManager(linearLayoutManager);
        vocaAreaAdapter = new VocaAreaAdapter(this, vocaArea);
        ListView_vocaAdd_listview.setAdapter(vocaAreaAdapter);

        // '단어장 :' 우측의 단어장 제목, 영역명(ex. 단어, 의미, ...), 각 영역별 입력칸(EditText),
        // 입력칸 우측 이미지 추가 버튼, 입력칸 아래 이미지뷰(이미지 넣기 전엔 안 보임) 초기화
        TextView_vocaAdd_vocagroupInput = findViewById(R.id.TextView_vocaAdd_vocagroupInput);
        TextView_vocaAdd_vocagroupArea1 = findViewById(R.id.TextView_vocaAdd_vocagroupArea1);
        TextView_vocaAdd_vocagroupArea2 = findViewById(R.id.TextView_vocaAdd_vocagroupArea2);
        Switch_vocaAdd_area1Switch = findViewById(R.id.Switch_vocaAdd_area1Switch);
        Switch_vocaAdd_area2Switch = findViewById(R.id.Switch_vocaAdd_area2Switch);
        EditText_vocaAdd_vocagroupArea1Input = findViewById(R.id.EditText_vocaAdd_vocagroupArea1Input);
        EditText_vocaAdd_vocagroupArea2Input = findViewById(R.id.EditText_vocaAdd_vocagroupArea2Input);
        ImageButton_vocaAdd_gallery1 = findViewById(R.id.ImageButton_vocaAdd_gallery1);
        ImageButton_vocaAdd_gallery2 = findViewById(R.id.ImageButton_vocaAdd_gallery2);
        ImageView_vocaAdd_imageView1 = findViewById(R.id.ImageView_vocaAdd_imageView1);
        ImageView_vocaAdd_imageView2 = findViewById(R.id.ImageView_vocaAdd_imageView2);

        // 가져온 단어장 정보에 맞게 단어 추가 액티비티의 텍스트뷰와 스위치(앞면, 뒷면 여부) 값 세팅
        TextView_vocaAdd_vocagroupInput.setText(vocagroup.getVocagroupName());
        TextView_vocaAdd_vocagroupArea1.setText(vocagroup.getVocagroupArea1());
        TextView_vocaAdd_vocagroupArea2.setText(vocagroup.getVocagroupArea2());
        Switch_vocaAdd_area1Switch.setChecked(vocagroup.isVocagroupAreaSwitch1());
        Switch_vocaAdd_area2Switch.setChecked(vocagroup.isVocagroupAreaSwitch2());
//        vocaArea.addAll(vocagroup.getVocagroupAreaList());
        for(int i = 0; i < vocagroup.getVocagroupAreaList().size(); i++){
            vocaAreaAdapter.addItem(new VocaArea(vocagroup.getVocagroupAreaList().get(i).getEditText_vocagroupAdd_vocagroupAreaInput(), "",
                    vocagroup.getVocagroupAreaList().get(i).isSwitch_vocagroupAdd_areaSwitch(), null, null));
            vocaAreaAdapter.notifyItemInserted(i);
            Log.d("VocaAdd addItem", i+" 스위치 값 제대로 들어가는지 확인-> "+vocagroup.getVocagroupAreaList().get(i).isSwitch_vocagroupAdd_areaSwitch());
        }

        // 이미지 추가 버튼에 카메라, 갤러리 호출하는 기능 부여
        ImageButton_vocaAdd_gallery1.setOnClickListener(new View.OnClickListener() {
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
                                ImageView_vocaAdd_imageView1.setImageBitmap(null);
                                ImageView_vocaAdd_imageView1.setTag(null);
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

        ImageButton_vocaAdd_gallery2.setOnClickListener(new View.OnClickListener() {
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
                                ImageView_vocaAdd_imageView2.setImageBitmap(null);
                                ImageView_vocaAdd_imageView2.setTag(null);
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

        // 화면 우측 상단 버튼 - 단어 추가 기능 구현 (사용자가 선택한 단어장에 사용자가 입력한 데이터를 가진 단어가 추가됨)
        Button_vocaAdd_add = findViewById(R.id.Button_vocaAdd_add);
        Button_vocaAdd_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EditText_vocaAdd_vocagroupArea1Input.getText().toString().isEmpty() &&
                        EditText_vocaAdd_vocagroupArea2Input.getText().toString().isEmpty() &&
                        vocaAreaAdapter.isVocaAreaEmpty() == 0) {
                    // 전부 다 빈 영역인 경우 토스트 메시지 출력
                    // 최소 2개의 영역에는 값이 있어야 함
                    Toast.makeText(getApplicationContext(), "영역 1, 2의 텍스트를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if(EditText_vocaAdd_vocagroupArea1Input.getText().toString().isEmpty() ||
                        EditText_vocaAdd_vocagroupArea2Input.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "영역 1, 2의 텍스트를 입력해주세요.", Toast.LENGTH_SHORT).show();

                } else {
                    // 1개 이상의 영역에 값이 있는 경우 데이터 저장됨

                    // VocaShowItem 형식에 맞추어 단어 정보를 담은 ArrayList 객체 생성
                    long now = System.currentTimeMillis(); // 단어 학습 주기에 따른 단어 출력 날짜를 지정하기 위해 단어를 생성한 시간을 단어 데이터에 포함시킴
                    Date date = new Date(now);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm", new Locale("ko", "KR"));
                    String getTime = format.format(date);

                    ArrayList<VocaShowItem> voca = new ArrayList<>();
                    voca.add(new VocaShowItem(vocagroup.isVocagroupAreaSwitch1(), EditText_vocaAdd_vocagroupArea1Input.getText().toString(), (String)ImageView_vocaAdd_imageView1.getTag(), getTime, vocagroupName));
                    voca.add(new VocaShowItem(vocagroup.isVocagroupAreaSwitch2(), EditText_vocaAdd_vocagroupArea2Input.getText().toString(), (String)ImageView_vocaAdd_imageView2.getTag(), Integer.toString(0), null));
                    for(int i = 0; i < vocaAreaAdapter.getItemCount(); i++){
                        voca.add(new VocaShowItem(vocaAreaAdapter.getItemSide(i), vocaAreaAdapter.getItemText(i), vocaAreaAdapter.getImageTag(i), null, null));
                    }

                    String vocaListKey = vocagroupName + " vocaList"; // sharedPreferences에 저장하기 위해 고유한 키값 만들기

                    // 새로 생성한 단어 정보를 기존에 다른 단어들이 포함된 ArrayList에 추가함
                    ArrayList<ArrayList<VocaShowItem>> vocaList = new ArrayList<>();
                    SharedPreferences sharedPreferences = getSharedPreferences(userID, MODE_PRIVATE);
                    Gson gson = new Gson();
                    String vocaListJson = sharedPreferences.getString(vocaListKey, null);
                    Type vocaListType = new TypeToken<ArrayList<ArrayList<VocaShowItem>>>(){}.getType();
                    if(gson.fromJson(vocaListJson, vocaListType) != null){
                        vocaList = gson.fromJson(vocaListJson, vocaListType);
                    }

                    vocaList.add(voca);

                    // 단어 추가 후, 바뀐 단어 목록 데이터를 다시 저장함
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    // '저장할_객체'를 '저장할_객체_클래스명'의 형식에 맞추어 String으로 변환함
                    String vocaListJsonSave = gson.toJson(vocaList);
                    // '쉐어드이름' xml 파일 안에 'String_식별값'이라는 이름으로 '스트링_변수명'의 내용을 저장함
                    editor.putString(vocaListKey, vocaListJsonSave);
                    editor.commit();

                    EditText_vocaAdd_vocagroupArea1Input.setText("");
                    ImageView_vocaAdd_imageView1.setImageBitmap(null);
                    ImageView_vocaAdd_imageView1.setTag(null);
                    EditText_vocaAdd_vocagroupArea2Input.setText("");
                    ImageView_vocaAdd_imageView2.setImageBitmap(null);
                    ImageView_vocaAdd_imageView2.setTag(null);
                    for(int i = 0; i < vocaAreaAdapter.getItemCount(); i++){
                        vocaAreaAdapter.setItemText(i);
                        vocaAreaAdapter.setBitmap(i, null);
                        vocaAreaAdapter.setImageTag(i, "");
                    }
                    Toast.makeText(getApplicationContext(), "단어 추가 완료",Toast.LENGTH_SHORT).show();


//                    Intent intentRefreshAction = new Intent(VocaAdd.this, VocaShow.class);
//                    intentRefreshAction.putExtra("intentRefreshAction", 10);
//
//                    startActivity(intentRefreshAction);
                }
            }
        });
    }



    private void permissionCheck(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                Log.d("권한 설정", "완료");
            } else {
                Log.d("권한 설정", "요청");
                ActivityCompat.requestPermissions(VocaAdd.this,
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
//            ImageView_vocaAdd_imageView1.setImageBitmap(imageBitmap);

            File file = new File(mCurrentPhotoPath);
            Bitmap bitmap;
            if(Build.VERSION.SDK_INT >= 29){
                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), Uri.fromFile(file));
                try{
                  bitmap = ImageDecoder.decodeBitmap(source);
                  if(bitmap != null) {
                      Drawable drawable = new BitmapDrawable(getResources(), bitmap);

                      String imagePath = mCurrentPhotoPath;
                      ImageView_vocaAdd_imageView1.setTag(imagePath);
                      Log.d("area1 imagePath", imagePath.toString());
                      ImageView_vocaAdd_imageView1.setImageBitmap(bitmap);
                  }
                }catch(IOException e){
                }
            } else {
                try{
                  bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
                  if(bitmap != null){
                      Drawable drawable = new BitmapDrawable(getResources(), bitmap);

                      String imagePath = mCurrentPhotoPath;
                      ImageView_vocaAdd_imageView1.setTag(imagePath);
                      Log.d("area1 imagePath", imagePath.toString());
                      ImageView_vocaAdd_imageView1.setImageBitmap(bitmap);
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
                ImageView_vocaAdd_imageView1.setTag(imagePath);
                Log.d("area1 imagePath", imagePath.toString());
                ImageView_vocaAdd_imageView1.setImageBitmap(img);
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
//            ImageView_vocaAdd_imageView2.setImageBitmap(imageBitmap);
            File file = new File(mCurrentPhotoPath);
            Bitmap bitmap;
            if(Build.VERSION.SDK_INT >= 29){
                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), Uri.fromFile(file));
                try{
                    bitmap = ImageDecoder.decodeBitmap(source);
                    if(bitmap != null) {
                        Drawable drawable = new BitmapDrawable(getResources(), bitmap);

                        String imagePath = mCurrentPhotoPath;
                        ImageView_vocaAdd_imageView2.setTag(imagePath);
                        Log.d("area2 imagePath", imagePath);
                        ImageView_vocaAdd_imageView2.setImageBitmap(bitmap);
                    }
                }catch(IOException e){
                }
            } else {
                try{
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
                    if(bitmap != null){
                        Drawable drawable = new BitmapDrawable(getResources(), bitmap);

                        String imagePath = mCurrentPhotoPath;
                        ImageView_vocaAdd_imageView2.setTag(imagePath);
                        Log.d("area2 imagePath", imagePath);
                        ImageView_vocaAdd_imageView2.setImageBitmap(bitmap);
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
                ImageView_vocaAdd_imageView2.setTag(imagePath);
                Log.d("area2 imagePath", imagePath);
                ImageView_vocaAdd_imageView2.setImageBitmap(img);
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
                        Log.d("추가 영역 사진 불러오기", "카메라 "+drawable);

                        String imagePath = mCurrentPhotoPath;
                        vocaAreaAdapter.setImageTag(position, imagePath);
                        Log.d("areaItem imagePath", imagePath);
                        vocaAreaAdapter.setBitmap(position, bitmap);
                    }
                }catch(IOException e){
                }
            } else {
                try{
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
                    if(bitmap != null){
                        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                        Log.d("추가 영역 사진 불러오기", "카메라 "+drawable);

                        String imagePath = mCurrentPhotoPath;
                        vocaAreaAdapter.setImageTag(position, imagePath);
                        Log.d("areaItem imagePath", imagePath);
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

                FileOutputStream out = new FileOutputStream(mCurrentPhotoPath);
                img.compress(Bitmap.CompressFormat.PNG, 70, out);
                out.close();

                Drawable drawable = new BitmapDrawable(getResources(), img);
//                Log.d("추가 영역 사진 불러오기", "갤러리 "+drawable);

                String imagePath = mCurrentPhotoPath;
                vocaAreaAdapter.setImageTag(position, imagePath);
                Log.d("areaItem imagePath", imagePath);
                vocaAreaAdapter.setBitmap(position, img);
            }catch(Exception e){
            }
        } else if(resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_SHORT).show();
        }
    }




    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PNG_"+timeStamp+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".png", storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent(int requestCode){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        System.out.println("카메라 인텐트 작동 여부 확인");
//        Log.d(TAG, ""+takePictureIntent.resolveActivity(getPackageManager()));
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;

            try{
                photoFile = createImageFile();
            }catch(IOException e){
            }
            if (photoFile != null){
                Uri photoURI = FileProvider.getUriForFile(this, "com.seahahn.vocacyclicreview.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, requestCode);
            }
//        }
    }

    private void galleryPhoto(int requestCode){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        System.out.println("갤러리 소환");

//        if (intent.resolveActivity(getPackageManager()) != null) {
//            File photoFile = null;
//
//            try{
//                photoFile = createImageFile();
//            }catch(IOException e){
//            }
//            if (photoFile != null){
//                Uri photoURI = FileProvider.getUriForFile(this, "com.example.vocacyclicreview.fileprovider", photoFile);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(intent, requestCode);
//            }
//        }

        startActivityForResult(intent, requestCode);
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("area1", EditText_vocaAdd_vocagroupArea1Input.getText().toString()); // 상태를 저장하는 코드. "데이터 이름"은 상수로 선언해주면 좋음
        outState.putString("area2", EditText_vocaAdd_vocagroupArea2Input.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

}