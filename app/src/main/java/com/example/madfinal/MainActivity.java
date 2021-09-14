package com.example.madfinal;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private TextView TextViewResult;
    String url =  "http://mad.mywork.gr/authenticate.php?t=430123";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextViewResult = findViewById(R.id.text_view);
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) ;
                {
                    final String myResponse = response.body().string();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String opentag = "<status>";
                            String closetag = "</status>";
                            String XMLCode = myResponse;
                            int p1 = XMLCode.indexOf(opentag) + opentag.length();
                            int p2 = XMLCode.indexOf(closetag);
                            String message = XMLCode.substring(p1, p2);
                            //TextViewResult.setText(message);

                            if (message.equals("0-FAIL")) {
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            } else {

                                startActivity(new Intent(MainActivity.this, MenuActivity.class));
                                String opentag2 = "<msg>";
                                String closetag2 = "</msg>";
                                String XMLCode2 = myResponse;
                                int p3 = XMLCode2.indexOf(opentag2) + opentag2.length();
                                int p4 = XMLCode2.indexOf(closetag2);
                                String message1 = XMLCode2.substring(p3, p4);
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this, MenuActivity.class);
                                intent.putExtra("auth", message1);
                                startActivity(intent);

                            }


                        }


                    });


                }

            }

        });
    }
}