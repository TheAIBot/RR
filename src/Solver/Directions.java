package Solver;

public enum Directions {
	Up(0,-1)
	{
		
		@Override
		public Directions getOppositeDirection() {
			return Directions.Down;
		}

		@Override
		public String toShortString() {
			return "U";
		}
	},
	Down(0,1)
	{
		
		@Override
		public Directions getOppositeDirection() {
			return Directions.Up;
		}
		
		@Override
		public String toShortString() {
			return "D";
		}
	},
	Left(-1,0)
	{
		
		@Override
		public Directions getOppositeDirection() {
			return Directions.Right;
		}
		
		@Override
		public String toShortString() {
			return "L";
		}
	},
	Right(1,0)
	{
		
		@Override
		public Directions getOppositeDirection() {
			return Directions.Left;
		}
		
		@Override
		public String toShortString() {
			return "R";
		}
	};
	
	public final int translationX;
	public final int translationY;
	
	Directions(int x, int y)
	{
		this.translationX = x;
		this.translationY = y;
	}
	
	public abstract Directions getOppositeDirection();
	
	public abstract String toShortString();
}
