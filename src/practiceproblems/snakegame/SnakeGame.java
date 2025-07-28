package src.practiceproblems.snakegame;

import java.util.*;

public class SnakeGame {
    public static void main(String[] args){
        Game game = new Game(10, 10);
        game.start();
    }
}

enum Direction{
    LEFT, RIGHT, DOWN, UP
}

enum GameState{
    RUNNING, OVER
}

class Position{
    int row, col;
    Position(int row, int col){
        this.row = row;
        this.col= col;
    }

    public Position move(Direction d){
        return switch (d) {
            case Direction.LEFT -> new Position(row - 1, col);
            case Direction.RIGHT -> new Position(row + 1, col);
            case Direction.UP -> new Position(row, col - 1);
            case Direction.DOWN -> new Position(row, col + 1);
        };
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Position other)) return false;
        return other.row == row && other.col == col;
    }

    @Override
    public int hashCode(){
        return Objects.hash(row, col);
    }
}

class Snake{
    Deque<Position> body;
    Direction currentDirection;

    Snake(Position start){
        body = new LinkedList<>();
        body.addFirst(start);
        currentDirection = Direction.RIGHT;
    }

    public void changeDirection(Direction direction){
        if((direction==Direction.UP && currentDirection==Direction.DOWN) ||
            (direction==Direction.DOWN && currentDirection==Direction.UP) ||
            (direction==Direction.LEFT && currentDirection==Direction.RIGHT) ||
            (direction==Direction.RIGHT && currentDirection==Direction.LEFT)){
            return;
        }
        currentDirection = direction;
    }

    public void move(Position position, boolean grow){
        body.addFirst(position);
        if(!grow) {
            body.removeLast();
        }
    }

    public Position getHead(){
        return body.peekFirst();
    }

    public List<Position> getBody(){
        return new ArrayList<>(body);
    }

    public boolean hasCollided(Position newPos){
        return body.contains(newPos);
    }
}

class Board{
    int rows, cols;
    Position food;
    Random random;

    public Board(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        random = new Random();
    }

    public boolean isValid(Position pos){
        return (pos.row>=0 && pos.row<rows && pos.col>=0 && pos.col<cols);
    }

    public void placeFood(Set<Position> snakeBody){
        while(true){
            int r = random.nextInt(rows);
            int c = random.nextInt(cols);
            Position newFood = new Position(r, c);
            if(!snakeBody.contains(newFood)){
                food = newFood;
                break;
            }
        }
    }

    public Position getFood(){
        return food;
    }

    public int getRows(){
        return rows;
    }

    public int getCols(){
        return cols;
    }
}

class Game{
    private final Board board;
    private final Snake snake;
    private final Scanner scanner;
    GameState gameState;

    public Game(int rows, int cols){
        board = new Board(rows, cols);
        snake = new Snake(new Position(rows/2, cols/2));
        gameState = GameState.RUNNING;
        scanner = new Scanner(System.in);
        board.placeFood(new HashSet<>(snake.getBody()));
    }

    public void start(){
        printInstructions();
        while(gameState.equals(GameState.RUNNING)){
            printBoard();
            System.out.print("Enter direction (WASD): ");
            String input = scanner.nextLine().toUpperCase();
            handleInput(input);
            Position nextHead = snake.getHead().move(snake.currentDirection);

            if (!board.isValid(nextHead) || snake.hasCollided(nextHead)) {
                gameState = GameState.OVER;
                System.out.println("Game Over!");
                break;
            }

            boolean foodEaten = snake.getHead().equals(board.getFood());
            snake.move(nextHead, foodEaten);

            if(foodEaten){
                board.placeFood(new HashSet<>(snake.getBody()));
            }
        }
    }

    public void handleInput(String d){
        switch(d) {
            case "W" -> snake.changeDirection(Direction.UP);
            case "S" -> snake.changeDirection(Direction.DOWN);
            case "A" -> snake.changeDirection(Direction.LEFT);
            case "D" -> snake.changeDirection(Direction.RIGHT);
        }
    }

    private void printInstructions() {
        System.out.println("=== Snake Game ===");
        System.out.println("Controls: W = Up, A = Left, S = Down, D = Right");
        System.out.println("Eat food (F), avoid crashing into walls or yourself!");
    }

    private void printBoard(){
        System.out.println("Current board state");
        List<Position> snakeBody = snake.getBody();
        for(int i=0;i<board.getRows();i++){
            for(int j=0;j<board.getCols();j++){
                Position curr = new Position(i, j);
                if(snakeBody.contains(curr)){
                    System.out.print("S ");
                }
                else if(board.getFood().equals(curr)){
                    System.out.print("F ");
                }
                else{
                    System.out.print("E ");
                }
            }
            System.out.println();
        }
    }

}


