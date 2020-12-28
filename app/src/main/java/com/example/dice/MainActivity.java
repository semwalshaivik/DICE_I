package com.example.dice;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Random;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {
    public int y=0;
    public static final int PERMISSION_REQUEST=0;
    public static final int RESULT_LOAD_IMAGE=1;
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView imageView;
    Button button2;
    MediaPlayer player;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private FusedLocationProviderClient client;
    public void gpsGetter(View view) {
        setContentView(R.layout.activity_get_gps);
        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(this);
        Button button = findViewById(R.id.getLocation);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    }
    public void alertGetter(View view) {
        setContentView(R.layout.activity_get_alert);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(R.string.camera);
        builder.setPositiveButton(R.string.open, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openCamera(view);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showDice(view);
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }
    public void videoGetter(View view) {
        setContentView(R.layout.activity_play_video);
        VideoView videoView = findViewById(R.id.videoView);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.the_tale_of_jiraiya_the_gallant;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(mediaController);
    }
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
            public void onClick(View v) {
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
    public void showDice(View view){
        setContentView(R.layout.activity_main);
    }
    public void openCamera(View view){
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }
    public void play(View view) {
        if(player == null) {
            player = MediaPlayer.create(this, R.raw.jiraiya_theme);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    playerRelease();
                }
            });
        }
        player.start();
    }
    public void  pause(View view) {
        if(player != null)
            player.pause();
    }
    public void stop(View view) {
        playerRelease();
    }
    private void playerRelease() {
        if(player != null)
            player.release();
        player = null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        playerRelease();
    }
    public int random() {
        Random n = new Random();
        return(n.nextInt(5)+1);
    }

    public void changeImage(View view){
        if(player!=null)
            playerRelease();
        int x = random();
        if(y==0) {
            if (x == 1)
                imageGetter(view);
            if (x == 2)
                videoGetter(view);
            if (x == 3)
                alertGetter(view);
            if (x == 4)
                setContentView(R.layout.activity_message4);
            if (x == 5)
                setContentView(R.layout.activity_thankyou);
            if (x == 6)
                gpsGetter();
            y = x;
        }
        else
        {
            while(x==y)
                x = random();
            if (x == 1)
                imageGetter(view);
            if (x == 2)
                videoGetter(view);
            if (x == 3)
                alertGetter(view);
            if (x == 4)
                setContentView(R.layout.activity_message4);
            if (x == 5)
                setContentView(R.layout.activity_thankyou);
            if(x == 6)
                gpsGetter();
            y=x;
        }
    }
}