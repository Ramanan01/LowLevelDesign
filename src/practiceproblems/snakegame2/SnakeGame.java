package src.practiceproblems.snakegame2;

import java.util.*;


public class SnakeGame {
    
}

enum Direction{
    LEFT,
    RIGHT,
    UP,
    DOWN
}

enum GameState{
    RUNNING, OVER
}

class InvalidDirectionException extends Exception{
    InvalidDirectionException(String message){
        super(message);
    }
}

class Position{
    int row, col;
    Position(int r, int c){
        this.row = r;
        this.col = c;
    }
    
    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(!(o instanceof Position)) return false;
        Position pos = (Position) o;
        if(pos.row == this.row && pos.col == this.col){
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(this.row, this.col);
    }

    public Position move(Direction d){
        return switch (d) {
            case Direction.LEFT -> new Position(row - 1, col);
            case Direction.RIGHT -> new Position(row + 1, col);
            case Direction.UP -> new Position(row, col - 1);
            case Direction.DOWN -> new Position(row, col + 1);
        };
    }
}

class Snake{
    Direction currentDirection;
    Deque<Position> body;
    
    Snake(int r, int c){
        body = new LinkedList<>();
        body.offer(new Position(r, c));
        currentDirection = Direction.RIGHT;
    }
    
    public void changeDirection(Direction direction) throws InvalidDirectionException{
        if((direction== Direction.UP && currentDirection== Direction.DOWN) ||
                (direction== Direction.DOWN && currentDirection== Direction.UP) ||
                (direction== Direction.LEFT && currentDirection== Direction.RIGHT) ||
                (direction== Direction.RIGHT && currentDirection== Direction.LEFT)){
            return;
        }
        currentDirection = direction;
    }
    
    public void move(Position position, boolean grow){
        body.addFirst(position);
        if(!grow){
            body.removeLast();
        }
    }
    
    public Position getHead(){
        return body.getFirst();
    }

    public List<Position> getBody(){
        return new ArrayList<>(body);
    }
}

class Board{
    int row, col;
    Position food;
    Random random;

    Board(int row, int col){
        this.row = row;
        this.col = col;
        random = new Random();
    }

    public boolean isValid(Position pos){
        return (pos.row>=0 && pos.row<row && pos.col>=0 && pos.col<col);
    }

    public void placeFood(Set<Position> snakeBody){
        boolean foodPlaced = false;
        while(!foodPlaced){
            int r = random.nextInt(row);
            int c = random.nextInt(col);
            Position newFood = new Position(r, c);
            if(!snakeBody.contains(food)){
                food = newFood;
                foodPlaced = true;
            }
        }
    }

    public Position getFood(){
        return food;
    }

    public int getRows(){
        return row;
    }

    public int getCols(){
        return col;
    }
}