package com.guoyanchen.chinesechess.piece;

import com.guoyanchen.chinesechess.serve.CanNotMoveToException;
import com.guoyanchen.chinesechess.serve.GameServe;
import com.guoyanchen.chinesechess.setting.BoardHelper;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.util.Objects;

public abstract class Piece extends Circle {
    public final boolean isRed;// true 红棋 | false 黑棋
    final GameServe gameServe;
    public int x;//棋盘横坐标
    public int y;//棋盘纵坐标
    public String name;

    public Piece(GameServe gameServe, int x, int y, boolean isRed) {
        this.gameServe = gameServe;
        setPosition(x, y);
        this.isRed = isRed;

        setRadius(BoardHelper.PIECE_SIZE);
        String imagePath = getImageName();
        Image image = new Image(String.valueOf(getClass().getResource(imagePath)));
        setFill(new ImagePattern(image));

        //棋子点击事件
        setOnMouseClicked(e -> {
            System.out.println("点击棋子");
            try {
                if (gameServe.isRedTurn.get() == this.isRed) {
                    beSelected(); //当前回合，选中
                } else if (gameServe.curSelected == null) {
                    throw new CanNotMoveToException("不在回合内");
                } else {//吃棋
                    gameServe.curMoveTo(this.x, this.y);
                }
            } catch (CanNotMoveToException exception) {
                exception.showError(gameServe.controller.getBoardArea());
            }
            e.consume();//消费点击事件，防止传递到棋盘
        });
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        gameServe.pieces[x][y] = this;
        setCenterX(BoardHelper.xToPx(x));
        setCenterY(BoardHelper.yToPx(y));
    }


    public abstract boolean isMovableTo(int targetX, int targetY);

    abstract String getImageName();

    public void beSelected() {
        if (gameServe.curSelected != null && gameServe.curSelected.equals(this)) cancelSelected();
        else {
            this.setStroke(Color.SNOW);
            this.setStrokeWidth(2);
            this.setEffect(new DropShadow(8, 3, 8, Color.DIMGRAY));
            this.setRadius(this.getRadius() * 1.1);

            if (gameServe.curSelected != null) {
                gameServe.curSelected.cancelSelected();
            }
            gameServe.curSelected = this;

        }
    }

    public void cancelSelected() {
        this.setRadius(BoardHelper.PIECE_SIZE);
        this.setEffect(null);
        this.setStroke(null);

        gameServe.curSelected = null;
    }

    public void die() {
        this.setVisible(false);
        gameServe.pieces[x][y] = null;
        if (this.isRed) {
            gameServe.rPieces.remove(this);
        } else {
            gameServe.bPieces.remove(this);
        }
        gameServe.eat();
    }

    public boolean isOKifMove(int targetX, int targetY) {
        //如果移动到target是否安全。 false-被将军，不可移动
        Piece target = gameServe.pieces[targetX][targetY];
        if (target != null && (Objects.equals(target.name, "帅") || Objects.equals(target.name, "将"))) return true;
        int tempX = x;
        int tempY = y;


        gameServe.pieces[targetX][targetY] = this;
        gameServe.pieces[tempX][tempY] = null;
        x = targetX;
        y = targetY;
        boolean is;
        if (target != null) {
            if (isRed) {
                gameServe.bPieces.remove(target);
                is = gameServe.isRedChecked();
                gameServe.bPieces.add(target);
            } else {
                gameServe.rPieces.remove(target);
                is = gameServe.isBlackChecked();
                gameServe.rPieces.add(target);
            }
        } else {
            is = isRed ? gameServe.isRedChecked() : gameServe.isBlackChecked();
        }
        x = tempX;
        y = tempY;
        gameServe.pieces[targetX][targetY] = target;
        gameServe.pieces[tempX][tempY] = this;

        return !is;
    }

    @Override
    public String toString() {

        return String.format("%s%s(%d,%d)", isRed ? "红" : "黑", name, x, y);
    }

}