/* (1)Name: Sumanth Immadi
   (2)Student Id: 807482254
   (3)I take a pledge of honesty that I did not copy/modify from other's codes
   (4)I declare my copyright that no one else should copy/modify the codes.
*/


import java.util.*;
import java.io.*;
/*Name: Sumanth Immadi .
I have worked on this whole assignment from Sep 1 - Sep 5  */
class topSort {
    // Function that performs the DFS for given graph
    private static boolean dfs(int node, int visited[], int pathVisited[], Stack<Integer> st,
            ArrayList<ArrayList<Integer>> adjacencyMatrix) {
        visited[node] = 1;
        pathVisited[node] = 1;
        for (int i : adjacencyMatrix.get(node)) {
            // Returning true if a cycle exists
            if (visited[i] == 0) {
                if (dfs(i, visited, pathVisited, st, adjacencyMatrix)) {
                    return true;
                }
            } else if (pathVisited[i] == 1) {
                return true;
            }
        }
        // Pushing the visited nodes to stack
        st.push(node);
        pathVisited[node] = 0;
        return false;
    }

    // Function that forms the adjacency matrix based on provided edges
    static void addEdge(ArrayList<ArrayList<Integer>> adjacencyMatrix, int u, int v) {
        adjacencyMatrix.get(u).add(v);
    }

    // Function that returns the topologically sorted list of vertices
    static int[] topologicalSort(int V, ArrayList<ArrayList<Integer>> adjacencyMatrix, int[] inDegree) {
        int visited[] = new int[V];
        int pathVisited[] = new int[V];
        Stack<Integer> st = new Stack<Integer>();
        int res[] = new int[V];
        for (int i = 0; i < V; i++) {
            if (visited[i] == 0)
                // Checks if there is a cycle in the graph
                if (dfs(i, visited, pathVisited, st, adjacencyMatrix)) {
                    int flag = 0;
                    // Printing the vertices that have 0 in-degree
                    for (int j = 0; j < V; j++)
                        if (inDegree[j] == 0) {
                            flag = 1;
                            System.out.print(j + " -> ");
                        }
                    if (flag == 0)
                        System.out.println("No in-degree 0 vertex; not an acyclic graph.");
                    else
                        System.out.println("no more in-degree 0 vertex; not an acyclic graph.");
                    return null;
                }
        }
        int i = 0;
        // Forming topologically sorted list if graph is acyclic
        while (!st.isEmpty()) {
            res[i++] = st.peek();
            st.pop();
        }
        return res;
    }

    // Main method that processes input and prints topological order
    public static void main(String[] args) throws IOException {
        String edges;

        // Validating user input
        if (args.length == 0) {
            System.out.println("Please enter a file name.");
            return;
        }

        // Reading the file provided by user
        FileReader fr = new FileReader(args[0]);
        BufferedReader br = new BufferedReader(fr);

        int numberOfGraphs = Integer.parseInt(br.readLine().split("\\s+")[0]);

        System.out.println("Topological Orders: ");
        // Iterating through the number of graphs
        for (int i = 0; i < numberOfGraphs; i++) {
            ArrayList<ArrayList<Integer>> adjacencyMatrix = new ArrayList<>();
            br.readLine();
            String vertices[] = br.readLine().split("\\s+");
            int V = Integer
                    .parseInt(vertices[vertices.length - 1].substring(0, vertices[vertices.length - 1].length() - 1))
                    + 1;
            int inDegree[] = new int[V];
            // Initialising adjacency matrix and inDegree array
            for (int j = 0; j < V; j++) {
                adjacencyMatrix.add(new ArrayList<>());
                inDegree[j] = 0;
            }
            br.readLine();
            edges = br.readLine();
            // Extracting edges from graph
            while (!edges.contains("---")) {
                String numbers[] = edges.split(", ");
                numbers[0] = numbers[0].trim().substring(1, numbers[0].trim().length());
                numbers[1] = numbers[1].trim();

                int u = Integer.parseInt(numbers[0].trim());
                int v;
                if (numbers[1].charAt(numbers[1].length() - 1) == '}')
                    v = Integer.parseInt(numbers[1].substring(0, numbers[1].length() - 3));
                else
                    v = Integer.parseInt(numbers[1].substring(0, numbers[1].length() - 1));
                // Forming adjaccency matrix
                addEdge(adjacencyMatrix, u, v);
                inDegree[v] = 1;
                edges = br.readLine();
            }
            System.out.print("G" + (i + 1) + ": ");
            int res[] = topologicalSort(V, adjacencyMatrix, inDegree);
            // Printing list of vertices when graph is acyclic
            if (res != null) {
                for (int k = 0; k < V; k++)
                    System.out.print(res[k] + " ");
                System.out.println();
            }
        }
        System.out.println("\n*** Asg 1 by Sumanth Immadi.\n");
        // Closing Buffered Reader
        br.close();
    }
}


