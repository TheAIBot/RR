package Solver;

import java.awt.Point;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Loader {
	
	public static Board loadBoard(String filepath) {
		try {
			return loadBoard(new Scanner(new FileReader(filepath)));
		} catch (Exception e) {
			return null;
		}
    }
	
	public static Board loadBoard(Scanner br)
	{
        int size = Integer.parseInt(br.nextLine());
        int numberOfRobots = Integer.parseInt(br.nextLine());
        
        char[][] b = new char[size][];
        Point[] robots = new Point[numberOfRobots];
        Point goal = null;
        List<String> lol = new ArrayList<String>();
        for (int i = 0; i < size; i++) {
			lol.add(br.nextLine());
			b[i] = new char[size];
		}
        for (int i = 0; i < lol.size(); i++) {
            char[] line = lol.get(i).toCharArray();
        	for (int j = 0; j < line.length; j++) {
                if (line[j] == Board.GOAL_CHAR) {
                    goal = new Point(j, i);
                } else if (Character.isDigit(line[j])) {
                    int number = Character.getNumericValue(line[j]);
                    robots[number] = new Point(j, i);
                } else if (line[j] != Board.EMPTY_CHAR &&
                           line[j] != Board.WALL_CHAR) {
                    System.err.println("Error: Unknown character '" + line[j] + "' on line " + (i+1) + ", column " + (j+1));
                }

                b[j][i] = line[j];
            }
		}
        
        if (goal == null) {
            System.err.println("Error: No goal found on board");
        }
        
        return new Board(b, robots, goal);
	}
}
