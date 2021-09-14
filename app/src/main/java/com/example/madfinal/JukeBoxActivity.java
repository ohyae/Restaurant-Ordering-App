package com.example.madfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.media.MediaPlayer;
import java.lang.String;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JukeBoxActivity extends AppCompatActivity {
    ImageButton requestbtn;
    ImageButton play;
    ImageButton pause;
    TextView tvstatus;
    TextView mp3;
    TextView song;
    TextView singer;
    String url =  "http://mad.mywork.gr/get_song.php?t=430123";
    String message;
    String message1;
    String message2;
    final MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juke_box);
        requestbtn = findViewById(R.id.btn_request);
        pause = findViewById(R.id.btn_pause);
        play = findViewById(R.id.btn_play);
        tvstatus = findViewById(R.id.tv_status);
        mp3 = findViewById(R.id.artist3);
        song = findViewById(R.id.song);
        singer = findViewById(R.id.Singer);
        requestbtn.setEnabled(true);
        pause.setEnabled(false);
        play.setEnabled(false);
        tvstatus.setText("Stopped");
        requestbtn.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        //request for a song
        requestbtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v)
            {

                requestbtn.setEnabled(false); //Request_btn is disabled after requesting a song
                requestbtn.getBackground().setColorFilter(null);
                pause.setEnabled(false);  //Pause and play buttons disabled during the request for a song
                pause.getBackground().setColorFilter(null);
                play.setEnabled(false);
                play.getBackground().setColorFilter(null);
                tvstatus.setText("Requesting song from CTower");  //tv_status during the request for a song

                //Contact to server
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
                            JukeBoxActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Parsing the XML response (artist name, title of a song and url)
                                    String opentag = "<url>";
                                    String closetag = "</url>";
                                    String XMLCode = myResponse;
                                    int p1 = XMLCode.indexOf(opentag) + opentag.length();
                                    int p2 = XMLCode.indexOf(closetag);
                                    message = XMLCode.substring(p1, p2);
                                    mp3.setText(message);

                                    String opentag1 = "<artist>";
                                    String closetag1 = "</artist>";
                                    String XMLCode1 = myResponse;
                                    int p3 = XMLCode1.indexOf(opentag1) + opentag1.length();
                                    int p4 = XMLCode1.indexOf(closetag1);
                                    message1 = XMLCode1.substring(p3, p4);
                                    singer.setText(message1);

                                    String opentag2 = "<title>";
                                    String closetag2 = "</title>";
                                    String XMLCode2 = myResponse;
                                    int p5 = XMLCode2.indexOf(opentag2) + opentag2.length();
                                    int p6 = XMLCode2.indexOf(closetag2);
                                    message2 = XMLCode2.substring(p5, p6);
                                    song.setText(message2);

                                    // Playing the song via MediapLayer
                                    try {
                                        if (mediaPlayer!=null) {
                                            mediaPlayer.stop(); //Current music must be stopped
                                            mediaPlayer.reset(); //Before a setDataSource call it needs to reset MP obj.
                                            mediaPlayer.setDataSource(message);
                                            mediaPlayer.prepare();
                                            mediaPlayer.start(); // New song must be started to play
                                            tvstatus.setText("Playing");
                                        }else {

                                            mediaPlayer.reset(); //Before a setDataSource call, it needs to reset MP obj.
                                            mediaPlayer.setDataSource(message);
                                            mediaPlayer.prepare();
                                            mediaPlayer.start();
                                            tvstatus.setText("Playing");
                                        }
                                    } catch (Exception e) {

                                        e.printStackTrace();
                                    }

                                    requestbtn.setEnabled(true); //Request_btn is enabled after starting music
                                    requestbtn.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);

                                    pause.setEnabled(true); //Pause_btn is enabled when a song is playing
                                    pause.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);

                                    //Pausing the song
                                    pause.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mediaPlayer.pause();
                                            tvstatus.setText("Stopped");
                                            pause.setEnabled(false);
                                            pause.getBackground().setColorFilter(null);
                                            play.setEnabled(true); //Play_btn is enabled when no song is playing
                                            play.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
                                        }
                                    });

                                    //Playing the song after the pause
                                    play.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mediaPlayer.start();
                                            tvstatus.setText("Playing");
                                            play.setEnabled(false);
                                            play.getBackground().setColorFilter(null);
                                            pause.setEnabled(true);
                                            pause.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);


                                        }
                                    });



                                }


                            });

                        }

                    }
                });

            }

        });

    }
    @Override
    protected void onStop() {
        super.onStop();
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            pause.setEnabled(false);
            pause.getBackground().setColorFilter(null);
            play.setEnabled(true);
            play.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
            tvstatus.setText("Stopped");


        }
    }

}
