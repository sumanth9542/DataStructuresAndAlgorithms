import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

public class preflowPush {

    // Function to print an array
    public static void printArr(int[] ef) {
        for (int i = 0; i < ef.length; i++) {
            System.out.print(ef[i] + " ");
        }
        System.out.println();
    }

    // Function to print the flow matrix
    public static void printFlow(int[][] flow) {
        int V = flow.length;
        for (int j = 0; j < V; j++) {
            System.out.printf("%6d:", j);
            for (int i = 0; i < V; i++) {
                System.out.printf("%6d", flow[i][j]);
            }
            System.out.println();
        }
    }

    // Function to get a vertex with excess flow
    public static int getefV(int[][] g, int[][] flow, int[] ef, int V) {
        int x = -1;
        for (int i = 1; i < V - 1; i++) {
            if (ef[i] > 0) {
                x = i;
                return x;
            }
        }
        return x;
    }

    // Function to push flow from a vertex with excess flow
    public static boolean pushFlow(int[][] g, int[] h, int[] ef, int[][] flow, int V, int efV) {
        boolean pushed = false;
        for (int i = 0; i < V; i++) {
            if (g[efV][i] != 0) {
                if (g[efV][i] == flow[efV][i]) continue;
                if (h[efV] > h[i]) {
                    int currentFlow = ef[efV];
                    if (g[efV][i] - flow[efV][i] < currentFlow && g[efV][i] - flow[efV][i] > 0) {
                        currentFlow = g[efV][i] - flow[efV][i];
                    }
                    ef[efV] -= currentFlow;
                    ef[i] += currentFlow;
                    flow[efV][i] += currentFlow;
                    flow[i][efV] -= currentFlow;
                    pushed = true;
                }
            }
            if (g[i][efV] != 0) {
                if (flow[i][efV] > 0 && h[efV] > h[i]) {
                    int currentFlow = ef[efV];
                    if (flow[i][efV] - currentFlow < 0) {
                        currentFlow = flow[i][efV];
                    }
                    ef[efV] -= currentFlow;
                    ef[i] += currentFlow;
                    flow[i][efV] -= currentFlow;
                    pushed = true;
                    return true;
                }
            }
        }
        return pushed;
    }

    // Function to increase height of a vertex
    public static void increaseH(int[][] g,  int[][] flow, int[] h, int V, int efV) {
        int minConnectedV = Integer.MAX_VALUE;
        for (int i = 0; i < V; i++) {
            if (g[efV][i] != 0) {
                if (g[efV][i] == flow[efV][i])
                    continue;
                if (h[i] < minConnectedV) {
                    minConnectedV = h[i];
                    h[efV] = minConnectedV + 1;
                }
            } else if (g[i][efV] != 0) {
                if (flow[i][efV] > 0 && h[i] < minConnectedV) {
                    minConnectedV = h[i];
                    h[efV] = minConnectedV + 1;
                }
            }
        }
    }

    // Function to find and print the maximum flow in the network
    public static int printMaxFlow(int[][] g, int[] h, int[] ef, int[][] flow, int V) {
        int src = 0;
        int dest = V - 1;
        h[src] = V;
        int efV = -1;
        for (int i = src + 1; i < V; i++) {
            if (g[src][i] != 0) {
                flow[src][i] = g[src][i];
                ef[i] += flow[src][i];
                flow[i][src] -= flow[src][i];
            }
        }
        efV = getefV(g, flow, ef, V);
        while (efV != -1) {
            boolean pushed = pushFlow(g, h, ef, flow, V, efV);
            if (!pushed) {
                increaseH(g, flow, h, V, efV);
            }
            efV = getefV(g, flow, ef, V);
        }

        // Print the flow network and maximum flow if the graph size is small
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

            for (int i = 0; i < V; i++) {
                System.out.print("\t" + i + ":");
                for (int j = 0; j < V; j++) {
                    String output = (flow[i][j] > 0) ? String.format("%6d", flow[i][j]) : String.format("%6s", "-");
                    System.out.print(output);
                }
                System.out.println();
            }
        }
        return ef[V-1];
    }

    // Main method
    public static void main(String[] args) throws IOException {
        System.out.println();
        System.out.println("Preflow-Push algorithm");
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
                        int[] h = new int[V];
                        int[] ef = new int[V];
                        int[][] flow = new int[V][V];
                        Date begin = new Date();
                        int ans = printMaxFlow(g, h, ef, flow, V);
                        Date end = new Date();
                        System.out.println("  Max Flow ==> " + ans + " (" + (end.getTime() - begin.getTime()) + " ms)");
                        break;
                    }
                    if (line.startsWith("(u")) continue;
                    line = line.replaceAll(" ", "");
                    line = line.split("\\)")[0];
                    line = line.split("\\(")[1];
                    String[] data = line.split(",");
                    int u = Integer.parseInt(data[0].trim());
                    int v = Integer.parseInt(data[1].trim());
                    int weight = Integer.parseInt(data[2].trim());
                    g[u][v] = weight;
                }
            }
        }

        System.out.println("asg by Sumanth Immadi");

        reader.close();
    }
}
