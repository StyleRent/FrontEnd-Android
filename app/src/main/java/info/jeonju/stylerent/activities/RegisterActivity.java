package info.jeonju.stylerent.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import info.jeonju.stylerent.R;
import info.jeonju.stylerent.auth.ApiInterface;
import info.jeonju.stylerent.auth.RegisterRequest;
import info.jeonju.stylerent.auth.RegisterResponse;
import info.jeonju.stylerent.auth.RetrofitClient;

import java.util.regex.Pattern;
import android.util.Patterns;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private Button btnregister;
    private EditText usernameText, emailText, passwordText;
    private TextView loginText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameText = findViewById(R.id.username_text);
        emailText = findViewById(R.id.email_text);
        passwordText = findViewById(R.id.password_text);
        btnregister = findViewById(R.id.registerBtn1);

        loginText = findViewById(R.id.loginTextview);

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnregisterClicked();
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // ID 유효성 검사 함수
    private boolean isValidEmail(String email) {
        // 이메일 형식 패턴
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    // 비밀번호 유효성 검사 함수
    private boolean isValidPassword(String password) {
        // 비밀번호 최소 길이 6
        return password.length() >= 6;
    }

    private void btnregisterClicked() {
        String username = usernameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if(username.isEmpty()){
            Toast.makeText(RegisterActivity.this, "Please Enter Username!!", Toast.LENGTH_SHORT).show();
        }else if(email.isEmpty()){
            Toast.makeText(RegisterActivity.this, "Please Enter Email!!", Toast.LENGTH_SHORT).show();
        }else if(password.isEmpty()){
            Toast.makeText(RegisterActivity.this, "Please Enter Password!!", Toast.LENGTH_SHORT).show();
        }else if(!isValidEmail(email)){
            Toast.makeText(RegisterActivity.this, "Please create an email!!", Toast.LENGTH_SHORT).show();
        }else if(!isValidPassword(password)){
            Toast.makeText(RegisterActivity.this, "Please write at least 6 characters!!", Toast.LENGTH_SHORT).show();
        }else{

            //Registration request

            ApiInterface apiInterface = RetrofitClient.getRetrofitInstance(RegisterActivity.this).create(ApiInterface.class);

            Call<RegisterResponse> call = apiInterface.getRegisterInformation(new RegisterRequest(username, email, password));
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                    if(response.body().getError() != null){
                        Toast.makeText(RegisterActivity.this, response.body().getError(), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(RegisterActivity.this, "Registration successful: Your Token->" + response.body().getToken(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    Log.e(TAG, "onResponse: " + response.code());
                    Log.e(TAG, "onResponse: " + response.body());

                }


                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Log.e(TAG, "onFailure: "+ t.getMessage());

                }
            });
        }
    }
}