package tak;

public class Stack {
	/**
	 * A bit-packed representation of the pieces in the stack.
	 * P1=0, P2=1.
	 * The lowest order bit, bit 0, is the top of the stack.
	 */
	private long bits = 0;
	
	/** The number of pieces in the stack */
	private int height = 0;

	/**
	 * What's on top of the current stack?
	 * <code>null</code> if the stack is empty.
	 */
	private Orientation state = null;

	/**
	 * Create an empty stack.
	 */
	public Stack() {
	}
	
	/**
	 * Create a stack with a single piece with the given color (player) in the given state.
	 * @param piece
	 * @param state
	 */
	public Stack(Player piece, Orientation state) {
		this.bits = piece.ordinal();
		this.height = 1;
		this.state = state;
	}
	
	/**
	 * Create a stack with a single piece with the given color (player) in the given state.
	 * @param action Contains the information for which piece was placed.
	 * @see #Stack(Player, Orientation)
	 */
	public Stack(Place action) {
		this(action.player, action.orientation);
	}
	
	public Stack(Stack stack) {
		this.bits = stack.bits;
		this.height = stack.height;
		this.state = stack.state;
	}
	
	public static int mask(int n) {
		return ~(-1 << n);
	}
	
	public boolean isEmpty() {
		return height == 0;
	}

	public int getHeight() {
		return height;
	}

	public Orientation getState()  {
		return state;
	}

	public Player getController() {
		return Player.values()[(int) (bits & 0x1L)];
	}
	
	public boolean equals(Stack stack) {
		return this.height == stack.height
				&& this.state == stack.state
				&& this.bits == stack.bits;
	}
	
	/**
	 * Adds the given stack on top of this one. This stack is modified, and the given
	 * stack is left unchanged, but should be discarded.
	 * 
	 * @param top
	 */
	public void add(Stack top) {
		this.bits = this.bits << top.height | top.bits;
		this.height += top.height;
		this.state = top.state;
	}
	
	/**
	 * Drop the given number of pieces of the bottom of this stack. The dropped
	 * pieces are returned as a new Stack.
	 * 
	 * @param count How many pieces to drop.
	 * @return Dropped pieces as a new stack.
	 */
	public Stack drop(int count) {
		if (count < 0)
			throw new IllegalArgumentException("Can only drop a non-negative number of pieces: " + count);
		if (count > height)
			throw new IllegalArgumentException("Stack too small to drop " + count + ": " + height);
		
		Stack dropped = new Stack();
		
		dropped.bits = this.bits & mask(count);
		this.bits >>>= count;
		
		dropped.height = count;
		this.height -= count;
		
		if (this.height > 0) {
			dropped.state = Orientation.ROAD;
		} else {
			dropped.state = this.state;
			this.state = null;
		}
		
		return dropped;
	}
	
	@Override
	public String toString() {
		return (height > 0 ? getController().ordinal() : " ") + (height > 1 ? "+" : " ");
	}
	
	@Override
	public Stack clone() {
		return new Stack(this);
	}
}
