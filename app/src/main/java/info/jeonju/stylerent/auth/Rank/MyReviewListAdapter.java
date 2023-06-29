package info.jeonju.stylerent.auth.Rank;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import info.jeonju.stylerent.R;

import java.util.List;

public class MyReviewListAdapter extends BaseAdapter {

    List<ReviewListModel> myReveiwList;

    final LayoutInflater inflater;

    final Context context;

    public MyReviewListAdapter(List<ReviewListModel> myReveiwList, Context context) {
        this.myReveiwList = myReveiwList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
         return myReveiwList.size();
    }

    @Override
    public ReviewListModel getItem(int position) {
         return myReveiwList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.myreviewlist, parent, false);
            holder = new ViewHolder();
            holder.userNameTextView = convertView.findViewById(R.id.userNameTextView);
            holder.productImageView = convertView.findViewById(R.id.productImageView);
            holder.reviewTextView = convertView.findViewById(R.id.reviewTextView);
            holder.ratingBar = convertView.findViewById(R.id.ratingBar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ReviewListModel myreview = myReveiwList.get(position);


        if (!myreview.getProductImage().isEmpty()) {
            byte[] image = Base64.decode(myreview.getProductImage(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            holder.productImageView.setImageBitmap(bitmap);
        }

        holder.userNameTextView.setText(myreview.getUsername());
        holder.reviewTextView.setText(myreview.getReviewString());
        float rank = (float) myreview.getRank();
        holder.ratingBar.setRating(rank);
        return convertView;
    }


    private static class ViewHolder {
        TextView userNameTextView;
        ImageView productImageView;
        TextView reviewTextView;
        RatingBar ratingBar;
    }
}
