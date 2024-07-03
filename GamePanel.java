import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends Canvas implements Runnable {
    public static final int NUM_ROWS = 40; // Number of rows in the game board
    public static final int NUM_COLUMNS = 40; // Number of columns in the game board
    private static final int CELL_SIZE = 10; // Size of each cell in pixels

    private Snake snake;
    private Node food;
    private boolean running;
    private Thread gameThread;
    private int score;

    public GamePanel() {
        snake = new Snake(10, 10, Color.GREEN); // Initial position of the snake
        food = createFood(); // Initialize food position
        score = 0;

        setPreferredSize(new Dimension(NUM_COLUMNS * CELL_SIZE, NUM_ROWS * CELL_SIZE)); // Set preferred size of the canvas
        setBackground(Color.BLACK); // Set background color

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_UP:
                        snake.setDirection(0); // Up
                        break;
                    case KeyEvent.VK_DOWN:
                        snake.setDirection(2); // Down
                        break;
                    case KeyEvent.VK_LEFT:
                        snake.setDirection(3); // Left
                        break;
                    case KeyEvent.VK_RIGHT:
                        snake.setDirection(1); // Right
                        break;
                }
            }
        });

        setFocusable(true); // Allow the canvas to receive keyboard events
        requestFocus(); // Request focus so that the canvas can start receiving events

        startGame(); // Start the game loop
    }

    private void startGame() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start(); // Start the game loop thread
    }

    @Override
    public void run() {
        while (running) {
            gameUpdate(); // Update game state
            repaint(); // Repaint the canvas
            try {
                Thread.sleep(100); // Adjust delay as needed (controls game speed)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void gameUpdate() {
        snake.move(); // Move the snake

        // Check for collision with food
        if (snake.getHead().x == food.x && snake.getHead().y == food.y) {
            snake.grow(); // Grow the snake
            food = createFood(); // Generate new food
            score++; // Increase score
        }

        // Check for collision with boundaries or itself
        if (snake.checkCollision()) {
            gameOver(); // Game over if collision detected
        }
    }

    private Node createFood() {
        // Generate random position for food
        int x = (int) (Math.random() * NUM_COLUMNS);
        int y = (int) (Math.random() * NUM_ROWS);
        return new Node(x, y);
    }

    private void gameOver() {
        running = false;
        System.out.println("Game Over! Score: " + score);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawSnake(g); // Draw the snake
        drawFood(g); // Draw the food
        drawScore(g); // Draw the score
    }

    private void drawSnake(Graphics g) {
        g.setColor(snake.getColor());
        for (Node segment : snake.getBody()) {
            g.fillRect(segment.x * CELL_SIZE, segment.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
    }

    private void drawFood(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(food.x * CELL_SIZE, food.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }

    private void drawScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Score: " + score, 10, 20);
    }
}
