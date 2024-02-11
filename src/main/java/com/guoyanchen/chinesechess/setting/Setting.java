package com.guoyanchen.chinesechess.setting;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Setting {
    public static final BooleanProperty isRegretAllowed = new SimpleBooleanProperty(true);
    public static final BooleanProperty isTimeLimited = new SimpleBooleanProperty(false);
    public static final BooleanProperty isMusicPlayed = new SimpleBooleanProperty(true);
    private static final Stage settingStage = new Stage();
    public static int limitedTime = 5;

    public static void show() {
        settingStage.show();
    }

    public static void init(Stage primaryStage) {
        CheckBox allowMusic = new CheckBox("开启音效");
        CheckBox allowRetract = new CheckBox("允许悔棋");
        CheckBox allowLimitedTime = new CheckBox("开启限时(默认5min)");

        Bindings.bindBidirectional(allowMusic.selectedProperty(), isMusicPlayed);
        Bindings.bindBidirectional(allowRetract.selectedProperty(), isRegretAllowed);
        Bindings.bindBidirectional(allowLimitedTime.selectedProperty(), isTimeLimited);

        TextField input = new TextField();
        input.promptTextProperty().set("当前：" + limitedTime);
        input.setPrefWidth(70);

        Label label = new Label("分钟");
        label.setPrefSize(50, 25);

        Button button = new Button("确认");
        button.setOnAction(e -> getLimitedTime(input));

        HBox hBox = new HBox(10, input, label, button);
        hBox.setPadding(new Insets(0, 0, 0, 15));
        hBox.disableProperty().bind(allowLimitedTime.selectedProperty().not());

        VBox vBox = new VBox(20, allowMusic, allowRetract, allowLimitedTime, hBox);
        vBox.setPadding(new Insets(30, 30, 20, 35));

        Scene scene = new Scene(vBox, 300, 300);
        settingStage.setTitle("设置");
        settingStage.setScene(scene);
        settingStage.initOwner(primaryStage);
        settingStage.setMinHeight(300);
        settingStage.setMinWidth(300);
    }

    private static void getLimitedTime(TextField input) {
        try {
            String intString = input.getText();
            int temp = Integer.parseInt(intString.trim());
            if (temp > 0 && temp <= 60) limitedTime = temp;
            else throw new NumberFormatException();
            input.promptTextProperty().set("当前：" + limitedTime);
            System.out.println("开启限时：" + Setting.limitedTime + "min");
            input.clear();

        } catch (NumberFormatException ex) {
            input.clear();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("请输入1~60的整数！");
            alert.showAndWait();
        }

    }

}
