import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.AbstractID3v2;
import org.farng.mp3.id3.ID3v1;

import java.io.IOException;
import java.util.ArrayList;
import java.io.File;

public class album {
    private String albumArtist;
    private String albumCoverLocation;
    private String albumLocation;
    private String albumName; // TODO: Modify Constructor to pull album name tags for this var
    private int currentSongIndex;
    private ArrayList<song> songs;

    private void trackSort(int tagVersion) throws IOException, TagException {
        String[] trackNumTokens;
        switch (tagVersion) {
            case 0: // Unknown ID version
                break;
            case 1: // ID3v1
                ID3v1 tempID;
                MP3File tempMP3;
                int bucketLoc;

                song[] songBucket = new song[songs.size()];

                for (song s : songs ) {
                    tempMP3 = new MP3File(new File(s.getSongLocation()));
                    tempID = tempMP3.getID3v1Tag();
                    trackNumTokens = (tempID.getTrackNumberOnAlbum()).split("/");
                    bucketLoc = Integer.parseInt(trackNumTokens[0]);
                    songBucket[bucketLoc - 1] =  s;
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
                    tempMP3v2 = new MP3File(new File(s.getSongLocation()));
                    tempIDv2 = tempMP3v2.getID3v2Tag();
                    trackNumTokens = (tempIDv2.getTrackNumberOnAlbum()).split("/");
                    bucketLocv2 = Integer.parseInt(trackNumTokens[0]);
                    songBucketv2[bucketLocv2 - 1] =  s;
                }

                for (int i = 0; i < songs.size(); i++) {
                    songs.set(i, songBucketv2[i]);
                }
                break;
        }
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

        boolean foundAlbumName;

        System.out.println("In Album Constructor");

        File folder = new File(albumLoc);
        String[] filesInDir = folder.list();
        String[] fileTokens;
        File tempFile;
        MP3File tempMp3;
        ID3v1 tempIDv1;
        AbstractID3v2 tempIDv2;
        int tagVersion = 0;

        if (filesInDir == null) {return;}

        for (String s : filesInDir) {
            fileTokens = s.split("\\.");
            if (fileTokens[fileTokens.length - 1].toUpperCase().equals("MP3")) {
                try {
                    tempFile = new File(albumLoc + s);
                    tempMp3 = new MP3File(tempFile);

                    if (tempMp3.hasID3v1Tag()) {
                        tempIDv1 = tempMp3.getID3v1Tag();
                        songs.add(new song(1, albumLoc + s, tempIDv1.getSongTitle(), 1));
                        albumName = tempIDv1.getAlbumTitle();
                        tagVersion = 1;
                    } else if (tempMp3.hasID3v2Tag()) {
                        tempIDv2 = tempMp3.getID3v2Tag();
                        songs.add(new song(1, albumLoc + s, tempIDv2.getSongTitle(), 2));
                        albumName = tempIDv2.getAlbumTitle();
                        tagVersion = 2;
                    } else {
                        songs.add(new song(1, albumLoc + s, "UNKNOWN", 0));
                        albumName = "UNKNOWN";
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
        int count = 1;
        for (song s : songs) {
            System.out.println(count + ". " + s.getSongName() + " By " + albumArtist);
            count++;
        }
    }
    public song getSong() {
        return songs.get(currentSongIndex);
    }
    public song getSong(int x) {
        return songs.get(x);
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

    public String getAlbumName() {return albumName; }

}
