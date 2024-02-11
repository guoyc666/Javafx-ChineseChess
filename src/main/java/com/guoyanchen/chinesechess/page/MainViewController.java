package com.guoyanchen.chinesechess.page;

import com.guoyanchen.chinesechess.piece.Piece;
import com.guoyanchen.chinesechess.serve.CanNotMoveToException;
import com.guoyanchen.chinesechess.serve.GameServe;
import com.guoyanchen.chinesechess.setting.BoardHelper;
import com.guoyanchen.chinesechess.setting.Setting;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    private GameServe gameServe;
    private Timer timer;
    private Indicator indicator;

    //region FXML控件

    @FXML
    private AnchorPane root;
    @FXML
    private AnchorPane boardArea;
    @FXML
    private AnchorPane flag;

    @FXML
    private TextArea turnsRecorder;
    @FXML
    private Polygon blackArrow;
    @FXML
    private Label blackLbl;
    @FXML
    private TextArea blackStepRecorder;
    @FXML
    private Polygon redArrow;
    @FXML
    private Label redLbl;
    @FXML
    private TextArea redStepRecorder;

    @FXML
    private SplitMenuButton startBtn;
    @FXML
    private MenuItem customizeBtn;
    @FXML
    private MenuItem openFileBtn;
    @FXML
    private MenuItem saveBtn;
    @FXML
    private ImageView settingBtn;

    @FXML
    private ButtonBar functionMenu;
    @FXML
    private ImageView drawBtn;
    @FXML
    private ImageView pauseBtn;
    @FXML
    private ImageView resignBtn;
    @FXML
    private ImageView retractBtn;

    @FXML
    private ImageView reminder;
    @FXML
    private Text timeRemaining;
    @FXML
    private Label timeLbl;

    //endregion

    //region Getter方法

    public void setGameServe(GameServe gameServe) {
        this.gameServe = gameServe;
    }

    public AnchorPane getBoardArea() {
        return boardArea;
    }

    public AnchorPane getRoot() {
        return root;
    }

    public Polygon getBlackArrow() {
        return blackArrow;
    }

    public Label getBlackLbl() {
        return blackLbl;
    }

    public Polygon getRedArrow() {
        return redArrow;
    }

    public Label getRedLbl() {
        return redLbl;
    }

    public MenuItem getSaveBtn() {
        return saveBtn;
    }

    public SplitMenuButton getStartBtn() {
        return startBtn;
    }

    public Text getTimeRemaining() {
        return timeRemaining;
    }

    public AnchorPane getFlag() {
        return flag;
    }

//endregion


    @FXML
    protected void setStartBtn() {
        startView();
        System.out.println("新的对局");
        gameServe.initBoard();

        boardArea.getChildren().addAll(gameServe.rPieces);
        boardArea.getChildren().addAll(gameServe.bPieces);
    }

    private void startView() {
        boardArea.getChildren().clear();
        redStepRecorder.clear();
        blackStepRecorder.clear();
        turnsRecorder.clear();
        flag.setVisible(true);

        boardArea.setDisable(false);
        functionMenu.setDisable(false);
        timer.refresh();
        timer.start();
    }

    public void endView() {
        boardArea.setDisable(true);
        functionMenu.setDisable(true);
        indicator.stop();
        timer.end();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameServe = new GameServe(this);

        //交换棋手监听器
        gameServe.isRedTurn.addListener((observable, oldValue, newValue) -> changePlayer(newValue));

        BackgroundImage backgroundImage = new BackgroundImage(
                new Image(String.valueOf(getClass().getResource("chessBoard.gif"))),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT
        );
        boardArea.setBackground(new Background(backgroundImage));

        indicator = new Indicator(this);
        timer = new Timer(timeRemaining);

        //棋谱记录绑定
        turnsRecorder.scrollTopProperty().addListener((observableValue, number, t1) -> {
            redStepRecorder.setScrollTop((Double) t1);
            blackStepRecorder.setScrollTop((Double) t1);
        });

        //允许悔棋监听器
        Setting.isRegretAllowed.addListener((observable, oldValue, newValue)
                -> retractBtn.setDisable(!newValue));

        //思考限时监听器
        Setting.isTimeLimited.addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                System.out.println("不限时");
            }
            timer.end();
            timeLbl.setText(newValue ? "剩余时间" : "所用时间");
            timer.initTimer(newValue);
            if (!boardArea.isDisable())
                timer.start();
        });

        customizeBtn.setOnAction(e -> System.out.println("自定义对局未完成"));

    }

    private void changePlayer(boolean isRedTurn) {
        //交换棋手
        if (isRedTurn) {
            //回合数
            gameServe.turnsCount++;
            turnsRecorder.appendText(String.format("%03d\n", gameServe.turnsCount));
            System.out.println("当前红方行棋");
        } else {
            System.out.println("当前黑方行棋");
        }
        indicator.blink(isRedTurn);
        timer.refresh();
    }

    @FXML//棋盘点击事件
    protected void setBoard(MouseEvent e) {
        if (gameServe.curSelected != null) {
            int x = BoardHelper.pxToX(e.getX());
            int y = BoardHelper.pxToY(e.getY());
            if (x != -1 && y != -1) {
                System.out.printf("点击棋盘(%d,%d)\n", x, y);
                try {
                    gameServe.curMoveTo(x, y);
                } catch (CanNotMoveToException exception) {
                    exception.showError(boardArea);
                }
            }
        }
//        e.consume();
    }

    //棋谱记录
    public void setRecordTextArea(String content) {
        System.out.println(content);
        if (gameServe.curSelected.isRed) {
            redStepRecorder.appendText(content + "\n");
        } else {
            blackStepRecorder.appendText(content + "\n");
        }
    }

    @FXML//设置
    protected void setSettingBtn() {
        Setting.show();
    }

    @FXML//暂停
    protected void setPauseBtn() {
        indicator.stop();
        timer.end();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("游戏暂停");
        alert.setHeaderText("游戏暂停");
        alert.setContentText("点击确认回到游戏");
        alert.showAndWait();

        indicator.blink(gameServe.isRedTurn.get());
        timer.start();
    }

    @FXML//认输
    protected void setResignBtn() {
        String loser = gameServe.isRedTurn.get() ? "红方" : "黑方";
        System.out.println(loser + "认输");
        String result = gameServe.isRedTurn.get() ? "黑方获胜" : "红方获胜";
        gameServe.gameOver(result);
    }

    @FXML//求和
    protected void setDrawBtn() {
        //理应是一方求和一方同意与否，但没有联机功能，没有意义，姑且算是个接口吧
        String result = gameServe.isRedTurn.get() ? "红方求和" : "黑方求和";
        gameServe.gameOver(result);
    }

    @FXML//悔棋
    protected void setRetractBtn() {
        String player = gameServe.isRedTurn.get() ? "黑方" : "红方";
        if (!gameServe.withdraw()) {
            System.out.println(player + "仍未走棋");
            return;
        }

        if (gameServe.isRedTurn.get()) {//红方悔棋
            int len = redStepRecorder.getText().length();
            redStepRecorder.deleteText(len - 5, len);
            System.out.println("红方悔棋");
        } else {//黑方悔棋
            int len = blackStepRecorder.getText().length();
            blackStepRecorder.deleteText(len - 5, len);
            System.out.println("黑方悔棋");
        }
        int len = turnsRecorder.getText().length();
        turnsRecorder.deleteText(len - 4, len);
    }

    //TODO
    //region 放弃，保留项目
    @FXML
    protected void setOpenFileBtn() {
        System.out.println("打开文件还没有完成");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("c:"));
        fileChooser.setTitle("打开文件");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Chess Files", "*.chess"),
                new FileChooser.ExtensionFilter("DataBase Files", "*.db"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            System.out.println(selectedFile);
        } else System.out.println("取消选择");
    }

    @FXML
    protected void setSaveBtn() {
        System.out.println("保存对局还没有完成");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("保存对局");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("ChessFiles", "*.chess")
        );
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            String folderPath = file.getAbsolutePath();
            System.out.println(folderPath);
        }
    }

    @FXML
    @Deprecated
    protected void setCustomizeBtn() {
        System.out.println("自定义残局还没有完成");

        FlowPane flowPane = new FlowPane();
        flowPane.setPrefWrapLength(500);
        gameServe.pieces = new Piece[9][10];
//        Game.shuai = new Jiang(true);
//        Game.jiang = new Jiang(false);

        //region
//        flowPane.getChildren().addAll(
//                new Shi(true), new Shi(true),
//                new Xiang(true), new Xiang(true),
//                new Ma(true), new Ma(true),
//                new Ju(true), new Ju(true),
//                new Pao(true), new Pao(true),
//                new Zu(true), new Zu(true), new Zu(true),
//                new Zu(true), new Zu(true),
//
//                new Shi(false), new Shi(false),
//                new Xiang(false), new Xiang(false),
//                new Ma(false), new Ma(false),
//                new Ju(false), new Ju(false),
//                new Pao(false), new Pao(false),
//                new Zu(false), new Zu(false), new Zu(false),
//                new Zu(false), new Zu(false)
//        );
        //endregion

        flowPane.getChildren().add(gameServe.shuai);
        flowPane.getChildren().add(gameServe.jiang);

        flowPane.getChildren().forEach(p -> {
            if (p instanceof Piece) {
                p.setOnMouseClicked(e -> {
                    ((Piece) p).beSelected();
                    gameServe.curSelected = (Piece) p;
                });
            }
        });
        Stage stage = new Stage();
        Button button = new Button("确认");
        button.setOnAction(e -> {
            if (flowPane.getChildren().contains(gameServe.shuai) || flowPane.getChildren().contains(gameServe.jiang)) {
                System.out.println("必须添加将帅");
            } else {
                stage.close();
                boardArea.setOnMouseClicked(this::setBoard);
            }
        });

        flowPane.getChildren().add(button);
        Scene scene = new Scene(flowPane);
        stage.setScene(scene);
        stage.setTitle("自定义");
        stage.show();

        boardArea.setDisable(false);
        boardArea.setOnMouseClicked(e -> {
            int x = BoardHelper.pxToX(e.getX());
            int y = BoardHelper.pxToY(e.getY());
            if (x == -1 || y == -1) return;
            System.out.println(x + " " + y);
            flowPane.getChildren().remove(gameServe.curSelected);
            boardArea.getChildren().add(gameServe.curSelected);
//设置位置莫名出错了，棋盘坐标正确，但在界面上的位置异常
            gameServe.curSelected.setPosition(x, y);
            gameServe.curSelected.setOnMouseClicked(null);
        });
    }

    //endregion

}
