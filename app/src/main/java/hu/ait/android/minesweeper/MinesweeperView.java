package hu.ait.android.minesweeper;

/**
 * Created by alicetan on 9/21/17.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MinesweeperView extends View {
    Paint paintBg;
    Paint paintLine;
    Paint paintBomb;
    Paint paintBox;
    private Paint paintText;
    private int xDim = 5;
    private int yDim = 5;
    private boolean hitMine;
    private boolean wonGame;
    private int numTried;

    private Bitmap bitmapBomb;
    private Bitmap bitmapFlag;
    private Bitmap bitmapBackground;


    public MinesweeperView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paintBg = new Paint();
        paintBg.setColor(0x30ff0000);
        paintBg.setStyle(Paint.Style.FILL);

        paintLine = new Paint();
        paintLine.setColor(Color.BLACK);
        paintLine.setStrokeWidth(7);
        paintLine.setStyle(Paint.Style.STROKE);

        paintText = new Paint();
        paintText.setColor(Color.BLUE);
        paintText.setTextSize(60);

        paintBomb = new Paint();
        paintBomb.setColor(Color.RED);
        paintBomb.setTextSize(60);

        paintBox = new Paint();
        paintBox.setColor(Color.WHITE);
        paintBox.setStyle(Paint.Style.FILL);

        bitmapBomb = BitmapFactory.decodeResource(getResources(), R.drawable.bomb);
        bitmapFlag = BitmapFactory.decodeResource(getResources(), R.drawable.flag);


        MinesweeperModel.getInstance().placeMines();
        MinesweeperModel.getInstance().setNeighborCount();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmapBomb = Bitmap.createScaledBitmap(bitmapBomb, getWidth() / 5, getHeight() / 5, false);
        bitmapFlag = Bitmap.createScaledBitmap(bitmapFlag, getWidth() / 5, getHeight() / 5, false);
    }


    // called with every invalidate
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintBg);
        //drawBoard
        drawGameArea(canvas);

        drawShapes(canvas);
    }

    private void drawGameArea(Canvas canvas) {

        //border
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintLine);

        //vertical lines
        canvas.drawLine(getWidth() / xDim, 0, getWidth() / xDim, getHeight(), paintLine);
        canvas.drawLine(2 * getWidth() / xDim, 0, 2 * getWidth() / xDim, getHeight(), paintLine);
        canvas.drawLine(3 * getWidth() / xDim, 0, 3 * getWidth() / xDim, getHeight(), paintLine);
        canvas.drawLine(4 * getWidth() / xDim, 0, 4 * getWidth() / xDim, getHeight(), paintLine);


        //horizontal lines
        canvas.drawLine(0, getHeight() / yDim, getWidth(), getHeight() / yDim, paintLine);
        canvas.drawLine(0, 2 * (getHeight() / yDim), getWidth(), 2 * (getHeight() / yDim), paintLine);
        canvas.drawLine(0, 3 * (getHeight() / yDim), getWidth(), 3 * (getHeight() / yDim), paintLine);
        canvas.drawLine(0, 4 * (getHeight() / yDim), getWidth(), 4 * (getHeight() / yDim), paintLine);

    }

    private void drawShapes(Canvas canvas) {
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                if (MinesweeperModel.getInstance().getFieldContent(i, j).isFlagged()) {
                    canvas.drawBitmap(bitmapFlag, i * getWidth() / 5, j * getHeight() / 5, null);
                }
                // if cell is a bomb and has been clicked
                if (MinesweeperModel.getInstance().getFieldContent(i, j).isMine() &&
                        MinesweeperModel.getInstance().getFieldContent(i, j).isTried()) {
                    canvas.drawBitmap(bitmapBomb, i * getWidth() / 5, j * getHeight() / 5, null);
                } else if (!MinesweeperModel.getInstance().getFieldContent(i, j).isMine() &&
                        MinesweeperModel.getInstance().getFieldContent(i, j).isTried()) {
                    canvas.drawText(Integer.toString(MinesweeperModel.getInstance().getFieldContent(i, j).getNeighboringMines()),
                            i * getWidth() / 5 + 50, j * getHeight() / 5 + 70, paintText);

                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int tX = ((int) event.getX()) / (getWidth() / 5);
            int tY = ((int) event.getY()) / (getHeight() / 5);
            if (hitMine == false && wonGame == false) {
                // if user decides to flag (flag button is toggled) and hasn't been clicked yet
                if (((MainActivity) getContext()).buttonStatus() == false &&
                        !MinesweeperModel.getInstance().getFieldContent(tX, tY).isTried()) {

                    // if it hasn't been flagged
                    if (!MinesweeperModel.getInstance().getFieldContent(tX, tY).isFlagged()) {
                        // place flag in the square
                        MinesweeperModel.getInstance().getFieldContent(tX, tY).setFlag(true);
                        MinesweeperModel.getInstance().getFieldContent(tX, tY).setClicked(true);
                    } else {
                        MinesweeperModel.getInstance().getFieldContent(tX, tY).setFlag(false);
                    }

                }
                // if user decides to try a square
                if (((MainActivity) getContext()).buttonStatus() == true &&
                        !MinesweeperModel.getInstance().getFieldContent(tX, tY).isTried()) {

                    numTried++;

                    MinesweeperModel.getInstance().getFieldContent(tX, tY).setFlag(false);
                    MinesweeperModel.getInstance().getFieldContent(tX, tY).setTried(true);
                    if (MinesweeperModel.getInstance().getFieldContent(tX, tY).isMine()) {
                        hitMine = true;
                        MinesweeperModel.getInstance().unflagMines();
                        MinesweeperModel.getInstance().revealBombs();
                        ((MainActivity) getContext()).youWinLose(getContext().getString(R.string.lose_text));
                    }
                }
                if (MinesweeperModel.getInstance().allTried(numTried)) {
                    wonGame = true;
                    ((MainActivity) getContext()).youWinLose(getContext().getString(R.string.win_text));
                }
            }
            invalidate();
            return true;
        }
        return super.onTouchEvent(event);
    }

    public String showHint() {
        return MinesweeperModel.getInstance().showHint();
    }


    public void clearBoard() {
        MinesweeperModel.getInstance().resetGame();
        MinesweeperModel.getInstance().placeMines();
        MinesweeperModel.getInstance().setNeighborCount();
        MinesweeperModel.getInstance().resetHints();
        hitMine = false;
        numTried = 0;
        wonGame = false;
        ((MainActivity) getContext()).youWinLose(getContext().getString(R.string.progress_txt));
        invalidate();
    }
}