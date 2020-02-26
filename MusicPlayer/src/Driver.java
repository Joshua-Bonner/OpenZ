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
            boolean newSong = true;
            int frames = 0;
            while (choice != 0) {
                System.out.println("%%%%%%%%%% OPENZ TUI PLAYER %%%%%%%%%%");
                System.out.println("%%%  0 - EXIT                      %%%");
                System.out.println("%%%  1 - PAUSE                     %%%");
                System.out.println("%%%  2 - START                     %%%");
                System.out.println("%%%  3 - NEXT                      %%%");
                System.out.println("%%%  4 - PREV                      %%%");
                System.out.println("%%%  5 - NEW ALBUM                 %%%");
                System.out.println("%%%  6 - WAIT                      %%%");
                System.out.println("%%%  7 - SELECT POSITION IN SONG   %%%");
                System.out.println("%%%  8 - CHANGE VOLUME             %%%");
                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                System.out.println("%%% " + "PLAYING: "  + albumChoice.getSong().getSongName() + " BY: "
                                          + albumChoice.getArtist() + "  FROM: " + albumChoice.getAlbumName() + " %%%");

                if (newSong) {
                    frames = musicDriver.getSongFrames();
                    newSong = false;
                }
                System.out.println("DURATION: " + frames);

                choice = Integer.parseInt(input.readLine());

                if (choice == 1) {
                    musicDriver.stopThread(false);
                }
                else if (choice == 2) {
                    thread = new Thread(musicDriver);
                    thread.start();
                }
                else if (choice  == 3) {
                    newSong = true;
                    musicDriver.stopThread(false);
                    musicDriver = new MusicDriver(albumChoice.getSong(player.nextSong()).getSongLocation());
                    thread = new Thread(musicDriver);
                    thread.start();
                }
                else if (choice == 4) {
                    newSong = true;
                    musicDriver.stopThread(false);
                    musicDriver = new MusicDriver(albumChoice.getSong(player.previousSong()).getSongLocation());
                    thread = new Thread(musicDriver);
                    thread.start();
                }
                else if (choice == 5) {
                    newSong = true;
                    player.outAll();

                    choice = Integer.parseInt(input.readLine());
                    albumChoice = player.getAlbum(choice - 1);

                    albumChoice.outAll();

                    choice = Integer.parseInt(input.readLine());
                    songChoice = albumChoice.getSong(choice - 1);

                    musicDriver.stopThread(false);
                    musicDriver = new MusicDriver(songChoice.getSongLocation());
                    thread = new Thread(musicDriver);
                    thread.start();
                }
                else if (choice == 6) {
                    if (musicDriver.getState() == MusicDriver.FINISHED_STATE) {
                        newSong = true;
                        musicDriver.stopThread(false);
                        musicDriver = new MusicDriver(albumChoice.getSong(player.nextSong()).getSongLocation());
                        thread = new Thread(musicDriver);
                        thread.start();
                    }
                }
                else if (choice == 7) {
                    System.out.println("0% -> 100% Of the way through the song?");
                    choice = Integer.parseInt(input.readLine());
                    musicDriver.stopThread(false);
                    int pauseFrame = (int) ((choice * 1.0 / 100) * frames);
                    musicDriver.setPauseFrame(pauseFrame / 26);
                    thread = new Thread(musicDriver);
                    thread.start();
                }
                else if (choice == 8) {
                    System.out.println("0% -> 100% Volume");
                    choice = Integer.parseInt(input.readLine());
                    Runtime.getRuntime().exec("amixer -D pulse sset Master " + choice + "%");
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