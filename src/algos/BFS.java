package algos;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.*;

import algos.Fulkerson;

public class BFS {
	static Node node;
	static Hashtable<String,String> prev = new Hashtable<String,String>();
	public static HashMap<String,Node> adjacencyList = new HashMap<String,Node>();
	public BFS(int vertex)
	{
		for (int i = 0; i < vertex; i++)
			adjacencyList.put(""+i,new Node());
	}
	public void defineEdge(String u,String v,int wt)
	{		
		node = adjacencyList.get(u);
		node.ll.put(v,wt);
	}
	public Node getEdge(String u)
	{
		return adjacencyList.get(u);
	}
	
	public static void bfsMain(String[] args) {

		int vertex,edge,count=0;
		String source="0",sink="3";
		String v;
		BFS adjList = null;			
		try
		{
			System.out.println("Enter the path of the input file: ");
			File fname = new File(args[1]);
			long startTime = System.currentTimeMillis();
			FileReader fr = new FileReader(fname);
		    LineNumberReader lnr = new LineNumberReader(fr);
			Scanner scan = new Scanner(fname);
			int linenumber = 0;
            while (lnr.readLine() != null){	
        	linenumber++;
            }
			adjList = new BFS(linenumber);
			while(count != linenumber)
			{
				String[] splited = scan.nextLine().split("\\s+");
				for(int i=0; i<splited.length;i=i+2){
					System.out.println("Enter the number of Vertices: "+splited[i]);
					if(splited[i].trim().isEmpty())
						break;
				v=splited[i];
				int wt=Integer.valueOf(splited[i+1]);
				adjList.defineEdge(""+count,v,wt);
				}
				count++;
			}
			System.out.println("Adjacency List Representation of the Graph:");
			for(int i=0;i<linenumber;i++)
			{
				
				Node edgeList = adjList.getEdge(""+i);
				System.out.print(i);
				for(String node: edgeList.ll.keySet()){
					System.out.print("-("+edgeList.ll.get(node)+")->"+node);
				}
				System.out.println();
			}
			source = args[2];
			sink = args[3];

		boolean sinkReached = bfsTraversal(source,sink,adjacencyList);
		long endTime = System.currentTimeMillis();
        System.out.println("The time of execution in MilliSeconds: "+(endTime-startTime));
		
		scan.close();
		}
		catch(Exception e)
		{
			System.out.println("Exception"+e);
		}	
	}



public static boolean bfsTraversal(String source,String sink,HashMap<String,Node> adjacencyList)
{
	
	Queue<String> queue= new LinkedList<String>();
	int vertex;
	String checkNode;
	boolean sinkReached = false;
	vertex=adjacencyList.size();
	Hashtable<String,Boolean> flag=new Hashtable<String,Boolean>();
	for(String i:adjacencyList.keySet())
	{
		flag.put(""+i, false);
		prev.put(""+i, "-1");
	}
	flag.put("d0", false);
	queue.clear();
	flag.put(source, true);
queue.add(source);
while(!queue.isEmpty())
{
	checkNode = queue.remove();
	node=adjacencyList.get(checkNode);
	for(String k: node.ll.keySet())
	{
		
		if(flag.get(k) == false && node.ll.get(k)!=0)
		{
			flag.put(k, true);
			prev.put(k, checkNode);
			queue.add(k);
		}
	}	
}
	if (flag.get(sink) == true) 
	{
		sinkReached = true;
		System.out.println("Path found");
	}
	else
		System.out.println("Path not found");
	System.out.println("shortest path from "+source+" to "+sink);
	
	while(!prev.get(sink).equalsIgnoreCase("-1")){
		
		System.out.print( sink + "\t");
		sink=prev.get(sink);
	}
	System.out.print(sink);
	return sinkReached;
 }//end of bfs traversal
}// end of bfs class

class Node{
	
	Map<String,Integer> ll;
	Node(){
		ll = new LinkedHashMap<String,Integer>();
		  }	
}