
public class Driver {
    public static void main(String[] args) {
        System.out.println("wow");

        musicPlayer player = new musicPlayer();

        album test = player.getAlbum(0);
        song test2 = test.getSong();

        try {
            Thread thread = new Thread(new MusicDriver(test2.getSongLocation()));
            thread.start();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}