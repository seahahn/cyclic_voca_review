package com.example.vocacyclicreview.vocagroup;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Environment;
import android.widget.Toast;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vocacyclicreview.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class VocagroupAdapter extends RecyclerView.Adapter<VocagroupAdapter.VocagroupViewHolder> implements ItemTouchHelperListener {

    private static final int REQUEST_VOCAGROUP_MODIFY = 2;
    private static final int REQUEST_VOCA_UPLOAD = 3;

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
        public void onClick(View v) {
            if(v == vocagroup_name){
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HH:mm");

                Intent intent = new Intent(context, VocaShow.class);
//                intent.putExtra("vocagroupName", mData.get(getAdapterPosition()).getVocagroupName());
//                intent.putExtra("단어장 포지션", getAdapterPosition());

                String vocagroupName = mData.get(getAdapterPosition()).getVocagroupName();
                SharedPreferences sharedPreferences = context.getSharedPreferences("Vocagroup", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String vocagroupJson = gson.toJson(mData.get(getAdapterPosition()));
                editor.putString("vocagroupName", vocagroupName);
                editor.putInt("단어장 포지션", getAdapterPosition());
                editor.putString(vocagroupName, vocagroupJson); // 저장할 값 입력하기
                editor.commit();

                context.startActivity(intent);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(v == vocagroup_name){
                System.out.println("되니?");

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

//                        // 엑셀 파일 이용한 대량의 단어 데이터 추가 시작
//                        // 선택한 단어장의 정보 저장
////                String vocagroupName = mData.get(getAdapterPosition()).getVocagroupName();
                        SharedPreferences sharedPreferences = context.getSharedPreferences("Vocagroup", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        Gson gson = new Gson();
//                        String vocagroupJson = gson.toJson(mData.get(getAdapterPosition()));
                editor.putInt("단어장 포지션", getAdapterPosition());
                editor.commit();
////                editor.putString(vocagroupName, vocagroupJson); // 저장할 값 입력하기
////                editor.commit();
////
////                vocagroupName = sharedPreferences.getString("vocagroupName", null);
////
////                String vocaListKey = vocagroupName + " vocaList";
//
//                        // 단어장 데이터 불러오기
//                        Vocagroup vocagroup = mData.get(getAdapterPosition());
//                        // 단어장의 추가 영역 데이터 불러오기
//                        vocaAreaAdapter = new VocaAreaAdapter(context, vocaArea);
//                        for(int i = 0; i < vocagroup.getVocagroupAreaList().size(); i++){
//                            vocaAreaAdapter.addItem(new VocaArea(vocagroup.getVocagroupAreaList().get(i).getEditText_vocagroupAdd_vocagroupAreaInput(), "",
//                                    vocagroup.getVocagroupAreaList().get(i).isSwitch_vocagroupAdd_areaSwitch(), null, null));
////                    vocaAreaAdapter.notifyItemInserted(i);
//                        }
//                        // ArrayList<VocaShowItem> voca 만들어서 영역 1, 2, 추가영역 각각에 voca.add(new VocaShowItem(...)) 한다
//                        long now = System.currentTimeMillis(); // 단어 학습 주기에 따른 단어 출력 날짜를 지정하기 위해 단어를 생성한 시간을 단어 데이터에 포함시킴
//                        Date date = new Date(now);
//                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm", new Locale("ko", "KR"));
//                        String getTime = format.format(date);
//
//                        // 내용 없는 단어 객체 만들기
//                        ArrayList<VocaShowItem> voca = new ArrayList<>();
//                        voca.add(new VocaShowItem(vocagroup.isVocagroupAreaSwitch1(), "", null, getTime));
//                        voca.add(new VocaShowItem(vocagroup.isVocagroupAreaSwitch2(), "", null, Integer.toString(0)));
//                        for(int i = 0; i < vocaAreaAdapter.getItemCount(); i++){
//                            voca.add(new VocaShowItem(vocaAreaAdapter.getItemSide(i), "", null, null));
//                        }
//
//                        // ArrayList<ArrayList<VocaShowItem>> vocaList 만든 후, vocaList에 선택한 단어장에 담긴 단어 목록을 불러온다
//                        String vocaListKey = vocagroupJson + " vocaList";
//                        ArrayList<ArrayList<VocaShowItem>> vocaList = new ArrayList<>();
//                        String vocaListJson = sharedPreferences.getString(vocaListKey, null);
//                        Type vocaListType = new TypeToken<ArrayList<ArrayList<VocaShowItem>>>(){}.getType();
//                        if(gson.fromJson(vocaListJson, vocaListType) != null){
//                            vocaList = gson.fromJson(vocaListJson, vocaListType);
//                        }
//
//                        // 엑셀 파일 불러와서 추가하기
//                        try {
////                            AssetManager assetManager = context.getAssets();
////                            AssetFileDescriptor fileDescriptor = assetManager.openFd("test.xlsx");
//
////                            File file = new File(context.getFilesDir() + "/app/src/main/assets/test.xlsx");
////                            File file = new File();
//                            FileInputStream fileInputStream = new FileInputStream(context.getExternalFilesDir(DIRECTORY_DOWNLOADS) +"/test.xlsx");
////                            FileInputStream fileInputStream = context.getAssets().open("test.xlsx");
////                            FileInputStream file = fileDescriptor.createInputStream();
//                            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
//
//                            int rowindex = 0;
//                            int columnindex = 0;
//
//                            //시트 수 (첫번째에만 존재하므로 0을 준다)
//                            //만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다
//                            XSSFSheet sheet = workbook.getSheetAt(0);
//
//                            //행의 수
//                            int rows = sheet.getPhysicalNumberOfRows();
//                            for (rowindex = 1; rowindex < rows; rowindex++) {
//                                XSSFRow row = sheet.getRow(rowindex);
//                                ArrayList<String> vocaEachArea = new ArrayList<>();
//                                if (row != null) {
//                                    //셀의 수
//                                    int cells = row.getPhysicalNumberOfCells();
//                                    for (columnindex = 0; columnindex <= cells; columnindex++) {
//                                        //셀값을 읽는다
//                                        XSSFCell cell = row.getCell(columnindex);
//                                        String value = "";
//                                        //셀이 빈값일경우를 위한 널체크
//                                        if (cell == null) {
//                                            continue;
//                                        } else {
//                                            //타입별로 내용 읽기
//                                            switch (cell.getCellType()) {
//                                                case FORMULA:
//                                                    value = cell.getCellFormula();
//                                                    break;
//                                                case NUMERIC:
//                                                    value = cell.getNumericCellValue() + "";
//                                                    break;
//                                                case STRING:
//                                                    value = cell.getStringCellValue() + "";
//                                                    break;
//                                                case BLANK:
//                                                    value = cell.getBooleanCellValue() + "";
//                                                    break;
//                                                case ERROR:
//                                                    value = cell.getErrorCellValue() + "";
//                                                    break;
//                                            }
//                                        }
//                                        vocaEachArea.add(value);
//                                    }
//                                    // 내용 없는 단어 객체에 내용 채워넣기
//                                    voca.set(0, new VocaShowItem(vocagroup.isVocagroupAreaSwitch1(), vocaEachArea.get(0), null, getTime));
//                                    voca.set(1, new VocaShowItem(vocagroup.isVocagroupAreaSwitch2(), vocaEachArea.get(1), null, Integer.toString(0)));
//                                    for(int i = 0; i < vocaAreaAdapter.getItemCount(); i++){
//                                        voca.set(i+2, new VocaShowItem(vocaAreaAdapter.getItemSide(i), vocaEachArea.get(i+2), null, null));
//                                    }
//                                    vocaList.add(voca);
//                                }
//                            }
//                        } catch(Exception e) {
//                            e.printStackTrace();
//                        }
//
//                        // 바뀐 vocaList를 다시 같은 SF Key에 저장한다
//                        String vocaListJsonSave = gson.toJson(vocaList);
//                        editor.putString(vocaListKey, vocaListJsonSave);
//                        editor.commit();
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

        SharedPreferences sharedPreferences = context.getSharedPreferences("Vocagroup", MODE_PRIVATE);
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
        SharedPreferences sharedPreferencesM = context.getSharedPreferences("VocaLearningCycle", MODE_PRIVATE);
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
            Intent intentModify = new Intent(context, VocagroupModify.class);
            intentModify.putExtra("vocagroupName", mData.get(position).getVocagroupName());
//        intentModify.putExtra("단어장 제목", mData.get(position).getVocagroupName());
//        intentModify.putExtra("단어장 학습 주기", mData.get(position).getVocaLearningCycle());
//        intentModify.putExtra("단어장 영역1", mData.get(position).getVocagroupArea1());
//        intentModify.putExtra("단어장 영역2", mData.get(position).getVocagroupArea2());
//        intentModify.putExtra("단어장 영역1 스위치", mData.get(position).isVocagroupAreaSwitch1());
//        intentModify.putExtra("단어장 영역2 스위치", mData.get(position).isVocagroupAreaSwitch2());
//        intentModify.putExtra("단어장 영역 리스트", mData.get(position).getVocagroupAreaList());
            intentModify.putExtra("단어장 포지션", position);

            String vocagroupName = mData.get(position).getVocagroupName();
            SharedPreferences sharedPreferences = context.getSharedPreferences("Vocagroup", MODE_PRIVATE);
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
        // 단어장을 메인에서 삭제하기 전에 단어장에 저장되어 있던 단어 데이터를 삭제시킨다
        String vocaListKey = mData.get(position).getVocagroupName() + " vocaList";
        SharedPreferences sharedPreferences = context.getSharedPreferences("Vocagroup", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
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
