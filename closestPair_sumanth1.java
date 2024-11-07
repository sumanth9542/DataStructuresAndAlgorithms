/*
 * 
 */
import java.io.*;
import java.time.*;
import java.util.*;

class P implements Comparator<Point> {
	public int compare(Point p1, Point p2) {
		return Double.compare(p1.getP(), p2.getP());
	}
}

class Q implements Comparator<Point> {
	public int compare(Point p1, Point p2) {
		return Double.compare(p1.getQ(), p2.getQ());
	}
}

class Point {
	private Double p = null;
	private Double q = null;

	public Point() {
	}

	public Point(Double p, Double q) {
		this.p = p;
		this.q = q;
	}

	public Double getP() {
		return this.p;
	}

	public Double getQ() {
		return this.q;
	}

	public Double distance(Point Y) {
		Double part1 = Math.pow(this.p - Y.getP(), 2);
		Double part2 = Math.pow(this.q - Y.getQ(), 2);
		Double dis = Math.sqrt(part1 + part2);
		return ((double) Math.round(dis * 1000000)) / 1000000;
	}

}

class CPair {
	public Point p1;
	public Point p2;
	public Double distance;

	public CPair() {
		this.p1 = new Point();
		this.p2 = new Point();
		this.distance = Double.MAX_VALUE;
	}

	public void updatePair(Point p1, Point p2, Double distance) {
		this.p1 = p1;
		this.p2 = p2;
		this.distance = distance;
	}

	public void printPair() {
		System.out.print("  (" + p1.getP() + ", " + p1.getQ() + ")-(" + p2.getP() + ", " + p2.getQ() + ")\n");
		System.out.print("  distance = " + distance);
	}
}

public class closestPair_sumanth {

	public static List<Point> xypoints = new ArrayList<>();
	public static CPair cp = new CPair();

	public static void closestPairBruteForce(List<Point> xypoints, int i, int j) {
		while (i < j) {
			int t = i + 1;
			while (t < j) {
				Double newD = xypoints.get(i).distance(xypoints.get(t));
				if (cp.distance > newD) {
					cp.updatePair(xypoints.get(i), xypoints.get(t), newD);
				}
				t++;
			}
			i++;
		}
	}

	public static void _closestPair(List<Point> xypoints, int i, int j) {
		if (j <= i + 3) {
			// brute force if there are only 3 points
			closestPairBruteForce(xypoints, i, j);
			return;
		}
		int k = (i + j) / 2;
		_closestPair(xypoints, i, k); // left half iteration
		_closestPair(xypoints, k, j); // right half iteration
		List<Point> strip = new ArrayList<>();
		for (int t = i; t < j; t++) {
			if (cp.distance >= Math.abs(xypoints.get(k).getP() - xypoints.get(t).getP())) {
				strip.add(xypoints.get(t));
			}
		}
		// sort by y axis
		strip.sort(new Q());
		for (int r = 0; r < strip.size(); r++) {
			for (int s = r + 1; s < strip.size(); s++) {
				if (cp.distance < Math.abs(strip.get(r).getQ() - strip.get(s).getQ())) {
					break;
				}

				Double newD = strip.get(r).distance(strip.get(s));
				if (cp.distance > newD) {
					cp.updatePair(strip.get(r), strip.get(s), newD);
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader inputBuffer = new BufferedReader(
				new FileReader(
						new File(args[0])));
		String row;
		// processing the input
		while ((row = inputBuffer.readLine()) != null) {
			if (row.charAt(0) == '*') {
				System.out.print(row.substring(3));
				cp.p1 = new Point();
				cp.p2 = new Point();
				cp.distance = Double.MAX_VALUE;
				xypoints.clear();
			} else if (row.charAt(0) == '(') {
				row = row.substring(1, row.length() - 1);
				String[] arr = row.split(", ");
				xypoints.add(new Point(Double.parseDouble(arr[0]), Double.parseDouble(arr[1])));
			} else if (row.charAt(0) == '-') {
				System.out.print(" " + xypoints.size() + " points\n");
				Instant start = Instant.now();
				xypoints.sort(new P());
				_closestPair(xypoints, 0, xypoints.size());
				Instant end = Instant.now();
				cp.printPair();
				long duration = Duration.between(start, end).toMillis();
				System.out.println(" (" + duration + " ms)");
				System.out.println();
			}
		}
		System.out.println("by Sumanth");
	}
}
