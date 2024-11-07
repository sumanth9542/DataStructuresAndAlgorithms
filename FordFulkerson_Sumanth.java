import java.io.*;
import java.util.Date;

public class FordFulkerson {
	// Function to find a path using BFS in the residual graph
	public static boolean findPath(int[][] residue, int[] currentPath, int V) {
		boolean[] visited = new boolean[V];
		int[] queue = new int[V];
		int q = -1;
		int src = 0;
		int dest = V - 1;
		q++;
		queue[q] = src;
		visited[q] = true;
		currentPath[q] = -1;

		// BFS to find an augmenting path in the residual graph
		
		while (q >= 0 & q < V) {
			int u = queue[q];
			q--;
			for (int v = 0; v < V && q < V; v++) {
				if (!visited[v]&& residue[u][v] >= 1) {
					currentPath[v] = u;
					if (v == dest) {
						return true; // Path found
					}
					visited[v] = true;
					q++;
					queue[q] = v;
				}
			}
		}
		return false; //path not found
	}
	
	// Function to print the maximum flow in the flow network
	public static int printMaxFlow(int[][] g, int V) {
		int src = 0;
		int dest = V - 1;
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
		while (findPath(residue, currentPath, V)) {
			maxPathFlow = Integer.MAX_VALUE;
			int end = dest;
			int start = end;

			// Find the minimum residual capacity in the augmenting path
			while (start != src) {
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
			while (start != src) {
				start = currentPath[end];
				residue[start][end] -= maxPathFlow;
				residue[end][start] += maxPathFlow;
				maxflow[start][end] = residue[start][end];
				maxflow[end][start] += maxPathFlow;
				end = currentPath[end];
			}
			maxFlow = maxFlow + maxPathFlow;
		}
		
		// Print the flow network and maximum flow if V is small

		if (V <= 10) {
			System.out.print("Flow network:\n");
			System.out.print("\t  ");
			for (int i = 0; i < V; i++) {
			    System.out.print("    "+ i + ":");
			}
			System.out.println();
			System.out.println("\t" + "-".repeat(7*V));
			
			for (int i = 0; i < V; i++) {
				System.out.print("\t"+ i + ":");
				for (int j = 0; j < V; j++) {
					String output = (g[i][j] > 0) ? String.format("%6d", g[i][j]) : String.format("%6s", "-");
					System.out.print(output);
				}
				System.out.println();
			}
			
			System.out.println();
			System.out.println("Maximum flow:");
			System.out.print("\t  ");
			for (int i = 0; i < V; i++) {
			    System.out.print("    "+ i + ":");
			}
			System.out.println();
			System.out.println("\t" + "-".repeat(7*V));
			
			for (int j = 0; j < V; j++) {
				System.out.print("\t" + j + ":");
				for (int i = 0; i < V; i++) {
					String output = (residue[i][j] > 0 && g[j][i] > 0 && foundInPath[j][i] == 1) ? String.format("%6d", maxflow[i][j]) : String.format("%6s", "-");
        System.out.print(output);
				}
				System.out.println();
			}
		}
		return maxFlow;
	}

	// Main function to read input, execute the algorithm, and print results

    public static void main(String[] args) throws IOException {
		System.out.println();
		System.out.println("FordFulkerson algorithm");
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
                    	int ans = printMaxFlow(g, V);
                    	Date end = new Date();
                    	System.out.println("  Max Flow ==> " + ans + " (" + (end.getTime() - begin.getTime()) + " ms)");
                    	break;
                    }
                    if (line.startsWith("(u")) continue;
                    line = line.replaceAll(" ", "");
                    line = line.split("\\)")[0];
                    line = line.split("\\(")[1];
                    String[] data = line.split(",");
            		int u = Integer.parseInt(data[0]);
                    int v = Integer.parseInt(data[1]);
                    int weight = Integer.parseInt(data[2]);
                    g[u][v] = weight;
                }

                
            }
        }

		System.out.println("asg by Sumanth Immadi");

        reader.close();
    }
}
