package Solver;

import java.util.Scanner;

public class Solve {

	public static void main(String[] args) {
		//System.in.read();
		
		
		int timeConstraint = Integer.valueOf(args[0]);
		//int timeConstraint = 5;
		
		
		long startTime = System.nanoTime();
		//Board board = Loader.loadBoard("obligatorisk_synlig\\Test5.in");
		//Board board = Loader.loadBoard("konkurrence_random\\1-200-Random.in");
		//Board board = Loader.loadBoard("konkurrence_random\\2-05-Random.in");
		//Board board = Loader.loadBoard("konkurrence_random\\3-07-Random.in");
		//Board board = Loader.loadBoard("konkurrence_random\\4-100-Random.in");
		//Board board = Loader.loadBoard("konkurrence_random\\challenge.in");
		Board board = Loader.loadBoard(new Scanner(System.in));
		BotPathDirection[] directions = RobotPath.getRobotPath(board, timeConstraint);
		//long stopTime = System.nanoTime();
		
		//System.out.println(board.toString());
		Tools.printPath(directions);
		//System.out.println("runtime: " + (stopTime - startTime) / 1000000000.0 + "s");
		//System.out.println("total memory used: " + ((Runtime.getRuntime().totalMemory()) / (1024 * 1024)) + "mb");
		//System.out.println("memory used by process: " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024)) + "mb");
	}
}
