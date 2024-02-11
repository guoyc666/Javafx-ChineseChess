package com.guoyanchen.chinesechess.page;

import com.guoyanchen.chinesechess.setting.Setting;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Timer {
    private final Timeline limited;
    private final Timeline unlimited;
    private final Text timeRemaining;
    private final Alert alert = new Alert(Alert.AlertType.INFORMATION);
    private int usedTime = 0;
    private boolean isTimeOver = false;
    private Timeline timeline;

    public Timer(Text timeRemaining) {
        this.timeRemaining = timeRemaining;
        limited = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer2()));
        limited.setCycleCount(Timeline.INDEFINITE);

        unlimited = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer1()));
        unlimited.setCycleCount(Timeline.INDEFINITE);

        timeline = unlimited;

        alert.setContentText("时间到！");
    }

    public void refresh() {
        usedTime = 0;
        isTimeOver = false;
    }

    public void start() {
        timeline.play();
    }

    public void end() {
        timeline.stop();
    }

    public void initTimer(boolean isTimeLimited) {
        timeline = isTimeLimited ? limited : unlimited;
    }

    private void updateTimer1() {
        //不限时
        usedTime++;
        int minutes = usedTime / 60;
        int seconds = usedTime % 60;
        timeRemaining.setText(String.format("%02d : %02d", minutes, seconds));
    }

    private void updateTimer2() {
        //限时
        if (isTimeOver) return;
        usedTime++;
        int remainTime = Setting.limitedTime * 60 - usedTime;
        if (remainTime == 0) {
            isTimeOver = true;
            timeRemaining.setText("超时！");
            alert.show();
        }
        int minutes = remainTime / 60;
        int seconds = remainTime % 60;
        timeRemaining.setText(String.format("%02d : %02d", minutes, seconds));
    }
}
