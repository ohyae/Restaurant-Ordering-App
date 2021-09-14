package com.example.madfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.LinkedList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderActivity extends AppCompatActivity {
    private  static TextView total;
    private static String oc;

    DatabaseHandler myDb;
    SQLiteDatabase database;
    public RecyclerView mRecyclerView;
    private LinkedList<Product> ProductList;
    TextView ID_tab;

    public  ImageButton btn_order;
    static int[] table_orders;
    String url = "http://mad.mywork.gr/send_order.php?t=430123&tid=TABLE_ID&oc=CONTENTS";
    public String myResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        total = findViewById(R.id.total);
        table_orders = new int[16];
        btn_order = findViewById(R.id.btn_order);
        ID_tab = findViewById(R.id.ID_tab);
        Bundle bundle = getIntent().getExtras();
        final Integer Tid = bundle.getInt("Tnumb");
        final Integer[] Tstatus = {bundle.getInt("Tstat")};
        ID_tab.setText(ID_tab.getText() + "\n"+ Tid);

        ProductList = new LinkedList<Product>();
        myDb = new DatabaseHandler(this);
        database = myDb.getReadableDatabase();
        Cursor c = myDb.getAllData1();
        c.moveToFirst();
                    if (c != null) {
                        do {
                            final Integer product_id = c.getInt(0);
                            final String product_title = c.getString(1);
                            final String product_price = c.getString(2);
                            ProductList.add(new Product(product_id, product_title, Float.parseFloat(product_price))
                           );
                        }
                        while (c.moveToNext());
                    }
        mRecyclerView = (RecyclerView) findViewById(R.id.db_product_recycler);
        ProductListAdapter mAdapter = new ProductListAdapter(this, ProductList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
           btn_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   String ord_price= total.getText().toString();
                   float ord_price1 = Float.parseFloat(ord_price);
                   System.out.println(ord_price1);
                   if (ord_price1<1)
                   {
                       Toast.makeText(OrderActivity.this, "No product has been selected", Toast.LENGTH_SHORT).show();
                   }
                   else {
                  String newUrl = url.replaceAll("TABLE_ID", String.valueOf(Tid));

                    newUrl= newUrl.replaceAll("CONTENTS", oc);
                    System.out.println(newUrl);
                    OkHttpClient client = new OkHttpClient();
                    final Request request = new Request.Builder()
                            .url(newUrl)
                            .build();
                    client.newCall(request).enqueue(new Callback() {

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            if (response.isSuccessful()) ;
                            {
                                myResponse = response.body().string();
                                OrderActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        System.out.println(myResponse);

                                        String opentag = "<status>";
                                        String closetag = "</status>";
                                        String XMLCode3 = myResponse;
                                        int p1 = XMLCode3.indexOf(opentag) + opentag.length();
                                        int p2 = XMLCode3.indexOf(closetag);
                                        String msg1 = XMLCode3.substring(p1, p2);

                                        String opentag1 = "<msg>";
                                        String closetag1 = "</msg>";
                                        String XMLCode = myResponse;
                                        int p3 = XMLCode.indexOf(opentag1) + opentag1.length();
                                        int p4 = XMLCode.indexOf(closetag1);
                                        String msg2 = XMLCode.substring(p3, p4);

                                        if (msg1.equals("4-FAIL")||msg1.equals("0-FAIL")) {
                                            Toast.makeText(OrderActivity.this, msg2, Toast.LENGTH_SHORT).show();
                                        } else {
                                            Intent intent = new Intent(OrderActivity.this, TableActivity.class);
                                            startActivity(intent);
                                            //IT STARTS TABLE ACTIVITY BUT WITHOUT CHANGING THE COLOR OF THE TSBLE, BECAUSE IN MY CODE
                                            // THE DATABASE DOES NOT UPDATES AUTOMATICALLY, ONLY BY CHANGING THE VERSION OF DATABASE IN DATABASEHANDLER ACRIVITY
                                            finish();


                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                        }

                    })

                    ;}}
            });
        }
public static void countOrder(float totalOrder, int Qty, int coffeeID) {
        float orderPrice = Float.parseFloat(total.getText().toString());
    orderPrice = orderPrice+totalOrder;
        table_orders[coffeeID-1] = Qty;
         oc = "";
        for (int i=0; i<table_orders.length; i++) {
            if (table_orders[i]!=0)
                oc = oc + (i+1) + "," + table_orders[i] + ";";
        }
    total.setText(Float.toString(orderPrice));
    System.out.println(oc);
}

public static void countOrder1(float totalOrder1, int Qty1, int coffeeID1) {
        float orderPrice1 = Float.parseFloat(total.getText().toString());
        orderPrice1 = orderPrice1-totalOrder1;
        total.setText(Float.toString(orderPrice1));
    table_orders[coffeeID1-1] = Qty1;
     oc = "";
    for (int i=0; i<table_orders.length; i++) {
        if (table_orders[i]!=0)
            oc = oc + (i+1) + "," + table_orders[i] + ";";
    }
    System.out.println(oc);
    }


}
