package com.example.tgapplication.fragment.account;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tgapplication.BaseFragment;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.trip.EditProfileActivity;
import com.example.tgapplication.fragment.trip.module.TripList;
import com.example.tgapplication.fragment.trip.module.User;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;


public class AccountFragment extends BaseFragment implements View.OnClickListener {


    private ImageView mTrip;
    TextView tvName, tvAge, mCity, tvSex, tvNatioanlity, tvLanguage, tvHeight, tvBodyType, tvEyes, tvHairs,
            tvLooking, tvWantToVisit, tvPlannedtrip, tvDate;
    ImageView  iv_fav, iv_profile_visitor, iv_my_fav;
    private TextView iv_profile_edit;
    private FirebaseUser fuser;
    private LinearLayout detail_list, image_list;
    private Button btn_details, btn_images;
    private List<TripList> myFavArray = new ArrayList<>();
    private List<TripList> myVisitArray = new ArrayList<>();
    private ConstraintLayout fragment_acc_constraintLayout;

    private static final int PICK_IMAGE_REQUEST = 234;


    //    Gallery simpleGallery;
//    CustomGalleryAdapter customGalleryAdapter;
    TripList tripL;
    List<User> userList=new ArrayList<>();
    List<TripList> listTrip;
    List<String> favArray;
    List<String> visitArray;

    @BindView(R.id.labelCity)
    LinearLayout labelCity;
    @BindView(R.id.labelSex)
    LinearLayout labelSex;
    @BindView(R.id.labelNationality)
    LinearLayout labelNationality;
    @BindView(R.id.labelLanguage)
    LinearLayout labelLanguage;
    @BindView(R.id.labelHeight)
    LinearLayout labelHeight;
    @BindView(R.id.labelBodyType)
    LinearLayout labelBodyType;
    @BindView(R.id.labelEyes)
    LinearLayout labelEyes;
    @BindView(R.id.labelHairs)
    LinearLayout labelHairs;
    @BindView(R.id.labelLooking)
    LinearLayout labelLooking;
    @BindView(R.id.labelWantToVisit)
    LinearLayout labelWantToVisit;
    @BindView(R.id.labelPlannedtrip)
    LinearLayout labelPlannedtrip;
    @BindView(R.id.tv_my_pic)
    TextView ivMyPic;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private ArrayList<Upload> uploads;
    private MyAdapter adapter;
    private Uri filePath;
    private String getDownloadImageUrl;
    private StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this, v);

        SharedPreferences prefs = getActivity().getSharedPreferences("Filter_TripList", MODE_PRIVATE);

        String str_city = prefs.getString("str_city", "not_defined");//"No name defined" is the default value.
        String str_lang = prefs.getString("str_lang", "not_defined"); //0 is the default value.

        String str_look = prefs.getString("str_look", "not_defined");
        String str_from = prefs.getString("str_from", "not_defined");
        String str_to = prefs.getString("str_to", "not_defined");
        String str_visit = prefs.getString("str_visit", "not_defined");


//        if(str_city.equalsIgnoreCase("not_defined"))
//        {
//            tripList();
//        }
//        else{
//            filterTripList(str_city);
//        }

//        simpleGallery = (Gallery) v.findViewById(R.id.simpleGallery);
        mTrip = v.findViewById(R.id.ivImage);
        tvName = v.findViewById(R.id.tvName);
        tvAge = v.findViewById(R.id.tvAge);
        mCity = v.findViewById(R.id.tvCity);
        detail_list = v.findViewById(R.id.detail_list);
        image_list = v.findViewById(R.id.image_list);

        btn_details = v.findViewById(R.id.btn_details);
        btn_images = v.findViewById(R.id.btn_images);

//        btn_send_msg = v.findViewById(R.id.btn_send_msg);
        iv_profile_edit = v.findViewById(R.id.iv_profile_edit);
//        iv_profile_visitor = v.findViewById(R.id.iv_profile_visitor);
//        iv_my_fav = v.findViewById(R.id.iv_my_fav);
        iv_fav = v.findViewById(R.id.iv_fav);
        tvSex = v.findViewById(R.id.tvSex);
        tvNatioanlity = v.findViewById(R.id.tvNatioanlity);
        tvLanguage = v.findViewById(R.id.tvLanguage);
        tvHeight = v.findViewById(R.id.tvHeight);
        tvBodyType = v.findViewById(R.id.tvBodyType);
        tvEyes = v.findViewById(R.id.tvEyes);
        tvHairs = v.findViewById(R.id.tvHairs);
        tvLooking = v.findViewById(R.id.tvLooking);
        tvWantToVisit = v.findViewById(R.id.tvWantToVisit);
        tvPlannedtrip = v.findViewById(R.id.tvPlannedtrip);
        tvDate = v.findViewById(R.id.tvDate);
        fragment_acc_constraintLayout=v.findViewById(R.id.fragment_acc_constraintLayout);


        fuser = FirebaseAuth.getInstance().getCurrentUser();

        iv_fav.setOnClickListener(this);
//        btn_send_msg.setOnClickListener(this);
        iv_profile_edit.setOnClickListener(this);
//        iv_my_fav.setOnClickListener(this);
        //iv_profile_visitor.setOnClickListener(this);

        btn_details.setOnClickListener(this);
        btn_images.setOnClickListener(this);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(mGridLayoutManager);

        progressDialog = new ProgressDialog(getActivity());

        uploads = new ArrayList<>();

//displaying progress dialog while fetching images
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        storageReference = FirebaseStorage.getInstance().getReference();
        PicturesInstance.child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
//dismissing the progress dialog
                progressDialog.dismiss();

//iterating through all the values in database
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    uploads.add(upload);
                }
//creating adapter
                adapter = new MyAdapter(getActivity(), fuser.getUid(), uploads, new MyAdapter.PhotoInterface() {
                    @Override
                    public void setProfilePhoto(String id, String previousValue,int pos) {
                        Toast.makeText(getActivity(), ""+id, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void removePhoto(String id) {
                        Toast.makeText(getActivity(), ""+id, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void setPhotoAsPrivate(String id) {
//                        Toast.makeText(getActivity(), ""+id, Toast.LENGTH_SHORT).show();
                    }
                });

//adding adapter to recyclerview
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

//        Log.i("Got Data back ",""+fav_int);
//        if(fav_int)
//        {
//            iv_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_fav_remove));
//            iv_fav.setTag("ic_action_fav_remove");
//        }
//        else {
//            iv_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_fav_add));
//            iv_fav.setTag("ic_action_fav_add");
//        }


//        btn_send_msg.setVisibility(View.GONE);
        iv_fav.setVisibility(View.GONE);
        iv_profile_edit.setVisibility(View.VISIBLE);
//                iv_my_fav.setVisibility(View.VISIBLE);
//                iv_profile_visitor.setVisibility(View.VISIBLE);
        ivMyPic.setVisibility(View.VISIBLE);



      /*  if (getActivity().getIntent() != null) {
            if (getActivity().getIntent().getSerializableExtra("MyObj") == null) {
                userList = (List<User>) getActivity().getIntent().getSerializableExtra("MyDataObj");
                Log.i("My Name", userList.get(i).getName());
                listTrip = (List<TripList>) getActivity().getIntent().getSerializableExtra("ListTrip");
                favArray = (List<String>) getActivity().getIntent().getSerializableExtra("ListFav");
                visitArray = (List<String>) getActivity().getIntent().getSerializableExtra("ListVisit");



                btn_send_msg.setVisibility(View.GONE);
                iv_fav.setVisibility(View.GONE);
//                iv_profile_edit.setVisibility(View.VISIBLE);
//                iv_my_fav.setVisibility(View.VISIBLE);
//                iv_profile_visitor.setVisibility(View.VISIBLE);
                ivMyPic.setVisibility(View.VISIBLE);


                setDetails(userList.get(i).getName(), userList.get(i).getGender(), userList.get(i).getAge(), userList.get(i).getLook(), userList.get(i).getLocation(), userList.get(i).getNationality(), userList.get(i).getLang(), userList.get(i).getHeight(), userList.get(i).getBody_type(), userList.get(i).getEyes(), userList.get(i).getHair(), userList.get(i).getVisit(), "", "", userList.get(i).getImageURL());
            } else {
                tripL = (TripList) getActivity().getIntent().getSerializableExtra("MyObj");
                int faValue = getActivity().getIntent().getIntExtra("FavId", 0);

                btn_send_msg.setVisibility(View.VISIBLE);
                iv_fav.setVisibility(View.VISIBLE);
//                iv_profile_edit.setVisibility(View.GONE);
//                iv_my_fav.setVisibility(View.GONE);
//                iv_profile_visitor.setVisibility(View.GONE);
                ivMyPic.setVisibility(View.GONE);

                Log.i("Fav Value", " " + faValue);
                if (faValue == 1) {
                    iv_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_fav_remove));
                    iv_fav.setTag("ic_action_fav_remove");
                } else {
                    iv_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_fav_add));
                    iv_fav.setTag("ic_action_fav_add");
                }
                setDetails(tripL.getName(), tripL.getGender(), tripL.getAge(), tripL.getLook(), tripL.getUserLocation(), tripL.getNationality(),
                        tripL.getLang(), tripL.getHeight(), tripL.getBody_type(), tripL.getEyes(), tripL.getHair(), tripL.getVisit(), tripL.getPlanLocation(), tripL.getFrom_to_date(), tripL.getImageUrl());
//            if(tripL==null)
//            {
//                ArrayList<User> userL=getIntent().getSerializableExtra()
//            }


//            mDate.setText(tripL.getFrom_to_date());
            }
        }*/

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        myList(fuser);
    }

    private void setDetails(String name, String gender, String age, ArrayList<String> look, String userLocation, String nationality, String lang, String height, String body_type, String eyes, String hair, String visit, String planLocation, String from_to_date, String imageUrl) {

        String str_look = null;

        if (name != null && !name.equalsIgnoreCase("")) {
            tvName.setText(name);
        }

        if (gender != null && !gender.equalsIgnoreCase("")) {
            tvSex.setText(gender);
            labelSex.setVisibility(View.VISIBLE);
        } else {
            labelSex.setVisibility(View.GONE);
        }

        if (age != null && !age.equalsIgnoreCase("")) {
            tvAge.setText(age + " years");
        }

        if(look!=null)
        {
            for (int j = 0; j < look.size(); j++) {
                if (str_look != null) {
                    str_look += ", " + look.get(j);
                } else {
                    str_look = look.get(j);
                }

            }

            if (str_look != null && !str_look.equalsIgnoreCase("")) {
                tvLooking.setText(str_look);
                labelLooking.setVisibility(View.VISIBLE);
            } else {
                labelLooking.setVisibility(View.GONE);
            }
        }




        if (userLocation != null && !userLocation.equalsIgnoreCase("")) {
            mCity.setText(userLocation);
            labelCity.setVisibility(View.VISIBLE);
        } else {
            labelCity.setVisibility(View.GONE);
        }

        if (nationality != null && !nationality.equalsIgnoreCase("")) {
            tvNatioanlity.setText(nationality);
            labelNationality.setVisibility(View.VISIBLE);
        } else {
            labelNationality.setVisibility(View.GONE);
        }

        if (lang != null && !lang.equalsIgnoreCase("")) {
            tvLanguage.setText(lang);
            labelLanguage.setVisibility(View.VISIBLE);
        } else {
            labelLanguage.setVisibility(View.GONE);
        }

        if (height != null && !height.equalsIgnoreCase("")) {
            tvHeight.setText(height);
            labelHeight.setVisibility(View.VISIBLE);
        } else {
            labelHeight.setVisibility(View.GONE);
        }

        if (body_type != null && !body_type.equalsIgnoreCase("")) {
            tvBodyType.setText(body_type);
            labelBodyType.setVisibility(View.VISIBLE);
        } else {
            labelBodyType.setVisibility(View.GONE);
        }

        if (eyes != null && !eyes.equalsIgnoreCase("")) {
            tvEyes.setText(eyes);
            labelEyes.setVisibility(View.VISIBLE);
        } else {
            labelEyes.setVisibility(View.GONE);
        }

        if (hair != null && !hair.equalsIgnoreCase("")) {
            tvHairs.setText(hair);
            labelHairs.setVisibility(View.VISIBLE);
        } else {
            labelHairs.setVisibility(View.GONE);
        }

        if (visit != null && !visit.equalsIgnoreCase("")) {
            tvWantToVisit.setText(visit);
            labelWantToVisit.setVisibility(View.VISIBLE);
        } else {
            labelWantToVisit.setVisibility(View.GONE);
        }

        if (planLocation != null && !planLocation.equalsIgnoreCase("")) {
            tvPlannedtrip.setText(planLocation);
            tvDate.setText(from_to_date);
            labelPlannedtrip.setVisibility(View.VISIBLE);
        } else {
            labelPlannedtrip.setVisibility(View.GONE);
        }

        if(getActivity()!=null)
        {


                Glide.with(getActivity()).load(imageUrl).placeholder(R.drawable.ic_broken_image_primary_24dp).into(mTrip);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_details:
                detail_list.setVisibility(View.VISIBLE);
                image_list.setVisibility(View.GONE);
                break;

            case R.id.btn_images:
                detail_list.setVisibility(View.GONE);
                image_list.setVisibility(View.VISIBLE);
                break;




            case R.id.iv_profile_edit:
                Intent msgIntent = new Intent(getActivity(), EditProfileActivity.class);
//              msgIntent.putExtra("nextActivity", "profileEdit");
                startActivity(msgIntent);
                break;

//            case R.id.iv_profile_visitor:
//                getMyVisit();
//                break;

//            case R.id.iv_my_fav:
//                getMyFav();
//                break;

            case R.id.iv_fav:
                Log.i("GotTag", iv_fav.getTag().toString());
                if (iv_fav.getTag().toString().equalsIgnoreCase("ic_action_fav_add")) {
                    setFav(fuser.getUid(), tripL.getId());
                    iv_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_fav_remove));
                    iv_fav.setTag("ic_action_fav_remove");
                } else if (iv_fav.getTag().toString().equalsIgnoreCase("ic_action_fav_remove")) {
                    removeFav(fuser.getUid(), tripL.getId());
                    iv_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_fav_add));
                    iv_fav.setTag("ic_action_fav_add");
                }

                break;
        }
    }

    private void getMyVisit() {
        myVisitArray.clear();
        Log.i("VisitArray", "" + visitArray.size());
        for (int i = 0; i < listTrip.size(); i++) {
            for (int j = 0; j < visitArray.size(); j++) {
                Log.i("Compare", listTrip.get(i).getId() + " ==> " + visitArray.get(j));
                if (listTrip.get(i).getId().equalsIgnoreCase(visitArray.get(j))) {
                    myVisitArray.add(listTrip.get(i));
                    Log.i("Got In Here ", listTrip.get(i).getId());
                }
            }

        }
//        Intent mIntent = new Intent(this, ProfileVisitorActivity.class);
//        mIntent.putExtra("myVisit", (Serializable) myVisitArray);
//        mIntent.putExtra("ListFav",(Serializable) favArray);
//        startActivity(mIntent);
    }

    private void getMyFav() {
        myFavArray.clear();
        for (int i = 0; i < listTrip.size(); i++) {
            for (int j = 0; j < favArray.size(); j++) {
                Log.i("Compare", listTrip.get(i).getId() + " ==> " + favArray.get(j));
                if (listTrip.get(i).getId().equalsIgnoreCase(favArray.get(j))) {
                    myFavArray.add(listTrip.get(i));
                    Log.i("Got In Here ", listTrip.get(i).getId());
                }
            }

        }
        Log.i("Checking Size",myFavArray.size()+" "+favArray.size());
//        Intent mIntent = new Intent(this, ProfileVisitorActivity.class);
//        mIntent.putExtra("myFav", (Serializable) myFavArray);
//        mIntent.putExtra("ListFav",(Serializable) favArray);
//        startActivity(mIntent);
    }







    public void myList(FirebaseUser fuser) {
        // any way you managed to go the node that has the 'grp_key'

        UsersInstance.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        userList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            User user = snapshot.getValue(User.class);
                            if (user != null && user.getId().equalsIgnoreCase(fuser.getUid())) {
                                userList.add(user);
                            }
                        }
                        if(userList.size()>0)
                        {
                            for(int i=0;i<userList.size();i++)
                            setDetails(userList.get(i).getName(), userList.get(i).getGender(), userList.get(i).getAge(), userList.get(i).getLook(), userList.get(i).getLocation(), userList.get(i).getNationality(), userList.get(i).getLang(), userList.get(i).getHeight(), userList.get(i).getBody_type(), userList.get(i).getEyes(), userList.get(i).getHair(), userList.get(i).getVisit(), "", "", "default");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    @OnClick(R.id.tv_my_pic)
    public void onViewClicked() {
        showFileChooser();
    }

    private void showFileChooser() {
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
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(Uri filePath) {
//checking if file is available
        Log.i("Result",""+filePath);
        if (filePath != null) {
//displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
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
                            snackBar(fragment_acc_constraintLayout,"File Uploaded ");

                            String uploadId = PicturesInstance.child(fuser.getUid()).push().getKey();
                            sRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if(task.isSuccessful())
                                    {
                                        getDownloadImageUrl = task.getResult().toString();
                                        Log.i("FirebaseImages",getDownloadImageUrl);

//creating the upload object to store uploaded image details
                                        Upload upload = new Upload(uploadId,"Image", getDownloadImageUrl,2);

//adding an upload to firebase database

                                        PicturesInstance.child(fuser.getUid()).child(uploadId).setValue(upload);
                                    }
                               else {
                                   snackBar(fragment_acc_constraintLayout,task.getException().getMessage());
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
            snackBar(fragment_acc_constraintLayout,"Please Select a Image");
        }
    }

}
