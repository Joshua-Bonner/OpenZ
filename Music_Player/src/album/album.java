import java.util.ArrayList;

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
		songs = new ArrayList<song>();

	}

	public album(String artist, String coverLoc, String albumLoc) {
		albumArtist = artist;
		albumCoverLocation = coverLoc;
		albumLocation = albumLoc;
		currentSongIndex = 0;

		//#TODO Here: Fill AList w/ songs using albumLoc
		// https://stackoverflow.com/questions/1844688/how-to-read-all-files-in-a-folder-from-java
		// May be useful.. will need to type case "File" MP3 related object to obtain tags
	}

	//#TODO Impliment these functions
	public song getSong(int) {
		// return song from index from array
		// usage: For display on UI
	}

	public int nextSong() {
		// return song index of next song
		// usage: To begin playing next song from MusicPlayer class
	}

	public int previousSong() {
		// return song index of prev song
		// usage: To begin playing previous song from MusicPlayer class
	}

	public String getCover() {
	
	}
	
	public String getArtist() {
	
	}

	public String getLocation() {
	
	}
}
