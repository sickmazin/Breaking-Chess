package project.backend.bitboard;

import project.backend.bitboard.Utils.SQUARE;

import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import static project.backend.bitboard.Constant.*;
import static project.backend.bitboard.Utils.SQUARE.*;

public class Board {
    //private final static String Piece_Regex = "[pbnrqkPBNRQK]";
    private static long[][] PAWN_ATTACK = new long[2][64];
    private static long[] KNIGHT_ATTACK = new long[64];
    private static long[] KING_ATTACK = new long[64];
    private static long[] BISHOP_MASK = new long[64];
    private static long[] ROOK_MASK = new long[64];
    private static long[][] PAWN_MOVEMENT = new long[2][64];

    private static long[][] BISHOP_ATTACK = new long[64][512];
    private static long[][] ROOK_ATTACK = new long[64][4096];

    private long WP = 0L, WN = 0L, WB = 0L, WR = 0L, WQ = 0L, WK = 0L,
                 BP = 0L, BN = 0L, BB = 0L, BR = 0L, BQ = 0L, BK = 0L;

    //private final HashMap<Character, Long> piece = new HashMap<>();

    private long WO, BO, O;

    private int side;
    private int castle;
    private SQUARE enPassant = NO_SQUARE;
    private int fiftyMove = 0;
    private int moves = 0;

    private List<String> FENList;

    static {
       generateFirstAttacks();
       generateSliderAttack(true);
       generateSliderAttack(false);
    }
    public Board(String FEN_String) {
        FENConverter(FEN_String);
    }
    public Board(List<String> FENList) {
        this.FENList = FENList;
        FENConverter(FENList.get(FENList.size() - 1));
    }

    private boolean noLegalMoves() {
        StringTokenizer tokenizer = new StringTokenizer(getMoves());
        while (tokenizer.hasMoreTokens()){
            String move = tokenizer.nextToken();
            if (makeMove(move)) {
                takeBack();
                return false;
            }
        }
        return true;
    }

    public boolean isCheckMate() {
        int kingSquare = (side==WHITE)? Utils.getLSB1Index(WK) : Utils.getLSB1Index(BK);
        return noLegalMoves() && isSquareAttacked(kingSquare, side);
    }

    public boolean isDraw() {
        return drawInsufficient() || drawRepetition() || drawStalemate() || (fiftyMove/2 == 50);
    }

    private boolean drawStalemate() {
        int kingSquare = (side==WHITE)? Utils.getLSB1Index(WK) : Utils.getLSB1Index(BK);
        return noLegalMoves() && !isSquareAttacked(kingSquare, side);
    }

    private boolean drawRepetition() {
        HashMap<String, Integer> map = new HashMap<>();
        for (String s : FENList) {
            map.put(s, map.getOrDefault(s, 0) + 1);
            if (map.getOrDefault(s, 0) == 3) {
                return true;
            }
        }
        return false;
    }

    private boolean drawInsufficient() {
        boolean kvsk = Utils.bitCount(BO) == 1 && Utils.bitCount(WO) == 1;
        boolean kvskb = (Utils.bitCount(BO) == 2 && Utils.bitCount(BB) == 1 && Utils.bitCount(WO) == 1) ||
                        (Utils.bitCount(WO) == 2 && Utils.bitCount(WB) == 1 && Utils.bitCount(BO) == 1);
        boolean kvskn = (Utils.bitCount(BO) == 2 && Utils.bitCount(BN) == 1 && Utils.bitCount(WO) == 1) ||
                        (Utils.bitCount(WO) == 2 && Utils.bitCount(WN) == 1 && Utils.bitCount(BO) == 1);
        return kvsk || kvskb || kvskn;
    }

    public String getFEN() {
        StringBuilder result = new StringBuilder();

        for (int i=0; i<8; i++) {
            int k=0;
            for (int j=0; j<8; j++) {
                if (((O >> i*8+j) & 1) != 1) {
                    k++;
                    continue;
                }
                if (k!=0) {
                    result.append(k);
                    k = 0;
                }
                if (((WP >> i*8+j) & 1) == 1) result.append('P');
                else if (((WN >> i*8+j) & 1) == 1) result.append('N');
                else if (((WB >> i*8+j) & 1) == 1) result.append('B');
                else if (((WR >> i*8+j) & 1) == 1) result.append('R');
                else if (((WQ >> i*8+j) & 1) == 1) result.append('Q');
                else if (((WK >> i*8+j) & 1) == 1) result.append('K');
                else if (((BP >> i*8+j) & 1) == 1) result.append('p');
                else if (((BN >> i*8+j) & 1) == 1) result.append('n');
                else if (((BB >> i*8+j) & 1) == 1) result.append('b');
                else if (((BR >> i*8+j) & 1) == 1) result.append('r');
                else if (((BQ >> i*8+j) & 1) == 1) result.append('q');
                else if (((BK >> i*8+j) & 1) == 1) result.append('k');
            }
            if (k!=0) result.append(k);
            if (i<7) result.append('/');
        }
        result.append((side==WHITE)? " w ":" b ");   // append side
        if ((castle & K) != 0) result.append("K");
        if ((castle & Q) != 0) result.append("Q");
        if ((castle & k) != 0) result.append("k");
        if ((castle & q) != 0) result.append("q");
        result.append((enPassant == NO_SQUARE)? " - ":" "+enPassant.name()+" ");   // append enPassant square
        result.append(fiftyMove).append(" ").append(moves);  // append 50 move rule and move counters

        return result.toString();
    }

    private static boolean verifyRank(String rank) {
        int count = 0;
        for (int i = 0; i < rank.length(); i++) {
            if (rank.charAt(i) >= '1' && rank.charAt(i) <= '8') {
                count += (rank.charAt(i) - '0');
            } else {
                count++;
            }
        }
        return count == 8;
    }
    private void FENConverter(String string) {
        if (!string.matches(FEN_Regex)) {
            throw new IllegalArgumentException("Invalid fen");
        }
        StringTokenizer stringTokenizer = new StringTokenizer(string, " ");
        String chessTable = stringTokenizer.nextToken();

        StringTokenizer tableTokenizer = new StringTokenizer(chessTable, "/");
        for (int i = 0; i < 8; i++) {
            String row = tableTokenizer.nextToken();
            if (!verifyRank(row)) throw new IllegalArgumentException("Invalid rank in FEN string");

            int index = 0;
            for (int j = 0; j < row.length(); j++) {
                if (Character.isDigit(row.charAt(j))) {
                    index += Integer.parseInt(row.substring(j, j + 1));
                }
                else {
                    int square = i*8+index;
                    switch (row.charAt(j)) {
                        // setting bit to corresponding bitboard in corresponding square to 1
                        case 'p' -> BP |= 1L <<  square;
                        case 'n' -> BN |= 1L <<  square;
                        case 'b' -> BB |= 1L <<  square;
                        case 'r' -> BR |= 1L <<  square;
                        case 'q' -> BQ |= 1L <<  square;
                        case 'k' -> BK |= 1L <<  square;
                        case 'P' -> WP |= 1L <<  square;
                        case 'N' -> WN |= 1L <<  square;
                        case 'B' -> WB |= 1L <<  square;
                        case 'R' -> WR |= 1L <<  square;
                        case 'K' -> WK |= 1L <<  square;
                        case 'Q' -> WQ |= 1L <<  square;
                    }
                    index++;
                }
            }
        }

        WO = WP | WN | WB | WR | WQ | WK;
        BO = BP | BN | BB | BR | BQ | BK;
        O = WO | BO;

//        piece.put('P', WP);piece.put('N', WN);piece.put('B', WB);piece.put('R', WR);piece.put('Q', WQ);piece.put('K', WK);
//        piece.put('p', BP);piece.put('n', BN);piece.put('b', BB);piece.put('r', BR);piece.put('q', BQ);piece.put('k', BK);
//

        side = stringTokenizer.nextToken().equals("w") ? WHITE : BLACK;
        String castlingRights = stringTokenizer.nextToken();
        for (Character c : castlingRights.toCharArray()) {
            switch (c) {
                case 'K' -> castle |= K;
                case 'Q' -> castle |= Q;
                case 'k' -> castle |= k;
                case 'q' -> castle |= q;
            }
        }
        String enPassantSquare = stringTokenizer.nextToken();
        enPassant = enPassantSquare.equals("-") ? NO_SQUARE : SQUARE.valueOf(enPassantSquare);

        fiftyMove = Integer.parseInt(stringTokenizer.nextToken());
        moves = Integer.parseInt(stringTokenizer.nextToken());
    }
    public Character[][] createBoard() {
        Character[][]  board = new Character[8][8];

        for (int i=0; i<64; i++) {
            if (((WP >> i) & 1) == 1) board[i/8][i%8] = 'P';
            else if (((WN >> i) & 1) == 1) board[i/8][i%8] = 'N';
            else if (((WB >> i) & 1) == 1) board[i/8][i%8] = 'B';
            else if (((WR >> i) & 1) == 1) board[i/8][i%8] = 'R';
            else if (((WQ >> i) & 1) == 1) board[i/8][i%8] = 'Q';
            else if (((WK >> i) & 1) == 1) board[i/8][i%8] = 'K';
            else if (((BP >> i) & 1) == 1) board[i/8][i%8] = 'p';
            else if (((BN >> i) & 1) == 1) board[i/8][i%8] = 'n';
            else if (((BB >> i) & 1) == 1) board[i/8][i%8] = 'b';
            else if (((BR >> i) & 1) == 1) board[i/8][i%8] = 'r';
            else if (((BQ >> i) & 1) == 1) board[i/8][i%8] = 'q';
            else if (((BK >> i) & 1) == 1) board[i/8][i%8] = 'k';
            else board[i/8][i%8] = ' ';
        }
        return board;
    }
    private static long generateDiagonalMask(int square) {
        long result = 0L;
        int r = square/8, f = square%8;
        for (int i = r-1, j = f-1; i > 0 && j > 0; i--, j--) result |= 1L << (i*8+j);
        for (int i = r-1, j = f+1; i > 0 && j < 7; i--, j++) result |= 1L << (i*8+j);
        for (int i = r+1, j = f-1; i < 7 && j > 0; i++, j--) result |= 1L << (i*8+j);
        for (int i = r+1, j = f+1; i < 7 && j < 7; i++, j++) result |= 1L << (i*8+j);
        return result;
    }
    private static long generateRayMask(int square) {
        long result = 0L;
        int r = square/8, f = square%8;

        for (int i = 1; i < 7; i++)
            if (i != r) result |= 1L << (i*8+f);
        for (int j = 1; j < 7; j++)
            if (j != f) result |= 1L << (r*8+j);

        return result;
    }

    public void setBit(char c, int square) {
        switch (c) {
            case 'K' -> WK |= (1L << square);
            case 'P' -> WP |= (1L << square);
            case 'N' -> WN |= (1L << square);
            case 'R' -> WR |= (1L << square);
            case 'B' -> WB |= (1L << square);
            case 'Q' -> WQ |= (1L << square);
            case 'k' -> BK |= (1L << square);
            case 'p' -> BP |= (1L << square);
            case 'n' -> BN |= (1L << square);
            case 'r' -> BR |= (1L << square);
            case 'b' -> BB |= (1L << square);
            case 'q' -> BQ |= (1L << square);
        }
    }
    public void popBit(char c, int square) {
        switch (c) {
            case 'K' -> WK ^= (1L << square);
            case 'P' -> WP ^= (1L << square);
            case 'N' -> WN ^= (1L << square);
            case 'R' -> WR ^= (1L << square);
            case 'B' -> WB ^= (1L << square);
            case 'Q' -> WQ ^= (1L << square);
            case 'k' -> BK ^= (1L << square);
            case 'p' -> BP ^= (1L << square);
            case 'n' -> BN ^= (1L << square);
            case 'r' -> BR ^= (1L << square);
            case 'b' -> BB ^= (1L << square);
            case 'q' -> BQ ^= (1L << square);
        }
    }
    public long getBit(char c, int square) {
        switch (c) {
            case 'K' -> { return WK & (1L << square); }
            case 'P' -> { return WP & (1L << square); }
            case 'N' -> { return WN & (1L << square); }
            case 'R' -> { return WR & (1L << square); }
            case 'B' -> { return WB & (1L << square); }
            case 'Q' -> { return WQ & (1L << square); }
            case 'k' -> { return BK & (1L << square); }
            case 'p' -> { return BP & (1L << square); }
            case 'n' -> { return BN & (1L << square); }
            case 'r' -> { return BR & (1L << square); }
            case 'b' -> { return BB & (1L << square); }
            case 'q' -> { return BQ & (1L << square); }
            default -> throw new IllegalArgumentException("Invalid character: "+c);
        }
    }
    public static long getBit(long bitboard, int square) {
        return bitboard & (1L << square);
    }

    public boolean makeMove(String move) {
        boolean increment = true;
        saveState();
        char piece = move.charAt(0);
        if (Character.isLowerCase(piece)!=(side==BLACK))
            throw new IllegalArgumentException("Invalid move: "+move+", it's "+
                                                (side==WHITE?"white":"black"+" turn"));
        if (Character.toLowerCase(piece)=='p') increment = false;
        char type = ' ';

        if (move.length() == 6) {
            type = move.charAt(move.length()-1);
            move = move.substring(0, move.length()-1);
        }

        int squareStart = SQUARE.valueOf(move.substring(1,3)).getIndex();
        int squareEnd = SQUARE.valueOf(move.substring(3)).getIndex();

        char[] enemyPieces = WHITE_PIECE;
        int enPassantSquare = 8;
        char rook = 'r';

        if (side == WHITE) {
            enemyPieces = BLACK_PIECE; enPassantSquare = -8; rook = 'R';
        }

        //handle movement
        popBit(piece, squareStart);
        setBit(piece, squareEnd);

        if (type != ' ') {
            // enPassant move
            if (type == 'p') popBit(enemyPieces[enemyPieces.length-1], squareEnd+enPassantSquare); //enemyPieces.length-1 è l'indice del pedone
            // castling move
            else if (type == 'c') {
                int rookEnding, rookStarting;

                switch (SQUARE.valueOf(SQUARE.getValue(squareEnd))) {
                    case g1 -> { rookEnding = f1.getIndex(); rookStarting = h1.getIndex(); }
                    case c1 -> { rookEnding = d1.getIndex(); rookStarting = a1.getIndex(); }
                    case g8 -> { rookEnding = f8.getIndex(); rookStarting = h8.getIndex(); }
                    case c8 -> { rookEnding = d8.getIndex(); rookStarting = a8.getIndex(); }
                    default -> throw new IllegalArgumentException("Invalid square: "+squareEnd);
                }

                popBit(rook, rookStarting);
                setBit(rook, rookEnding);
            }
            // promotion move
            else {
                popBit(piece, squareEnd);
                setBit(type, squareEnd);
            }
        }

        else
            //handle capture
            for (char c : enemyPieces)
                if (getBit(c, squareEnd) != 0) {
                    increment = false;
                    popBit(c, squareEnd);
                    break;
                }

        // update occupancies
        WO = WP | WN | WB | WR | WQ | WK;
        BO = BP | BN | BB | BR | BQ | BK;
        O = WO | BO;



        if (isSquareAttacked((side==WHITE)? Utils.getLSB1Index(WK) : Utils.getLSB1Index(BK), side)) {
            takeBack();
            return false;
        }

        else {
            // update castling rights
            castle &= castlingRights[squareStart];
            castle &= castlingRights[squareEnd];

            // change side
            side ^= 1;

            // increment moves
            if (side == WHITE)
                moves++;

            // increment ply for fifty move rule
            if (increment) fiftyMove++;
            else fiftyMove = 0;

        }
        return true;
    }

    private long[][] state = new long[2][6];

    private void saveState() {
        state[WHITE] = new long[] {WN, WB, WR, WQ, WK, WP}; // knight, bishop, rook, queen, king, pawn;
        state[BLACK] = new long[] {BN, BB, BR, BQ, BK, BP};
    }
    private void takeBack() {
        WN=state[WHITE][0]; WB=state[WHITE][1]; WR=state[WHITE][2]; WQ=state[WHITE][3]; WK=state[WHITE][4]; WP=state[WHITE][5];
        BN=state[BLACK][0]; BB=state[BLACK][1]; BR=state[BLACK][2]; BQ=state[BLACK][3]; BK=state[BLACK][4]; BP=state[BLACK][5];
        // update occupancies
        WO = WP | WN | WB | WR | WQ | WK;
        BO = BP | BN | BB | BR | BQ | BK;
        O = WO | BO;
    }

    private static void generateSliderAttack(boolean isBishop) {
        long mask; long[] pieceMagicNumber; int[] relevantBit;

        for (int square=0; square<64; square++) {
            if (isBishop) {
                BISHOP_MASK[square] = generateDiagonalMask(square);
                mask = BISHOP_MASK[square];
                pieceMagicNumber = BISHOP_MAGIC_NUMBERS;
                relevantBit = BISHOP_RELEVANT_BITS;
            }
            else {
                ROOK_MASK[square] = generateRayMask(square);
                mask = ROOK_MASK[square];
                pieceMagicNumber = ROOK_MAGIC_NUMBERS;
                relevantBit = ROOK_RELEVANT_BITS;
            }
            int relevantBitCount = Long.bitCount(mask);
            int occupancyIndices = (1 << relevantBitCount);

            for (int i = 0; i< occupancyIndices; i++) {
                long occupancy = Utils.setOccupancy(i, relevantBitCount, mask);
                int magicIndex = (int)((occupancy * pieceMagicNumber[square]) >>> (64 - relevantBit[square]));
                if (isBishop) BISHOP_ATTACK[square][magicIndex] = Utils.generateDiagonalAttack(square, occupancy);
                else ROOK_ATTACK[square][magicIndex] = Utils.generateRayAttack(square, occupancy);
            }
        }
    }
    private static void generateFirstAttacks() {
        for (int i=0; i<64; i++) {
            long bb = 1L << i;
            PAWN_ATTACK[BLACK][i] = (bb << 7 & NOT_H_FILE) | (bb << 9 & NOT_A_FILE);
            PAWN_ATTACK[WHITE][i] = (bb >>> 7 & NOT_A_FILE) | (bb >>> 9 & NOT_H_FILE);
            KNIGHT_ATTACK[i] = (bb << 6 & NOT_HG_FILE) | (bb << 10 & NOT_AB_FILE) | (bb << 15 & NOT_H_FILE) | (bb << 17 & NOT_A_FILE) |
                               (bb >>> 6 & NOT_AB_FILE) | (bb >>> 10 & NOT_HG_FILE) | (bb >>> 15 & NOT_A_FILE) | (bb >>> 17 & NOT_H_FILE);
            KING_ATTACK[i] = (bb >>> 9 & NOT_H_FILE) | (bb >>> 8) | (bb >>> 7 & NOT_A_FILE) |
                             (bb >>> 1 & NOT_H_FILE) | (bb << 1 & NOT_A_FILE)               |
                             (bb << 9 & NOT_A_FILE) | (bb << 8) | (bb << 7 & NOT_H_FILE);
            PAWN_MOVEMENT[BLACK][i] = (bb << 8);
            PAWN_MOVEMENT[WHITE][i] = (bb >>> 8);
        }
    }
    public static long getBishopAttacks(int square, long occupancy) {
        occupancy &= BISHOP_MASK[square];
        occupancy *= BISHOP_MAGIC_NUMBERS[square];
        occupancy >>>= (64 - BISHOP_RELEVANT_BITS[square]);
        return BISHOP_ATTACK[square][(int) occupancy];
    }
    public static long getRookAttacks(int square, long occupancy) {
        occupancy &= ROOK_MASK[square];
        occupancy *= ROOK_MAGIC_NUMBERS[square];
        occupancy >>>= (64 - ROOK_RELEVANT_BITS[square]);
        return ROOK_ATTACK[square][(int)occupancy];
    }
    public static long getQueenAttacks(int square, long occupancy) {
        long bishopOcc = occupancy;
        long rookOcc = occupancy;

        bishopOcc &= BISHOP_MASK[square];
        bishopOcc *= BISHOP_MAGIC_NUMBERS[square];
        bishopOcc >>>= (64 - BISHOP_RELEVANT_BITS[square]);

        rookOcc &= ROOK_MASK[square];
        rookOcc *= ROOK_MAGIC_NUMBERS[square];
        rookOcc >>>= (64 - ROOK_RELEVANT_BITS[square]);

        return BISHOP_ATTACK[square][(int)bishopOcc] | ROOK_ATTACK[square][(int)rookOcc];
    }
    public boolean isSquareAttacked(int square, int mySide) {
        long pawnBB, knightBB, kingBB, bishopBB, rookBB, queenBB;
        if (mySide == WHITE) {
            pawnBB = BP; knightBB = BN; kingBB = BK; bishopBB = BB; rookBB = BR; queenBB = BQ;
        }
        else {
            pawnBB = WP; knightBB = WN; kingBB = WK; bishopBB = WB; rookBB = WR; queenBB = WQ;
        }
        if ((PAWN_ATTACK[mySide][square] & pawnBB) != 0) return true;
        if ((KNIGHT_ATTACK[square] & knightBB) != 0) return true;
        if ((KING_ATTACK[square] & kingBB) != 0) return true;
        if ((getBishopAttacks(square, O) & bishopBB)!= 0 ) return true;
        if ((getRookAttacks(square, O) & rookBB) != 0 ) return true;
        return (getQueenAttacks(square, O) & queenBB) != 0;
    }

    private String getPawnMoves() {
        long bb, opponentOccupancy;
        String pB, pN, pR, pQ, pawn;
        if (side == WHITE) {
            bb = WP; opponentOccupancy = BO; pB = "B "; pN = "N "; pR = "R "; pQ = "Q "; pawn = "P";
        }
        else {
            bb = BP; opponentOccupancy = WO; pB = "b "; pN = "n "; pR = "r "; pQ = "q "; pawn = "p";
        }
        StringBuilder sb = new StringBuilder();

        while (bb!=0) {
            int square = Utils.getLSB1Index(bb);
            String squareString = SQUARE.getValue(square);

            long moves = (PAWN_ATTACK[side][square] & opponentOccupancy) | (PAWN_MOVEMENT[side][square] & ~O);
            //check for double push
            if (side == WHITE && square/8 == 6) moves |= (PAWN_MOVEMENT[side][square] >>> 8) & (~O >>> 8) & ~O;
            else if (side == BLACK && square/8 == 1) moves |= (PAWN_MOVEMENT[side][square] << 8) & ((~O << 8)) & ~O;

            //check for enPassant
            if (enPassant != NO_SQUARE && (PAWN_ATTACK[side][square] & (1L << enPassant.getIndex())) != 0)
                sb.append(squareString).append(enPassant.name()).append("p ");

            while (moves != 0) {
                int squareToGo = Utils.getLSB1Index(moves);
                String squareToGoString = SQUARE.getValue(squareToGo);

                //check for promotion
                if (squareToGo/8 == 7*side) {
                    sb.append(pawn).append(squareString).append(squareToGoString).append(pB).append(" ");
                    sb.append(pawn).append(squareString).append(squareToGoString).append(pN).append(" ");
                    sb.append(pawn).append(squareString).append(squareToGoString).append(pR).append(" ");
                    sb.append(pawn).append(squareString).append(squareToGoString).append(pQ).append(" ");
                }

                else sb.append(pawn).append(squareString).append(squareToGoString).append(" ");


                moves ^= (1L << squareToGo);
            }

            bb ^= (1L << square);
        }
        return sb.toString();
    }
    private String getKingMoves() {
        int kingsideC, queensideC, kingsideFirst, kingsideSecond, queensideFirst, queensideSecond, queensideThird;
        String kingSquare, king;

        long bb, myOccupancy;

        if (side == WHITE) {
            king = "K"; kingsideC = K; queensideC = Q;
            kingsideFirst = f1.getIndex(); kingsideSecond = g1.getIndex();
            queensideFirst = d1.getIndex(); queensideSecond = c1.getIndex(); queensideThird = b1.getIndex();
            kingSquare = e1.name();

            bb = WK; myOccupancy = WO;
        }
        else {
            king = "k"; kingsideC = k; queensideC = q;
            kingsideFirst = f8.getIndex(); kingsideSecond = g8.getIndex();
            queensideFirst = d8.getIndex(); queensideSecond = c8.getIndex(); queensideThird = b8.getIndex();
            kingSquare = e8.name();

            bb = BK; myOccupancy = BO;
        }
        StringBuilder sb = new StringBuilder();

        if (((castle & kingsideC) != 0)                    &&
                (O & (1L << kingsideFirst)) == 0               &&
                (O & (1L << kingsideSecond)) == 0              &&
                !isSquareAttacked(kingsideFirst, side)         &&
                !isSquareAttacked(kingsideSecond, side))
            sb.append(king).append(kingSquare).append(SQUARE.getValue(kingsideSecond)).append("c ");

        if (((castle & queensideC) != 0)                    &&
                (O & (1L << queensideFirst)) == 0               &&
                (O & (1L << queensideSecond)) == 0              &&
                (O & (1L << queensideThird)) == 0               &&
                !isSquareAttacked(queensideFirst, side)         &&
                !isSquareAttacked(queensideSecond, side))
            sb.append(king).append(kingSquare).append(SQUARE.getValue(queensideSecond)).append("c ");


        int piece = Utils.getLSB1Index(bb);

        long moves = KING_ATTACK[piece] & ~myOccupancy;
        while (moves != 0) {
            int squareToGo = Utils.getLSB1Index(moves);

            String squareToGoString = SQUARE.getValue(squareToGo);
            sb.append(king).append(SQUARE.getValue(piece)).append(squareToGoString).append(" ");
            moves ^= (1L << squareToGo);
        }

        return sb.toString();
    }
    private String getKnightMoves() {
        long bb, myOccupancy;
        String knight;
        if (side == WHITE) {
            bb = WN; myOccupancy = WO; knight = "N";
        }
        else {
            bb = BN; myOccupancy = BO; knight = "n";
        }
        StringBuilder sb = new StringBuilder();

        while (bb != 0) {
            int piece = Utils.getLSB1Index(bb);

            long moves = KNIGHT_ATTACK[piece] & ~myOccupancy;
            while (moves != 0) {
                int squareToGo = Utils.getLSB1Index(moves);

                String squareToGoString = SQUARE.getValue(squareToGo);
                sb.append(knight).append(SQUARE.getValue(piece)).append(squareToGoString).append(" ");
                moves ^= (1L << squareToGo);
            }
            bb ^= (1L << piece);
        }

        return sb.toString();
    }
    private String getBishopMoves() {
        long bb, myOccupancy;
        String bishop;

        if (side == WHITE) {
            bb = WB; myOccupancy = WO; bishop = "B";
        }
        else {
            bb = BB; myOccupancy = BO; bishop = "b";
        }
        StringBuilder sb = new StringBuilder();

        while (bb != 0) {
            int piece = Utils.getLSB1Index(bb);
            long moves = getBishopAttacks(piece, O) & ~myOccupancy;
            while (moves != 0) {
                int squareToGo = Utils.getLSB1Index(moves);

                String squareToGoString = SQUARE.getValue(squareToGo);
                sb.append(bishop).append(SQUARE.getValue(piece)).append(squareToGoString).append(" ");
                moves ^= (1L << squareToGo);
            }
            bb ^= (1L << piece);
        }

        return sb.toString();
    }
    private String getRookMoves() {
        long bb, myOccupancy;
        String rook;

        if (side == WHITE) {
            bb = WR; myOccupancy = WO; rook = "R";
        }
        else {
            bb = BR; myOccupancy = BO; rook = "r";
        }
        StringBuilder sb = new StringBuilder();
        while (bb != 0) {
            int piece = Utils.getLSB1Index(bb);
            long moves = getRookAttacks(piece, O) & ~myOccupancy;

            while (moves != 0) {
                int squareToGo = Utils.getLSB1Index(moves);

                String squareToGoString = SQUARE.getValue(squareToGo);
                sb.append(rook).append(SQUARE.getValue(piece)).append(squareToGoString).append(" ");
                moves ^= (1L << squareToGo);
            }
            bb ^= (1L << piece);
        }

        return sb.toString();
    }
    private String getQueenMoves() {
        long bb, myOccupancy;
        String queen;

        if (side == WHITE) {
            bb = WQ; myOccupancy = WO; queen = "Q";
        }
        else {
            bb = BQ; myOccupancy = BO; queen = "q";
        }
        StringBuilder sb = new StringBuilder();
        while (bb != 0) {
            int piece = Utils.getLSB1Index(bb);

            long moves = getQueenAttacks(piece, O) & ~myOccupancy;
            while (moves != 0) {
                int squareToGo = Utils.getLSB1Index(moves);

                String squareToGoString = SQUARE.getValue(squareToGo);
                sb.append(queen).append(SQUARE.getValue(piece)).append(squareToGoString).append(" ");
                moves ^= (1L << squareToGo);
            }
            bb ^= (1L << piece);
        }

        return sb.toString();
    }

    //pseudo-legal moves
    public String getMoves() {
        StringBuilder sb = new StringBuilder();
        //pawn moves
        sb.append(getPawnMoves());
        //king moves
        sb.append(getKingMoves());
        //knight moves
        sb.append(getKnightMoves());
        //bishop moves
        sb.append(getBishopMoves());
        //rook moves
        sb.append(getRookMoves());
        //queen moves
        sb.append(getQueenMoves());
        return sb.toString();
    }

    public final static String CUSTOM = "r3kbnr/ppp1pppp/8/8/3pP3/P4N2/1PPP1PPP/RNBQKB1R b KQkq e3 1 2";

//    public static void main(String[] args) {
//        Board b = new Board(CUSTOM);
//
//        String moves = b.getMoves();
//        Character[][] board = b.createBoard();
//        for (Character[] characters : board) {
//            System.out.println(Arrays.toString(characters));
//        }
//        StringTokenizer tokenizer = new StringTokenizer(moves);
//        while (tokenizer.hasMoreTokens()) {
//            System.out.println(tokenizer.nextToken());
//        }
//
//        Board b = new Board(CUSTOM);
//
//        System.out.println(new StringTokenizer(CUSTOM).nextToken());
//        System.out.println(b.getFEN());
//
//    }

    public boolean isEmpty(int square) {
        return (O & (1L << square)) != 0;
    }

    public String getMoves(int square) {
        String moves = getMoves();
        StringTokenizer tokenizer = new StringTokenizer(moves);
        String squareString = SQUARE.getValue(square);
        StringBuilder sb = new StringBuilder();
        while (tokenizer.hasMoreTokens()) {
            String move = tokenizer.nextToken();
            if (move.startsWith(squareString)) {
                sb.append(move);
            }
        }
        return sb.toString();
    }
}