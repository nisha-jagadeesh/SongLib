/**
 * Nisha Jagadeesh nj250
 * Beatrice Liang-Gilman bel56
 */
package view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.*;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SongLibController {
	
	@FXML TextField song;
	@FXML TextField artist;
	@FXML TextField album;
	@FXML TextField year;
	@FXML Text song_display;
	@FXML Text artist_display;
	@FXML Text album_display;
	@FXML Text year_display;
	@FXML ListView<String> listView;
	
	private ObservableList<String> obsList;
	private List<String> listOfSongs = readFile();

	public void start(Stage mainStage) {
		
		//read data from list
		obsList = getObsList();	
		listView.setItems(obsList);
		
		
		// select the first item
		listView.getSelectionModel().select(0);
		showItem();
		
		// set listener for the items
		listView
		.getSelectionModel()
		.selectedIndexProperty()
		.addListener(
		(obs, oldVal, newVal) ->
		showItem());
	}
	
	//display songs in listview based on songs.txt 
	private ObservableList<String> getObsList() {
		obsList = FXCollections.observableArrayList();
		int numberOfSongs = listOfSongs.size();
		int currSong = 0;
		while(currSong < numberOfSongs) {
			String songInfo = listOfSongs.get(currSong);
			String[] songInfoArr = songInfo.split("\\|");			
			String nameAndArtist = songInfoArr[0] + " | " + songInfoArr[1];			
			obsList.add(nameAndArtist);
			currSong+=1;
		}
		return obsList;
	}
	
	
	@FXML Button add;
	@FXML Button clear;
	@FXML Button edit;
	@FXML Button delete;	
	//add, edit, delete method commands
	public void modifyList(ActionEvent e) {

		Button b = (Button)e.getSource();
		
		//clear add/edit fields in anchorpane
		if (b == clear) {			
			song.setText("");
			artist.setText("");
			album.setText("");
			year.setText("");
			return;
		}
		
		//add a song to the list
		if (b == add) {
			String songInfo = song.getText();
			String artistInfo = artist.getText();
			String nameAndArtist = songInfo + " | " + artistInfo;
			
			if (songInfo.isEmpty() || artistInfo.isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Missing Fields");
				alert.setHeaderText("Name and artist are required fields. Please try again.");
				alert.show();
				return;
				
			}
			
			if (containsIgnoreCase(obsList, nameAndArtist)){
				
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Duplicate song");
				alert.setHeaderText("Error adding duplicate song. Please try again.");
				alert.show();
				return;
			} 
			
			
			String albumInfo = album.getText();
			String yearInfo = year.getText();
			
			if (albumInfo.isEmpty()) {
				albumInfo = "--";
			}
			if (yearInfo.isEmpty()) {
				yearInfo = "--";
			}
			String newSongInfo = songInfo + "|" + artistInfo + "|" + albumInfo + "|" + yearInfo + "\n";
			
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Add song");
			alert.setHeaderText("Are you sure you want to add this song?");
			Optional<ButtonType> opt = alert.showAndWait();
			
			if (opt.get() == ButtonType.OK) {
				editTextFile(newSongInfo,"a");
				listOfSongs = readFile();
				
				
				obsList = getObsList();
				listView.setItems(obsList);
				
				listView.getSelectionModel().select(obsList.indexOf(nameAndArtist));
				
				
			} 
			return;
		}
		
		if (listOfSongs.size() == 0) { return; }
		int index = listView.getSelectionModel().getSelectedIndex();
		String songInfo = listOfSongs.get(index);

		//delete a song from the list
		if (b == delete) {	
			//confirm delete
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Delete song");
			alert.setHeaderText("Are you sure you want to delete this song?");
			Optional<ButtonType> opt = alert.showAndWait();
			
			if (opt.get() == ButtonType.OK) {
				editTextFile(songInfo,"d");
				String[] songInfoArr = songInfo.split("\\|");			
				String nameAndArtist = songInfoArr[0] + " | " + songInfoArr[1];
				listOfSongs = readFile();
				obsList.remove(nameAndArtist);
				listView.setItems(obsList);

				song.setText("");
				artist.setText("");
				album.setText("");
				year.setText("");
				song_display.setText("");
				artist_display.setText("");
				album_display.setText("");
				year_display.setText("");
				
				listView.getSelectionModel().select(index);
				showItem();
				
			}
			
		}
		
		//edit a song in the list
		if (b == edit) {
			String songName = song.getText();
			String artistName = artist.getText();
			String nameAndArtist = songName + " | " + artistName;
			
			if (songName.isEmpty() || artistName.isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Missing Fields");
				alert.setHeaderText("Name and artist are required fields. Please try again.");
				alert.show();
				return;
			}
			
			if (containsIgnoreCase(obsList, nameAndArtist)){
				if (obsList.indexOf(nameAndArtist) != index) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Duplicate song");
					alert.setHeaderText("This song is already in the library. Please try again.");
					alert.show();
					return;
				}
			} 
			
			
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Edit song");
			alert.setHeaderText("Are you sure you want to edit this song?");
			Optional<ButtonType> opt = alert.showAndWait();
			
			if (opt.get() == ButtonType.OK) {
				editTextFile(songInfo,"e");
				listOfSongs = readFile();
				obsList = getObsList();
				listView.setItems(obsList);
				listView.getSelectionModel().select(index);
			}
		}
	}

		
	//edit songs.txt with new content
	private String editTextFile(String songInfo, String command) {
		
		try {
			String basePath = new File("").getAbsolutePath();
			File file;
			file = new File(basePath + "/src/view/songs.txt");
			
			FileReader fr = new FileReader(file);			
			BufferedReader br=new BufferedReader(fr);
			
			StringBuilder sb = new StringBuilder();
			String line;  
			
			//delete a song from the library
			if(command.equals("d")) {
				while((line=br.readLine())!=null) {  
					if(!line.equals(songInfo)) {
						sb.append(line).append("\n");
					}
				}  
			}
			
			//edit a song in the library
			if(command.equals("e")) {
				while((line=br.readLine())!=null) {  
					if(!line.equals(songInfo)) {
						sb.append(line).append("\n");
					}else {
						String user_song = song.getText();
						String user_artist = artist.getText();
						String user_album = album.getText();
						String user_year = year.getText();
						
						if (user_album.isEmpty()) {
							user_album = "--";
						}
						if (user_year.isEmpty()) {
							user_year = "--";
						}
						sb.append(user_song + "|" + user_artist + "|" + user_album + "|" + user_year + "\n");
					}
				} 
			}
			
			//add a song to the library
			if(command.equals("a")) {
				while((line=br.readLine())!=null) {  
					sb.append(line).append("\n");
				}
				sb.append(songInfo);
			}
			
			//overwrite the file
			String strFile = sb.toString();	
			FileWriter fw = new FileWriter(file);
			fw.write(strFile);
			fw.flush();
			fw.close();
			
			fr.close(); 
			return strFile;
			
			
		} catch (IOException e) {
			e.printStackTrace(); 
			return null;
		}
		
	}
	
	//displays item in anchorpane
	private void showItem() {
		if (listOfSongs.size() == 0) { return; }
		int index = listView.getSelectionModel().getSelectedIndex();
		if(index == -1) {
			return;
		}
		String[] sia = listOfSongs.get(index).split("\\|"); //sia = song information array

		song_display.setText(sia[0]);
		artist_display.setText(sia[1]);
		album_display.setText(sia[2]);
		year_display.setText(sia[3]);
		song.setText(sia[0]);
		artist.setText(sia[1]);
		album.setText(sia[2]);
		year.setText(sia[3]);
	}
		
	//reads from text file and returns sorted list of songs 
	private List<String> readFile() {

		try {
			String basePath = new File("").getAbsolutePath();
			File file = new File(basePath + "/src/view/songs.txt");
			
			FileReader fr = new FileReader(file);			
			BufferedReader br = new BufferedReader(fr);
			String line;  
			List<String> listOfSongs = new ArrayList<>();
			
			while((line=br.readLine())!=null) {  
				listOfSongs.add(line);
			}  
			fr.close(); 
			Collections.sort(listOfSongs, String.CASE_INSENSITIVE_ORDER);
			return listOfSongs;
			
		} catch (IOException e) {
			return null;
		}
	}
	
	//contains method for lists ignoring case
	private boolean containsIgnoreCase(ObservableList<String> list, String search) {
	    for (String song : list) {
	        if (song.equalsIgnoreCase(search)) {
	            return true;
	        }
	    }
	    return false;
	}
	
	
}




