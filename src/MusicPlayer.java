import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class MusicPlayer extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		String path = "file:///C:/Users/Student/workspace/PubSongDesktop/src/Muziek/TomOdell-Magnetised.mp3";
		Media song = new Media(path);
		System.out.println(song.getDuration().toSeconds());
		MediaPlayer player = new MediaPlayer(song);
		player.play();
		primaryStage.show();
		System.out.println(song.getDuration().toSeconds());
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
