public class song{
    private long songLength;
    private String songLocation;
    private String songName;
    private int tagType;

    public song(){
        songLength = 0;
        songLocation = "";
        songName = "";
        tagType = 0;
    }

    public song(long length, String location, String name, int tagVersion){
        songLength = length;
        songLocation = location;
        songName = name;
        tagType = tagVersion;
    }

    public long getSongLength(){
        return songLength;
    }

    public String getSongLocation(){
        return songLocation;
    }

    public String getSongName(){
        return songName;
    }
}
