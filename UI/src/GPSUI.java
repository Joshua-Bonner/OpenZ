import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.JavaLayerException;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import java.io.IOException;

public class GPSUI {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 480;
    private static JButton obd_1, obd_2, obd_3, obd_4;
    private static JLabel obd_5;
    public static JLabel music_label_1;
    private static OBDClient obd;
    private static int ALBUM_COUNT = 15;
    private static int currentSong = 5;

    private static ImageIcon albumArt;
    private static boolean initSong = false;

    public static JLabel startTime;
    public static JLabel endTime;
    public static JLabel currentTime;
    public static JSlider songTime;
    public static Thread thread;
    private static JSlider volumeSlider;
    private static JLabel volumeLabel;

    private static long songLengthMilli = 0;

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

        JButton showCodes;
        JButton clearCodes;

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
        tab_panel.add("                    OBD                    ", obd_panel);

        musicplayer_panel = new JPanel();
        tab_panel.add("                Music Player                 ", musicplayer_panel);

        gps_panel = new JPanel();
        tab_panel.add("                   GPS                  ", gps_panel);

        JPanel settings_panel = new JPanel();
        tab_panel.add("                  Settings                  ", settings_panel);


        JSlider colorR = new JSlider(0,255,34);
        JSlider colorG = new JSlider(0,255,34);
        JSlider colorB = new JSlider(0,255,34);
        JLabel colorR_lab = new JLabel("Red Val: 34");
        JLabel colorG_lab = new JLabel("Green Val: 34");
        JLabel colorB_lab = new JLabel("Blue Val: 34");
        JSlider textR = new JSlider(0,255,34);
        JSlider textG = new JSlider(0,255,34);
        JSlider textB = new JSlider(0,255,34);
        JLabel textR_lab = new JLabel("Text Red: 34");
        JLabel textG_lab = new JLabel("Text Green: 34");
        JLabel textB_lab = new JLabel("Text Blue: 34");
        settings_panel.add(colorR);
        settings_panel.add(colorG);
        settings_panel.add(colorB);
        settings_panel.add(colorR_lab);
        settings_panel.add(colorG_lab);
        settings_panel.add(colorB_lab);
        settings_panel.add(textR);
        settings_panel.add(textG);
        settings_panel.add(textB);
        settings_panel.add(textR_lab);
        settings_panel.add(textG_lab);
        settings_panel.add(textB_lab);

        colorR.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                musicplayer_panel.setBackground(new Color(colorR.getValue(),colorG.getValue(),colorB.getValue()));
                obd_panel.setBackground(new Color(colorR.getValue(),colorG.getValue(),colorB.getValue()));
                settings_panel.setBackground(new Color(colorR.getValue(),colorG.getValue(),colorB.getValue()));
                gps_panel.setBackground(new Color(colorR.getValue(),colorG.getValue(),colorB.getValue()));
                colorR_lab.setText("Red Val: " + colorR.getValue());
                Color col = new Color(colorR.getValue(),colorG.getValue(),colorB.getValue());
                colorR.setBackground(col);
                colorG.setBackground(col);
                colorB.setBackground(col);
                textR.setBackground(col);
                textG.setBackground(col);
                textB.setBackground(col);
                volumeSlider.setBackground(col);
                songTime.setBackground(col);
            }
        });

        colorG.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                musicplayer_panel.setBackground(new Color(colorR.getValue(),colorG.getValue(),colorB.getValue()));
                obd_panel.setBackground(new Color(colorR.getValue(),colorG.getValue(),colorB.getValue()));
                settings_panel.setBackground(new Color(colorR.getValue(),colorG.getValue(),colorB.getValue()));
                gps_panel.setBackground(new Color(colorR.getValue(),colorG.getValue(),colorB.getValue()));
                colorG_lab.setText("Green Val: " + colorG.getValue());

                Color col = new Color(colorR.getValue(),colorG.getValue(),colorB.getValue());
                colorR.setBackground(col);
                colorG.setBackground(col);
                colorB.setBackground(col);
                textR.setBackground(col);
                textG.setBackground(col);
                textB.setBackground(col);
                volumeSlider.setBackground(col);
                songTime.setBackground(col);
            }
        });

        colorB.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                musicplayer_panel.setBackground(new Color(colorR.getValue(),colorG.getValue(),colorB.getValue()));
                obd_panel.setBackground(new Color(colorR.getValue(),colorG.getValue(),colorB.getValue()));
                settings_panel.setBackground(new Color(colorR.getValue(),colorG.getValue(),colorB.getValue()));
                gps_panel.setBackground(new Color(colorR.getValue(),colorG.getValue(),colorB.getValue()));
                colorB_lab.setText("Blue Val: " + colorB.getValue());

                Color col = new Color(colorR.getValue(),colorG.getValue(),colorB.getValue());
                colorR.setBackground(col);
                colorG.setBackground(col);
                colorB.setBackground(col);
                textR.setBackground(col);
                textG.setBackground(col);
                textB.setBackground(col);
                volumeSlider.setBackground(col);
                songTime.setBackground(col);
            }
        });

        textR.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                textR_lab.setText("Text Red: " + textR.getValue());
                Color col = new Color(textR.getValue(),textG.getValue(),textB.getValue());
                startTime.setForeground(col);
                endTime.setForeground(col);
                currentTime.setForeground(col);
                colorR_lab.setForeground(col);
                colorG_lab.setForeground(col);
                colorB_lab.setForeground(col);
                textR_lab.setForeground(col);
                textB_lab.setForeground(col);
                textG_lab.setForeground(col);
                volumeLabel.setForeground(col);
                music_label_1.setForeground(col);
            }
        });

        textG.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                textG_lab.setText("Text Green: " + textG.getValue());
                Color col = new Color(textR.getValue(),textG.getValue(),textB.getValue());
                startTime.setForeground(col);
                endTime.setForeground(col);
                currentTime.setForeground(col);
                colorR_lab.setForeground(col);
                colorG_lab.setForeground(col);
                colorB_lab.setForeground(col);
                textR_lab.setForeground(col);
                textB_lab.setForeground(col);
                textG_lab.setForeground(col);
                volumeLabel.setForeground(col);
                music_label_1.setForeground(col);
            }
        });

        textB.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                textB_lab.setText("Text Blue: " + textB.getValue());
                Color col = new Color(textR.getValue(),textG.getValue(),textB.getValue());
                startTime.setForeground(col);
                endTime.setForeground(col);
                currentTime.setForeground(col);
                colorR_lab.setForeground(col);
                colorG_lab.setForeground(col);
                colorB_lab.setForeground(col);
                textR_lab.setForeground(col);
                textB_lab.setForeground(col);
                textG_lab.setForeground(col);
                volumeLabel.setForeground(col);
                music_label_1.setForeground(col);
            }
        });

        obd_panel.setLayout(layout_obd);
        musicplayer_panel.setLayout(layout_musicplayer);
        gps_panel.setLayout(layout_gps);

        obd_panel.setBackground(new Color(34,34,34));
        musicplayer_panel.setBackground(new Color(34,34,34));

        music_button_1 = new JButton();
        music_button_1.setIcon(new ImageIcon("music_albumselect.png"));
        musicplayer_panel.add(music_button_1);
        music_button_1.setPreferredSize(new java.awt.Dimension(600, 80));

        music_button_2 = new JButton();
        music_button_2.setIcon(new ImageIcon("music_play.png"));
        musicplayer_panel.add(music_button_2);
        music_button_2.setPreferredSize(new java.awt.Dimension(80, 80));
        music_button_2.setVisible(true);

        music_button_3 = new JButton();
        music_button_3.setIcon(new ImageIcon("music_pause.png"));
        musicplayer_panel.add(music_button_3);
        music_button_3.setPreferredSize(new java.awt.Dimension(80, 80));
        music_button_3.setVisible(false);

        music_button_next = new JButton();
        music_button_next.setIcon(new ImageIcon("music_next.png"));
        musicplayer_panel.add(music_button_next);
        music_button_next.setPreferredSize(new java.awt.Dimension(70, 70));
        music_button_next.setVisible(true);

        music_button_prev = new JButton();
        music_button_prev.setIcon(new ImageIcon("music_prev.png"));
        musicplayer_panel.add(music_button_prev);
        music_button_prev.setPreferredSize(new java.awt.Dimension(70, 70));
        music_button_prev.setVisible(true);

        music_label_1 = new JLabel("temp");
        music_label_1.setForeground(new Color(255,200,200));

        volumeLabel = new JLabel("Volume: 50");
        volumeLabel.setForeground(new Color(255,200,200));

        try {
            // uncomment this for jar compiling for the pi
            //Runtime.getRuntime().exec("amixer set PCM -- 50%");
            // and then comment this for the pi VVVVVV is for Jacob testing
            Runtime.getRuntime().exec("amixer -D pulse sset Master " + 50 + "%");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        startTime = new JLabel("0%");
        startTime.setForeground(new Color(255,200,200));

        endTime = new JLabel("100%");
        endTime.setForeground(new Color(255,200,200));
        
        songTime = new JSlider(0, 100, 0);
        songTime.setForeground(new Color(255,0,0));
        songTime.setBackground(new Color(34,34,34));

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
        volumeSlider.setForeground(new Color(255,0,0));
        musicplayer_panel.add(volumeSlider);
        volumeSlider.setVisible(true);
        volumeSlider.setPreferredSize(new java.awt.Dimension(300, 10));


        SpringLayout.Constraints cons = layout_musicplayer.getConstraints(music_button_1);
        cons.setX(Spring.constant(104));
        cons.setY(Spring.constant(299));


        cons = layout_musicplayer.getConstraints(music_button_2);
        cons.setX(Spring.constant(364));
        cons.setY(Spring.constant(164));

        cons = layout_musicplayer.getConstraints(music_button_3);
        cons.setX(Spring.constant(364));
        cons.setY(Spring.constant(164));

        cons = layout_musicplayer.getConstraints(music_button_prev);
        cons.setX(Spring.constant(279));
        cons.setY(Spring.constant(169));

        cons = layout_musicplayer.getConstraints(music_button_next);
        cons.setX(Spring.constant(459));
        cons.setY(Spring.constant(169));

        cons = layout_musicplayer.getConstraints(music_label_1);
        cons.setX(Spring.constant(24));
        cons.setY(Spring.constant(249));

        cons = layout_musicplayer.getConstraints(songTime);
        cons.setX(Spring.constant(219));
        cons.setY(Spring.constant(269));

        cons = layout_musicplayer.getConstraints(volumeLabel);
        cons.setX(Spring.constant(179));
        cons.setY(Spring.constant(144));

        cons = layout_musicplayer.getConstraints(startTime);
        cons.setX(Spring.constant(179));
        cons.setY(Spring.constant(266));

        cons = layout_musicplayer.getConstraints(endTime);
        cons.setX(Spring.constant(629));
        cons.setY(Spring.constant(266));

        cons = layout_musicplayer.getConstraints(currentTime);
        cons.setX(Spring.constant(399));
        cons.setY(Spring.constant(283));

        cons = layout_musicplayer.getConstraints(volumeSlider);
        cons.setX(Spring.constant(254));
        cons.setY(Spring.constant(149));

        SpringLayout layout_settings = new SpringLayout();
        settings_panel.setLayout(layout_settings);
        cons = layout_settings.getConstraints(colorR);
        cons.setX(Spring.constant(160));
        cons.setY(Spring.constant(100));
        cons = layout_settings.getConstraints(colorG);
        cons.setX(Spring.constant(160));
        cons.setY(Spring.constant(135));
        cons = layout_settings.getConstraints(colorB);
        cons.setX(Spring.constant(160));
        cons.setY(Spring.constant(170));
        cons = layout_settings.getConstraints(colorR_lab);
        cons.setX(Spring.constant(50));
        cons.setY(Spring.constant(100));
        cons = layout_settings.getConstraints(colorG_lab);
        cons.setX(Spring.constant(50));
        cons.setY(Spring.constant(135));
        cons = layout_settings.getConstraints(colorB_lab);
        cons.setX(Spring.constant(50));
        cons.setY(Spring.constant(170));

        cons = layout_settings.getConstraints(textR);
        cons.setX(Spring.constant(400));
        cons.setY(Spring.constant(100));
        cons = layout_settings.getConstraints(textG);
        cons.setX(Spring.constant(400));
        cons.setY(Spring.constant(135));
        cons = layout_settings.getConstraints(textB);
        cons.setX(Spring.constant(400));
        cons.setY(Spring.constant(170));
        cons = layout_settings.getConstraints(textR_lab);
        cons.setX(Spring.constant(600));
        cons.setY(Spring.constant(100));
        cons = layout_settings.getConstraints(textG_lab);
        cons.setX(Spring.constant(600));
        cons.setY(Spring.constant(135));
        cons = layout_settings.getConstraints(textB_lab);
        cons.setX(Spring.constant(600));
        cons.setY(Spring.constant(170));

        java.awt.Dimension obd_dim = new java.awt.Dimension(700, 40);
        // intake pressure
        obd_1 = new JButton("0");
        obd_1.setHorizontalTextPosition(SwingConstants.CENTER);
        obd_1.setForeground(new Color(255,0,0));
        obd_1.setIcon(new ImageIcon("obd_pressure.png"));
        // intake temp
        obd_2 = new JButton("0");
        obd_2.setHorizontalTextPosition(SwingConstants.CENTER);
        obd_2.setForeground(new Color(255,0,0));
        obd_2.setIcon(new ImageIcon("obd_temperature.png"));
        // engine load
        obd_3 = new JButton("0");
        obd_3.setHorizontalTextPosition(SwingConstants.CENTER);
        obd_3.setForeground(new Color(255,0,0));
        obd_3.setIcon(new ImageIcon("obd_engine.png"));
        // Throttle Position
        obd_4 = new JButton();

        obd_5 = new JLabel("Other statistics shown here");
        obd_5.setForeground(new Color(255,0,0));

        updateCodes();
        showCodes = new JButton();
        showCodes.setIcon(new ImageIcon("obd_show.png"));
        clearCodes = new JButton();
        clearCodes.setIcon(new ImageIcon("obd_clear.png"));
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

        album_cover[0] = new JButton();
        album_cover[0].setIcon(new ImageIcon(
                                (new ImageIcon("album_image.jpg")).getImage()
                                        .getScaledInstance(150, 150,  java.awt.Image.SCALE_SMOOTH)));
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
                albumArt = new ImageIcon(musicController.getAlbumCover());
                album_cover[0].setIcon(new ImageIcon(albumArt.getImage()
                                                     .getScaledInstance(150,150, Image.SCALE_SMOOTH)));
                if (thread != null) {
                    thread.stop();
                    if (musicController.getDriverState() == MusicDriver.PAUSE_STATE) {
                        musicController.driverNewSongOnPause();
                    }
                }

                music_button_2.setVisible(false);
                music_button_3.setVisible(true);

                thread = new Thread(musicController, "New Song");
                thread.start();
                music_label_1.setText("Playing Song: " + musicController.getSong() + " | By: " + musicController.getArtist());
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
                    musicController.readyToResume = true;
                    music_label_1.setText("Playing Song: " + musicController.getSong() + " | By: " + musicController.getArtist());
                    music_button_2.setVisible(false);
                    music_button_3.setVisible(true);
                }
            }
        });
        music_button_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                musicController.pause();
                music_button_2.setVisible(true);
                music_button_3.setVisible(false);
            }
        });
        music_button_next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                musicController.loadNext();
                if (music_button_2.isVisible()) {
                    music_button_2.setVisible(false);
                    music_button_3.setVisible(true);
                }
            }
        });
        music_button_prev.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                musicController.loadPrev();
                if (music_button_2.isVisible()) {
                    music_button_2.setVisible(false);
                    music_button_3.setVisible(true);
                }
            }
        });
        songTime.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (!((JSlider) e.getSource()).getValueIsAdjusting()) {
                    int skipFrame = 0;
                    try {
                        currentTime.setText(songTime.getValue() + "%");
                        skipFrame = (int) ((songTime.getValue() * 1.0 / 100) * musicController.getSongLength());
                    } catch (BitstreamException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    // breaks something w/ this me thinks
                    musicController.pause();
                    musicController.killProcess = true;
                    musicController.setDriverFrames(skipFrame / 26);
                    thread = new Thread(musicController, "Time Changer");
                    thread.start();
                }
            }
        });
        volumeSlider.setBackground(new Color(34,34,34));
        volumeSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                volumeLabel.setText("Volume: " + volumeSlider.getValue());
                try {
                    // uncomment this for jar compiling for the pi
                    //Runtime.getRuntime().exec("amixer set PCM -- " + volumeSlider.getValue() + "%");
                    // and then comment this for the pi VVVVVV is for Jacob testing
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

    public static String numToMS(long sec) {
        long minutes = sec / 60;
        long seconds = sec % 60;
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

    public static void updateThrottlePos(String throttle) {
        obd_4.setText("Throttle Position: " + throttle);
    }

}