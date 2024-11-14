package tak;

public class Place implements Action {
	public Player player;
	public Orientation orientation;
	
	/**
	 * <pre>
	 * 0 1 2
	 * 3 4 5
	 * 6 7 8
	 * </pre>
	 */
	public int location;
	
	public Place(Player player, Orientation orientation, int location) {
		this.player = player;
		this.orientation = orientation;
		this.location = location;
	}
}
