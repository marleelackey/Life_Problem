/**
 * HW 4 Question 4
 * @author Marlee Lackey
 * In this project, I coded over zoom with my classmate Taryn, so we handled most of the big problems together,
 * but I focused heavily on the later sections of checking the grid to find what position you are in 
 *
 */

/* imports */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.RandomAccessFile;

public class life {

	public static void main (String args[]) {
		
		
		int a = 0; /* used to index array of data strings */
		String data = null;  /* used to hold 1 line in file */
		String [] dataArray = new String [10]; /* holds all lines in file */
		
		/* read in text file */
		try {
			File sampleFile = new File("DiesMoreSlowly.txt");
			Scanner scan = new Scanner(sampleFile);
			while (scan.hasNext()) { /* will go through entire file */
				/* stores each object in the string to be parsed */
				data = scan.nextLine(); /* stores 1 line of the file into data */
				dataArray[a] = data; /* stores that one line into an array */
				a++;
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("No such file found!");
			e.printStackTrace();
		}
		
		int m = Character.getNumericValue((dataArray[0].charAt(0))); /* gets value of m from string */
		int n = Character.getNumericValue((dataArray[0].charAt(3))); /* gets value of n from string */
		
		int [] [] beforeGrid = new int [m][n];
		int [] [] afterGrid = new int [m][n];
		
		/* set array values */
		int line = 1; /* holds the line number of grid data from file in dataArray */
		int position = 0; /* holds the position in the line of the data */
		
		/* will put the correct values from the file into beforeGrid array */
		for (int i = 0; i < n ; i++ ){
			for (int j = 0; j < m; j++) {
					beforeGrid[i][j] = Character.getNumericValue((dataArray[line].charAt(position)));
					if (position < m  + 2) { /* reset the position, adding 2 to account for spaces */
						position += 2;
					}
					else { /* if at the end of the line, reset the position */
						position = 0;
					}
			}
			line++;
		}
		
		/* will print out inital grid */
		System.out.println("Initial grid: ");
		for (int i = 0; i < n ; i++ ){
			for (int j = 0; j < m ; j++) {
				System.out.print(beforeGrid[i][j]);
			}
			System.out.println();
		}
		/* determine how many generations would you like to run */
		System.out.println("How many generations would you like to run (as an integer)?");
		Scanner scanner = new Scanner(System.in);
		int generation = scanner.nextInt();
		
		/* will check conditions to then make final grid */
		int count = 0;
		while (count < generation) {
			System.out.println("Final grid:  ");
			for (int i = 0; i < n ; i++ ){
				for (int j = 0; j < m ; j++) {
					checkGrid(i,j,m,n,beforeGrid,afterGrid);
					System.out.print(afterGrid[i][j]);
				}
				System.out.println();
			}
		
			for(int k = 0; k < 5; k++) {
				for (int b = 0; b < 5; b++) {
					beforeGrid[k][b] = afterGrid[k][b];
				}
			}
			
			count++;
		} /* end of while loop */
	}
	
	private class GridThread extends Thread {
		
		private int m; /* the m this thread looks at */
		private int n; /* the n this thread looks at */
		
		public void addCoord(int x, int y) {
			m = x;
			n = y;
		}
		
		int[][] beforeGrid = new int[m][n];
		int[][] afterGrid = new int[m][n];
		
		public void addBeforeGrid(int [][] before) {
			beforeGrid = before;
		}
		
		public void addAfterGrid(int [][] after) {
			afterGrid = after;
		}
		
		public void run() {
			checkGrid(m,n,5, 5, beforeGrid, afterGrid);
			System.out.println("Final grid:  ");
			for (int i = 0; i < n ; i++ ){
				for (int j = 0; j < m ; j++) {
					checkGrid(i,j,m,n,beforeGrid,afterGrid);
					System.out.print(afterGrid[i][j]);
				}
				System.out.println();
			}
		}
	}
	

	/* this method will determine where in the grid you are, and direct you to the correct method for checking neighbors */
	private static void checkGrid(int m, int n, int sizeOfM, int sizeOfN, int [][] beforeGrid, int[][] afterGrid) {
		if (m == 0 && n == 0) { /* if in top left corner */
			checkTopLeftCorner(m, n, sizeOfM, sizeOfN, beforeGrid, afterGrid);
		}
				
		else if (m == 0 && n == sizeOfN - 1) { /* if top right corner */
			checkTopRightCorner(m, n, sizeOfM, sizeOfN, beforeGrid, afterGrid);
		}
		
		else if (m == sizeOfM - 1 && n == 0) { /* if bottom left corner */
			checkBotLeftCorner(m, n, sizeOfM, sizeOfN, beforeGrid, afterGrid);
		}
				
		else if (m == sizeOfM - 1 && n == sizeOfN - 1) { /* if bottom right corner */
			checkBotRightCorner(m, n, sizeOfM, sizeOfN, beforeGrid, afterGrid);
		}
				
		else if (n == 0 && m != 0 && m != sizeOfM - 1) { /* if on left edge and not a corner */
			checkLeftNotCorner(m, n, sizeOfM, sizeOfN, beforeGrid, afterGrid);
		}
							
		else if (n == sizeOfN - 1 && m != 0 && m != sizeOfM - 1) { /* if on right edge and not a corner */
			checkRightNotCorner(m, n, sizeOfM, sizeOfN, beforeGrid, afterGrid);
		}	
		
		else if (m == 0 && n != 0 && n != sizeOfN - 1) { /* if top edge and not a corner */
			checkTopNotCorner(m, n, sizeOfM, sizeOfN, beforeGrid, afterGrid);
		}
				
		else if (m == sizeOfM - 1 && n != 0 && n != sizeOfN - 1) { /* if bottom edge and not a corner */
			checkBotNotCorner(m, n, sizeOfM, sizeOfN, beforeGrid, afterGrid);
		}
				
		else { /* if in the center */
			checkNotEdge(m, n, sizeOfM, sizeOfN, beforeGrid, afterGrid);
		}
	}
	
	/* will check the neighbors of the any position that isn't an edge or corner */
	private static void checkNotEdge(int m, int n, int sizeOfM, int sizeOfN, int [][] beforeArray, int [][] afterArray) {
		int valueOfPosition = beforeArray[m][n]; /* 1 or 0 */
		int sum = 0;
		if (beforeArray[m-1][n] == valueOfPosition) {
			sum++;
		}
		if (beforeArray[m-1][n-1] == valueOfPosition) {
			sum++;
		}
		if (beforeArray[m-1][n+1] == valueOfPosition) {
			sum++;
		}
		if (beforeArray[m+1][n] == valueOfPosition) {
			sum++;
		}
		if (beforeArray[m+1][n-1] == valueOfPosition) {
			sum++;
		}
		if (beforeArray[m+1][n+1] == valueOfPosition) {
			sum++;
		}
		if (beforeArray[m][n+1] == valueOfPosition) {
			sum++;
		}
		if (beforeArray[m][n-1] == valueOfPosition) {
			sum++;
		}
	
		if (valueOfPosition == 1) { 	/* if beforeGrid value is a 1 */
			if (sum == 2 || sum == 3) { /* if there are 2 or 3 neighbors as a 1, stay 1 */
				afterArray[m][n] = 1;
			}
			else {
				afterArray[m][n] = 0; /* else, become 0 */
			}
		}
		
		else { /* if beforeGrid value is a 0 */
			if (sum == 5) { /* if there are exactly 3 neighbors as a 1 (aka 5 0's), become 1 */
				afterArray[m][n] = 1;
			}
			else {
				afterArray[m][n] = 0; /* else, stay 0 */
			}
		}
		
	}
	
	/* will check the neighbors of bottom edge positions, excluding the corners */
	private static void checkBotNotCorner(int m, int n, int sizeOfM, int sizeOfN, int [][] beforeArray, int [][] afterArray) {
		int valueOfPosition = beforeArray[m][n];
		int sum = 0;
		if (beforeArray[m-1][n] == valueOfPosition) { 
			sum++;
		}
		if (beforeArray[m-1][n-1] == valueOfPosition) {
			sum++;
		}
		if (beforeArray[m-1][n+1] == valueOfPosition) {
			sum++;
		}
		if (beforeArray[0][n] == valueOfPosition) { /* m +1 = 6, so put 0 instead */
			sum++;
		}
		if (beforeArray[0][n-1] == valueOfPosition) { /* m +1 = 6, so put 0 instead */
			sum++;
		}
		if (beforeArray[0][n+1] == valueOfPosition) { /* m +1 = 6, so put 0 instead */
			sum++;
		}
		if (beforeArray[m][n+1] == valueOfPosition) {
			sum++;
		}
		if (beforeArray[m][n-1] == valueOfPosition) {
			sum++;
		}
	
		if (valueOfPosition == 1) { 	/* if beforeGrid value is a 1 */
			if (sum == 2 || sum == 3) { /* if there are 2 or 3 neighbors as a 1, stay 1 */
				afterArray[m][n] = 1;
			}
			else {
				afterArray[m][n] = 0; /* else, become 0 */
			}
		}
		
		else { /* if beforeGrid value is a 0 */
			if (sum == 5) { /* if there are exactly 3 neighbors as a 1 (aka 5 0's), become 1 */
				afterArray[m][n] = 1;
			}
			else {
				afterArray[m][n] = 0; /* else, stay 0 */
			}
		}
	}
	
	/* will check the neighbors of the top edge positions, excluding the corners */
	private static void checkTopNotCorner(int m, int n, int sizeOfM, int sizeOfN, int [][] beforeArray, int [][] afterArray) {
		int valueOfPosition = beforeArray[m][n];
		int sum = 0;
		if (beforeArray[4][n] == valueOfPosition) { /* m-1 = -1, so 4 instead */
			sum++;
		}
		if (beforeArray[4][n-1] == valueOfPosition) { /* m-1 = -1, so 4 instead */
			sum++;
		}
		if (beforeArray[4][n+1] == valueOfPosition) { /* m-1 = -1, so 4 instead */
			sum++;
		}
		if (beforeArray[m+1][n] == valueOfPosition) {
			sum++;
		}
		if (beforeArray[m+1][n-1] == valueOfPosition) {
			sum++;
		}
		if (beforeArray[m+1][n+1] == valueOfPosition) {
			sum++;
		}
		if (beforeArray[m][n+1] == valueOfPosition) {
			sum++;
		}
		if (beforeArray[m][n-1] == valueOfPosition) {
			sum++;
		}
	
		if (valueOfPosition == 1) { 	/* if beforeGrid value is a 1 */
			if (sum == 2 || sum == 3) { /* if there are 2 or 3 neighbors as a 1, stay 1 */
				afterArray[m][n] = 1;
			}
			else {
				afterArray[m][n] = 0; /* else, become 0 */
			}
		}
		
		else { /* if beforeGrid value is a 0 */
			if (sum == 5) { /* if there are exactly 3 neighbors as a 1 (aka 5 0's), become 1 */
				afterArray[m][n] = 1;
			}
			else {
				afterArray[m][n] = 0; /* else, stay 0 */
			}
		}
	}

	/* will check the neighbors of the right edge positions, excluding the corners */
	private static void checkRightNotCorner(int m, int n, int sizeOfM, int sizeOfN, int [][] beforeArray, int [][] afterArray) {
		int valueOfPosition = beforeArray[m][n];
		int sum = 0;
		if (beforeArray[m-1][n] == valueOfPosition) {
			sum++;
		}
		if (beforeArray[m-1][n-1] == valueOfPosition) {
			sum++;
		}
		if (beforeArray[m-1][0] == valueOfPosition) { /* n + 1 goes to 5 (out of array, so 0 instead */
			sum++;
		}
		if (beforeArray[m+1][n] == valueOfPosition) {
			sum++;
		}
		if (beforeArray[m+1][n-1] == valueOfPosition) {
			sum++;
		}
		if (beforeArray[m+1][0] == valueOfPosition) { /* n + 1 goes to 5 (out of array, so 0 instead */
			sum++;
		}
		if (beforeArray[m][0] == valueOfPosition) { /* n + 1 goes to 5 (out of array, so 0 instead */
			sum++;
		}
		if (beforeArray[m][n-1] == valueOfPosition) {
			sum++;
		}
	
		if (valueOfPosition == 1) { 	/* if beforeGrid value is a 1 */
			if (sum == 2 || sum == 3) { /* if there are 2 or 3 neighbors as a 1, stay 1 */
				afterArray[m][n] = 1;
			}
			else {
				afterArray[m][n] = 0; /* else, become 0 */
			}
		}
		
		else { /* if beforeGrid value is a 0 */
			if (sum == 5) { /* if there are exactly 3 neighbors as a 1 (aka 5 0's), become 1 */
				afterArray[m][n] = 1;
			}
			else {
				afterArray[m][n] = 0; /* else, stay 0 */
			}
		}
	}
   
	/* will check the neighbors of the left edge positions, excluding the corners */
	private static void checkLeftNotCorner(int m, int n, int sizeOfM, int sizeOfN, int [][] beforeArray, int [][] afterArray) {
		int valueOfPosition = beforeArray[m][n];
		int sum = 0;
		if (beforeArray[m-1][n] == valueOfPosition) {
			sum++;
		}
		if (beforeArray[m-1][4] == valueOfPosition) { /* n - 1 = -1, so 4 instead */
			sum++;
		}
		if (beforeArray[m-1][n+1] == valueOfPosition) {
			sum++;
		}
		if (beforeArray[m+1][n] == valueOfPosition) {
			sum++;
		}
		if (beforeArray[m+1][4] == valueOfPosition) { /* n - 1 = -1, so 4 instead */
			sum++;
		}
		if (beforeArray[m+1][n+1] == valueOfPosition) {
			sum++;
		}
		if (beforeArray[m][n+1] == valueOfPosition) {
			sum++;
		}
		if (beforeArray[m][4] == valueOfPosition) { /* n - 1 = -1, so 4 instead */
			sum++;
		}
	
		if (valueOfPosition == 1) { 	/* if beforeGrid value is a 1 */
			if (sum == 2 || sum == 3) { /* if there are 2 or 3 neighbors as a 1, stay 1 */
				afterArray[m][n] = 1;
			}
			else {
				afterArray[m][n] = 0; /* else, become 0 */
			}
		}
		
		else { /* if beforeGrid value is a 0 */
			if (sum == 5) { /* if there are exactly 3 neighbors as a 1 (aka 5 0's), become 1 */
				afterArray[m][n] = 1;
			}
			else {
				afterArray[m][n] = 0; /* else, stay 0 */
			}
		}
	}
	
	/* will check the neighbors of the bottom right corner */
	private static void checkBotRightCorner(int m, int n, int sizeOfM, int sizeOfN, int [][] beforeArray, int [][] afterArray) {
		int valueOfPosition = beforeArray[m][n];
		int sum = 0;
		if (beforeArray[m-1][n] == valueOfPosition) {
			sum++;
		}
		if (beforeArray[m-1][n-1] == valueOfPosition) {
			sum++;
		}
		if (beforeArray[3][0] == valueOfPosition) { /* [m-1][n+1] out of bounds, so 3 0 */
			sum++;
		}
		if (beforeArray[0][4] == valueOfPosition) { /* [m+1][n] Out of bounds so  0 4 */
			sum++;
		}
		if (beforeArray[0][3] == valueOfPosition) { /* [m+1][n-1] Out of bounds so 0 3 */
			sum++;
		}
		if (beforeArray[0][0] == valueOfPosition) { /* [m+1][n+1] out of bounds so 0 0 */
			sum++;
		}
		if (beforeArray[4][0] == valueOfPosition) { /* [m][n+1] out of bounds so 4 0 */
			sum++;
		}
		if (beforeArray[m][n-1] == valueOfPosition) {
			sum++;
		}
	
		if (valueOfPosition == 1) { 	/* if beforeGrid value is a 1 */
			if (sum == 2 || sum == 3) { /* if there are 2 or 3 neighbors as a 1, stay 1 */
				afterArray[m][n] = 1;
			}
			else {
				afterArray[m][n] = 0; /* else, become 0 */
			}
		}
		
		else { /* if beforeGrid value is a 0 */
			if (sum == 5) { /* if there are exactly 3 neighbors as a 1 (aka 5 0's), become 1 */
				afterArray[m][n] = 1;
			}
			else {
				afterArray[m][n] = 0; /* else, stay 0 */
			}
		}
	}

	/* will check the neighbors of the bottom left corner */
	private static void checkBotLeftCorner(int m, int n, int sizeOfM, int sizeOfN, int [][] beforeArray, int [][] afterArray) {
		int valueOfPosition = beforeArray[m][n];
		int sum = 0;
		if (beforeArray[m-1][n] == valueOfPosition) { 
			sum++;
		}
		if (beforeArray[3][4] == valueOfPosition) { /* m-1 n-1 = 3 -1 out of bounds so 3 4 */
			sum++;
		}
		if (beforeArray[m-1][n+1] == valueOfPosition) {
			sum++;
		}
		if (beforeArray[0][0] == valueOfPosition) { /* m+1 n = 5 1 out of bounds so 0 0 */
			sum++;
		}
		if (beforeArray[0][4] == valueOfPosition) { /* m+1 n-1 = 5 -1 out of bounds so 0 4 */
			sum++;
		}
		if (beforeArray[0][1] == valueOfPosition) { /* m+1 n+1 = 5 1 out of bounds so 0 1 */
			sum++;
		}
		if (beforeArray[m][n+1] == valueOfPosition) {
			sum++;
		}
		if (beforeArray[4][4] == valueOfPosition) { /* [m][n-1] out of bounds so  4 4 */
			sum++;
		}
	
		if (valueOfPosition == 1) { 	/* if beforeGrid value is a 1 */
			if (sum == 2 || sum == 3) { /* if there are 2 or 3 neighbors as a 1, stay 1 */
				afterArray[m][n] = 1;
			}
			else {
				afterArray[m][n] = 0; /* else, become 0 */
			}
		}
		
		else { /* if beforeGrid value is a 0 */
			if (sum == 5) { /* if there are exactly 3 neighbors as a 1 (aka 5 0's), become 1 */
				afterArray[m][n] = 1;
			}
			else {
				afterArray[m][n] = 0; /* else, stay 0 */
			}
		}
	}

	/* will check the neighbors of the top right corner */
	private static void checkTopRightCorner(int m, int n, int sizeOfM, int sizeOfN, int [][] beforeArray, int [][] afterArray) {
		int valueOfPosition = beforeArray[m][n];
		int sum = 0;
		if (beforeArray[4][4] == valueOfPosition) { /* [m-1][n] out of bounds so 4  4 */
			sum++;
		}
		if (beforeArray[4][3] == valueOfPosition) { /* [m-1][n-1] out of bounds so  4 3 */
			sum++;
		}
		if (beforeArray[4][0] == valueOfPosition) { /* [m-1][n+1] out of bounds so 4 0 */
			sum++;
		}
		if (beforeArray[m+1][n] == valueOfPosition) {
			sum++;
		}
		if (beforeArray[m+1][n-1] == valueOfPosition) { 
			sum++;
		}
		if (beforeArray[1][0] == valueOfPosition) { /* [m+1][n+1] out of bounds so 1 0 */
			sum++;
		}
		if (beforeArray[0][0] == valueOfPosition) { /* [m][n+1] out of bounds so 0 0 */
			sum++;
		}
		if (beforeArray[m][n-1] == valueOfPosition) { 
			sum++;
		}
	
		if (valueOfPosition == 1) { 	/* if beforeGrid value is a 1 */
			if (sum == 2 || sum == 3) { /* if there are 2 or 3 neighbors as a 1, stay 1 */
				afterArray[m][n] = 1;
			}
			else {
				afterArray[m][n] = 0; /* else, become 0 */
			}
		}
		
		else { /* if beforeGrid value is a 0 */
			if (sum == 5) { /* if there are exactly 3 neighbors as a 1 (aka 5 0's), become 1 */
				afterArray[m][n] = 1;
			}
			else {
				afterArray[m][n] = 0; /* else, stay 0 */
			}
		}
	}

	/* will check the neighbors of the top left corner */
	private static void checkTopLeftCorner(int m, int n, int sizeOfM, int sizeOfN, int [][] beforeArray, int [][] afterArray) {
		int valueOfPosition = beforeArray[m][n];
		int sum = 0;
		if (beforeArray[4][0] == valueOfPosition) { /* [m-1][n] out of bounds so  4 0 */
			sum++;
		}
		if (beforeArray[4][4] == valueOfPosition) { /* [m-1][n-1] out of bounds so  4 4 */
			sum++;
		}
		if (beforeArray[4][1] == valueOfPosition) { /* [m-1][n+1] out of bounds so  4 1 */
			sum++;
		}
		if (beforeArray[m+1][n] == valueOfPosition) {
			sum++;
		}
		if (beforeArray[1][4] == valueOfPosition) { /* [m+1][n-1] out of bounds so  1 4 */
			sum++;
		}
		if (beforeArray[m+1][n+1] == valueOfPosition) { 
			sum++;
		}
		if (beforeArray[m][n+1] == valueOfPosition) {
			sum++;
		}
		if (beforeArray[0][4] == valueOfPosition) { /* [m][n-1] out of bounds so  0 4 */
			sum++;
		}
	
		if (valueOfPosition == 1) { 	/* if beforeGrid value is a 1 */
			if (sum == 2 || sum == 3) { /* if there are 2 or 3 neighbors as a 1, stay 1 */
				afterArray[m][n] = 1;
			}
			else {
				afterArray[m][n] = 0; /* else, become 0 */
			}
		}
		
		else { /* if beforeGrid value is a 0 */
			if (sum == 5) { /* if there are exactly 3 neighbors as a 1 (aka 5 0's), become 1 */
				afterArray[m][n] = 1;
			}
			else {
				afterArray[m][n] = 0; /* else, stay 0 */
			}
		}
	}
}
