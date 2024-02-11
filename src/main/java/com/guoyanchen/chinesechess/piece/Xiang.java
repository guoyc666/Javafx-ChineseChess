package com.guoyanchen.chinesechess.piece;

import com.guoyanchen.chinesechess.serve.GameServe;

public class Xiang extends Piece {

    public Xiang(GameServe gameServe, int x, int y, boolean isRed) {
        super(gameServe, x, y, isRed);
        name = isRed ? "相" : "象";
    }

    @Override
    String getImageName() {
        return isRed ? "rXiang.png" : "bXiang.png";
    }

    @Override
    public boolean isMovableTo(int targetX, int targetY) {
        if (isRed && targetY <= 4 || !isRed && targetY >= 5) //不得过河
            return false;
        if (Math.abs(targetX - x) != 2 || Math.abs(targetY - y) != 2) //走田字
            return false;
        int midX = (targetX + x) / 2;
        int midY = (targetY + y) / 2;
        return gameServe.pieces[midX][midY] == null;//象眼
    }

}
