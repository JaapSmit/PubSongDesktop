import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MusicPlayer extends Application {
	
	public boolean playing;
	MediaPlayer player;
	Text information;
	Text songDuration;
	Media song;
	static ArrayList<String> huidigeList;
	
	@Override
	public void start(Stage primaryStage) throws UnirestException {
		
		//aanroepen rest
		aanroepenRest();
		
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
		play(firstSong());
		
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
			String path = "file:///C:/Users/Student/workspace/PubSongDesktop/src/Muziek/" + url + ".mp3";
			//Check if path exists
			File f = new File("C:/Users/Student/workspace/PubSongDesktop/src/Muziek/" + url + ".mp3");
			if(!f.exists()) {
				// nope, ready to rickroll
				path = "file:///C:/Users/Student/workspace/PubSongDesktop/src/Muziek/Rick_Astley-Never_Gonna_Give_You_Up.mp3";
			}
			song = new Media(path);
			player = new MediaPlayer(song);
			
		player.setOnEndOfMedia(new Runnable() {
			@Override
			public void run() {
				playing = false;
				player.stop();
				// haal hier de volgende uit de database, evt play met een string starten
				try {
					play(nextSong());
				} catch (UnirestException e) {
					e.printStackTrace();
				}
			}
		});
		
		player.setOnReady(new Runnable() {
			@Override
			public void run() {
				player.play();
				playing = true;
				try {
					setInformation();
				} catch (UnirestException e) {
					e.printStackTrace();
				}		
			}
		});
		

		
	}
	
	// de eerste moet opgehaald worden zonder de laatste te verwijderen
	public static String firstSong() throws UnirestException {
		HttpResponse<AfspeellijstData> afspeellijstDataResponse = Unirest.get("http://localhost:8080/getNumberOneSong").asObject(AfspeellijstData.class);
		String artiest = afspeellijstDataResponse.getBody().getNummer().getArtiest().replace('/', '-').replace(' ', '_');
		String titel = afspeellijstDataResponse.getBody().getNummer().getTitel().replace('/', '-').replace(' ', '_');
		String urlSong = artiest + "-" + titel;
		return urlSong;
	}
	
	// verwijder de laatst afgespeelde en voeg de volgende in de lijst toe
	public static String nextSong() throws UnirestException {
		HttpResponse<AfspeellijstData> afspeellijstDataResponse = Unirest.get("http://localhost:8080/getNextSong").asObject(AfspeellijstData.class);
		String artiest = afspeellijstDataResponse.getBody().getNummer().getArtiest().replace('/', '-').replace(' ', '_');
		String titel = afspeellijstDataResponse.getBody().getNummer().getTitel().replace('/', '-').replace(' ', '_');
		String urlSong = artiest + "-" + titel;
		return urlSong;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void setInformation() throws UnirestException {
		HttpResponse<AfspeellijstData> afspeellijstDataResponse = Unirest.get("http://localhost:8080/getNumberOneSong").asObject(AfspeellijstData.class);
		String artiest = afspeellijstDataResponse.getBody().getNummer().getArtiest().replace('/', '-').replace(' ', '_');
		String titel = afspeellijstDataResponse.getBody().getNummer().getTitel().replace('/', '-').replace(' ', '_');
		information.setText(
				"Artiest: " + artiest + "\n" +
				"Titel: " + titel + "\n"
				);
	}
	
	public void aanroepenRest() throws UnirestException {
		// Only one time
		Unirest.setObjectMapper(new ObjectMapper() {
		    private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
		                = new com.fasterxml.jackson.databind.ObjectMapper();

		    public <T> T readValue(String value, Class<T> valueType) {
		        try {
		            return jacksonObjectMapper.readValue(value, valueType);
		        } catch (IOException e) {
		            throw new RuntimeException(e);
		        }
		    }

		    public String writeValue(Object value) {
		        try {
		            return jacksonObjectMapper.writeValueAsString(value);
		        } catch (JsonProcessingException e) {
		            throw new RuntimeException(e);
		        }
		    }
		});
		
	}
}
