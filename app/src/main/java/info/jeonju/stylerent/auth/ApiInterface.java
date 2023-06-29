package info.jeonju.stylerent.auth;

import info.jeonju.stylerent.auth.ChattingModels.ChatHistoryRequest;
import info.jeonju.stylerent.auth.ChattingModels.ChatHistoryResponse;
import info.jeonju.stylerent.auth.ChattingModels.MessageInit;
import info.jeonju.stylerent.auth.ChattingModels.MessageInitResponse;
import info.jeonju.stylerent.auth.ChattingModels.MessageRequest;
import info.jeonju.stylerent.auth.ChattingModels.MessageResponse;
import info.jeonju.stylerent.auth.ChattingModels.MyChatResponse;
import info.jeonju.stylerent.auth.ChattingModels.RentRequest;
import info.jeonju.stylerent.auth.ChattingModels.RentResponse;
import info.jeonju.stylerent.auth.FavoriteModels.FavModel;
import info.jeonju.stylerent.auth.FavoriteModels.FavResponse;
import info.jeonju.stylerent.auth.ProductModels.ProductDataResponse;
import info.jeonju.stylerent.auth.ProductModels.ProductImageResponse;
import info.jeonju.stylerent.auth.ProductModels.ProductInfoRequest;
import info.jeonju.stylerent.auth.ProductModels.ProductInfoResponse;
import info.jeonju.stylerent.auth.ProductModels.ProductInitResponse;
import info.jeonju.stylerent.auth.Rank.RankRequest;
import info.jeonju.stylerent.auth.Rank.RankResponse;
import info.jeonju.stylerent.auth.Rank.ReviewListModel;
import info.jeonju.stylerent.userdata.CoordinateResponse;
import info.jeonju.stylerent.userdata.GetNearByData;
import info.jeonju.stylerent.userdata.ImageResponse;
import info.jeonju.stylerent.userdata.ItemProductImageResponse;
import info.jeonju.stylerent.userdata.OtherUserDataResponse;
import info.jeonju.stylerent.userdata.UserProfileResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {

    @POST("/api/v1/auth/register")
        //Email, Username, Password
        //{username: example, email: example@gmail.com, password: example}
    Call<RegisterResponse> getRegisterInformation(@Body RegisterRequest body);


    @POST("/api/v1/auth/login")
        //Email, Password
        //{email: example@gmail.com, password: example}
    Call<LoginResponse> getLoginInformation(@Body LoginRequest body);

    @GET("/api/v1/getuserdata")
    Call<UserProfileResponse> getUserProfile(@Header("Authorization") String TOKEN);

    @Multipart
    @POST("/api/v1/uploadimage")
    Call<ImageResponse> uploadImage(@Header("Authorization") String TOKEN, @Part MultipartBody.Part image);

    //map

    @POST("/api/v1/setcurrentlocation")
    Call<CoordinateResponse> setCurrentLocation(@Header("Authorization") String TOKEN, @Body LocationData locationData);

    @GET("/api/v1/getcurrentlocation")
    Call<CoordinateResponse> getCurrentLocation(@Header("Authorization") String TOKEN);

    @POST("/api/v1/updatecurrentlocation")
    Call<CoordinateResponse> updatecurrentlocation(@Header("Authorization") String TOKEN, @Body LocationData locationData);


    @GET("/api/v1/getnearbyusers/{distance}")
    Call<List<GetNearByData>> getNearByUsers(@Header("Authorization") String TOKEN, @Path("distance") Integer distance);

    //product
    @POST("/api/v1/product/image")
    Call<ResponseBody> getImageByPath(@Header("Authorization") String TOKEN, @Body ImagePathRequest imagePathRequest);

    //product APIs
    @POST("/api/v1/product/newproduct")
    Call<ProductInitResponse> productInitRequest(@Header("Authorization") String TOKEN);

    @POST("/api/v1/product/addproductinfo")
    Call<ProductInfoResponse> productAddInfo(@Header("Authorization") String TOKEN, @Body ProductInfoRequest productInfoRequest);

    @Multipart
    @POST("/api/v1/product/newimage/{productId}")
    Call<ProductImageResponse> uploadProductImage(@Header("Authorization") String TOKEN, @Path("productId") Integer productId, @Part MultipartBody.Part image);

    //chat apis
    @GET("/api/v1/chat")
    Call<List<MyChatResponse>> getMyChat(@Header("Authorization") String TOKEN);

    @POST("/api/v1/chat/init")
    Call<MessageInitResponse> messageInit(@Header("Authorization") String TOKEN, @Body MessageInit request);


    @POST("/api/v1/chat/sendmessage")
    Call<MessageResponse> sendMessage(@Header("Authorization") String TOKEN, @Body MessageRequest request);

    @POST("/api/v1/chat/history")
    Call<ChatHistoryResponse> chatHistory(@Header("Authorization") String TOKEN, @Body ChatHistoryRequest request);


    @GET("/api/v1/getotheruserdata/{userId}")
    Call<OtherUserDataResponse> getOtherUserData(@Header("Authorization") String TOKEN, @Path("userId") Integer userId);


    @GET("/api/v1/product/like")
    Call<List<FavModel>> getMyFav(@Header("Authorization") String TOKEN);

    @DELETE("/api/v1/product/like/{productId}")
    Call<FavResponse> deleteFav(@Header("Authorization") String TOKEN, @Path("productId") Integer productId);

    @POST("/api/v1/product/like/{productId}")
    Call<FavResponse> addFav(@Header("Authorization") String TOKEN, @Path("productId") Integer productId);

    @GET("/api/v1/product/getproductimages/{productId}")
    Call<List<ItemProductImageResponse>> findProductImage(@Header("Authorization") String TOKEN, @Path("productId") Integer productId);


    @POST("/api/v1/rent/add")
    Call<RentResponse> addRent(@Header("Authorization") String TOKEN, @Body RentRequest request);

    @POST("/api/v1/rent/return")
    Call<RentResponse> returnRent(@Header("Authorization") String TOKEN, @Body RentRequest request);

    // remove product
    @DELETE("/api/v1/product/delete/{productId}")
    Call<RentResponse> removeProduct(@Header("Authorization") String TOKEN, @Path("productId") Integer productId);

    // set rank api
    @POST("/api/v1/setrank")
    Call<RankResponse> addRating(@Header("Authorization") String TOKEN, @Body RankRequest request);

    // update username
    @POST("/api/v1/updateuser")
    Call<UserNameUpdateResponse> updateUserName(@Header("Authorization") String TOKEN, @Body UserNameRequest request);


    // update product info
    @POST("/api/v1/product/updateproductinfo")
    Call<UserNameUpdateResponse> updateProductInfo(@Header("Authorization") String TOKEN, @Body ProductInfoRequest request);

    // get product info
    @GET("/api/v1/product/info/{productId}")
    Call<ProductDataResponse> getProductInfo(@Header("Authorization") String TOKEN, @Path("productId") Integer productId);

    // delete product Image
    @DELETE("/api/v1/product/removeproductimage/{imageId}")
    Call<UserNameUpdateResponse> deleteProductImage(@Header("Authorization") String TOKEN, @Path("imageId") Integer imageId);

    @GET("/api/v1/getmyreview")
    Call<List<ReviewListModel>> getMyReview(@Header("Authorization") String TOKEN);

    @GET("/api/v1/getreview/{userId}")
    Call<List<ReviewListModel>> getReview(@Header("Authorization") String TOKEN, @Path("userId") Integer userId);

}

//    @POST("/api/v1/setrank")
//    Call<RatingResponse> setRank(@Header("Authorization") String TOKEN, @Body RatingRequest body);






