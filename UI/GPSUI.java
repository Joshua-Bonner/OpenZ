import javax.swing.*;
import java.awt.event.*;

public class GPSUI
{
    private static OBDClient obd = new OBDClient();
	private static final int WIDTH = 800;
	private static final int HEIGHT = 480;

	private static SpringLayout layout_obd;
	private static SpringLayout layout_musicplayer;
	private static SpringLayout layout_gps;

	private static JFrame top_panel;

	private static JTabbedPane tab_panel;
	private static JPanel musicplayer_panel;
	private static JPanel obd_panel;
	private static JPanel gps_panel;

	private static JButton music_button_1;
	private static JButton music_button_2;
	private static JButton music_button_3;
	private static JButton music_button_prev;
	private static JButton music_button_next;
	private static JLabel music_label_1;

	private static JButton obd_1;
	private static JButton obd_2;
	private static JButton obd_3;
	private static JButton obd_4;
	private static JLabel obd_5;

	private static JButton showCodes;
	private static JButton clearCodes;

	private static JButton gps_home;
	private static JButton gps_destination;
	private static JButton gps_turn;
	private static JButton gps_eat;

	private static JButton album_cover;

	public static int ALBUM_COUNT = 15;
	public static int currentSong = 5;

	public static void main(String[] args)
	{
		layout_obd = new SpringLayout();
		layout_musicplayer = new SpringLayout();
		layout_gps = new SpringLayout();

		JFrame top_panel = new JFrame("H&B GPS Device");
		top_panel.setSize(WIDTH,HEIGHT);
		top_panel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		top_panel.setVisible(true);

		tab_panel = new JTabbedPane();
		tab_panel.setVisible(true);

		top_panel.add(tab_panel);

		obd_panel = new JPanel();
	    tab_panel.add("OBD",obd_panel);

		musicplayer_panel = new JPanel();
	    tab_panel.add("Music Player",musicplayer_panel);

	    gps_panel = new JPanel();
	    tab_panel.add("GPS",gps_panel);

	    obd_panel.setLayout(layout_obd);
	    musicplayer_panel.setLayout(layout_musicplayer);
	    gps_panel.setLayout(layout_gps);


	    music_button_1 = new JButton("Select Album");
	    musicplayer_panel.add(music_button_1);
	    music_button_1.setPreferredSize(new java.awt.Dimension(600,80));

	    music_button_2 = new JButton("Play");
	    musicplayer_panel.add(music_button_2);
	    music_button_2.setPreferredSize(new java.awt.Dimension(80,80));
	    music_button_2.setVisible(true);

	    music_button_3 = new JButton("Pause");
	    musicplayer_panel.add(music_button_3);
	    music_button_3.setPreferredSize(new java.awt.Dimension(80,80));
	    music_button_3.setVisible(false);

	    music_button_next = new JButton("Next");
	    musicplayer_panel.add(music_button_next);
	    music_button_next.setPreferredSize(new java.awt.Dimension(70,70));
	    music_button_next.setVisible(true);

	    music_button_prev = new JButton("Prev");
	    musicplayer_panel.add(music_button_prev);
	    music_button_prev.setPreferredSize(new java.awt.Dimension(70,70));
	    music_button_prev.setVisible(true);

	    music_label_1 = new JLabel("temp");
	    musicplayer_panel.add(music_label_1);


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

	    java.awt.Dimension obd_dim = new java.awt.Dimension(700,40);
	    obd_1 = new JButton("Real Time Parameters: 2345 RPM Current Temperature: XX Celsius");
	    obd_2 = new JButton("Speed: 100 MPH     Throttle Position: 50%");
	    obd_3 = new JButton("Vehicle Identification Number: 7H15154P....");
	    obd_4 = new JButton("Mileage: 50000");
	    obd_5 = new JLabel("");
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
	    showCodes.setPreferredSize(new java.awt.Dimension(150,40));
	    obd_panel.add(clearCodes);
	    clearCodes.setPreferredSize(new java.awt.Dimension(150,40));
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

	    gps_home.setPreferredSize(new java.awt.Dimension(155,50));
	    gps_destination.setPreferredSize(new java.awt.Dimension(155,50));
	    gps_turn.setPreferredSize(new java.awt.Dimension(155,50));
	    gps_eat.setPreferredSize(new java.awt.Dimension(155,50));

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
	    album_cover = new JButton() { public void paint(java.awt.Graphics g) { g.drawImage(img, 0, 0, 150, 150, null); } };
	    album_cover.setPreferredSize(new java.awt.Dimension(150,150));
	    musicplayer_panel.add(album_cover);

	    cons = layout_musicplayer.getConstraints(album_cover);
	    cons.setX(Spring.constant(325));
	    cons.setY(Spring.constant(0));


	    music_button_1.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { System.out.println("Album Select screen would trigger"); } } );
	    music_button_2.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { System.out.println("Started Playing"); music_button_2.setVisible(false); music_button_3.setVisible(true); }});
	    music_button_3.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { System.out.println("Paused Music"); music_button_2.setVisible(true); music_button_3.setVisible(false); }});
	    music_button_next.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { currentSong = (currentSong+1)%ALBUM_COUNT; music_label_1.setText("Playing song " + (currentSong+1) + "/" + ALBUM_COUNT); }});
	    music_button_prev.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { currentSong = (currentSong-1); if(currentSong<0) { currentSong=ALBUM_COUNT-1; } music_label_1.setText("Playing song " + (currentSong+1) + "/" + ALBUM_COUNT); }});
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
                }
                catch (OBDConnectionException ex) {
                    obd_5.setText(ex.getMessage());
                }

            }
        });
	}

	public static void updateCodes() {
	    try {
            obd_5.setText(obd.readCode());
        }
	    catch (OBDConnectionException e) {
	        obd_5.setText(e.getMessage());
        }
    }

}