package simulation;

/**
 * Four possible directions for each car
 * 
 * @author LiuJilin
 */
public enum Direction {
	
	/**
	 * North, or up.
	 */
	NORTH(0, -1),

	/**
	 * South, or down.
	 */
	SOUTH(0, 1),

	/**
	 * West, or left.
	 */
	WEST(-1, 0),

	/**
	 * East, or right.
	 */
	EAST(1, 0);
	private final int dx;
	private final int dy ;
	
	private Direction(int deltaX, int deltaY) {
		this.dx = deltaX;
		this.dy = deltaY;
	}
	
	public int getDeltaX() {
		return dx;
	}
	
	public int getDeltaY() {
		return dy;
	}
}
