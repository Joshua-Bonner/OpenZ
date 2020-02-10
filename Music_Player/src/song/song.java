public class song{
	private int songLength;
	private String songLocation;
	private String songName;
	
  song(){
  	songLength = this.getSongLength();
  	songLocation = this.getSongLocation();
  	getSongName = this.getSongName();
  }
	
	public int getSongLength(){
		return songLength;
	}
	
	public String getSongLocation(){
		return songLocation;
	}
	
	public String getSongName(){
		return songName;
	}
}
