package com.seahahn.cyclicvocareview;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;

public class Help extends AppCompatActivity {

    ImageButton ImageButton_help_goback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // 뒤로가기 버튼
        ImageButton_help_goback = findViewById(R.id.ImageButton_help_goback);
        ImageButton_help_goback.setClickable(true);
        ImageButton_help_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Help.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ViewPager viewPager = findViewById(R.id.ViewPager_help);
        HelpAdapter adapter = new HelpAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }
}