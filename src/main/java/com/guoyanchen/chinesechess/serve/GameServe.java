package com.guoyanchen.chinesechess.serve;

import com.guoyanchen.chinesechess.page.CheckAndEat;
import com.guoyanchen.chinesechess.page.MainViewController;
import com.guoyanchen.chinesechess.piece.*;
import com.guoyanchen.chinesechess.setting.BoardHelper;
import com.guoyanchen.chinesechess.setting.Setting;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class GameServe {
    public final MainViewController controller;
    public final BooleanProperty isRedTurn = new SimpleBooleanProperty(false);
    public final List<Piece> rPieces = new ArrayList<>();
    public final List<Piece> bPieces = new ArrayList<>();
    private final Deque<Step> stepList = new LinkedList<>();
    private final CheckAndEat checkAndEat;
    private final StepRecorder stepRecorder;
    public Piece[][] pieces;
    public int turnsCount = 0;
    public Piece curSelected = null;//当前选中的棋子
    public Piece shuai;
    public Piece jiang;

    public GameServe(MainViewController controller) {
        this.controller = controller;
        checkAndEat = new CheckAndEat(controller.getRoot());
        stepRecorder = new StepRecorder(this);
    }

    public void initBoard() {
        isRedTurn.set(false);
        pieces = new Piece[9][10];
        rPieces.clear();
        bPieces.clear();
        curSelected = null;
        turnsCount = 0;
        stepList.clear();
        //region  初始化棋盘,摆放棋子

        //将
        shuai = new Jiang(this, 4, 9, true);
        jiang = new Jiang(this, 4, 0, false);
        rPieces.add(shuai);
        bPieces.add(jiang);

        rPieces.add(new Shi(this, 3, 9, true));
        rPieces.add(new Shi(this, 5, 9, true));
        rPieces.add(new Xiang(this, 2, 9, true));
        rPieces.add(new Xiang(this, 6, 9, true));
        rPieces.add(new Ma(this, 1, 9, true));
        rPieces.add(new Ma(this, 7, 9, true));
        rPieces.add(new Ju(this, 0, 9, true));
        rPieces.add(new Ju(this, 8, 9, true));
        rPieces.add(new Pao(this, 1, 7, true));
        rPieces.add(new Pao(this, 7, 7, true));
        rPieces.add(new Zu(this, 0, 6, true));
        rPieces.add(new Zu(this, 2, 6, true));
        rPieces.add(new Zu(this, 4, 6, true));
        rPieces.add(new Zu(this, 6, 6, true));
        rPieces.add(new Zu(this, 8, 6, true));

        bPieces.add(new Shi(this, 3, 0, false));
        bPieces.add(new Shi(this, 5, 0, false));
        bPieces.add(new Xiang(this, 2, 0, false));
        bPieces.add(new Xiang(this, 6, 0, false));
        bPieces.add(new Ma(this, 1, 0, false));
        bPieces.add(new Ma(this, 7, 0, false));
        bPieces.add(new Ju(this, 0, 0, false));
        bPieces.add(new Ju(this, 8, 0, false));
        bPieces.add(new Pao(this, 1, 2, false));
        bPieces.add(new Pao(this, 7, 2, false));
        bPieces.add(new Zu(this, 0, 3, false));
        bPieces.add(new Zu(this, 2, 3, false));
        bPieces.add(new Zu(this, 4, 3, false));
        bPieces.add(new Zu(this, 6, 3, false));
        bPieces.add(new Zu(this, 8, 3, false));

        //endregion

        checkAndEat.showBegin();
        isRedTurn.set(true);
    }

    public void gameOver(String result) {
        System.out.println("对局结束," + result + "!");
        isRedTurn.set(false);
        controller.endView();

        Alert gameOverAlert = new Alert(Alert.AlertType.INFORMATION);
        gameOverAlert.setTitle("游戏结束");
        gameOverAlert.setHeaderText(result);
        gameOverAlert.setContentText(null);
        gameOverAlert.setGraphic(null);
        gameOverAlert.show();
    }

    public boolean withdraw() {//悔棋
        if (stepList.isEmpty()) return false;

        Step step = stepList.pop();
        step.piece().setPosition(step.fromX(), step.fromY());
        if (step.atePiece() != null) {
            step.atePiece().setPosition(step.toX(), step.toY());
            step.atePiece().setVisible(true);
            if (step.atePiece().isRed) {
                rPieces.add(step.atePiece());
            } else {
                bPieces.add(step.atePiece());
            }
        } else {
            pieces[step.toX()][step.toY()] = null;
        }
        System.out.println(isRedTurn.get() ? "黑方悔棋" : "红方悔棋");
        checkAndEat.showWithdraw();

        isRedTurn.set(!isRedTurn.get());
        if (curSelected != null) {
            curSelected.cancelSelected();
        }
        turnsCount--;
        return true;
    }

    public void curMoveTo(int targetX, int targetY) throws CanNotMoveToException {
        //移动到自身，取消选中
        if (targetX == curSelected.x && targetY == curSelected.y) {
            curSelected.cancelSelected();
        }

        //移动到同阵营，切换选中
        if (pieces[targetX][targetY] != null && pieces[targetX][targetY].isRed == curSelected.isRed) {
            curSelected.cancelSelected();
            pieces[targetX][targetY].beSelected();
            return;
        }

        if (targetX > 8 || targetY > 9) {
            throw new CanNotMoveToException("越界");
        }

        if (!curSelected.isMovableTo(targetX, targetY)) {
            throw new CanNotMoveToException("无法抵达");
        }

        if (!curSelected.isOKifMove(targetX, targetY)) {
            throw new CanNotMoveToException("您将被将军");
        }

        if (Setting.isMusicPlayed.get()) checkAndEat.puttingPlay();
        String record = stepRecorder.getRecord(curSelected.x, curSelected.y, targetX, targetY);
        Step step = new Step(curSelected, curSelected.x, curSelected.y, targetX, targetY, pieces[targetX][targetY]);
        stepList.push(step);

        if (pieces[targetX][targetY] != null) {
            pieces[targetX][targetY].die();//吃子
        }

        //移动
        controller.setRecordTextArea(record);
        pieces[curSelected.x][curSelected.y] = null;
        curSelected.setPosition(targetX, targetY);

        curSelected.cancelSelected();
        curSelected = null;

        //绝杀否
        if (isRedTurn.get() && isBlackImmovable()) {
            gameOver("红方胜利！");
            return;
        } else if (!isRedTurn.get() && isRedImmovable()) {
            gameOver("黑方胜利！");
            return;
        }

        if (isRedTurn.get() && isBlackChecked() || !isRedTurn.get() && isRedChecked()) {
            check();//将军
        }
        isRedTurn.set(!isRedTurn.get());
    }

    public boolean isRedChecked() {
        for (var p : bPieces) {
            if (p.isMovableTo(shuai.x, shuai.y)) {
                return true;
            }
        }
        return BoardHelper.getCount(shuai.x, shuai.y, jiang.x, jiang.y, pieces) == 0;
    }

    public boolean isBlackChecked() {
        for (var p : rPieces) {
            if (p.isMovableTo(jiang.x, jiang.y)) {
                return true;
            }
        }
        return BoardHelper.getCount(shuai.x, shuai.y, jiang.x, jiang.y, pieces) == 0;
    }

    private void check() {
        System.out.println("将军");
        checkAndEat.showCheck();
    }

    public void eat() {
        System.out.println("吃");
        checkAndEat.showEat();
    }

    public boolean isRedImmovable() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 10; j++) {
                if (pieces[i][j] != null && pieces[i][j].isRed) continue;

                for (Piece p : rPieces) {
                    if (p.isMovableTo(i, j)) {
                        if (p.isOKifMove(i, j)) {
                            System.out.printf("[红%s can from (%d,%d) to (%d,%d)]%n", p.name, p.x, p.y, i, j);
                            return false;
                        }
                    }
                }
            }
        }
        checkAndEat.showKilling();
        return true;
    }

    public boolean isBlackImmovable() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 10; j++) {
                if (pieces[i][j] != null && !pieces[i][j].isRed) continue;

                for (Piece p : bPieces) {
                    if (p.isMovableTo(i, j)) {
                        if (p.isOKifMove(i, j)) {
                            System.out.printf("[黑%s can from (%d,%d) to (%d,%d)]%n", p.name, p.x, p.y, i, j);
                            return false;
                        }
                    }
                }
            }
        }
        checkAndEat.showKilling();
        return true;
    }
}
