package com.seahahn.cyclicvocareview.vocagroup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.seahahn.cyclicvocareview.MainActivity;
import com.seahahn.cyclicvocareview.R;

public class VocagroupInfo extends AppCompatActivity {

    ImageButton ImageButton_vocagroupInfo_goback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocagroup_info);

        ImageButton_vocagroupInfo_goback = findViewById(R.id.ImageButton_vocagroupInfo_goback);

        ImageButton_vocagroupInfo_goback.setClickable(true);
        ImageButton_vocagroupInfo_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VocagroupInfo.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}