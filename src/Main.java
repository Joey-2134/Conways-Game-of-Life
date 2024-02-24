import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.util.Arrays;

public class Main extends JFrame implements Runnable, MouseListener {

    public static final int WINDOW_SIZE_X = 800;
    public static final int WINDOW_SIZE_Y = 900;
    private static final long FRAME_TIME = 250;

    private static final Dimension WindowSize = new Dimension(WINDOW_SIZE_X, WINDOW_SIZE_Y);
    private final BufferStrategy strategy;

    private boolean[][] grid = new boolean[40][40];
    private boolean isPaused = true;


    public Main() {
        this.setTitle("Conways Game of Life");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

        int x = screenSize.width / 2 - WINDOW_SIZE_X / 2;
        int y = screenSize.height / 2 - WINDOW_SIZE_Y / 2;
        setBounds(x, y, WindowSize.width, WindowSize.height);
        setVisible(true);

        createBufferStrategy(2);
        strategy = getBufferStrategy();

        addMouseListener(this);

        for (boolean[] booleans : grid) {
            Arrays.fill(booleans, false);
        }

        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getY() < 800) {
            int x = e.getX() / 20;
            int y = e.getY() / 20;

            grid[x][y] = !grid[x][y];
            repaint();
        }

        if (e.getX() > 0 && e.getX() < 400 && e.getY() > 800 && e.getY() < 900) {
            isPaused = !isPaused;
            repaint();
        }
        if (e.getX() > 400 && e.getX() < 800 && e.getY() > 800 && e.getY() < 900) {
            for (boolean[] booleans : grid) {
                for (int j = 0; j < grid[0].length; j++) {
                    booleans[j] = Math.random() < 0.2;
                }
            }
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void paint(Graphics g) {
        g = strategy.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WindowSize.width, WindowSize.height);

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j]) {
                    g.setColor(Color.WHITE);
                    g.fillRect(i * 20, j * 20, 20, 20);
                }
            }
        }

        if (isPaused) {
            displayPausedUI(g);
        } else {
            displayRunningUI(g);
        }

        strategy.show();
    }

    private void displayRunningUI(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(0,800,400,100);
        g.setFont(new Font("Arial", Font.PLAIN, 50));
        g.setColor(Color.WHITE);
        g.drawString("Stop", 150, 850);
        displayRandomButton(g);
    }

    private void displayPausedUI(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(0,800,400,100);
        g.setFont(new Font("Arial", Font.PLAIN, 50));
        g.setColor(Color.WHITE);
        g.drawString("Start", 150, 850);
        displayRandomButton(g);
    }

    private void displayRandomButton(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(400,800,400,100);
        g.setFont(new Font("Arial", Font.PLAIN, 50));
        g.setColor(Color.WHITE);
        g.drawString("Random", 550, 850);
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (!isPaused) {
                    calcNextGen();
                    repaint();
                }
                Thread.sleep(FRAME_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void calcNextGen() {
        boolean [][] newGrid = new boolean[40][40];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                int neighbourCells = 0;
                for (int k = -1; k < 2; k++){
                    for (int l = -1; l < 2; l++) {
                        if (i + k >= 0 && i + k < grid.length && j + l >= 0 && j + l < grid[i].length) {
                            if (k == 0 && l == 0) {
                                continue;
                            }
                            if (grid[i+k][j+l]) {
                                neighbourCells++;
                            }
                        }
                    }
                }
                if (grid[i][j]) {
                    if (neighbourCells < 2 || neighbourCells > 3) {
                        newGrid[i][j] = false;
                    } else {
                        newGrid[i][j] = true;
                    }
                } else {
                    if (neighbourCells == 3) {
                        newGrid[i][j] = true;
                    }
                }
            }
        }
        grid = newGrid;
    }


    public static void main(String[] args) {
        new Main();
    }
}