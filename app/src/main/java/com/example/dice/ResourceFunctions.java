package com.example.dice;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.MediaController;
import android.widget.VideoView;

import java.util.Random;


public class ResourceFunctions {
    MediaPlayer player;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    public void videoGetter(MainActivity mainActivity) {
        VideoView videoView = mainActivity.findViewById(R.id.videoView);
        String videoPath = "android.resource://" + mainActivity.getPackageName() + "/" + R.raw.the_tale_of_jiraiya_the_gallant;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);
        MediaController mediaController = new MediaController(mainActivity);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(mediaController);
    }
    public void alertGetter(MainActivity mainActivity) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setMessage(R.string.camera);
        builder.setPositiveButton(R.string.open, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openCamera(mainActivity);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }
    public void openCamera(MainActivity mainActivity){
        mainActivity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }
    public void play(MainActivity mainActivity) {
        if(player == null) {
            player = MediaPlayer.create(mainActivity.getApplicationContext(), R.raw.jiraiya_theme);
//            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    playerRelease();
//                }
//            });
        }
        player.start();
    }
    public void  pause() {
        if(player != null)
            player.pause();
    }
    void playerRelease() {
        if(player != null)
            player.release();
        player = null;
    }
    public void stop() {
        playerRelease();
    }
    public int random() {
        Random n = new Random();
        return(n.nextInt(5)+1);
    }
}
