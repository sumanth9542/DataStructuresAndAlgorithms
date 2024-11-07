/* (1)Name: Sumanth Immadi
   (2)Student Id: 807482254
   (3)I take a pledge of honesty that I did not copy/modify from other's codes
   (4)I declare my copyright that no one else should copy/modify the codes.
*/


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class RNAfolding {
    private static int basePair = 0;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        // Get the input file name from command line arguments
        String fileName = args[0];
        int count = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                basePair = 0; // Reset the basePair count for each RNA sequence
                // Check if the line is not empty and doesn't start with '*'
                if (!line.trim().isEmpty() && line.charAt(0) != '*') {
                    int s;
                    int e;
                    char need;
                    int prev = 0;
                    int m;
                    int val;
                    int[][] optValues = new int[line.length()][line.length()];
                    int[][] optChoices = new int[line.length()][line.length()];
                    for (int i = 0; i < line.length(); i++) {
                        for (int j = 0; j < line.length(); j++) {
                            optValues[i][j] = 0; // Initialize the array to store optimal values
                            optChoices[i][j] = 0; // Initialize the array to store optimal choices
                        }
                    }
                    for (int i = 0; i < line.length() - 4; i++) {
                        for (int j = 5; j < line.length(); j++) {
                            s = j - 5;
                            e = j + i;
                            if (e >= line.length()) {
                                break;
                            }
                            if (line.charAt(e) == 'A') {
                                need = 'U';
                            } else if (line.charAt(e) == 'U') {
                                need = 'A';
                            } else if (line.charAt(e) == 'C') {
                                need = 'G';
                            } else {
                                need = 'C';
                            }

                            int temp;
                            prev = optValues[s][e - 1];
                            if (e - s - prev <= 5 && i > 0) {
                                optValues[s][e] = prev;
                                optChoices[s][e] = 0;
                            } else {
                                int tempMax = 0;
                                int tempm = 0;
                                for (int k = s; k <= e - 5; k++) {
                                    m = k;
                                    if (line.charAt(k) == need) {
                                        if (m > 0)
                                            temp = 1 + optValues[s][m - 1] + optValues[m + 1][e - 1];
                                        else
                                            temp = 1 + 0 + optValues[m + 1][e - 1];
                                    } else {
                                        temp = 0;
                                    }
                                    if (temp > tempMax) {
                                        tempMax = temp;
                                        tempm = m;
                                    }
                                }
                                if (prev >= tempMax) {
                                    optValues[s][e] = prev;
                                    optChoices[s][e] = 0;
                                } else {
                                    optValues[s][e] = tempMax;
                                    optChoices[s][e] = tempm + 1;
                                }
                            }
                        }
                    }

                    System.out.println(
                            "** RNA-" + count + ", length=" + line.length() + ", Optimal secondary structure:");

                    // Print RNA secondary structure and count base pairs
                    printRNAPairs(optChoices, 0, line.length() - 1, line);
                    System.out.println("Total number of base pairs:" + basePair);
                    System.out.println();
                    count++;
                }
            }
        } catch (IOException io) {
            // Handle any IOException that might occur
        }
        System.out.println("All by Sumanth Immadi"); 
    }

    /**
     * Recursively print RNA base pairs and update the basePair count.
     * 
     * @param optChoices 2D array of choices
     * @param start      Starting index of the RNA sequence
     * @param end        Ending index of the RNA sequence
     * @param line       RNA sequence
     */
    public static void printRNAPairs(int[][] optChoices, int start, int end, String line) {
        if (start >= end) {
            return;
        }

        int choice = optChoices[start][end];
        if (choice == 0) {
            printRNAPairs(optChoices, start, end - 1, line);
        } else {
            int pairedBase = choice - 1;
            System.out.println(
                    line.charAt(optChoices[pairedBase][end] - 1) + "-" + line.charAt(end) + " (" + (pairedBase + 1)
                            + ","
                            + (end + 1)
                            + ")");
            basePair++;
            printRNAPairs(optChoices, start, pairedBase - 1, line);
            printRNAPairs(optChoices, pairedBase + 1, end - 1, line);
        }
    }
}

