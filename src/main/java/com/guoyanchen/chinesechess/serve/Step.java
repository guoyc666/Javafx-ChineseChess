package com.guoyanchen.chinesechess.serve;

import com.guoyanchen.chinesechess.piece.Piece;

public record Step(Piece piece, int fromX, int fromY, int toX, int toY, Piece atePiece) {

}
