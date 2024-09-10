package domenico.dropthenumber;

import java.util.ArrayList;
import java.util.Random;

import static domenico.dropthenumber.RandomMatrixApp.COL_SIZE;
import static domenico.dropthenumber.RandomMatrixApp.ROW_SIZE;

public class randBlockValue {

    //private static ArrayList<Integer> listRandValueBlock = new ArrayList<Integer>(Arrays.asList(2,4,8,16,32,64,128,256,512,1024,2048));
    private static ArrayList<Integer> listRandValueBlock;

    public randBlockValue() {
        listRandValueBlock = new ArrayList<>() {{
            add(2);
        }};
        Random random = new Random();
    }

    public static int getRandom() {
        int rnd = new Random().nextInt(listRandValueBlock.size());
        listRandValueBlock.add(listRandValueBlock.get(rnd));
        return listRandValueBlock.get(rnd);
    }

    public void print() {
        for (int v : listRandValueBlock) {
            System.out.println(v + " ");
        }
    }


    public void addValueToArray(int[][] matrix) {
        for (int i = ROW_SIZE - 1; i >= 0; i--) {
            for (int j = COL_SIZE - 1; j >= 0; j--) {
                if (matrix[i][j] != 0 && matrix[i][j] <= 16) {
                    listRandValueBlock.add(matrix[i][j]);
                }
            }
        }
    }


}
