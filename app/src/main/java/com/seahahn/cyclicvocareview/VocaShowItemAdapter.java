package com.seahahn.cyclicvocareview;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VocaShowItemAdapter extends RecyclerView.Adapter<VocaShowItemAdapter.VocaShowItemViewHolder> implements VocaShowItemLongClickListener {

    Context context;
    private List<VocaShowItem> mData;

    private VocaShowItemClickInterface listener;
    private VocaShowItemLongClickListener longClickListener;

    public VocaShowItemAdapter(Context context, List<VocaShowItem> mData, VocaShowItemClickInterface listener) {
        this.context = context;
        this.mData = mData;
        this.listener = listener;
    }

    public void setOnItemLongClickListener(VocaShowItemLongClickListener longClickListener){
        this.longClickListener = longClickListener;
    }

    @Override
    public void onItemLongClick(VocaShowItemViewHolder holder, View view, int position) {
        if(longClickListener != null){
            longClickListener.onItemLongClick(holder, view, position);
        }
    }

    public interface VocaShowItemClickInterface {
        // VocaShow 액티비티에 앞/뒤(false/true) 값 전달을 위한 인터페이스
        // 리사이클러뷰의 아이템을 누르면 앞/뒤가 바뀌도록 해줌
        void sideChange();
    }

    public class VocaShowItemViewHolder extends RecyclerView.ViewHolder{
        TextView TextView_vocaShow_item;
        ImageView ImageView_vocaShow_item;

        public VocaShowItemViewHolder(@NonNull View itemView) {
            super(itemView);
            TextView_vocaShow_item = itemView.findViewById(R.id.TextView_vocaShow_item);
            ImageView_vocaShow_item = itemView.findViewById(R.id.ImageView_vocaShow_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    sideChange();
                    listener.sideChange();
                }
            });
        }

        public void onBind(VocaShowItem vocaShowItem){
            TextView_vocaShow_item.setText(vocaShowItem.getVocaShowText());
            if (vocaShowItem.getImage() != null) {
                ImageView_vocaShow_item.setImageBitmap(BitmapFactory.decodeFile(vocaShowItem.getImage()));
            }
            if(!mData.get(getAdapterPosition()).isShowingSide()){
                TextView_vocaShow_item.setVisibility(View.VISIBLE);
                ImageView_vocaShow_item.setVisibility(View.VISIBLE);
            } else {
                TextView_vocaShow_item.setVisibility(View.GONE);
                ImageView_vocaShow_item.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public VocaShowItemAdapter.VocaShowItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voca_show, parent, false);
        return new VocaShowItemAdapter.VocaShowItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VocaShowItemAdapter.VocaShowItemViewHolder holder, int position) {
        holder.onBind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
//        return (null != mData ? mData.size() : 0);
    }

    public List<VocaShowItem> getData(){
        return mData;
    }

    public VocaShowItem getItem(int position){
        return mData.get(position);
    }

}
