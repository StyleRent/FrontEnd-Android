package info.jeonju.stylerent.auth.ChattingModels;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import info.jeonju.stylerent.R;
import info.jeonju.stylerent.activities.MessageActivity;

import java.util.List;

//CustomAdapter is for creating views for each item in the Listview

public class CustomAdapter extends BaseAdapter {
    List<MyChatResponse> myChat;
    final LayoutInflater inflater;

    final Context context;

    public CustomAdapter(Context context, List<MyChatResponse> myChat) {
        this.context = context;
        this.myChat = myChat;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return myChat.size();
    }

    @Override
    public MyChatResponse getItem(int position) {
        return myChat.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setChatList(List<MyChatResponse> myChat) {
        this.myChat = myChat;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_user, parent, false);
            holder = new ViewHolder();
            holder.userNameTextView = convertView.findViewById(R.id.userNameTextView);
            holder.userImageView = convertView.findViewById(R.id.userImageView);
            holder.messageHistoryLayout = convertView.findViewById(R.id.messageHistoryLayout);
            holder.messageTextView = convertView.findViewById(R.id.messageTextView);
            holder.productImageView = convertView.findViewById(R.id.productImageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MyChatResponse mychatResponse = myChat.get(position);

        //user image base64
        if (mychatResponse.getReceiverImage() != null  ) {
            byte[] image = Base64.decode(mychatResponse.getReceiverImage(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            holder.userImageView.setImageBitmap(bitmap);
        }

        // productimage
        if (!mychatResponse.getProductImage().isEmpty()) {
            byte[] image = Base64.decode(mychatResponse.getProductImage(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            holder.productImageView.setImageBitmap(bitmap);
        }


        holder.userNameTextView.setText(mychatResponse.getReceiverName());
        holder.messageTextView.setText(myChat.get(position).getProductName());


        holder.messageHistoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("messageId", myChat.get(position).getMessageId());
                intent.putExtra("receiverId", myChat.get(position).getReceiverId());
                intent.putExtra("productId", myChat.get(position).getProductId());
                intent.putExtra("rentStatus", myChat.get(position).getRentStatus());
                intent.putExtra("isRentedToMe", myChat.get(position).getRentedToMe());
                intent.putExtra("myChat", myChat.get(position).getMyChat());
                intent.putExtra("productImage", myChat.get(position).getProductImage());
                intent.putExtra("productName", myChat.get(position).getProductName());
                intent.putExtra("productPrice", myChat.get(position).getProductPrice());
                context.startActivity(intent);
            }
        });

        return convertView;
    }



    private static class ViewHolder {
        TextView userNameTextView;
        ImageView userImageView;
        TextView messageTextView;
        LinearLayout messageHistoryLayout;
        ImageView productImageView;
    }
}
