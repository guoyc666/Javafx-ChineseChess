package com.guoyanchen.chinesechess.page;

import com.guoyanchen.chinesechess.setting.Setting;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.util.Objects;

public class CheckAndEat {
    private final Timeline checkTimeline;//将军
    private final Timeline eatTimeline;//吃
    private final Timeline killingTimeline;//绝杀
    private final Timeline beginTimeline;//开局

    private final Timeline withdrawTimeline;
    private final Runnable beginPlayer;
    private final Runnable checkPlayer;
    private final Runnable eatPlayer;
    private final Runnable killingPlayer;

    private final Runnable puttingPlayer;

    public CheckAndEat(Pane root) {
        checkTimeline = createTimeline("hintImg/check.png", root);
        eatTimeline = createTimeline("hintImg/eat.png", root);
        killingTimeline = createTimeline("hintImg/victory.png", root);
        beginTimeline = createTimeline("hintImg/begin.png", root);

        beginPlayer = createPlayer("media/begin.MP3");
        checkPlayer = createPlayer("media/check.MP3");
        eatPlayer = createPlayer("media/eat.MP3");
        killingPlayer = createPlayer("media/victory.MP3");
        puttingPlayer = createPlayer("media/put.MP3");

        withdrawTimeline = createTimeline("hintImg/withdraw.png", root);

    }

    private static Timeline createTimeline(String imgName, Pane root) {
        Image image = new Image(String.valueOf(CheckAndEat.class.getResource(imgName)));
        ImageView imageView = new ImageView(image);
        imageView.setX(root.getPrefWidth() / 2 - image.getWidth() / 2);
        imageView.setY(root.getPrefHeight() / 2 - image.getHeight() / 2);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), event -> root.getChildren().add(imageView)),
                new KeyFrame(Duration.seconds(0.4), event -> root.getChildren().remove(imageView))
        );
        timeline.setCycleCount(1); // 只执行一次
        return timeline;
    }

    public void showCheck() {
        checkTimeline.play();
        if (Setting.isMusicPlayed.get()) new Thread(checkPlayer).start();
    }

    public void showEat() {
        eatTimeline.play();
        if (Setting.isMusicPlayed.get()) new Thread(eatPlayer).start();
    }

    public void showKilling() {
        killingTimeline.play();
        if (Setting.isMusicPlayed.get()) new Thread(killingPlayer).start();
    }

    public void showBegin() {
        beginTimeline.play();
        if (Setting.isMusicPlayed.get()) new Thread(beginPlayer).start();
    }

    public void puttingPlay() {
        new Thread(puttingPlayer).start();
    }

    public void showWithdraw() {
        withdrawTimeline.play();
    }

    private Runnable createPlayer(String bgmName) {
        return () -> {
            try {
                AdvancedPlayer player = new AdvancedPlayer(
                        Objects.requireNonNull(getClass().getResourceAsStream(bgmName)),
                        FactoryRegistry.systemRegistry().createAudioDevice()
                );
                player.play();
            } catch (JavaLayerException e) {
                e.printStackTrace();
            }
        };
    }
}
