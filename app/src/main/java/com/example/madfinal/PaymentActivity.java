package com.example.madfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PaymentActivity extends AppCompatActivity {
    TextView table_id;
    TextView cost;
    TextView paid;
    TextView balance;
    EditText et_am;
    ImageButton cashier;
    public String myResponse;
    LinkedList<Product> ProductList;
    public RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Bundle bundle = getIntent().getExtras();
        Integer Tid = bundle.getInt("Tnumb");
        Integer Tstatus = bundle.getInt("Tstat");
        table_id = findViewById(R.id.table_id);
        table_id.setText(Integer.toString(Tid));
        cost = findViewById(R.id.cost);
        paid = findViewById(R.id.paid);
        balance = findViewById(R.id.balance);
        et_am = findViewById(R.id.et_am);
        cashier = findViewById(R.id.cashier);
        ProductList = new LinkedList<Product>();




        String URL = "http://mad.mywork.gr/get_order.php?t=430123&tid=TAB_ID";
        String newURL = URL.replaceAll("TAB_ID", String.valueOf(Tid));
        System.out.println(newURL);
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(newURL)
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) ;
                {
                    myResponse = response.body().string();
                    PaymentActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            System.out.println(myResponse);
                            String opentag = "<status>";
                            String closetag = "</status>";
                            String XMLCode3 = myResponse;
                            int p1 = XMLCode3.indexOf(opentag) + opentag.length();
                            int p2 = XMLCode3.indexOf(closetag);
                            String msg1 = XMLCode3.substring(p1, p2);

                            if (msg1.equals("0-FAIL") || msg1.equals("5-FAIL")) {
                                String opentag1 = "<msg>";
                                String closetag1 = "</msg>";
                                String XMLCode = myResponse;
                                int p3 = XMLCode.indexOf(opentag1) + opentag1.length();
                                int p4 = XMLCode.indexOf(closetag1);
                                String msg2 = XMLCode.substring(p3, p4);
                                System.out.println(msg2);
                                Toast.makeText(PaymentActivity.this, msg2, Toast.LENGTH_SHORT).show();

                            } else
                                try {
                                    StringReader sr = new StringReader(myResponse);
                                    InputSource is = new InputSource(sr);
                                    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                                    DocumentBuilder dBuilder = builderFactory.newDocumentBuilder();
                                    Document doc = dBuilder.parse(is);
                                    Element element = doc.getDocumentElement();
                                    element.normalize();
                                    NodeList nList = doc.getElementsByTagName("order");
                                    NodeList nList1 = doc.getElementsByTagName("product");
                                    for (int i = 0; i < nList.getLength(); i++) {

                                        Node node = nList.item(i);
                                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                                            Element element2 = (Element) node;
                                            String costT = getValue("cost", element2);
                                            String paymentT = getValue("payment", element2);
                                            final String balanceT = getValue("balance", element2);
                                            //System.out.println(costT);
                                            cost.setText(costT);
                                            paid.setText(paymentT);
                                            balance.setText(balanceT);
                                            et_am.setText(balanceT);
                                            cashier.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Editable et_am1 = et_am.getText();
                                                         String et_am2= et_am1.toString();
                                                        if (Float.parseFloat(balanceT)<Float.parseFloat(et_am2)||(Float.parseFloat(et_am2))==0) {
                                                            Toast.makeText(PaymentActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else {
                                                            String url = "http://mad.mywork.gr/send_payment.php?t=430123&tid=TAB_ID&a=AMOUNT";

                                                            String newUrl = url.replaceAll("TAB_ID", table_id.getText().toString());
                                                             newUrl = newUrl.replaceAll("AMOUNT", et_am2);
                                                             System.out.println(newUrl);
                                                            OkHttpClient client = new OkHttpClient();
                                                            final Request request = new Request.Builder()
                                                                    .url(newUrl)
                                                                    .build();
                                                            client.newCall(request).enqueue(new Callback() {
                                                                @Override
                                                                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                                                                }

                                                                @Override
                                                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                                                    if (response.isSuccessful()) ;
                                                                    {
                                                                        myResponse = response.body().string();
                                                                        PaymentActivity.this.runOnUiThread(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                System.out.println(myResponse);

                                                                                String opentag3 = "<status>";
                                                                                String closetag3 = "</status>";
                                                                                String XMLCode5 = myResponse;
                                                                                int p6 = XMLCode5.indexOf(opentag3) + opentag3.length();
                                                                                int p7 = XMLCode5.indexOf(closetag3);
                                                                                String msg4 = XMLCode5.substring(p6,p7);

                                                                                String opentag6 = "<msg>";
                                                                                String closetag6 = "</msg>";
                                                                                String XMLCode6 = myResponse;
                                                                                int p10 = XMLCode6.indexOf(opentag6) + opentag6.length();
                                                                                int p8 = XMLCode6.indexOf(closetag6);
                                                                                String msg5= XMLCode6.substring(p10,p8);

                                                                                String open = "<new_balance>";
                                                                                String close = "</new_balance>";
                                                                                String XMLCod = myResponse;
                                                                                int p0 = XMLCod.indexOf(open) + open.length();
                                                                                int p9 = XMLCod.indexOf(close);
                                                                                String msg9= XMLCod.substring(p0,p9);
                                                                                float new_balance = Float.parseFloat(msg9);

                                                                                if (msg4.equals("0-FAIL")||msg4.equals("6-FAIL")) {
                                                                                    Toast.makeText(PaymentActivity.this,msg5, Toast.LENGTH_SHORT).show();
                                                                                }
                                                                                else {
                                                                                    Intent intent = new Intent(PaymentActivity.this, TableActivity.class);
                                                                                    startActivity(intent);
                                                                                    //IT STARTS TABLE ACTIVITY BUT WITHOUT CHANGING THE COLOR OF THE TABLE, BECAUSE IN MY CODE
                                                                                    // THE DATABASE DOES NOT UPDATES AUTOMATICALLY, ONLY THROUGH CHANGING THE VERSION OF DATABASE IN DATABASEHANDLER ACRIVITY
                                                                                    finish();
                                                                                }

                                                                            }
                                                                        });
                                                                    };
                                                                }
                                                            });

                                                        }

                                                    }
                                                });
                                        }
                                    }
                                    for (int i = 0; i < nList1.getLength(); i++) {
                                        Node node = nList1.item(i);
                                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                                            Element element2 = (Element) node;
                                            String product_id = getValue("id", element2);
                                            String product_title = getValue("title", element2);
                                            String product_price = getValue("price", element2);
                                            String pay_qty = getValue("quantity", element2);

                                            int p_id = Integer.parseInt(product_id);
                                            float p_price = Float.parseFloat(product_price);
                                            int p_quantity = Integer.parseInt(pay_qty);
                                           // System.out.println(pay_qty);


                                            ProductList.add(new Product(p_id, product_title, p_price));


                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                            protected String getValue (String tag, Element element){
                                NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
                                Node node = nodeList.item(0);
                                return node.getNodeValue();
                            }


                    });


                }


            }
       // });

        @Override
        public void onFailure (@NotNull Call call, @NotNull IOException e){

        }

    });

        mRecyclerView = (RecyclerView) findViewById(R.id.product_recycler);
        ProductListAdapter mAdapter = new ProductListAdapter(this, ProductList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

}}
