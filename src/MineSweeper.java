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
    int seconds = 0, minutes = 0;
    int mineCounter;
    int squareSize = 0;
    int boardFontSize = 4;

    JPanel selectDifficultyPanel;
    JPanel mainBoard;
    JLabel[][] playBoard;
    JSlider selectRows, selectColumns, selectMines;
    ImageIcon flag;
    ImageIcon notSure;
    ImageIcon mineIcon;
    JButton confirmButton = new JButton("Gioca");
    JButton fieldOptions9x9;
    JButton fieldOptions16x16;
    JButton fieldOptions30x16;
    Dimension screenDimensions = Toolkit.getDefaultToolkit().getScreenSize();
    Timer chronometer;
    JPanel chronometerPanel;
    JLabel displayTime;

    // Contains all the numbers and mines of the board (the mines are indicated with the -1 number)
    int[][] numberBoard;
    // A first input is created in order to generate the board after it
    boolean firstInput = false;

    // Some height is deleted because of the quick access bar at the bottom of every OS
    int screenHeight = (int)screenDimensions.getHeight() - 201;
    int screenWidth = (int)screenDimensions.getWidth();

    boolean startMatch = false;
    Border boardBorder = BorderFactory.createLineBorder(new Color(0x2F2E30), 1);
    Color backgroundTheme = new Color(0x121212);
    Color backgroundCellFound = new Color(0x262626);
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

        selectRows = new JSlider(8, 65, 8);
        selectColumns = new JSlider(8, 65, 8);
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

    public void boardGeneration() {
        // Setting the correct width based on the number of rows and columns
        squareSize = screenHeight / height;
        // A chosen font size standard for all dimensions
        boardFontSize = (squareSize - 1) * 8 / 9;
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

        // play board creation: matrix declaration
        playBoard = new JLabel[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                playBoard[i][j] = new JLabel();
                mainBoard.add(playBoard[i][j]);
                playBoard[i][j].setOpaque(true);
                playBoard[i][j].setBackground(backgroundTheme);
                playBoard[i][j].setForeground(Color.WHITE);
                playBoard[i][j].setBorder(boardBorder);
                playBoard[i][j].setHorizontalTextPosition(JLabel.CENTER);
                playBoard[i][j].setHorizontalTextPosition(JLabel.CENTER);
                playBoard[i][j].setHorizontalAlignment(JLabel.CENTER);
                playBoard[i][j].setVerticalAlignment(JLabel.CENTER);
                playBoard[i][j].addMouseListener(this);
                playBoard[i][j].setFont(new Font("Futura", Font.BOLD, boardFontSize));
            }
        }
    }

    public void timerGeneration() {
        // Chronometer declaration
        chronometer = new Timer(1000, this);
        chronometer.start();

        // Adding the chronometer panel to the main screen
        chronometerPanel = new JPanel();
        this.add(chronometerPanel);
        chronometerPanel.setBounds(10, 25, screenWidth - 10, 100);
        displayTime = new JLabel("00:00");
        chronometerPanel.add(displayTime);

        // Panel design
        chronometerPanel.setBackground(backgroundTheme);
        displayTime.setForeground(Color.WHITE);
        displayTime.setFont(buttonsFont);
    }

    public void numberGeneration(int ignoreRow, int ignoreColumn) {
        Random randomizer = new Random();
        numberBoard = new int[height][width];
        // Assigning 0 to all cells
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                numberBoard[i][j] = 0;
            }
        }

        // Calculation of the mines based on the percentage, width and height
        int totalCellNumber = width * height;
        int mineNumber = totalCellNumber * mines / 100;
        mineCounter = mineNumber;
        int assignRow, assignColumn;
        while (mineNumber > 0) {
            assignRow = randomizer.nextInt(0, height);
            assignColumn = randomizer.nextInt(0, width);
            // The "and" condition was added in order not to delete mineNumbers if the randomisation was equal to the first input
            if (numberBoard[assignRow][assignColumn] != -1 && (assignRow != ignoreRow || assignColumn != ignoreColumn)) {
                mineNumber--;
            }
            if (assignRow != ignoreRow || assignColumn != ignoreColumn) {
                numberBoard[assignRow][assignColumn] = -1;
            }
        }

        // Assigning numbers to all cells
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (numberBoard[i][j] != -1) {
                    int num = 0;
                    // Checks all the cells around the selected one (around numberBoard[i][j]
                    for (int r = -1; r < 2; r++) {
                        for (int c = -1; c < 2; c++) {
                            try {
                                if (numberBoard[i+r][j+c] == -1) {
                                    num++;
                                }
                            } catch (IndexOutOfBoundsException e) {}
                        }
                    }
                    if (num == 0) {
                        playBoard[i][j].setText("0");
                    }
                    numberBoard[i][j] = num;
                    paintNumbers(i, j);
                }
            }
        }
    }

    // Used for painting the numbers based on their number
    public void paintNumbers(int i, int j) {
        switch(numberBoard[i][j]) {
            case 1:
                playBoard[i][j].setForeground(new Color(38, 122,255,255));
                break;
            case 2:
                playBoard[i][j].setForeground(new Color(0,128,0,255));
                break;
            case 3:
                playBoard[i][j].setForeground(new Color(255,0,0,255));
                break;
            case 4:
                playBoard[i][j].setForeground(new Color(65, 89,128,255));
                break;
            case 5:
                playBoard[i][j].setForeground(new Color(128,0,0,255));
                break;
            case 6:
                playBoard[i][j].setForeground(new Color(0,128,128,255));
                break;
            case 7:
                playBoard[i][j].setForeground(new Color(0,0,0,255));
                break;
            case 8:
                playBoard[i][j].setForeground(new Color(128,128,128,255));
                break;
        }
    }

    // Information for findAround() and ifZero() about the result -> -1 (mine in the cell), -2 (index went out of bound), -3 (cells with 0 that have been visited)
    public int[] findAround(int i, int j) {
        int[] result = new int[9];
        int k = 0;
        for (int r = -1; r < 2; r++) {
            for (int c = -1; c < 2; c++) {
                try {
                    result[k] = numberBoard[i+r][j+c];
                    k++;
                } catch (IndexOutOfBoundsException e) {
                    result[k] = -2;
                    k++;
                }
            }
        }
        return result;
    }

    public void ifZero(int i, int j) {
        playBoard[i][j].setText("!");
        numberBoard[i][j] = -3;
        int[] around = findAround(i, j);
        ArrayList<Integer> zeros = new ArrayList<Integer>();
        for (int k = 0; k < around.length; k++) {
            // if around[k] is > 0 we need to display the number in it
            if (around[k] > 0) {
                playBoard[i][j].setBackground(backgroundCellFound);
                switch (k) {
                    case 0:
                        playBoard[i - 1][j - 1].setText("" + numberBoard[i - 1][j - 1]);
                        playBoard[i - 1][j - 1].setBackground(backgroundCellFound);
                        break;
                    case 1:
                        playBoard[i - 1][j].setText("" + numberBoard[i - 1][j]);
                        playBoard[i - 1][j].setBackground(backgroundCellFound);
                        break;
                    case 2:
                        playBoard[i - 1][j + 1].setText("" + numberBoard[i - 1][j + 1]);
                        playBoard[i - 1][j + 1].setBackground(backgroundCellFound);
                        break;
                    case 3:
                        playBoard[i][j - 1].setText("" + numberBoard[i][j - 1]);
                        playBoard[i][j - 1].setBackground(backgroundCellFound);
                        break;
                    case 5:
                        playBoard[i][j + 1].setText("" + numberBoard[i][j + 1]);
                        playBoard[i][j + 1].setBackground(backgroundCellFound);
                        break;
                    case 6:
                        playBoard[i + 1][j - 1].setText("" + numberBoard[i + 1][j - 1]);
                        playBoard[i + 1][j - 1].setBackground(backgroundCellFound);
                        break;
                    case 7:
                        playBoard[i + 1][j].setText("" + numberBoard[i + 1][j]);
                        playBoard[i + 1][j].setBackground(backgroundCellFound);
                        break;
                    case 8:
                        playBoard[i + 1][j + 1].setText("" + numberBoard[i + 1][j + 1]);
                        playBoard[i + 1][j + 1].setBackground(backgroundCellFound);
                        break;
                }
            } else if (around[k] == 0) {
                playBoard[i][j].setBackground(backgroundCellFound);
                switch (k) {
                    case 0:
                        playBoard[i - 1][j - 1].setText("!");
                        numberBoard[i - 1][j - 1] = -3;
                        break;
                    case 1:
                        playBoard[i - 1][j].setText("!");
                        numberBoard[i - 1][j] = -3;
                        break;
                    case 2:
                        playBoard[i - 1][j + 1].setText("!");
                        numberBoard[i - 1][j + 1] = -3;
                        break;
                    case 3:
                        playBoard[i][j - 1].setText("!");
                        numberBoard[i][j - 1] = -3;
                        break;
                    case 5:
                        playBoard[i][j + 1].setText("!");
                        numberBoard[i][j + 1] = -3;
                        break;
                    case 6:
                        playBoard[i + 1][j - 1].setText("!");
                        numberBoard[i + 1][j - 1] = -3;
                        break;
                    case 7:
                        playBoard[i + 1][j].setText("!");
                        numberBoard[i + 1][j] = -3;
                        break;
                    case 8:
                        playBoard[i + 1][j + 1].setText("!");
                        numberBoard[i + 1][j + 1] = -3;
                        break;
                }
                zeros.add(k);
            }
        }
        for (int k = 0; k < zeros.size(); k++) {
            switch (zeros.get(k)) {
                case 0:
                    ifZero(i-1, j-1);
                    break;
                case 1:
                    ifZero(i-1, j);
                    break;
                case 2:
                    ifZero(i-1, j+1);
                    break;
                case 3:
                    ifZero(i, j-1);
                    break;
                case 5:
                    ifZero(i, j+1);
                    break;
                case 6:
                    ifZero(i+1, j-1);
                    break;
                case 7:
                    ifZero(i+1, j);
                    break;
                case 8:
                    ifZero(i+1, j+1);
                    break;
            }
        }
        playBoard[i][j].setText("");
        playBoard[i][j].setBackground(backgroundCellFound);
    }

    MineSweeper() {
        initialSettings();
        selectDifficultyPanel = new JPanel();
        this.add(selectDifficultyPanel);
        selectDifficulty();
        this.setLayout(null);
        mainBoard = new JPanel();
        this.add(mainBoard);
        boardGeneration();
        timerGeneration();
        flag = new ImageIcon(new ImageIcon("flag.png").getImage().getScaledInstance(squareSize, squareSize, Image.SCALE_SMOOTH));
        notSure = new ImageIcon(new ImageIcon("notsure.png").getImage().getScaledInstance(squareSize, squareSize, Image.SCALE_SMOOTH));
        mineIcon = new ImageIcon(new ImageIcon("mine.png").getImage().getScaledInstance(squareSize, squareSize, Image.SCALE_SMOOTH));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == chronometer) {
            seconds++;
            chronometerPanel.repaint();
            if (seconds == 60) {
                minutes++;
                seconds = 0;
                if (minutes == 60) {
                    minutes = 59;
                    seconds = 59;
                    chronometer.stop();
                }
            }
            // Display the first zero for single bit numbers in the chronometer
            if (minutes < 10 && seconds < 10) {
                displayTime.setText("0" + minutes + ":" + "0" + seconds);
            } else if (minutes < 10 && seconds >= 10) {
                displayTime.setText("0" + minutes + ":" + seconds);
            } else if (minutes >= 10 && seconds < 10) {
                displayTime.setText(minutes + ":" + "0" + seconds);
            } else if (minutes >= 10 && seconds >= 10) {
                displayTime.setText(minutes + ":" + seconds);
            }
        }
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
        if ((e.getModifiers() & e.BUTTON1_MASK) != 0) { // Left mouse
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (e.getSource() == playBoard[i][j]) {
                        if (!firstInput) {
                            firstInput = true;
                            numberGeneration(i, j);
                        }
                        if (playBoard[i][j].getIcon() != flag && playBoard[i][j].getIcon() != notSure) {
                            if (numberBoard[i][j] == 0) {
                                ifZero(i, j);
                            } else if (numberBoard[i][j] > 0) {
                                playBoard[i][j].setText("" + numberBoard[i][j]);
                                playBoard[i][j].setBackground(backgroundCellFound);
                            } else if (numberBoard[i][j] == -1){
                                chronometer.stop();
                                String[] choices = {"Nuova partita", "Esci", "Guarda il campo"};
                                int endChoice = JOptionPane.showOptionDialog(null, "Hai preso una mina!", "Fine partita", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, choices, 0);
                                if (endChoice == 0) {
                                    Main.main(null);
                                    this.dispose();
                                    System.exit(0);
                                } else if (endChoice == 1) {
                                    this.dispose();
                                    System.exit(0);
                                } else {
                                    for (int s = 0; s < height; s++) {
                                        for (int t = 0; t < width; t++) {
                                            if (numberBoard[s][t] != -3 && numberBoard[s][t] != 0) {
                                                if (numberBoard[s][t] == -1) {
                                                    playBoard[s][t].setText("!");
                                                } else {
                                                    playBoard[s][t].setText("" + numberBoard[s][t]);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if ((e.getModifiers() & e.BUTTON3_MASK) != 0) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (e.getSource() == playBoard[i][j]) {
                        if (playBoard[i][j].getText().equals("") && playBoard[i][j].getIcon() != flag && playBoard[i][j].getIcon() != notSure) {
                            playBoard[i][j].setIcon(flag);
                        } else if (playBoard[i][j].getText().equals("") && playBoard[i][j].getIcon() == flag) {
                            playBoard[i][j].setIcon(notSure);
                        } else {
                            playBoard[i][j].setIcon(null);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {

    }

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {
        /*for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

            }
        }*/
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {

    }
}
