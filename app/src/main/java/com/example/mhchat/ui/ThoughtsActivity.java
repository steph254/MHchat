package com.example.mhchat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mhchat.R;

public class ThoughtsActivity extends AppCompatActivity {

    private Button viewAll, addNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_thoughts );

        viewAll = findViewById ( R.id.viewAll );
        addNew = findViewById ( R.id.addNew );

        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ThoughtsActivity.this, ItemsActivity.class);
                startActivity(i);
            }
        });
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ThoughtsActivity.this, UploadActivity.class);
                startActivity(i);
            }
        });

    }


}
