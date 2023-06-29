package info.jeonju.stylerent.activities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import info.jeonju.stylerent.R;
import info.jeonju.stylerent.auth.ApiInterface;
import info.jeonju.stylerent.auth.ChattingModels.ChatHistoryRequest;
import info.jeonju.stylerent.auth.ChattingModels.ChatHistoryResponse;
import info.jeonju.stylerent.auth.ChattingModels.MessageAdapter;
import info.jeonju.stylerent.auth.ChattingModels.MessageRequest;
import info.jeonju.stylerent.auth.ChattingModels.MessageResponse;
import info.jeonju.stylerent.auth.ChattingModels.ReceiverHistory;
import info.jeonju.stylerent.auth.ChattingModels.RentRequest;
import info.jeonju.stylerent.auth.ChattingModels.RentResponse;
import info.jeonju.stylerent.auth.ChattingModels.SenderHistory;
import info.jeonju.stylerent.auth.Rank.RankRequest;
import info.jeonju.stylerent.auth.Rank.RankResponse;
import info.jeonju.stylerent.auth.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;

    private ListView messageHistory;

    private MessageAdapter messageAdapter;

    private ChatHistoryResponse chatHistoryData;

    private List<SenderHistory> senderList = new ArrayList<>();
    private List<ReceiverHistory> receiverList = new ArrayList<>();

    private Integer messageId;
    private Integer receiverId;
    private Integer productId;

    private String productName;

    private String productPrice;

    private String productImage;

    private Handler handler;



    private EditText messageEditText;
    private ImageView sendImageView, productImageView;
    private TextView productNameTV, productPriceTV;

    private ImageButton backBtn;
    private Switch switchRent;
    private  Boolean myChat;
    private TextView switchLabel;
    private LinearLayout reviewLayout, rentLayout;

    private Boolean rentStatus;

    private Boolean isRentedToMe;
    private Button reviewButton;
    private ApiInterface apiInterface;
    private RentRequest request;
    private String TOKEN;
    private AlertDialog alertDialog;





    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        TOKEN = sharedPreferences.getString("TOKEN", null);

        messageId = getIntent().getIntExtra("messageId", -1);
        receiverId = getIntent().getIntExtra("receiverId", -1);
        productId = getIntent().getIntExtra("productId", -1);
        myChat = getIntent().getBooleanExtra("myChat", false);
        rentStatus = getIntent().getBooleanExtra("rentStatus", false);
        isRentedToMe = getIntent().getBooleanExtra("isRentedToMe", false);

        productName = getIntent().getStringExtra("productName");
        productPrice = getIntent().getStringExtra("productPrice");
        productImage = getIntent().getStringExtra("productImage");


        backBtn = findViewById(R.id.relative_back_btn);
        messageEditText = findViewById(R.id.messageEditText);
        sendImageView = findViewById(R.id.sendImageView);
        messageHistory = findViewById(R.id.messageHistory);
        switchRent = findViewById(R.id.switchRent);
        switchLabel = findViewById(R.id.switchLabel);
        rentLayout = findViewById(R.id.rentLayout);
        reviewLayout = findViewById(R.id.reviewLayout);
        reviewButton = findViewById(R.id.reviewButton);
        productNameTV = findViewById(R.id.productName);
        productPriceTV = findViewById(R.id.productPrice);
        productImageView = findViewById(R.id.productImage);


        productNameTV.setText(productName);
        productPriceTV.setText(productPrice);
        request = new RentRequest(receiverId, productId);
        apiInterface = RetrofitClient.getRetrofitInstance(MessageActivity.this).create(ApiInterface.class);

        switchRent.setChecked(rentStatus);

        // productimage
        if (!productImage.isEmpty()) {
            byte[] image = Base64.decode(productImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            productImageView.setImageBitmap(bitmap);
        }


        // start check chat
        if(myChat){
            // auto correct
            // check is rent is true
            // switch change on true
            rentLayout.setVisibility(View.VISIBLE);
            reviewLayout.setVisibility(View.GONE);



                    // request on change
            switchRent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // Switch is ON
                        // add rent

                        Call<RentResponse> rentResponseApi = apiInterface.addRent("Bearer" + TOKEN, request);
                        rentResponseApi.enqueue(new Callback<RentResponse>() {
                            @Override
                            public void onResponse(Call<RentResponse> call, Response<RentResponse> response) {
                                if(response.body().getMessage() != null){

                                    Toast.makeText(getApplication(), "Successfully rented", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getApplication(), response.body().getError(), Toast.LENGTH_SHORT).show();

                                    switchRent.setChecked(false);
                                    switchLabel.setText("대여중");
                                }
                            }

                            @Override
                            public void onFailure(Call<RentResponse> call, Throwable t) {
                            }
                        });

                        // Perform any action you want when the switch is turned ON
                        // For example, enable a feature or change a setting
                    } else {
                        // Switch is OFF
                        // return rent

                        String TOKEN = sharedPreferences.getString("TOKEN", null);

                        RentRequest request = new RentRequest(receiverId, productId);
                        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(MessageActivity.this).create(ApiInterface.class);
                        Call<RentResponse> rentResponseApi = apiInterface.returnRent("Bearer" + TOKEN, request);
                        rentResponseApi.enqueue(new Callback<RentResponse>() {
                            @Override
                            public void onResponse(Call<RentResponse> call, Response<RentResponse> response) {
                                if(response.body().getMessage() != null){
                                    Toast.makeText(getApplication(), "Successfully rent returned", Toast.LENGTH_SHORT).show();

                                }
                                else{

                                    Toast.makeText(getApplication(), response.body().getError(), Toast.LENGTH_SHORT).show();
                                    switchRent.setChecked(true);
                                }
                            }
                            @Override
                            public void onFailure(Call<RentResponse> call, Throwable t) {
                            }
                        });
                        // Perform any action you want when the switch is turned OFF
                        // For example, disable a feature or revert a setting
                    }
                }
            });
        }else{

            //else-not mychat  1)아무것도 보이지 않도록 해야 되고 ->2) 주인이 대여하기 스위츠 할때 '리뷰' 버튼 visible
            // invisible switch -> visible review

            if(isRentedToMe){
                rentLayout.setVisibility(View.GONE);
                reviewLayout.setVisibility(View.VISIBLE);
                reviewButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // open dialog for enter review
                        //when click enter make review api request
                        showDialogReview();
                    }
                });
            }else{
                rentLayout.setVisibility(View.GONE);
                reviewLayout.setVisibility(View.GONE);
            }
        }

        // end





        messageHistory.setDivider(null);

        if(messageId >= 0 && receiverId >= 0 && productId >= 0){
            ChatHistoryRequest request = new ChatHistoryRequest(messageId, receiverId, productId);
            System.out.println("FIrst get chat history ------->");
            getChatHistory(request, true);
        }


        handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ChatHistoryRequest request = new ChatHistoryRequest(messageId, receiverId, productId);
                getChatHistory(request, false);
                System.out.println("second get chat history ------->");
                handler.postDelayed(this, 3000); // Fetch data every 3 seconds
            }
        }, 3000); // Initial delay before first data fetch

        //get chat history -> request POST
        // response -> chat history with receiver and sender chat


        messageEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    String messageText = messageEditText.getText().toString().trim();
                    if (!messageText.isEmpty()) {
                        // Hide keyboard
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(messageEditText.getWindowToken(), 0);
                        messageEditText.setText("");
                        // Send request
                        MessageRequest messageRequest = new MessageRequest(messageText, messageId, receiverId, productId);
                        sendChat(messageRequest, true);
                    } else {
                        Toast.makeText(MessageActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });



        sendImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageEditText.getText().toString().trim();
                if (!messageText.isEmpty()) {

                    // Hide keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(sendImageView.getWindowToken(), 0);
                    messageEditText.setText("");
                    // send request
                    MessageRequest messageRequest = new MessageRequest(messageText, messageId, receiverId, productId);
                    sendChat(messageRequest, true);
                } else {
                    // Hide keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(sendImageView.getWindowToken(), 0);
                    Toast.makeText(MessageActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void showDialogReview(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(getApplication()).inflate(R.layout.dialog_review, null);
        builder.setView(dialogView);

        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBarReview);
        ratingBar.setStepSize(1);
        EditText editText = dialogView.findViewById(R.id.reviewEditT);



        Button btnYes = dialogView.findViewById(R.id.btn_yes);
        Button btnNo = dialogView.findViewById(R.id.btn_no);

        alertDialog = builder.create();
        // 상대방이 반납했나요? => yes 즉 이제 다른사람들도 빌려갈수있음
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ratingBar.getRating() == 0){
                    Toast.makeText(getApplication(), "Please add rating star", Toast.LENGTH_SHORT).show();
                } else if (editText.getText().toString().isEmpty()) {
                    Toast.makeText(getApplication(), "댓글 입력하세요!", Toast.LENGTH_SHORT).show();
                }else{
                    float rating = ratingBar.getRating();
                    int stars = (int) rating;
                    addReview(stars, editText.getText().toString());
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


    // add rating
    private void addReview(Integer rating, String ratingText){
        // request add reivew
        RankRequest requestMessage = new RankRequest(rating, receiverId, productId, ratingText);
        Call<RankResponse> chatResponseApi = apiInterface.addRating("Bearer" + TOKEN, requestMessage);
        chatResponseApi.enqueue(new Callback<RankResponse>() {
            @Override
            public void onResponse(Call<RankResponse> call, Response<RankResponse> response) {
                if (response.isSuccessful()) {
                    RankResponse rankResponse = response.body();
                    if(rankResponse.getError() == null){
                        Toast.makeText(getApplication(), "평가 추가되었습니다!", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }else {
                        Toast.makeText(getApplication(), "평가 이미 추가되었습니다!", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<RankResponse> call, Throwable t) {
                // Handle network failure
                Log.e(TAG, "Failed to set product info: " + t.getMessage());


            }
        });
    }


    private void sendChat(MessageRequest requestMessage, boolean scrollDown){
        Call<MessageResponse> chatResponseApi = apiInterface.sendMessage("Bearer" + TOKEN, requestMessage);
        chatResponseApi.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    MessageResponse messageResponse = response.body();
                    if(messageResponse.getError() == null){
                        getChatHistory(new ChatHistoryRequest(messageId, receiverId, productId), scrollDown);
                    }else {
                        System.out.println("error send message");
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                // Handle network failure
                Log.e(TAG, "Failed to set product info: " + t.getMessage());

            }
        });
    }


    private void getChatHistory(ChatHistoryRequest request, boolean scrollDown){
        String TOKEN = sharedPreferences.getString("TOKEN", null);

        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(MessageActivity.this).create(ApiInterface.class);
        Call<ChatHistoryResponse> chatResponseApi = apiInterface.chatHistory("Bearer" + TOKEN, request);
        chatResponseApi.enqueue(new Callback<ChatHistoryResponse>() {
            @Override
            public void onResponse(Call<ChatHistoryResponse> call, Response<ChatHistoryResponse> response) {
                System.out.println(response.body());
                if (response.isSuccessful()) {
                    if (response.body().getError() == null){
                        if(senderList.size() == 0 && receiverList.size() == 0){
                            chatHistoryData = response.body();
                            senderList = chatHistoryData.getSender();
                            receiverList = chatHistoryData.getReceiver();
                            messageAdapter = new MessageAdapter(getApplication(), senderList, receiverList);
                            messageHistory.setAdapter(messageAdapter);
                            System.out.println("SIze ->>>>>> 00000" + messageAdapter.getCount());

                            // after 1sec get chat history run message scroll down
                            if (scrollDown && senderList.size() + receiverList.size() > 0) {
                                // Delay the auto-scrolling by 1 second
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        int lastItemIndex = messageAdapter.getCount() - 1;
                                        if (lastItemIndex >= 0) {
                                            System.out.println("size 0000 -> scrolling down ----->>>>>");

                                            messageHistory.setSelection(lastItemIndex);
                                        }
                                    }
                                }, 100);
                            }

                        }else{
                            chatHistoryData = response.body();
                            boolean checkUpdate = false;
                            if (chatHistoryData.getSender().size() > senderList.size() || chatHistoryData.getReceiver().size() > receiverList.size()) {
                                checkUpdate = true;
                            }
                            senderList.clear();
                            receiverList.clear();
                            senderList = chatHistoryData.getSender();
                            receiverList = chatHistoryData.getReceiver();
                            messageAdapter.setSenderList(senderList);
                            messageAdapter.setReceiverList(receiverList);
                            messageAdapter.notifyDataSetChanged();
                            if (checkUpdate) {
                                System.out.println("ELSE scrolling down ----->>>>>");
                                int lastItemIndex = messageAdapter.getCount()-1;
                                messageHistory.smoothScrollToPosition(lastItemIndex);
                            }
                        }
                    }else{
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<ChatHistoryResponse> call, Throwable t) {
                // Handle network failure
                Log.e(TAG, "Failed to set product info: " + t.getMessage());

            }
        });
    }



//    private void displayChatMessages() {
//        // Update the messageAdapter with the new list of messages
//        messageAdapter.setMessageList(messageList);
//        messageAdapter.notifyDataSetChanged();
//    }
}