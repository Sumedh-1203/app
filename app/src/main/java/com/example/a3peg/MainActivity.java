package com.example.a3peg;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText message = (EditText) findViewById(R.id.edtTxtMsg);
        Button sendButton = (Button) findViewById(R.id.btnSend);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.137.142/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                APIService service = retrofit.create(APIService.class);

                Call<ResponseBody> call = service.sendString(message.getText().toString());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Response successful", Toast.LENGTH_SHORT).show();
                            System.out.println(response.body());
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Response unsuccessful", Toast.LENGTH_SHORT).show();
                            System.out.println(response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                        Log.e("RetrofitError", t.getMessage());
                    }
                });
            }
        });
    }

    interface APIService {
        @POST("string")
        @FormUrlEncoded
        Call<ResponseBody> sendString(@Field("string") String string);
    }
}
