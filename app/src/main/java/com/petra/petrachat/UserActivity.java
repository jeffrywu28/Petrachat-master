package com.petra.petrachat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class UserActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView muserList;
    private DatabaseReference mUserDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mToolbar = findViewById(R.id.userBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All User");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        muserList = findViewById(R.id.userlist);
        muserList.setHasFixedSize(true);
        muserList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Users,UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UserViewHolder>(
                Users.class,
                R.layout.user_single,
                UserViewHolder.class,
                mUserDatabase
        ) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull Users model) {
                UserViewHolder.setName(model.getName());
            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }
        };
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
        }
        public void setName(String name){
            TextView userNameView = mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);
        }
    }
}
