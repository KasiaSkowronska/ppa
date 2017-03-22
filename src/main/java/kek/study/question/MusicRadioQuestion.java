package kek.study.question;

import javafx.scene.Node;
import javafx.scene.control.ToggleGroup;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Created by Konrad on 2017-01-02.
 */
public class MusicRadioQuestion extends RadioQuestion {

    // doesn't work. Need to be rethinked.
    // I think it need his own mediaPlayer with one track given by factory, when created.

    public MediaPlayer mediaPlayer;

    public MusicRadioQuestion(Node renderedQuestion, String id, ToggleGroup group, MediaPlayer mediaPlayer) {
        super(renderedQuestion, id, group);
        this.mediaPlayer = mediaPlayer;

    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }



    public void runTrack(int i){
//        String path =  folderPath + Integer.toString(i) + ".mp3";
        mediaPlayer.setMute(false);
        mediaPlayer.setAutoPlay(true);
        MediaView mediaView = new MediaView(mediaPlayer);
    }

    public void terminateTrack(){
        mediaPlayer.setMute(true);
    }

    @Override
    public void terminate(){
        terminateTrack();
    }

}