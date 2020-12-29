package com.example.dice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {
    ResourceFunctions resourceFunctions = new ResourceFunctions();
    public int y=0;
    public static final int PERMISSION_REQUEST=0;
    public static final int RESULT_LOAD_IMAGE=1;
    ImageView imageView;
    Button button2;
    Button play;
    Button pause;
    Button stop;
    MediaPlayer player;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    /*private FusedLocationProviderClient client;
    public void gpsGetter(View view) {
        setContentView(R.layout.activity_get_gps);
        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(this);
        Button button = findViewById(R.id.getLocation);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                    return;
                }
                client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        if (o != null) {
                            TextView textView = findViewById(R.id.location);
                            textView.setText(o.toString());
                        }
                    }
                });
            }
        });
    }
    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }*/
    public void imageGetter(View view)
    {
        setContentView(R.layout.activity_get_image);
        imageView = (ImageView) findViewById(R.id.imageView);
        button2 = (Button) findViewById(R.id.button2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
        }
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_LOAD_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePatchColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePatchColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePatchColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
//    public void showDice(View view){
//        setContentView(R.layout.activity_main);
//    }
//    public void play(View view) {
//        if(player == null) {
//            player = MediaPlayer.create(this, R.raw.jiraiya_theme);
//            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    playerRelease();
//                }
//            });
//        }
//        player.start();
//    }
//    public void  pause(View view) {
//        if(player != null)
//            player.pause();
//    }
//    public void stop(View view) {
//        resourceFunctions.playerRelease();
//    }
//    void playerRelease() {
//        if(player != null)
//            player.release();
//        player = null;
//    }

    @Override
    protected void onStop() {
        super.onStop();
        resourceFunctions.playerRelease();
    }
//    public int random() {
//        Random n = new Random();
//        return(n.nextInt(5)+1);
//    }

    public void changeImage(View view){
        if(player!=null)
            resourceFunctions.playerRelease();
        int x = resourceFunctions.random();
        if(y==0) {
            if (x == 1)
                imageGetter(view);
            if (x == 2) {
                setContentView(R.layout.activity_play_video);
                resourceFunctions.videoGetter(MainActivity.this);
            }
            if (x == 3) {
                setContentView(R.layout.activity_get_alert);
                resourceFunctions.alertGetter(MainActivity.this);
            }
            if (x == 4) {
                setContentView(R.layout.activity_message4);
                play = (Button) findViewById(R.id.play);
                pause = (Button) findViewById(R.id.pause);
                stop = (Button) findViewById(R.id.stop);
                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        resourceFunctions.play(MainActivity.this);
                    }
                });
                pause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        resourceFunctions.pause();
                    }
                });

                stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resourceFunctions.stop();
                    }
                });
            }
            if (x == 5)
                setContentView(R.layout.activity_thankyou);
            //if (x == 6)
                //gpsGetter(view);
            y = x;
        }
        else
        {
            while(x==y)
                x = resourceFunctions.random();
            if (x == 1)
                imageGetter(view);
            if (x == 2) {
                setContentView(R.layout.activity_play_video);
                resourceFunctions.videoGetter(MainActivity.this);
            }
            if (x == 3) {
                setContentView(R.layout.activity_get_alert);
                resourceFunctions.alertGetter(MainActivity.this);
            }
            if (x == 4) {
                setContentView(R.layout.activity_message4);

                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        resourceFunctions.play(MainActivity.this);
                    }
                });
                pause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        resourceFunctions.pause();
                    }
                });
                stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        resourceFunctions.stop();
                    }
                });
            }
            if (x == 5)
                setContentView(R.layout.activity_thankyou);
            //if(x == 6)
                //gpsGetter(view);
            y=x;
        }
    }
}