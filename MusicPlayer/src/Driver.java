import javazoom.jl.decoder.JavaLayerException;

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

            while (true) {
                System.out.println("%%%%%%%%%%%%%%%%%%% CONTROLS %%%%%%%%%%%%%%%%%%%%%%%");
                System.out.println("1 - PAUSE");
                System.out.println("2 - START");
                choice = Integer.parseInt(input.readLine());

                if (choice == 1) {
                    musicDriver.stopThread();
                }
                else {
                    thread = new Thread(musicDriver);
                    thread.start();
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }




    }
}