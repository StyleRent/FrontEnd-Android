package info.jeonju.stylerent.fragments;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import info.jeonju.stylerent.R;
import info.jeonju.stylerent.auth.ApiInterface;
import info.jeonju.stylerent.auth.FavoriteModels.CustomAdapterFav;
import info.jeonju.stylerent.auth.FavoriteModels.FavModel;
import info.jeonju.stylerent.auth.FavoriteModels.FavResponse;
import info.jeonju.stylerent.auth.ImagePathRequest;
import info.jeonju.stylerent.auth.RetrofitClient;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteFragment extends Fragment implements CustomAdapterFav.RemoveFavoriteListener {
    private ListView favListView;
    private List<FavModel> fList = new ArrayList<>();
    private CustomAdapterFav customAdapterFav;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        sharedPreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        favListView = rootView.findViewById(R.id.favListV);
        customAdapterFav = new CustomAdapterFav(getContext(), fList);
        customAdapterFav.setRemoveFavoriteListener(this);
        favListView.setAdapter(customAdapterFav);

        getMyFav(rootView);

        return rootView;
    }

    private void getImage(View view) {
        List<Bitmap> bitmapList = new ArrayList<>();

        for (FavModel f : fList) {
            getImageByPath(f.getProductImage(), new BitmapCallback() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap) {
                    System.out.println("Bitmap --------->>" + bitmap.toString());
                    bitmapList.add(bitmap);

                    if (bitmapList.size() == fList.size()) {
                        // All bitmaps are loaded, add them to the FavModel objects
                        for (int i = 0; i < fList.size(); i++) {
                            fList.get(i).setImageBitmap(bitmapList.get(i));
                            System.out.println("change bitmap");
                        }

                        // Notify the adapter that the data has changed
                        customAdapterFav.setFavList(fList);
                        customAdapterFav.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    // Handle the failure here
                }
            });
        }
        System.out.println("finish get image request");

        System.out.println("finish get image request" + bitmapList.size());
    }

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

    private void getMyFav(View view) {
        String TOKEN = sharedPreferences.getString("TOKEN", null);

        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(getContext()).create(ApiInterface.class);
        Call<List<FavModel>> userProfileCall = apiInterface.getMyFav("Bearer " + TOKEN);
        userProfileCall.enqueue(new Callback<List<FavModel>>() {
            @Override
            public void onResponse(Call<List<FavModel>> call, Response<List<FavModel>> response) {
                if (response.isSuccessful()) {
                    fList = response.body();
                    for (FavModel f : fList) {
                        System.out.println("Items --------->" + f.getProductName());
                    }

                    getImage(view);
                }
            }

            @Override
            public void onFailure(Call<List<FavModel>> call, Throwable t) {
                Log.e(TAG, "Failed to get user's favorite items: " + t.getMessage());
            }
        });
    }

    @Override
    public void onRemoveFavorite(int position) {
        FavModel itemToRemove = fList.get(position);

        String TOKEN = sharedPreferences.getString("TOKEN", null);
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(getContext()).create(ApiInterface.class);
        Call<FavResponse> userProfileCall = apiInterface.deleteFav("Bearer " + TOKEN, itemToRemove.getProductId());
        userProfileCall.enqueue(new Callback<FavResponse>() {
            @Override
            public void onResponse(Call<FavResponse> call, Response<FavResponse> response) {
                if (response.isSuccessful()) {
                    if(response.body().getError() == null){
                        fList.remove(position);
                        customAdapterFav.setFavList(fList);
                        System.out.println("removeddddd");
                        customAdapterFav.notifyDataSetChanged();
                    }

                } else {
                    // Handle API request failure
                }
            }

            @Override
            public void onFailure(Call<FavResponse> call, Throwable t) {
                // Handle network failure
            }
        });
    }
}