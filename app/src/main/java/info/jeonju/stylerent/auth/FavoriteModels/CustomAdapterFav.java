package info.jeonju.stylerent.auth.FavoriteModels;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import info.jeonju.stylerent.R;

import java.util.List;

public class CustomAdapterFav extends BaseAdapter {
    private List<FavModel> favList;
    private LayoutInflater inflater;

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;
    private RemoveFavoriteListener removeFavoriteListener;
    public interface RemoveFavoriteListener {
        void onRemoveFavorite(int position);
    }
    public CustomAdapterFav(Context context, List<FavModel> favList) {
        this.favList = favList;
        inflater = LayoutInflater.from(context);
    }
    public void setRemoveFavoriteListener(RemoveFavoriteListener listener) {
        this.removeFavoriteListener = listener;
    }

    public void setFavList(List<FavModel> favList){
        this.favList = favList;
    }


    @Override
    public int getCount() {
        return favList.size();
    }

    @Override
    public FavModel getItem(int position) {
        return favList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fav_item, parent, false);
            holder = new ViewHolder();

            holder.productImage = convertView.findViewById(R.id.productImage);
            holder.productPrice = convertView.findViewById(R.id.productPrice);
            holder.productTitle = convertView.findViewById(R.id.productTitle);
            holder.removefavBtn = convertView.findViewById(R.id.removefav);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        FavModel fav = favList.get(position);

        holder.productImage.setImageBitmap(fav.getImageBitmap());
        holder.productTitle.setText(fav.getProductName());
        holder.productPrice.setText(fav.getProductPrice());

        holder.removefavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (removeFavoriteListener != null) {
                    removeFavoriteListener.onRemoveFavorite(position);
                }
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView productPrice;
        ImageView productImage;
        TextView productTitle;

        ImageButton removefavBtn;
    }


}