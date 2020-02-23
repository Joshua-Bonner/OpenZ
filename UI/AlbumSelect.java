import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class AlbumSelect extends JFrame
{
	private ArrayList<ArrayList<String>> map;

	private JList<String> albumList;
	private JList<String> songList;
	private ArrayList<String> albumNames;



	public AlbumSelect()
	{
		//Container pane = this.getContentPane();
		//pane.setLayout(new BorderLayout());
		//pane.add(albumList,BorderLayout.WEST);
		//pane.add(songList,BorderLayout.EAST);
		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);
		setSize(800,600);
		map = new ArrayList<ArrayList<String>>();
		albumNames = new ArrayList<String>();
		albumList = new JList<String>();
		add(albumList);
		albumList.setVisible(true);
		songList = new JList<String>();
		songList.setVisible(true);
		add(songList);
		albumList.setSize(400,800);
		songList.setSize(400,800);
		SpringLayout.Constraints cons = layout.getConstraints(albumList);
		cons.setX(Spring.constant(0));
		cons.setY(Spring.constant(0));
		cons = layout.getConstraints(songList);
		cons.setX(Spring.constant(400));
		cons.setY(Spring.constant(0));
		JFrame thisPanel = this;
		albumList.addListSelectionListener(new ListSelectionListener() { public void valueChanged(ListSelectionEvent e) { songList.clearSelection(); songList.setListData(map.get(albumList.getSelectedIndex()).toArray(new String[0]));  }});
		songList.addListSelectionListener(new ListSelectionListener() { public void valueChanged(ListSelectionEvent e) { if (songList.getSelectedIndex() >= 0) { System.out.println("Playing song " + songList.getSelectedIndex() + " of album " + albumList.getSelectedIndex() + "!"); thisPanel.setVisible(false); }} });
	}

	public void addAlbum(String name)
	{
		map.add(new ArrayList<String>());
		albumNames.add(name);
	}

	public void addSong(String name, int albumIndex)
	{
		map.get(albumIndex).add(name);
	}

	public void refreshAlbums()
	{
		albumList.setListData(albumNames.toArray(new String[0]));
	}
}