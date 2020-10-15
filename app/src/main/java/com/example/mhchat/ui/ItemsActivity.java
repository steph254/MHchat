package com.example.mhchat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import com.example.mhchat.ThoughtsAdapter;
import com.example.mhchat.models.Thoughts;
import com.example.mhchat.R;


public class ItemsActivity extends AppCompatActivity implements ThoughtsAdapter.OnItemClickListener{

    private RecyclerView mRecyclerView;
    private ThoughtsAdapter mAdapter;
    private ProgressBar mProgressBar;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Thoughts> mThoughts;

    private void openDetailActivity(String[] data){
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("USERNAME_KEY",data[0]);
        intent.putExtra("THOUGHT_KEY",data[1]);
        intent.putExtra("IMAGE_KEY",data[2]);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_items );

        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        mProgressBar = findViewById(R.id.myDataLoaderProgressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        mThoughts = new ArrayList<> ();
        mAdapter = new ThoughtsAdapter (ItemsActivity.this, mThoughts);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(ItemsActivity.this);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("thoughts_uploads");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mThoughts.clear();

                for (DataSnapshot thoughtSnapshot : dataSnapshot.getChildren()) {
                    Thoughts upload = thoughtSnapshot.getValue(Thoughts.class);
                    upload.setKey(thoughtSnapshot.getKey());
                    mThoughts.add(upload);
                }
                mAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ItemsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });

    }
    public void onItemClick(int position) {
        Thoughts clickedThought = mThoughts.get(position);
        String[] thoughtData = {clickedThought.getUserName(),clickedThought.getThought(),clickedThought.getImageUrl()};
        openDetailActivity(thoughtData);
    }

    @Override
    public void onShowItemClick(int position) {
        Thoughts clickedThought = mThoughts.get(position);
        String[] thoughtData = {clickedThought.getUserName(),clickedThought.getThought(),clickedThought.getImageUrl()};
        openDetailActivity(thoughtData);
    }

    @Override
    public void onDeleteItemClick(int position) {
        Thoughts selectedItem = mThoughts.get(position);
        final String selectedKey = selectedItem.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void> () {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(ItemsActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });

    }
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

}

