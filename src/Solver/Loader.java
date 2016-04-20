package Solver;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Loader {
	public static Board loadBoard(String filepath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            int size = Integer.parseInt(br.readLine());
            int numberOfRobots = Integer.parseInt(br.readLine());
            
            char[][] b = new char[size][size];
            Point[] robots = new Point[numberOfRobots];
            
            Point goal = null;
            
            Stream<String> fisk = br.lines();
            List<String>lol = fisk.collect(Collectors.toList());
            for (int i = 0; i < lol.size(); i++) {
                char[] line = lol.get(i).toCharArray();
            	for (int j = 0; j < line.length; j++) {
                    if (line[j] == Board.GOAL_CHAR) {
                        goal = new Point(i, j);
                    } else if (Character.isDigit(line[j])) {
                        int number = Character.getNumericValue(line[j]);
                        robots[number] = new Point(i, j);
                    } else if (line[j] != Board.EMPTY_CHAR &&
                               line[j] != Board.WALL_CHAR) {
                        System.err.println("Error: Unknown character '" + line[j] + "' on line " + (i+1) + ", column " + (j+1));
                    }

                    b[i][j] = line[j];
                }
			}
            
            if (goal == null) {
                System.err.println("Error: No goal found on board");
            }
            
            return new Board(b, robots, goal);
        }
    }
}
