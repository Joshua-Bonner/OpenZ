public class Driver {
    public static void main(String[] args) {
        System.out.println("wow");

        album test = new album("Death Grips", "/home/jacob/Music/Death Grips — Year Of The Snitch (2018)/cover.jpg",
                "/home/jacob/Music/Death Grips — Year Of The Snitch (2018)/");
        test.outAll();

        musicPlayer player = new musicPlayer();

    }
}