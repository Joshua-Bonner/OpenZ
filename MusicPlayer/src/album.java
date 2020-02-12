import javazoom.spi.mpeg.sampled.file.MpegAudioFileFormat;
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.AbstractID3v2;
import org.farng.mp3.id3.ID3v1;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.util.Arrays;
import java.util.Map;

public class album {
    private String albumArtist;
    private String albumCoverLocation;
    private String albumLocation;
    private int currentSongIndex;
    private ArrayList<song> songs;

    private void trackSort(int tagVersion) throws IOException, TagException {
        switch (tagVersion) {
            case 0: // Unknown ID version
                break;
            case 1: // ID3v1
                ID3v1 tempID;
                MP3File tempMP3;
                int bucketLoc;

                song[] songBucket = new song[songs.size()];

                for (song s : songs ) {
                    tempMP3 = new MP3File(new File(s.getSongName()));
                    tempID = tempMP3.getID3v1Tag();

                    bucketLoc = Integer.parseInt(tempID.getTrackNumberOnAlbum());
                    songBucket[bucketLoc] =  s;
                }

                for (int i = 0; i < songs.size(); i++) {
                    songs.set(i, songBucket[i]);
                }
                break;
            case 2: // ID3v2
                AbstractID3v2 tempIDv2;
                MP3File tempMP3v2;
                int bucketLocv2;

                song[] songBucketv2 = new song[songs.size()];

                for (song s : songs ) {
                    tempMP3v2 = new MP3File(new File(s.getSongName()));
                    tempIDv2 = tempMP3v2.getID3v2Tag();

                    bucketLocv2 = Integer.parseInt(tempIDv2.getTrackNumberOnAlbum());
                    songBucketv2[bucketLocv2] =  s;
                }

                for (int i = 0; i < songs.size(); i++) {
                    songs.set(i, songBucketv2[i]);
                }
                break;
        }
        return;
    }

    public album() {
        albumArtist = "";
        albumCoverLocation = "";
        albumLocation = "";
        currentSongIndex = 0;
        songs = new ArrayList<>();

    }

    public album(String artist, String coverLoc, String albumLoc){
        albumArtist = artist;
        albumCoverLocation = coverLoc;
        albumLocation = albumLoc;
        currentSongIndex = 0;
        songs = new ArrayList<>();
        System.out.println("In Album Constructor");

        //#TODO Here: Fill AList w/ songs using albumLoc
        // https://stackoverflow.com/questions/1844688/how-to-read-all-files-in-a-folder-from-java
        // May be useful.. will need to type case "File" MP3 related object to obtain tags
        File folder = new File(albumLoc);
        String[] filesInDir = folder.list();
        String[] fileTokens;
        File tempFile;
        AudioFile base;
        MP3File tempMp3;
        Map properties;
        ID3v1 tempIDv1;
        AbstractID3v2 tempIDv2;
        int tagVersion = 0;

        if (filesInDir == null) {return;}

        for (String s : filesInDir) {
            fileTokens = s.split("\\.");
            if (fileTokens[fileTokens.length - 1].toUpperCase().equals("MP3")) {
                try {
                    tempFile = new File(albumLoc + s);
                    // issues getting time w/ VVVVVVVVVV tried mutiple packages
                    //base = AudioFileIO.read(new File(albumLoc + s));
                    tempMp3 = new MP3File(tempFile);

                    if (tempMp3.hasID3v1Tag()) {
                        tempIDv1 = tempMp3.getID3v1Tag();
                        songs.add(new song(1, albumLoc + s, tempIDv1.getSongTitle(), 1));
                        tagVersion = 1;
                    } else if (tempMp3.hasID3v2Tag()) {
                        tempIDv2 = tempMp3.getID3v2Tag();
                        songs.add(new song(1, albumLoc + s, tempIDv2.getSongTitle(), 2));
                        tagVersion = 2;
                    } else {
                        songs.add(new song(1, albumLoc + s, "UNKNOWN", 0));
                        tagVersion = 0;
                    }

                } catch (IOException e) {
                    System.err.println("ERROR FINDING FILE");
                } catch (TagException e) {
                    System.err.println("ERROR FINDING TAGS");
                }
            } else {
                System.err.println("I don't care about this file");
            }
        }
        try {
            trackSort(tagVersion);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TagException e) {
            e.printStackTrace();
        }
    }

    // Delete this later :) or not.. who knows!
    public void outAll() {
        for (song s : songs) {
            System.out.println(s.getSongName() + " By: " + albumArtist);
        }
    }
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
