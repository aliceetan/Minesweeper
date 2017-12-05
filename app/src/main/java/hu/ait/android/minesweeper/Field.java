package hu.ait.android.minesweeper;

/**
 * Created by alicetan on 9/25/17.
 */

public class Field {

    private int x;
    private int y;
    private boolean isMine = false;
    private boolean clicked;
    private boolean isFlagged;
    private int neighboringMines;
    private boolean tried;
    private boolean hinted;

    public Field(int x, int y, boolean isMine, boolean isFlagged, boolean clicked,
                 int neighboringMines, boolean tried, boolean hinted) {
        this.x = x;
        this.y = y;
        this.isMine = isMine;
        this.isFlagged = isFlagged;
        this.clicked = clicked;
        this.neighboringMines = neighboringMines;
        this.tried = tried;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public boolean getClicked(int x, int y) {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlag(boolean flag) {
        isFlagged = flag;
    }

    public int getNeighboringMines() {
        return neighboringMines;
    }

    public void setNeighboringMines(int neighboringMines) {
        this.neighboringMines = neighboringMines;
    }

    public void incrementMineCount() {
        neighboringMines = neighboringMines + 1;
        setNeighboringMines(neighboringMines);
    }

    public boolean isTried() {
        return tried;
    }

    public void setTried(boolean tried) {
        this.tried = tried;
    }

    public boolean isHinted() {
        return hinted;
    }

    public void setHinted(boolean hinted) {
        this.hinted = hinted;
    }
}