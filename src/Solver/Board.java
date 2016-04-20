package Solver;

import java.awt.Point;

public class Board {
	public static final char GOAL_CHAR = 'G';
	public static final char WALL_CHAR = '#';
	public static final char EMPTY_CHAR = ' ';

    private char[][] board;
    private int size;
    private Point[] robots;
    private Point goal;

    public Board(char[][] board, Point[] robots, Point goal) {
    	this.board = board;
        this.size = board.length;
        this.robots = robots;
        this.goal = goal;
    }
  
    private boolean insideBoard(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }
    private boolean insideBoard(Point p) {
        return p.getX() >= 0 && p.getX() < size && p.getY() >= 0 && p.getY() < size;
    }

    public String toString() {
    	StringBuilder sb = new StringBuilder(size * size);

        for (int i = 0; i < board.length; i++) {
        	sb.append(board[i]);
            sb.append(System.getProperty("line.separator"));
        }
        return sb.toString();
    }
}
