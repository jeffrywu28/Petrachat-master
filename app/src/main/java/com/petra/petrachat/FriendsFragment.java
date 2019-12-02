package com.petra.petrachat;


import android.content.Context;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsFragment extends Fragment {
    private RecyclerView mFriendlist;
    private DatabaseReference mFriendsDatabase;
    private DatabaseReference mUsersDatabase;
    private FirebaseAuth mAuth;
    private LinearLayoutManager mlinaerlayout;
    private String mCurrent_user_id;
    private View mMainView;

    public FriendsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_friends, container, false);
        mFriendlist = mMainView.findViewById(R.id.friend_list);
        mAuth = FirebaseAuth.getInstance();
        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        //friend db
        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("friends").child(mCurrent_user_id);
        mFriendsDatabase.keepSynced(true);

        //user db
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);

        mFriendlist.setHasFixedSize(true);
        mFriendlist.setLayoutManager(new LinearLayoutManager(getContext()));
        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Friends> optionfriends= new FirebaseRecyclerOptions.Builder<Friends>()
                        .setQuery(mFriendsDatabase,Friends.class)
                        .setLifecycleOwner(this)
                        .build();
        FirebaseRecyclerAdapter<Friends, FriendsViewHolder> friendsRecyclerAdapter = new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>(optionfriends)
                {
                    @NonNull
                    @Override
                    public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_single_layout,parent,false);
                        return new FriendsViewHolder(mView);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull final FriendsViewHolder holder, int position, @NonNull Friends model) {
                        holder.setDate(model.getDate());
                        final String list_user_id=getRef(position).getKey();
                        mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final String name=dataSnapshot.child("name").getValue().toString();
                                final String userThumb=dataSnapshot.child("thumb_image").getValue().toString();

                                holder.setName(name);
                                holder.setUserImage(userThumb,getContext());
                                /*if (dataSnapshot.hasChild("online")){
                                    String useronline=dataSnapshot.child("online").getValue().toString();
                                    holder.setUserOnline(useronline);
                                }*/

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                };

        mFriendlist.setAdapter(friendsRecyclerAdapter);
    }
    public static class FriendsViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public FriendsViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }
        public  void setDate(String date)
        {
            TextView userNameView= mView.findViewById(R.id.user_single_status);
            userNameView.setText(date);

        }
        public void setName(String name){
            TextView usernameview= mView.findViewById(R.id.user_single_name);
            usernameview.setText(name);
        }
        public void setStatus(String status){
            TextView userstatus= mView.findViewById(R.id.user_single_status);
            userstatus.setText(status);
        }

        public void setUserImage(String image, Context ctx){
            CircleImageView circleImageView2=mView.findViewById(R.id.user_single_image);
            Picasso.with(ctx).load(image).placeholder(R.drawable.defaultprofile).into(circleImageView2);
        }


    }
}
