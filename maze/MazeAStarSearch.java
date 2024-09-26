import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Title:        MazeAStarSearch<p>
 * Description:  Demo program for Java AI Programming<p>
 * Copyright:    Copyright (c) Mark Watson, Released under Open Source Artistic License<p>
 * Company:      Mark Watson Associates<p>
 * @author Mark Watson
 * @version 1.0
 */

public class MazeAStarSearch extends javax.swing.JFrame {
    JPanel jPanel1 = new JPanel();
    AStarEngine currentSearchEngine = null;
    
    BufferedImage[] images; //contain images

    public MazeAStarSearch() {
        try {
          jbInit();
          loadimages(); //load images in array
        } catch (Exception e) {
          System.out.println("GUI initilization error: " + e);
        }
        currentSearchEngine = new AStarEngine(25, 25);
        repaint();
    }

    public void paint(Graphics g_unused) {
        if (currentSearchEngine == null) return;
        Maze maze = currentSearchEngine.getMaze();
        int width = maze.getWidth();
        int height = maze.getHeight();
        System.out.println("Size of current maze: " + width + " by " + height);
        Graphics g = jPanel1.getGraphics();
        Graphics g2= jPanel1.getGraphics();       
        g2.setColor(Color.white);
        g2.fillRect(0, 0, 320, 320);
        g2.setColor(Color.black);
        maze.setValue(0, 0, Maze.START_LOC_VALUE);
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                short val = maze.getValue(x,y);
                
                if ( val == Maze.OBSTICLE) {
                	g2.drawImage(images[0],6 + x * 29, 3 + y * 29, null);
                
                } else if (val == Maze.START_LOC_VALUE) {
                	g2.drawImage(images[1],6 + x * 29, 3 + y * 29, null);

                } else if (val == Maze.GOAL_LOC_VALUE) {
                	g2.drawImage(images[2],6 + x * 29, 3 + y * 29, null);

                } else {

                	g2.setColor(Color.WHITE);
                	g2.fillRect(6 + x * 29, 3 + y * 29, 29, 29);
                    g2.setColor(Color.BLUE);
                	g2.drawRect(6 + x * 29, 3 + y * 29, 29, 29);
                }
            }
        }
        // redraw the path in black:
        g2.setColor(Color.black);
        Dimension [] path = currentSearchEngine.getPath();
        for (int i=1; i< (path.length); i++) {
          int x = path[i].width;
          int y = path[i].height;
          short val = maze.getValue(x,y);
          g2.drawString("" + (path.length - i), 16 + x * 29, 19 + y * 29);
        }


    }

    public static void main(String[] args) {
        MazeAStarSearch mazeSearch1 = new MazeAStarSearch();
    }
    
    
    private void loadimages() {
        try {
        	
            images = new BufferedImage[4];
            images[0] = ImageIO.read(new File("./brick.png"));
            images[1] = ImageIO.read(new File("./monster.png"));
            images[2] = ImageIO.read(new File("./pacman.png"));
            
           
        } catch (IOException ex) {
            Logger.getLogger(MazeAStarSearch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void jbInit() throws Exception {

        this.setContentPane(jPanel1);
        this.setCursor(null);
        this.setDefaultCloseOperation(3);
        this.setTitle("MazeAStarSearch");
        this.getContentPane().setLayout(null);
        jPanel1.setBackground(Color.white);
        jPanel1.setDebugGraphicsOptions(DebugGraphics.NONE_OPTION);
        jPanel1.setDoubleBuffered(false);
        jPanel1.setRequestFocusEnabled(false);
        jPanel1.setLayout(null);
        this.setSize(320, 340);
        this.setVisible(true);
    }
}


