import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MusicPlayer extends Application {
	
	public boolean playing;
	MediaPlayer player;
	
	@Override
	public void start(Stage primaryStage) {
		String path = "file:///C:/Users/Student/workspace/PubSongDesktop/src/Muziek/TomOdell-Magnetised.mp3";
		Media song = new Media(path);
		//System.out.println(song.getDuration().toSeconds());
		player = new MediaPlayer(song);
		//player.play();
		
		// ui textbox
		Text songDuration = new Text();
		songDuration.setText("Lied lengte");
		StackPane root = new StackPane();
		root.getChildren().add(songDuration);
		Scene scene = new Scene(root, 300, 300);
		primaryStage.setScene(scene);
		primaryStage.show();
		System.out.println(song.getDuration().toSeconds());
		
		player.setOnEndOfMedia(new Runnable() {
			@Override
			public void run() {
				playing = false;
				System.out.println("Song ended");
			}
		});
		
		player.setOnReady(new Runnable() {
			@Override
			public void run() {
				player.play();
				playing = true;
				System.out.println(song.getDuration().toSeconds());
			}
		});
		
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				float total = (float)song.getDuration().toSeconds();
				float current = (float)player.getCurrentTime().toSeconds();
				if(current > 0) {
					float percentage = (current/total)*100;
					songDuration.setText("PLAYING: " + current + " : " + total + "   " + percentage + "%");
				}
				
			}
		}, 0, 1000);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
