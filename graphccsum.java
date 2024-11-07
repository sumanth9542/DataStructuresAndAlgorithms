import java.io.*;
import java.util.*;


class graphccsum {
    // Number of vertices in the graph
    private int V;

    // Edges represented as an ArrayList of ArrayLists
    private ArrayList<ArrayList<Integer>> edges;

    // Constructor for initializing the graph with V vertices
    graphccsum(int v) {
        V = v;
        edges = new ArrayList<ArrayList<Integer>>(v);
        
        // Initialize the adjacency list for each vertex
        for (int i = 0; i < v; ++i)
            edges.add(new ArrayList<Integer>());
    }

    
    // Method to perform breadth-first search (BFS) on the graph
    void bfs() {
        // we initialize data structures for BFS
        List<List<Integer>> components = new ArrayList<>();
        Set<Integer> visit = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();

        //we loop through each vertex in the graph
        for (int v = 0; v < V; v++) {
            if (!visit.contains(v)) {
                List<Integer> component = new ArrayList<>();
                queue.add(v);
                visit.add(v);

                // we explore the connected component using BFS
                while (!queue.isEmpty()) {
                    int u = queue.poll();
                    component.add(u);

                    // we will visit all neighbors of the current vertex
                    for (int neigh : edges.get(u)) {
                        if (!visit.contains(neigh)) {
                            queue.add(neigh);
                            visit.add(neigh);
                        }
                    }
                }
                components.add(component);
            }
        }

        // we will print the connected components found by BFS
        System.out.println("      Breadth First Search:");
        System.out.print("            ");
        for (List<Integer> x : components) {
            for (Integer y : x) {
                System.out.print(y + " ");
            }
            System.out.println();
            System.out.print("            ");
        }
    }

    static void dfs(graphccsum graph) {
    
    List<List<Integer>> components = new ArrayList<>();
    Set<Integer> visit = new HashSet();
    int v = 0; // we will initialize the vertex index

    // we  use a while loop to iterate through vertices in the graph
    while (v < graph.V) {
        if (!visit.contains(v)) {
            List<Integer> component = new ArrayList<>(); // List to store the current connected component
            dfsVisitNeighbour(v, visit, component, graph); // we will perform DFS starting from the current vertex
            components.add(component); // we will add the component to the list of components
        }
        v++; 
    }

    //we will print a blank line and a header for the Depth First Search (DFS) result
    System.out.println();
    System.out.println("       Depth First Search:");
    System.out.print("            ");

    // we will use a while loop to iterate through connected components found by DFS
    Iterator<List<Integer>> iterator = components.iterator();
    while (iterator.hasNext()) {
        List<Integer> x = iterator.next();
        // we will use an iterator to loop through the vertices in the current component
        Iterator<Integer> vertexIterator = x.iterator();
        while (vertexIterator.hasNext()) {
            Integer y = vertexIterator.next();
            System.out.print(y + " "); // we print the vertex
        }
        System.out.println(); // we will move to the next line for formatting as requested
        System.out.print("            "); // we are printing indentation for formatting as requested
    }
}


static void dfsVisitNeighbour(int u, Set<Integer> visit, List<Integer> component, graphccsum graph) {
    visit.add(u); // we will mark the current vertex as visited
    component.add(u); // we will add the current vertex to the current component

    // we will use a while loop to recursively visit all neighbors of the current vertex
    Iterator<Integer> neighborIterator = graph.edges.get(u).iterator();
    while (neighborIterator.hasNext()) {
        int n = neighborIterator.next();
        if (!visit.contains(n)) {
            dfsVisitNeighbour(n, visit, component, graph); // we will recursively call to visit the neighbor
        }
    }
}


    
    public static void main(String args[]) {
        // We will initialize a file reader to read input data from a file
        File inputFile = null;

        // We will read command-line arguments, if provided
        if (0 < args.length) {
            inputFile = new File(args[0]);
        }

        BufferedReader br = null;

        try {
            String start;

            // We will read data from the file and convert it into a BufferedReader
            br = new BufferedReader(new FileReader(inputFile));
            int flag = 0;
            graphccsum graph = null;

            // we will read lines from the file
            while ((start = br.readLine()) != null) {
                String t = start;
                int c = 0;

                // we will skip empty lines and lines starting with 'N'
                if (t.trim().equals(""))
                    continue;
                if (t.trim().charAt(3) == 'N')
                    continue;

                // we will check for lines starting with '*'
                if (t.trim().charAt(0) == '*') {
                    String[] a = t.trim().split("[: ,}]+");
                    String f = a[1];
                    System.out.println("** " + f + "'s connected components");
                    String b = a[a.length - 1];
                    c = Integer.parseInt(b);
                    graph = new graphccsum(c + 1);
                } else if (t.trim().charAt(1) == 'u') {
                    flag = 1;
                } else if (flag == 1) {
                    if (t.trim().charAt(0) == '-') {
                        flag = 0;
                        // we will perform BFS and DFS on the graph
                        graph.bfs();
                        dfs(graph);
                        System.out.println();
                        continue;
                    }
                    // we will add edges to the graph
                    String[] d = t.trim().split("[ (,)]+");

                    //we will add an edge from d[1] to d[2] 
                    graph.edges.get(Integer.parseInt(d[1])).add(Integer.parseInt(d[2]));
                    graph.edges.get(Integer.parseInt(d[2])).add(Integer.parseInt(d[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}









// Time Complexity (TC):-

    

//     For BFS method the outer loop iterates through each vertex in the graph, which takes O(V) time, where V is the number of vertices.

//     Inside the loop, BFS is performed on each unvisited component. In the worst case, BFS can visit all vertices and edges once, resulting in a time complexity of O(V + E), where E is the number of edges.

//     Therefore, the overall time complexity of bfs is O(V^2 + VE).

//     For DFS method also, the outer loop iterates through each vertex in the graph, which takes O(V) time.

//     Inside the loop, DFS is performed on each unvisited component. In the worst case, DFS can visit all vertices and edges once, resulting in a time complexity of O(V + E).

//     Therefore, the overall time complexity of dfs is O(V^2 + VE).

//     So, the overall time complexity of code is O(V^2 + VE).

// Space Complexity (SC):-

    
//     The edges ArrayList stores the graph's adjacency lists, which consumes O(V + E) space.
//     The components List stores the connected components found during BFS or DFS, which can consume up to O(V) space in the worst case.
//     The visit Set is used to keep track of visited vertices, consuming O(V) space.
//     The queue used in BFS can consume up to O(V) space in the worst case.
//     Therefore, the overall space complexity is O(V + E).

