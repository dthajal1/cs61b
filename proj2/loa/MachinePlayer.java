/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import javax.print.DocFlavor;
import java.util.List;
import java.util.Random;

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
//        System.out.println(getBoard().toString());
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
            return heuristic(board);
        }
        int bestSoFar = 0;
        Board copied = new Board(board);
        List<Move> allMoves = copied.legalMoves();
        for (Move move : allMoves) {
            board.makeMove(move);
            int best = findMove(copied, depth - 1, false, -sense, alpha, beta);
            if (sense == 1) {
                bestSoFar = -INFTY;
                bestSoFar = Math.max(bestSoFar, best);
                if (saveMove) {
                    _foundMove = move;
                }
                alpha = Math.max(best, alpha);
            } else {
                bestSoFar = INFTY;
                bestSoFar = Math.min(bestSoFar, best);
                if (saveMove) {
                    _foundMove = move;
                }
                beta = Math.min(best, beta);
            }
            if (alpha >= beta) {
                break;
            }
            board.retract();
        }

        return bestSoFar;
        //fixed
    }

    /** Return a search depth for the current position. */
    private int chooseDepth() {
        return 1;  // FIXME
    }

    // FIXME: Other methods, variables here.
    private int heuristic(Board board) {
        Board copied = new Board(board);
        List<Move> allMoves = copied.legalMoves();
        int result = 0;
        for (Move move : allMoves) {
            int beforeOpp = copied.getRegionSizes(copied.turn().opposite()).size();
            int beforeMe = copied.getRegionSizes(copied.turn()).size();
            Square to = move.getTo();
            int distanceD4 = move.getFrom().distance(Square.sq(4, 4));
            int distanceD5 = move.getFrom().distance(Square.sq(4, 5));
            int distanceE4 = move.getFrom().distance(Square.sq(5, 4));
            int distanceE5 = move.getFrom().distance(Square.sq(5, 5));
            copied.makeMove(move);
            if (distanceD4 <= 2 || distanceD5 <= 2 || distanceE4 <= 2 || distanceE5 <= 2) {
                result += getGame().randInt(100);
            }
            if (to.col() == 7 || to.row() == 7 || to.row() == 0 || to.col() == 0) {
                result -= getGame().randInt(70);
            } else {
                result += getGame().randInt(70);
            }
            int afterOpp = copied.getRegionSizes(copied.turn().opposite()).size();
            int afterMe = copied.getRegionSizes(copied.turn()).size();
            if (move.isCapture()) {
                result += getGame().randInt(80);
                if (copied.piecesContiguous(copied.turn())) {
                    result += WINNING_VALUE;
                }
                if (copied.piecesContiguous(copied.turn().opposite())) {
                    result -= WINNING_VALUE;
                }
            }
            if (copied.piecesContiguous(copied.turn())) {
                result += WINNING_VALUE;
            }
            if (beforeOpp < afterOpp && beforeMe > afterMe) {
                result += WINNING_VALUE - 1000;
            }
            if (beforeOpp < afterOpp) {
                result += getGame().randInt(1000);
            }
            if (beforeMe > afterMe) {
                result += getGame().randInt(1000);
            }
            copied.retract();
        }
        return result;
    }


    /** Used to convey moves discovered by findMove. */
    private Move _foundMove;

}
