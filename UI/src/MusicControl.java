import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.JavaLayerException;

import java.io.IOException;

public class MusicControl implements Runnable{
    private musicPlayer player;
    private MusicDriver driver;
    private album albumChoice;
    private int albumIndx;
    private song songChoice;
    private long startTime = 0;
    private long elapsedTime = 0;
    private int frame;
    private int songLength;
    private boolean newSong = false;
    private boolean startTrackingTime = false;

    public volatile boolean killProcess = false;
    public volatile boolean readyToResume = false;
    public volatile boolean isRunning = false;

    public MusicControl() {
        player = new musicPlayer();
    }

    public String[] getAlbumList() {
        return player.outAll();
    }

    public String[] setAlbum(int indx) {
        albumChoice = player.getAlbum(indx);
        albumIndx = indx;
        return albumChoice.outAll();
    }

    public void setSongChoice(int indx) {
        songChoice = albumChoice.getSong(indx);
    }
    public String getAlbumCover() {
        System.out.println(albumIndx);
        return albumChoice.getCover();
    }
    @Override
    public void run() {
        try {
            if ( driver == null || driver.getState() != MusicDriver.PAUSE_STATE) {
                driver = new MusicDriver(songChoice.getSongLocation());
                startTrackingTime = true;
                startTime = System.currentTimeMillis();
            }
        } catch (JavaLayerException e) {
            System.err.println("AHHHH");
        }
        if (GPSUI.thread != null) {
            GPSUI.thread.interrupt();
        }

        GPSUI.thread = new Thread(driver, "driver");
        GPSUI.thread.start();
        isRunning = true;

        while (true) { // change this shit later

            if (newSong) {
                startTime = System.currentTimeMillis();
                elapsedTime = (System.currentTimeMillis() - startTime);
                newSong = false;
                startTrackingTime = true;
            }

            if (driver.getState() == MusicDriver.NEED_NEXT_STATE) {
                System.out.println("But soft what light through yonder window breaks");
                driver.stopThread(false);
                songChoice = albumChoice.getSong(albumChoice.nextSong());
                try {
                    driver = new MusicDriver(songChoice.getSongLocation());
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
                GPSUI.music_label_1.setText("Playing Song: " + songChoice.getSongName() + " | By: " + albumChoice.getArtist());
                GPSUI.thread = new Thread(driver, "Load New");
                GPSUI.thread.start();
                GPSUI.songTime.setValue(0);
                newSong = true;
                startTrackingTime = false;
            }

            if (driver.getState() == MusicDriver.NEED_PREV_STATE) {
                System.out.println("It is the east and Juliet is the Sun");
                driver.stopThread(false);
                songChoice = albumChoice.getSong(albumChoice.previousSong());
                try {
                    driver = new MusicDriver(songChoice.getSongLocation());
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
                GPSUI.music_label_1.setText("Playing Song: " + songChoice.getSongName() + " | By: " + albumChoice.getArtist());
                GPSUI.thread = new Thread(driver, "Load Prev");
                GPSUI.thread.start();
                GPSUI.songTime.setValue(0);
                newSong = true;
                startTrackingTime = false;
            }

            if (driver.getState() == MusicDriver.NEED_LOAD_STATE) {
                System.out.println("Arise Fair Sun and Kill the envious Moon");
                driver.stopThread(false);
                songChoice = albumChoice.getSong(albumChoice.nextSong());
                try {
                    driver = new MusicDriver(songChoice.getSongLocation());
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
                GPSUI.music_label_1.setText("Playing Song: " + songChoice.getSongName() + " | By: " + albumChoice.getArtist());
                GPSUI.thread = new Thread(driver, "Load New");
                GPSUI.thread.start();
                GPSUI.songTime.setValue(0);
                newSong = true;
                startTrackingTime = false;
            }

            if (driver.getState() == MusicDriver.PAUSE_STATE && readyToResume) {
                System.out.println("For She is sick and pale with grief");
                GPSUI.music_label_1.setText("Playing Song: " + songChoice.getSongName() + " | By: " + albumChoice.getArtist());
                GPSUI.thread = new Thread(driver, "Resume");
                GPSUI.thread.start();
                newSong = false;
                startTrackingTime = false;
                readyToResume = false;
            }

            if (killProcess) {
                killProcess = false;
                driver.stopThread(false);
                return;
            }

            if (!driver.getSongPath().equals(songChoice.getSongLocation())) {
                return;
            }
        }
    }

    public void loadNext()  {
        driver.stopThread(false);
        driver.setState(MusicDriver.NEED_NEXT_STATE);
    }

    public void loadPrev() {
        driver.stopThread(false);
        driver.setState(MusicDriver.NEED_PREV_STATE);
    }

    public String getSong() {
        return songChoice.getSongName();
    }

    public String getArtist() {
        return albumChoice.getArtist();
    }

    public void pause() {
        driver.stopThread(false);
        readyToResume = false;
    }


    public int getSongLength() throws BitstreamException, IOException {
        return driver.getSongFrames();
    }

    public void setDriverFrames(int frameNum) {
        driver.setPauseFrame(frameNum);
    }

}
