package tak;

import java.util.ArrayList;
import java.util.List;

import static tak.Orientation.ROAD;
import static tak.Orientation.WALL;

public class GameTreeNode {
	public GameState state;
	public Integer payout = null;
	public List<GameTreeNode> next = new ArrayList<>();
	
	public GameTreeNode(GameState state) {
		this.state = state;
	}
	
	public void buildNextMoves() {
		for (int i = 0; i < state.getSizeSq(); i++) {
			if (this.state.get(i).isEmpty()) {
				// place flat and wall in each empty spaces
				for (Orientation o : new Orientation[] {ROAD, WALL}) { 
					GameState state = this.state.clone();
					GameTreeNode node = new GameTreeNode(state);
					state.get(i).add(new Stack(state.getTurn(), o));
					state.reduceRemainingPieces();
					if (state.isOver()) {
						// TODO: ...?
						node.payout = state.score();
					} else {
						state.nextTurn();
					}
					next.add(node);
				}
			} else {
				// TODO: for each direction, move stack
				
			}
		}
	}
}
