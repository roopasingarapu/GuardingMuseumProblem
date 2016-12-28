package algos;
import java.util.*;
import java.io.*;

import algos.Fulkerson;
import algos.Node;
import algos.BFS;

public class Watchman {
    public static void watchmanMain(String[] args) {
    	Vertex vertex;
    	try {
    		/*System.out.println("Enter the path to the file:");
    		Scanner scan = new Scanner(System.in);*/
    		String fpath = args[1];
			File fname = new File(fpath);
			Scanner input = new Scanner(fname);

			long startTime = System.currentTimeMillis();
			int caseNo = 1;
			int wall = input.nextInt();
			int art = input.nextInt();
			int guard = input.nextInt();
			
			String opfpath=args[2];
			//String opfpath = new String("C:/Users/singarapuroopa/Desktop/sub/Algos/proj1/op.txt");
			File opfile = new File(opfpath);
			if(opfile.exists()){
				opfile.delete();
			}
			opfile.createNewFile();
			FileWriter fw = new FileWriter(opfile, true);

        while(wall > 0 || art > 0 || guard > 0) {
            
            ArrayList<drawLine> lines = new ArrayList<drawLine>();
            ArrayList<drawArc> arcs = new ArrayList<drawArc>();
          
            // Reading and storing Wall coordinates
            for(int i = 0; i < wall; i++) {
                int m = input.nextInt();  
                int initX = input.nextInt();
                int initY = input.nextInt();
                int x1 = initX;
                int y1 = initY;
                int dx = 0;
                int dy = 0;
                
                String cs = new String();
                if (input.hasNext("c"))
                {
                	cs = input.next("c");
                	dx = input.nextInt();
                	dy = input.nextInt();
                }
                else if(input.hasNext("s"))
                {
					cs = input.next("s");
				}
                for(int j = 1; j < m; j++)
                {
                    int x2 = input.nextInt();
                    int y2 = input.nextInt();
                    
                    if (cs.equals("c")) 
                    {
						arcs.add(new drawArc(new Vertex(x1,y1), new Vertex(x2,y2), new Vertex(dx,dy)));
					}
					else if(cs.equals("s"))
					{
						lines.add(new drawLine(new Vertex(x1,y1), new Vertex(x2,y2)));
					}
					x1 = x2;
					y1 = y2;
					cs = input.next();
					if (cs.equals("c"))
					{
						dx = input.nextInt();
						dy = input.nextInt();
					}
				}
				if (cs.equals("c")) 
				{
					arcs.add(new drawArc(new Vertex(x1,y1), new Vertex(initX,initY), new Vertex(dx,dy)));
				}
				else if (cs.equals("s"))
				{
					lines.add(new drawLine(new Vertex(x1,y1), new Vertex(initX,initY)));
				}
			}

            // For reading and storing Art coordinates and level
            HashMap<Integer,Vertex> artCord = new HashMap<Integer,Vertex>();
            int[] artLevel = new int[art]; 
            int totalArtLevel = 0;
            
            for(int i = 0; i < art; i++) {
            	vertex = new Vertex(input.nextInt(), input.nextInt());
                artCord.put(i,vertex);
            	artLevel[i] = input.nextInt();
                totalArtLevel = totalArtLevel + artLevel[i];
            }
            
            // For reading and storing Guard coordinates and level
            HashMap<Integer,Vertex> guardCord = new HashMap<Integer,Vertex>();
            int[] guardLevel = new int[guard];
    
            for(int i = 0; i < guard; i++) {
            	vertex = new Vertex(input.nextInt(), input.nextInt());
            	guardCord.put(i,vertex);
            	guardLevel[i] = input.nextInt();
            }

            // Adding Super Source and Super Sink and generating Graph
            HashMap<String,Node> adjacencyList1 = new HashMap<String,Node>();
            adjacencyList1.put("s0", new Node());
                for(int i = 0; i < guard; i++) {
                    adjacencyList1.get("s0").ll.put("g"+i, guardLevel[i]);
                }
                
                for(int i = 0; i < guard; i++) {
                	adjacencyList1.put("g"+i, new Node());
                    for(int j = 0; j < art; j++) {
                    	adjacencyList1.put("a"+j, new Node());
                        if (check(guardCord.get(i),artCord.get(j),arcs,lines)) {
                             adjacencyList1.get("g"+i).ll.put("a"+j, 1);
                        }
                    }
                }
                
                for(int j = 0; j < art; j++) {
                    adjacencyList1.get("a"+j).ll.put("d0", artLevel[j]);
                }
                System.out.print("Case "+caseNo+": ");
                adjacencyList1.put("d0", new Node());
            	   if (Fulkerson.fordFulkerson("s0", "d0", adjacencyList1) >= totalArtLevel)
                	{
                	System.out.println("Yes");
                	fw.write("Case"+caseNo+":Yes");
           
                	}
                else {
                	System.out.println("No");
                	fw.write("Case"+caseNo+":No");
                
                }
            wall = input.nextInt();
            art = input.nextInt();
            guard = input.nextInt();
            caseNo++;
        }
    	long endTime = System.currentTimeMillis();
        System.out.println("The time of execution in MilliSeconds: "+(endTime-startTime));
		fw.flush();
		fw.close();
		
    	}
        catch(Exception e) {
        	e.printStackTrace();
        }
    }
    
    public static boolean check(Vertex v1, Vertex v2, ArrayList<drawArc> arcs, ArrayList<drawLine> lines) {
        for(drawArc arc : arcs) {
            if (arc.testIntersect(v1,v2))
            	return false;
        }
        for(drawLine l2 : lines) {
            if (l2.testIntersect(v1,v2))
            	return false;
        }
        return true;
    }
}

class drawLine {
    double xD,yD,xyD;
    Vertex v1, v2;
    public drawLine(Vertex v1, Vertex v2) {
        this.v1 = v1;
        this.v2 = v2;
        xD = v2.y - v1.y; 
        yD = v1.x - v2.x; 
        xyD = v1.x * v2.y - v2.x * v1.y;
    }
    
    public Vertex intersect(drawLine l) {
        double det = xD * l.yD - l.xD * yD;
        return new Vertex((l.yD * xyD - yD * l.xyD)/det, (xD * l.xyD - l.xD * xyD)/det);
    }
    
    public boolean testIntersect(drawLine l) {
        double det = xD * l.yD - l.xD * yD;
        if (det == 0) return false;
        Vertex intPt = new Vertex((l.yD * xyD - yD * l.xyD)/det, (xD * l.xyD - l.xD * xyD)/det);
        return (((v1.diffVertex(intPt).dotProduct(v2.diffVertex(intPt))) < 0) && (l.v1.diffVertex(intPt).dotProduct(l.v2.diffVertex(intPt)) < 0));
    }
    
    public boolean testIntersect(Vertex v1, Vertex v2) {
        return testIntersect(new drawLine(v1,v2));
    }
}

class Vertex {
    double x,y;
    public Vertex(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public Vertex addVertex(Vertex v) {
        return new Vertex(x+v.x, y+v.y);
    }
    
    public Vertex diffVertex(Vertex v) {
        return new Vertex(x-v.x, y-v.y);
    }
 
    public Vertex multiply(double mul) {
        return new Vertex(x*mul, y*mul);
    }
    
    public double dotProduct(Vertex v) {
        return x*v.x+y*v.y;
    }
    
    public double sqr() {
        return x*x+y*y;
    }

    public Vertex perpend() {
        return new Vertex(y,-x);
    }
    
    public String toString() {
        return "("+x+","+y+")";
    }
}

class drawArc {
    Vertex p1, p2, tangent, center;
    double radsqr;
    public drawArc(Vertex p1, Vertex p2, Vertex tangent) {
        this.p1 = p1;
        this.p2 = p2;
        this.tangent = tangent;
        drawLine tperpend = new drawLine(p1, p1.addVertex(tangent.perpend()));
        Vertex mpt = p1.addVertex(p2).multiply(.5);
        drawLine mperpend = new drawLine(mpt, mpt.addVertex(p1.diffVertex(p2).perpend()));
        center = mperpend.intersect(tperpend);
        radsqr = center.diffVertex(p1).sqr();
    }
    
    public static List<Vertex> getCircleLineIntersectionPoint(Vertex gr,Vertex ar, Vertex center, double radsqr) {
        double agX = ar.x - gr.x;
        double agY = ar.y - gr.y;
        double cgX = center.x - gr.x;
        double cgY = center.y - gr.y;

        double a = agX * agX + agY * agY;
        double b2 = agX * cgX + agY * cgY;
        double c = cgX * cgX + cgY * cgY - radsqr;

        double p = b2 / a;
        double q = c / a;
        
        double d = p * p - q;
        if (d < 0) 
        {
            return Collections.emptyList();
        }
        double tSqr = Math.sqrt(d);
        double sf1 = -p + tSqr;
        double sf2 = -p - tSqr;

        Vertex v1 = new Vertex(gr.x - agX * sf1, gr.y - agY * sf1);
        if (d == 0) 
        { 
            return Collections.singletonList(v1);
        }
        Vertex v2 = new Vertex(gr.x - agX * sf2, gr.y - agY * sf2);
        return Arrays.asList(v1, v2);
    }

   
    public boolean testIntersect(Vertex a, Vertex b) {
    	List<Vertex> al = new ArrayList<Vertex>();
    	al = getCircleLineIntersectionPoint(a,b,this.center,this.radsqr);
    	
    	Vertex arcBegin = p1.diffVertex(center);
        Vertex arcEnd = p2.diffVertex(center);
		
        if (al.size() == 1) {
			Vertex cs1 = al.get(0).diffVertex(center);
			double r1 = arcBegin.dotProduct(cs1);
			double r = arcBegin.dotProduct(arcEnd);
			
			if (arcEnd.dotProduct(tangent) < 0) {
				r = -r - 2 * arcBegin.sqr();
			}

			if (cs1.dotProduct(tangent) < 0)
				r1 = -r1 - 2 * arcBegin.sqr();

			if (al.get(0).diffVertex(a).dotProduct(al.get(0).diffVertex(b)) < 0	&& r < r1) {
				return true;
			}

		}
		else if (al.size() == 2) {
			Vertex cs1 = al.get(0).diffVertex(center);
			double r1 = arcBegin.dotProduct(cs1);
			Vertex cs2 = al.get(1).diffVertex(center);
			double r2 = arcBegin.dotProduct(cs2);
			
			if (cs1.dotProduct(tangent) < 0)
				r1 = -r1 - 2 * arcBegin.sqr();

			if (cs2.dotProduct(tangent) < 0)
				r2 = -r2 - 2 * arcBegin.sqr();
			
			double r = arcBegin.dotProduct(arcEnd);
			
			if (arcEnd.dotProduct(tangent) < 0) {
				r = -r - 2 * arcBegin.sqr();
			}
			if (al.get(0).diffVertex(a).dotProduct(al.get(0).diffVertex(b)) < 0 && r < r1) {
				return true;
			}
			if (al.get(1).diffVertex(a).dotProduct(al.get(1).diffVertex(b)) < 0 && r < r2) {
				return true;
			}
		}     
        return false;
    }
}
    