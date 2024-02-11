package com.guoyanchen.chinesechess;

import com.guoyanchen.chinesechess.setting.Setting;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ChineseChessApplication extends Application {

    private static BGMPlayer bgmPlayer;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            primaryStage.setResizable(false);
            primaryStage.setTitle("中国象棋");
            primaryStage.getIcons().add(new Image(String.valueOf(getClass().getResource("appIcon.png"))));
            primaryStage.setScene(scene);
            primaryStage.show();

            Setting.init(primaryStage);
            bgmPlayer = new BGMPlayer();
            if (Setting.isMusicPlayed.get()) bgmPlayer.playBGM();
            Setting.isMusicPlayed.addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    bgmPlayer.playBGM();
                } else bgmPlayer.stopBGM();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        bgmPlayer.stopBGM();
    }
}
