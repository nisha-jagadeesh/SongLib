package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.SongLibController;

public class SongLib extends Application {

	@Override
	public void start(Stage primaryStage)
		throws Exception {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(
					getClass().getResource("/view/songlib.fxml"));
			
			AnchorPane root = (AnchorPane)loader.load();
			SongLibController songLibController =
			loader.getController();
			songLibController.start(primaryStage);
			Scene scene = new Scene(root, 500, 400);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Song Library");
			primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
