package com.guoyanchen.chinesechess.piece;

import com.guoyanchen.chinesechess.serve.GameServe;

public class Ma extends Piece {

    public Ma(GameServe gameServe, int x, int y, boolean isRed) {
        super(gameServe, x, y, isRed);
        name = "马";
    }

    @Override
    String getImageName() {
        return isRed ? "rMa.png" : "bMa.png";
    }

    @Override
    public boolean isMovableTo(int targetX, int targetY) {
        if (Math.abs(targetX - x) == 2 && Math.abs(targetY - y) == 1) {//横跳日
            return gameServe.pieces[(targetX + x) / 2][y] == null;//马腿
        }
        if (Math.abs(targetX - x) == 1 && Math.abs(targetY - y) == 2) {//纵跳日
            return gameServe.pieces[x][(targetY + y) / 2] == null;//马腿
        }
        return false;
    }

}
