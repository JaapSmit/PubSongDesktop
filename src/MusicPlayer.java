import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
import javafx.util.Duration;

public class MusicPlayer extends Application {
	
	public boolean playing;
	MediaPlayer player;
	Text information;
	Text songDuration;
	Media song;
	static ArrayList<String> huidigeList;
	
	@Override
	public void start(Stage primaryStage) {
		
		huidigeList = getHuidigeList();
		
		// ui textbox
		songDuration = new Text();
		information = new Text();
		songDuration.setText("");
		information.setText("hier komt info");
		Slider volumeSlider = new Slider(0, 1, 1);
		Slider durationSlider = new Slider(0, 1, 1);
		VBox root = new VBox();
		root.getChildren().addAll(information, songDuration, volumeSlider, durationSlider);
		Scene scene = new Scene(root, 300, 300);
		primaryStage.setScene(scene);
		primaryStage.show();
		play(huidigeList.get(0));
		
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
					//durationSlider.setValue(player.getCurrentTime().toSeconds()/song.getDuration().toSeconds()); gaat hakkelen
				}
			}
		}, 0, 1000);
		
		volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov,
					Number old_val, Number new_val) {
						player.setVolume(new_val.doubleValue());
			}
		});
		
		durationSlider.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov,
					Number old_val, Number new_val) {
						double jumpPoint = new_val.doubleValue() * song.getDuration().toSeconds();
						Duration jumpPointTime = new Duration(jumpPoint*1000);
						player.seek(jumpPointTime);
			}
		});
	}
		public void play(String url) {
			//String path = "file:///C:/Users/Student/workspace/PubSongDesktop/src/Muziek/Best10SecIntroSound.mp3";
			String path = "file:///C:/Users/Student/workspace/PubSongDesktop/src/Muziek/" + url + ".mp3";
			song = new Media(path);
			//System.out.println(song.getDuration().toSeconds());
			player = new MediaPlayer(song);
			//player.play();
			
		player.setOnEndOfMedia(new Runnable() {
			@Override
			public void run() {
				playing = false;
				System.out.println("Song ended");
				player.stop();
				// haal hier de volgende uit de database, evt play met een string starten
				play(nextSong());
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
		

		
	}
		
	public static String nextSong() {
		String tmpFirst = huidigeList.get(0);
		huidigeList.remove(0);
		String urlSong = huidigeList.get(0);
		huidigeList.add(tmpFirst);
		return urlSong;
	}

	public static ArrayList<String> getHuidigeList() {
		ArrayList<String> huidigeLijst = new ArrayList<>();
		BufferedReader file = null;
		try {
			file = new BufferedReader(new FileReader("C:/Users/Student/workspace/PubSongDesktop/src/huidigAfspeellijst.txt"));
		    StringBuilder sb = new StringBuilder();
		    String line = file.readLine();

		    while (line != null) {
		    	if(!line.isEmpty()) {
		    		huidigeLijst.add(line);
		    	}
		        //sb.append(System.lineSeparator());
		        line = file.readLine();
		    }
		    //String everything = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return huidigeLijst;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void setInformation() {
		Media currentSong = player.getMedia();
		information.setText(
				"Artiest: " + huidigeList.get(0).substring(0, huidigeList.get(0).indexOf('-')) + "\n" +
				"Titel: " + huidigeList.get(0).substring(huidigeList.get(0).indexOf('-')+1) + "\n"
				);
	}
}
