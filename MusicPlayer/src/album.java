import org.farng.mp3.MP3File;

import java.util.ArrayList;
import java.io.File;

public class album {
    private String albumArtist;
    private String albumCoverLocation;
    private String albumLocation;
    private int currentSongIndex;
    private ArrayList<song> songs;

    public album() {
        albumArtist = "";
        albumCoverLocation = "";
        albumLocation = "";
        currentSongIndex = 0;
        songs = new ArrayList<>();

    }

    public album(String artist, String coverLoc, String albumLoc) {
        albumArtist = artist;
        albumCoverLocation = coverLoc;
        albumLocation = albumLoc;
        currentSongIndex = 0;

        //#TODO Here: Fill AList w/ songs using albumLoc
        // https://stackoverflow.com/questions/1844688/how-to-read-all-files-in-a-folder-from-java
        // May be useful.. will need to type case "File" MP3 related object to obtain tags
        File folder = new File(albumLoc);
        String[] filesInDir = folder.list();
        String[] fileTokens;

        for (int i = 0; i < filesInDir.length; i++) {
            fileTokens = filesInDir[i].split(".");

            if (fileTokens[1].equals("mp3")){

            }
        }
    }

    //#TODO Impliment these functions
    public song getSong(int songIndex) {
        return songs.get(songIndex);
    }

    public int nextSong() {
        currentSongIndex++;

        if (currentSongIndex >= songs.size())
            currentSongIndex = 0;

        return currentSongIndex;
    }

    public int previousSong() {
        currentSongIndex--;

        if (currentSongIndex < 0)
            currentSongIndex = songs.size() - 1;

        return currentSongIndex;
    }

    public String getCover() {
        return albumCoverLocation;
    }

    public String getArtist() {
        return albumArtist;
    }

    public String getLocation() {
        return albumLocation;
    }
}
