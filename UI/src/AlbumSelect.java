import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class AlbumSelect extends JFrame
{
	private ArrayList<ArrayList<String>> map;
	private MusicControl musicControl;
	private JList<String> albumList;
	private JList<String> songList;
	private ArrayList<String> albumNames;

	//album buttons
	private JButton[] albumButtons = new JButton[10];
	private JButton albumScrollUp = new JButton();
	private JButton albumScrollDown = new JButton();
	//song buttons
	private JButton[] songButtons = new JButton[10];
	private JButton songScrollUp = new JButton();
	private JButton songScrollDown = new JButton();

	//dimensions for song selection button
	java.awt.Dimension select_dim = new java.awt.Dimension(330, 44);
	//dimensions for scrolling
	java.awt.Dimension scroll_dim = new java.awt.Dimension(80, 220);

	public AlbumSelect(MusicControl mc)
	{
		//Container pane = this.getContentPane();
		//pane.setLayout(new BorderLayout());
		//pane.add(albumList,BorderLayout.WEST);
		//pane.add(songList,BorderLayout.EAST);
		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);
		setSize(800,480);
		map = new ArrayList<ArrayList<String>>();
		musicControl = mc;
		albumNames = new ArrayList<String>();
		albumList = new JList<String>();
		add(albumList);
		albumList.setVisible(true);
		songList = new JList<String>();
		songList.setVisible(true);
		add(songList);
		albumList.setSize(400,480);
		songList.setSize(400,480);
		SpringLayout.Constraints cons = layout.getConstraints(albumList);
		cons.setX(Spring.constant(0));
		cons.setY(Spring.constant(0));
		cons = layout.getConstraints(songList);
		cons.setX(Spring.constant(400));
		cons.setY(Spring.constant(0));
		JFrame thisPanel = this;

		//layout all the buttons, starting with the album buttons
		for(int i = 0; i < albumButtons.length; i++) {
			albumButtons[i] = new JButton();
			albumButtons[i].setPreferredSize(select_dim);
			add(albumButtons[i]);
			SpringLayout.Constraints constraints = layout.getConstraints(albumButtons[i]);
			constraints.setX(Spring.constant(0));
			constraints.setY(Spring.constant(i * 44)); //this will place one button below the other
		}

		//add the scroll buttons for the albums
		SpringLayout.Constraints scrollCons;
		albumScrollDown.setPreferredSize(scroll_dim);
		add(albumScrollDown);
		scrollCons = layout.getConstraints(albumScrollDown);
		scrollCons.setX(Spring.constant(330));
		scrollCons.setY(Spring.constant(220));
		albumScrollUp.setPreferredSize(scroll_dim);
		add(albumScrollUp);
		scrollCons = layout.getConstraints(albumScrollUp);
		scrollCons.setX(Spring.constant(330));
		scrollCons.setY(Spring.constant(0));


		
		albumList.addListSelectionListener(new ListSelectionListener() { 
			public void valueChanged(ListSelectionEvent e) { 
				songList.clearSelection();
				String[] tmp = musicControl.setAlbum(albumList.getSelectedIndex());
				for (int i=0; i<tmp.length; i++)
				{
				if (tmp[i].length() > 50)
				{
					tmp[i] = tmp[i].substring(0,50) + "...";
				}
			}
				songList.setListData(tmp);
			}
		});

		songList.addListSelectionListener(new ListSelectionListener() { 
			public void valueChanged(ListSelectionEvent e) {
				if (songList.getSelectedIndex() >= 0) {
					musicControl.setSongChoice(songList.getSelectedIndex());
					thisPanel.setVisible(false);
				}
			}
		});
	}

	public void refreshAlbums()
	{
		String[] tmp = musicControl.getAlbumList();
		for (int i=0; i<tmp.length; i++)
		{
			if (tmp[i].length() > 50)
			{
				albumNames.add(tmp[i].substring(0,50) + "...");
			}
			else {
				albumNames.add(tmp[i]);
			}
		}
	}
}