import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSliderUI;

import java.awt.*;
import java.awt.event.MouseListener;
import java.util.Random;

public class MineSweeper extends JFrame implements MouseListener, ActionListener {

    // ATTENTION: height is the number of rows and width is the number of columns
    int width = 8, height = 8, mines = 10;

    JPanel selectDifficultyPanel;
    JPanel mainBoard;
    JLabel[][] playBoard;
    JSlider selectRows, selectColumns, selectMines;
    ImageIcon flag = new ImageIcon("flag.jpg");
    JButton confirmButton = new JButton("Gioca");
    JButton fieldOptions9x9;
    JButton fieldOptions16x16;
    JButton fieldOptions30x16;
    Dimension screenDimensions = Toolkit.getDefaultToolkit().getScreenSize();

    // Some height is deleted because of the quick access bar at the bottom of every OS
    int screenHeight = (int)screenDimensions.getHeight() - 201;
    int screenWidth = (int)screenDimensions.getWidth();

    boolean startMatch = false;
    Border test = BorderFactory.createLineBorder(Color.WHITE, 1);
    Border boardBorder = BorderFactory.createLineBorder(new Color(0x7c7b7d), 1);
    Color backgroundTheme = new Color(0x121212);
    Color panelTheme = new Color(0x1e1e1e);
    Font buttonsFont = new Font("Futura", Font.BOLD, 30);

    public void initialSettings() {
        this.setResizable(true);
        this.setTitle("Minesweeper");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        this.getContentPane().setBackground(backgroundTheme);
    }

    public void selectDifficulty() {
        selectDifficultyPanel.setLayout(null);
        selectDifficultyPanel.setBackground(backgroundTheme);

        // Creation of a dedicated panel to each section: rows, columns, mines
        GridLayout selectionLayout = new GridLayout(2, 1);
        JPanel selectRowsPanel = new JPanel();
        JPanel selectColumnsPanel = new JPanel();
        JPanel selectMinesPanel = new JPanel();
        selectRowsPanel.setLayout(null);
        selectRowsPanel.setBackground(panelTheme);
        selectRowsPanel.setBounds(40, 40, 500, 100);
        selectColumnsPanel.setLayout(null);
        selectColumnsPanel.setBackground(panelTheme);
        selectColumnsPanel.setBounds(700, 40, 500, 100);
        selectMinesPanel.setLayout(null);
        selectMinesPanel.setBackground(panelTheme);
        selectMinesPanel.setBounds(1350, 40, 500, 100);

        // Label regarding the number chosen in the JSlider
        JLabel rowsNumber = new JLabel(" ");
        JLabel columnsNumber = new JLabel(" ");
        JLabel minesNumber = new JLabel(" ");
        selectRowsPanel.add(rowsNumber);
        selectColumnsPanel.add(columnsNumber);
        selectMinesPanel.add(minesNumber);
        rowsNumber.setForeground(Color.WHITE);
        rowsNumber.setFont(new Font("Futura", Font.BOLD, 30));
        rowsNumber.setBounds(15, 5, 500, 50);
        columnsNumber.setForeground(Color.WHITE);
        columnsNumber.setFont(new Font("Futura", Font.BOLD, 30));
        columnsNumber.setBounds(15, 5, 500, 50);
        minesNumber.setForeground(Color.WHITE);
        minesNumber.setFont(new Font("Futura", Font.BOLD, 30));
        minesNumber.setBounds(15, 5, 500, 50);

        selectRows = new JSlider(8, 75, 8);
        selectColumns = new JSlider(8, 75, 8);
        selectMines = new JSlider(5, 99, 5);
        selectRows.setBackground(panelTheme);
        selectColumns.setBackground(panelTheme);
        selectMines.setBackground(panelTheme);
        selectRows.setBounds(15, 70, 475, 15);
        selectColumns.setBounds(15, 70, 475, 15);
        selectMines.setBounds(15, 70, 475, 15);
        selectRowsPanel.add(selectRows);
        selectColumnsPanel.add(selectColumns);
        selectMinesPanel.add(selectMines);

        confirmButton.setPreferredSize(new Dimension(100, 50));
        confirmButton.setFocusable(false);
        confirmButton.setBackground(new Color(0xc38fff));
        confirmButton.addActionListener(this);
        confirmButton.setBounds(40, 200, 200, 70);
        confirmButton.setFont(buttonsFont);
        confirmButton.setForeground(new Color(0x191220));

        // JPanel used as a separator between the suggested levels and the selection part
        JPanel separatingLine = new JPanel();
        separatingLine.setBounds(20, 350, 1850, 8);
        separatingLine.setBorder(BorderFactory.createLineBorder(panelTheme, 4));


        // Adding the default options under the button
        fieldOptions9x9 = new JButton("Principiante 9x9");
        fieldOptions16x16 = new JButton("Intermedio 16x16");
        fieldOptions30x16 = new JButton("Esperto 30x16");
        fieldOptions9x9.setBounds(40, 430, 300, 70);
        fieldOptions9x9.setFont(buttonsFont);
        fieldOptions9x9.setFocusable(false);
        fieldOptions9x9.setBackground(new Color(0xc38fff));
        fieldOptions9x9.setForeground(new Color(0x191220));
        fieldOptions9x9.addActionListener(this);
        fieldOptions16x16.setBounds(500, 430, 300, 70);
        fieldOptions16x16.setFont(buttonsFont);
        fieldOptions16x16.setFocusable(false);
        fieldOptions16x16.setBackground(new Color(0xc38fff));
        fieldOptions16x16.setForeground(new Color(0x191220));
        fieldOptions16x16.addActionListener(this);
        fieldOptions30x16.setBounds(950, 430, 300, 70);
        fieldOptions30x16.setFont(buttonsFont);
        fieldOptions30x16.setFocusable(false);
        fieldOptions30x16.setBackground(new Color(0xc38fff));
        fieldOptions30x16.setForeground(new Color(0x191220));
        fieldOptions30x16.addActionListener(this);


        // Adding all the subpanels to the main panel
        selectDifficultyPanel.add(selectRowsPanel);
        selectDifficultyPanel.add(selectColumnsPanel);
        selectDifficultyPanel.add(selectMinesPanel);
        selectDifficultyPanel.add(confirmButton);
        selectDifficultyPanel.add(separatingLine);
        selectDifficultyPanel.add(fieldOptions9x9);
        selectDifficultyPanel.add(fieldOptions16x16);
        selectDifficultyPanel.add(fieldOptions30x16);

        while (!startMatch) {
            rowsNumber.setText("Larghezza: " + selectRows.getValue());
            columnsNumber.setText("Altezza: " + selectColumns.getValue());
            minesNumber.setText("Percentuale mine: " + selectMines.getValue() + "%");
        }
        width = selectRows.getValue();
        height = selectColumns.getValue();
        mines = selectMines.getValue();

        // Removes everything on screen
        this.getContentPane().removeAll();
        this.repaint();
    }

    public void boardCreation() {
        // Setting the correct width based on the number of rows and columns
        int squareSize = screenHeight / height;
        int boardWidth = squareSize * width;
        int boardHeight = screenHeight;
        // Handling the case where the boardWidth becomes higher than the screenWidth
        if (boardWidth > screenWidth) {
            squareSize = screenWidth / width;
            boardHeight = squareSize * height;
            boardWidth = screenWidth;
            mainBoard.setBounds(0, 130, boardWidth, boardHeight);
        } else {
            int emptySpaceBoard = (screenWidth - boardWidth) / 2;
            mainBoard.setBounds(emptySpaceBoard, 130, boardWidth, boardHeight);
        }

        // Settings regarding the main board
        mainBoard.setBackground(backgroundTheme);
        mainBoard.setLayout(new GridLayout(height, width));
        mainBoard.setBorder(test);

        // play board creation: matrix declaration
        playBoard = new JLabel[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                playBoard[i][j] = new JLabel();
                mainBoard.add(playBoard[i][j]);
                playBoard[i][j].setForeground(Color.WHITE);
                playBoard[i][j].setBorder(boardBorder);
            }
        }
    }

    MineSweeper() {
        initialSettings();
        selectDifficultyPanel = new JPanel();
        this.add(selectDifficultyPanel);
        selectDifficulty();
        this.setLayout(null);
        mainBoard = new JPanel();
        this.add(mainBoard);
        boardCreation();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == confirmButton) {
            startMatch = true;
        } else if (e.getSource() == fieldOptions9x9) {
            selectRows.setValue(9);
            selectColumns.setValue(9);
            selectMines.setValue(12);
        } else if (e.getSource() == fieldOptions16x16) {
            selectRows.setValue(16);
            selectColumns.setValue(16);
            selectMines.setValue(16);
        } else if (e.getSource() == fieldOptions30x16) {
            selectRows.setValue(30);
            selectColumns.setValue(16);
            selectMines.setValue(21);
        }
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {

    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {

    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {

    }

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {

    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {

    }
}
