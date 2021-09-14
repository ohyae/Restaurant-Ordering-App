package com.example.madfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText mail1;
    private TextView text1;
    private ImageButton click2;
    private TextView textView2;
    String url = "http://mad.mywork.gr/generate_token.php?e=your_email";
    String mail;
    String token2;
    public String myResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        text1 = findViewById(R.id.text1);
        mail1 = findViewById(R.id.mail1);
        click2 = findViewById(R.id.click2);
        textView2 = findViewById(R.id.textView2);
        click2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mail = mail1.getText().toString();
                mail1.setVisibility(View.INVISIBLE);
                click2.setVisibility(View.INVISIBLE);
                textView2.setVisibility(View.INVISIBLE);
                String newURL = url.replaceAll("your_email", mail);
                OkHttpClient client = new OkHttpClient();
                final Request request = new Request.Builder()
                        .url(newURL)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) ;
                        {
                            myResponse = response.body().string();
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String opentag = "<msg>";
                                    String closetag = "</msg>";
                                    String XMLCode = myResponse;
                                    int p1 = XMLCode.indexOf(opentag) + opentag.length();
                                    int p2 = XMLCode.indexOf(closetag);
                                    String msg = XMLCode.substring(p1,p2);
                                    text1.setText(msg);
                                    token2 = msg.replaceAll("[^0-9]", "");

                                    String opentag3 = "<status>";
                                    String closetag3 = "</status>";
                                    String XMLCode3 = myResponse;
                                    int p4 = XMLCode3.indexOf(opentag3) + opentag3.length();
                                    int p5 = XMLCode3.indexOf(closetag3);
                                    String msg3 = XMLCode3.substring(p4,p5);

                                    if (msg3.equals("1-FAIL")){
                                        LoginActivity.this.recreate();
                                        Toast.makeText(LoginActivity.this, "Authentification Failed", Toast.LENGTH_LONG).show();

                                    }


                                }

                            });

                        }
                    }

                });

            }

        });

    }

}