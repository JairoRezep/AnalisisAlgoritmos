import java.awt.Dimension;
import java.lang.Math;

/**
 * Class Maze - private class for representing search space as a two-dimensional maze
 */
public class Maze {
    public static short OBSTICLE = -1;
    public static short START_LOC_VALUE = -2;
    public static short GOAL_LOC_VALUE = -3;
    private int width = 0;
    private int height = 0;
    public Dimension startLoc = new Dimension();
    public Dimension goalLoc  = new Dimension();
    /**
     * The maze (or search space) data is stored as a short integer rather than
     * as a boolean so that bread-first style searches can use the array to store
     * search depth. A value of -1 indicates a barrier in the maze.
     */
    private short [][]maze;
    public Maze(int width, int height) {
        System.out.println("New maze of size " + width + " by " + height);
        this.width = width;
        this.height = height;
        maze = new short[width+2][height+2];
        for (int i=0; i<width+2; i++) {
            for (int j=0; j<height+2; j++) {
                maze[i][j] = 0;
            }
        }
        for (int i=0; i<height+2; i++) {
            maze[0][i] = maze[width+1][i] = OBSTICLE;
        }
        for (int i=0; i<width+2; i++) {
            maze[i][0] = maze[i][height+1] = OBSTICLE;
        }
        /**
         * Randomize the maze by putting up arbitray obsticals
         */
        
       int max_obsticles = (width * height) / 3;
        for (int i=0; i<max_obsticles; i++) {
            int x = (int)(Math.random()*width);
            int y = (int)(Math.random()*height);
            setValue(x, y, OBSTICLE);
        }
        
        
        /**
         * Fixed obsticals
         */
        /* 
        setValue(1, 3, OBSTICLE);
        setValue(2, 5, OBSTICLE);
        setValue(3, 7, OBSTICLE);
        setValue(4, 0, OBSTICLE);
        setValue(5, 2, OBSTICLE);
        setValue(6, 4, OBSTICLE);
        setValue(7, 6, OBSTICLE);
        setValue(8, 8, OBSTICLE);
        setValue(9, 1, OBSTICLE);
        setValue(0, 9, OBSTICLE);
        setValue(1, 4, OBSTICLE);
        setValue(2, 7, OBSTICLE);
        setValue(3, 1, OBSTICLE);
        setValue(4, 8, OBSTICLE);
        setValue(5, 3, OBSTICLE);
        setValue(6, 9, OBSTICLE);
        setValue(7, 0, OBSTICLE);
        setValue(8, 2, OBSTICLE);
        setValue(9, 5, OBSTICLE);
        setValue(0, 6, OBSTICLE);
        setValue(1, 9, OBSTICLE);
        setValue(2, 0, OBSTICLE);
        setValue(3, 8, OBSTICLE);
        setValue(4, 7, OBSTICLE);
        setValue(5, 1, OBSTICLE);
        setValue(6, 3, OBSTICLE);
        setValue(7, 4, OBSTICLE);
        setValue(8, 9, OBSTICLE);
        setValue(9, 2, OBSTICLE);
        setValue(0, 7, OBSTICLE);
        setValue(1, 6, OBSTICLE);
        setValue(2, 4, OBSTICLE);
        setValue(3, 5, OBSTICLE);
        setValue(4, 1, OBSTICLE);
        */
       

        
        
        /**
         * Specify the starting location
         */
         startLoc.width = 0;
         startLoc.height = 0;
         setValue(0, 0, (short)0);
        /**
         * Specify the goal location
         */
        goalLoc.width = width - 1;
        goalLoc.height = height - 1;
        setValue(width - 1, height - 1, GOAL_LOC_VALUE);
    }
    synchronized public short getValue(int x, int y) { return maze[x+1][y+1]; }
    synchronized public void setValue(int x, int y, short value) { maze[x+1][y+1] = value; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
