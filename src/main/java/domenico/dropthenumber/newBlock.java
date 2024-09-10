package domenico.dropthenumber;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("newBlock")
public class newBlock {


    @Param(0)
    private int row;
    @Param(1)
    private int column;
    @Param(2)
    private int value;
    @Param(3)
    private int score;

    public newBlock(int r, int c, int v) {
        this.row = r;
        this.column = c;
        this.score = v;

    }

    public newBlock() {
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}


