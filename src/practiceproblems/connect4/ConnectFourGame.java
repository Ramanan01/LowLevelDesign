package src.practiceproblems.connect4;

import java.util.Scanner;

enum DiscColour {
    RED,
    YELLOW;
}

enum GameState {
    IN_PROGRESS,
    WIN,
    DRAW
}

class Player {
    String name;
    DiscColour colour;

    public Player(DiscColour colour, String name) {
        this.colour = colour;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public DiscColour getColour() {
        return colour;
    }
}

class Board {
    private static final int ROWS = 6;
    private static final int COLS = 7;

    private final DiscColour[][] board;
    private int placedCount = 0;

    public Board() {
        board = new DiscColour[ROWS][COLS];
    }

    public boolean validColumn(int col) {
        if(col<0 || col>=COLS) {
            return false;
        }

        return board[0][col] == null;
    }

    public int placeDisc(DiscColour colour, int col) {
        if(!validColumn(col)) {
            return -1;
        }

        for(int i=ROWS-1;i>=0;i--) {
            if(board[i][col] == null) {
                board[i][col] = colour;
                placedCount++;
                return i;
            }
        }

        return -1;
    }

    public boolean checkWin(int row, int col, DiscColour colour) {
        int[] r = {1, 1, 1, 0};
        int[] c = {-1, 1, 0, 1};
        int count;
        for(int i=0;i<4;i++) {
           int dr = r[i];
           int dc = c[i];
           count = countInDirection(row, col, dr, dc, colour) + countInDirection(row, col, -dr, -dc, colour) - 1;
           if(count >= 4) {
               return true;
           }
        }

        return false;
    }

    private int countInDirection(int row, int col, int dr, int dc , DiscColour colour) {
        int count = 0;
        int i=row, j=col;
        while(i<ROWS && i>=0 && j<COLS && j>=0) {
            if(board[i][j] == colour){
                count++;
                i+=dr;
                j+=dc;
            }
            else{
                break;
            }
        }

        return count;
    }

    public boolean isFull() {
        return placedCount == (ROWS * COLS);
    }

    public void printBoard() {
        for(int i=0;i<ROWS;i++) {
            for(int j=0;j<COLS;j++) {
                if(board[i][j]==null)
                    System.out.print(". ");
                else if(board[i][j]==DiscColour.RED)
                    System.out.print("R ");
                else
                    System.out.print("Y ");
            }
            System.out.println();
        }
    }
}


class Game {
    private GameState gameState;
    private Player player1;
    private Player player2;
    private Player winner;
    private Player currentPlayer;
    private Board board;

    Game() {
        player1 = new Player(DiscColour.RED, "Pavan");
        player2 = new Player(DiscColour.YELLOW, "Ramanan");
        currentPlayer = player1;
        gameState = GameState.IN_PROGRESS;
        board = new Board();
    }

    public void placeDisc(Player player, int col) {
        if(player != currentPlayer) {
            throw new IllegalArgumentException("Player out of turn");
        }

        if(gameState != GameState.IN_PROGRESS) {
            throw new IllegalArgumentException("Game has ended");
        }

        int row = board.placeDisc(currentPlayer.getColour(), col);
        if(row==-1) {
            throw new IllegalArgumentException("Cannot place disc in this column");
        }

        if(board.checkWin(row, col, currentPlayer.getColour())) {
            gameState = GameState.WIN;
            winner = currentPlayer;
            System.out.println("Game has ended. Player " + winner.getName() + " has won");
        }
        else if(board.isFull()) {
            gameState = GameState.DRAW;
            System.out.println("Game has ended. Match is a draw");
        }
        else{
            currentPlayer = currentPlayer==player1 ? player2 : player1;
        }

        board.printBoard();
    }


    public GameState getGameState() {
        return gameState;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}


public class ConnectFourGame {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Game game = new Game();

        System.out.println("====== CONNECT FOUR ======");

        while(game.getGameState() == GameState.IN_PROGRESS) {

            Player currentPlayer = game.getCurrentPlayer();

            System.out.println(
                    "\nCurrent Player: "
                            + currentPlayer.getName()
                            + " ("
                            + currentPlayer.getColour()
                            + ")"
            );

            System.out.print(
                    "Enter column (0-6): "
            );

            try {
                int col = sc.nextInt();

                game.placeDisc(currentPlayer, col);

            } catch(IllegalArgumentException e) {
                System.out.println(
                        "Error: " + e.getMessage()
                );
            } catch(Exception e) {
                System.out.println(
                        "Invalid input. Enter a number."
                );

                sc.nextLine(); // clear invalid input
            }
        }

        sc.close();
    }
}