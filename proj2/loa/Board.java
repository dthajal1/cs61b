/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import java.util.*;

import java.util.regex.Pattern;

import static loa.Piece.*;
import static loa.Square.*;

/** Represents the state of a game of Lines of Action.
 *  @author Diraj Thajali
 */
class Board {

    /** Default number of moves for each side that results in a draw. */
    static final int DEFAULT_MOVE_LIMIT = 60;

    /** Pattern describing a valid square designator (cr). */
    static final Pattern ROW_COL = Pattern.compile("^[a-h][1-8]$");

    /** A Board whose initial contents are taken from INITIALCONTENTS
     *  and in which the player playing TURN is to move. The resulting
     *  Board has
     *        get(col, row) == INITIALCONTENTS[row][col]
     *  Assumes that PLAYER is not null and INITIALCONTENTS is 8x8.
     *
     *  CAUTION: The natural written notation for arrays initializers puts
     *  the BOTTOM row of INITIALCONTENTS at the top.
     */
    Board(Piece[][] initialContents, Piece turn) {
        initialize(initialContents, turn);
    }

    /** A new board in the standard initial position. */
    Board() {
        this(INITIAL_PIECES, BP);
    }

    /** A Board whose initial contents and state are copied from
     *  BOARD. */
    Board(Board board) {
        this();
        copyFrom(board);
    }

    /** Set my state to CONTENTS with SIDE to move. */
    void initialize(Piece[][] contents, Piece side) {
        // FIXME Done
        initializedBoard = new Piece[contents.length][contents[0].length];
        for (int i = 0; i < contents.length; i += 1) {
            for (int j = 0; j < contents[0].length; j += 1) {
                Square temp = sq(j,i);
                _board[temp.index()] = initializedBoard[i][j] = contents[i][j];
            }
        }

        //fixed
        _turn = side;
        _moveLimit = DEFAULT_MOVE_LIMIT;
    }

    /** Set me to the initial configuration. */
    void clear() {
        initialize(INITIAL_PIECES, BP);
    }

    /** Set my state to a copy of BOARD. */
    void copyFrom(Board board) {
        if (board == this) {
            return;
        }
        // FIXME Done
        for (int i = 0; i < board.initializedBoard.length; i += 1) {
            for (int j = 0; j < board.initializedBoard[0].length; j += 1) {
                initializedBoard[i][j] = board.initializedBoard[i][j];
            }
        }
        _turn = board._turn;
//        _winner = null;
//        _winnerKnown = false;
//        _subsetsInitialized = false;
        //fixed
    }

    /** Return the contents of the square at SQ. */
    Piece get(Square sq) {
        return _board[sq.index()];
    }

    /** Set the square at SQ to V and set the side that is to move next
     *  to NEXT, if NEXT is not null. */
    void set(Square sq, Piece v, Piece next) {
        // FIXME Done
        _board[sq.index()] = v;
        if (next != null) {
            _turn = next;
        }
        //fixed
    }

    /** Set the square at SQ to V, without modifying the side that
     *  moves next. */
    void set(Square sq, Piece v) {
        set(sq, v, null);
    }

    /** Set limit on number of moves by each side that results in a tie to
     *  LIMIT, where 2 * LIMIT > movesMade(). */
    void setMoveLimit(int limit) {
        if (2 * limit <= movesMade()) {
            throw new IllegalArgumentException("move limit too small");
        }
        _moveLimit = 2 * limit;
    }

    /** Assuming isLegal(MOVE), make MOVE. Assumes MOVE.isCapture()
     *  is false. */
    void makeMove(Move move) {
        assert isLegal(move);
        // FIXME Done
        if (get(move.getTo()) == turn().opposite()) {
            Move capture = Move.mv(move.getFrom(), move.getTo(), true);
            _moves.add(capture);
        } else {
            _moves.add(move);
        }
        set(move.getTo(), get(move.getFrom()), get(move.getFrom()).opposite());
        set(move.getFrom(), EMP, get(move.getFrom())); //might have to switch player

        //fixed
    }

    /** Retract (unmake) one move, returning to the state immediately before
     *  that move.  Requires that movesMade () > 0. */
    void retract() {
        assert movesMade() > 0;
        // FIXME Done
        Move removed = _moves.remove(movesMade() - 1);
        if (removed.isCapture()) {
            _board[removed.getFrom().index()] = get(removed.getTo());
            _board[removed.getTo().index()] = get(removed.getTo()).opposite();
        } else {
            _board[removed.getFrom().index()] = get(removed.getFrom());
            _board[removed.getTo().index()] = EMP;
        }
        //fixed
    }

    /** Return the Piece representing who is next to move. */
    Piece turn() {
        return _turn;
    }

    /** Return true iff FROM - TO is a legal move for the player currently on
     *  move. */
    boolean isLegal(Square from, Square to) {
        // FIXME Done
        return from.isValidMove(to) && !blocked(from, to);
        //fixed
    }

    /** Return true iff MOVE is legal for the player currently on move.
     *  The isCapture() property is ignored. */
    boolean isLegal(Move move) {
        return isLegal(move.getFrom(), move.getTo());
    }

    /** Finds lines of action for the given square on the given direction. */
    int findLineOfAction(Square sq, int dir) {
        int result = 0;
        Square currentDir = Square.sq(sq.col(), sq.row());
        while (currentDir != null) {
            if (get(currentDir) == turn() || get(currentDir) == turn().opposite()) {
                result += 1;
            }
            currentDir = currentDir.moveDest(dir, 1);
        }
        int oppDir = 0;
        if (dir < 4) {
            oppDir = 4 + dir;
        } else if (dir == 4) {
            oppDir = 0;
        } else if (dir > 4) {
            oppDir = dir - 4;
        }
        Square currentOppDir = Square.sq(sq.col(), sq.row());
        if (currentOppDir != null) {
            currentOppDir = currentOppDir.moveDest(oppDir, 1);
        }
        while (currentOppDir != null) {
            if (get(currentOppDir) == turn() || get(currentOppDir) == turn().opposite()) {
                result += 1;
            }
            currentOppDir = currentOppDir.moveDest(oppDir, 1);
        }
        return result;
    }

    /** Return a sequence of all legal moves from this position. */
    List<Move> legalMoves() {
        // FIXME Done
        List<Move> legalMoves = new ArrayList<>();
        for (int i = 0; i < ALL_SQUARES.length; i += 1) {
            if (get(ALL_SQUARES[i]) == turn()) {
                for (int j = 0; j < 8; j += 1) {
                    int LOA = findLineOfAction(ALL_SQUARES[i], j);
                    Square potentialMove = ALL_SQUARES[i].moveDest(j, LOA);
                    if (potentialMove != null) {
                        if (isLegal(ALL_SQUARES[i], potentialMove)) {
                            legalMoves.add(Move.mv(ALL_SQUARES[i], potentialMove));
                        }
                    }
                }
            }
        }
        return legalMoves;
        //fixed
    }

    /** Return true iff the game is over (either player has all his
     *  pieces continguous or there is a tie). */
    boolean gameOver() {
        return winner() != null;
    }

    /** Return true iff SIDE's pieces are continguous. */
    boolean piecesContiguous(Piece side) {
        return getRegionSizes(side).size() == 1;
    }

    /** Return the winning side, if any.  If the game is not over, result is
     *  null.  If the game has ended in a tie, returns EMP. */
    Piece winner() {
        if (!_winnerKnown) {
            //look at whiteRegion and blackRegion and if one's size is greater than the other than that side wins
            // FIXME
            _winnerKnown = true;
        }
        return _winner;
    }

    /** Return the total number of moves that have been made (and not
     *  retracted).  Each valid call to makeMove with a normal move increases
     *  this number by 1. */
    int movesMade() {
        return _moves.size();
    }

    @Override
    public boolean equals(Object obj) {
        Board b = (Board) obj;
        return Arrays.deepEquals(_board, b._board) && _turn == b._turn;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(_board) * 2 + _turn.hashCode();
    }

    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("===%n");
        for (int r = BOARD_SIZE - 1; r >= 0; r -= 1) {
            out.format("    ");
            for (int c = 0; c < BOARD_SIZE; c += 1) {
                out.format("%s ", get(sq(c, r)).abbrev());
            }
            out.format("%n");
        }
        out.format("Next move: %s%n===", turn().fullName());
        return out.toString();
    }

    /** Return true if a move from FROM to TO is blocked by an opposing
     *  piece or by a friendly piece on the target square. */
    private boolean blocked(Square from, Square to) {
        // FIXME Done
        int dir = from.direction(to);
        int LOA = findLineOfAction(from, dir);
        Square curr = Square.sq(from.col(), from.row());
        while (LOA > 0 && curr != null) {
            Square dest = curr.moveDest(dir, 1);
            if (dest != null) {
                if ((dest == to && get(to) == turn()) || (get(dest) == turn().opposite() && dest != to)) {
                    return true;
                }
            }
            curr = dest;
            LOA -= 1;
        }
        if (LOA == 0 && curr != to) {
            return true;
        } else if (LOA > 0) {
            return true;
        }
        return false;
        //fixed
    }

    /** Return the size of the as-yet unvisited cluster of squares
     *  containing P at and adjacent to SQ.  VISITED indicates squares that
     *  have already been processed or are in different clusters.  Update
     *  VISITED to reflect squares counted. */
    private int numContig(Square sq, boolean[][] visited, Piece p) {
        // FIXME
        if (p == EMP || p == p.opposite()) {
            return 0;
        }
        int counter = 1;
        Square[] adjSquares = sq.adjacent();
        for (Square adj : adjSquares) {
            if (get(adj) == p && !visited[adj.row()][adj.col()]) {
                visited[adj.row()][adj.col()] = true;
                counter += numContig(adj, visited, p);
            }
        }
        return counter;
        //fixed
    }

//    private int numContigHelper(Square sq, boolean[][] visited, Piece p, int result) {
//        Square[] adjSquares = sq.adjacent();
//        for (int i = 0; i < adjSquares.length; i += 1) {
//            if (i == adjSquares.length - 1 && get(adjSquares[i]) != p) {
//                return result + 1;
//            }
//            Square a = adjSquares[i];
//            int row, col;
//            row = adjSquares[i].row();
//            col = adjSquares[i].col();
//            Piece b = get(adjSquares[i]);
//            boolean c = visited[row][col];
//            if (get(adjSquares[i]) == p && !visited[adjSquares[i].row()][adjSquares[i].col()]) {
//                visited[adjSquares[i].row()][adjSquares[i].col()] = true;
//                numContigHelper(adjSquares[i], visited, p, result + 1);
//            }
//        }
//        return result;
//    }

    /** Set the values of _whiteRegionSizes and _blackRegionSizes. */
    private void computeRegions() {
        if (_subsetsInitialized) {
            return;
        }
        _whiteRegionSizes.clear();
        _blackRegionSizes.clear();
        // FIXME
        boolean[][] visit = new boolean[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i += 1) {
            for (int j = 0; j < BOARD_SIZE; j += 1) {
                visit[i][j] = false;
            }
        }
        for (int i = 0; i < BOARD_SIZE; i += 1) {
            for (int j = 0; j < BOARD_SIZE; j += 1) {
                Square curr = Square.sq(j, i);
                Piece a = get(curr);
                if (get(curr) != EMP && !visit[i][j]) {
                    if (turn() == WP && get(curr) == turn()) {
                        visit[i][j] = true;
                        int wGroup = numContig(curr, visit, WP);
                        if (wGroup > 0) {
                            _whiteRegionSizes.add(wGroup);
                        }
                    } else {
                        visit[i][j] = true;
                        int bGroup = numContig(curr, visit, BP);
                        if (bGroup > 0) {
                            _blackRegionSizes.add(bGroup);
                        }
                    }
                }
            }
        }
        System.out.println(_whiteRegionSizes.size());
        System.out.println(_blackRegionSizes.size());
        //fixed
        Collections.sort(_whiteRegionSizes, Collections.reverseOrder());
        Collections.sort(_blackRegionSizes, Collections.reverseOrder());
        _subsetsInitialized = true;
    }

    /** Return the sizes of all the regions in the current union-find
     *  structure for side S. */
    List<Integer> getRegionSizes(Piece s) {
        computeRegions();
        if (s == WP) {
            return _whiteRegionSizes;
        } else {
            return _blackRegionSizes;
        }
    }

    // FIXME: Other methods, variables?
    /** Initialized board. */
    Piece[][] initializedBoard;
    //fixed

    /** The standard initial configuration for Lines of Action (bottom row
     *  first). */
    static final Piece[][] INITIAL_PIECES = {
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP }
    };

    /** Current contents of the board.  Square S is at _board[S.index()]. */
    private final Piece[] _board = new Piece[BOARD_SIZE  * BOARD_SIZE];

    /** List of all unretracted moves on this board, in order. */
    private final ArrayList<Move> _moves = new ArrayList<>();
    /** Current side on move. */
    private Piece _turn;
    /** Limit on number of moves before tie is declared.  */
    private int _moveLimit;
    /** True iff the value of _winner is known to be valid. */
    private boolean _winnerKnown;
    /** Cached value of the winner (BP, WP, EMP (for tie), or null (game still
     *  in progress).  Use only if _winnerKnown. */
    private Piece _winner;

    /** True iff subsets computation is up-to-date. */
    private boolean _subsetsInitialized;

    /** List of the sizes of continguous clusters of pieces, by color. */
    private final ArrayList<Integer>
        _whiteRegionSizes = new ArrayList<>(),
        _blackRegionSizes = new ArrayList<>();
}
