package com.petra.petrachat;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestsFragment extends Fragment {
    private RecyclerView mFriend_reqlist;
    private DatabaseReference mFriends_reqDatabase;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String mcurrent_userid;
    private View mMainview;
    private Query db;

    public RequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        mMainview=inflater.inflate(R.layout.fragment_requests, container, false);
        mFriend_reqlist = (RecyclerView) mMainview.findViewById(R.id.requestlist);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()!=null)
        {
            mcurrent_userid = mAuth.getCurrentUser().getUid();
        }
        else
        {
            startActivity(new Intent(getActivity(),login_activity.class));
        }

        mFriends_reqDatabase = FirebaseDatabase.getInstance().getReference().child("friend_req").child(mcurrent_userid);
        db=mFriends_reqDatabase.orderByChild("request_type").equalTo("received");

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        mFriend_reqlist.setHasFixedSize(true);
        mFriend_reqlist.setLayoutManager(new LinearLayoutManager(getContext()));
        return mMainview;

    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Friend_req> option_request=
                new FirebaseRecyclerOptions.Builder<Friend_req>()
                        .setQuery(db,Friend_req.class)
                        .setLifecycleOwner(this)
                        .build();

        FirebaseRecyclerAdapter<Friend_req,Friends_reqViewHolder> friend_reqrecycleradapter=new
                FirebaseRecyclerAdapter<Friend_req, Friends_reqViewHolder>(option_request) {

                    @NonNull
                    @Override
                    public Friends_reqViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_single_layout,parent,false);
                        return new Friends_reqViewHolder(mView);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull final Friends_reqViewHolder holder, int position, @NonNull Friend_req model) {

                        final String list_user_id1 = getRef(position).getKey();
                        mDatabase.child(list_user_id1).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                final String username = dataSnapshot.child("name").getValue().toString();
                                final String userthumb_img = dataSnapshot.child("thumb_image").getValue().toString();
                                String userstatus = dataSnapshot.child("status").getValue().toString();
                                holder.setName(username);
                                holder.setStatus(userstatus);
                                holder.setUserImage(userthumb_img, getContext());

                                holder.mview1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getContext(), ProfileActivity.class);
                                        intent.putExtra("user_id", list_user_id1);
                                        startActivity(intent);
                                    }
                                });


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }

                        });
                    }

                };
        mFriend_reqlist.setAdapter(friend_reqrecycleradapter);
    }

    public static class Friends_reqViewHolder extends RecyclerView.ViewHolder {
        View mview1;
        public Friends_reqViewHolder(View itemView) {
            super(itemView);
            mview1=itemView;
        }
        public void setName(String name){
            TextView usernameview=(TextView) mview1.findViewById(R.id.user_single_name);
            usernameview.setText(name);
        }
        public void setStatus(String status){
            TextView userstatus=(TextView) mview1.findViewById(R.id.user_single_status);
            userstatus.setText(status);
        }

        public void setUserImage(String image, Context ctx){
            CircleImageView circleImageView2=mview1.findViewById(R.id.user_single_image);
            Picasso.with(ctx).load(image).placeholder(R.drawable.defaultprofile).into(circleImageView2);
        }
    }

}
