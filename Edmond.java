import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Define a class to represent an edge in a graph
class Edge {
    int begin;         // begin vertex of the edge
    int end;    // end vertex of the edge
    double reward_per_edge;      // reward_per_edge or cost associated with the edge

    Edge(int begin, int end, double reward_per_edge) {
        this.begin = begin;
        this.end = end;
        this.reward_per_edge = reward_per_edge;
    }
}

public class Edmond {
    private int nodes;        // Number of nodes in the graph
    private List<Edge> edges;    // List to store edges in the graph

    // Method to add an edge to the graph
    public void insertEdge(int begin, int end, double reward_per_edge) {
        Edge edge = new Edge(begin, end, reward_per_edge);
        edges.add(edge);
    }

    public Edmond(int nodes) {
        this.nodes = nodes;
        this.edges = new ArrayList<>(); // Load the list of edges
    }

    // Load parent array for Bellman-Ford algorithm
    private int[] LoadParents() {
        int[] parents = new int[nodes];
        for (int i = 0; i < nodes; i++) {
            parents[i] = -1;
        }
        return parents;
    }

    // Load distances for Bellman-Ford algorithm
    private double[] LoadDistances() {
        double[] distances = new double[nodes];
        for (int i = 0; i < nodes; i++) {
            distances[i] = Double.POSITIVE_INFINITY;
        }
        distances[0] = 0;
        return distances;
    }

    // Run the Bellman-Ford algorithm to find shortest paths
    private boolean runBellmanFord(int begin, double[] distances, int[] parents) {
        for (int i = 0; i < nodes - 1; i++) {
            for (Edge edge : edges) {
                relax(edge, distances, parents);
            }
        }

        for (Edge edge : edges) {
            int beginVertex = edge.begin;
            int endVertex = edge.end;
            double reward_per_edge = edge.reward_per_edge;
            if (distances[beginVertex] != Double.POSITIVE_INFINITY && distances[beginVertex] + reward_per_edge < distances[endVertex]) {
                return false; // Negative reward_per_edge cycle detected
            }
        }
        return true;
    }

    // Relaxation step in Bellman-Ford algorithm
    private boolean relax(Edge edge, double[] distances, int[] parents) {
        int begin = edge.begin;
        int end = edge.end;
        double reward_per_edge = edge.reward_per_edge;
        if (distances[begin] != Double.POSITIVE_INFINITY && distances[begin] + reward_per_edge < distances[end]) {
            distances[end] = distances[begin] + reward_per_edge;
            parents[end] = begin;
            return true;
        }
        return false;
    }

    // Construct the minimum arborescence (a directed tree) from parent array
    private List<Edge> constructArborescence(int[] parents) {
        List<Edge> arborescence = new ArrayList<>();
        for (int i = 1; i < nodes; i++) {
            int begin = parents[i];
            int end = i;
            double reward_per_edge = getEdgereward_per_edge(begin, end);
            arborescence.add(new Edge(begin, end, reward_per_edge));
        }
        return arborescence;
    }

    // Get the reward_per_edge of an edge given begin and end nodes
    private double getEdgereward_per_edge(int begin, int end) {
        for (Edge edge : edges) {
            if (edge.begin == begin && edge.end == end) {
                return edge.reward_per_edge;
            }
        }
        return Double.POSITIVE_INFINITY;
    }

    // Find the minimum arborescence rooted at a designated vertex
    public void findMinimumArborescence(int designatedVertex) {
        double[] distances = LoadDistances();
        int[] parents = LoadParents();

        if (!runBellmanFord(0, distances, parents)) {
            System.out.println("Negative reward_per_edge cycle detected. Cannot find minimum arborescence.");
            return;
        }

        List<Edge> arborescence = constructArborescence(parents);
        double totalreward_per_edge = calculateTotalreward_per_edge(arborescence);

        System.out.println("Minimum Arborescence rooted at vertex " + designatedVertex + ":");
        System.out.println();
        for (Edge edge : arborescence) {
            System.out.println("(" + edge.begin + ", " + edge.end + ", " + edge.reward_per_edge + ")");
        }
        System.out.println();
        System.out.println("Total reward_per_edge: " + totalreward_per_edge);
    }

    // Calculate the total reward_per_edge of an arborescence
    private double calculateTotalreward_per_edge(List<Edge> arborescence) {
        double totalreward_per_edge = 0;
        for (Edge edge : arborescence) {
            totalreward_per_edge += edge.reward_per_edge;
        }
        return totalreward_per_edge;
    }

    // Main method to run the program
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide the input file path as a command-line argument.");
            return;
        }

        String filePath = args[0];
        File file = new File(filePath);

        try {
            Scanner scanner = new Scanner(file);
            boolean readingEdges = false;
            int nodes = 0;
            Edmond edmond = null;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.startsWith("** G")) {
                    if (readingEdges) {
                        // Finish processing the previous graph
                        int designatedVertex = 0; // Change this value as per your requirement
                        edmond.findMinimumArborescence(designatedVertex);
                        System.out.println();
                    }

                    nodes = Integer.parseInt(line.split("\\|V\\| = ")[1].trim());
                    edmond = new Edmond(nodes);
                    readingEdges = false;
                } else if (line.contains("(u, v) E = {")) {
                    readingEdges = true;
                } else if (readingEdges && line.contains("(")) {
                    // Parse edge data
                    String[] parts = line.split("\\(|\\)");
                    String[] values = parts[1].split(",");
                    int begin = Integer.parseInt(values[0].trim());
                    int end = Integer.parseInt(values[1].trim());
                    double reward_per_edge = Double.parseDouble(values[2].trim());
                    edmond.insertEdge(begin, end, reward_per_edge);
                }
            }

            // Finish processing the last graph
            if (edmond != null) {
                int designatedVertex = 0; // Change this value as per your requirement
                edmond.findMinimumArborescence(designatedVertex);
                System.out.println();
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
        }
    }
}