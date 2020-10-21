package com.example.mhchat;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import com.example.mhchat.models.Thoughts;
import com.example.mhchat.R;


public  class ThoughtsAdapter extends RecyclerView.Adapter<ThoughtsAdapter.RecyclerViewHolder>{
    private Context mContext;
    private List<Thoughts> thoughts;
    private OnItemClickListener mListener;

    public ThoughtsAdapter(Context context, List<Thoughts> uploads) {
        mContext = context;
        thoughts = uploads;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_thought, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Thoughts currentThought = thoughts.get(position);
        holder.usernameTextView.setText(currentThought.getUserName());
        holder.thoughtTextView.setText(currentThought.getThought());
        Picasso.get()
                .load(currentThought.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .fit()
                .centerCrop()
                .into(holder.thoughtImageView);
    }

    @Override
    public int getItemCount() {
        return thoughts.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView usernameTextView,thoughtTextView;
        public ImageView thoughtImageView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            usernameTextView =itemView.findViewById ( R.id.usernameTextView );
            thoughtTextView = itemView.findViewById(R.id.thoughtTextView);
            thoughtImageView = itemView.findViewById(R.id.thoughtImageView);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem showItem = menu.add( Menu.NONE, 1, 1, "Show");
            MenuItem deleteItem = menu.add(Menu.NONE, 2, 2, "Delete");

            showItem.setOnMenuItemClickListener(this);
            deleteItem.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId()) {
                        case 1:
                            mListener.onShowItemClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteItemClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onShowItemClick(int position);
        void onDeleteItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

}
