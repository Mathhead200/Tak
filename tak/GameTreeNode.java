package tak;

import java.util.ArrayList;
import java.util.List;

public class GameTreeNode {
	public GameState state;
	public Integer payout = null;
	public List<GameTreeNode> next = new ArrayList<>();
	
	public GameTreeNode(GameState state) {
		this.state = state;
	}
}
