package algos;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Fulkerson {

	public static void fulkersonMain(String[] args) {
		try{
			String fpath = new String();
		Scanner filep = new Scanner(System.in);
		fpath = filep.next();
		File fname = new File(fpath);
		Scanner fileR = new Scanner(fname);
		
		//int maxFlow = Fulkerson.fordFulkerson(source,sink,adjacencyList);
		//System.out.println("MAX FLOW "+maxFlow);
		
		filep.close();
		fileR.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}

	}
	
	 public static int fordFulkerson(String source, String sink, Map<String, Node> adjacencyList){
	        String u, v;
	        int maxFlow = 0;
	        int pathFlow;
	    	HashMap<String, Node> residualList = new HashMap<String, Node>(adjacencyList);
	    	ArrayList<String> flist = new ArrayList<String>();
	    	
	    	File opfile = new File("output.txt");
	    	//Scanner opscan = new Scanner(opfile);
	        while(BFS.bfsTraversal(source, sink,residualList))
	{
	            pathFlow = Integer.MAX_VALUE;
	            //System.out.println("Path flow: "+pathFlow);
	            //System.out.println(sink);
	            //System.out.println(prev[sink]);
	            ArrayList<String> alpath = new ArrayList<String>();
	            ArrayList<String> oplist = new ArrayList<String>();
	            for (v = sink; v != source; v = BFS.prev.get(v))
	            {	
	            	//System.out.println("value at this point: "+prev[v]);
	            	//System.out.println("print1");
	            	u = BFS.prev.get(v);
	            	alpath.add(u);
	                //System.out.println(u);
	                //System.out.println("print2");
	                pathFlow = Math.min(pathFlow, residualList.get(u).ll.get(v));
	            }
	            System.out.print("pathflow min "+pathFlow);
	            Pattern p = Pattern.compile("[ag][\\d+]");
	            
	            for(String pt:alpath){
	            	Matcher m = p.matcher(pt);
	            	if(m.find()){
	            		oplist.add(pt);
	            	}
	            }
	            flist.addAll(oplist);
	            
	            for (v = sink; v != source; v = BFS.prev.get(v))
	            {
	                u = BFS.prev.get(v);
	                System.out.println("pathflow::");
	                residualList.get(u).ll.put(v,residualList.get(u).ll.get(v)-pathFlow);
	                //residualGraph[u][v] -= pathFlow;
	                System.out.println("pathflow::"+residualList.get(u).ll.get(v));
	                //residualList.get(v).ll.put(u,residualList.get(v).ll.get(u)+pathFlow);
	                //System.out.println("pathflow::"+residualList.get(v).ll.get(u));
	                //residualGraph[v][u] += pathFlow;
	            }
	            maxFlow += pathFlow;	
	        }
	        
	        Iterator<String> itr = flist.iterator();
	        Pattern p2 = Pattern.compile("[g][\\d+]");
	        String tst;
	        
	        HashSet<String> slist = new HashSet<String>();
	        System.out.println("Flist: "+flist);
	        Collections.reverse(flist);
	        System.out.println("Reverse: "+flist);
	        
	        for(int k=0;k<flist.size();k++){
	        	tst = itr.next();
	        	Matcher m2 = p2.matcher(tst);
	        	if(m2.find()){
	        		slist.add(tst);
	        	}
	        }
	        
	        Iterator<String> ittr2 = slist.iterator();
	        String com, com2;
	        while(ittr2.hasNext()){
	        	com=new String(ittr2.next());
	        	System.out.print("\n"+com+" : ");
	        	Iterator<String> ittr = flist.iterator();
	        	while(ittr.hasNext())
	        	{
	        		com2 = new String(ittr.next());
	        		if(com2.equals(com2)){
	        			com2 = new String(ittr.next());
	        			System.out.print(com2+", ");
	        			
	        		}
	        	}
	        }
	        
	        return maxFlow;
	    }

}