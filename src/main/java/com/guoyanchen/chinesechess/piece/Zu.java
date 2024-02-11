package com.guoyanchen.chinesechess.piece;

import com.guoyanchen.chinesechess.serve.GameServe;

public class Zu extends Piece {
    public Zu(GameServe gameServe, int x, int y, boolean isRed) {
        super(gameServe, x, y, isRed);
        name = isRed ? "兵" : "卒";
    }

    @Override
    public boolean isMovableTo(int targetX, int targetY) {
        if (Math.abs(targetX - x) + Math.abs(targetY - y) != 1) //只能走一格
            return false;
        if (isRed) {
            if (y >= 5) {
                return targetY == y - 1;//没过河,只能向上
            } else {
                return targetY != y + 1;//不能后退
            }
        } else {
            if (y <= 4) {
                return targetY == y + 1;//没过河,只能向下
            } else {
                return targetY != y - 1;//不能后退
            }
        }
    }

    @Override
    String getImageName() {
        return isRed ? "rZu.png" : "bZu.png";
    }
}
