import java.io.File;
import java.util.ArrayList;

public class musicPlayer {
    private int albumIndex;
    private ArrayList<album> albums;
    private int currentAlbumIndex;
    private int librarySize;
    private boolean isPaused;
    private int pausedSongTime;
    private String musicLib;

    public musicPlayer(){
        musicLib = "/home/jacob/Music/";
        albumIndex = 0;
        albums = new ArrayList<>();
        currentAlbumIndex = 0;
        librarySize = 0;
        isPaused = false;
        pausedSongTime = 0;

        File musicDir = new File(musicLib);
    }

    public album getAlbum(int albumIndex){
        return albums.get(albumIndex);
    }

    public int nextAlbum(){
        currentAlbumIndex++;
        if (currentAlbumIndex >= albums.size()) currentAlbumIndex = 0;
        return currentAlbumIndex;
    }

    public void pause(){
        //TODO: Implement this method
        isPaused = true;
    }

    public void play(){
        //TODO: Implement this method
    }

    public void nextSong(){
        //TODO: Implement this method
    }

    public void previousSong(){
        //TODO: Implement this method
    }

    public void selectAlbum(int selection){
        //TODO: Implement this method
    }

    public void syncUSB(){
        //TODO: Implement this method
    }

    public void drawVisualizer(){
        //TODO: Implement this method
    }
}
