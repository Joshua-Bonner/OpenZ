import javazoom.jl.decoder.JavaLayerException;

public class MusicControl implements Runnable{
    private musicPlayer player;
    private MusicDriver driver;
    private album albumChoice;
    private int albumIndx;
    private song songChoice;
    private Thread thread;
    private int frame;
    private boolean newSong;

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
            driver = new MusicDriver(songChoice.getSongLocation());
        } catch (JavaLayerException e) {
            System.err.println("AHHHH");
        }
        thread = new Thread(driver);
        thread.start();
        while (true) { // change this shit later

        }
    }
}
