package com.petra.petrachat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
//firebase
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class Register_activity extends AppCompatActivity {

    private TextInputLayout mDisplayname;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private ProgressDialog mRegProgress;
    private Button mCreateBtn;
    //firebase auth
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextInputLayout edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activity);

        //regis dialog
        mRegProgress = new ProgressDialog(this);

        //firebase auth
        mAuth = FirebaseAuth.getInstance();

        mDisplayname = (TextInputLayout)findViewById(R.id.reg_display_name);
        mEmail = (TextInputLayout)findViewById(R.id.login_email);
        mPassword = (TextInputLayout)findViewById(R.id.login_password);
        mCreateBtn = (Button) findViewById(R.id.login_btn);

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //nyimpan inputan masuk ke variable
                String displayName = mDisplayname.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();

                    //cek inputan kosong
                   if(displayName.isEmpty() || email.isEmpty() || password.isEmpty())
                   {
                       AlertDialog alertDialog = new AlertDialog.Builder(Register_activity.this).create();
                       alertDialog.setTitle("Alert");
                       alertDialog.setMessage("Harap cek inputan anda Petra People");
                       alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                               new DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface dialog, int which) {
                                       dialog.dismiss();
                                   }
                               });
                       alertDialog.show();
                   }
                   else
                   {
                       //loading dialog
                       mRegProgress.setTitle("Registering user");
                       mRegProgress.setMessage("Mohon ditunggu Petra People");
                       mRegProgress.setCanceledOnTouchOutside(false);
                       mRegProgress.show();

                       register_user(displayName,email,password);
                   }

            }
        });
    }



    private void register_user(final String display_name, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){


                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = current_user.getUid();

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    String devicetoken=FirebaseInstanceId.getInstance().getToken();

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("name", display_name);
                    userMap.put("status", "Hi there I'm using Petrachat.");
                    userMap.put("image", "default");
                    userMap.put("thumb_image", "default");
                    userMap.put("devicetoken",devicetoken);

                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){

                                mRegProgress.dismiss();

                                Intent mainIntent = new Intent(Register_activity.this, MainActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                finish();

                            }

                        }
                    });
             }else {
                 mRegProgress.hide();
                 Toast.makeText(Register_activity.this,"Tidak bisa sign in, tolong cek kembali inputan anda!",Toast.LENGTH_LONG).show();
             }
            }
        });
    }
}
