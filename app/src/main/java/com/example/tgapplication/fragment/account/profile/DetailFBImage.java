package com.example.tgapplication.fragment.account.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tgapplication.BaseActivity;
import com.example.tgapplication.R;
import com.example.tgapplication.photo.FB_Adapter;
import com.example.tgapplication.photo.MyAdapter;
import com.example.tgapplication.photo.Upload;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFBImage extends BaseActivity {
    @BindView(R.id.detail_recyclerview)
    RecyclerView detailRecyclerview;
    private FirebaseUser fuser;

    ArrayList<FacebookImage.FbImage> detailUploads=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_fb_image);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("bundle");
        detailUploads = (ArrayList<FacebookImage.FbImage>) args.getSerializable("detailFb");


        Log.i(TAG, "onCreate: "+detailUploads.size());
        fuser = FirebaseAuth.getInstance().getCurrentUser();


        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 3);
        detailRecyclerview.setLayoutManager(mGridLayoutManager);

        //creating adapter
        DetailFBAdapter fbadapter = new DetailFBAdapter(DetailFBImage.this, detailUploads, new DetailFBAdapter.DetailFbInterface() {
            @Override
            public void fetchFbImage(String imgUrl) {
                String uploadId = PicturesInstance.child(fuser.getUid()).push().getKey();
                Upload upload = new Upload(uploadId,"FB_Image", imgUrl,2);
                PicturesInstance.child(fuser.getUid()).child(uploadId).setValue(upload);
            }
        });

        detailRecyclerview.setAdapter(fbadapter);

    }
}
