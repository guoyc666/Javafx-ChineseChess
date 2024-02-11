package com.guoyanchen.chinesechess.piece;

import com.guoyanchen.chinesechess.serve.GameServe;
import com.guoyanchen.chinesechess.setting.BoardHelper;

public class Ju extends Piece {

    public Ju(GameServe gameServe, int x, int y, boolean isRed) {
        super(gameServe, x, y, isRed);
        name = "车";
    }

    @Override
    public boolean isMovableTo(int targetX, int targetY) {
        if (targetX != x && targetY != y) return false;
        int count = BoardHelper.getCount(x, y, targetX, targetY, gameServe.pieces);
        return count == 0; //无阻碍
    }

    @Override
    String getImageName() {
        return isRed ? "rJu.png" : "bJu.png";
    }
}
