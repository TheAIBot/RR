package Solver;

import java.util.List;

public class Tools {
	
	public static void printPath(BotPathDirection[] pathDirections) {
		for (BotPathDirection botPathDirection : pathDirections) {
			System.out.println(botPathDirection.toString());
		}
	}
	
	public static void printPath(List<BotPathDirection> pathDirections) {
		for (BotPathDirection botPathDirection : pathDirections) {
			System.out.println(botPathDirection.toString());
		}
	}
}
