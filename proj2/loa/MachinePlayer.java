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
        if (depth == 0 || board.gameOver()) {
            return heuristic(board);
        }
        Board copied = new Board(board);
        Move bestMove = null;
        if (sense == 1) {
            int bestSoFar = -INFTY;
            List<Move> allMoves = copied.legalMoves();
            for (Move move : allMoves) {
                copied.makeMove(move);
                int best = findMove(copied, depth - 1,
                        false, -sense, alpha, beta);
                copied.retract();
                if (best >= bestSoFar) {
                    bestSoFar = best;
                    alpha = Math.max(best, alpha);
                    bestMove = move;
                    if (alpha >= beta) {
                        break;
                    }
                }
            }
            if (saveMove) {
                _foundMove = bestMove;
            }
            return bestSoFar;
        } else {
            int bestSoFar = INFTY;
            List<Move> allMoves = copied.legalMoves();
            for (Move move : allMoves) {
                copied.makeMove(move);
                int best = findMove(copied, depth - 1,
                        false, -sense, alpha, beta);
                copied.retract();
                if (best <= bestSoFar) {
                    bestSoFar = best;
                    beta = Math.min(best, beta);
                    bestMove = move;
                    if (alpha >= beta) {
                        break;
                    }
                }
            }
            if (saveMove) {
                _foundMove = bestMove;
            }
            return bestSoFar;
        }
    }

    /** Return a search depth for the current position. */
    private int chooseDepth() {
        return DEFAULT_DEPTH;
    }

    /** @return int
     * @param board
     * Evaluation function. */
    private int heuristic(Board board) {
        Board copied = new Board(board);
        List<Move> allMoves = copied.legalMoves();
        int result = 0;
        for (Move move : allMoves) {
            int before = copied.getRegionSizes(copied.turn()).size();
            Square to = move.getTo();
            Square from = move.getFrom();
            int distanceD4 = to.distance(Square.sq(1, 2));
            int distanceD5 = to.distance(Square.sq(1, 3));
            int distanceE4 = to.distance(Square.sq(2, 2));
            int distanceE5 = to.distance(Square.sq(2, 3));
            if (distanceD4 <= 1 || distanceD5 <= 1
                    || distanceE4 <= 1 || distanceE5 <= 1) {
                if (move.isCapture()) {
                    result += getGame().randInt(HUN_THOU);
                } else {
                    result += getGame().randInt(TEN_THOU);
                }
            } else if (distanceD4 < 3 || distanceD5 < 3
                    || distanceE4 < 3 || distanceE5 < 3) {
                if (move.isCapture()) {
                    result += getGame().randInt(FIVE_500);
                } else {
                    result += getGame().randInt(FIVE_THOU);
                }
            } else {
                result -= TWO_HUN_THOU;
            }
            int me = from.distance(Square.sq(4, 4));
            int me1 = from.distance(Square.sq(4, 5));
            int me2 = from.distance(Square.sq(5, 4));
            int me3 = from.distance(Square.sq(5, 5));
            if (me <= 1 || me1 <= 1 || me2 <= 1 || me3 <= 1) {
                continue;
            }
            copied.makeMove(move);
            if (copied.piecesContiguous(copied.turn())) {
                result += WINNING_VALUE;
            } else if (copied.piecesContiguous(copied.turn().opposite())) {
                result -= WINNING_VALUE;
            }
            if (copied.winner() != null) {
                Piece winner = copied.winner();
                if (winner == WP) {
                    result += getGame().randInt(TEN_THOU1);
                } else if (winner == BP) {
                    result -= getGame().randInt(TEN_THOU2);
                }
            }
            int after = copied.getRegionSizes(copied.turn()).size();

            if (move.isCapture() && after < before) {
                result += getGame().randInt(HUN_THOU1);
            }
            if ((to.row() == 0 && to.col() == 7) || (to.row() == 7
                    && to.col() == 0) || (to.row() == 7 && to.col() == 7)) {
                result -= getGame().randInt(THREE_THOU);
            }
            copied.retract();
        }
        return result;
    }

    /** Default depth. */
    private static final int DEFAULT_DEPTH = 2;

    private static final int HUN_THOU = 100000;
    private static final int TEN_THOU = 10000;
    private static final int FIVE_500 = 5500;
    private static final int FIVE_THOU = 5000;
    private static final int TEN_THOU1 = 10001;
    private static final int TEN_THOU2 = 10000;
    private static final int HUN_THOU1 = 100001;
    private static final int THREE_THOU = 3000;
    private static final int TWO_HUN_THOU =200000;



    /** Used to convey moves discovered by findMove. */
    private Move _foundMove;

}
