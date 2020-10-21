package com.example.mhchat.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mhchat.models.Chat;
import com.example.mhchat.models.User;
import com.example.mhchat.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends AppCompatActivity {
	public static final String MESSAGES_CHILD = "chat";

	private DatabaseReference mFirebaseDatabaseReference;
	private FirebaseRecyclerAdapter<Chat, MessageViewHolder> mFirebaseAdapter;

	private Button mSendButton;
	private RecyclerView mMessageRecyclerView;
	private LinearLayoutManager mLinearLayoutManager;
	private EditText mMessageEditText;
	private String mEmail = "Anonymous";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		mMessageEditText = findViewById(R.id.messageEditText);
		mMessageRecyclerView = findViewById(R.id.messageRecyclerView);
		mLinearLayoutManager = new LinearLayoutManager(this);
		mLinearLayoutManager.setStackFromEnd(true);

		// Initialize Firebase Auth
		FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
		FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
		mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
		mFirebaseDatabaseReference.child("users").child(mFirebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				User user = dataSnapshot.getValue(User.class);
				mEmail = user.email;
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {}
		});

		Query query = mFirebaseDatabaseReference.child(MESSAGES_CHILD);
		FirebaseRecyclerOptions<Chat> options = new FirebaseRecyclerOptions.Builder<Chat>()
				.setQuery(query, Chat.class)
				.build();

		mFirebaseAdapter = new FirebaseRecyclerAdapter<Chat, MessageViewHolder>(options) {
			@Override
			protected void onBindViewHolder(@NonNull MessageViewHolder viewHolder, int position, Chat chat) {
				if (chat.getUsername().equals(mEmail)) {
					viewHolder.row.setGravity(Gravity.END);
				} else {
					viewHolder.row.setGravity(Gravity.START);
				}
				viewHolder.usernameTextView.setText(chat.getUsername());
				viewHolder.chatTextView.setText(chat.getText());
		}

			@Override
			public MessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
				LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
				return new MessageViewHolder(inflater.inflate(R.layout.item_message, viewGroup, false));
			}
		};

		mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
			@Override
			public void onItemRangeInserted(int positionStart, int itemCount) {
				super.onItemRangeInserted(positionStart, itemCount);
				int chatCount = mFirebaseAdapter.getItemCount();
				int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();

				if (lastVisiblePosition == -1 || (positionStart >= (chatCount - 1) && lastVisiblePosition == (positionStart - 1))) {
					mMessageRecyclerView.scrollToPosition(positionStart);
				}
			}
		});
		mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
		mMessageRecyclerView.setAdapter(mFirebaseAdapter);

		mMessageEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				if (charSequence.toString().trim().length() > 0) {
					mSendButton.setEnabled(true);
				} else {
					mSendButton.setEnabled(false);
				}
			}

			@Override
			public void afterTextChanged(Editable editable) {
			}
		});

		mSendButton = findViewById(R.id.sendButton);
		mSendButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Chat chat = new Chat(mMessageEditText.getText().toString(), mEmail);
				mFirebaseDatabaseReference.child(MESSAGES_CHILD).push().setValue(chat);
				mMessageEditText.setText("");
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (mFirebaseAdapter != null) {
			mFirebaseAdapter.startListening();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mFirebaseAdapter != null) {
			mFirebaseAdapter.stopListening();
		}
	}

	public static class MessageViewHolder extends RecyclerView.ViewHolder {
		LinearLayout row;
		TextView usernameTextView;
		TextView chatTextView;

		MessageViewHolder(View v) {
			super(v);
			row = itemView.findViewById(R.id.row);
			usernameTextView = itemView.findViewById(R.id.usernameTextView);
			chatTextView = itemView.findViewById(R.id.chatTextView);
		}
	}
}