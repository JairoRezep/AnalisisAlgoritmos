import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.*;

public class MazeGenerator extends JPanel {
    
    private JLabel generationLabel;
    private static final int ROWS = 20;
    private static final int COLS =20;
    private static final int CELL_SIZE = 50;
    private static final int WALL_THICKNESS = 2;
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
        objectiveCell = new int[]{9, 9};
        // Initialize the generation label
        generationLabel = new JLabel("Generation 1", SwingConstants.CENTER);
        generationLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        // Set up the panel layout
        setLayout(new BorderLayout());
        add(generationLabel, BorderLayout.SOUTH);

        // Generar el laberinto usando DFS
        generateMaze(0, 0);
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