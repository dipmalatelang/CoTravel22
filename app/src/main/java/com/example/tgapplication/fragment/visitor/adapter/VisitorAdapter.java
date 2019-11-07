package com.example.tgapplication.fragment.visitor.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.tgapplication.MainActivity;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.visitor.UserImg;

import java.util.List;

public class VisitorAdapter extends RecyclerView.Adapter<VisitorAdapter.VisitorViewHolder >
{

    private Context mContext;
    private List<UserImg> mTrip;
    private String uid;


    public VisitorAdapter(Context mContext, String uid, List<UserImg> mTrip, VisitorInterface listener)
    {
        this.uid=uid;
        this.mContext = mContext;
        this.mTrip = mTrip;
        this.listener=listener;
//        this.favArray=favArray;
    }


    @NonNull
    @Override
    public VisitorViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_visitor, parent, false);
        return new VisitorViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final VisitorViewHolder holder, int position)
    {

        final UserImg tList = mTrip.get(position);
  /*      if(tList.getUser().getImageURL().equalsIgnoreCase("default"))
        {
            Glide.with(mContext).load(R.drawable.ic_broken_image_primary_24dp).placeholder(R.drawable.ic_broken_image_primary_24dp).apply(new RequestOptions().override(200, 300)).into(holder.mImage);
        }
        else
        {*/
          /*  for(int i=0;i<mTrip.size();i++)
            {
                Log.i("TAG", "onBindViewHolder: "+mTrip.get(i).getUser().getId()+" "+mTrip.get(i).getUser().getName()+" "+"default");
            }
            Glide.with(mContext).load("default").placeholder(R.drawable.ic_broken_image_primary_24dp).apply(new RequestOptions().override(200, 300)).into(holder.mImage);
*/
         /*   Log.i("TAGnnnn", "onBindViewHolder: "+tList.getUser().getImageURL());
            new BitmapAsync(holder.mImage,mContext).execute(tList.getUser().getImageURL());*/
//            Glide.with(mContext).load(getBitmapFromURL(tList.getUser().getImageURL())).into(holder.mImage);
//        }

        if(tList.getUser().getGender().equalsIgnoreCase("Female")||tList.getUser().getGender().equalsIgnoreCase("Girl"))
        {
            if (tList.getUser().getAccount_type() == 1) {
                Glide.with(mContext).asBitmap().load(mTrip.get(position).getPictureUrl())
                        .centerCrop()
                        .override(450, 600)
                        .listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
//                            holder.progressBar.setVisibility(View.GONE);
                                holder.mImage.setImageResource(R.drawable.no_photo_female);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//                            holder.progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                holder.mImage.setImageBitmap(resource);
                            }
                        });
            }
            else {
                Glide.with(mContext).load(R.drawable.hidden_photo_female_thumb)
                        .centerCrop()
                        .override(450, 600)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                holder.progressBar.setVisibility(View.GONE);
                                holder.mImage.setImageResource(R.drawable.hidden_photo_female_thumb);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                holder.progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(holder.mImage);
            }
        }
        else {
            if (tList.getUser().getAccount_type() == 1) {
                Glide.with(mContext).asBitmap().load(mTrip.get(position).getPictureUrl())
                        .centerCrop()
                        .override(450, 600)
                        .listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
//                            holder.progressBar.setVisibility(View.GONE);
                                holder.mImage.setImageResource(R.drawable.no_photo_male);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//                            holder.progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                holder.mImage.setImageBitmap(resource);
                            }
                        });
            }
            else {
                Glide.with(mContext).load(R.drawable.hidden_photo_male_thumb)
                        .centerCrop()
                        .override(450, 600)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                holder.progressBar.setVisibility(View.GONE);
                                holder.mImage.setImageResource(R.drawable.hidden_photo_male_thumb);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                holder.progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(holder.mImage);
            }
        }

//        Glide.with(mContext).load(tList.getPictureUrl()).placeholder(R.drawable.ic_broken_image_primary_24dp).centerCrop().into(holder.mImage);
//        holder.mImage.setImageResource(tList.getPictureUrl());
        holder.mTitle.setText(tList.getUser().getName());

        holder.mCity.setVisibility(View.GONE);
//        holder.mDate.setVisibility(View.GONE);

//        holder.mCity.setText(tList.getUser().getPlanLocation());
//        holder.mDate.setText(tList.getUser().getFrom_to_date());

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setProfileVisit(uid,tList.getUser().getId());
                listener.setData(tList,position);
//                int fav_id= getFav(favArray,tList.getUser().getId());
//                Log.i("Got Needed Value"," "+fav_id);
                /*Intent mIntent = new Intent(mContext, DetailActivity.class);
                mIntent.putExtra("MyObj", tList);
//                mIntent.putExtra("FavId",fav_id);
                mContext.startActivity(mIntent);*/
            }
        });


        holder.ivTitle.setImageDrawable(mContext.getResources().getDrawable(R.drawable.delete));

        holder.ivTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)mContext).removeVisit(uid, mTrip.get(position).getUser().getId());
//                mTrip.get(position).setFavid(0);
                mTrip.remove(position);
                notifyDataSetChanged();
            }
        });
    }





    @Override
    public int getItemCount() {
        return mTrip.size();
    }

    class VisitorViewHolder extends RecyclerView.ViewHolder
    {

        ImageView mImage, ivTitle;
        TextView mTitle, mCity;
        CardView mCardView;

        VisitorViewHolder(View itemView)
        {
            super(itemView);

            mImage = itemView.findViewById(R.id.ivImage);
            ivTitle=itemView.findViewById(R.id.ivTitle);
            mTitle = itemView.findViewById(R.id.tvTitle);
            mCity = itemView.findViewById(R.id.tvCity);
//            mDate = itemView.findViewById(R.id.tvDate);
            mCardView = itemView.findViewById(R.id.cardview);
        }
    }

/*    private class BitmapAsync extends AsyncTask<String, Void, Bitmap>
    {
        ImageView imageView;

        public BitmapAsync(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... args) {
            try {
                URL url = new URL(args[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap yourBitmap = BitmapFactory.decodeStream(input);
                Log.i("TAGi", "doInBackground: "+yourBitmap.getWidth()+" "+yourBitmap.getHeight());
                yourBitmap = Bitmap.createScaledBitmap(yourBitmap,(300), (300), true);
                return yourBitmap;

            }catch (NullPointerException n){
                return null;
            }
            catch (IOException e) {
                // Log exception
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            Glide.with(mContext).load(getRoundedCornerBitmap(bitmap)).into(imageView);
        }
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 12;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }*/

    VisitorInterface listener;
    public interface VisitorInterface{
        void setProfileVisit(String uid, String id);
        void setData(UserImg mTrip,int position);
    }
}