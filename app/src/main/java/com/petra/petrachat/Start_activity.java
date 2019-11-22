package com.petra.petrachat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Start_activity extends AppCompatActivity {

    private Button mRegBtn;
    private Button mLogBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_activity);

        mRegBtn=(Button) findViewById(R.id.start_reg_btn);
        mLogBtn=(Button)findViewById(R.id.start_login_btn);
        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg_intent=new Intent(Start_activity.this,Register_activity.class);
                startActivity(reg_intent);
            }
        });
        mLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent log_intent=new Intent(Start_activity.this,login_activity.class);
                startActivity(log_intent);
            }
        });
    }
}
