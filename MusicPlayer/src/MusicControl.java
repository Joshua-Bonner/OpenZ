public class MusicControl implements Runnable{
    private musicPlayer player;
    private album albumChoice;
    private song songChoice;
    private Thread thread;
    private int frame;
    private boolean newSong;

    public MusicControl() {
        player = new musicPlayer();
        thread = new Thread();
    }

    public String[] getAlbumList() {
        return player.outAll();
    }

    public String[] setAlbum(int indx) {
        albumChoice = player.getAlbum(indx);
        return albumChoice.outAll();
    }
    @Override
    public void run() {

    }
}
