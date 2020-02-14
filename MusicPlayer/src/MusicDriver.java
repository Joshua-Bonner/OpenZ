import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MusicDriver extends PlaybackListener implements Runnable{
    private AdvancedPlayer player;
    private String songPath;
    private FileInputStream mp3File;
    private int pauseFrame = 0;

    public MusicDriver(String path) throws JavaLayerException {
        songPath = path;
        try {
            mp3File = new FileInputStream(songPath);
        }catch (FileNotFoundException ex) {
            System.err.println("I really doubt this will happen");
        }
        player = new AdvancedPlayer(mp3File);
        player.setPlayBackListener(new PlaybackListener() {
            @Override
            public void playbackFinished(PlaybackEvent playbackEvent) {
                pauseFrame = playbackEvent.getFrame();
            }
        });
    }
    public void run(){
        System.out.println("Running thread");
        try {
            player.play();
        } catch (JavaLayerException e) {
            System.err.println("Error playing song");
        }
    }
}
