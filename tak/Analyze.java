package tak;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Analyze {

	private static int SIZE = 3;
	
	private static List<GameTreeNode> OPENINGS;
	static {
		try {
			OPENINGS = new ArrayList<>(12);
			for (int[] opening : new int[][] {
				{5,1}, {5,2},
				{2,1}, {2,4}, {2,5}, {2,7}, {2,8},
				{1,2}, {1,3}, {1,4}, {1,6}, {1,9}
			})
				OPENINGS.add(new GameTreeNode(GameState.opening(SIZE, opening[0] - 1, opening[1] - 1)));
			OPENINGS = Collections.unmodifiableList(OPENINGS);
		} catch(Throwable ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
	
	public static void main(String[] args) {
		// tests
		GameState state21 = GameState.opening(SIZE, 2-1, 1-1);
		GameState state23 = GameState.opening(SIZE, 2-1, 3-1);
		System.out.println(state21.equals(state23));
		
		GameTreeNode root21 = new GameTreeNode(state21);
		root21.buildNextMoves();
		for (GameTreeNode next : root21.next)
			System.out.println(next.state + "\n");
	}

}
