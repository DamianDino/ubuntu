// skel PA 2017

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Clasa cu 2 membri de orice tip
 * Echivalent cu std::pair din C++
 */
class Pair<F, S> {
    public F first;
    public S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }
}

/**
 * Reprezinta o mutare efectuata de un jucator
 */
class Move {
    public int player, x, y;

    public Move(int player, int x, int y) {
        this.player = player; /* Jucatorul care face mutarea */
        this.x = x; /* Linia */
        this.y = y; /* Coloana */
    }

    public Move(int player) {
        this(player, -1, -1);
    }
}

/**
 * Reprezinta starea jocului
 */
class Reversi {
    public static int Inf = 123456789;
    public static int N = 6; /* Pastrati N par, N >= 4 */

    private int data[][];

    public Reversi() {
        data = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                data[i][j] = 0;
        data[N / 2 - 1][N / 2 - 1] = data[N / 2][N / 2] = 1;
        data[N / 2 - 1][N / 2] = data[N / 2][N / 2 - 1] = -1;
    }

    /**
     * Intoarce true daca jocul este intr-o stare finala
     */
    public boolean ended() {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                Reversi tmp = (Reversi) this.clone();
                if (tmp.apply_move(new Move(1, i, j)))
                    return false;
                tmp = (Reversi) this.clone();
                if (tmp.apply_move(new Move(-1, i, j)))
                    return false;
            }
        return true;
    }

    /**
     * Returns the list of all possible moves
     */
    public List<Move> generateMoves(int player) {
        List<Move> allMoves = new ArrayList<>();
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                Reversi tmp = (Reversi) this.clone();
                Move currentMove = new Move(player, i, j);
                if (tmp.apply_move(currentMove)) {
                    allMoves.add(currentMove);
                }
            }

        return allMoves;
    }

    /**
     * Returns 1 if player won, 0 if draw and -1 if lost
     */
    int winner(int player) {
        if (!ended())
            return 0;
        int s = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                if (data[i][j] == player)
                    s++;
                else if (data[i][j] == -player)
                    s--;
            }
        return Integer.compare(s, 0);
    }

    /**
     * Functia de evaluare a starii curente a jocului
     * Evaluarea se face din perspectiva jucatorului
     * aflat curent la mutare (player)
     */
//    public int eval(int player) {
//        /**
//         * Aceasta trebuie sa intoarca:
//         * Inf daca jocul este terminat in favoarea lui player
//         * -Inf daca jocul este terminat in defavoarea lui player
//         *
//         * In celelalte cazuri ar trebui sa intoarca un scor cu atat
//         * mai mare, cu cat player ar avea o sansa mai mare de castig
//         */
//        int score;
//
//        if (ended()) {
//            switch (winner(player)) {
//                case 1:
//                    return Inf;
//                case -1:
//                    return -Inf;
//                case 0:
//                    return 0;
//            }
//        }
//
//        Random random = new Random();
//        return random.nextInt(2001) - 1000;
//    }

    public int eval(int player) {
        if (ended()) {
            if (winner(player) == 1) {
                return Inf;
            } else if (winner(player) == -1) {
                return -Inf;
            }
        }

        int points = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (data[i][j] == player) {
                    points += 1;
                }
            }
        }
        return points;

    }

    /**
     * Aplica o mutare a jucatorului, modificand starea jocului
     * Plaseaza piesa jucatorului move.player in pozitia move.x, move.y
     * Mutarea move.x == -1, move.y == -1 semnifica ca jucatorul
     * paseaza randul la mutare
     * Returneaza true daca mutarea este valida
     */
    public boolean apply_move(Move move) {
        if (move.x == -1 && move.y == -1)
            return true;

        if (data[move.x][move.y] != 0)
            return false;

        boolean ok = false;

        for (int x_dir = -1; x_dir <= 1; x_dir++)
            for (int y_dir = -1; y_dir <= 1; y_dir++) {
                if (x_dir == 0 && y_dir == 0)
                    continue;
                int i = move.x + x_dir, j = move.y + y_dir;
                for (; i >= 0 && j >= 0 && i < N && j < N && data[i][j] == -move.player; i += x_dir, j += y_dir) ;
                if (i >= 0 && j >= 0 && i < N && j < N && data[i][j] == move.player && (P2.abs(move.x - i) > 1 || P2.abs(move.y - j) > 1)) {
                    ok = true;
                    for (i = move.x + x_dir, j = move.y + y_dir; i >= 0 && j >= 0 && i < N && j < N && data[i][j] == -move.player; i += x_dir, j += y_dir)
                        data[i][j] = move.player;
                }
            }

        if (!ok)
            return false;

        data[move.x][move.y] = move.player;

        return true;
    }

    /**
     * Afiseaza starea jocului
     */
    public String toString() {
        String ret = "  ";
        for (int i = 0; i < N; i++) {
            ret += i + " ";
        }
        ret += "\n";

        for (int i = 0; i < N; i++) {
            ret += i + " ";
            for (int j = 0; j < N; j++) {
                if (data[i][j] == 0)
                    ret += ".";
                else if (data[i][j] == 1)
                    ret += "O";
                else
                    ret += "X";
                ret += " ";
            }
            ret += "\n";
        }
        ret += "\n";

        return ret;
    }

    /**
     * Intoarce o copie a starii de joc
     */
    public Object clone() {
        Reversi ret = new Reversi();
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                ret.data[i][j] = data[i][j];
        return ret;
    }
}

class P2 {

    /**
     * nextPlayer
     */
    static int nextPlayer(int player) {
        return -player;
    }

    static int abs(int x) {
        return x > 0 ? x : -x;
    }

    static Pair<Integer, Move> minimax(Reversi init, int player, int depth) {

        // Stop case
        if (init.ended() || depth == 0) {
            int score = init.eval(player);
            return new Pair<Integer, Move>(score, new Move(player));
        } else {
            List<Move> movesList = init.generateMoves(player);

            // The player can't move
            if (movesList.isEmpty()) {
                int score = init.eval(player);
                return new Pair<Integer, Move>(score, new Move(player));
            } else {
                int bestScore = Integer.MIN_VALUE;
                Move bestMove = new Move(player);

                for (Move move : movesList) {
                    Reversi tmp = (Reversi) init.clone();
                    tmp.apply_move(move);
                    Pair<Integer, Move> result = minimax(tmp,
                            nextPlayer(player),
                            depth - 1);

                    int currentScore = -result.first;
                    if (currentScore > bestScore) {
                        bestScore = currentScore;
                        bestMove = move;
                    }
                }

                return new Pair<Integer, Move>(bestScore, bestMove);
            }
        }
    }

    static Pair<Integer, Move> minimax_abeta(Reversi init, int player, int depth, int alpha, int beta) {
        // Stop case
        if (init.ended() || depth == 0) {
            int score = init.eval(player);
            return new Pair<Integer, Move>(score, new Move(player));
        } else {
            List<Move> movesList = init.generateMoves(player);

            // The player can't move
            if (movesList.isEmpty()) {
                int score = init.eval(player);
                return new Pair<Integer, Move>(score, new Move(player));
            } else {
                Move bestMove = new Move(player);

                for (Move move : movesList) {
                    Reversi tmp = (Reversi) init.clone();
                    tmp.apply_move(move);
                    Pair<Integer, Move> result = minimax_abeta(tmp,
                            nextPlayer(player),
                            depth - 1,
                            -beta, -alpha);

                    int currentScore = -result.first;

                    if (currentScore > alpha) {
                        alpha = currentScore;
                        bestMove = move;
                    }

                    if (alpha >= beta) {
                        break;               // cut-off
                    }
                }

                return new Pair<Integer, Move>(alpha, bestMove);
            }
        }
    }

    public static void main(String[] args) {
        Reversi rev = new Reversi();
        System.out.print(rev);

        /* Choose here if you want COMP vs HUMAN or COMP vs COMP */
        boolean ALPHA_BETA_PRUNING = true;
        boolean HUMAN_PLAYER = false;
        int player = 1;

        while (!rev.ended()) {
            System.out.println("Player1 possible moves:");
            for(Move move : rev.generateMoves(1)) {
                System.out.print("(" + move.x + " " + move.y + ") ");
            }
            System.out.println();

            System.out.println("Player2 possible moves:");
            for(Move move : rev.generateMoves(-1)) {
                System.out.print("(" + move.x + " " + move.y + ") ");
            }
            System.out.println();

            Pair<Integer, Move> p;
            if (player == 1) {
                if(!ALPHA_BETA_PRUNING)
                    p = minimax(rev, player, 6);
                else
                    p = minimax_abeta(rev, player, 6, -Reversi.Inf, Reversi.Inf);

                System.out.println("Move Player1: (" + p.second.x + " " + p.second.y + ")");

                System.out.println("Bot 1" + " evaluates to " + p.first);
                rev.apply_move(p.second);
            } else {
                if (!HUMAN_PLAYER) {
                    if(ALPHA_BETA_PRUNING)
                        p = minimax(rev, player, 6);
                    else
                        p = minimax_abeta(rev, player, 6, -Reversi.Inf, Reversi.Inf);
                    System.out.println("Move Player2: (" + p.second.x + " " + p.second.y + ")");

                    System.out.println("Bot 2" + " evaluates to " + p.first);
                    rev.apply_move(p.second);
                } else {
                    boolean valid = false;
                    while (!valid) {
                        Scanner keyboard = new Scanner(System.in);
                        System.out.print("Insert position [0..N - 1], [0..N - 1] ");

                        int x = keyboard.nextInt();
                        int y = keyboard.nextInt();

                        valid = rev.apply_move(new Move(player, x, y));
                    }
                }
            }

            System.out.print(rev);
            player *= -1;
        }

        int w = rev.winner(1);
        if (w == 1)
//            System.out.println("Player 1 WON!");
            System.out.println("Player1 won!");
        else if (w == 0)
            System.out.println("DRAW!");
        else
            System.out.println("Player2 won!");
//            System.out.println("Player 2 WON!");
    }
}
