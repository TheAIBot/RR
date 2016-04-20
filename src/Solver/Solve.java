package Solver;

import java.io.IOException;

public class Solve {

	public static void main(String[] args) throws IOException {
		Board board = Loader.loadBoard("D:\\Java Programs\\RR\\obligatorisk_synlig\\Test1.in");
		System.out.println(board.toString());
	}

}
