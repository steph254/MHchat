package com.example.mhchat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import com.example.mhchat.R;

public class DetailsActivity extends AppCompatActivity {

    TextView usernameTextView, thoughtTextView;
    ImageView thoughtImageView;

    private void initializeWidgets(){
        usernameTextView = findViewById(R.id.usernameTextView);
        thoughtTextView = findViewById(R.id.thoughtTextView);
        thoughtImageView=findViewById(R.id.thoughtImageView);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initializeWidgets();


        Intent i=this.getIntent();
        String username = i.getExtras().getString("USERNAME_KEY");
        String thought = i.getExtras().getString("THOUGHT_KEY");
        String imageURL = i.getExtras().getString("IMAGE_KEY");


        usernameTextView.setText(username);
        thoughtTextView.setText(thought);
        Picasso.get()
                .load(imageURL)
                .placeholder(R.drawable.placeholder)
                .fit()
                .centerCrop()
                .into(thoughtImageView);
    }

}
