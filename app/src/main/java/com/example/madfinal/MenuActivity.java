package com.example.madfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MenuActivity extends AppCompatActivity {
    private TextView success;
    private Button jukebox;
    String result;
    private Button shop_app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        success = findViewById(R.id.success);
        result = getIntent().getStringExtra("auth");
        success.setText(result);
        //Toast.makeText(Menu1Activity.this, "Authentification successful", Toast.LENGTH_LONG).show();
        jukebox = findViewById(R.id.jukebox);

        shop_app = findViewById(R.id.shop_app);
        shop_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, TableActivity.class);
                startActivity(intent);
            }
        });
        jukebox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, JukeBoxActivity.class);
                startActivity(intent);


            }
        });

    }
}