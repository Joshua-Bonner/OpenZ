import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MusicDriver extends PlaybackListener implements Runnable{
    private AdvancedPlayer player;
    private String songPath;
    private FileInputStream mp3File;
    private long startTime;
    private int pauseFrame = 0;
    private volatile int STATE = 0;
    private int songFrames = 0;
    private boolean trackFinished = false;

    public static final int PLAY_STATE      = 1;
    public static final int PAUSE_STATE     = 2;
    public static final int FINISHED_STATE  = 3;
    public static final int NEED_NEXT_STATE = 4;
    public static final int NO_STATE        = 0;

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
                pauseFrame += (int) ((System.currentTimeMillis() - startTime) / 26);
                System.out.println(pauseFrame);
            }
        });
    }
    public void run(){
        System.out.println("Running thread");
        startTime = System.currentTimeMillis();
        try {
            if (STATE == NO_STATE || STATE == FINISHED_STATE) {
                STATE = PLAY_STATE;
                player.play(0, Integer.MAX_VALUE);
                if (trackFinished) {
                    STATE = FINISHED_STATE;
                }
                else {
                    stopThread(true);
                }
            }
            else if (STATE == PAUSE_STATE) {
                STATE = PLAY_STATE;
                mp3File = new FileInputStream(songPath);
                player = new AdvancedPlayer(mp3File);
                player.setPlayBackListener(new PlaybackListener() {
                    @Override
                    public void playbackFinished(PlaybackEvent playbackEvent) {
                        pauseFrame  += (int) ((System.currentTimeMillis() - startTime) / 26);
                        System.out.println(pauseFrame);
                    }
                });
                System.out.println(pauseFrame);
                player.play(pauseFrame, Integer.MAX_VALUE);
                stopThread(true);
            }
        } catch (JavaLayerException | IOException e) {
            e.printStackTrace();
        }
    }
    public void stopThread(boolean calledInternally) {
        System.out.println("Do Only I call this?");
        if (STATE == PLAY_STATE) {
            STATE = PAUSE_STATE;
            if (calledInternally) {
                trackFinished = true;
                STATE = NEED_NEXT_STATE;
            }
            else {
                trackFinished = false;
                player.stop();
            }
        }
    }
    public void setPauseFrame(int newPause) {
        pauseFrame = newPause;
    }
    public int getSongFrames() throws BitstreamException, IOException {
        Bitstream bs = new Bitstream(mp3File);
        Header h = bs.readFrame();

        return (int) h.total_ms((int) mp3File.getChannel().size());
    }
    public int getState() {return STATE;}
    public void setState(int state) {STATE = state;}
}
