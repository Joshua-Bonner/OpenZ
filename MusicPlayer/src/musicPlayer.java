import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.AbstractID3v2;
import org.farng.mp3.id3.ID3v1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class musicPlayer {
    private int albumIndex;
    private ArrayList<album> albums;
    private int currentAlbumIndex;
    private int librarySize;
    private int pausedSongTime;
    private String musicLib;

    public musicPlayer(){
        musicLib = "/home/jacob/Music/";
        albumIndex = 0;
        albums = new ArrayList<>();
        currentAlbumIndex = 0;
        librarySize = 0;
        pausedSongTime = 0;

        ArrayList<File> albumDirs = new ArrayList<File>();
        File musicDir = new File(musicLib);

        for (String folder : Objects.requireNonNull(musicDir.list())) {
            albumDirs.add(new File(musicDir + "/" + folder + "/"));
        }

        String albumLocation;
        String albumArtist;
        String coverLocation;
        String tempLocation;
        String fileExtension;
        MP3File tempMp3;
        ID3v1 tempID1;
        AbstractID3v2 tempID2;
        boolean pulledTag = false;
        boolean pulledCover = false;
        String[] songTokens;

        for (File folder : albumDirs) {
            albumArtist = "UNKNOWN";
            coverLocation = "UNKNOWN";
            albumLocation = folder.toString() + "/";
            for (String fileName : folder.list()) {
                tempLocation = folder.toString() + "/" + fileName;

                songTokens = fileName.split("\\.");
                fileExtension = songTokens[songTokens.length - 1].toUpperCase();

                if (fileExtension.equals("MP3") && !(pulledTag)) {
                    try {
                        tempMp3 = new MP3File(new File(tempLocation));
                        if (tempMp3.hasID3v1Tag()) {
                            pulledTag = true;
                            tempID1 = tempMp3.getID3v1Tag();
                            albumArtist = tempID1.getArtist();
                        }
                        else if (tempMp3.hasID3v2Tag()) {
                            pulledTag = true;
                            tempID2 = tempMp3.getID3v2Tag();
                            albumArtist = tempID2.getLeadArtist();
                        }
                    } catch (IOException e) {
                        System.err.println("ERROR WITH FILE");
                    } catch (TagException e) {
                        System.err.println("ERROR WITH TAGS");
                    }
                }
                else if ((fileExtension.equals("PNG") || fileExtension.equals("JPG") || fileExtension.equals("JPEG")) && !(pulledCover)) {
                    pulledCover = true;
                    coverLocation = tempLocation;
                }

                if (pulledCover && pulledTag) {
                    albums.add(new album(albumArtist, coverLocation, albumLocation ));
                    pulledCover = false;
                    pulledTag = false;
                    break;
                }
            }
        }


    }

    public album getAlbum(int albumIndex){
        return albums.get(albumIndex);
    }

    public void outAll() {
        for (album alb : albums) {
            alb.outAll();
            System.out.println("////////");
        }
    }

    public int nextAlbum(){
        currentAlbumIndex++;
        if (currentAlbumIndex >= albums.size()) currentAlbumIndex = 0;
        return currentAlbumIndex;
    }

    public void pause(){
        //TODO: Implement this method
    }

    public void play()  {
        //TODO: Implement this method
        song currentSong;

        currentSong = albums.get(currentAlbumIndex).getSong();

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
