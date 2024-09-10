package domenico.dropthenumber;

import java.util.ArrayList;

import static domenico.dropthenumber.RandomMatrixApp.COL_SIZE;
import static domenico.dropthenumber.RandomMatrixApp.ROW_SIZE;


public class ArrayPossibleBlock {

    private ArrayList<int[]> newBlocks;

    public ArrayPossibleBlock(int[][] matrix) {
        newBlocks = new ArrayList<>();
        findPossibleNewBlock(matrix);
    }

    public ArrayList<int[]> findPossibleNewBlock(int[][] matrix) {

        int rand = randBlockValue.getRandom();
        System.out.println("Rand generato : " + rand);

        for (int j = 0; j < COL_SIZE; j++) {
            for (int i = ROW_SIZE - 1; i >= 0; i--) {
                if (matrix[i][j] == 0) {
                    int[] arr = {i, j, rand};
                    newBlocks.add(arr);
                    break;
                }
            }
        }
        return newBlocks;
    }

    public ArrayList<int[]> getNewBlocks() {
        return newBlocks;
    }

    public void printPBlock() {
        for (int[] nB : newBlocks) {
            System.out.println("key : " + nB[0] + "," + nB[1] + " , value : " + nB[2]);
        }
    }

}
