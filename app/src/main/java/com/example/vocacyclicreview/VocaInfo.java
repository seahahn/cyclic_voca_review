package com.example.vocacyclicreview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.vocacyclicreview.R;

public class VocaInfo extends AppCompatActivity {

    ImageButton ImageButton_vocaInfo_goback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca_info);

        ImageButton_vocaInfo_goback = findViewById(R.id.ImageButton_vocaInfo_goback);

        ImageButton_vocaInfo_goback.setClickable(true);
        ImageButton_vocaInfo_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(VocaInfo.this, VocaFront.class);
//                startActivity(intent);
                finish();
            }
        });
    }
}