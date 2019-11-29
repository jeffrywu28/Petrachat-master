package com.petra.petrachat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private ImageView mProfileImage;
    private TextView mProfileName,mProfileStatus,mProfileFriendsCount;
    private Button mProfileSendReqBtn;

    //db user
    private DatabaseReference mUsersDatabase;
    private FirebaseUser mCurrent_user;
    //db friend
    private DatabaseReference mFriendReqReference;
    private String mCurrent_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String user_id =getIntent().getStringExtra("user_id");

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mFriendReqReference =FirebaseDatabase.getInstance().getReference().child("friend_req");
        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();

        mProfileImage= findViewById(R.id.profileImage);
        mProfileName = findViewById(R.id.profile_displayname);
        mProfileStatus = findViewById(R.id.profile_status);
        mProfileFriendsCount=findViewById(R.id.profile_totalfriend);
        mProfileSendReqBtn= findViewById(R.id.profile_send_req_btn);

        //declare friend
        mCurrent_state="not friends";

        //load data from db
        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();

                mProfileName.setText(name);
                mProfileStatus.setText(status);
                Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.defaultprofile).into(mProfileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mProfileSendReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mProfileSendReqBtn.setEnabled(false);

                if(mCurrent_state.equals("not friends")){
                    mFriendReqReference.child(mCurrent_user.getUid()).child(user_id).child("request type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mFriendReqReference.child(user_id).child(mCurrent_user.getUid()).child("request type").setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(ProfileActivity.this,"Request Sent Success",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }else {
                                Toast.makeText(ProfileActivity.this,"Failed Sending request",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

    }//t1ekcxg
}
