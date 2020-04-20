import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class AlbumSelect extends JFrame {
    private ArrayList<ArrayList<String>> map;
    private MusicControl musicControl;
    private ArrayList<String> albumNames;
    private ArrayList<String> songNames;

    //album buttons
    private JButton[] albumButtons = new JButton[10];
    private JButton albumScrollUp = new JButton();
    private JButton albumScrollDown = new JButton();
    //song buttons
    private JButton[] songButtons = new JButton[10];
    private JButton songScrollUp = new JButton();
    private JButton songScrollDown = new JButton();

    //positions for song and album
    int albumPos, songPos, selectedAlbum = -1;

    //dimensions for song selection button
    java.awt.Dimension select_dim = new java.awt.Dimension(310, 44);
    //dimensions for scrolling
    java.awt.Dimension scroll_dim = new java.awt.Dimension(80, 220);

    public AlbumSelect(MusicControl mc) {
        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        setSize(800, 480);
        map = new ArrayList<ArrayList<String>>();
        musicControl = mc;
        albumNames = new ArrayList<String>();
        songNames = new ArrayList<>();

        //layout all the buttons, starting with the album buttons
        for (int i = 0; i < albumButtons.length; i++) {
            //TODO add coloring here
            albumButtons[i] = new JButton();
            albumButtons[i].setPreferredSize(select_dim);
            add(albumButtons[i]);
            int finalI = i;
            albumButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleAlbumAction(finalI); //we do it this way so we can get the position of the button that triggered the action, the only other way to do this is to get the text of the button but that could cause issues
                }
            });
            SpringLayout.Constraints constraints = layout.getConstraints(albumButtons[i]);
            constraints.setX(Spring.constant(0));
            constraints.setY(Spring.constant(i * 44)); //this will place one button below the other
        }

        //add the scroll buttons for the albums
        //TODO create icons for the scroll buttons
        //TODO color buttons
        SpringLayout.Constraints scrollCons;
        albumScrollDown.setPreferredSize(scroll_dim);
        add(albumScrollDown);
        scrollCons = layout.getConstraints(albumScrollDown);
        scrollCons.setX(Spring.constant(310));
        scrollCons.setY(Spring.constant(220));
        albumScrollDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(albumPos + albumButtons.length < albumNames.size()) { //only change if we have more albums to populate
                    albumPos += albumButtons.length;
                    populateAlbums();
                }
            }
        });
        albumScrollUp.setPreferredSize(scroll_dim);
        add(albumScrollUp);
        scrollCons = layout.getConstraints(albumScrollUp);
        scrollCons.setX(Spring.constant(310));
        scrollCons.setY(Spring.constant(0));
        albumScrollUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                albumPos -= 10;
                if(albumPos < 0) {
                    albumPos = 0; //makes sure we don't go too far
                }
                populateAlbums();
            }
        });

        //now layout all of the song buttons
        for (int i = 0; i < albumButtons.length; i++) {
            //TODO add coloring here
            songButtons[i] = new JButton();
            songButtons[i].setPreferredSize(select_dim);
            add(songButtons[i]);
            int finalI = i;
            songButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleSongAction(finalI);
                }
            });
            SpringLayout.Constraints constraints = layout.getConstraints(songButtons[i]);
            constraints.setX(Spring.constant(390));
            constraints.setY(Spring.constant(i * 44));
        }

        //now add the scroll buttons for the songs
        //TODO create icons for the scroll buttons
        //TODO color buttons
        songScrollDown.setPreferredSize(scroll_dim);
        add(songScrollDown);
        scrollCons = layout.getConstraints(songScrollDown);
        scrollCons.setX(Spring.constant(700));
        scrollCons.setY(Spring.constant(220));
        songScrollDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(songPos + songButtons.length < songNames.size()) { //only scroll down if we'll see more songs
                    songPos += songButtons.length;
                    populateSongs();
                }
            }
        });
        songScrollUp.setPreferredSize(scroll_dim);
        add(songScrollUp);
        scrollCons = layout.getConstraints(songScrollUp);
        scrollCons.setX(Spring.constant(700));
        scrollCons.setY(Spring.constant(0));
        songScrollUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                songPos -= 10;
                if(songPos < 0) {
                    songPos = 0; //makes sure we don't go too far
                }
                populateSongs();
            }
        });

    }

    public void refreshAlbums() {
        String[] tmp = musicControl.getAlbumList();
        for (int i = 0; i < tmp.length; i++) {
            if (tmp[i].length() > 50) {
                albumNames.add(tmp[i].substring(0, 50) + "...");
            } else {
                albumNames.add(tmp[i]);
            }
        }
        albumPos = 0;
        populateAlbums();
    }

    public void handleAlbumAction(int buttonPos) {
        songNames.clear();
        songPos = 0;
        selectedAlbum = getAlbumIndex(buttonPos);
        String[] tmp = musicControl.setAlbum(getAlbumIndex(buttonPos));
        for (int i = 0; i < tmp.length; i++) {
            if (tmp[i].length() > 50) {
                songNames.add(tmp[i].substring(0, 50) + "...");
            } else {
                songNames.add(tmp[i]);
            }
            populateSongs();
        }
    }

    public void handleSongAction(int buttonPos) {
        if (getSongIndex(buttonPos) >= 0) {
            musicControl.setSongChoice(getSongIndex(buttonPos));
            this.setVisible(false);
        }
    }

    public void populateAlbums() {
        for (int i = 0; i < albumButtons.length; i++) {
            if (albumPos + i < albumNames.size()) { //only populate buttons if we have enough albums to do so
                albumButtons[i].setText(albumNames.get(albumPos + i));
            } else {
                albumButtons[i].setText("");
            }
        }

    }

    public void populateSongs() {
        if (selectedAlbum >= 0) { //don't populate songs if an album isn't selected (it will be set to -1)
            for (int i = 0; i < songButtons.length; i++) {
                if (songPos + i < songNames.size()) { //only populate the buttons if we have enough songs to do so
                    songButtons[i].setText(songNames.get(songPos + i));
                } else {
                    songButtons[i].setText("");
                }
            }
        }
    }

    public int getAlbumIndex(int buttonPos) {
        return albumPos + buttonPos;
    }

    public int getSongIndex(int buttonPos) {
        return songPos + buttonPos;
    }
}