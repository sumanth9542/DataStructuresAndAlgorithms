
/* (1)Name: Sumanth Immadi
   (2)Student Id: 807482254
   (3)I take a pledge of honesty that I did not copy/modify from other's codes
   (4)I declare my copyright that no one else should copy/modify the codes.
*/

import java.io.*;
import java.util.*;
import java.util.Date;

//I worked on this assignment from last 1 weeks.

public class maxMatch {
	// Function to find a path using BFS in the residual graph
	public static boolean findPath(int[][] residue, int[] currentPath, int V, int s, int dest) {
		boolean[] visited = new boolean[V];
		Queue<Integer> queue = new LinkedList<>();
		visited[s] = true;
		currentPath[s] = -1;
        queue.add(s);
		// BFS to find an augmenting path in the residual graph
		while (queue.size() !=0) {
			int u = queue.poll();
			for (int v = 0; v < V ; v++) {
				if (!visited[v]&& residue[u][v] >= 1) {
					currentPath[v] = u;
					visited[v] = true;
					queue.add(v);*****88888
				}
			}
		}
		return visited[dest]; 
	}
	
	// Function to print the maximum flow in the flow network
	public static void printMaxFlow(int[][] g, int V, int s, int dest) {
		int[] currentPath = new int[V]; 
		int maxFlow = 0;  
		int maxPathFlow; 
		
		int[][] residue = new int[V][V]; 
		int[][] maxflow = new int[V][V];
		int[][] foundInPath = new int[V][V];
		for (int i = 0; i < g.length; i++) { 
			for (int j = 0; j < g.length; j++) {
				residue[i][j] = g[i][j];
				maxflow[i][j] = 0;
			}
		}
		// Main loop to find augmenting paths and update the flow network
		while (findPath(residue, currentPath, V, s, dest)) {
			maxPathFlow = Integer.MAX_VALUE; 
			int end = dest;
			int start = end;

			// Find the minimum residual capacity in the augmenting path
			while (start != s) {
				start = currentPath[end];
				foundInPath[start][end] = 1;
				if (maxPathFlow > residue[start][end]) {
					maxPathFlow = residue[start][end];
				}
				end = currentPath[end];
			}

			// Update the residual capacities in the augmenting path
			end = dest;
			start = end;
			while (start != s) {
				start = currentPath[end];
				residue[start][end] -= maxPathFlow;
				residue[end][start] += maxPathFlow;
				maxflow[start][end] = residue[start][end];
				maxflow[end][start] += maxPathFlow;
				end = currentPath[end];
			}
			maxFlow = maxFlow + maxPathFlow;
		}
		//print matched pairs
		int i,j;
		for(i=0;i<V;i++ )
		{
			for(j=0;j<V;j++)
			{
				if(i < s && j< s && i < dest && j < dest)
				{
					if(maxflow[i][j] > 0)
					{
						System.out.println("(" + i + ", " + j + ")");
					}
				}
			}
		}
		//print total matches 
		System.out.print("Matches: "+ maxFlow);
	}
	

	// Main function to read input, execute the algorithm, and print results
    public static void main(String[] args) throws IOException {
		System.out.println();
		System.out.println("Maximum number of matches in bipartite graphs in "+args[0]);
		System.out.println();
        BufferedReader reader = new BufferedReader(new FileReader(args[0]));
        String line;

        int G = 0;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("** G")) {
            	G++;
                int V = Integer.parseInt(line.split(",")[0].split("=")[1]);
                System.out.println("** G" + G + "  |V|=" + V);
                int g[][] = new int[V][V];
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("----------------")) {
                    	Date begin = new Date();
						
                    	bipartite(g, V, G);
						//System.out.println("isbipartite: "+b);
                    	Date end = new Date();
                    	System.out.println(" (" + (end.getTime() - begin.getTime()) + " ms)");
						System.out.println();
                    	break;
                    }
                    if (line.contains("u")) continue;
                    line = line.replaceAll(" ", "");
                    line = line.split("\\)")[0];
                    line = line.split("\\(")[1];
					//System.out.println("line: "+ line);
                    String[] data = line.split(",");
            		int u = Integer.parseInt(data[0]);
                    int v = Integer.parseInt(data[1]);
                    g[u][v] = 1;
					g[v][u] = 1;
                }

                
            }
        }

		System.out.println("asg by Sumanth Immadi");

        reader.close();
    }

	public static void bipartite(int[][] graph, int vertices , int G) {
        int[] setAssignment = new int[vertices];
		int[][] ng = new int[vertices][vertices];
        Arrays.fill(setAssignment, -1);
		boolean isbipartite = true;
        for (int i = 0; i < vertices; i++) {
            if (setAssignment[i] == -1 && !dfs(i, 0, setAssignment, graph, vertices)) {
                isbipartite =  false;
            }
        }
		if (isbipartite) {
			
            List<Set<Integer>> disjointSets = findDisjointSets(setAssignment, vertices);
            int set1Size = disjointSets.get(0).size();
			int[] disjointset1 = new int[set1Size];
			int set2Size = disjointSets.get(1).size();
			int[] disjointset2 = new int[set2Size];
			int set1count =0;
            for (Integer element : disjointSets.get(0)) {
				disjointset1[set1count] = element;
				set1count++;
			}
			int set2count =0;
            for (Integer element : disjointSets.get(1)) {
				disjointset2[set2count] = element;
				set2count++;
			}
			for (int i = 0; i < disjointset1.length; i++) {
				for (int j = 0; j < disjointset2.length; j++) {
					int set1Index = disjointset1[i];
					int set2Index = disjointset2[j];
					if (graph[set1Index][set2Index] == 1) {
						ng[set1Index][set2Index] = 1;
					}
				}
			}
			int s = vertices, dest = vertices + 1;

			int[][] nmg = new int[vertices + 2][vertices + 2];
			for (int i = 0; i < vertices; i++) {
				System.arraycopy(ng[i], 0, nmg[i], 0, vertices);
			}

			for (int vertex : disjointset1) {
				nmg[s][vertex] = 1;
			}
			for (int vertex : disjointset2) {
				nmg[vertex][dest] = 1;
			}
			printMaxFlow(nmg, vertices + 2, s, dest) ;
            
        } else {
			
            System.out.println("Not a bipartite graph");
        }
        
    }

	public static boolean dfs(int vertex, int set, int[] setAssignment, int[][] adjacencyMatrix, int vertices) {
		setAssignment[vertex] = set;
	
		for (int neighbor = 0; neighbor < vertices; neighbor++) {
			if (adjacencyMatrix[vertex][neighbor] == 1) {
				if (setAssignment[neighbor] == -1) {
					if (!dfs(neighbor, 1 - set, setAssignment, adjacencyMatrix, vertices)) {
						return false;
					}
				} else if (setAssignment[neighbor] == setAssignment[vertex]) {
					return false; // Graph is not bipartite
				}
			}
		}
	
		return true; // Graph is bipartite
	}

	private static List<Set<Integer>> findDisjointSets(int[] setAssignment, int vertices) {
        List<Set<Integer>> disjointSets = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Set<Integer> set = new HashSet<>();
            for (int j = 0; j < vertices; j++) {
                if (setAssignment[j] == i) {
                    set.add(j);
                }
            }
            disjointSets.add(set);
        }
        return disjointSets;
    }

}
