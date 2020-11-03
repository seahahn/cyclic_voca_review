package com.seahahn.cyclicvocareview;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class VocaAreaAdapter extends RecyclerView.Adapter<VocaAreaAdapter.VocaAreaViewHolder> {

    // VocaAdd(단어 추가), VocaModify(단어 수정) 액티비티에서 사용되는 어댑터
    // Vocagroup(단어장)을 추가할 때 Vocagroup에 포함되어 있는 VocagroupArea(단어장 추가 영역)의 데이터를 출력하기 위해서 있음

    private static final int REQUEST_IMAGE_CAPTURE_ITEM = 3;
    private static final int REQUEST_PICTURE_ITEM = 6;
    String mCurrentPhotoPathInAdapter;

    Context context;
    private ArrayList<VocaArea> mData;

    public VocaAreaAdapter(Context context, ArrayList<VocaArea> mData) {
        this.context = context;
        this.mData = mData;
    }

    public class VocaAreaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView TextView_vocaAdd_vocaArea;
        EditText EditText_vocaAdd_vocaAreaInput;
        Switch Switch_vocaAdd_areaSwitch;
        ImageButton ImageButton_vocaAdd_gallery;
        ImageView ImageView_vocaAdd_imageView;

        MyCustomEditTextListener myCustomEditTextListener;
        MyOnCheckedChanged myOnCheckedChanged;
//        MyImageSetter myImageSetter;

        public VocaAreaViewHolder(@NonNull View itemView, MyCustomEditTextListener myCustomEditTextListener,
                                  MyOnCheckedChanged myOnCheckedChanged) {
            super(itemView);
            TextView_vocaAdd_vocaArea = itemView.findViewById(R.id.TextView_vocaAdd_vocaArea);
            EditText_vocaAdd_vocaAreaInput = itemView.findViewById(R.id.EditText_vocaAdd_vocaAreaInput);
            Switch_vocaAdd_areaSwitch = itemView.findViewById(R.id.Switch_vocaAdd_areaSwitch);
            ImageButton_vocaAdd_gallery = itemView.findViewById(R.id.ImageButton_vocaAdd_gallery);
            ImageView_vocaAdd_imageView = itemView.findViewById(R.id.ImageView_vocaAdd_imageView);

            this.myCustomEditTextListener = myCustomEditTextListener;
            this.EditText_vocaAdd_vocaAreaInput.addTextChangedListener(myCustomEditTextListener);

            this.myOnCheckedChanged = myOnCheckedChanged;
            this.Switch_vocaAdd_areaSwitch.setOnCheckedChangeListener(myOnCheckedChanged);

//            this.myImageSetter = myImageSetter;
//            this.ImageView_vocaAdd_imageView.setImageBitmap(myImageSetter);
        }

        public void onBind(VocaArea vocaArea){
            TextView_vocaAdd_vocaArea.setText(vocaArea.getVocaAreaName());
            ImageButton_vocaAdd_gallery.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // 이미지버튼 눌러서 사진 불러오기 - 카메라 or 갤러리 호출
            if(v == ImageButton_vocaAdd_gallery){

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("이미지 가져오기");
                builder.setItems(R.array.takePhoto, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // 카메라 불러오는 경우
                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
                                    File photoFile = null;

                                    try{
                                        photoFile = createImageFile();
                                    }catch(IOException e){
                                    }
                                    if (photoFile != null){
                                        Uri photoURI = FileProvider.getUriForFile(context.getApplicationContext(), "com.seahahn.vocacyclicreview.fileprovider", photoFile);
                                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                                        // 인텐트로 전달하면 onActivityResult에서 null 되므로 아예 값 저장한 후에 onActivityResult에서 불러올 것임
                                        SharedPreferences sharedPreferences = context.getSharedPreferences("photo", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("mCurrentPhotoPathInAdapter", mCurrentPhotoPathInAdapter);
                                        editor.putInt("photoPosition", getAdapterPosition());
                                        editor.commit();

                                        Log.d("mCurrentPhotoPathInAdapter", mCurrentPhotoPathInAdapter);
                                        ((Activity)context).startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_ITEM);
                                    }
                                }
                                break;
                            case 1:
                                // 갤러리 불러오는 경우
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);

                                if (intent.resolveActivity(context.getPackageManager()) != null) {
                                    File photoFile = null;

                                    try{
                                        photoFile = createImageFile();
                                    }catch(IOException e){
                                    }
                                    if (photoFile != null){
                                        Uri photoURI = FileProvider.getUriForFile(context, "com.seahahn.vocacyclicreview.fileprovider", photoFile);
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                                        SharedPreferences sharedPreferences = context.getSharedPreferences("photo", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putInt("photoPosition", getAdapterPosition());
                                        editor.commit();

                                        ((Activity)context).startActivityForResult(intent, REQUEST_PICTURE_ITEM);
                                    }
                                }
//                                ((Activity)context).startActivityForResult(intent, REQUEST_PICTURE_ITEM);
                                break;
                            case 2: // 사진 제거
                                ImageView_vocaAdd_imageView.setImageBitmap(null);
                                ImageView_vocaAdd_imageView.setTag(null);
                            default:
                                break;
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        }
    }

    @Override
    public VocaAreaAdapter.VocaAreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voca_area, parent, false);
        VocaAreaViewHolder vocaAreaViewHolder = new VocaAreaViewHolder(view, new MyCustomEditTextListener(), new MyOnCheckedChanged());

        return vocaAreaViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VocaAreaAdapter.VocaAreaViewHolder holder, int position) {
        holder.onBind(mData.get(position));

        holder.myCustomEditTextListener.updatePosition(holder.getAdapterPosition());
        holder.EditText_vocaAdd_vocaAreaInput.setText(mData.get(holder.getAdapterPosition()).getEditText_vocaAdd_vocaAreaInput());

        holder.myOnCheckedChanged.updatePosition(holder.getAdapterPosition());
        holder.Switch_vocaAdd_areaSwitch.setChecked(mData.get(holder.getAdapterPosition()).isSwitch_vocaAdd_areaSwitch());


        holder.ImageView_vocaAdd_imageView.setImageBitmap(mData.get(holder.getAdapterPosition()).getImageView_vocaAdd_imageView());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addItem(VocaArea vocaArea){
        mData.add(vocaArea);
        notifyDataSetChanged();
    }

    public void setItem(int position, ArrayList<VocaArea> mData, String text, String imagePath){
        mData.get(position).setEditText_vocaAdd_vocaAreaInput(text);
        mData.get(position).setImageView_vocaAdd_imageView(BitmapFactory.decodeFile(imagePath));
        mData.get(position).setImageView_vocaAdd_photoPath(imagePath);
        notifyDataSetChanged();
    }

    public boolean getItemSide(int position){
        return mData.get(position).isSwitch_vocaAdd_areaSwitch();
    }

    public String getItemText(int position){
        return mData.get(position).getEditText_vocaAdd_vocaAreaInput();
    }

    public void setItemText(int position){
        mData.get(position).setEditText_vocaAdd_vocaAreaInput("");
    }

//    public String getItemImage(int position){
//        mData.get(position).getImageView_vocaAdd_imageView()
//        return BitmapToString(mData.get(position).getImageView_vocaAdd_imageView());
//        return mData.get(position).getImageView_vocaAdd_imageView();
//    }

//    public String getItemDrawableToString(int position){
//        return DrawableToString(mData.get(position).getImageView_vocaAdd_imageView());
//    }

    public int isVocaAreaEmpty(){
        // 추가 영역에 포함된 EditText에 값이 있는지 없는지 판별하기 위한 메소드
        // 값이 있으면 emptyCheck가 1 올라감
        boolean isEmpty;
        int emptyCheck = 0;

        for(int i = 0; i < mData.size(); i++){
            isEmpty = mData.get(i).getEditText_vocaAdd_vocaAreaInput().isEmpty();
            if(!isEmpty){
                emptyCheck++;
            }
        }
        return emptyCheck;
    }

    public void setBitmap(int position, Bitmap bitmap){
        mData.get(position).setImageView_vocaAdd_imageView(bitmap);
        notifyItemChanged(position);
    }

    public void setDrawable(int position, Drawable drawable){
        Log.d("추가 영역 사진 불러오기", "setDrawable 호출됨");
//        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
//        Drawable drawableForSetting = new BitmapDrawable(context.getResources(), bitmap);

//        mData.get(position).setImageView_vocaAdd_imageView(drawable);
        notifyItemChanged(position);
    }

    public void setImageTag(int position, String tag){
        mData.get(position).setImageView_vocaAdd_photoPath(tag);
    }

    public String getImageTag(int position){
        String photoPath = "";
        if(mData.get(position).getImageView_vocaAdd_photoPath() != null){
            photoPath = mData.get(position).getImageView_vocaAdd_photoPath();
        }
        return photoPath;
    }

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PNG_"+timeStamp+"_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".png", storageDir);

        mCurrentPhotoPathInAdapter = image.getAbsolutePath();
        return image;
    }

    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            mData.get(position).setEditText_vocaAdd_vocaAreaInput(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }

    private class MyOnCheckedChanged implements CompoundButton.OnCheckedChangeListener{

        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                mData.get(position).setSwitch_vocaAdd_areaSwitch(isChecked);
            } else {
                mData.get(position).setSwitch_vocaAdd_areaSwitch(isChecked);
            }
        }
    }

//    private class MyImageSetter implements ImageSetter{
//        private int position;
//
//        public void updatePosition(int position) {
//            this.position = position;
//        }
//
//        public void ImageSetter(Bitmap bitmap){
//            mData.get(position).setImageView_vocaAdd_imageView(bitmap);
//        }
//    }
//
//    public interface ImageSetter {
//        public void ImageSetter(Bitmap bitmap);
//    }
}