/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;


import java.util.Formatter;
import java.util.regex.Pattern;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
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
        for (int i = 0; i < contents.length; i += 1) {
            for (int j = 0; j < contents[0].length; j += 1) {
                Square temp = sq(j, i);
                _board[temp.index()] = contents[i][j];
            }
        }
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
        System.arraycopy(board._board, 0, _board, 0, board._board.length);
        _moveLimit = board._moveLimit;
        _turn = board.turn();
        _winner = board._winner;
        _winnerKnown = board._winnerKnown;
        _subsetsInitialized = board._subsetsInitialized;
    }

    /** Return the contents of the square at SQ. */
    Piece get(Square sq) {
        return _board[sq.index()];
    }

    /** Set the square at SQ to V and set the side that is to move next
     *  to NEXT, if NEXT is not null. */
    void set(Square sq, Piece v, Piece next) {
        _board[sq.index()] = v;
        if (next != null) {
            _turn = next;
        }
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

    /** Assuming isLegal(MOVE), make MOVE. This function assumes that
     *  MOVE.isCapture() will return false.  If it saves the move for
     *  later retraction, makeMove itself uses MOVE.captureMove() to produce
     *  the capturing move. */
    void makeMove(Move move) {
        assert isLegal(move);
        if (get(move.getTo()) == turn().opposite()) {
            Move capture = Move.mv(move.getFrom(), move.getTo(), true);
            _moves.add(capture);
        } else {
            _moves.add(move);
        }
        set(move.getTo(), get(move.getFrom()), turn().opposite());
        set(move.getFrom(), EMP);
        _subsetsInitialized = false;

    }

    /** Retract (unmake) one move, returning to the state immediately before
     *  that move.  Requires that movesMade () > 0. */
    void retract() {
        assert movesMade() > 0;
        Move removed = _moves.remove(movesMade() - 1);
        if (removed.isCapture()) {
            _board[removed.getFrom().index()] = get(removed.getTo());
            _board[removed.getTo().index()] = get(removed.getTo()).opposite();
        } else {
            _board[removed.getFrom().index()] = get(removed.getTo());
            _board[removed.getTo().index()] = EMP;
        }
        _turn = turn().opposite();
        _subsetsInitialized = false;
    }

    /** Return the Piece representing who is next to move. */
    Piece turn() {
        return _turn;
    }

    /** Return true iff FROM - TO is a legal move for the player currently on
     *  move. */
    boolean isLegal(Square from, Square to) {
        if (get(from) == EMP) {
            return false;
        }
        return from.isValidMove(to) && !blocked(from, to);
    }

    /** Return true iff MOVE is legal for the player currently on move.
     *  The isCapture() property is ignored. */
    boolean isLegal(Move move) {
        return isLegal(move.getFrom(), move.getTo());
    }

    /** @param sq initial square
     * @param dir direction
     * @return int
     * Finds lines of action for the given square on the given direction. */
    int findLineOfAction(Square sq, int dir) {
        int result = 0;
        Square currentDir = Square.sq(sq.col(), sq.row());
        while (currentDir != null) {
            if (get(currentDir) == turn()
                    || get(currentDir) == turn().opposite()) {
                result += 1;
            }
            currentDir = currentDir.moveDest(dir, 1);
        }
        int oppDir;
        if (dir < 4) {
            oppDir = 4 + dir;
        } else if (dir == 4) {
            oppDir = 0;
        } else {
            oppDir = dir - 4;
        }
        Square currentOppDir = Square.sq(sq.col(), sq.row());
        if (currentOppDir != null) {
            currentOppDir = currentOppDir.moveDest(oppDir, 1);
        }
        while (currentOppDir != null) {
            if (get(currentOppDir) == turn()
                    || get(currentOppDir) == turn().opposite()) {
                result += 1;
            }
            currentOppDir = currentOppDir.moveDest(oppDir, 1);
        }
        return result;
    }

    /** Return a sequence of all legal moves from this position. */
    List<Move> legalMoves() {
        List<Move> legalMoves = new ArrayList<>();
        for (Square allSquare : ALL_SQUARES) {
            if (get(allSquare) == turn()) {
                for (int j = 0; j < 8; j += 1) {
                    int loa = findLineOfAction(allSquare, j);
                    Square potentMove = allSquare.moveDest(j, loa);
                    if (potentMove != null) {
                        if (isLegal(allSquare, potentMove)) {
                            legalMoves.add(Move.mv(allSquare, potentMove));
                        }
                    }
                }
            }
        }
        return legalMoves;
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
            if (piecesContiguous(turn().opposite())) {
                _winner = turn().opposite();
                _winnerKnown = true;
            } else if (piecesContiguous(turn())) {
                _winner = turn();
                _winnerKnown = true;
            }
            _subsetsInitialized = false;
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
        if (getClass() != obj.getClass()) {
            return false;
        }
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
        int dir = from.direction(to);
        int loa = findLineOfAction(from, dir);
        Square curr = Square.sq(from.col(), from.row());
        while (loa > 0 && curr != null) {
            Square dest = curr.moveDest(dir, 1);
            if (dest != null) {
                if ((dest == to && get(to) == turn())
                        || (get(dest) == turn().opposite() && dest != to)) {
                    return true;
                }
            }
            curr = dest;
            loa -= 1;
        }
        if (loa == 0 && curr != to) {
            return true;
        } else return loa > 0;
    }

    /** Return the size of the as-yet unvisited cluster of squares
     *  containing P at and adjacent to SQ.  VISITED indicates squares that
     *  have already been processed or are in different clusters.  Update
     *  VISITED to reflect squares counted. */
    private int numContig(Square sq, boolean[][] visited, Piece p) {
        if (p == EMP || p == get(sq).opposite()) {
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
    }

    /** Set the values of _whiteRegionSizes and _blackRegionSizes. */
    private void computeRegions() {
        if (_subsetsInitialized) {
            return;
        }
        _whiteRegionSizes.clear();
        _blackRegionSizes.clear();
        boolean[][] visit = new boolean[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i += 1) {
            for (int j = 0; j < BOARD_SIZE; j += 1) {
                visit[i][j] = false;
            }
        }
        for (int i = 0; i < BOARD_SIZE; i += 1) {
            for (int j = 0; j < BOARD_SIZE; j += 1) {
                Square curr = Square.sq(j, i);
                if (get(curr) != null && get(curr) != EMP && !visit[i][j]) {
                    visit[i][j] = true;
                    int bGroup = numContig(curr, visit, BP);
                    if (bGroup > 0) {
                        _blackRegionSizes.add(bGroup);
                    }
                    int wGroup = numContig(curr, visit, WP);
                    if (wGroup > 0) {
                        _whiteRegionSizes.add(wGroup);
                    }
                }
            }
        }

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
