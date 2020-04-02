/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import com.sun.tools.internal.ws.wsdl.document.soap.SOAP12Binding;
import com.sun.xml.internal.xsom.impl.scd.Iterators;
import org.junit.Test;

import java.security.AlgorithmConstraints;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static loa.Square.ALL_SQUARES;
import static org.junit.Assert.*;

import static loa.Piece.*;
import static loa.Square.sq;
import static loa.Move.mv;

/** Tests of the Board class API.
 *  @author Diraj Thajali
 */
public class BoardTest {

    // my tests
    @Test
    public void testInitializeBoard() {
        Board a = new Board();
        a.initialize(BOARD1, BP);
        for (int i = 0; i < a.initializedBoard.length; i += 1) {
            for (int j = 0; j < a.initializedBoard[0].length; j += 1) {
                assertEquals(BOARD1[i][j], a.initializedBoard[i][j]);
            }
        }
    }

    @Test
    public void testFindLinesOfAction() {
        Board a = new Board();
        a.initialize(BOARD1, WP);
        for (int i = 0; i < ALL_SQUARES.length; i += 1) {
            if (i == 3) {
                assertEquals(3, a.findLineOfAction(ALL_SQUARES[i], 2));
            } else if (i == 24) {
                assertEquals(1, a.findLineOfAction(ALL_SQUARES[i], 1));
            }
        }
    }

    @Test
    public void testLegalMoves() {
        Board a = new Board(BOARD1, BP);
        List<Move> moves = a.legalMoves();
        int count = 0;
        for (Move move : moves) {
            if (a.isLegal(move)) {
                count += 1;
            }
        }
        assertEquals(count, moves.size());
    }

    @Test
    public void test() {
        Board a = new Board(BOARD1, BP);
        a.makeMove(Move.mv("b1-b3"));
        Board b = new Board();
        a.copyFrom(b);
        System.out.println(a.toString());
    }

    @Test
    public void testWinner() {
//        Pattern b = Pattern.compile("[\\S]*");
//        Matcher c = b.matcher("Diraj");
//        System.out.println(c.matches());
        Board a = new Board(testBoard, WP);
        System.out.println(a.toString());
        assertFalse(a.gameOver());
        Move move = Move.mv("b1-e4");
        a.makeMove(move);
        assertTrue(a.gameOver());
        assertEquals(WP, a.winner());
    }

    static final Piece[][] testBoard = {
            { EMP, WP,  EMP,  EMP,  EMP, EMP, EMP, EMP },
            { EMP, EMP,  EMP,  EMP,  EMP, EMP, EMP, EMP },
            { EMP, EMP,  EMP,  EMP,  EMP, EMP, EMP, EMP },
            { EMP, BP,  WP,  EMP,  BP, EMP, EMP, EMP },
            { EMP, WP,  BP,  WP,  WP, EMP, EMP, EMP },
            { EMP, EMP,  BP,  BP,  WP, WP, WP, EMP },
            { EMP, WP,  WP,  WP,  EMP, EMP, EMP, EMP },
            { EMP, EMP,  EMP,  EMP,  EMP, EMP, EMP, EMP },
    };

//    static final Piece[][] testBoard1 = {
//            { EMP, EMP,  EMP,  EMP,  EMP, EMP, EMP, EMP },
//            { EMP, EMP,  EMP,  EMP,  EMP, EMP, EMP, EMP },
//            { EMP, EMP,  EMP,  EMP,  EMP, EMP, EMP, EMP },
//            { EMP, EMP,  EMP,  EMP,  EMP, EMP, EMP, EMP },
//            { EMP, EMP,  EMP,  EMP,  EMP, EMP, EMP, EMP },
//            { EMP, EMP,  EMP,  EMP,  EMP, EMP, EMP, EMP },
//            { EMP, EMP,  EMP,  EMP,  EMP, EMP, EMP, EMP },
//            { EMP, EMP,  EMP,  EMP,  EMP, EMP, EMP, EMP },
//    };

    //my tests ends
    /** A "general" position. */
    static final Piece[][] BOARD1 = {
        { EMP, BP,  EMP,  BP,  BP, EMP, EMP, EMP },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP,  BP,  BP, EMP, WP  },
        { WP,  EMP,  BP, EMP, EMP,  WP, EMP, EMP  },
        { WP,  EMP,  WP,  WP, EMP,  WP, EMP, EMP  },
        { WP,  EMP, EMP, EMP,  BP, EMP, EMP, WP  },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP  },
        { EMP, BP,  BP,  BP,  EMP,  BP,  BP, EMP }
    };

    /** A position in which black, but not white, pieces are contiguous. */
    static final Piece[][] BOARD2 = {
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP,  BP,  WP,  BP,  BP,  BP, EMP, EMP },
        { EMP,  WP,  BP,  WP,  WP, EMP, EMP, EMP },
        { EMP, EMP,  BP,  BP,  WP,  WP, EMP,  WP },
        { EMP,  WP,  WP,  BP, EMP, EMP, EMP, EMP },
        { EMP, EMP, EMP,  BP, EMP, EMP, EMP, EMP },
    };

    /** A position in which black, but not white, pieces are contiguous. */
    static final Piece[][] BOARD3 = {
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP,  BP,  WP,  BP,  WP, EMP, EMP, EMP },
        { EMP,  WP,  BP,  WP,  WP, EMP, EMP, EMP },
        { EMP, EMP,  BP,  BP,  WP,  WP,  WP, EMP },
        { EMP,  WP,  WP,  WP, EMP, EMP, EMP, EMP },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
    };


    static final String BOARD1_STRING =
        "===\n"
        + "    - b b b - b b - \n"
        + "    - - - - - - - - \n"
        + "    w - - - b - - w \n"
        + "    w - w w - w - - \n"
        + "    w - b - - w - - \n"
        + "    w - - - b b - w \n"
        + "    w - - - - - - w \n"
        + "    - b - b b - - - \n"
        + "Next move: black\n"
        + "===";

    /** Test display */
    @Test
    public void toStringTest() {
        assertEquals(BOARD1_STRING, new Board(BOARD1, BP).toString());
    }

    /** Test legal moves. */
    @Test
    public void testLegality1() {
        Board b = new Board(BOARD1, BP);
        assertTrue("f3-d5", b.isLegal(mv("f3-d5")));
        assertTrue("f3-h5", b.isLegal(mv("f3-h5")));
        assertTrue("f3-h1", b.isLegal(mv("f3-h1")));
        assertTrue("f3-b3", b.isLegal(mv("f3-b3")));
        assertFalse("f3-d1", b.isLegal(mv("f3-d1")));
        assertFalse("f3-h3", b.isLegal(mv("f3-h3")));
        assertFalse("f3-e4", b.isLegal(mv("f3-e4")));
        assertFalse("c4-c7", b.isLegal(mv("c4-c7")));
        assertFalse("b1-b4", b.isLegal(mv("b1-b4")));
    }

    /** Test contiguity. */
    @Test
    public void testContiguous1() {
        Board b1 = new Board(BOARD1, BP);
        assertFalse("Board 1 black contiguous?", b1.piecesContiguous(BP));
        assertFalse("Board 1 white contiguous?", b1.piecesContiguous(WP));
        assertFalse("Board 1 game over?", b1.gameOver());
        Board b2 = new Board(BOARD2, BP);
        assertTrue("Board 2 black contiguous?", b2.piecesContiguous(BP));
        assertFalse("Board 2 white contiguous?", b2.piecesContiguous(WP));
        assertTrue("Board 2 game over", b2.gameOver());
        Board b3 = new Board(BOARD3, BP);
        assertTrue("Board 3 white contiguous?", b3.piecesContiguous(WP));
        assertTrue("Board 3 black contiguous?", b3.piecesContiguous(WP));
        assertTrue("Board 3 black contiguous?", b3.piecesContiguous(BP));
        assertTrue("Board 3 game over", b3.gameOver());
    }

    @Test
    public void testEquals1() {
        Board b1 = new Board(BOARD1, BP);
        Board b2 = new Board(BOARD1, BP);

        assertEquals("Board 1 equals Board 1", b1, b2);
    }

    @Test
    public void testMove1() {
        Board b0 = new Board(BOARD1, BP);
        Board b1 = new Board(BOARD1, BP);
        b1.makeMove(mv("f3-d5"));
        assertEquals("square d5 after f3-d5", BP, b1.get(sq(3, 4)));
        assertEquals("square f3 after f3-d5", EMP, b1.get(sq(5, 2)));
        assertEquals("Check move count for board 1 after one move",
                     1, b1.movesMade());
        b1.retract();
        assertEquals("Check for board 1 restored after retraction", b0, b1);
        assertEquals("Check move count for board 1 after move + retraction",
                     0, b1.movesMade());
    }

}
