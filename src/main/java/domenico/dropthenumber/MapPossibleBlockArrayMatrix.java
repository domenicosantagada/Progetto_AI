package domenico.dropthenumber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static domenico.dropthenumber.RandomMatrixApp.COL_SIZE;
import static domenico.dropthenumber.RandomMatrixApp.ROW_SIZE;

public class MapPossibleBlockArrayMatrix {

    // Class Member
    private ArrayPossibleBlock possBlock;
    private HashMap<int[], ArrayList<int[][]>> map;


    // CONSTRUCTOR
    public MapPossibleBlockArrayMatrix(int[][] matrix) {
        map = new HashMap<>();
        possBlock = new ArrayPossibleBlock(matrix);
        initializeKey(matrix, possBlock);

    }

    private void initializeKey(int[][] matrix, ArrayPossibleBlock possBlock) {
        for (int[] pB : possBlock.getNewBlocks()) {
            ArrayList<int[][]> initialArray = new ArrayList<>();
            initialArray.add(makeACopyOfMatrix(matrix));
            map.put(pB, initialArray);
        }
    }

    public void searchForMatch() {
        for (int[] block : map.keySet()) {
            //Copy of Block
            int[] copyBlock = block.clone();

            boolean match_found = true;

            while (match_found) {
                ArrayList<int[][]> list = map.get(block);
                int[][] lastMatrix = list.get(list.size() - 1);

                // MATCH BELOW
                if (copyBlock[0] + 1 < ROW_SIZE && copyBlock[2] == lastMatrix[copyBlock[0] + 1][copyBlock[1]]) {
                    System.out.println("Posso accoppiare giù x : " + copyBlock[0] + " y : " + copyBlock[1]);
                    updateMatrix("lower", block, copyBlock, lastMatrix);
                    System.out.println("copyBlock dopo match giù x : " + copyBlock[0] + " y : " + copyBlock[1] + " value : " + copyBlock[2]);
                    continue;

                }
                // MATCH SX
                if (copyBlock[1] - 1 >= 0 && copyBlock[2] == lastMatrix[copyBlock[0]][copyBlock[1] - 1]) {
                    System.out.println("Posso accoppiare sx x : " + copyBlock[0] + " y : " + copyBlock[1]);

                    //continue;

                }

                // MATCH DX
                if (copyBlock[1] + 1 < COL_SIZE && copyBlock[2] == lastMatrix[copyBlock[0]][copyBlock[1] + 1]) {
                    System.out.println("Posso accoppiare dx x : " + copyBlock[0] + " y : " + copyBlock[1] + " value : " + copyBlock[2]);

                    //continue;

                }

                match_found = false;

            }


        }

    }

    private void updateMatrix(String typeOfMatch, int[] block, int[] copyBlock, int[][] matrix) {
        switch (typeOfMatch) {

            case "lower":

                System.out.println("CASE lower con x : " + copyBlock[0] + " y : " + copyBlock[1]);

                int[][] copyLower = makeACopyOfMatrix(matrix);
                copyLower[copyBlock[0] + 1][copyBlock[1]] = copyBlock[2] * 2;
                copyLower[copyBlock[0]][copyBlock[1]] = 0;
                addElementAtTheMatrix(block, copyLower);

                //Update copyBlock for future match
                copyBlock[0] += 1;
                copyBlock[2] *= 2;
                break;

            case "sx":
                System.out.println("CASE sx con x : " + copyBlock[0] + " y : " + copyBlock[1]);
                int[][] copySx = makeACopyOfMatrix(matrix);


                break;
            case "dx":
                System.out.println("CASE dx con x : " + copyBlock[0] + " y : " + copyBlock[1]);
                int[][] copyDx = makeACopyOfMatrix(matrix);
                break;


        }
    }

    public void printPossibleBlock() {
        for (int[] pB : map.keySet()) {
            System.out.println(Arrays.toString(pB));
        }
    }


    private void addElementAtTheMatrix(int[] key, int[][] matrix) {
        ArrayList<int[][]> arrayOfMatrix = map.get(key);
        arrayOfMatrix.add(matrix);
    }


    public void printMap() {
        System.out.println();
        for (Map.Entry<int[], ArrayList<int[][]>> entry : map.entrySet()) {

            System.out.println("Key: " + Arrays.toString(entry.getKey()));
            System.out.println("Value: ");
            for (int[][] array : entry.getValue()) {
                System.out.println(Arrays.deepToString(array));
            }
        }
        System.out.println();
    }


    private int[][] makeACopyOfMatrix(int[][] original) {
        assert original != null;

        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }

        return copy;
    }

}
