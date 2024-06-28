import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class SudokuGUI extends JFrame {
    private static final int SIZE = 9;
    private static final int SUBGRID_SIZE = 3;
    private static final int FILLED_CELLS = 20; // Number of initially filled cells
    private JTextField[][] cells = new JTextField[SIZE][SIZE];
    private SudokuSolver solver = new SudokuSolver();
    private int[][] board = new int[SIZE][SIZE];

    public SudokuGUI() {
        setTitle("Sudoku Solver");
        setLayout(new BorderLayout());
        add(createGridPanel(), BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        generateRandomSudoku();
    }

    private JPanel createGridPanel() {
        JPanel panel = new JPanel(new GridLayout(SIZE, SIZE));
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col] = new JTextField();
                cells[row][col].setHorizontalAlignment(JTextField.CENTER);
                cells[row][col].setFont(new Font("Arial", Font.BOLD, 20));
                cells[row][col].setPreferredSize(new Dimension(50, 50));
                cells[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cells[row][col].setEditable(false); // Initially non-editable
                if ((row / SUBGRID_SIZE + col / SUBGRID_SIZE) % 2 == 0) {
                    cells[row][col].setBackground(Color.LIGHT_GRAY);
                }
                panel.add(cells[row][col]);
            }
        }
        return panel;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        JButton solveButton = new JButton("Solve");
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solveSudoku();
            }
        });
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearGrid();
                generateRandomSudoku();
            }
        });
        panel.add(solveButton);
        panel.add(clearButton);
        return panel;
    }

    private void generateRandomSudoku() {
        Random rand = new Random();
        clearGrid();
        for (int i = 0; i < FILLED_CELLS; i++) {
            int row, col, num;
            do {
                row = rand.nextInt(SIZE);
                col = rand.nextInt(SIZE);
                num = rand.nextInt(SIZE) + 1;
            } while (board[row][col] != 0 || !solver.isValid(board, row, col, num));
            board[row][col] = num;
            cells[row][col].setText(String.valueOf(num));
        }
    }

    private void solveSudoku() {
        new Thread(() -> {
            if (solver.solveStepByStep(board, this::updateGridCell)) {
                JOptionPane.showMessageDialog(this, "Sudoku solved!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No solution found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }).start();
    }

    private void updateGridCell(int row, int col, int num) {
        SwingUtilities.invokeLater(() -> cells[row][col].setText(String.valueOf(num)));
        try {
            Thread.sleep(100); // Pause to visually show the solving process
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void clearGrid() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col].setText("");
                board[row][col] = 0;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SudokuGUI());
    }
}

