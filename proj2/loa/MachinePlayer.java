/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import java.util.List;

import static loa.Piece.*;

/** An automated Player.
 *  @author Diraj Thajali
 */
class MachinePlayer extends Player {

    /** A position-score magnitude indicating a win (for white if positive,
     *  black if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new MachinePlayer with no piece or controller (intended to produce
     *  a template). */
    MachinePlayer() {
        this(null, null);
    }

    /** A MachinePlayer that plays the SIDE pieces in GAME. */
    MachinePlayer(Piece side, Game game) {
        super(side, game);
    }

    @Override
    String getMove() {
        Move choice;

        assert side() == getGame().getBoard().turn();
        int depth;
        choice = searchForMove();
        getGame().reportMove(choice);
        return choice.toString();
    }

    @Override
    Player create(Piece piece, Game game) {
        return new MachinePlayer(piece, game);
    }

    @Override
    boolean isManual() {
        return false;
    }

    /** Return a move after searching the game tree to DEPTH>0 moves
     *  from the current position. Assumes the game is not over. */
    private Move searchForMove() {
        Board work = new Board(getBoard());
        int value;
        assert side() == work.turn();
        _foundMove = null;
        if (side() == WP) {
            value = findMove(work, chooseDepth(), true, 1, -INFTY, INFTY);
        } else {
            value = findMove(work, chooseDepth(), true, -1, -INFTY, INFTY);
        }
        return _foundMove;
    }

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _foundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _foundMove. If the game is over
     *  on BOARD, does not set _foundMove. */
    private int findMove(Board board, int depth, boolean saveMove,
                         int sense, int alpha, int beta) {
        // FIXME
        if (depth == 0 || board.gameOver()) {
            return heuristic(board, sense); //static value of this position
        }
        int bestSoFar = 0;
        List<Move> allMoves = board.legalMoves();
        for (Move move : allMoves) {
            board.makeMove(move);
            int best = findMove(board, depth - 1, !saveMove, -sense, alpha, beta);
            if (best > bestSoFar) { //maybe redefine?
                bestSoFar = best;
                if (saveMove) {
                    _foundMove = move; // FIXME
                }
            }
            if (sense == 1) {
                alpha = Math.max(best, alpha);
            } else if (sense == -1) {
                beta = Math.min(best, beta);
            }
            if (alpha >= beta) {
                break;
            }
        }
        return bestSoFar;
        //fixed
    }

    /** Return a search depth for the current position. */
    private int chooseDepth() {
        return 2;  // FIXME
    }

    // FIXME: Other methods, variables here.
    private int heuristic(Board board, int sense) {
        Board copied = new Board(board);
        int bestValue = 0;
        List<Move> moves = copied.legalMoves();
        int possibleVal;
        for (Move move : moves) {
            if (sense == 1) {
                int before = copied.getRegionSizes(copied.turn()).size();
                copied.makeMove(move);
                if (copied.piecesContiguous(copied.turn())) {
                    bestValue = WINNING_VALUE;
                } else if (before > copied.getRegionSizes(copied.turn()).size()) {
                    possibleVal = 500;
                    if (possibleVal > bestValue) {
                        bestValue = possibleVal;
                    }
                } else if (move.isCapture()) {
                    possibleVal = 100;
                    if (possibleVal > bestValue) {
                        bestValue = possibleVal;
                    }
                }
                copied.retract();
            } else if (sense == -1) {
                int before = copied.getRegionSizes(copied.turn().opposite()).size();
                copied.makeMove(move);
                if (copied.piecesContiguous(copied.turn().opposite())) {
                    bestValue = WINNING_VALUE;
                } else if (before > copied.getRegionSizes(copied.turn().opposite()).size()) {
                    possibleVal = -500;
                    if (possibleVal < bestValue) {
                        bestValue = possibleVal;
                    }
                } else if (move.isCapture()) {
                    possibleVal = -100;
                    if (possibleVal < bestValue) {
                        bestValue = possibleVal;
                    }
                }
            }
        }
//            if (sense == 1) {
//                if (board.piecesContiguous(board.turn())) {
//                    bestValue = WINNING_VALUE;
//                } else if (move.isCapture()) {
//                    bestValue = 1000;
//                }
//
//            } else if (sense == -1) {
//                if (board.piecesContiguous(board.turn())) {
//                    bestValue = -WINNING_VALUE;
//                } else if (move.isCapture()) {
//                    bestValue = -1000;
//                }
//            }
            //if both piece are contiguous,
            //try capturing
        return bestValue;
    }


    /** Used to convey moves discovered by findMove. */
    private Move _foundMove;

}
