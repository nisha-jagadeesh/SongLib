package view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class SongLibController {
	
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
	
	private void showItem(Stage mainStage) {
		//String item = listView.getSelectionModel().getSelectedItem();
		int index = listView.getSelectionModel().getSelectedIndex();
		String[] sia = listOfSongs.get(index).split("\\|"); //sia = song information array

		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(mainStage);
		alert.setTitle("Selected Song");
		alert.setHeaderText("Song Information");
		String content = "Name: " + sia[0] + "\nArtist: " + sia[1] + "\nAlbum: " + sia[2] + "\nYear: " + sia[3];
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	
	private void showItemInputDialog(Stage mainStage) {
		String item = listView.getSelectionModel().getSelectedItem();
		int index = listView.getSelectionModel().getSelectedIndex();
		
		TextInputDialog dialog = new TextInputDialog(item);
		dialog.initOwner(mainStage); dialog.setTitle("List Item");
		dialog.setHeaderText("Song Name: " + item + "\nArtist: " + "\nAlbum: " + "\nYear: ");
		
		//dialog.setContentText("Enter song name: ");


		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) { obsList.set(index, result.get()); }
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

