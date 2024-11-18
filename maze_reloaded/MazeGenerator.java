import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.*;

public class MazeGenerator extends JPanel {
    
    private JLabel generationLabel;
    private static final int ROWS = 15;
    private static final int COLS = 15;
    private static final int CELL_SIZE = 50;
    private static final int WALL_THICKNESS = 5;
    private static final int BORDER_THICKNESS = 2;
    private boolean[][] visited;
    public boolean[][][] walls;
    private Color[][] cellColors = new Color[ROWS][COLS];
    public int[] initialCell, objectiveCell;

    public MazeGenerator() {
        visited = new boolean[ROWS][COLS];
        walls = new boolean[ROWS][COLS][4]; // [top, right, bottom, left]

        // Inicializar todas las paredes como presentes
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                for (int i = 0; i < 4; i++) {
                    walls[r][c][i] = true;
                }
            }
        }
        initialCell = new int[]{0, 0};
        objectiveCell = new int[]{14, 14};
        // Initialize the generation label
        generationLabel = new JLabel("Generation 1", SwingConstants.CENTER);
        generationLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        // Set up the panel layout
        setLayout(new BorderLayout());
        add(generationLabel, BorderLayout.SOUTH);

        // Generar el laberinto usando DFS
        generateMaze(0, 0);
        //generateMazeWithRecursiveDivision(0, 0, 24, 24);
        //generateMazeWithPrims();
        //generateMazeWithEllers();
        paintCell(initialCell[0], initialCell[1], Color.YELLOW);
        paintCell(objectiveCell[0], objectiveCell[1], Color.BLUE);

        repaint();
    }

    private void generateMaze(int row, int col) {
        visited[row][col] = true;
        List<int[]> directions = new ArrayList<>();
        directions.add(new int[]{-1, 0, 0}); // Arriba
        directions.add(new int[]{1, 0, 2});  // Abajo
        directions.add(new int[]{0, -1, 3}); // Izquierda
        directions.add(new int[]{0, 1, 1});  // Derecha

        Collections.shuffle(directions, new Random());

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            int wallIndex = dir[2];

            if (newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLS && !visited[newRow][newCol]) {
                // Romper las paredes entre la celda actual y la celda vecina
                walls[row][col][wallIndex] = false;
                walls[newRow][newCol][(wallIndex + 2) % 4] = false;
                generateMaze(newRow, newCol);
            }
        }
    }

    private void generateMazeWithRecursiveDivision(int rowStart, int colStart, int rowEnd, int colEnd) {
        if (rowEnd - rowStart < 2 || colEnd - colStart < 2) return;
    
        Random random = new Random();
    
        // Horizontal or Vertical Division
        boolean horizontal = (rowEnd - rowStart) > (colEnd - colStart) ? true : random.nextBoolean();
    
        if (horizontal) {
            int divideRow = rowStart + random.nextInt(rowEnd - rowStart - 1) + 1;
            int gapCol = colStart + random.nextInt(colEnd - colStart);
    
            for (int col = colStart; col < colEnd; col++) {
                if (col != gapCol) {
                    walls[divideRow][col][0] = false;
                    walls[divideRow - 1][col][2] = false;
                }
            }
            generateMazeWithRecursiveDivision(rowStart, colStart, divideRow, colEnd);
            generateMazeWithRecursiveDivision(divideRow, colStart, rowEnd, colEnd);
        } else {
            int divideCol = colStart + random.nextInt(colEnd - colStart - 1) + 1;
            int gapRow = rowStart + random.nextInt(rowEnd - rowStart);
    
            for (int row = rowStart; row < rowEnd; row++) {
                if (row != gapRow) {
                    walls[row][divideCol][3] = false;
                    walls[row][divideCol - 1][1] = false;
                }
            }
            generateMazeWithRecursiveDivision(rowStart, colStart, rowEnd, divideCol);
            generateMazeWithRecursiveDivision(rowStart, divideCol, rowEnd, colEnd);
        }
    }

    private void generateMazeWithPrims() {
        List<int[]> wallsList = new ArrayList<>();
        int startRow = new Random().nextInt(ROWS);
        int startCol = new Random().nextInt(COLS);
    
        visited[startRow][startCol] = true;
        addWalls(startRow, startCol, wallsList);
    
        Random random = new Random();
        while (!wallsList.isEmpty()) {
            int[] wall = wallsList.remove(random.nextInt(wallsList.size()));
            int row = wall[0];
            int col = wall[1];
            int wallIndex = wall[2];
            int newRow = row + (wallIndex == 0 ? -1 : wallIndex == 2 ? 1 : 0);
            int newCol = col + (wallIndex == 3 ? -1 : wallIndex == 1 ? 1 : 0);
    
            if (newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLS && !visited[newRow][newCol]) {
                walls[row][col][wallIndex] = false;
                walls[newRow][newCol][(wallIndex + 2) % 4] = false;
                visited[newRow][newCol] = true;
                addWalls(newRow, newCol, wallsList);
            }
        }
    }
    
    private void addWalls(int row, int col, List<int[]> wallsList) {
        if (row > 0) wallsList.add(new int[]{row, col, 0}); // Top
        if (row < ROWS - 1) wallsList.add(new int[]{row, col, 2}); // Bottom
        if (col > 0) wallsList.add(new int[]{row, col, 3}); // Left
        if (col < COLS - 1) wallsList.add(new int[]{row, col, 1}); // Right
    }
    
    private void generateMazeWithEllers() {
        int[][] sets = new int[ROWS][COLS];
        int setCounter = 1; // Unique set identifier
    
        // Iterate through each row
        for (int row = 0; row < ROWS; row++) {
            // Step 1: Assign unique sets to uninitialized cells
            for (int col = 0; col < COLS; col++) {
                if (sets[row][col] == 0) {
                    sets[row][col] = setCounter++;
                }
            }
    
            // Step 2: Randomly join adjacent cells in the same row
            Random random = new Random();
            for (int col = 0; col < COLS - 1; col++) {
                if (sets[row][col] != sets[row][col + 1] && random.nextBoolean()) {
                    joinCells(row, col, row, col + 1, sets, sets[row][col], sets[row][col + 1], 1);
                }
            }
    
            // Step 3: Create vertical connections to the next row
            if (row < ROWS - 1) {
                Map<Integer, List<Integer>> setToCols = new HashMap<>();
    
                // Group columns by set
                for (int col = 0; col < COLS; col++) {
                    setToCols.computeIfAbsent(sets[row][col], k -> new ArrayList<>()).add(col);
                }
    
                // For each set, ensure at least one cell connects to the row below
                for (Map.Entry<Integer, List<Integer>> entry : setToCols.entrySet()) {
                    List<Integer> columns = entry.getValue();
                    int numConnections = random.nextInt(columns.size()) + 1; // At least one connection
                    Collections.shuffle(columns, random);
    
                    for (int i = 0; i < numConnections; i++) {
                        int col = columns.get(i);
                        joinCells(row, col, row + 1, col, sets, entry.getKey(), setCounter++, 2);
                    }
                }
            }
        }
    }
    
    private void joinCells(int row1, int col1, int row2, int col2, int[][] sets, int set1, int set2, int wallDirection) {
        // Remove the wall between the cells
        walls[row1][col1][wallDirection] = false;
        walls[row2][col2][(wallDirection + 2) % 4] = false;
    
        // Merge sets if the cells are in different rows
        if (row1 == row2) {
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    if (sets[r][c] == set2) {
                        sets[r][c] = set1;
                    }
                }
            }
        } else {
            sets[row2][col2] = set1; // Assign the new row's cell to the same set
        }
    }
    

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.LIGHT_GRAY);

        // Draw the borders of the cells
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                int x = c * CELL_SIZE;
                int y = r * CELL_SIZE;
                g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
            }
        }

        g.setColor(Color.BLACK);

        // Draw the maze walls
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                int x = c * CELL_SIZE;
                int y = r * CELL_SIZE;

                if (walls[r][c][0]) g.fillRect(x, y, CELL_SIZE, WALL_THICKNESS);             // Top wall
                if (walls[r][c][1]) g.fillRect(x + CELL_SIZE - WALL_THICKNESS, y, WALL_THICKNESS, CELL_SIZE); // Right wall
                if (walls[r][c][2]) g.fillRect(x, y + CELL_SIZE - WALL_THICKNESS, CELL_SIZE, WALL_THICKNESS); // Bottom wall
                if (walls[r][c][3]) g.fillRect(x, y, WALL_THICKNESS, CELL_SIZE);             // Left wall
            }
        }

        // Draw the colored cells
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (cellColors[r][c] != null) {
                    int x = c * CELL_SIZE;
                    int y = r * CELL_SIZE;
                    g.setColor(cellColors[r][c]);
                    g.fillRect(x + WALL_THICKNESS, y + WALL_THICKNESS,
                            CELL_SIZE - 2 * WALL_THICKNESS, CELL_SIZE - 2 * WALL_THICKNESS);
                }
            }
        }

    }

    public void paintCell(int x, int y, Color color) {
        if (x >= 0 && x < COLS && y >= 0 && y < ROWS) {
            cellColors[y][x] = color;
        }
    }

    public void updateGeneration(int generation) {
        generationLabel.setText("Generation " + generation);
        repaint();
    }
    

    public int[] getCellColorRGBA(int x, int y) {
        if (x >= 0 && x < COLS && y >= 0 && y < ROWS) {
            Color color = cellColors[y][x];
            if (color != null) {
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                int alpha = color.getAlpha();
                return new int[]{red, green, blue, alpha};
            }
        }
        return null; // Return null if the cell is out of bounds or no color is set
    }
    
    
    

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COLS * CELL_SIZE, ROWS * CELL_SIZE);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Maze Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MazeGenerator maze = new MazeGenerator();
        frame.add(maze);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        Fitness fit = new Fitness(100, ROWS * COLS, maze, maze.initialCell, maze.objectiveCell);
    }

    
}