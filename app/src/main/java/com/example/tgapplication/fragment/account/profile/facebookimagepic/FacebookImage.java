package com.example.tgapplication.fragment.account.profile.facebookimagepic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.tgapplication.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FacebookImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_image);


        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/475277016355311/albums",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        /* handle the result */
                        String albumID = null;

                        try{
                            JSONObject json = response.getJSONObject();
                            Log.i("Tag", "onCompleted: "+json);
                            JSONArray jarray = json.getJSONArray("data");
                            for(int i = 0; i < jarray.length(); i++) {
                                JSONObject oneAlbum = jarray.getJSONObject(i);
                                //get albums id
                                if (oneAlbum.getString("name").equals("Profile Pictures")) {
                                    albumID = oneAlbum.getString("id");
                                }
                            }
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

}