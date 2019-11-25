package com.petra.petrachat;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private TextInputLayout mStatus;
    private Button mSavebtn;

    //Firebase
    private DatabaseReference mStatusDataBase;
    private FirebaseUser mCurrentUser;

    //Progress
    private ProgressDialog mProgress;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        //Firebase
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currnt_uid = mCurrentUser.getUid();

        mStatusDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(currnt_uid);

        mToolbar = (Toolbar) findViewById(R.id.status_appBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Account Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mStatus = (TextInputLayout) findViewById(R.id.status_input);
        mSavebtn = (Button) findViewById(R.id.status_save_btn);

        mSavebtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //Progress
                mProgress = new ProgressDialog(getApplicationContext());
                mProgress.setTitle("Saving Changes");
                mProgress.setMessage("Mohon Ditunggu sedang disimpan");
                mProgress.show();

                String status = mStatus.getEditText().getText().toString();

                mStatusDataBase.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){

                            mProgress.dismiss();

                        }else{
                            Toast.makeText(getApplicationContext(), "saving changes sedang error", Toast.LENGTH_LONG).show();
                        }

                    }
                })
            }
        });

    }
}
