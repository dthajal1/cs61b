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
//        System.out.println(getBoard().toString());
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
            return heuristic(board);
        }
        Board copied = new Board(board);
        Move bestMove = null;
        if (sense == 1) {
            int bestSoFar = -INFTY;
            List<Move> allMoves = copied.legalMoves();
            for (Move move : allMoves) {
                copied.makeMove(move);
                int best = findMove(copied, depth - 1, false, -sense, alpha, beta);
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
                int best = findMove(copied, depth - 1, false, -sense, alpha, beta);
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
        //fixed
    }

    /** Return a search depth for the current position. */
    private int chooseDepth() {
//        return DEFAULT_DEPTH;  // FIXME
        return 1;
    }

    // FIXME: Other methods, variables here.
//    private int heuristic(Board board, int sense) {
////        Board copied = new Board(board);
////        List<Move> allMoves = copied.legalMoves();
////        int result = 0;
////        for (Move move : allMoves) {
////            ArrayList<Integer> blackBefore = copied.getBlackRegionSizes();
////            ArrayList<Integer> whiteBefore = copied.getWhiteRegionSizes();
////
////            int bestInBB = max(blackBefore);
////            int bestInWB = max(whiteBefore);
////
////            copied.makeMove(move);
////            if (copied.winner() != null) {
////                if (copied.winner() == WP) {
////                    return WINNING_VALUE;
////                }
////                return -WINNING_VALUE;
////            }
////
////            ArrayList<Integer> blackAfter = copied.getBlackRegionSizes();
////            ArrayList<Integer> whiteAfter = copied.getWhiteRegionSizes();
////            int bestInBA = max(blackAfter);
////            int bestInWA = max(whiteAfter);
////
////            if (sense == 1) {
////
////            }
////            if (bestInBA >= bestInBB) {
////                result += getGame().randInt(10000);
////            } else {
////                result -= getGame().randInt(10000);
////            }
////            if (bestInWA >= bestInWB) {
////                result += getGame().randInt(10000);
////            }
////            copied.retract();
////        }
////        return result;
////    }
////
////    public int max(List<Integer> list) {
////        int max = 0;
////        for (int i : list) {
////            if (i > max) {
////                max = i;
////            }
////        }
////        return max;
////    }

    private int heuristic(Board board) {
        Board copied = new Board(board);
        List<Move> allMoves = copied.legalMoves();
        int result = 0;
        for (Move move : allMoves) {
            int before = copied.getRegionSizes(copied.turn()).size();
            Square to = move.getTo();
            Square from = move.getFrom();
//            int distanceD4 = move.getTo().distance(Square.sq(4, 4));
//            int distanceD5 = move.getTo().distance(Square.sq(4, 5));
//            int distanceE4 = move.getTo().distance(Square.sq(5, 4));
//            int distanceE5 = move.getTo().distance(Square.sq(5, 5));
            int distanceD4 = move.getTo().distance(Square.sq(1, 2));
            int distanceD5 = move.getTo().distance(Square.sq(1, 3));
            int distanceE4 = move.getTo().distance(Square.sq(2, 2));
            int distanceE5 = move.getTo().distance(Square.sq(2, 3));
            //Distance of the move.to to the center
            if (distanceD4 <= 1 || distanceD5 <= 1 || distanceE4 <= 1 || distanceE5 <= 1) {
                result += WINNING_VALUE;
            } else if (distanceD4 < 3 || distanceD5 < 3 || distanceE4 < 3 || distanceE5 < 3) {
                result += getGame().randInt(5500);
            } else {
                result -= 200000;
            }
            int me = from.distance(Square.sq(4, 4));
            int me1 = from.distance(Square.sq(4, 5));
            int me2 = from.distance(Square.sq(5, 4));
            int me3 = from.distance(Square.sq(5, 5));
            //Distance of from to the center
            if (me <= 1 || me1 <= 1 || me2 <= 1 || me3 <= 1) {
                continue;
            }
            copied.makeMove(move);
            if (copied.winner() != null) {
                if (copied.winner() == WP) {
                    result += WINNING_VALUE;
                }
                result -= WINNING_VALUE;
            }
            int after = copied.getRegionSizes(copied.turn()).size();

            if (move.isCapture() && after < before) {
                result += getGame().randInt(100000);
            }
            //avoid corners
            //(to.row() == 0 && to.col() == 0) ||
            if ( (to.row() == 0 && to.col() == 7) || (to.row() == 7 && to.col() == 0) || (to.row() == 7 && to.col() == 7)) {
                result -= getGame().randInt(3000);
            }
            copied.retract();
        }
        return result;
    }

    /** Default depth. */
    private static int DEFAULT_DEPTH = 5;

    /** Used to convey moves discovered by findMove. */
    private Move _foundMove;

}
