package cz.idio;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import cz.idio.api.ApiClient;
import cz.idio.api.StringEncryption;
import cz.idio.api.response.LoginResponse;
import cz.idio.model.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;
   private String username,password,pwdSha1;
   private int personId;
   private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Context mContext = getApplicationContext();
        preferences = mContext.getSharedPreferences("data",Context.MODE_PRIVATE);
        mUsernameEditText = findViewById(R.id.editTextLogin);
        mPasswordEditText = findViewById(R.id.editTextTextPwd);
        mLoginButton = findViewById(R.id.Login);
        mUsernameEditText.setText(preferences.getString("username", ""));
        mPasswordEditText.setText(preferences.getString("password", ""));
        if (mLoginButton != null) {
            mLoginButton.setOnClickListener(v -> {
                username = mUsernameEditText.getText().toString();
                password = mPasswordEditText.getText().toString();
                // Pokud jsou oba řetězce neprázdné
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    try {
                        pwdSha1 = StringEncryption.SHA1(password);
                    } catch (NoSuchAlgorithmException |
                             UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }


                    ApiService apiService = ApiClient.getClient().create(ApiService.class);
                    Call<LoginResponse> call = apiService.login("Login","Login",username, pwdSha1);
                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (response.isSuccessful()) {
                                LoginResponse loginResponse= response.body();
                                personId=loginResponse.getPersonId();
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("username", username);
                                editor.putString("pwdSha1", pwdSha1);
                                editor.putString("password",password);
                                editor.putString("personId", String.valueOf(personId));
                                editor.apply();
                                Toast.makeText(mContext , "Přihlášeni proběhlo úspěšně", Toast.LENGTH_SHORT).show();
                            }
                        }



                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            // Chyba při komunikaci s API
                            t.printStackTrace();
                            Toast.makeText(mContext , "Chyba onFailure: "+ t, Toast.LENGTH_SHORT).show();
                        }
                    });
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // Vypiš chybovou zprávu, pokud jsou pole prázdné
                    Toast.makeText(LoginActivity.this, "Vyplňte prosím přihlašovací údaje", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

