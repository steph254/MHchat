package com.example.mhchat.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mhchat.R;
import com.squareup.picasso.Picasso;

public class GroupInfoActivity extends AppCompatActivity {

    TextView groupnameTextView;
    ImageView icongroupImageView;

    private void initializeWidgets(){
        groupnameTextView = findViewById(R.id.groupnameTextView);
        icongroupImageView=findViewById(R.id.thoughtImageView);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        initializeWidgets();


        Intent i=this.getIntent();
        String groupname = i.getExtras().getString("GROUPNAME_KEY");
        String imageURL = i.getExtras().getString("IMAGE_KEY");


        groupnameTextView.setText(groupname);
        Picasso.get()
                .load(imageURL)
                .placeholder(R.drawable.placeholder)
                .fit()
                .centerCrop()
                .into(icongroupImageView);
    }
}