import java.util.stream.IntStream;

public class Maze {
    private boolean[][] up;
    private boolean[][] down;
    private boolean[][] right;
    private boolean[][] left;

    private boolean[][] isVisited;

    private final int cols;
    private final int rows;
    private final int startingCol;
    private final int startingRow;

    private boolean isFound;
    private int finalRow;

    private boolean finalMark = false;

    public Maze(int cols, int rows,int startingCol, int startingRow) {
        this.cols = cols;
        this.rows = rows;
        this.startingRow = startingRow;
        this.startingCol = startingCol;
        int height = 800;
        int width = (int) Math.round(1.0 * height * cols / rows);
        StdDraw.setCanvasSize(width, height);
        StdDraw.setXscale(0, cols + 2);
        StdDraw.setYscale(0, rows + 2);
        initialize();
        generate(1,1);
        for (int i = 1; i <= cols; i++)
            for (int j = 1; j <= rows; j++)
                isVisited[i][j] = false;
        isFound = false;
        StdDraw.enableDoubleBuffering();
        drawMaze();
        dfs(startingCol,startingRow);

    }

    private void drawStringingPoint(int startingCol, int startingRow){
        StdDraw.setPenColor(StdDraw.BLACK);
        if(startingCol == 1){
            StdDraw.filledSquare(startingCol - 0.5, startingRow + 0.5, 0.5);
            StdDraw.filledSquare(startingCol + 0.5, startingRow + 0.5, 0.5);
        }else if(startingRow == 1){
            StdDraw.filledSquare(startingCol + 0.5, startingRow - 0.5, 0.5);
            StdDraw.filledSquare(startingCol + 0.5, startingRow + 0.5, 0.5);
        }else{
            StdDraw.filledSquare(startingCol + 0.5, startingRow + 0.5, 0.5);

        }

    }
    private void initialize() {

        isVisited = new boolean[cols + 2][rows + 2];
        IntStream.range(0, cols + 2).forEach(i -> {
            isVisited[i][0] = true;
            isVisited[i][rows + 1] = true;
        });
        IntStream.range(0, rows + 2).forEach(i -> {
            isVisited[0][i] = true;
            isVisited[cols + 1][i] = true;
        });

        up = new boolean[cols + 2][rows + 2];
        right = new boolean[cols + 2][rows + 2];
        down = new boolean[cols + 2][rows + 2];
        left = new boolean[cols + 2][rows + 2];

        for(int i = 0; i < cols + 2; i++)
            for (int j = 0; j < rows + 2; j++) {
                up[i][j] = true;
                right[i][j] = true;
                left[i][j] = true;
                down[i][j] = true;
            }

        finalRow = StdRandom.uniform(1,rows);

    }

    private void generate(int col, int row) {
        isVisited[col][row] = true;

        while(!isVisited[col+1][row] || !isVisited[col][row+1] || !isVisited[col-1][row] || !isVisited[col][row-1]){
            while(true){

                int random = StdRandom.uniform(4);
                if(col==cols && row==finalRow && !finalMark){
                    right[col][finalRow] = false;
                    StdDraw.setPenColor(StdDraw.RED);
                    StdDraw.filledSquare(col + 0.5, row + 0.5, 0.5);
                    StdDraw.filledSquare(col + 1.5, row + 0.5, 0.5);
                    finalMark =true;
                }
                else if (random == 0 && !isVisited[col][row + 1]) {
                    up[col][row] = false;
                    down[col][row + 1] = false;
                    generate(col, row + 1);
                    break;
                }
                else if (random == 1 && !isVisited[col + 1][row]) {
                    right[col][row] = false;
                    left[col + 1][row] = false;
                    generate(col + 1, row);
                    break;
                }
                else if (random == 2 && !isVisited[col][row - 1]) {
                    down[col][row] = false;
                    up[col][row - 1] = false;
                    generate(col, row - 1);
                    break;
                }
                else if (random == 3 && !isVisited[col - 1][row]) {
                    left[col][row] = false;
                    right[col - 1][row] = false;
                    generate(col - 1, row);
                    break;
                }
            }
        }
    }

    public void drawMaze() {
        StdDraw.setPenColor(StdDraw.BLACK);
        for (int col = 1; col <= cols; col++) {
            for (int row = 1; row <= rows; row++) {
                if (down[col][row]){
                    StdDraw.line(col, row, col + 1, row);
                }
                if (up[col][row]){
                    StdDraw.line(col, row + 1, col + 1, row + 1);
                }
                if (left[col][row]){
                    StdDraw.line(col, row, col, row + 1);
                }
                if (right[col][row]){
                    StdDraw.line(col + 1, row, col + 1, row + 1);
                }
            }
        }
        StdDraw.show();
    }

    public void dfs(int col, int row){
        if (isFound) {
            return;
        }
        if (col == 0 || row == 0 || col == cols + 1 || row == rows + 1) {
            return;
        }
        if (isVisited[col][row]) {
            return;
        }
        isVisited[col][row] = true;

        StdDraw.setPenColor(StdDraw.ORANGE);
        StdDraw.filledSquare(col + 0.5, row + 0.5, 0.40);
        StdDraw.show();

        if(col == startingCol && row == startingRow){
            drawStringingPoint(col,row);
        }

        if (col == cols && row == finalRow ){
            StdDraw.setPenColor(StdDraw.GREEN);
            StdDraw.filledSquare(col + 0.5, row + 0.5, 0.5);
            StdDraw.filledSquare(col + 1.5, row + 0.5, 0.5);
            isFound = true;
            StdDraw.show();
            return;
        }

        if (!up[col][row]) {
            StdDraw.filledSquare(col + 0.5, row + 0.70, 0.40);
            dfs(col, row + 1);
        }
        if (!right[col][row]) {
            StdDraw.filledSquare(col + 0.70, row + 0.5, 0.40);
            dfs(col + 1, row);
        }
        if (!down[col][row]) {
            StdDraw.filledSquare(col + 0.5, row + 0.30, 0.40);
            dfs(col, row - 1);
        }
        if (!left[col][row]) {
            StdDraw.filledSquare(col + 0.30, row + 0.5, 0.40);
            dfs(col - 1, row);
        }

        if (isFound) {
            return;
        }

        StdDraw.setPenColor(StdDraw.YELLOW);
        StdDraw.filledSquare(col + 0.5, row + 0.5, 0.50);
        StdDraw.show();
    }
}
