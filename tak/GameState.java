package tak;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class GameState {
	
	/** How many pieces do both players start with for each board size? */
	private static Map<Integer, Integer> STARTING_PIECE_COUNT = new HashMap<>(6, 1.0f);
	static {
		STARTING_PIECE_COUNT.put(3, 10);
//		STARTING_PIECE_COUNT.put(4, 15);
//		STARTING_PIECE_COUNT.put(5, );
//		STARTING_PIECE_COUNT.put(6, );
//		STARTING_PIECE_COUNT.put(8, );
	}
	
	/** Stores the stacks for each square in row-major order. */
	private Stack[] spaces;
	
	/** Stores the number of remaining pieces for each player. */
	private EnumMap<Player, Integer> remainingPieces = new EnumMap<>(Player.class);
	
	/** Who's turn is it? */
	private Player turn;
	
	public static GameState opening(int size, int first, int second) {
		if (first == second)
			throw new IllegalArgumentException("Illegal opening. Moves must be in different locations: " + first);
		
		GameState state = new GameState(size);
		state.spaces[first] = new Stack(Player.P2, Orientation.ROAD);
		state.spaces[second] = new Stack(Player.P1, Orientation.ROAD);
		state.remainingPieces.put(Player.P1, state.remainingPieces.get(Player.P1) - 1);
		state.remainingPieces.put(Player.P2, state.remainingPieces.get(Player.P2) - 1);
		return state;
	}

	/**
	 * Creates an empty board of the given size.
	 * @param size
	 */
	public GameState(int size) {
		size *= size;
		spaces = new Stack[size];
		for (int i = 0; i < size; i++)
			spaces[i] = new Stack();
		turn = Player.P1;
		
		int count = STARTING_PIECE_COUNT.get(size);
		remainingPieces.put(Player.P1, count);
		remainingPieces.put(Player.P2, count);
	}
	
	/** Rotate once clockwise. */
	public void rotate() {
		// TODO: hard coded to only work for 3 by 3 Tak. Update for general Tak.
		Stack temp = spaces[0];
		spaces[0] = spaces[3];
		spaces[3] = spaces[6];
		spaces[6] = spaces[7];
		spaces[7] = spaces[8];
		spaces[8] = spaces[5];
		spaces[5] = spaces[2];
		spaces[2] = spaces[1];
		spaces[1] = temp;
	}
	
	/** Flip horizontally. */
	public void flip() {
		// TODO: hard coded to only work for 3 by 3 Tak. Update for general Tak.
		for (int i = 0; i < spaces.length; i += 3) {
			int j = i + 2;
			Stack temp = spaces[i];
			spaces[i] = spaces[j];
			spaces[j] = temp;
		}
	}
	
	/**
	 * Precondition: <code>this</code> and <code>state</code> are from games of the same size.
	 * @param state
	 * @return
	 */
	private boolean _equals(GameState state) {
		for (Player p : Player.values())
			if (!this.remainingPieces.get(p).equals(state.remainingPieces.get(p)))
				return false;
		for (int i = 0; i < spaces.length; i++)
			if (!this.spaces[i].equals(state.spaces[i]))
				return false;
		if (this.turn != state.turn)
			return false;
		return true;
	}
	
	/**
	 * Precondition: <code>this</code> and <code>state</code> are from games of the same size.
	 * May leave <code>state</code> in an equivalent but rotated or flipped state.
	 * @param state
	 * @return
	 */
	public boolean equals(GameState state) {
		for (int j = 0; j < 2; j++) {
			for (int i = 0; i < 4; i++) {
				if (this._equals(state))
					return true;
				state.rotate();
			}
			state.flip();
		}
		return false;
	}
	
}
