package com.petra.petrachat;

import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    private List<Messages> mMessageList;
    private FirebaseAuth mAuth;

    public MessageAdapter(List<Messages> mMessageList) {
        this.mMessageList = mMessageList;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mAuth = FirebaseAuth.getInstance();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout ,parent, false);
        return new MessageViewHolder(v);
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;
        public CircleImageView profileImage;
        //public TextView displayName;
        //public ImageView messageImage;

        public MessageViewHolder(View view) {
            super(view);
            messageText = (TextView) view.findViewById(R.id.message_text_layout);
            profileImage = view.findViewById(R.id.message_profile_layout);
            //displayName = (TextView) view.findViewById(R.id.message_text_layout);
            //messageImage = (ImageView) view.findViewById(R.id.message_profile_layout);

        }
    }

    //GET PESAN DISIMPAN DI HP
    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {

        String current_user_id=mAuth.getCurrentUser().getUid();
        Messages c = mMessageList.get(i);
        String from_user=c.getFrom();
        if(from_user.equals(current_user_id)){
            viewHolder.messageText.setBackgroundColor(Color.WHITE);
            viewHolder.messageText.setTextColor(Color.BLACK);
        }else{
            viewHolder.messageText.setBackgroundColor(R.drawable.message_text_background);
            viewHolder.messageText.setTextColor(Color.WHITE);
        }
        viewHolder.messageText.setText(c.getMessage());
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }






}