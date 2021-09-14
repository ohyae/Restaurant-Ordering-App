package com.example.madfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;

import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Checksum;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class TableActivity extends AppCompatActivity {
    TextView TextViewRes;

    Button but1;
    Button but2;
    Button but3;
    Button but4;
    Button but5;
    Button but6;
    Button but7;
    Button but8;
    Button but9;
    String url = "http://mad.mywork.gr/get_coffee_data.php?t=430123";
    DatabaseHandler myDb;
    SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myDb = new DatabaseHandler(this);
        database = myDb.getReadableDatabase();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        TextViewRes = findViewById(R.id.TextViewRes);
        but1 = findViewById(R.id.but1);
        but2 = findViewById(R.id.but2);
        but3 = findViewById(R.id.but3);
        but4 = findViewById(R.id.but4);
        but5 = findViewById(R.id.but5);
        but6 = findViewById(R.id.but6);
        but7 = findViewById(R.id.but7);
        but8 = findViewById(R.id.but8);
        but9 = findViewById(R.id.but9);


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
                    TableActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {
                                StringReader sr = new StringReader(myResponse);
                                InputSource is = new InputSource(sr);
                                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                                DocumentBuilder dBuilder = builderFactory.newDocumentBuilder();
                                Document doc = dBuilder.parse(is);
                                Element element = doc.getDocumentElement();
                                element.normalize();
                                NodeList nList = doc.getElementsByTagName("product");
                                NodeList nList1 = doc.getElementsByTagName("table");


                                for (int i = 0; i < nList.getLength(); i++) {

                                    Node node = nList.item(i);
                                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                                        Element element2 = (Element) node;
                                        String id = getValue("id", element2) + "\n";
                                        String title = getValue("title", element2) + "\n";
                                        String price = getValue("price", element2) + "\n";

                                        /*TextViewRes.setText(TextViewRes.getText() + "\nID : " + id);
                                        TextViewRes.setText(TextViewRes.getText() + "Title : " + title);
                                        TextViewRes.setText(TextViewRes.getText() + "Price : " + price);
                                        TextViewRes.setText(TextViewRes.getText() + "-----------------------");*/
                                        myDb.insert(id, title, price, database);

                                    }
                                }
                                for (int i = 0; i < nList1.getLength(); i++) {
                                    Node node = nList1.item(i);
                                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                                        Element element2 = (Element) node;
                                        String table_id = getValue("id", element2) + "\n";
                                        String status = getValue("status", element2) + "\n";

                                      /*  TextViewRes.setText(TextViewRes.getText() + "\nID : " + table_id);
                                        TextViewRes.setText(TextViewRes.getText() + "Status: " + status);
                                        TextViewRes.setText(TextViewRes.getText() + "-----------------------");*/
                                        myDb.insert(table_id, status, database);

                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        protected String getValue(String tag, Element element) {
                            NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
                            Node node = nodeList.item(0);
                            return node.getNodeValue();
                        }
                    });
                    myDb = new DatabaseHandler(getApplicationContext());
                    database = myDb.getReadableDatabase();
                    Cursor c = myDb.getAllData();
                    if (c == null) {
                    }
                    else {
                        if (c.getCount() > 0 ) {
                            if (c.moveToFirst()) {
                                do {

                                    final Integer Tid = c.getInt(0);
                                    final Integer Tstatus = c.getInt(1);
                                    //TextViewRes.setText(TextViewRes.getText() + "\nID : " + Tid);
                                   // TextViewRes.setText(TextViewRes.getText() + "\nStatus : " + Tstatus);

                                    if (Tid == 101 &&  Tstatus < 1) {
                                        but1.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                                        but1.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(TableActivity.this, OrderActivity.class);
                                                intent.putExtra("Tnumb", Tid);
                                                intent.putExtra("Tstat", Tstatus);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                    else if (101 == Tid && Tstatus > 0  ) {
                                        but1.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                                        but1.setOnLongClickListener(new View.OnLongClickListener() {
                                            @Override
                                            public boolean onLongClick(View v) {
                                                Intent intent = new Intent(TableActivity.this, PaymentActivity.class);
                                                intent.putExtra("Tnumb", Tid);
                                                intent.putExtra("Tstat", Tstatus);
                                                startActivity(intent);
                                                return true;
                                            }
                                        });
                                        but1.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(TableActivity.this, OrderActivity.class);
                                                intent.putExtra("Tnumb", Tid);
                                                intent.putExtra("Tstat", Tstatus);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                    if (Tid == 102 &&  Tstatus < 1) {
                                        but2.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                                        but2.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(TableActivity.this, OrderActivity.class);
                                                intent.putExtra("Tnumb", Tid);
                                                intent.putExtra("Tstat", Tstatus);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                    else if (102 == Tid && Tstatus > 0  ) {
                                        but2.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                                        but2.setOnLongClickListener(new View.OnLongClickListener() {
                                            @Override
                                            public boolean onLongClick(View v) {
                                                Intent intent = new Intent(TableActivity.this, PaymentActivity.class);
                                                intent.putExtra("Tnumb", Tid);
                                                intent.putExtra("Tstat", Tstatus);
                                                startActivity(intent);
                                                return true;
                                            }
                                        });
                                        but2.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(TableActivity.this, OrderActivity.class);
                                                intent.putExtra("Tnumb", Tid);
                                                intent.putExtra("Tstat", Tstatus);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                    if (Tid == 103 &&  Tstatus < 1) {
                                        but3.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                                        but3.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(TableActivity.this, OrderActivity.class);
                                                intent.putExtra("Tnumb", Tid);
                                                intent.putExtra("Tstat", Tstatus);
                                                startActivity(intent);
                                            }
                                        });

                                    }
                                    else if (103 == Tid && Tstatus > 0  ) {
                                        but3.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                                        but3.setOnLongClickListener(new View.OnLongClickListener() {
                                            @Override
                                            public boolean onLongClick(View v) {
                                                Intent intent = new Intent(TableActivity.this, PaymentActivity.class);
                                                intent.putExtra("Tnumb", Tid);
                                                intent.putExtra("Tstat", Tstatus);
                                                startActivity(intent);
                                                return true;
                                            }
                                        });
                                        but3.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(TableActivity.this, OrderActivity.class);
                                                intent.putExtra("Tnumb", Tid);
                                                intent.putExtra("Tstat", Tstatus);
                                                startActivity(intent);
                                            }
                                        });

                                    }
                                    if (Tid == 104 &&  Tstatus < 1) {
                                        but4.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                                        but4.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(TableActivity.this, OrderActivity.class);
                                                intent.putExtra("Tnumb", Tid);
                                                intent.putExtra("Tstat", Tstatus);
                                                startActivity(intent);
                                            }
                                        });

                                    }
                                    else if (104 == Tid && Tstatus > 0  ) {
                                        but4.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                                        but4.setOnLongClickListener(new View.OnLongClickListener() {
                                            @Override
                                            public boolean onLongClick(View v) {
                                                Intent intent = new Intent(TableActivity.this, PaymentActivity.class);
                                                intent.putExtra("Tnumb", Tid);
                                                intent.putExtra("Tstat", Tstatus);
                                                startActivity(intent);
                                                return true;
                                            }
                                        });
                                        but4.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(TableActivity.this, OrderActivity.class);
                                                intent.putExtra("Tnumb", Tid);
                                                intent.putExtra("Tstat", Tstatus);
                                                startActivity(intent);
                                            }
                                        });

                                    }
                                    if (Tid == 105 &&  Tstatus < 1) {
                                        but5.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                                        but5.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(TableActivity.this, OrderActivity.class);
                                                intent.putExtra("Tnumb", Tid);
                                                intent.putExtra("Tstat", Tstatus);
                                                startActivity(intent);
                                            }
                                        });

                                    }
                                    else if (105 == Tid && Tstatus > 0  ) {
                                        but5.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                                        but5.setOnLongClickListener(new View.OnLongClickListener() {
                                            @Override
                                            public boolean onLongClick(View v) {
                                                Intent intent = new Intent(TableActivity.this, PaymentActivity.class);
                                                intent.putExtra("Tnumb", Tid);
                                                intent.putExtra("Tstat", Tstatus);
                                                startActivity(intent);
                                                return true;
                                            }
                                        });
                                        but5.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(TableActivity.this, OrderActivity.class);
                                                intent.putExtra("Tnumb", Tid);
                                                intent.putExtra("Tstat", Tstatus);
                                                startActivity(intent);
                                            }
                                        });

                                    }
                                    if (Tid == 106 &&  Tstatus < 1) {
                                        but6.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                                        but6.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(TableActivity.this, OrderActivity.class);
                                                intent.putExtra("Tnumb", Tid);
                                                intent.putExtra("Tstat", Tstatus);
                                                startActivity(intent);
                                            }
                                        });

                                    }
                                    else if (106 == Tid && Tstatus > 0  ) {
                                        but6.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                                        but6.setOnLongClickListener(new View.OnLongClickListener() {
                                            @Override
                                            public boolean onLongClick(View v) {
                                                Intent intent = new Intent(TableActivity.this, PaymentActivity.class);
                                                intent.putExtra("Tnumb", Tid);
                                                intent.putExtra("Tstat", Tstatus);
                                                startActivity(intent);
                                                return true;
                                            }
                                        });
                                        but6.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(TableActivity.this, OrderActivity.class);
                                                intent.putExtra("Tnumb", Tid);
                                                intent.putExtra("Tstat", Tstatus);
                                                startActivity(intent);
                                            }
                                        });

                                    }
                                    if (Tid == 107 &&  Tstatus < 1) {
                                        but7.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                                        but7.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(TableActivity.this, OrderActivity.class);
                                                intent.putExtra("Tnumb", Tid);
                                                intent.putExtra("Tstat", Tstatus);
                                                startActivity(intent);
                                            }
                                        });

                                    }
                                    else if (107 == Tid && Tstatus > 0  ) {
                                        but7.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                                        but7.setOnLongClickListener(new View.OnLongClickListener() {
                                            @Override
                                            public boolean onLongClick(View v) {
                                                Intent intent = new Intent(TableActivity.this, PaymentActivity.class);
                                                intent.putExtra("Tnumb", Tid);
                                                intent.putExtra("Tstat", Tstatus);
                                                startActivity(intent);
                                                return true;
                                            }
                                        });
                                        but7.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(TableActivity.this, OrderActivity.class);
                                                intent.putExtra("Tnumb", Tid);
                                                intent.putExtra("Tstat", Tstatus);
                                                startActivity(intent);
                                            }
                                        });

                                    }
                                    if (Tid == 108 &&  Tstatus < 1) {
                                        but8.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                                        but8.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(TableActivity.this, OrderActivity.class);
                                                intent.putExtra("Tnumb", Tid);
                                                intent.putExtra("Tstat", Tstatus);
                                                startActivity(intent);
                                            }
                                        });

                                    }
                                    else if (108 == Tid && Tstatus > 0  ) {
                                        but8.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                                        but8.setOnLongClickListener(new View.OnLongClickListener() {
                                            @Override
                                            public boolean onLongClick(View v) {
                                                Intent intent = new Intent(TableActivity.this, PaymentActivity.class);
                                                intent.putExtra("Tnumb", Tid);
                                                intent.putExtra("Tstat", Tstatus);
                                                startActivity(intent);
                                                return true;
                                            }
                                        });
                                        but8.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(TableActivity.this, OrderActivity.class);
                                                intent.putExtra("Tnumb", Tid);
                                                intent.putExtra("Tstat", Tstatus);
                                                startActivity(intent);
                                            }
                                        });

                                    }
                                    if (Tid == 109 &&  Tstatus < 1) {
                                        but9.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                                        but9.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(TableActivity.this, OrderActivity.class);
                                                intent.putExtra("Tnumb", Tid);
                                                intent.putExtra("Tstat", Tstatus);
                                                startActivity(intent);
                                            }
                                        });

                                    }
                                    else if (109 == Tid && Tstatus > 0  ) {
                                        but9.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                                        but9.setOnLongClickListener(new View.OnLongClickListener() {
                                            @Override
                                            public boolean onLongClick(View v) {
                                                Intent intent = new Intent(TableActivity.this, PaymentActivity.class);
                                                intent.putExtra("Tnumb", Tid);
                                                intent.putExtra("Tstat", Tstatus);
                                                startActivity(intent);
                                                return true;
                                            }
                                        });
                                        but9.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(TableActivity.this, OrderActivity.class);
                                                intent.putExtra("Tnumb", Tid);
                                                intent.putExtra("Tstat", Tstatus);
                                                startActivity(intent);
                                            }
                                        });

                                    }
                                }
                                while (c.moveToNext());

                            }


                        }
                    }

                }

            }


        });

    }
    }



