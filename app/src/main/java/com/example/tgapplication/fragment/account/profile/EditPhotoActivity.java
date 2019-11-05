package com.example.tgapplication.fragment.account.profile;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.tgapplication.Constants.PicturesInstance;

public class EditPhotoActivity extends BaseActivity {

    @BindView(R.id.public_recyclerView)
    RecyclerView publicRecyclerView;
    @BindView(R.id.private_recyclerView)
    RecyclerView privateRecyclerView;
    private FirebaseUser fuser;
    private ArrayList<Upload> public_uploads, private_uploads, upload1, upload2;
    private MyAdapter public_adapter, private_adapter;
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
        publicRecyclerView.setLayoutManager(mGridLayoutManager);
        GridLayoutManager mGridLayoutManager1 = new GridLayoutManager(this, 3);
        privateRecyclerView.setLayoutManager(mGridLayoutManager1);

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference();

        PicturesInstance.child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//dismissing the progress dialog
                dismissProgressDialog();
                upload1 = new ArrayList<>();
                upload2 = new ArrayList<>();
                public_uploads = new ArrayList<>();
                private_uploads = new ArrayList<>();

//iterating through all the values in database
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    if (Objects.requireNonNull(upload).getType() == 3) {
                        private_uploads.add(upload);
                    }
                    else if(upload.getType()==1) {
                        upload1.add(upload);
                    }
                    else if(upload.getType()==2) {
                        upload2.add(upload);
                    }

                }

                if (upload1.size() > 0) {
                    public_uploads.addAll(upload1);
                }
                if (upload2.size() > 0) {
                    public_uploads.addAll(upload2);
                }

                if (public_uploads.size() > 0) {


//creating adapter
                    public_adapter = new MyAdapter(EditPhotoActivity.this, fuser.getUid(), public_uploads, new MyAdapter.PhotoInterface() {

                        @Override
                        public void setProfilePhoto(String id, String previousValue, int pos) {
                            PicturesInstance
                                    .child(fuser.getUid())
                                    .child(id).child("type").setValue(1);

                            if(!previousValue.equals("") && !previousValue.equals(id))
                                PicturesInstance.child(fuser.getUid()).child(previousValue).child("type").setValue(2);
                            Log.i(TAG, "setProfilePhoto: "+public_uploads.get(pos).getUrl());
                            profilePhotoDetails(public_uploads.get(pos).getUrl());
                        }

                        @Override
                        public void removePhoto(String id) {
                            PicturesInstance.child(fuser.getUid()).child(id).removeValue();
                        }

                        @Override
                        public void setPhotoAsPrivate(String id) {
                            PicturesInstance
                                    .child(fuser.getUid())
                                    .child(id).child("type").setValue(3);
                        }
                    });
                }

                private_adapter = new MyAdapter(EditPhotoActivity.this, fuser.getUid(), private_uploads, new MyAdapter.PhotoInterface() {

                    @Override
                    public void setProfilePhoto(String id, String previousValue, int pos) {
                        PicturesInstance
                                .child(fuser.getUid())
                                .child(id).child("type").setValue(1);

                        if(!previousValue.equals("") && !previousValue.equals(id))
                            PicturesInstance.child(fuser.getUid()).child(previousValue).child("type").setValue(2);
                        profilePhotoDetails(private_uploads.get(pos).getUrl());
                    }

                    @Override
                    public void removePhoto(String id) {
                        PicturesInstance.child(fuser.getUid()).child(id).removeValue();
                    }

                    @Override
                    public void setPhotoAsPrivate(String id) {
                        PicturesInstance
                                .child(fuser.getUid())
                                .child(id).child("type").setValue(2);
                    }
                });

//adding adapter to recyclerview
                publicRecyclerView.setAdapter(public_adapter);
                privateRecyclerView.setAdapter(private_adapter);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                dismissProgressDialog();
            }
        });

    /*    PicturesInstance.child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
//dismissing the progress dialog
                dismissProgressDialog();
                public_uploads = new ArrayList<>();
                private_uploads = new ArrayList<>();

//iterating through all the values in database
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    if (upload.getType() == 3) {
                        private_uploads.add(upload);
                    } else {
                        public_uploads.add(upload);
                    }

                }
//creating adapter
                public_adapter = new MyAdapter(EditPhotoActivity.this, fuser.getUid(), public_uploads, new MyAdapter.PhotoInterface() {
                    @Override
                    public void setProfilePhoto(String id, String previousValue, int pos) {
                        PicturesInstance
                                .child(fuser.getUid())
                                .child(id).child("type").setValue(1);

                        if(!previousValue.equals("") && !previousValue.equals(id))
                        PicturesInstance.child(fuser.getUid()).child(previousValue).child("type").setValue(2);
                        profilePhotoDetails(public_uploads.get(pos).getUrl());
                    }

                    @Override
                    public void removePhoto(String id) {
                        PicturesInstance.child(fuser.getUid()).child(id).removeValue();
                    }

                    @Override
                    public void setPhotoAsPrivate(String id) {
                        PicturesInstance
                                .child(fuser.getUid())
                                .child(id).child("type").setValue(3);
                    }
                });

                private_adapter = new MyAdapter(EditPhotoActivity.this, fuser.getUid(), private_uploads, new MyAdapter.PhotoInterface() {
                    @Override
                    public void setProfilePhoto(String id, String previousValue,int pos) {
                        PicturesInstance
                                .child(fuser.getUid())
                                .child(id).child("type").setValue(1);

                        if(!previousValue.equals("") && !previousValue.equals(id))
                        PicturesInstance.child(fuser.getUid()).child(previousValue).child("type").setValue(2);
                        profilePhotoDetails(private_uploads.get(pos).getUrl());
                    }

                    @Override
                    public void removePhoto(String id) {
                        PicturesInstance.child(fuser.getUid()).child(id).removeValue();
                    }

                    @Override
                    public void setPhotoAsPrivate(String id) {
                        PicturesInstance
                                .child(fuser.getUid())
                                .child(id).child("type").setValue(2);
                    }
                });

//adding adapter to recyclerview
                publicRecyclerView.setAdapter(public_adapter);
                privateRecyclerView.setAdapter(private_adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dismissProgressDialog();
            }
        });*/


    }
    private void profilePhotoDetails(String imageUrl) {
//        Log.i(TAG, "profilePhotoDetails: "+imageUrl);
       SharedPreferences sharedPreferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ImageUrl", imageUrl);
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.photo_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.gallery:
                showFileChooser();
                break;

            case R.id.facebook:

                startActivity(new Intent(this,FacebookImage.class));
//                Toast.makeText(this, "Facebook", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
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
        Log.i("Result", "" + filePath);
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

                            String uploadId = PicturesInstance.child(fuser.getUid()).push().getKey();

                            sRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        getDownloadImageUrl = Objects.requireNonNull(task.getResult()).toString();
                                        Log.i("FirebaseImages", getDownloadImageUrl);

//creating the upload object to store uploaded image details
                                        Upload upload;
                                        if (public_uploads.size() == 0) {
                                            upload = new Upload(uploadId, "Image", getDownloadImageUrl, 1);
                                        } else {
                                            upload = new Upload(uploadId, "Image", getDownloadImageUrl, 2);
                                        }

//adding an upload to firebase database
                                        Log.i(TAG, "onComplete: " + PicturesInstance.child(fuser.getUid()).getKey());

                                        PicturesInstance.child(fuser.getUid()).child(Objects.requireNonNull(uploadId)).setValue(upload);
                                    } else {
                                        Toast.makeText(EditPhotoActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
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
                            Log.i("Failure", Objects.requireNonNull(exception.getMessage()));
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
