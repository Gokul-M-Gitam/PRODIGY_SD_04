class SudokuSolver {
    private static final int SIZE = 9;
    private static final int EMPTY = 0;

    public boolean solveStepByStep(int[][] board, CellUpdateCallback callback) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == EMPTY) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;
                            callback.update(row, col, num);
                            if (solveStepByStep(board, callback)) {
                                return true;
                            }
                            board[row][col] = EMPTY;
                            callback.update(row, col, EMPTY);
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isValid(int[][] board, int row, int col, int num) {
        for (int x = 0; x < SIZE; x++) {
            if (board[row][x] == num || board[x][col] == num || board[row - row % 3 + x / 3][col - col % 3 + x % 3] == num) {
                return false;
            }
        }
        return true;
    }

    public interface CellUpdateCallback {
        void update(int row, int col, int num);
    }
}
