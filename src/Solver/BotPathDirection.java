package Solver;

public class BotPathDirection {
	public final int botNumber;
	public final Directions direction;
	
	public BotPathDirection(int botNumber, Directions direction)
	{
		this.botNumber = botNumber;
		this.direction = direction;
	}
	
	public String toString()
	{
		return "" + botNumber + direction.toShortString();
	}
}
