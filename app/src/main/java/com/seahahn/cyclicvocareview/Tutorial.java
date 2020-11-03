package com.seahahn.cyclicvocareview;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class Tutorial extends AppCompatActivity {

    ImageButton ImageButton_tutorial_goback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        ImageButton_tutorial_goback = findViewById(R.id.ImageButton_tutorial_goback);

        ImageButton_tutorial_goback.setClickable(true);
        ImageButton_tutorial_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tutorial.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}