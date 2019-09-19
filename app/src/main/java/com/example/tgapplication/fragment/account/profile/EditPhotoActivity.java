package com.example.tgapplication.fragment.account.profile;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tgapplication.BaseActivity;
import com.example.tgapplication.R;
import com.example.tgapplication.photo.MyAdapter;
import com.example.tgapplication.photo.Upload;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditPhotoActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private FirebaseUser fuser;
    private ArrayList<Upload> uploads;
    private MyAdapter adapter;
    private Uri filePath;
    private static final int PICK_IMAGE_REQUEST = 234;
    private StorageReference storageReference;
    private String getDownloadImageUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);
        ButterKnife.bind(this);

        showProgressDialog();
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mGridLayoutManager);

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference();

        mDatabase = FirebaseDatabase.getInstance().getReference("Pictures").child(fuser.getUid());
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
//dismissing the progress dialog
                dismissProgressDialog();
                uploads = new ArrayList<>();

                uploads.add(new Upload("Gallery","R.drawable.avatar_boy_32"));
                uploads.add(new Upload("Fb","R.drawable.avatar_girl_32"));
//iterating through all the values in database
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    uploads.add(upload);
                }
//creating adapter
                adapter = new MyAdapter(EditPhotoActivity.this,fuser.getUid(), uploads);

//adding adapter to recyclerview
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dismissProgressDialog();
            }
        });


    }

    public void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
            filePath = data.getData();
            uploadFile(filePath);
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(Uri filePath) {
//checking if file is available
        Log.i("Result",""+filePath);
        if (filePath != null) {
//displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

//getting the storage reference
            final StorageReference sRef = storageReference.child("uploads/" + System.currentTimeMillis() + "." + getFileExtension(filePath));

//adding the file to reference
            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
//dismissing the progress dialog
                            progressDialog.dismiss();


//displaying success toast
//                            snackBar(fragment_acc_constraintLayout,"File Uploaded ");
                            Toast.makeText(EditPhotoActivity.this, "File Uploaded", Toast.LENGTH_SHORT).show();

                            sRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if(task.isSuccessful())
                                    {
                                        getDownloadImageUrl = task.getResult().toString();
                                        Log.i("FirebaseImages",getDownloadImageUrl);

//creating the upload object to store uploaded image details
                                        Upload upload = new Upload("Image", getDownloadImageUrl);

//adding an upload to firebase database
                                        String uploadId = mDatabase.push().getKey();
                                        mDatabase.child(uploadId).setValue(upload);
                                    }
                                    else {
                                        Toast.makeText(EditPhotoActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                        snackBar(fragment_acc_constraintLayout,task.getException().getMessage());
                                    }
                                }
                            });
                        }
                    })


                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Log.i("Failure",exception.getMessage());
                        }
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        //displaying the upload progress
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    });
        } else {
            Toast.makeText(this, "Please Select a Image", Toast.LENGTH_SHORT).show();
//            snackBar(fragment_acc_constraintLayout,"Please Select a Image");
        }
    }
}
