package com.guoyanchen.chinesechess.page;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Indicator {
    private final Timeline redBlinkTimeline;
    private final Timeline blackBlinkTimeline;
    private final MainViewController controller;

    public Indicator(MainViewController controller) {
        this.controller = controller;
        redBlinkTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.5), e -> controller.getRedArrow().setVisible(true)),
                new KeyFrame(Duration.seconds(1), e -> controller.getRedArrow().setVisible(false))
        );
        redBlinkTimeline.setCycleCount(Timeline.INDEFINITE);

        blackBlinkTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.4), e -> controller.getBlackArrow().setVisible(true)),
                new KeyFrame(Duration.seconds(0.9), e -> controller.getBlackArrow().setVisible(false))
        );
        blackBlinkTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void blink(boolean isRed) {
        if (isRed) {
            controller.getRedArrow().setVisible(true);
            redBlink();
        } else {
            controller.getBlackArrow().setVisible(true);
            blackBlink();
        }
    }

    public void stop() {
        redBlinkTimeline.stop();
        blackBlinkTimeline.stop();
    }

    private void redBlink() {
        blackBlinkTimeline.stop();
        controller.getBlackLbl().setVisible(false);
        controller.getBlackArrow().setVisible(false);
        controller.getRedLbl().setVisible(true);
        redBlinkTimeline.play();
    }

    private void blackBlink() {
        redBlinkTimeline.stop();
        controller.getRedArrow().setVisible(false);
        controller.getRedLbl().setVisible(false);
        controller.getBlackLbl().setVisible(true);
        blackBlinkTimeline.play();
    }

}
