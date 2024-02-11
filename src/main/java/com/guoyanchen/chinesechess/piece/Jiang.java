package com.guoyanchen.chinesechess.piece;

import com.guoyanchen.chinesechess.serve.GameServe;

public class Jiang extends Piece {

    public Jiang(GameServe gameServe, int x, int y, boolean isRed) {
        super(gameServe, x, y, isRed);
        name = isRed ? "帅" : "将";
    }

    @Override
    public boolean isMovableTo(int targetX, int targetY) {
        if (Math.abs(targetX - x) + Math.abs(targetY - y) != 1) //只能走一格
            return false;
        if (targetX < 3 || targetX > 5) return false;   //不出九宫
        return (!isRed && targetY <= 2) || (isRed && targetY >= 7);
    }

    @Override
    public void die() {
        super.die();
        String result = gameServe.isRedTurn.get() ? "红方获胜" : "黑方获胜";
        gameServe.gameOver(result);
    }

    @Override
    String getImageName() {
        return isRed ? "rJiang.png" : "bJiang.png";
    }
}
