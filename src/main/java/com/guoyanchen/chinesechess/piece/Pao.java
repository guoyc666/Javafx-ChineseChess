package com.guoyanchen.chinesechess.piece;

import com.guoyanchen.chinesechess.serve.GameServe;
import com.guoyanchen.chinesechess.setting.BoardHelper;

public class Pao extends Piece {
    public Pao(GameServe gameServe, int x, int y, boolean isRed) {
        super(gameServe, x, y, isRed);
        name = "炮";
    }

    @Override
    public boolean isMovableTo(int targetX, int targetY) {
        if (targetX != x && targetY != y) //不走斜线
            return false;
        int count = BoardHelper.getCount(x, y, targetX, targetY, gameServe.pieces);
        if (gameServe.pieces[targetX][targetY] == null) {
            return count == 0;//无阻,移动
        } else {
            return count == 1;//炮架,吃子
        }
    }

    @Override
    String getImageName() {
        return isRed ? "rPao.png" : "bPao.png";
    }
}
