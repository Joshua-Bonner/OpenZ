public class song{
	private int songLength;
	private String songLocation;
	private String songName;
	
	public song(){
  		songLength = 0;
  		songLocation = "";
  		songName = "";
  	}

	public song(int length, String location, String name){
		songLength = length;
		songLocation = location;
		songName = name;
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
