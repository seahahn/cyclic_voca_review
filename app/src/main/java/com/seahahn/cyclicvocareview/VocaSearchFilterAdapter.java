package com.seahahn.cyclicvocareview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.seahahn.cyclicvocareview.MainActivity.userID;

public class VocaSearchFilterAdapter extends RecyclerView.Adapter<VocaSearchFilterAdapter.VocaSearchFilterViewHolder> {

    private static final String TAG = "VocaSearchFilterAdapter";
    private Context context;
    private ArrayList<String> mData;

    String vocagroupName;
    ArrayList<ArrayList<VocaShowItem>> vocaList = new ArrayList<>();

    public VocaSearchFilterAdapter(Context context, ArrayList<String> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public VocaSearchFilterAdapter.VocaSearchFilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_listview, parent, false);
        return new VocaSearchFilterAdapter.VocaSearchFilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VocaSearchFilterAdapter.VocaSearchFilterViewHolder holder, int position) {
        holder.onBind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class VocaSearchFilterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView TextView_vocaSearch;

        public VocaSearchFilterViewHolder(@NonNull View itemView) {
            super(itemView);
            TextView_vocaSearch = itemView.findViewById(R.id.TextView_vocaSearch);
        }

        public void onBind(String string){
            if(string.equals(context.getResources().getString(R.string.searchFilterVocaCycleListWrong))
                    || string.equals(context.getResources().getString(R.string.searchFilterVocaCycleListNew))
                    || string.equals(context.getResources().getString(R.string.searchFilterVocaCycleListReview))) {
                TextView_vocaSearch.setText("단어 구분 : " + string);
                TextView_vocaSearch.setTag(string);
            } else if(string.equals(context.getResources().getString(R.string.searchFilterNone))){
                TextView_vocaSearch.setText(string);
                TextView_vocaSearch.setTag(string);
            } else {
                TextView_vocaSearch.setText("단어장 : "+string);
                TextView_vocaSearch.setTag(string);
            }
            TextView_vocaSearch.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v == TextView_vocaSearch){
                String vocaSearchFilter = TextView_vocaSearch.getTag().toString();
                SharedPreferences sharedPreferences = context.getSharedPreferences(userID, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("VocaSearchFilter", vocaSearchFilter); // 저장할 값 입력하기
                editor.commit();

                Intent mIntent = new Intent(context.getApplicationContext(), VocaSearch.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(mIntent);
            }
        }
    }
}
