package com.example.spane;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;

public class MediaHandler {

    public static void embedImage(Pane pane, String path) {
        Image image = new Image("file:" + path);               // Load image from file
        ImageView imageView = new ImageView(image);            // Create ImageView to show the image
        imageView.setPreserveRatio(true);                      // Keep aspect ratio
        imageView.setFitWidth(500);                            // Set width
        pane.getChildren().add(imageView);                     // Add to the pane
    }

    public static void embedVideo(Pane pane, String path) {
        Media media = new Media(new File(path).toURI().toString());  // Load video as media
        MediaPlayer player = new MediaPlayer(media);                 // Create media player
        MediaView view = new MediaView(player);                      // Create view to show video
        view.setFitWidth(300);                                       // Set width
        view.setPreserveRatio(true);                                 // Maintain aspect ratio

        // Create Play and Pause buttons
        Button playButton = new Button("Play");
        Button pauseButton = new Button("Pause");

        // Button actions to control playback
        playButton.setOnAction(e -> player.play());
        pauseButton.setOnAction(e -> player.pause());

        // Combine video view and buttons into a VBox and add to pane
        VBox mediaControls = new VBox(5, view, playButton, pauseButton);
        pane.getChildren().add(mediaControls);
    }

    public static void embedAudio() {
        // TODO: Implement audio embedding with play/pause controls
    }
}
