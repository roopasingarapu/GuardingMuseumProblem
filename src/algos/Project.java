package algos;

import java.util.Scanner;

public class Project {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Scanner abc = new Scanner(System.in);
		//args = new String(abc.next());
		if(args.length > 0){
			if(args[0] == "-b"){
				BFS.bfsMain(args);
			}else if(args[0] == "-f"){
				Fulkerson.fulkersonMain(args);
			}else if(args[0] == "-m"){
				Watchman.watchmanMain(args);
			}
		}
	}

}
