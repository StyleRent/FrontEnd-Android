
package info.jeonju.stylerent.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import info.jeonju.stylerent.R;
import info.jeonju.stylerent.auth.ApiInterface;
import info.jeonju.stylerent.auth.ChattingModels.CustomAdapter;
import info.jeonju.stylerent.auth.ChattingModels.MyChatResponse;
import info.jeonju.stylerent.auth.ImagePathRequest;
import info.jeonju.stylerent.auth.RetrofitClient;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChattingFragment extends Fragment {

    // Define the BitmapCallback interface here


    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;

    private ListView userListView;
    List<MyChatResponse> chatList = new ArrayList<>();
    private CustomAdapter customAdapter;


    public ChattingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = requireActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chatting, container,false);
        userListView = rootView.findViewById(R.id.userListView);
        // get my chat request -> GET request
        getMyChats();

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getMyChats();
    }


    // get my chats
    private void getMyChats(){
        String TOKEN = sharedPreferences.getString("TOKEN", null);
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(getContext()).create(ApiInterface.class);
        Call<List<MyChatResponse>> getNearByDataRequest = apiInterface.getMyChat("Bearer " + TOKEN);
        getNearByDataRequest.enqueue(new Callback<List<MyChatResponse>>() {
            @Override
            public void onResponse(Call<List<MyChatResponse>> call, Response<List<MyChatResponse>> response) {
                chatList = response.body();


                //create the custom adapter
                customAdapter = new CustomAdapter(getContext(), chatList);

                userListView.setAdapter(customAdapter);

//                getImage(rootView);


            }

            @Override
            public void onFailure(Call<List<MyChatResponse>> call, Throwable t) {

            }

        });
    }

//    private void getImage(View view) {
//        List<List<Bitmap>> bitmapLists = new ArrayList<>();
//
//        for (MyChatResponse p: chatList) {
//            List<ProductImage> productImages = p.getImagePath();
//            List<Bitmap> bitmapList = new ArrayList<>();
//
//            for (ProductImage image : productImages) {
//                getImageByPath(image.getPath(), new BitmapCallback() {
//                    @Override
//                    public void onBitmapLoaded(Bitmap bitmap) {
//                        bitmapList.add(bitmap);
//
//                        if (bitmapList.size() == productImages.size()) {
//                            // All bitmaps are loaded, add them to the MyChatResponse object
//                            p.setImageBitmaps(bitmapList);
//
//                            // Notify the adapter that the data has changed
//                            customAdapter.setChatList(chatList);
//                            customAdapter.notifyDataSetChanged();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t) {
//                        // Handle the failure here
//                    }
//                });
//            }
//
//            bitmapLists.add(bitmapList);
//        }
//    }


    private void getImageByPath(String imagePath, BitmapCallback callback) {
        String TOKEN = sharedPreferences.getString("TOKEN", null);
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(getContext()).create(ApiInterface.class);
        ImagePathRequest imagePathRequest = new ImagePathRequest(imagePath);
        Call<ResponseBody> userProfileCall = apiInterface.getImageByPath("Bearer " + TOKEN, imagePathRequest);

        userProfileCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {

                    InputStream inputStream = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    callback.onBitmapLoaded(bitmap);

                } else {
                    callback.onFailure(new Exception("Failed to load image"));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

}