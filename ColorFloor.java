
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class ColorFloor extends JFrame implements ActionListener {
    
    // Constants for grid size and cell size
    private static final int GRID_SIZE = 30;
    private static final int CELL_SIZE = 20;

    // UI Components
    private JPanel drawingPanel;
    private JTextField inputField;
    private JButton drawButton;

    // Pixel data to represent the grid
    private int[][] pixelData;
    private int currentColor; // Current color for drawing
    private boolean isDrawing; // Flag to indicate drawing mode
    private boolean isDiscoMode; // Flag for disco mode

    public ColorFloor() {
        // Set up the frame
        setTitle("Color Floor");
        setSize(GRID_SIZE * CELL_SIZE, GRID_SIZE * CELL_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize pixel data
        pixelData = new int[GRID_SIZE][GRID_SIZE];
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                pixelData[row][col] = Color.WHITE.getRGB(); // Start with white cells
            }
        }

        // Set up the drawing panel
        drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int row = 0; row < GRID_SIZE; row++) {
                    for (int col = 0; col < GRID_SIZE; col++) {
                        g.setColor(new Color(pixelData[row][col]));
                        g.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    }
                }
            }
        };

        // Add mouse click listener for drawing on the panel
        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isDrawing) {
                    int col = e.getX() / CELL_SIZE;
                    int row = e.getY() / CELL_SIZE;
                    if (row < GRID_SIZE && col < GRID_SIZE) { // Ensure within bounds
                        pixelData[row][col] = currentColor;
                        drawingPanel.repaint();
                    }
                }
            }
        });

        // Set up input panel with text field and button
        JPanel inputPanel = new JPanel();
        inputField = new JTextField(1);
        drawButton = new JButton("Draw");
        drawButton.addActionListener(this);
        inputField.addActionListener(this); // Handle input when Enter is pressed
        inputPanel.add(inputField);
        inputPanel.add(drawButton);

        // Add components to the frame
        add(drawingPanel, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        // Set the default color for drawing to black
        currentColor = Color.BLACK.getRGB();

        setVisible(true); // Make the frame visible
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        processInput(); // Process input from button or text field
    }

    private void processInput() {
        String input = inputField.getText().toLowerCase().trim();
        inputField.setText(""); // Clear the input field

        switch (input) {
            case "p": // Activate drawing mode with black color
                isDrawing = true;
                currentColor = Color.BLACK.getRGB();
                break;
            case "e": // Activate drawing mode with white color (eraser)
                isDrawing = true;
                currentColor = Color.WHITE.getRGB();
                break;
            case "d": // Start disco mode with random colors
                isDiscoMode = true;
                new Thread(this::runDiscoMode).start();
                break;
            case "s": // Stop drawing and disco modes
                isDrawing = false;
                isDiscoMode = false;
                break;
            default:
                JOptionPane.showMessageDialog(this, "Invalid input. Use 'p', 'e', 'd', or 's'.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
        }
    }

    private void runDiscoMode() {
        while (isDiscoMode) {
            for (int row = 0; row < GRID_SIZE; row++) {
                for (int col = 0; col < GRID_SIZE; col++) {
                    pixelData[row][col] = new Random().nextInt(0xFFFFFF); // Assign random color
                }
            }
            drawingPanel.repaint(); // Update the panel

            try {
                Thread.sleep(100); // Pause for disco effect
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        new ColorFloor(); // Create an instance of the application
    }
}
