module com.guoyanchen.chinesechess {
    requires javafx.controls;
    requires javafx.fxml;
    requires jlayer;

    opens com.guoyanchen.chinesechess to javafx.fxml;
    exports com.guoyanchen.chinesechess;
    exports com.guoyanchen.chinesechess.page;
    opens com.guoyanchen.chinesechess.page to javafx.fxml;
    exports com.guoyanchen.chinesechess.serve;
    opens com.guoyanchen.chinesechess.serve to javafx.fxml;
    exports com.guoyanchen.chinesechess.piece;
    opens com.guoyanchen.chinesechess.piece to javafx.fxml;
    exports com.guoyanchen.chinesechess.setting;
    opens com.guoyanchen.chinesechess.setting to javafx.fxml;

}