import javax.swing.*;

public class GPSUI
{
	private static final int WIDTH = 800;
	private static final int HEIGHT = 480;

	private static JFrame top_panel;

	private static JTabbedPane tab_panel;
	private static JPanel musicplayer_panel;
	private static JPanel obd_panel;
	private static JPanel gps_panel;

	private static JButton music_button_1;
	private static JButton music_button_2;
	private static JButton obd_button_1;

	public static void main(String[] args)
	{
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

	    music_button_1 = new JButton("Album Art");
	    musicplayer_panel.add(music_button_1);
	    music_button_1.setPreferredSize(new java.awt.Dimension(300,300));

	    music_button_2 = new JButton("Visualizer");
	    musicplayer_panel.add(music_button_2);
	    music_button_2.setPreferredSize(new java.awt.Dimension(300,200));

	    obd_button_1 = new JButton("OBD Example Button");
	    obd_panel.add(obd_button_1);
	    obd_button_1.setPreferredSize(new java.awt.Dimension(300,300));

	}

}