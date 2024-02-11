package com.guoyanchen.chinesechess.setting;

import com.guoyanchen.chinesechess.piece.Piece;

public class BoardHelper {
    public static final double PIECE_SIZE = 26; //棋子大小
    private static final double ACCURACY = 0.25;//棋盘移动位置的识别精度
    private static final double CELL_SIZE = 57.5; //每个格子的大小
    private static final double LEFT = 50;//棋盘左边界
    private static final double TOP = 52;//棋盘上边界

    public static double xToPx(int x) {//棋盘横坐标转像素坐标
        return LEFT + x * CELL_SIZE;
    }

    public static double yToPx(int y) {//棋盘纵坐标转像素坐标
        return TOP + y * CELL_SIZE;
    }


    public static int pxToX(double px) {//像素转棋盘横坐标
        double t = (px - LEFT) / CELL_SIZE;
        int x = (int) Math.round(t);
        return Math.abs(x - t) > ACCURACY ? -1 : x;
    }

    public static int pxToY(double px) {//像素转棋盘纵坐标
        double t = (px - TOP) / CELL_SIZE;
        int y = (int) Math.round(t);
        return Math.abs(y - t) > ACCURACY ? -1 : y;
    }

    //返回两个棋子中间的棋子数
    public static int getCount(int x1, int y1, int x2, int y2, Piece[][] pieces) {
        if (x1 != x2 && y1 != y2) return -1;//斜线
        if (x1 == x2 && y1 == y2) return -2;//自身

        int count = 0;
        if (x2 == x1 && y2 < y1) {//up
            for (int j = y1 - 1; j > y2; --j) {
                if (pieces[x1][j] != null) ++count;
            }
        } else if (x2 == x1) {//down
            for (int j = y1 + 1; j < y2; ++j) {
                if (pieces[x1][j] != null) ++count;
            }
        } else if (x2 < x1) {//left
            for (int i = x1 - 1; i > x2; --i) {
                if (pieces[i][y1] != null) ++count;
            }
        } else {//right
            for (int i = x1 + 1; i < x2; ++i) {
                if (pieces[i][y2] != null) ++count;
            }
        }
        return count;
    }

    public static void printBoard(Piece[][] pieces) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 9; j++) {
                if (pieces[j][i] == null) {
                    System.out.print(" ");
                } else {
                    System.out.print(pieces[j][i].name);
                }
            }
            System.out.println();
        }
    }
}
