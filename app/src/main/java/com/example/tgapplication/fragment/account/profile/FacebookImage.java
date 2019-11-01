package com.example.tgapplication.fragment.account.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tgapplication.BaseActivity;
import com.example.tgapplication.R;
import com.example.tgapplication.photo.FB_Adapter;
import com.example.tgapplication.photo.Upload;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FacebookImage extends BaseActivity {

    @BindView(R.id.fb_recyclerview)
    RecyclerView fbRecyclerview;
    private ArrayList<FbImage> lstFBImages;
    private ArrayList<Images> photoAlbums = new ArrayList<>();
    FB_Adapter fb_adapter;
    private FirebaseUser fuser;

    String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_facebook_image);
        ButterKnife.bind(this);

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 3);
        fbRecyclerview.setLayoutManager(mGridLayoutManager);

        getRemoteFbUrl();

//     getAlbum();

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
                                        Log.i(TAG, "onCompleted: " + jaData.length());
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
            Toast.makeText(this, "First login with facebook", Toast.LENGTH_SHORT).show();
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
                                    for (int i = 0; i < jaData.length(); i++)//Get no. of images
                                    {
                                        JSONObject joAlbum = jaData.getJSONObject(i);
                                        JSONArray jaImages = joAlbum.getJSONArray("images");

                                        if (jaImages.length() > 0) {

                                            lstFBImages.add(new FbImage(jaImages.getJSONObject(0).getString("source"),0));//lstFBImages is Images object array
                                        }
                                    }
                                  /*  Log.i("TAG", "onCompleted: " + lstFBImages.size());
                                    for (int j = 0; j < lstFBImages.size(); j++) {
                                        Log.i("TAG", "onCompleted: " + lstFBImages.get(j).getImage_Url());
                                    }*/

                                    Log.i("TAG", "onCompleted: " + name + " " + lstFBImages.size());
                                    if (lstFBImages.size() > 0)
                                        photoAlbums.add(new Images(albumId, name, lstFBImages));

                                }

                                fb_adapter = new FB_Adapter(FacebookImage.this, fuser.getUid(), photoAlbums, new FB_Adapter.FbInterface() {
                                    @Override
                                    public void proceed(ArrayList<FbImage> image_url) {
                                        Intent intent = new Intent(FacebookImage.this, DetailFBImage.class);
                                        Bundle args = new Bundle();
                                        args.putSerializable("bundle",(Serializable)image_url);
                                        intent.putExtra("detailFb",args);
                                        startActivity(intent);
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

    int valueInt;
    public int getRemoteFbUrl(){


        Toast.makeText(this, "Hi "+valueInt, Toast.LENGTH_SHORT).show();
        return valueInt;
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

    public class FbImage implements Serializable {
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