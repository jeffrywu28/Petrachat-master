package com.petra.petrachat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class UserActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView muserList;
    private DatabaseReference mUserDatabase;
    private FirebaseRecyclerAdapter<Users,UserViewHolder> adapter;

    //Memanggil layout untuk All users
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //toolbar
        mToolbar = findViewById(R.id.userBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All User");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //mengambil data userlist dari firebase
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        muserList = findViewById(R.id.userlist);
        muserList.setHasFixedSize(true);
        muserList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        //membuat recycler options
        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(mUserDatabase, Users.class)
                        .setLifecycleOwner(this)
                        .build();

        //membuat objek recycler
        adapter = new FirebaseRecyclerAdapter<Users, UserViewHolder>(options) {

            //get data user
            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new UserViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_single, parent, false));
            }
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull Users model) {
                holder.setName(model.getName());
            }

        };
        //mengatur adapter supaya dapat dijalankan
        muserList.setAdapter(adapter);
    }

    //fungsi untuk get data user
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setName(String name){
            TextView userNameView = mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);
        }
        public void 
    }
}
