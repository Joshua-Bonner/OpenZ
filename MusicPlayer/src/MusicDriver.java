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
    private int STATE = 0;
    private boolean trackFinished = false;

    public static final int PLAY_STATE     = 1;
    public static final int PAUSE_STATE    = 2;
    public static final int FINISHED_STATE = 3;
    public static final int NO_STATE       = 0;

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
                System.out.println(pauseFrame);
                trackFinished = true;
            }
        });
    }
    public void run(){
        System.out.println("Running thread");
        try {
            if (STATE == NO_STATE || STATE == FINISHED_STATE) {
                STATE = PLAY_STATE;
                player.play(0, Integer.MAX_VALUE);
                if (trackFinished) {
                    STATE = FINISHED_STATE;
                }
                else {
                    stopThread();
                }
            }
            else if (STATE == PAUSE_STATE) {
                STATE = PLAY_STATE;
                mp3File = new FileInputStream(songPath);
                player = new AdvancedPlayer(mp3File);
                player.setPlayBackListener(new PlaybackListener() {
                    @Override
                    public void playbackFinished(PlaybackEvent playbackEvent) {
                        pauseFrame = playbackEvent.getFrame();
                        System.out.println(pauseFrame);
                        trackFinished = true;
                    }
                });
                System.out.println(pauseFrame);
                player.play(pauseFrame, Integer.MAX_VALUE);
                //STATE = FINISHED_STATE;
                stopThread();
            }
        } catch (JavaLayerException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void stopThread() {
        System.out.println("STOPPING THREAD");
        if (STATE == PLAY_STATE) {
            STATE = PAUSE_STATE;
            player.stop();
            trackFinished = false;
        }
    }
    public void startThread() throws JavaLayerException {
        System.out.println(STATE);
        if (STATE == NO_STATE || STATE == FINISHED_STATE) {
            STATE = PLAY_STATE;
            player.play(0, Integer.MAX_VALUE);
            STATE = FINISHED_STATE;
            stopThread();
        }
        else if (STATE == PAUSE_STATE) {
            STATE = PLAY_STATE;
            player.play(pauseFrame, Integer.MAX_VALUE);
            STATE = FINISHED_STATE;
            stopThread();
        }
    }
}
