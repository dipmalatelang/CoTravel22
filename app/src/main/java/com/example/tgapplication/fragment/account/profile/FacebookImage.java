package com.example.tgapplication.fragment.account.profile;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tgapplication.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FacebookImage extends AppCompatActivity {

    private ArrayList<Images> lstFBImages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_facebook_image);

        if(AccessToken.getCurrentAccessToken()!=null)
        {
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
                                        for (int i = 0; i < jaData.length(); i++) {//find no. of album using jaData.length()
                                            JSONObject joAlbum = jaData.getJSONObject(i); //convert perticular album into JSONObject
                                            GetFacebookImages(joAlbum.optString("id")); //find Album ID and get All Images from album
                                        }
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
        else {
            Toast.makeText(this, "First login with facebook", Toast.LENGTH_SHORT).show();
        }




//        new GraphRequest(
//                AccessToken.getCurrentAccessToken(),
//                "/475277016355311/albums",
//                null,
//                HttpMethod.GET,
//                new GraphRequest.Callback() {
//                    public void onCompleted(GraphResponse response) {
//                        /* handle the result */
//                        String albumID = null;
//
//                        try{
//                            JSONObject json = response.getJSONObject();
//                            Log.i("Tag", "onCompleted: "+json);
//                            JSONArray jarray = json.getJSONArray("data");
//                            for(int i = 0; i < jarray.length(); i++) {
//                                JSONObject oneAlbum = jarray.getJSONObject(i);
//                                //get albums id
//                                if (oneAlbum.getString("name").equals("Profile Pictures")) {
//                                    albumID = oneAlbum.getString("id");
//                                }
//                            }
//                        }
//                        catch(JSONException e){
//                            e.printStackTrace();
//                        }
//                    }
//                }
//        ).executeAsync();
    }


    public void GetFacebookImages(final String albumId) {
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
                                    Log.i("TAG", "onCompleted: "+jaData.getJSONObject(0).getJSONArray("images").getJSONObject(0).getString("source"));
                                    lstFBImages = new ArrayList<>();
                                    for (int i = 0; i < jaData.length(); i++)//Get no. of images
                                         {
                                        JSONObject joAlbum = jaData.getJSONObject(i);
                                    JSONArray jaImages = joAlbum.getJSONArray("images");

                                    if (jaImages.length() > 0) {
                                       Images objImages = new Images();//Images is custom class with string url field
                                        objImages.setImage_Url(jaImages.getJSONObject(0).getString("source"));
                                        lstFBImages.add(objImages);//lstFBImages is Images object array
                                    }

                            }
                                    Log.i("TAG", "onCompleted: "+lstFBImages.size());
                                    for(int j=0;j<lstFBImages.size();j++)
                                    {
                                        Log.i("TAG", "onCompleted: "+lstFBImages.get(j).getImage_Url());
                                    }

                                }


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
        String image_Url;

        public String getImage_Url() {
            return image_Url;
        }

        public void setImage_Url(String image_Url) {
            this.image_Url = image_Url;
        }
    }
}