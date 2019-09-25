package com.example.tgapplication.fragment.visitor.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tgapplication.MainActivity;
import com.example.tgapplication.R;
import com.example.tgapplication.fragment.trip.module.User;
import com.example.tgapplication.fragment.visitor.BitmapAsync;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class VisitorAdapter extends RecyclerView.Adapter<VisitorAdapter.VisitorViewHolder >
{

    private Context mContext;
    private List<User> mTrip;
    private String uid;
    private int fav_int;
    private List<String> favArray;

    public VisitorAdapter(Context mContext, String uid, List<User> mTrip)
    {
        this.uid=uid;
        this.mContext = mContext;
        this.mTrip = mTrip;
//        this.favArray=favArray;
    }


    @Override
    public VisitorViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_visitor, parent, false);
        return new VisitorViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final VisitorViewHolder holder, int position)
    {

        final User tList = mTrip.get(position);
        if(tList.getImageURL().equalsIgnoreCase("default"))
        {
            Glide.with(mContext).load(R.drawable.ic_broken_image_primary_24dp).apply(new RequestOptions().override(200, 300)).into(holder.mImage);
        }
        else
        {
            Log.i("TAGnnnn", "onBindViewHolder: "+tList.getImageURL());
            new BitmapAsync(holder.mImage,mContext).execute(tList.getImageURL());
//            Glide.with(mContext).load(getBitmapFromURL(tList.getImageURL())).into(holder.mImage);
        }

        holder.mTitle.setText(tList.getName());

        holder.mCity.setVisibility(View.GONE);
//        holder.mDate.setVisibility(View.GONE);

//        holder.mCity.setText(tList.getPlanLocation());
//        holder.mDate.setText(tList.getFrom_to_date());

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setProfileVisit(uid,tList.getId());
//                int fav_id= getFav(favArray,tList.getId());
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
                ((MainActivity)mContext).removeVisit(uid, mTrip.get(position).getId());
//                mTrip.get(position).setFavid(0);
                mTrip.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    private void setProfileVisit(String uid, String id)
    {

        final DatabaseReference visitedRef = FirebaseDatabase.getInstance().getReference("ProfileVisitor")
                .child(id)
                .child(uid);
        visitedRef.child("id").setValue(uid);

    }

    @Override
    public int getItemCount() {
        return mTrip.size();
    }

    class VisitorViewHolder extends RecyclerView.ViewHolder
    {

        ImageView mImage, ivTitle;
        TextView mTitle, mCity, mDate;
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

}