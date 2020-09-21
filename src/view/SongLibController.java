package view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class SongLibController {
	
	@FXML
	ListView<String> listView;
	private ObservableList<String> obsList;
	public void start(Stage mainStage) {
		
		//read data from list
		readSongLibFile();
		
		// create an ObservableList
		// from an ArrayList
		obsList = FXCollections.observableArrayList(
		"Painkiller",
		"Alejandro",
		"Tombe");
		listView.setItems(obsList);
		
		// select the first item
		listView.getSelectionModel().select(0);
		// set listener for the items
		listView
		.getSelectionModel()
		.selectedIndexProperty()
		.addListener(
		(obs, oldVal, newVal) ->
		showItemInputDialog(mainStage));
	}
	
	private void showItemInputDialog(Stage mainStage) {
		String item = listView.getSelectionModel().getSelectedItem();
		int index = listView.getSelectionModel().getSelectedIndex();
		
		TextInputDialog dialog = new TextInputDialog(item);
		dialog.initOwner(mainStage); dialog.setTitle("List Item");
		dialog.setHeaderText("Selected Item (Index: " + index + ")");
		dialog.setContentText("Enter name: ");
		
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) { obsList.set(index, result.get()); }
	}
	
	private void readSongLibFile() {

		try {
			String basePath = new File("").getAbsolutePath();
			File file = new File(basePath + "/src/view/songs.txt");
			FileReader fr = new FileReader(file);
			
			//reads the file  
			BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream  
			StringBuffer sb=new StringBuffer();    //constructs a string buffer with no characters  
			String line;  
			
			while((line=br.readLine())!=null) {  
				sb.append(line);      //appends line to string buffer  
				sb.append("\n");     //line feed   
			}  
			
			fr.close();    //closes the stream and release the resources  
			System.out.println("Contents of File: ");  
			System.out.println(sb.toString());   //returns a string that textually represents the object  
		} catch (IOException e) {
			e.printStackTrace();  
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

