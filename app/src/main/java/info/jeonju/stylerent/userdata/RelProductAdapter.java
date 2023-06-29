package info.jeonju.stylerent.userdata;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import info.jeonju.stylerent.R;

import java.text.DecimalFormat;
import java.util.List;

public class RelProductAdapter extends BaseAdapter {

    private RelProductEventListener relProductEventListener;

    private List<RelProductAdapterModel> relProducts;
    private LayoutInflater inflater;
    private Context context;

    public interface RelProductEventListener {
        void onEventListener(int position);
    }

    public void setRelProductEventListener(RelProductEventListener listener) {
        this.relProductEventListener = listener;
    }


    public RelProductAdapter(Context context, List<RelProductAdapterModel> relProducts) {
        this.context = context;
        this.relProducts = relProducts;
        inflater = LayoutInflater.from(context);
    }

    public void setMyProducts(List<RelProductAdapterModel> relProductAdapterModels) {
        relProducts = relProductAdapterModels;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return relProducts.size();
    }

    @Override
    public RelProductAdapterModel getItem(int position) {
        return relProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_rel_products, parent, false);
            holder = new ViewHolder();
            holder.productName = convertView.findViewById(R.id.productName);
            holder.productPrice = convertView.findViewById(R.id.productPrice);
            holder.productImage = convertView.findViewById(R.id.productImage);
            holder.onClickItemLayout = convertView.findViewById(R.id.onClickItemLayout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RelProductAdapterModel relProductRes = relProducts.get(position);

        holder.onClickItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relProductEventListener.onEventListener(position);
            }
        });

        holder.productName.setText(relProductRes.getProductName());
        holder.productPrice.setText(formatPrice(relProductRes.getProductPrice()));



        // Set the image to the ImageView
        if (relProductRes.getImageBitmaps() != null && !relProductRes.getImageBitmaps().isEmpty()) {
            Bitmap bitmap = relProductRes.getImageBitmaps().get(0);
            holder.productImage.setImageBitmap(bitmap);
        } else {
            // Set a default image if there is no image available
            holder.productImage.setImageResource(R.drawable.ic_user);
        }

        return convertView;
    }
    private String formatPrice(String price) {
        double amount = Double.parseDouble(price);
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        return decimalFormat.format(amount);
    }



    private static class ViewHolder {
        TextView productName;
        LinearLayout onClickItemLayout;
        ImageView productImage;
        TextView productPrice;
    }


}