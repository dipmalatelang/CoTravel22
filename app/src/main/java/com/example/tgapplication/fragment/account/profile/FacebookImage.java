package com.example.tgapplication.fragment.account.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tgapplication.BaseActivity;
import com.example.tgapplication.R;
import com.example.tgapplication.photo.FB_Adapter;
import com.example.tgapplication.photo.Upload;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FacebookImage extends BaseActivity {

    @BindView(R.id.fb_recyclerview)
    RecyclerView fbRecyclerview;
    @BindView(R.id.detail_recyclerview)
    RecyclerView detailRecyclerview;
    @BindView(R.id.login_button)
    LoginButton loginButton;
    private ArrayList<FbImage> lstFBImages;
    private ArrayList<Images> photoAlbums = new ArrayList<>();
    FB_Adapter fb_adapter;
    private FirebaseUser fuser;
    private CallbackManager mCallbackManager;
//    private FirebaseAuth mAuth;

    String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_facebook_image);
        ButterKnife.bind(this);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        //mAuth = FirebaseAuth.getInstance();

        mCallbackManager = CallbackManager.Factory.create();

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 3);
        fbRecyclerview.setLayoutManager(mGridLayoutManager);

        GridLayoutManager mGridLayoutManager1 = new GridLayoutManager(this, 3);
        detailRecyclerview.setLayoutManager(mGridLayoutManager1);

        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Tiger", "facebook:onSuccess:" + loginResult);
                Log.d("Tiger", "facebook:token:" + loginResult.getAccessToken());
//                handleFacebookAccessToken(loginResult.getAccessToken().getToken());

//                  //your fb AccessToken
                new GraphRequest(
                        loginResult.getAccessToken(),
                        "/" + AccessToken.getCurrentAccessToken().getUserId() + "/albums",//user id of login user
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                Log.d("TAG", "Facebook Albums: " + response.toString());
                                try {
                                    if (response.getError() == null) {
                                        JSONObject joMain = response.getJSONObject(); //convert GraphResponse response to JSONObject
                                        if (joMain.has("data")) {
                                            JSONArray jaData = joMain.optJSONArray("data"); //find JSONArray from JSONObject
                                            Log.i(TAG, "onCompleted: " + Objects.requireNonNull(jaData).length());
                                            for (int i = 0; i < jaData.length(); i++) {//find no. of album using jaData.length()
                                                JSONObject joAlbum = jaData.getJSONObject(i); //convert perticular album into JSONObject
                                                Log.i(TAG, "onCompleted: " + joAlbum.optString("name"));
                                                GetFacebookImages(joAlbum.optString("id"), joAlbum.optString("name"));
//                                            Log.i(TAG, "onCompleted: "+joAlbum.optString("id"));

                                            }
                                            //find Album ID and get All Images from album
                                        }
                                    } else {
                                        Log.d("Test", response.getError().toString());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                ).executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d("Tiger", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Tiger", "facebook:onError", error);
                // ...
            }
        });

        getAlbum();
    }

 /*   private void handleFacebookAccessToken(String token) {
        showProgressDialog();
        AuthCredential credential = FacebookAuthProvider.getCredential(token);
        Log.d("Tiger", "" + credential);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Tiger", "handleFacebookAccessToken:" + task.isSuccessful());
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Tiger", "signInWithCredential:success");
                            dismissProgressDialog();

                            getAlbum();


                        }

                    }
                });
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void getAlbum() {

        if (AccessToken.getCurrentAccessToken() != null) {
            /*make API call*/
            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),  //your fb AccessToken
                    "/" + AccessToken.getCurrentAccessToken().getUserId() + "/albums",//user id of login user
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            Log.d("TAG", "Facebook Albums: " + response.toString());
                            try {
                                if (response.getError() == null) {
                                    JSONObject joMain = response.getJSONObject(); //convert GraphResponse response to JSONObject
                                    if (joMain.has("data")) {
                                        JSONArray jaData = joMain.optJSONArray("data"); //find JSONArray from JSONObject
                                        Log.i(TAG, "onCompleted: " + Objects.requireNonNull(jaData).length());
                                        for (int i = 0; i < jaData.length(); i++) {//find no. of album using jaData.length()
                                            JSONObject joAlbum = jaData.getJSONObject(i); //convert perticular album into JSONObject
                                            Log.i(TAG, "onCompleted: " + joAlbum.optString("name"));
                                            GetFacebookImages(joAlbum.optString("id"), joAlbum.optString("name"));
//                                            Log.i(TAG, "onCompleted: "+joAlbum.optString("id"));

                                        }
                                        //find Album ID and get All Images from album
                                    }
                                } else {
                                    Log.d("Test", response.getError().toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            ).executeAsync();

        } else {
//            Toast.makeText(this, "First login with facebook", Toast.LENGTH_SHORT).show();
            loginButton.performClick();
        }
    }


    public void GetFacebookImages(String albumId, String name) {
//        String url = "https://graph.facebook.com/" + "me" + "/"+albumId+"/photos?access_token=" + AccessToken.getCurrentAccessToken() + "&fields=images";
        Bundle parameters = new Bundle();
        parameters.putString("fields", "images");
        /* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + albumId + "/photos",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        /* handle the result */
                        Log.v("TAG", "Facebook Photos response: " + response);
//                        tvTitle.setText("Facebook Images");
                        try {
                            if (response.getError() == null) {

                                JSONObject joMain = response.getJSONObject();
                                if (joMain.has("data")) {
                                    JSONArray jaData = joMain.optJSONArray("data");
//                                    Log.i("TAG", "onCompleted: " + jaData.getJSONObject(0).getJSONArray("images").getJSONObject(0).getString("source"));
                                    lstFBImages = new ArrayList<>();
                                    for (int i = 0; i < Objects.requireNonNull(jaData).length(); i++)//Get no. of images
                                    {
                                        JSONObject joAlbum = jaData.getJSONObject(i);
                                        JSONArray jaImages = joAlbum.getJSONArray("images");

                                        if (jaImages.length() > 0) {

                                            lstFBImages.add(new FbImage(jaImages.getJSONObject(0).getString("source"), 0));//lstFBImages is Images object array
                                        }
                                    }
                                  /*  Log.i("TAG", "onCompleted: " + lstFBImages.size());
                                    for (int j = 0; j < lstFBImages.size(); j++) {
                                        Log.i("TAG", "onCompleted: " + lstFBImages.get(j).getImage_Url());
                                    }*/

                                    Log.i("TAG", "onCompleted: " + name + " " + lstFBImages.size());
                                    if (lstFBImages.size() > 0) {
                                        PicturesInstance.child(fuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                    Upload upload = ds.getValue(Upload.class);
                                                    Log.i(TAG, "onDataChange: " + Objects.requireNonNull(upload).getUrl());
                                                    if (upload.getName().equalsIgnoreCase("FB_Image")) {
                                                        for (int k = 0; k < lstFBImages.size(); k++) {
                                                            if (upload.getUrl().equals(lstFBImages.get(k).getUrl())) {
                                                                Log.i(TAG, "onDataChange working: " + 1);
                                                                lstFBImages.get(k).setStatus(1);
                                                            }
                                                        }

                                                    }

                                                }
                                                photoAlbums.add(new Images(albumId, name, lstFBImages));

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });

                                    }


                                }

                                fb_adapter = new FB_Adapter(FacebookImage.this, fuser.getUid(), photoAlbums, new FB_Adapter.FbInterface() {
                                    @Override
                                    public void proceed(ArrayList<FbImage> image_url) {
                                        fbRecyclerview.setVisibility(View.GONE);
                                        detailRecyclerview.setVisibility(View.VISIBLE);



//                                        Intent intent = new Intent(FacebookImage.this, DetailFBImage.class);
//                                        intent.putExtra("detailFb", new Gson().toJson(image_url));
//                                        startActivity(intent);

                                        //creating adapter
                                        DetailFBAdapter fbadapter = new DetailFBAdapter(FacebookImage.this, image_url, new DetailFBAdapter.DetailFbInterface() {
                                            @Override
                                            public void fetchFbImage(String imgUrl) {
                                                String uploadId = PicturesInstance.child(fuser.getUid()).push().getKey();
                                                Upload upload = new Upload(uploadId, "FB_Image", imgUrl, 2);
                                                PicturesInstance.child(fuser.getUid()).child(Objects.requireNonNull(uploadId)).setValue(upload);
                                            }
                                        });

                                        detailRecyclerview.setAdapter(fbadapter);
                                    }
                                });

//adding adapter to recyclerview
                                fbRecyclerview.setAdapter(fb_adapter);
                                //set your adapter here
                            } else {
                                Log.v("TAG", response.getError().toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }).executeAsync();
    }

    public class Images {
        String id, name;
        ArrayList<FbImage> image_Url;

        public Images(String id, String name, ArrayList<FbImage> image_Url) {
            this.id = id;
            this.name = name;
            this.image_Url = image_Url;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ArrayList<FbImage> getImage_Url() {
            return image_Url;
        }

        public void setImage_Url(ArrayList<FbImage> image_Url) {
            this.image_Url = image_Url;
        }
    }

    public class FbImage {
        String url;
        int status;

        public FbImage(String url, int status) {
            this.url = url;
            this.status = status;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}