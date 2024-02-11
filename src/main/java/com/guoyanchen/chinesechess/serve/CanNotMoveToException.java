package com.guoyanchen.chinesechess.serve;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class CanNotMoveToException extends Exception {
    private final String message;

    public CanNotMoveToException(String msg) {
        message = msg;
    }

    public void showError(Pane pane) {
        System.out.println(message);
        Label label = new Label(message);
        label.setFont(Font.font(20));
        label.setTextFill(Color.BLUE);
        label.setTranslateX(pane.getWidth() / 2 - 40);
        label.setTranslateY(pane.getHeight() / 2 - 15);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), event -> pane.getChildren().add(label)),
                new KeyFrame(Duration.seconds(0.4), event -> pane.getChildren().remove(label))
        );
        timeline.play();
    }
}
