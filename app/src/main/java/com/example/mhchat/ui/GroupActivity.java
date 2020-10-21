package com.example.mhchat.ui;

import android.content.Intent;
import android.os.Bundle;

import com.example.mhchat.R;
import com.example.mhchat.GroupsAdapter;
import com.example.mhchat.models.Group;
import com.example.mhchat.models.Thoughts;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity implements GroupsAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private GroupsAdapter mAdapter;
    private ProgressBar mProgressBar;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Group> mGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        mProgressBar = findViewById(R.id.myDataLoaderProgressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        mGroups = new ArrayList<>();
        mAdapter = new GroupsAdapter(GroupActivity.this, mGroups);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(GroupActivity.this);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("groups");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mGroups.clear();

                for (DataSnapshot thoughtSnapshot : dataSnapshot.getChildren()) {
                    Group upload = thoughtSnapshot.getValue(Group.class);
                    upload.setKey(thoughtSnapshot.getKey());
                    mGroups.add(upload);
                }
                mAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(GroupActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
        findViewById(R.id.fab_new_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GroupActivity.this, NewGroupActivity.class));
            }
        });
    }

    private void openGroupInfoActivity(String[] data){
        Intent intent = new Intent(this, GroupInfoActivity.class);
        intent.putExtra("GROUPNAME_KEY",data[0]);
        intent.putExtra("IMAGE_KEY",data[1]);
        startActivity(intent);
    }

    public void onItemClick(int position) {
        Group clickedGroup = mGroups.get(position);
        String[] groupData = {clickedGroup.getGroupName(),clickedGroup.getImageUrl()};
        openGroupInfoActivity(groupData);
    }

    @Override
    public void onShowItemClick(int position) {
        Group clickedGroup = mGroups.get(position);
        String[] groupData = {clickedGroup.getGroupName(),clickedGroup.getImageUrl()};
        openGroupInfoActivity(groupData);
    }

    @Override
    public void onDeleteItemClick(int position) {
        Group selectedItem = mGroups.get(position);
        final String selectedKey = selectedItem.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(GroupActivity.this, "Group deleted", Toast.LENGTH_SHORT).show();
            }
        });

    }

    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_chat:
                startActivity(new Intent(this, ChatActivity.class));
                return true;
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, SignInActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}