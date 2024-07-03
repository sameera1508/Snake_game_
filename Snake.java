import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Snake {
    private List<Node> body; // List to store all segments of the snake
    private int direction; // Direction of movement (0 = up, 1 = right, 2 = down, 3 = left)
    private Color color; // Snake color (optional)
    private boolean growPending; // Flag to track if the snake should grow

    public Snake(int startX, int startY, Color color) {
        this.body = new ArrayList<>();
        this.body.add(new Node(startX, startY)); // Initialize snake with one segment
        this.direction = 1; // Start moving right initially
        this.color = color;
        this.growPending = false;
    }

    public void move() {
        // Move the snake by adding a new head in the current direction
        Node head = getHead();
        Node newHead;
        switch (direction) {
            case 0: // Up
                newHead = new Node(head.x, head.y - 1);
                break;
            case 1: // Right
                newHead = new Node(head.x + 1, head.y);
                break;
            case 2: // Down
                newHead = new Node(head.x, head.y + 1);
                break;
            case 3: // Left
                newHead = new Node(head.x - 1, head.y);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
        
        body.add(0, newHead); // Add new head to the front
        if (!growPending) {
            body.remove(body.size() - 1); // Remove tail segment if not growing
        } else {
            growPending = false; // Reset grow flag
        }
        
        System.out.println("Snake position: (" + newHead.x + ", " + newHead.y + ")"); // Debugging statement
    }

    public void setDirection(int direction) {
        // Prevent 180-degree turns
        if (this.direction == 0 && direction == 2 ||
            this.direction == 2 && direction == 0 ||
            this.direction == 1 && direction == 3 ||
            this.direction == 3 && direction == 1) {
            return;
        }
        this.direction = direction;
    }

    public void grow() {
        // Set flag to grow snake in the next move
        growPending = true;
    }

    public boolean checkCollision() {
        Node head = getHead();
        // Check collision with boundaries
        if (head.x < 0 || head.x >= GamePanel.NUM_COLUMNS || head.y < 0 || head.y >= GamePanel.NUM_ROWS) {
            return true;
        }
        // Check collision with itself
        for (int i = 1; i < body.size(); i++) {
            Node segment = body.get(i);
            if (head.x == segment.x && head.y == segment.y) {
                return true;
            }
        }
        return false;
    }

    // Getter for head position (useful for collision detection, etc.)
    public Node getHead() {
        return body.get(0);
    }

    // Getter for snake body
    public List<Node> getBody() {
        return body;
    }

    // Getter for snake color
    public Color getColor() {
        return color;
    }
}
