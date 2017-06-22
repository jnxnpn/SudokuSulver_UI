//import java.util.Scanner; // UI version will not use it.
package sample;
@SuppressWarnings("unused")
public class Sudoku {

    public Sudoku() {
        // no need for constrctor
    }

    private static int[][] input = new int[9][9];
    private static boolean[][][] possibility = new boolean[9][9][10];
    private static int globalMsg1 = 0;
    private static boolean testMode = false;
    private static boolean hard = false;

    public Sudoku(int[][] in) {
        this.input = in;
        /*Scanner scan = new Scanner(System.in); // UI version will not use scanner, but will depend upon the main class call for input.
        for (int i = 0; i < 9; i++) {
            while (true) {
                System.out.println(i + 1);
                String buff = scan.next();
                if (isNNum(buff) && buff.length() == 9) {
                    for (int u = 0; u < 9; u++) {
                        input[i][u] = Character.getNumericValue(buff.charAt(u));
                    }
                    break;
                }
            }
        }
        // throughSift(); //old program flow idea
        // debugDisplay(); //shows the possibilities
        scan.close();*/
        solve(input);
        if (ifSolved(input) == false) {
            hard = true;
            int hypoNum = 0;
            while (globalMsg1 == 0) {
                hypoNum++;
                int[][] hypothesis = new int[9][9];
                generateHypo(hypoNum, hypothesis);
                solve(hypothesis);
                if (ifSolved(hypothesis) == true) {
                    for (int i = 0; i < 9; i++) {
                        input[i] = hypothesis[i].clone();
                    }
                    break;
                }
            }
        }
        //showCurrentInput(input); // show final answer or whatever it gets
        // debugDisplay();
    }

    private static void waitTime(long time) { // UI version does not use
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void typing(long gap, int loops, int dots, String text) {  // UI version does not use
        System.out.print(text);
        waitTime(gap);
        for (int i = 0; i < loops; i++) {

            for (int u = 0; u < dots; u++) {
                System.out.print(".");
                waitTime(gap);
            }
            waitTime(gap);
            for (int u = 0; u < dots; u++) {
                System.out.print("\b");
            }

        }
        for (int i = 0; i < text.length(); i++) {
            System.out.print("\b");
        }
    }

    private static void solve(int[][] obj) {
        throughSift(obj);
        while (throughSFill(obj) == true || primarativeFill(obj) == true) {
            throughSift(obj);
            // showCurrentInput(); // for debugging
        }
    }

    private static void generateHypo(int root, int[][] loc) {
        throughSift(input);
        // debugDisplay();
        int[][] hypothesis = new int[9][9];

        for (int i = 0; i < 9; i++) {
            hypothesis[i] = input[i].clone();
        }
        // throughSift(hypothesis);

        int index = 0;

        for (int i = 0; i < 9; i++) {
            for (int u = 0; u < 9; u++) {
                for (int v = 1; v < 10; v++) {
                    if (possibility[i][u][v] == true) {
                        index++;
                        // System.out.print(index + "\t");
                        if (index == root) {
                            hypothesis[i][u] = v;
                            break;
                        }
                    }
                }
                if (index == root) {
                    break;
                }
            }
            if (index == root) {
                break;
            }
        }

        if (index < root) {
            globalMsg1 = 1;
        }
        for (int i = 0; i < 9; i++) {
            loc[i] = hypothesis[i].clone();
        }
    }

    private static boolean primarativeFill(int[][] obj) {
        boolean change = false;

        for (int i = 0; i < 9; i++) {
            for (int u = 0; u < 9; u++) {
                int sit = 0; // possible solution
                int theChoice = 0; // the possible solution

                if (possibility[i][u][0] != true) // if undecided
                    for (int v = 1; v < 10; v++) { // then see how many possible
                        // solution
                        if (possibility[i][u][v] == true) {
                            sit++;
                            theChoice = v;
                        }
                    }
                if (sit == 1) {
                    obj[i][u] = theChoice;
                    change = true;
                }
            }
        }
        // throughSift();
        System.out.print(testMode ? 'x' : "");
        return change;

    }

    private static void throughSift(int[][] obj) {

        for (boolean[][] i : possibility) {
            for (boolean[] u : i) {
                for (int v = 1; v < 10; v++)
                    u[v] = true;
            }
        }

        for (int i = 0; i < 9; i++) {

            for (int u = 0; u < 9; u++) {

                sift(new int[]{i, u}, obj);
            }
        }
        System.out.print(testMode ? 's' : "");
    }

    private static void sift(int[] position, int[][] obj) {
        // boolean[] specificPossibility = new boolean[10]; //old algorithm
        if (obj[position[0]][position[1]] != 0) {
            possibility[position[0]][position[1]][0] = true; // already
            // had a
            // num -->put
            // [0]=true ie
            // its certain
            for (int i = 1; i < 10; i++) {
                possibility[position[0]][position[1]][i] = false;
            }

        } else {
            possibility[position[0]][position[1]][0] = false; // else it's not
            // certain
            for (int i = 0; i < 9; i++) { // then look through relevant
                // lines/cube
                if (obj[position[0]][i] != 0) { // same column
                    possibility[position[0]][position[1]][obj[position[0]][i]] = false;
                }
                if (obj[i][position[1]] != 0) { // same row
                    possibility[position[0]][position[1]][obj[i][position[1]]] = false;
                }
                if (obj[(position[0]) / 3 * 3 + i % 3][(position[1]) / 3 * 3
                        + i / 3] != 0) { // same
                    // cube
                    possibility[position[0]][position[1]][obj[(position[0]) / 3
                            * 3 + i % 3][(position[1]) / 3 * 3 + i / 3]] = false;
                }
            }
        }
    }

    private static boolean isNNum(String testee) { // N stands for natural // UI version does not use
        for (int i = 0; i < testee.length(); i++) {
            if (Character.isDigit(testee.charAt(i)) == false)
                return false;
        }
        return true;
    }

    private static void debugDisplay() { // shows possibility
        for (int i = 0; i < 9; i++) { // for testing purposes
            for (int u = 0; u < 9; u++) {
                for (int v = 0; v < 10; v++) {
                    System.out.print(v);
                    System.out.print(possibility[i][u][v]);
                }

                System.out.print("\t");

            }
            System.out.print("\n");
        }
    }

    private static void showCurrentInput(int[][] obj) { // UI version does not use
        for (int i = 0; i < 9; i++) { // for testing purposes
            for (int u = 0; u < 9; u++) {
 /*
  * for(int v=0 ; v< 10 ; v++){
  * System.out.print(possibility[i][u][v]); }
  *
  * System.out.print("\t");
 */

                System.out.print(obj[i][u]);
            }
            System.out.print("\n");
        } // */
    }

    private static boolean throughSFill(int[][] obj) {
        boolean change = false;
        for (int i = 0; i < 9; i++) {
            for (int u = 1; u < 4; u++) {
                if (secondaryFill(u, i, obj) == true)
                    change = true;

            }
        }
        System.out.print(testMode ? 'y' : "");
        // throughSift();
        return change;
    }

    private static boolean secondaryFill(int type, int num, int[][] obj) { // type
        // 1:row
        // type 2:column
        // type 3:cube
        boolean change = false;
        switch (type) {
            case 1:

                for (int u = 1; u < 10; u++) {
                    int pos = 0;
                    int theOnly = 0;

                    for (int v = 0; v < 9; v++) {
                        if (possibility[num][v][u] == true) {
                            pos++;
                            theOnly = v;
                        }
                    }

                    if (pos == 1) {
                        obj[num][theOnly] = u;
                        change = true;
                    }
                }
                break;
            case 2:

                for (int u = 1; u < 10; u++) {
                    int pos = 0;
                    int theOnly = 0;

                    for (int v = 0; v < 9; v++) {
                        if (possibility[v][num][u] == true) {
                            pos++;
                            theOnly = v;
                        }
                    }

                    if (pos == 1) {
                        obj[theOnly][num] = u;
                        change = true;
                    }
                }
                break;
            case 3:
                for (int u = 1; u < 10; u++) {
                    int pos = 0;
                    int theOnly = 0;

                    for (int v = 0; v < 9; v++) {
                        if (possibility[num % 3 * 3 + v % 3][num / 3 * 3 + v / 3][u] == true) {
                            pos++;
                            theOnly = v;
                        }
                    }

                    if (pos == 1) {
                        obj[num % 3 * 3 + theOnly % 3][num / 3 * 3 + theOnly / 3] = u;
                        change = true;
                    }
                }
                break;

        }
        // System.out.print(change);
        return change;
    }

    private static boolean ifSolved(int[][] obj) {
        for (int[] i : obj) {
            for (int u : i) {
                if (u == 0)
                    return false;
            }
        }
        return true;
    }

    public boolean ifSolved() {
        for (int[] i : input) {
            for (int u : i) {
                if (u == 0)
                    return false;
            }
        }
        return true;
    }

    public boolean isHard(){
        return hard;
    }

    public int[][] getResult() {
        return input;
    }

}// THIS IS END OF Sudoku class!

