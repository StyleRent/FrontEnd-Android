package info.jeonju.stylerent.userdata;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import info.jeonju.stylerent.R;
import info.jeonju.stylerent.activities.ProductEditActivity;
import info.jeonju.stylerent.auth.ApiInterface;
import info.jeonju.stylerent.auth.ChattingModels.RentRequest;
import info.jeonju.stylerent.auth.ChattingModels.RentResponse;
import info.jeonju.stylerent.auth.ProductModels.MyProductAdapterModel;
import info.jeonju.stylerent.auth.RetrofitClient;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Call;

public class ProductAdapter extends BaseAdapter {
    List<MyProductAdapterModel> myProducts;

    private String TOKEN;
    final LayoutInflater inflater;
    private Integer renterId;
    private Boolean rentStatus;
    public Integer productId;

    final Context context;
    private RemoveProductListener removeProductListener;


    public interface RemoveProductListener {
        void onRemoveProduct(int position);
    }


    public ProductAdapter(Context context, List<MyProductAdapterModel> myProducts) {
        this.context = context;
        this.myProducts = myProducts;
        inflater = LayoutInflater.from(context);
    }

    public void setMyProducts(List<MyProductAdapterModel> myProducts){
        this.myProducts = myProducts;
    }



    @Override
    public int getCount() {
        return myProducts.size();
    }

    @Override
    public MyProductAdapterModel getItem(int position) {
        return myProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @SuppressLint("ResourceType")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_self_products, parent, false);
            holder = new ViewHolder();
            holder.productName = convertView.findViewById(R.id.productName);
            holder.productPrice = convertView.findViewById(R.id.productPrice);
            holder.productImage = convertView.findViewById(R.id.productImage);
            holder.productEdit = convertView.findViewById(R.id.productEditBtn);
            holder.productDelete = convertView.findViewById(R.id.productDeleteBtn);
            holder.switchRent = convertView.findViewById(R.id.switchRent); // 스위치 초기화 추가
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MyProductAdapterModel myProductRes = myProducts.get(position);
        holder.productName.setText(myProductRes.getProductName());
        holder.productPrice.setText(formatPrice(myProductRes.getProductPrice()));
        holder.switchRent.setChecked(myProductRes.getRentStatus());

        TOKEN = myProductRes.getTOKEN();

        // productimage
        if (!myProductRes.getProductImage().isEmpty()) {
            byte[] image = Base64.decode(myProductRes.getProductImage(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            holder.productImage.setImageBitmap(bitmap);
        }


        onViewAppear(holder, position);


        holder.productEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductEditActivity.class);
                intent.putExtra("productId", myProducts.get(position).getProductId());
                context.startActivity(intent);
            }
        });
        holder.productDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDeleteProductDialog(holder, position);
            }
        });

        return convertView;
    }

    private String formatPrice(String price) {
        double amount = Double.parseDouble(price);
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        return decimalFormat.format(amount);
    }

    private void appearSwitch(ViewHolder holder){
        holder.switchRent.setOnCheckedChangeListener(null); // 기존 리스너 제거
        holder.productImage.setAlpha(0.2f); // 블러 효과 적용
        holder.switchRent.setVisibility(View.VISIBLE); // 스위치 보이기
    }

    private void hideSwitch(ViewHolder holder){
        holder.switchRent.setOnCheckedChangeListener(null); // 기존 리스너 제거
        holder.productImage.setAlpha(1.0f); // 블러 효과 제거
        holder.switchRent.setVisibility(View.GONE); // 스위치 숨기기
    }


    // 대여 코드
    private void onViewAppear(ViewHolder holder, int position) {

        if(holder.switchRent.isChecked()){
            appearSwitch(holder);
        }else{
            hideSwitch(holder);
        }

        holder.switchRent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    showConfirmationDialog(holder, position);
                }
            }
        });
    }


    // delete confirmation dialog
    private void showConfirmationDeleteProductDialog(ViewHolder holder, int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_confirmation_delete_product, null);
        builder.setView(dialogView);


        Button btnYes = dialogView.findViewById(R.id.btn_yes);
        Button btnNo = dialogView.findViewById(R.id.btn_no);

        AlertDialog alertDialog = builder.create();
        // 상대방이 반납했나요? => yes 즉 이제 다른사람들도 빌려갈수있음
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (removeProductListener != null) {
                    System.out.println("remove product is not null----!!!-----....");
                    if(holder.switchRent.isChecked()){
                        Toast.makeText(context, "먼저 대여 가능으로 바꾸세요!", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }else {
                        removeProductListener.onRemoveProduct(position);
                        alertDialog.dismiss();
                    }
                }
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("반납안함");
                alertDialog.dismiss(); // 다이얼로그 닫기
            }
        });
        alertDialog.show();

    }


    // 대여 가능으로 변경할 때 확인 다이얼로그 표시
    private void showConfirmationDialog(ViewHolder holder, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_confirmation, null);
        builder.setView(dialogView);


        Button btnYes = dialogView.findViewById(R.id.btn_yes);
        Button btnNo = dialogView.findViewById(R.id.btn_no);

        AlertDialog alertDialog = builder.create();

        // 상대방이 반납했나요? => yes 즉 이제 다른사람들도 빌려갈수있음
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("반납");

                RentRequest request = new RentRequest(myProducts.get(position).getRenterId(),myProducts.get(position).getProductId());
                ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(context).create(ApiInterface.class);
                Call<RentResponse> rentResponseApi = apiInterface.returnRent("Bearer" + TOKEN, request);
                rentResponseApi.enqueue(new Callback<RentResponse>() {
                    @Override
                    public void onResponse(Call<RentResponse> call, Response<RentResponse> response) {
                        if(response.body().getMessage() != null){
                            System.out.println("반납 성공");

                            holder.productImage.setAlpha(1.0f); // 블러 효과 제거
                            holder.switchRent.setVisibility(View.INVISIBLE); // 스위치 숨기기
                            holder.switchRent.setOnCheckedChangeListener(null);
                            hideSwitch(holder);
                            alertDialog.dismiss(); // 다이얼로그 닫기

                        }
                        else{
                            System.out.println("반납 실패" + response.body().getError());
                            System.out.println("id -> " + renterId + "product id->  " + productId);
                            appearSwitch(holder);
                            holder.switchRent.setChecked(true);
                        }
                    }
                    @Override
                    public void onFailure(Call<RentResponse> call, Throwable t) {
                    }
                });
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("반납안함");
                holder.switchRent.setChecked(true);
                alertDialog.dismiss(); // 다이얼로그 닫기
            }
        });

        alertDialog.show();
    }

    public void setRemoveProductListener(RemoveProductListener listener) {
        this.removeProductListener = listener;
    }



    private static class ViewHolder {

        TextView productName;
        ImageView productImage;
        TextView productPrice;
        Switch switchRent;

        Button productEdit;
        Button productDelete;
    }
}