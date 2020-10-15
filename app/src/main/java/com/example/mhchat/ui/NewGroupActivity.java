package com.example.mhchat.ui;

import android.os.Bundle;

import com.example.mhchat.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mhchat.models.Group;
import com.example.mhchat.models.User;

import android.app.ProgressDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class NewGroupActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;
    private DatabaseReference mDatabase;
    private EditText groupname;
    private FloatingActionButton mSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        groupname = findViewById(R.id.editGroupName);
        mSubmitButton = findViewById(R.id.fab_submit_post);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterGroup();
            }
        });
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    private boolean validateForm(String title, String body) {
        if (TextUtils.isEmpty(title)) {
            groupname.setError(getString(R.string.required));
            return false;
        } else {
            groupname.setError(null);
            return true;
        }
    }

    private void enterGroup() {
        final String groupName = groupname.getText().toString().trim();
        final String userId = getUid();


    }

    private void setEditingEnabled(boolean enabled) {
        groupname.setEnabled(enabled);
        if (enabled) {
            mSubmitButton.setVisibility(View.VISIBLE);
        } else {
            mSubmitButton.setVisibility(View.GONE);
        }
    }

    private void createNewGroup(String groupName) {

        String key = mDatabase.child("groups").push().getKey();


    }
}