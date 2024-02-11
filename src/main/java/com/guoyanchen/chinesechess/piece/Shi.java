package com.guoyanchen.chinesechess.piece;

import com.guoyanchen.chinesechess.serve.GameServe;

public class Shi extends Piece {

    public Shi(GameServe gameServe, int x, int y, boolean isRed) {
        super(gameServe, x, y, isRed);
        name = isRed ? "仕" : "士";
    }

    @Override
    public boolean isMovableTo(int targetX, int targetY) {
        if (Math.abs(targetX - x) != 1 || Math.abs(targetY - y) != 1) //须走斜线
            return false;
        if (targetX < 3 || targetX > 5) return false; //不出九宫
        return (!isRed && targetY <= 2) || (isRed && targetY >= 7);
    }

    @Override
    String getImageName() {
        return isRed ? "rShi.png" : "bShi.png";
    }
}
