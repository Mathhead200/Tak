package tak;

public enum Player {
	P1, P2;
	
	public Player next() {
		Player[] v = Player.values();
		return v[(ordinal() + 1) % v.length];
	}
}
