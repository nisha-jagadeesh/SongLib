package view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class SongLibController {
	
	@FXML TextField song;
	@FXML TextField artist;
	@FXML TextField album;
	@FXML TextField year;
	@FXML TextField song_display;
	@FXML TextField artist_display;
	@FXML TextField album_display;
	@FXML TextField year_display;
	@FXML
	ListView<String> listView;
	private ObservableList<String> obsList;
	private List<String> listOfSongs = readFile();

	public void start(Stage mainStage) {
		
		//read data from list
		//System.out.println("list of songs" + listOfSongs);
		
		obsList = FXCollections.observableArrayList();
		int numberOfSongs = listOfSongs.size();
		int currSong = 0;
		while(currSong < numberOfSongs) {
			String songInfo = listOfSongs.get(currSong);
			//System.out.println("songinfo" + songInfo);
			String[] songInfoArr = songInfo.split("\\|");
			//System.out.println(songInfoArr[1]);
			
			
			String nameAndArtist = songInfoArr[0] + " | " + songInfoArr[1];
			//System.out.println(nameAndArtist);
			
			obsList.add(nameAndArtist);
			currSong+=1;
		}
		
		listView.setItems(obsList);
		
		
		// select the first item
		listView.getSelectionModel().select(0);
		// set listener for the items
		listView
		.getSelectionModel()
		.selectedIndexProperty()
		.addListener(
		(obs, oldVal, newVal) ->
		showItem(mainStage));
	}
	
	
	@FXML Button add;
	@FXML Button edit;
	@FXML Button delete;
	public void modifyList(ActionEvent e) {
		Button b = (Button)e.getSource();
		if (listOfSongs.size() == 0) { return; }
		int index = listView.getSelectionModel().getSelectedIndex();
		String songInfo = listOfSongs.get(index);

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
			}
			
		}
		if (b == edit) {
			//Do we want to display "Enter text here: "
			
			//make save/cancel buttons
			//if save -> edit file

		}
	}

		
	//when delete button is clicked should also remove song from the text file 
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
						sb.append(user_song + "|" + user_artist + "|" + user_album + "|" + user_year + "\n");
					}
				} 
			}
			
			//add a song to the library
			if(command.equals("a")) {

			}
			
			//overwrite the file
			String strFile = sb.toString();	
			System.out.println("file contents:\n" + strFile);
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
	
	private void showItem(Stage mainStage) {
		//String item = listView.getSelectionModel().getSelectedItem();
		if (listOfSongs.size() == 0) { return; }
		int index = listView.getSelectionModel().getSelectedIndex();
		String[] sia = listOfSongs.get(index).split("\\|"); //sia = song information array

		song_display.setText(sia[0]);
		artist_display.setText(sia[1]);
		album_display.setText(sia[2]);
		year_display.setText(sia[3]);
	}
		
	private List<String> readFile() {

		try {
			String basePath = new File("").getAbsolutePath();
			File file = new File(basePath + "/src/view/songs.txt");
			
			FileReader fr = new FileReader(file);			
			BufferedReader br=new BufferedReader(fr);
			String line;  
			List<String> listOfSongs = new ArrayList<>();
			
			while((line=br.readLine())!=null) {  
				listOfSongs.add(line);
			}  
			fr.close(); 
			Collections.sort(listOfSongs, String.CASE_INSENSITIVE_ORDER);
			return listOfSongs;
			
		} catch (IOException e) {
			e.printStackTrace();  
			return null;
		}
	}
	
	//REMOVE LATER
	private void showItemInputDialog(Stage mainStage) {
		String item = listView.getSelectionModel().getSelectedItem();
		int index = listView.getSelectionModel().getSelectedIndex();
		
		TextInputDialog dialog = new TextInputDialog(item);
		dialog.initOwner(mainStage); dialog.setTitle("List Item");
		dialog.setHeaderText("Song Name: " + item + "\nArtist: " + "\nAlbum: " + "\nYear: ");
		
		//dialog.setContentText("Enter song name: ");

	}
	
	
}
	
	/**
	 * SONG LIST DISPLAY REQ
	 * https://stackoverflow.com/questions/52129099/javafx-how-to-read-and-write-an-observablelist-to-a-file
	 * 
	 * figure out what data structure to use for list
	 * make sure you can add, edit, delete from it
	 * 
	 * case insensitive
	 * list display NAME and ARTIST
	 * sorted alphabetically by songs
	 * for duplicate songs, sort by artist name
	 * 
	 * TODO later: separate name and artist into two columns
	 */
	
	/**
	 * SONG DETAIL 
	 * 
	 * name, artist, album, year
	 */



