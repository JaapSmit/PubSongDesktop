import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MusicPlayer extends Application {
	
	public boolean playing;
	MediaPlayer player;
	Text information;
	
	@Override
	public void start(Stage primaryStage) {
		String path = "file:///C:/Users/Student/workspace/PubSongDesktop/src/Muziek/TomOdell-Magnetised.mp3";
		Media song = new Media(path);
		//System.out.println(song.getDuration().toSeconds());
		player = new MediaPlayer(song);
		//player.play();
		
		// ui textbox
		Text songDuration = new Text();
		information = new Text();
		songDuration.setText("");
		information.setText("hier komt info");
		Slider volumeSlider = new Slider(0, 1, 1);
		VBox root = new VBox();
		root.getChildren().addAll(information, songDuration, volumeSlider);
		Scene scene = new Scene(root, 300, 300);
		primaryStage.setScene(scene);
		primaryStage.show();
		System.out.println(song.getDuration().toSeconds());
		
		player.setOnEndOfMedia(new Runnable() {
			@Override
			public void run() {
				playing = false;
				System.out.println("Song ended");
				// Gooi het nummer van de database lijst af
				// Start het volgende nummer op de lijst
				
			}
		});
		
		player.setOnReady(new Runnable() {
			@Override
			public void run() {
				player.play();
				playing = true;
				setInformation();		
			}
		});
		
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				float total = (float)song.getDuration().toSeconds();
				float current = (float)player.getCurrentTime().toSeconds();
				if(current > 0) {
					float percentage = (current/total)*100;
					String strTotal = String.format("%.0f", total);
					String strCurrent = String.format("%.0f", current);
					String strPercentage = String.format("%.2f", percentage);
					songDuration.setText("PLAYING: " + strCurrent + " : " + strTotal + " seconden   " + strPercentage + "%");
				}
			}
		}, 0, 1000);
		
		volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov,
					Number old_val, Number new_val) {
						player.setVolume(new_val.doubleValue());
			}
		});
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void setInformation() {
		Media currentSong = player.getMedia();
		information.setText("Hier kan nog data komen van de database");
	}
}
