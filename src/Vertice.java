import java.util.HashMap;
import java.util.Map;

public class Vertice implements Comparable<Vertice> {
	private String name;
	public Map<Vertice, Integer> adjacentNodes;
	public int cost;
	public Vertice(String name) {
		this.name=name;
		adjacentNodes = new HashMap<>();
		cost = Integer.MAX_VALUE;
	}
	public void setCost(int cost) {
        this.cost = cost;
    }
	public void add(Vertice neighborNode, int cost){
        adjacentNodes.put(neighborNode, cost);
    }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<Vertice, Integer> getAdjacentNodes() {
		return adjacentNodes;
	}
	public void setAdjacentNodes(Map<Vertice, Integer> adjacentNodes) {
		this.adjacentNodes = adjacentNodes;
	}
	@Override
    public int compareTo(Vertice otherNode) {
        return Integer.compare(this.cost, otherNode.cost);
    }
	
}
