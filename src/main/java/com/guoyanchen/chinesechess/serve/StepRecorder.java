package com.guoyanchen.chinesechess.serve;


import java.util.Objects;


public class StepRecorder {
    static final String[] toChineseNum = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
    private final GameServe gameServe;

    public StepRecorder(GameServe gameServe) {
        this.gameServe = gameServe;
    }

    public String getRecord(int x1, int y1, int x2, int y2) {
        return getFrom(x1, y1) + getTo(x1, y1, x2, y2);
    }

    private String getFrom(int x, int y) {

        //车 马 炮
        if (Objects.equals(gameServe.curSelected.name, "车")
                || Objects.equals(gameServe.curSelected.name, "马")
                || Objects.equals(gameServe.curSelected.name, "炮")) {
            int flag = 0;
            for (int j = 0; j < 10; j++) {
                if (gameServe.pieces[x][j] == null || j == y) continue;
                if (Objects.equals(gameServe.pieces[x][j].name, gameServe.curSelected.name)
                        && gameServe.pieces[x][j].isRed == gameServe.curSelected.isRed) {
                    flag = j < y ? -1 : 1;
                    break;
                }
            }
            if (flag == 0) return gameServe.curSelected.name + getColumn(x);
            else if (flag < 0 && gameServe.curSelected.isRed || flag > 0 && !gameServe.curSelected.isRed) {
                return "后" + gameServe.curSelected.name;
            } else {
                return "前" + gameServe.curSelected.name;
            }

        }

        //兵 卒
        if (gameServe.curSelected.name.compareTo("兵") == 0) {
            int up = 0;
            for (int j = 0; j < y; j++) {
                if (gameServe.pieces[x][j] != null && gameServe.pieces[x][j].name.compareTo(gameServe.curSelected.name) == 0) {
                    up++;
                }
            }
            for (int j = y + 1; j < 10; j++) {
                if (gameServe.pieces[x][j] != null && gameServe.pieces[x][j].name.compareTo(gameServe.curSelected.name) == 0) {
                    return toChineseNum[up + 1] + getColumn(x);
                }
            }
            if (up == 0) return "兵" + getColumn(x);
            return toChineseNum[up + 1] + getColumn(x);
        }
        if (gameServe.curSelected.name.compareTo("卒") == 0) {
            int down = 0;
            for (int j = y + 1; j < 10; j++) {
                if (gameServe.pieces[x][j] != null && gameServe.pieces[x][j].name.compareTo(gameServe.curSelected.name) == 0) {
                    down++;
                }
            }
            for (int j = 0; j < y; j++) {
                if (gameServe.pieces[x][j] != null && gameServe.pieces[x][j].name.compareTo(gameServe.curSelected.name) == 0) {
                    return toChineseNum[down + 1] + getColumn(x);
                }
            }
            if (down == 0) return "卒" + getColumn(x);
        }

        // 将 帅 士 仕 像 相
        return gameServe.curSelected.name + getColumn(x);
    }

    private String getTo(int x1, int y1, int x2, int y2) {
        if (Objects.equals(gameServe.curSelected.name, "马") ||
                Objects.equals(gameServe.curSelected.name, "相") ||
                Objects.equals(gameServe.curSelected.name, "象") ||
                Objects.equals(gameServe.curSelected.name, "仕") ||
                Objects.equals(gameServe.curSelected.name, "士")) {
            if (y2 < y1) {
                return gameServe.curSelected.isRed ? "进" + toChineseNum[9 - x2] : "退" + (9 - x2);
            } else {
                return gameServe.curSelected.isRed ? "退" + toChineseNum[9 - x2] : "进" + (9 - x2);
            }
        } else {
            if (y2 < y1) {
                return gameServe.curSelected.isRed ? "进" + toChineseNum[y1 - y2] : "退" + (y1 - y2);
            } else if (y2 > y1) {
                return gameServe.curSelected.isRed ? "退" + toChineseNum[y2 - y1] : "进" + (y2 - y1);
            } else {
                return "平" + getColumn(x2);
            }
        }
    }

    private String getColumn(int x) {
        return gameServe.curSelected.isRed ? toChineseNum[9 - x] : Integer.toString(x + 1);
    }

}