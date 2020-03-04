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
		
		albumList.addListSelectionListener(new ListSelectionListener() { 
			public void valueChanged(ListSelectionEvent e) { 
				songList.clearSelection();
				songList.setListData(musicControl.setAlbum(albumList.getSelectedIndex()));
			}
		});

		songList.addListSelectionListener(new ListSelectionListener() { 
			public void valueChanged(ListSelectionEvent e) {
				if (songList.getSelectedIndex() >= 0) {
					musicControl.setSongChoice(songList.getSelectedIndex());
					System.out.println("Playing song " + songList.getSelectedIndex() + " of album " + albumList.getSelectedIndex() + "!");
					thisPanel.setVisible(false);

				}
			}
		});
	}

	public void refreshAlbums()
	{
		albumList.setListData(musicControl.getAlbumList());
	}
}
