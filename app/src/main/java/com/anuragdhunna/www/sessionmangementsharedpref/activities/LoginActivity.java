package com.anuragdhunna.www.sessionmangementsharedpref.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anuragdhunna.www.sessionmangementsharedpref.utils.LoginServices;
import com.anuragdhunna.www.sessionmangementsharedpref.R;
import com.anuragdhunna.www.sessionmangementsharedpref.utils.RetrofitClient;
import com.anuragdhunna.www.sessionmangementsharedpref.utils.SaveSharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

/**
 * @author anuragdhunna
 */
public class LoginActivity extends AppCompatActivity {

    EditText username,password;
    Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.usernameText);
        password = findViewById(R.id.passwordText);
        submitBtn = findViewById(R.id.submit);

        // Check if UserResponse is Already Logged In
        if(SaveSharedPreference.getLoggedStatus(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(intent);
        } else {
            submitBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    userLogin(username.getText().toString(), password.getText().toString());
                }
            });
        }
    }

    /**
     * Login API call
     * TODO: Please modify according to your need it is just an example
     * @param username
     * @param password
     */
    private void userLogin(String username, String password) {

        Retrofit retrofit = RetrofitClient.getClient();
        final LoginServices loginServices = retrofit.create(LoginServices.class);
        Call<Void> call = loginServices.userLogin(username, password);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.isSuccessful()) {
                    // Set Logged In statue to 'true'
                    SaveSharedPreference.setLoggedIn(getApplicationContext(), true);
                    Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK |FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Credentials are not Valid.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("TAG", "=======onFailure: " + t.toString());
                t.printStackTrace();
                // Log error here since request failed
            }
        });
    }
}
