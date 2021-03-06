package com.petra.petrachat;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    private List<Messages> mMessageList;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private TextView mTime;

    public MessageAdapter(List<Messages> mMessageList) {
        this.mMessageList = mMessageList;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mAuth = FirebaseAuth.getInstance();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single_layout ,parent, false);
        return new MessageViewHolder(v);
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;
        public CircleImageView profileImage;
        public TextView displayName;
        public ImageView messageImage;

        public MessageViewHolder(View view) {
            super(view);
            messageText  = view.findViewById(R.id.custom_bar_seen);
            profileImage = view.findViewById(R.id.custom_bar_image);
            displayName  = view.findViewById(R.id.custom_bar_title);
            messageImage = view.findViewById(R.id.message_image_layout);
            mTime        = view.findViewById(R.id.time_text_layout);
        }
    }

    //GET PESAN DISIMPAN DI HP
    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {

        String current_user_id = mAuth.getCurrentUser().getUid();
        Messages c             = mMessageList.get(i);
        String from_user       = c.getFrom();
        String message_type    = c.getType();
        final String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        mTime.setText(date);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name    = dataSnapshot.child("name").getValue().toString();
                String image   = dataSnapshot.child("thumb_image").getValue().toString();

                viewHolder.displayName.setText(name);
                Picasso.with(viewHolder.profileImage.getContext()).load(image)
                        .placeholder(R.drawable.defaultprofile).into(viewHolder.profileImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        if(message_type.equals("text")) {
            viewHolder.messageText.setText(c.getMessage());
            viewHolder.messageImage.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.messageText.setVisibility(View.INVISIBLE);
            Picasso.with(viewHolder.profileImage.getContext()).load(c.getMessage())
                    .placeholder(R.drawable.defaultprofile).into(viewHolder.messageImage);
        }
    }

    @Override
    public int getItemCount() { return mMessageList.size(); }
}