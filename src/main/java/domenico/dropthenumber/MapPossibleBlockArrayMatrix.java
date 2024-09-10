package domenico.dropthenumber;

import java.util.*;

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
        calculateFinalMatrix();
    }

    private void initializeKey(int[][] matrix, ArrayPossibleBlock possBlock) {
        for (int[] pB : possBlock.getNewBlocks()) {
            ArrayList<int[][]> initialArray = new ArrayList<>();
            initialArray.add(makeACopyOfMatrix(matrix));
            initialArray.add(firstMove(pB, makeACopyOfMatrix(matrix)));
            map.put(pB, initialArray);
        }
    }

    public int[][] returnFinalMatrix(int[] block) {
        for (int[] key : map.keySet()) {
            if (Arrays.equals(block, key)) {
                ArrayList<int[][]> list = map.get(key);
                return list.get(list.size() - 1);
            }
        }
        return null;
    }

    private void calculateFinalMatrix() {
        for (int[] block : map.keySet()) {
            searchForMatch(block, block);

            ArrayList<int[][]> list = map.get(block);
            int[][] lastMatrix = list.get(list.size() - 1);

            for (int i = ROW_SIZE - 1; i >= 0; i--) {
                for (int j = COL_SIZE - 1; j >= 0; j--) {
                    if (lastMatrix[i][j] != 0) {
                        int[] matrixBlock = new int[]{i, j, lastMatrix[i][j]};
                        if (searchForMatch(block, matrixBlock)) {
                            i = ROW_SIZE - 1;
                            j = COL_SIZE - 1;
                        }
                    }
                }
            }
        }
    }


    private boolean searchForMatch(int[] key, int[] block) {
        //Copy of Block
        int[] copyBlock = block.clone();

        boolean match_found = true;
        boolean match_done = false;

        while (match_found) {
            ArrayList<int[][]> list = map.get(key);
            int[][] lastMatrix = list.get(list.size() - 1);

            // MATCH BELOW
            if (copyBlock[0] + 1 < ROW_SIZE && copyBlock[2] == lastMatrix[copyBlock[0] + 1][copyBlock[1]]) {
                System.out.println("Posso accoppiare giù x : " + copyBlock[0] + " y : " + copyBlock[1]);
                updateMatrix("lower", key, copyBlock, lastMatrix);
                System.out.println("copyBlock dopo match giù x : " + copyBlock[0] + " y : " + copyBlock[1] + " value : " + copyBlock[2]);
                match_done = true;
                continue;
            }
            // MATCH SX
            if (copyBlock[1] - 1 >= 0 && copyBlock[2] == lastMatrix[copyBlock[0]][copyBlock[1] - 1]) {
                System.out.println("Posso accoppiare sx x : " + copyBlock[0] + " y : " + copyBlock[1]);
                updateMatrix("sx", key, copyBlock, lastMatrix);
                System.out.println("copyBlock dopo match sx  x : " + copyBlock[0] + " y : " + copyBlock[1] + " value : " + copyBlock[2]);
                match_done = true;
                continue;
            }

            // MATCH DX
            if (copyBlock[1] + 1 < COL_SIZE && copyBlock[2] == lastMatrix[copyBlock[0]][copyBlock[1] + 1]) {
                System.out.println("Posso accoppiare dx x : " + copyBlock[0] + " y : " + copyBlock[1]);
                updateMatrix("dx", key, copyBlock, lastMatrix);
                System.out.println("copyBlock dopo match dx  x : " + copyBlock[0] + " y : " + copyBlock[1] + " value : " + copyBlock[2]);
                match_done = true;
                continue;
            }

            match_found = false;
        }
        return match_done;
    }

    private void updateMatrix(String typeOfMatch, int[] key, int[] copyBlock, int[][] matrix) {
        switch (typeOfMatch) {

            case "lower":

                System.out.println("CASE lower con x : " + copyBlock[0] + " y : " + copyBlock[1]);

                int[][] copyLowerMatrix = makeACopyOfMatrix(matrix);
                copyLowerMatrix[copyBlock[0] + 1][copyBlock[1]] = copyBlock[2] * 2;
                copyLowerMatrix[copyBlock[0]][copyBlock[1]] = 0;
                addElementAtTheMatrix(key, copyLowerMatrix);

                /*if(copyBlock[0] != 0) {
                    for(int i = copyBlock[0] - 1; i > 0 ; i--) {
                        copyLowerMatrix[i][copyBlock[1]] = copyLowerMatrix[i - 1][copyBlock[1]];
                    }
                }*/

                //Update copyBlock for future match
                copyBlock[0] += 1;
                copyBlock[2] *= 2;
                break;

            case "sx":

                System.out.println("CASE sx con x : " + copyBlock[0] + " y : " + copyBlock[1]);

                int[][] copySxMatrix = makeACopyOfMatrix(matrix);
                copySxMatrix[copyBlock[0]][copyBlock[1]] = copyBlock[2] * 2;
                copySxMatrix[copyBlock[0]][copyBlock[1] - 1] = 0;

                if (copyBlock[0] != 0 && copySxMatrix[copyBlock[0] - 1][copyBlock[1] - 1] != 0) {
                    for (int i = copyBlock[0] - 1; i >= 0; i--) {
                        copySxMatrix[i + 1][copyBlock[1] - 1] = copySxMatrix[i][copyBlock[1] - 1];
                        if (copySxMatrix[i][copyBlock[1] - 1] == 0) {
                            break;
                        }
                    }
                }

                addElementAtTheMatrix(key, copySxMatrix);

                //Update copyBlock for future match
                copyBlock[2] *= 2;

                break;

            case "dx":
                System.out.println("CASE dx con x : " + copyBlock[0] + " y : " + copyBlock[1]);

                int[][] copyDxMatrix = makeACopyOfMatrix(matrix);
                copyDxMatrix[copyBlock[0]][copyBlock[1]] = copyBlock[2] * 2;
                copyDxMatrix[copyBlock[0]][copyBlock[1] + 1] = 0;

                if (copyBlock[0] != 0 && copyDxMatrix[copyBlock[0] - 1][copyBlock[1] + 1] != 0) {
                    for (int i = copyBlock[0] - 1; i >= 0; i--) {
                        copyDxMatrix[i + 1][copyBlock[1] + 1] = copyDxMatrix[i][copyBlock[1] + 1];
                        if (copyDxMatrix[i][copyBlock[1] + 1] == 0) {
                            break;
                        }
                    }
                }

                addElementAtTheMatrix(key, copyDxMatrix);

                //Update copyBlock for future match
                copyBlock[2] *= 2;

                break;
        }
    }


    public Set<int[]> getPossibleBlock() {
        return map.keySet();
    }

    public int getPossibleBlockAndScore(int[] pB) {
        return getScoreForPossibleBlock(pB);
    }

    private int getScoreForPossibleBlock(int[] block) {
        ArrayList<int[][]> list = map.get(block);
        int[][] lastMatrix = list.get(list.size() - 1);
        return (int) Arrays.stream(lastMatrix).flatMapToInt(Arrays::stream).filter(num -> num != 0).count();
    }


    private void addElementAtTheMatrix(int[] key, int[][] matrix) {
        ArrayList<int[][]> arrayOfMatrix = map.get(key);
        arrayOfMatrix.add(matrix);
    }


    private int[][] firstMove(int[] key, int[][] matrix) {
        matrix[key[0]][key[1]] = key[2];
        return matrix;
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

    public ArrayList<int[][]> getArrayOfMatrixForABlock(int[] block) {
        for (int[] key : map.keySet()) {
            if (Arrays.equals(block, key)) {
                return map.get(key);
            }
        }
        return null;
    }


    public String findIfMatch(int[] key) {
        ArrayList<int[][]> arrayOfMatrix = map.get(key);
        return arrayOfMatrix.size() > 2 ? "true" : "false";
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
