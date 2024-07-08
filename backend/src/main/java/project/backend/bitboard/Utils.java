package project.backend.bitboard;

public final class Utils {

    enum SQUARE {
        a8(0), b8(1), c8(2), d8(3), e8(4), f8(5), g8(6), h8(7),
        a7(8), b7(9), c7(10), d7(11), e7(12), f7(13), g7(14), h7(15),
        a6(16), b6(17), c6(18), d6(19), e6(20), f6(21), g6(22), h6(23),
        a5(24), b5(25), c5(26), d5(27), e5(28), f5(29), g5(30), h5(31),
        a4(32), b4(33), c4(34), d4(35), e4(36), f4(37), g4(38), h4(39),
        a3(40), b3(41), c3(42), d3(43), e3(44), f3(45), g3(46), h3(47),
        a2(48), b2(49), c2(50), d2(51), e2(52), f2(53), g2(54), h2(55),
        a1(56), b1(57), c1(58), d1(59), e1(60), f1(61), g1(62), h1(63), NO_SQUARE(-1);

        private final int index;
        SQUARE(int index) {
            this.index = index;
        }
        public int getIndex() {
            return index;
        }
        public static String getValue(int index) {
            for (SQUARE s : SQUARE.values()) {
                if (s.getIndex() == index) {
                    return s.name();
                }
            }
            return null;
        }
    }


    public static void printBitBoard(long bitboard) {
        String files = "       a  b  c  d  e  f  g  h";

        StringBuilder stringBuilder = new StringBuilder(100);
        for (int i=0; i<8; i++) {
            stringBuilder.append((8 - i)).append("      ");
            for (int j=0; j<8; j++) {
                stringBuilder.append(((bitboard >> i * 8 + j) & 1)).append("  ");
            }
            stringBuilder.append("\n");
        }
        stringBuilder.append("\n")
                .append(files)
                .append("\n\nBitboard: ")
                .append(bitboard)
                .append("\n");
        System.out.println(stringBuilder);
    }

    public static int bitCount(long bitboard) {
        int count = 0;
        while (bitboard != 0) {
            count++;
            bitboard &= bitboard - 1;
        }
        return count;
    }

    public static int getLSB1Index(long bitboard) {
        if (bitboard == 0L) return -1;
        return bitCount((bitboard & -bitboard)-1);
    }

    public static long setOccupancy(int index, int numOf1Bits, long attackMask) {
        long occupancy = 0L;
        for (int i=0; i<numOf1Bits; i++) {
            int square = getLSB1Index(attackMask);
            attackMask ^= (1L << square);
            //System.out.println("attackMask:\n");
            //printBitBoard(attackMask);

            if ((index & (1L << i))!=0)
                occupancy |= (1L << square);
            //System.out.println("occupancy:\n");
        }
        return occupancy;
    }

    public static long generateDiagonalAttack(int square, long occupancies) {
        long result = 0L;
        int r = square/8, f = square%8;
        for (int i = r-1, j = f-1; i >= 0 && j >= 0; i--, j--) {
            result |= 1L << (i*8+j);
            if (((1L << (i*8+j) & occupancies)) != 0) break;
        }
        for (int i = r-1, j = f+1; i >= 0 && j <= 7; i--, j++) {
            result |= 1L << (i*8+j);
            if (((1L << (i*8+j) & occupancies)) != 0) break;
        }
        for (int i = r+1, j = f-1; i <= 7 && j >= 0; i++, j--) {
            result |= 1L << (i*8+j);
            if (((1L << (i*8+j) & occupancies)) != 0) break;
        }
        for (int i = r+1, j = f+1; i <= 7 && j <= 7; i++, j++) {
            result |= 1L << (i*8+j);
            if (((1L << (i*8+j) & occupancies)) !=0) break;
        }
        return result;
    }
    public static long generateRayAttack(int square, long occupancies) {
        long result = 0L;
        int r = square/8, f = square%8;

        for (int i = r-1; i >=0; i--) {
            result |= 1L << (i*8+f);
            if (((1L << (i*8+f) & occupancies)) != 0) break;
        }
        for (int i = r+1; i <=7; i++) {
            result |= 1L << (i*8+f);
            if (((1L << (i*8+f) & occupancies)) != 0) break;
        }
        for (int j = f-1; j >= 0; j--) {
            result |= 1L << (r*8+j);
            if (((1L << (r*8+j) & occupancies)) != 0) break;
        }
        for (int j = f+1; j <= 7; j++) {
            result |= 1L << (r*8+j);
            if (((1L << (r*8+j) & occupancies)) != 0) break;
        }

        return result;
    }
}
