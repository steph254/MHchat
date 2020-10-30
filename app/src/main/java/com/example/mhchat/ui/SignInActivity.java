package com.example.mhchat.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.app.ProgressDialog;

import com.example.mhchat.R;
import com.example.mhchat.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
	private EditText mEmailField, mPasswordField;
	private FirebaseAuth mAuth;
	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);

		mEmailField = findViewById(R.id.field_email);
		mPasswordField = findViewById(R.id.field_password);
		findViewById(R.id.button_sign_in).setOnClickListener(this);
		findViewById(R.id.button_sign_up).setOnClickListener(this);


		mAuth = FirebaseAuth.getInstance();
	}

	@Override
	public void onStart() {
		super.onStart();
		if (mAuth.getCurrentUser() != null) {
			onAuthSuccess(mAuth.getCurrentUser());
		}
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

	private void onAuthSuccess(FirebaseUser firebaseUser) {
		String email = firebaseUser.getEmail();
		String username = email;
		if (email != null && email.contains("@")) {
			username = email.split("@")[0];
		}

		User user = new User(username, email);
		DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
		mDatabase.child("users").child(firebaseUser.getUid()).setValue(user);

		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
		
		finish();
	}

	private void signIn() {
		String email = mEmailField.getText().toString().trim();
		String password = mPasswordField.getText().toString().trim();

		if (validateForm(email, password)) {
			showProgressDialog();
			mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
				@Override
				public void onComplete(@NonNull Task<AuthResult> task) {
					hideProgressDialog();
					if (task.isSuccessful()) {
						onAuthSuccess(task.getResult().getUser());
					} else {
						Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
					}
				}
			});
		}
	}

	private void signUp() {
		String email = mEmailField.getText().toString().trim();
		String password = mPasswordField.getText().toString().trim();

		if (validateForm(email, password)) {
			showProgressDialog();
			mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
				@Override
				public void onComplete(@NonNull Task<AuthResult> task) {
					hideProgressDialog();
					if (task.isSuccessful()) {
						onAuthSuccess(task.getResult().getUser());
					} else {
						Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
	}

	private boolean validateForm(String email, String password) {
		if (TextUtils.isEmpty(email)) {
			mEmailField.setError(getString(R.string.required));
			return false;
		} else if (TextUtils.isEmpty(password)) {
			mPasswordField.setError(getString(R.string.required));
			return false;
		} else {
			mEmailField.setError(null);
			mPasswordField.setError(null);
			return true;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.button_sign_in:
				signIn();
				break;
			case R.id.button_sign_up:
				signUp();
				break;
		}
	}
}