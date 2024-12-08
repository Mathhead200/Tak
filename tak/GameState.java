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
	
	private int size;
	
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
		this.size = size;
		
		int sizeSq = size * size;
		spaces = new Stack[sizeSq];
		for (int i = 0; i < sizeSq; i++)
			spaces[i] = new Stack();
		turn = Player.P1;
		
		if (!STARTING_PIECE_COUNT.containsKey(size))
			throw new IllegalArgumentException("size = " + size);
		int count = STARTING_PIECE_COUNT.get(size);
		remainingPieces.put(Player.P1, count);
		remainingPieces.put(Player.P2, count);
	}
	
	public GameState(GameState state) {
		this.size = state.size;
		this.spaces = new Stack[state.spaces.length];
		for (int i = 0; i < spaces.length; i++)
			this.spaces[i] = state.spaces[i].clone();
		for (Player p : Player.values())
			this.remainingPieces.put(p, state.remainingPieces.get(p));
		this.turn = state.turn;
	}
	
	/** Rotate once clockwise. */
	public void rotate() {
		// TODO: hard coded to only work for 3 by 3 Tak. Update for general Tak.
		Stack temp = spaces[0];  // corners
		spaces[0] = spaces[6];
		spaces[6] = spaces[8];
		spaces[8] = spaces[2];
		spaces[2] = temp;
		
		temp = spaces[1];       // edges
		spaces[1] = spaces[3];
		spaces[3] = spaces[7];
		spaces[7] = spaces[5];
		spaces[5] = temp;
	}
	
	/** Flip horizontally. */
	public void flip() {
		for (int row = 0; row < spaces.length; row += size) {
			int i = row;
			int j = i + size - 1;
			while (i < j) {
				Stack temp = spaces[i];
				spaces[i] = spaces[j];
				spaces[j] = temp;
				i++;
				j--;
			}
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
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof GameState))
			return false;
		GameState state = (GameState) obj;
		
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
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (int row = 0; row < size; row++) {
			if (row != 0)
				str.append("\n--------\n");
			for (int col= 0; col < size; col++) {
				if (col != 0)
					str.append("|");
				str.append(spaces[row * size + col]);
			}
		}
		str.append("\n");
		for (Player p : Player.values()) {
			if (turn == p)
				str.append("*");
			str.append(p).append(": ").append(remainingPieces.get(p)).append("; ");
		}
		return str.toString();
	}
	
	@Override
	public GameState clone() {
		return new GameState(this);
	}

	public int getSize() {
		return size;
	}
	
	public int getSizeSq() {
		return spaces.length;
	}
	
	public Stack get(int i) {
		return spaces[i];
	}
	
	public Player getTurn() {
		return turn;
	}
	
	public Player nextTurn() {
		return turn = turn.next();
	}
	
	public boolean isFull() {
		// board is full
		for (int i = 0; i < spaces.length; i++)
			if (!spaces[i].isEmpty())
				return false;
		return true;
	}
	
	public boolean isOver() {
		if (isFull())
			return true;
		
		// player placed last piece
		for (Player p : Player.values())
			if (remainingPieces.get(p) <= 0)
				return true;
		
		// TODO: completed road?
		// ...
		
		return false;
	}
	
	public int score() {
		if (!isOver())
			throw new IllegalStateException("Game is not over");
		
		return 0;  // TODO: determine winner and score
	}

	public int reduceRemainingPieces() {
		int count = remainingPieces.get(turn);
		if (count <= 0)
			throw new IllegalStateException("No more pieces");
		remainingPieces.put(turn, count - 1);
		return count - 1;
	}
}
