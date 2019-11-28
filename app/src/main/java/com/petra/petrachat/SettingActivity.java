package com.petra.petrachat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingActivity extends AppCompatActivity {

    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;

    //Android Layout
    private CircleImageView mDisplayImage;
    private TextView mName;
    private TextView mStatus;
    private Button mStatusBtn;
    private Button mimageBtn;
    private static final int GALLERY_PICK = 1;

    //Storage
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mDisplayImage = findViewById(R.id.setting_image);
        mName = findViewById(R.id.setting_display_name);
        mStatus = findViewById(R.id.setting_status);

        mStatusBtn = findViewById(R.id.setting_status_btn);
        mimageBtn= findViewById(R.id.setting_image_btn);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        String current_uid = mCurrentUser.getUid();
        mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                mName.setText(name);
                mStatus.setText(status);


                if(!image.equals("default")) {
                    Picasso.with(SettingActivity.this).load(image).placeholder(R.drawable.defaultprofile).into(mDisplayImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status_value = mStatus.getText().toString();

                Intent status_intent = new Intent(SettingActivity.this,StatusActivity.class);
                status_intent.putExtra("status_value",status_value);
                startActivity(status_intent);
            }
        });

        mimageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent,"Select Image"), GALLERY_PICK);
                /*CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SettingActivity.this);*/
            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_PICK && resultCode == RESULT_OK ){

            Uri imageUri = data.getData();

            //cropping gambar
            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this);


        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();

                File thumb_filePath = new File(Objects.requireNonNull(resultUri.getPath()));

                String current_user_id = mCurrentUser.getUid();

                //compressing image
                //---------------------------------------------------------------------
                Bitmap thumb_bitmap= null;
                try {
                    new Compressor(this)
                            .setMaxWidth(250)
                            .setMaxHeight(250)
                            .setQuality(50)
                            .compressToBitmap(thumb_filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                assert thumb_bitmap != null;
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();

                //----------------------------------------------------------------------------

                final StorageReference filepath = mStorageRef.child("profile_images").child(current_user_id + ".jpg");
                final StorageReference thumb_filepath = mStorageRef.child("profile_images").child("thumb_image").child(current_user_id + ".jpg");


                filepath.putFile(resultUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw Objects.requireNonNull(task.getException());
                        }
                        return filepath.getDownloadUrl();
                    }
                }) .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            Uri downloadUri = task.getResult();
                            Map<String, Object> map = new HashMap<String, Object>();
                            assert downloadUri != null;
                            map.put("image",downloadUri.toString());
                            mUserDatabase.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(SettingActivity.this,"Upload berhasil",Toast.LENGTH_LONG).show();
                                }
                                    else {
                                        Toast.makeText(SettingActivity.this,"Upload Error",Toast.LENGTH_LONG).show();
                            }
                    }
                });
                        }else{
                            Toast.makeText(SettingActivity.this, "Upload error", Toast.LENGTH_LONG ).show();
                        }
                    }
                });

                thumb_filepath.putBytes(thumb_byte).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(task.isSuccessful()){
                            throw Objects.requireNonNull(task.getException());
                        }
                        return thumb_filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("thumb_image", downloadUri.toString());
                            mUserDatabase.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SettingActivity.this, "Upload berhasil", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(SettingActivity.this, "Upload Error", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(SettingActivity.this, "Upload Error", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public static String random(){
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++)
        {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }


}
