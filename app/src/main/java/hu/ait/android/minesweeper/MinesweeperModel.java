package hu.ait.android.minesweeper;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by alicetan on 9/25/17.
 */

public class MinesweeperModel {

    private static MinesweeperModel minesweeperModel = null;
    private int xDim = 5;
    private int yDim = 5;
    private int numMines = 3;
    private Field[][] model;
    private int neighboringMines = 0;


    private MinesweeperModel() {
        model = new Field[xDim][yDim];
        for (int rows = 0; rows < xDim; rows++) {
            for (int cols = 0; cols < yDim; cols++) {
                model[rows][cols] = new Field(rows, cols, false, false, false, neighboringMines, false, false);
            }
        }
    }

    public static MinesweeperModel getInstance() {
        if (minesweeperModel == null) {
            minesweeperModel = new MinesweeperModel();
        }
        return minesweeperModel;
    }

    public Field getFieldContent(int x, int y) {
        return model[x][y];
    }

    public void placeMines() {

        int minesPlaced = 0;
        Random random = new Random();

        while (minesPlaced < numMines) {
            int x = random.nextInt(xDim);
            int y = random.nextInt(yDim);
            if (model[x][y].isMine() != true) {
                model[x][y].setMine(true);
                minesPlaced++;
            }
        }
    }

    public void setNeighborCount() {
        for (int rows = 0; rows < xDim; rows++) {
            for (int cols = 0; cols < yDim; cols++) {
                getNeighbors(model[rows][cols]);
            }
        }
    }

    public void getNeighbors(Field cell) {
        int rowNum = cell.getX();
        int colNum = cell.getY();
        // inner matrix- all have 8 neighbors
        if (rowNum > 0 && rowNum < xDim - 1 && colNum > 0 && colNum < yDim - 1) {
            if (cell.isMine()) {
                //left above diagonal
                model[rowNum - 1][colNum - 1].incrementMineCount();
                //above
                model[rowNum - 1][colNum].incrementMineCount();
                //right above diagonal
                model[rowNum - 1][colNum + 1].incrementMineCount();
                //right
                model[rowNum][colNum + 1].incrementMineCount();
                //right below diagonal
                model[rowNum + 1][colNum + 1].incrementMineCount();
                //below
                model[rowNum + 1][colNum].incrementMineCount();
                //left below diagonal
                model[rowNum + 1][colNum - 1].incrementMineCount();
                //left
                model[rowNum][colNum - 1].incrementMineCount();
            }
        }
        // only right, right below diagonal, and below
        else if (rowNum == 0 && colNum == 0 && cell.isMine()) {
            //right
            model[rowNum][colNum + 1].incrementMineCount();
            //right below diagonal
            model[rowNum + 1][colNum + 1].incrementMineCount();
            //below
            model[rowNum + 1][colNum].incrementMineCount();
        }
        // only left, left below diagonal, and below
        else if (rowNum == 0 && colNum == yDim - 1 && cell.isMine()) {
            //below
            model[rowNum + 1][colNum].incrementMineCount();
            //left below diagonal
            model[rowNum + 1][colNum - 1].incrementMineCount();
            //left
            model[rowNum][colNum - 1].incrementMineCount();
        }
        // top, above diagonal, and right
        else if (rowNum == xDim - 1 && colNum == 0 && cell.isMine()) {
            //above
            model[rowNum - 1][colNum].incrementMineCount();
            //right above diagonal
            model[rowNum - 1][colNum + 1].incrementMineCount();
            //right
            model[rowNum][colNum + 1].incrementMineCount();
        }
        // only above, left diagonal, and left
        else if (rowNum == xDim - 1 && colNum == yDim - 1 && cell.isMine()) {
            //left above diagonal
            model[rowNum - 1][colNum - 1].incrementMineCount();
            //above
            model[rowNum - 1][colNum].incrementMineCount();
            //left
            model[rowNum][colNum - 1].incrementMineCount();
        }
        // check left, right, left below diagonal, below, and right below diagonal
        else if (rowNum == 0 && colNum > 0 && colNum < yDim - 1 && cell.isMine()) {
            //right
            model[rowNum][colNum + 1].incrementMineCount();
            //right below diagonal
            model[rowNum + 1][colNum + 1].incrementMineCount();
            //below
            model[rowNum + 1][colNum].incrementMineCount();
            //left below diagonal
            model[rowNum + 1][colNum - 1].incrementMineCount();
            //left
            model[rowNum][colNum - 1].incrementMineCount();
        }
        // check left, left above diagonal, above, right above diagonal, right
        else if (rowNum == xDim - 1 && colNum > 0 && colNum < yDim - 1 && cell.isMine()) {
            //left
            model[rowNum][colNum - 1].incrementMineCount();
            //left above diagonal
            model[rowNum - 1][colNum - 1].incrementMineCount();
            //above
            model[rowNum - 1][colNum].incrementMineCount();
            //right above diagonal
            model[rowNum - 1][colNum + 1].incrementMineCount();
            //right
            model[rowNum][colNum + 1].incrementMineCount();
        }
        // check above, right above diagonal, right, right below diagonal, below
        else if (colNum == 0 && rowNum > 0 && rowNum < xDim - 1 && cell.isMine()) {
            //above
            model[rowNum - 1][colNum].incrementMineCount();
            //right above diagonal
            model[rowNum - 1][colNum + 1].incrementMineCount();
            //right
            model[rowNum][colNum + 1].incrementMineCount();
            //right below diagonal
            model[rowNum + 1][colNum + 1].incrementMineCount();
            //below
            model[rowNum + 1][colNum].incrementMineCount();
        }
        // check above, left above diagonal, left, left bottom diagonal, below
        else if (colNum == yDim - 1 && rowNum > 0 && rowNum < xDim - 1 && cell.isMine()) {
            //below
            model[rowNum + 1][colNum].incrementMineCount();
            //left below diagonal
            model[rowNum + 1][colNum - 1].incrementMineCount();
            //left
            model[rowNum][colNum - 1].incrementMineCount();
            //left above diagonal
            model[rowNum - 1][colNum - 1].incrementMineCount();
            //above
            model[rowNum - 1][colNum].incrementMineCount();
        }
    }

    public boolean allTried(int numTried) {
        if (numTried == ((xDim * yDim) - numMines)) {
            return true;
        }
        return false;
    }


    public void revealBombs() {
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                if (model[i][j].isMine()) {
                    model[i][j].setTried(true);
                }
            }
        }
    }


    public void unflagMines() {
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                if (model[i][j].isMine() && model[i][j].isFlagged()) {
                    model[i][j].setFlag(false);
                }
            }
        }
    }


    public String showHint() {
        String x = "";
        String y = "";
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                if (model[i][j].isMine() && !model[i][j].isHinted()) {
                    x = Integer.toString(i);
                    y = Integer.toString(j);
                    model[i][j].setHinted(true);
                    return "The following is a bomb: (" + x + "," + y + ")";
                }
            }
        }
        return "No more hints to give";
    }


    public void resetHints() {
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                model[i][j].setHinted(false);
            }
        }
    }


    public void resetGame() {
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                model[i][j].setMine(false);
                model[i][j].setFlag(false);
                model[i][j].setTried(false);
                model[i][j].setNeighboringMines(0);
                model[i][j].setMine(false);
                model[i][j].setClicked(false);
            }

        }
    }
}
