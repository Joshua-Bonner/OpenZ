import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Driver {
    public static void main(String[] args) throws IOException {
        System.out.println("wow");

        musicPlayer player = new musicPlayer();

        player.outAll();
        BufferedReader input = new BufferedReader ( new InputStreamReader( System.in ) );
        int choice;
        choice = Integer.parseInt(input.readLine());

        album albumChoice = player.getAlbum(choice - 1);

        albumChoice.outAll();

        choice = Integer.parseInt(input.readLine());

        song songChoice = albumChoice.getSong(choice - 1);

        try {
            MusicDriver musicDriver = new MusicDriver(songChoice.getSongLocation());
            Thread thread = new Thread(musicDriver);
            thread.start();

            while (choice != 0) {
                System.out.println("%%%%%%%%%%%%%%%%%%% CONTROLS %%%%%%%%%%%%%%%%%%%%%%%");
                System.out.println("0 - Exit");
                System.out.println("1 - PAUSE");
                System.out.println("2 - START");
                System.out.println("3 - NEXT");
                System.out.println("4 - PREV");
                System.out.println("5 - NEW ALBUM");
                choice = Integer.parseInt(input.readLine());

                if (choice == 1) {
                    musicDriver.stopThread();
                }
                else if (choice == 2) {
                    thread = new Thread(musicDriver);
                    thread.start();
                }
                else if (choice  == 3) {
                    musicDriver.stopThread();
                    musicDriver = new MusicDriver(albumChoice.getSong(player.nextSong()).getSongLocation());
                    thread = new Thread(musicDriver);
                    thread.start();
                }
                else if (choice == 4) {
                    musicDriver.stopThread();
                    musicDriver = new MusicDriver(albumChoice.getSong(player.previousSong()).getSongLocation());
                    thread = new Thread(musicDriver);
                    thread.start();
                }
                else if (choice == 5) {
                    player.outAll();

                    choice = Integer.parseInt(input.readLine());
                    albumChoice = player.getAlbum(choice - 1);

                    albumChoice.outAll();

                    choice = Integer.parseInt(input.readLine());
                    songChoice = albumChoice.getSong(choice - 1);

                    musicDriver.stopThread();
                    musicDriver = new MusicDriver(songChoice.getSongLocation());
                    thread = new Thread(musicDriver);
                    thread.start();
                }
                else {
                    choice = 0;
                    thread.interrupt();
                    System.exit(0);
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }




    }
}