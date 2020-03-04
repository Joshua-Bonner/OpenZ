import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.JavaLayerException;

import java.io.IOException;

public class MusicControl implements Runnable{
    private musicPlayer player;
    private MusicDriver driver;
    private album albumChoice;
    private int albumIndx;
    private song songChoice;
    private Thread thread;
    private int frame;
    private int songLength;
    private boolean newSong = false;

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
            }
        } catch (JavaLayerException e) {
            System.err.println("AHHHH");
        }
        if (thread != null) {
            thread.stop();
        }

        try {
            GPSUI.endTime.setText(GPSUI.numToMS(driver.getSongFrames() / 1000));
        } catch (BitstreamException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        thread = new Thread(driver);
        thread.start();

        while (true) { // change this shit later
            if (newSong) {
                try {
                    Thread.sleep(500);
                    GPSUI.endTime.setText(GPSUI.numToMS(driver.getSongFrames() / 1000));
                } catch (BitstreamException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                newSong = false;
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
                thread = new Thread(driver);
                thread.start();
                newSong = true;
            }
        }
    }

    public void loadNext()  {
        driver.stopThread(false);
        songChoice = albumChoice.getSong(albumChoice.nextSong());
        driver.setState(MusicDriver.FINISHED_STATE);
    }

    public void loadPrev() {
        driver.stopThread(false);
        songChoice = albumChoice.getSong(albumChoice.previousSong());
        driver.setState(MusicDriver.FINISHED_STATE);
    }

    public String getSong() {
        return songChoice.getSongName();
    }

    public String getArtist() {
        return albumChoice.getArtist();
    }

    public void pause() {
        driver.stopThread(false);
    }


    public int getSongLength() throws BitstreamException, IOException {
        return driver.getSongFrames();
    }

    public void setDriverFrames(int frameNum) {
        driver.setPauseFrame(frameNum);
    }
}
