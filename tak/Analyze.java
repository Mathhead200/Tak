package tak;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Analyze {

	private static int SIZE = 3;
	
	private static List<GameTreeNode> OPENINGS;
	static {
		OPENINGS = new ArrayList<>(12);
		for (int[] opening : new int[][] {
			{5,1}, {5,2},
			{2,1}, {2,4}, {2,5}, {2,7}, {2,8},
			{1,2}, {1,3}, {1,4}, {1,6}, {1,9}
		})
			OPENINGS.add(new GameTreeNode(GameState.opening(SIZE, opening[0] - 1, opening[1] - 1)));
		OPENINGS = Collections.unmodifiableList(OPENINGS);
	}
	
	public static void main(String[] args) {
		
	}

}
