import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.JavaLayerException;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import java.io.IOException;

public class GPSUI {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 480;
    private static JButton obd_1, obd_2, obd_3, obd_4;
    private static JLabel obd_5;
    private static OBDClient obd;
    private static int ALBUM_COUNT = 15;
    private static int currentSong = 5;
    private static Thread thread;
    private static boolean initSong = false;

	private static MusicControl musicController = new MusicControl();

    public static void main(String[] args) {
        obd = new OBDClient();
		SpringLayout layout_obd;
        SpringLayout layout_musicplayer;
        SpringLayout layout_gps;

        JFrame top_panel;

        JTabbedPane tab_panel;
        JPanel musicplayer_panel;
        JPanel obd_panel;
        JPanel gps_panel;

        JButton music_button_1;
        JButton music_button_2;
        JButton music_button_3;
        JButton music_button_prev;
        JButton music_button_next;
        JLabel music_label_1;
        JLabel volumeLabel;
        JLabel startTime;
        JLabel endTime;
        JLabel currentTime;

        JButton showCodes;
        JButton clearCodes;

        JButton gps_home;
        JButton gps_destination;
        JButton gps_turn;
        JButton gps_eat;

        JSlider songTime;
        JSlider volumeSlider;

		JButton[] album_cover = new JButton[1];

        layout_obd = new SpringLayout();
        layout_musicplayer = new SpringLayout();
        layout_gps = new SpringLayout();

        top_panel = new JFrame("H&B GPS Device");
        top_panel.setSize(WIDTH, HEIGHT);
        top_panel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        top_panel.setVisible(true);

        tab_panel = new JTabbedPane();
        tab_panel.setVisible(true);

        top_panel.add(tab_panel);

        obd_panel = new JPanel();
        tab_panel.add("OBD", obd_panel);

        musicplayer_panel = new JPanel();
        tab_panel.add("Music Player", musicplayer_panel);

        gps_panel = new JPanel();
        tab_panel.add("GPS", gps_panel);

        obd_panel.setLayout(layout_obd);
        musicplayer_panel.setLayout(layout_musicplayer);
        gps_panel.setLayout(layout_gps);


        music_button_1 = new JButton("Select Album");
        musicplayer_panel.add(music_button_1);
        music_button_1.setPreferredSize(new java.awt.Dimension(600, 80));

        music_button_2 = new JButton("Play");
        musicplayer_panel.add(music_button_2);
        music_button_2.setPreferredSize(new java.awt.Dimension(80, 80));
        music_button_2.setVisible(true);

        music_button_3 = new JButton("Pause");
        musicplayer_panel.add(music_button_3);
        music_button_3.setPreferredSize(new java.awt.Dimension(80, 80));
        music_button_3.setVisible(false);

        music_button_next = new JButton("Next");
        musicplayer_panel.add(music_button_next);
        music_button_next.setPreferredSize(new java.awt.Dimension(70, 70));
        music_button_next.setVisible(true);

        music_button_prev = new JButton("Prev");
        musicplayer_panel.add(music_button_prev);
        music_button_prev.setPreferredSize(new java.awt.Dimension(70, 70));
        music_button_prev.setVisible(true);

        music_label_1 = new JLabel("temp");
        volumeLabel = new JLabel("Volume: 50");
        startTime = new JLabel("0:00");
        if (args.length > 0) {
            endTime = new JLabel(numToMS(Integer.parseInt(args[0])));
            songTime = new JSlider(0, Integer.parseInt(args[0]), 0);
        } else {
            endTime = new JLabel("0:30");
            songTime = new JSlider(0, 30, 0);
        }
        musicplayer_panel.add(music_label_1, null, JLabel.CENTER);
        currentTime = new JLabel("0:00");

        musicplayer_panel.add(volumeLabel);
        musicplayer_panel.add(startTime);
        musicplayer_panel.add(endTime);
        musicplayer_panel.add(currentTime);

        musicplayer_panel.add(songTime);
        songTime.setVisible(true);
        songTime.setPreferredSize(new java.awt.Dimension(400, 15));

        volumeSlider = new JSlider(0, 100, 50);
        musicplayer_panel.add(volumeSlider);
        volumeSlider.setVisible(true);
        volumeSlider.setPreferredSize(new java.awt.Dimension(300, 10));


        SpringLayout.Constraints cons = layout_musicplayer.getConstraints(music_button_1);
        cons.setX(Spring.constant(105));
        cons.setY(Spring.constant(300));


        cons = layout_musicplayer.getConstraints(music_button_2);
        cons.setX(Spring.constant(365));
        cons.setY(Spring.constant(165));

        cons = layout_musicplayer.getConstraints(music_button_3);
        cons.setX(Spring.constant(365));
        cons.setY(Spring.constant(165));

        cons = layout_musicplayer.getConstraints(music_button_prev);
        cons.setX(Spring.constant(280));
        cons.setY(Spring.constant(170));

        cons = layout_musicplayer.getConstraints(music_button_next);
        cons.setX(Spring.constant(460));
        cons.setY(Spring.constant(170));

        cons = layout_musicplayer.getConstraints(music_label_1);
        cons.setX(Spring.constant(355));
        cons.setY(Spring.constant(250));

        cons = layout_musicplayer.getConstraints(songTime);
        cons.setX(Spring.constant(220));
        cons.setY(Spring.constant(270));

        cons = layout_musicplayer.getConstraints(volumeLabel);
        cons.setX(Spring.constant(180));
        cons.setY(Spring.constant(145));

        cons = layout_musicplayer.getConstraints(startTime);
        cons.setX(Spring.constant(180));
        cons.setY(Spring.constant(267));

        cons = layout_musicplayer.getConstraints(endTime);
        cons.setX(Spring.constant(630));
        cons.setY(Spring.constant(267));

        cons = layout_musicplayer.getConstraints(currentTime);
        cons.setX(Spring.constant(400));
        cons.setY(Spring.constant(284));

        cons = layout_musicplayer.getConstraints(volumeSlider);
        cons.setX(Spring.constant(255));
        cons.setY(Spring.constant(150));

        java.awt.Dimension obd_dim = new java.awt.Dimension(700, 40);
        obd_1 = new JButton("Intake Pressure: ");
        obd_2 = new JButton("Intake Temperature: ");
        obd_3 = new JButton("Engine Load");
        obd_4 = new JButton("Mileage: 50000");
        obd_5 = new JLabel("Other statistics shown here");
        updateCodes();
        showCodes = new JButton("View Trouble Codes");
        clearCodes = new JButton("Clear Trouble Codes");
        obd_1.setPreferredSize(obd_dim);
        obd_panel.add(obd_1);
        obd_panel.add(obd_2);
        obd_panel.add(obd_3);
        obd_panel.add(obd_4);
        obd_2.setPreferredSize(obd_dim);
        obd_3.setPreferredSize(obd_dim);
        obd_4.setPreferredSize(obd_dim);

        obd_panel.add(obd_5);
        obd_panel.add(showCodes);
        showCodes.setPreferredSize(new java.awt.Dimension(150, 40));
        obd_panel.add(clearCodes);
        clearCodes.setPreferredSize(new java.awt.Dimension(150, 40));
        SpringLayout.Constraints cons2 = layout_obd.getConstraints(obd_1);
        cons2.setX(Spring.constant(20));
        cons2.setY(Spring.constant(20));

        cons2 = layout_obd.getConstraints(obd_2);
        cons2.setX(Spring.constant(20));
        cons2.setY(Spring.constant(80));

        cons2 = layout_obd.getConstraints(obd_3);
        cons2.setX(Spring.constant(20));
        cons2.setY(Spring.constant(300));

        cons2 = layout_obd.getConstraints(obd_4);
        cons2.setX(Spring.constant(20));
        cons2.setY(Spring.constant(360));

        cons2 = layout_obd.getConstraints(showCodes);
        cons2.setX(Spring.constant(380));
        cons2.setY(Spring.constant(250));

        cons2 = layout_obd.getConstraints(clearCodes);
        cons2.setX(Spring.constant(550));
        cons2.setY(Spring.constant(250));

        cons2 = layout_obd.getConstraints(obd_5);
        cons2.setX(Spring.constant(300));
        cons2.setY(Spring.constant(200));

        gps_home = new JButton("Go Home");
        gps_destination = new JButton("Enter Destination");
        gps_turn = new JButton("Next Turn: Olmsted 700 Feet");
        gps_eat = new JButton("EAT: 2 min");

        gps_panel.add(gps_home);
        gps_panel.add(gps_destination);
        gps_panel.add(gps_turn);
        gps_panel.add(gps_eat);

        gps_home.setPreferredSize(new java.awt.Dimension(155, 50));
        gps_destination.setPreferredSize(new java.awt.Dimension(155, 50));
        gps_turn.setPreferredSize(new java.awt.Dimension(155, 50));
        gps_eat.setPreferredSize(new java.awt.Dimension(155, 50));

        SpringLayout.Constraints cons3 = layout_gps.getConstraints(gps_home);
        cons3.setX(Spring.constant(10));
        cons3.setY(Spring.constant(350));

        cons3 = layout_gps.getConstraints(gps_destination);
        cons3.setX(Spring.constant(220));
        cons3.setY(Spring.constant(350));

        cons3 = layout_gps.getConstraints(gps_turn);
        cons3.setX(Spring.constant(410));
        cons3.setY(Spring.constant(350));

        cons3 = layout_gps.getConstraints(gps_eat);
        cons3.setX(Spring.constant(600));
        cons3.setY(Spring.constant(350));

        java.awt.Image img = java.awt.Toolkit.getDefaultToolkit().getImage("album_image.jpg");
        album_cover[0] = new JButton() {
            public void paint(java.awt.Graphics g) {
                g.drawImage(img, 0, 0, 150, 150, null);
            }
        };
        album_cover[0].setPreferredSize(new java.awt.Dimension(150, 150));
        musicplayer_panel.add(album_cover[0]);

        cons = layout_musicplayer.getConstraints(album_cover[0]);
        cons.setX(Spring.constant(325));
        cons.setY(Spring.constant(0));

        AlbumSelect album_panel = new AlbumSelect(musicController);
        album_panel.setVisible(false);
        album_panel.addComponentListener(new ComponentListener() {
			@Override
			public void componentResized(ComponentEvent componentEvent) {

			}

			@Override
			public void componentMoved(ComponentEvent componentEvent) {

			}

			@Override
			public void componentShown(ComponentEvent componentEvent) {

			}

			@Override
			public void componentHidden(ComponentEvent componentEvent) {
				System.out.println("SadnesS");
				java.awt.Image img = java.awt.Toolkit.getDefaultToolkit().getImage(musicController.getAlbumCover());
                // Paint the fun times album cover :) ask blum :( i am sad
                System.out.println("Started Playing");
                thread = new Thread(musicController);
                thread.start();
                music_label_1.setText("Playing Song: " + musicController.getSong() + " | By: " + musicController.getArtist());
                music_button_2.setVisible(false);
                music_button_3.setVisible(true);

                initSong = true;

			}
		});
        album_panel.refreshAlbums();

        music_button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                album_panel.setVisible(true);

            }
        });
        music_button_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (initSong) {
                    System.out.println("Started Playing");
                    thread = new Thread(musicController);
                    thread.start();
                    music_label_1.setText("Playing Song: " + musicController.getSong() + " | By: " + musicController.getArtist());
                    music_button_2.setVisible(false);
                    music_button_3.setVisible(true);
                }
            }
        });
        music_button_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Paused Music");
                musicController.pause();
                music_button_2.setVisible(true);
                music_button_3.setVisible(false);
            }
        });
        music_button_next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                musicController.pause();
                musicController.loadNext();
                thread.interrupt();
                thread = new Thread(musicController);
                thread.start();
                music_label_1.setText("Playing Song: " + musicController.getSong() + " | By: " + musicController.getArtist());
                if (music_button_2.isVisible()) {
                    music_button_2.setVisible(false);
                    music_button_3.setVisible(true);
                }
            }
        });
        music_button_prev.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                musicController.pause();
                musicController.loadPrev();
                thread.interrupt();
                thread = new Thread(musicController);
                thread.start();
                music_label_1.setText("Playing Song: " + musicController.getSong() + " | By: " + musicController.getArtist());
                if (music_button_2.isVisible()) {
                    music_button_2.setVisible(false);
                    music_button_3.setVisible(true);
                }
            }
        });
        songTime.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                System.out.println(songTime.getValue() + " (SONG)");
                currentTime.setText(numToMS(songTime.getValue()));
            }
        });
        volumeSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                System.out.println(volumeSlider.getValue() + " (VOLUME)");
                volumeLabel.setText("Volume: " + volumeSlider.getValue());
                try {
                    Runtime.getRuntime().exec("amixer -D pulse sset Master " + volumeSlider.getValue() + "%");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        showCodes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCodes();
            }
        });

        clearCodes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    obd.clearCode();
                    updateCodes();
                } catch (OBDConnectionException ex) {
                    obd_5.setText(ex.getMessage());
                }

            }
        });
    }

    public static String numToMS(int sec) {
        int minutes = sec / 60;
        int seconds = sec % 60;
        return minutes + ":" + ((seconds < 10) ? "0" : "") + seconds;
    }

    public static void updateCodes() {
        try {
            if (obd.isRunning()) {
                obd.stop();
            }
            obd_5.setText(obd.readCode());
        } catch (OBDConnectionException e) {
            obd_5.setText(e.getMessage());
        }
        obd.start();
    }

    public static void updateEngineLoad(String load) {
        obd_3.setText("Engine Load: " + load);
    }

    public static void updateIntakeTemp(String temp) {
        obd_2.setText("Intake Temp: " + temp);
    }

    public static void updateIntakePressure(String pressure) {
        obd_1.setText("Intake Pressure: " + pressure);
    }

}