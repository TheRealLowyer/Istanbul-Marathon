import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class MyGraph {
	private HashSet<Vertice> nodes;
    private PriorityQueue<Vertice> heap;


    public MyGraph(){
        nodes = new HashSet<>();
        heap = new PriorityQueue<>();
    }
    
    public HashSet<Vertice> getNodes() {
		return nodes;
	}

	public void setNodes(HashSet<Vertice> nodes) {
		this.nodes = nodes;
	}

	public PriorityQueue<Vertice> getHeap() {
		return heap;
	}

	public void setHeap(PriorityQueue<Vertice> heap) {
		this.heap = heap;
	}

	public void addNode(Vertice newNode){
        nodes.add(newNode);
    }
}
