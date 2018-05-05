package gaussian;

import java.io.*;
import java.util.*;

public class Gaussian {
	static ArrayList<Integer> rows;

	public static void main(String[] args) throws FileNotFoundException {
		Scanner scan = new Scanner(System.in);

		int num;
		String choice;
		double[][] eqs;
		
		System.out.println("Gaussian Elimination");

		while (true) {
			System.out.print("Enter number of linear equations to solve: ");
			num = scan.nextInt();

			System.out.println("Choose an option from the following:");
			System.out.println("1. Manually input coefficients");
			System.out.println("2. Input coefficients from a file");
			System.out.println("3. Generate random coefficients");

			scan.nextLine();
			choice = scan.nextLine();

			eqs = new double[num][num + 1];
			rows = new ArrayList<>();
			
			switch (choice) {
			case "1":
				userInput(num, eqs, scan);
				solve(num, eqs);
				break;
			case "2":
				fileInput(num, eqs, scan);
				solve(num, eqs);
				break;
			case "3":
				genRandom(num, eqs);
				solve(num, eqs);
				break;
			default:
				System.out.println("Invalid input.");
				break;
			}

			System.out.print("Would you like to try again? (y/n): ");
			choice = scan.nextLine();
			choice.toLowerCase();

			if (choice.matches("n|N")) {
				System.out.println("Goodbye.");
				break;
			}
		}
		scan.close();
	}

	public static void userInput(int n, double[][] eq, Scanner in) {

		for (int i = 0; i < n; i++) {
			for (int j = 0; j <= n; j++) {
				if (j == n) {
					System.out.print("Enter the right hand side for equation " + (i + 1) + " : ");
				} else
					System.out.print("Enter coefficient c" + (i + 1) + "" + (j + 1) + ": ");

				eq[i][j] = in.nextDouble();
			}
		}
		in.nextLine();
	}

	public static void fileInput(int n, double[][] eq, Scanner in) throws FileNotFoundException{
		String fileName;

		in = new Scanner(System.in);

		while (true) {
			System.out.print("Enter the name of the file: ");
			fileName = in.nextLine();

			File inputFile = new File(fileName);

			if (!inputFile.exists()) {
				System.out.println("File does not exist.");
			} else {
				in = new Scanner(inputFile);
				break;
			}
		}

		for (int i = 0; i < n; i++) {
			for (int j = 0; j <= n; j++) {
				eq[i][j] = in.nextDouble();
			}
		}
		
		in.close();
	}
	
	public static void genRandom(int n, double[][] eq) {
		Random rand = new Random();
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j <= n; j++) { 
				eq[i][j] = rand.nextInt(10) + 1;
			}
		}
	}

	public static void solve(int n, double[][] eq) {
		boolean[] visited = new boolean[n];
		double[] maxVector = new double[n];
		double[] scaleVector;
		ArrayList<Integer> backSeq = new ArrayList<>();
		int pivRow, scaleInd;

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (eq[i][j] > maxVector[i])
					maxVector[i] = eq[i][j];
			}
			rows.add(i);
		}

		for (int i = 0; i < (n - 1); i++) {
			pivRow = 0;
			scaleInd = 0;
			scaleVector = new double[n - i];

			for (int j = 0; j < n; j++) {
				if (!visited[j]) {
					scaleVector[scaleInd] = Math.abs(eq[j][i] / maxVector[j]);

					if (scaleVector[scaleInd++] > scaleVector[pivRow])
						pivRow = j;
				}
			}

			visited[pivRow] = true;
			backSeq.add(pivRow);
			rows.remove(rows.indexOf(pivRow));
			elim(i, n, pivRow, eq, visited);
		}

		System.out.println();

		backSeq.add(rows.get(0));
		Collections.reverse(backSeq);
		backSub(n, eq, backSeq);
	}
	
	public static void elim(int begin, int n, int piv, double[][] eq, boolean[] v) {
		double temp;

		for (int i = 0; i < n; i++) {
			if (!v[i]) {
				temp = eq[i][begin];

				for (int j = begin; j <= n; j++)
					eq[i][j] = eq[i][j] - (eq[piv][j] * (temp / eq[piv][begin]));
			}
		}
	}

	public static void backSub(int n, double[][] eq, ArrayList<Integer> seq) {
		double[] solVector = new double[n];

		for (int i = 0; i < n; i++) {
			solVector[n - (i + 1)] = eq[seq.get(0)][n];

			for (int j = n - 1; j >= n - (i + 1); j--) {
				if (j == n - (i + 1)) {
					solVector[n - (i + 1)] /= eq[seq.get(0)][j];
				} else
					solVector[n - (i + 1)] += -1 * (eq[seq.get(0)][j] * solVector[j]);
			}
			seq.remove(0);
		}

		for (int i = 0; i < n; i++)
			System.out.println("x" + (i + 1) + " = " + solVector[i]);
	}
}