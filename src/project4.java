import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class project4 {
	public static void main(String args[]) {
		int numberOfFlags=0;
		int numberOfVertex=0;
		String startPoint=null;
		String endPoint=null;
		ArrayList<String> flags= new ArrayList<>();		
		ArrayList<Vertice> nodes=new ArrayList<>();
		HashMap<String, Vertice> cars = new HashMap<String, Vertice>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(args[0]));
			String line;
			int counter=0;
			while((line=reader.readLine()) != null) {
		          String[]input= line.split(" ");
		          if (counter==0) {
		        	  numberOfVertex=Integer.parseInt(input[0]);
		        	  counter++;
		        	  continue;
		          }
		          else if(counter==1) {
		        	  numberOfFlags=Integer.parseInt(input[0]);
		        	  counter++; 
		        	  continue;
		          }
		          else if(counter==2) {
		        	  startPoint=input[0];
		        	  endPoint=input[1];
		        	  counter++;
		        	  continue;
		          }
		          else if(counter==3) {
		        	  for(int i =0;i<input.length;i++) {
		        		  flags.add(input[i]);
		        	  }
		        	  counter++;
		        	  continue;
		          }
		          else {
		        	  if(cars.containsKey(input[0])==true) {
		        		  
		        	  }
		        	  else {
		        		  Vertice home=new Vertice(input[0]);
		        		  nodes.add(home);
		        		  cars.put(input[0], home);
		        	  }
		        	  
		        	  for(int i=1;i<input.length;i=i+2) {
		        		  Vertice home =cars.get(input[0]);
		        		  if (cars.containsKey(input[i])) {
		        			  Vertice komsu= cars.get(input[i]);
		        			  home.add(komsu, Integer.parseInt(input[i+1]));
		        			  komsu.add(home, Integer.parseInt(input[i+1]));
		        		  }
		        		  else {
		        			  Vertice komsu= new Vertice(input[i]);
		        			  nodes.add(komsu);
		        			  cars.put(input[i], komsu);
		        			  home.add(komsu, Integer.parseInt(input[i+1]));
		        			  komsu.add(home, Integer.parseInt(input[i+1]));
		        		  }		        		  
		        	  }
		          }
			}
			reader.close();
		}catch(IOException e ) {
			e.printStackTrace();
		}	
		
		MyGraph matrix= new MyGraph();
		Vertice sourceNode = null;
		Vertice finalNode= null;
		for(Vertice k : nodes) {
			if(k.getName().equals(startPoint)) {
				sourceNode=k;
			}
			if(k.getName().equals(endPoint)) {
				finalNode=k;
			}
			matrix.addNode(k);
		}
		
		int shortest= dijkstra(matrix,sourceNode, finalNode);		
		reCost(matrix);
		if(flags.size()<=1){
			writing(shortest,0,args[1]);
		}
		else {
			ArrayList<Vertice> myFlags=getFlags(matrix,flags);
			MyGraph flagGraph= new MyGraph();
			flagGraph=flagGraph(matrix,myFlags);
			if(flagGraph == null ) {
				writing(shortest,-1,args[1]);
			}
			else {
				int mstCost = Mst(flagGraph, myFlags.get(0));
				writing(shortest,mstCost,args[1]);
			}
			
		}		
	}
	public static ArrayList<Vertice> getFlags(MyGraph matrix,ArrayList<String> flags ){
		HashSet<Vertice> nl= matrix.getNodes();
		ArrayList<Vertice> Fl = new ArrayList<>();
		for(Vertice node: nl) {
			if(flags.contains(node.getName())) {
				Fl.add(node);
			}
		}
		return Fl;
	}
	public static int Mst(MyGraph grp, Vertice source) {
		 HashSet<String> settledNodes = new HashSet<>();
		 HashMap<String,Integer> keys = new HashMap<>();
		 PriorityQueue<Vertice> heap= grp.getHeap();
		 HashSet<Vertice> nodes= grp.getNodes();
		 for(Vertice e : nodes) {
			 String name = e.getName();
			 if(name.equals(source.getName())==true) {
				 keys.put(e.getName(), 0);
				 heap.add(e);
			 }
			 else {
				 keys.put(e.getName(), Integer.MAX_VALUE); 
			 }			 
		 }
		 
		 while (settledNodes.size() != nodes.size()){
			 if (heap.isEmpty()){
	                return 0 ;
	         }
			 Vertice minDistanceNode = heap.poll();
			 if (settledNodes.contains(minDistanceNode.getName()))
	                continue;
			 settledNodes.add(minDistanceNode.getName());
			 Map<Vertice, Integer> newOne = new HashMap<>();
			 newOne= minDistanceNode.getAdjacentNodes();
			 for(Map.Entry<Vertice, Integer> adjacentPair: newOne.entrySet()){
				 Vertice adjacentNode = adjacentPair.getKey();
	             Integer weight = adjacentPair.getValue();
	             if(settledNodes.contains(adjacentNode.getName())== false) {
	            	 if(weight<keys.get(adjacentNode.getName())) {
	            		 keys.replace(adjacentNode.getName(), weight);
	            	 }
	             }
			 }
			 Integer Min= Integer.MAX_VALUE;
			 for(Map.Entry<String, Integer> edges: keys.entrySet()){
				String node = edges.getKey();
				Integer weight= edges.getValue();
				if(settledNodes.contains(node)== false) {
					if(weight<Min) {
						Min = keys.get(node);
					}
				}
			 }
			 for(Map.Entry<String, Integer> edges: keys.entrySet()){
					String node = edges.getKey();
					Integer weight= edges.getValue();
					if(settledNodes.contains(node)== false) {
						if(weight==Min) {
							heap.clear();
							heap.add(get(node, grp));
						}
					}
				 }
		 }
		 Integer finalCost=0;
		 for(Map.Entry<String, Integer> edges: keys.entrySet()){
			 Integer cost= edges.getValue();
			 finalCost+=cost;
		 }
		 return finalCost;
		
	}
	public static Vertice get(String s, MyGraph matrix) {
		for(Vertice e : matrix.getNodes()) {
			if( s.equals(e.getName())==true) {
				return e;
			}
		}
		return null;
		
	}
	public static MyGraph flagGraph(MyGraph matrix,ArrayList<Vertice> flags ) {
		MyGraph FlagGraph= new MyGraph();
		for(Vertice e : flags) {
			Vertice flag= new Vertice(e.getName());
			Map<Vertice, Integer> sk= new HashMap<>();
			sk=newDijkstra(matrix, e, flags);
			if(sk==null) {
				return null;
			}
			flag.setAdjacentNodes(sk);
			reCost(matrix);
			FlagGraph.addNode(flag);
		}		
		return FlagGraph;
	}
	public static void reCost(MyGraph matrix) {
		HashSet<Vertice> nl= matrix.getNodes();
		for (Vertice v : nl) {
			v.cost=Integer.MAX_VALUE;
		}
		matrix.setNodes(nl);
	}
	public static void writing(int path, int flag,String args) {
		try {
		      BufferedWriter myWriter = new BufferedWriter(new FileWriter(args));
		      String p= Integer.toString(path);
		      String f = Integer.toString(flag);
		      myWriter.write(p+"\n");
		      myWriter.write(f);
		      myWriter.close();
		     
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
	}
	public static HashMap<Vertice, Integer> newDijkstra(MyGraph grp, Vertice source , ArrayList<Vertice> flags) {
		 HashSet<Vertice> settledNodes = new HashSet<>();
		 source.setCost(0);
		 PriorityQueue<Vertice> heap= grp.getHeap();
		 HashSet<Vertice> nodes= grp.getNodes();
		 heap.add(source);
		 while (settledNodes.size() != nodes.size()){
			 if (heap.isEmpty()){
	                return null;
	            }
			 Vertice minDistanceNode = heap.poll();
			 if (settledNodes.contains(minDistanceNode))
	                continue;
			 settledNodes.add(minDistanceNode);
			 for(Map.Entry<Vertice, Integer> adjacentPair: minDistanceNode.adjacentNodes.entrySet()){
				 Vertice adjacentNode = adjacentPair.getKey();
	             Integer weight = adjacentPair.getValue();
	             if (!settledNodes.contains(adjacentNode)){
	            	 int costOfMinDistanceNode = minDistanceNode.cost;
	                 if(costOfMinDistanceNode + weight < adjacentNode.cost){
	                    adjacentNode.setCost(costOfMinDistanceNode + weight);
	                 }
	                 heap.add(adjacentNode);   
	             }
			 }
			 
		 }
		 HashMap<Vertice, Integer> flagNodes = new HashMap<>();
		 for(Vertice e : flags) {
			 if(e== source) {
				 continue;
			 }
			 int edge= e.cost;
			 if(edge==Integer.MAX_VALUE) {
				 return null;
			 }
			 Vertice sk = new Vertice(e.getName());
			 flagNodes.put(sk, edge);
		 }
		
		 return flagNodes;
		
	}
	public static int dijkstra(MyGraph grp, Vertice source , Vertice finalNode) {
		 HashSet<Vertice> settledNodes = new HashSet<>();
		 source.setCost(0);
		 PriorityQueue<Vertice> heap= grp.getHeap();
		 HashSet<Vertice> nodes= grp.getNodes();
		 heap.add(source);
		 while (settledNodes.size() != nodes.size()){
			 if (heap.isEmpty()){
	                return -1;
	            }
			 Vertice minDistanceNode = heap.poll();
			 if (settledNodes.contains(minDistanceNode))
	                continue;
			 settledNodes.add(minDistanceNode);
			 for(Map.Entry<Vertice, Integer> adjacentPair: minDistanceNode.adjacentNodes.entrySet()){
				 Vertice adjacentNode = adjacentPair.getKey();
	             Integer weight = adjacentPair.getValue();
	             if (!settledNodes.contains(adjacentNode)){
	            	 int costOfMinDistanceNode = minDistanceNode.cost;
	                 if(costOfMinDistanceNode + weight < adjacentNode.cost){
	                    adjacentNode.setCost(costOfMinDistanceNode + weight);
	                 }
	                 heap.add(adjacentNode);   
	             }
			 }
			 
		 }
		
		return finalNode.cost;
		
	}
}
