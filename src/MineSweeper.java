import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSliderUI;

import java.awt.*;
import java.awt.event.MouseListener;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class MineSweeper extends JFrame implements MouseListener, ActionListener {

    // ATTENTION: height is the number of rows and width is the number of columns
    int width = 8, height = 8, mines = 10;
    int seconds = 0, minutes = 0;
    int mineCounter = 1;
    int squareSize = 0;
    int boardFontSize = 4;
    ArrayList<String> scores;

    JPanel selectDifficultyPanel;
    JPanel mainBoard;
    JLabel[][] playBoard;
    JPanel minesPanel;
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
    JLabel minesLeftLabel;

    // Declares all the objects that are necessary for the top menu
    JMenuBar optionBar;
    JMenu otherMenu;
    JMenuItem scoresItem;
    JMenuItem exitItem;
    JMenuItem rulesItem;
    JMenuItem restartItem;
    JMenuItem resetScore;

    // Contains all the numbers and mines of the board (the mines are indicated with the -1 number)
    int[][] numberBoard;
    // A first input is created in order to generate the board after it
    boolean firstInput = false;
    // Makes sure not to iterate useless cycles when the game ends
    boolean endGame = false;
    boolean scoreExist = false;

    // Some height is deleted because of the quick access bar at the bottom of every OS
    int screenHeight = (int)screenDimensions.getHeight() - 235;
    int screenWidth = (int)screenDimensions.getWidth();

    // Utilities for file management
    File scoresFile;
    Scanner reader;

    boolean startMatch = false;
    Border boardBorder = BorderFactory.createLineBorder(new Color(0x2F2E30), 1);
    Color backgroundTheme = new Color(0x121212);
    Color backgroundFlag = new Color(0x1B1B1B);
    Color backgroundCellFound = new Color(0x262626);
    Color panelTheme = new Color(0x1e1e1e);
    Font buttonsFont = new Font("Futura", Font.BOLD, 30);
    Color backgroundMenu = new Color(0xCECECE);
    Color backgroundNotFoundCells = new Color(0x424242);

    public void initialSettings() {
        this.setResizable(true);
        this.setTitle("Minesweeper");
        this.setSize(1200, 700);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        this.getContentPane().setBackground(backgroundTheme);

        // Verifying scoresFile existence and initializing the arraylist with the scores
        setScore();

        // Adding the optionBar
        optionBar = new JMenuBar();
        otherMenu = new JMenu("Altro");
        rulesItem = new JMenuItem("Regole");
        scoresItem = new JMenuItem("Punteggi");
        exitItem = new JMenuItem("Esci");
        restartItem = new JMenuItem("Riavvia");
        resetScore = new JMenuItem("Cancella punteggi");
        this.setJMenuBar(optionBar);
        optionBar.add(otherMenu);
        otherMenu.add(rulesItem);
        otherMenu.add(scoresItem);
        otherMenu.add(resetScore);
        otherMenu.add(exitItem);
        otherMenu.add(restartItem);
        rulesItem.addActionListener(this);
        scoresItem.addActionListener(this);
        resetScore.addActionListener(this);
        exitItem.addActionListener(this);
        restartItem.addActionListener(this);

        // Customizing the optionBar settings
        otherMenu.setFont(new Font("Futura", Font.BOLD, 15));
        rulesItem.setFont(new Font("Futura", Font.BOLD, 20));
        scoresItem.setFont(new Font("Futura", Font.BOLD, 20));
        resetScore.setFont(new Font("Futura", Font.BOLD, 20));
        exitItem.setFont(new Font("Futura", Font.BOLD, 20));
        restartItem.setFont(new Font("Futura", Font.BOLD, 20));
        optionBar.setBackground(backgroundMenu);
        exitItem.setBackground(backgroundMenu);
        scoresItem.setBackground(backgroundMenu);
        resetScore.setBackground(backgroundMenu);
        rulesItem.setBackground(backgroundMenu);
        otherMenu.setForeground(Color.BLACK);
        restartItem.setBackground(backgroundMenu);
    }

    public void selectDifficulty() {
        selectDifficultyPanel.setLayout(null);
        selectDifficultyPanel.setBackground(backgroundTheme);

        // Creation of a dedicated panel to each section: rows, columns, mines
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
        JLabel minesLeft = new JLabel(" ");
        selectRowsPanel.add(rowsNumber);
        selectColumnsPanel.add(columnsNumber);
        selectMinesPanel.add(minesLeft);
        rowsNumber.setForeground(Color.WHITE);
        rowsNumber.setFont(new Font("Futura", Font.BOLD, 30));
        rowsNumber.setBounds(15, 5, 500, 50);
        columnsNumber.setForeground(Color.WHITE);
        columnsNumber.setFont(new Font("Futura", Font.BOLD, 30));
        columnsNumber.setBounds(15, 5, 500, 50);
        minesLeft.setForeground(Color.WHITE);
        minesLeft.setFont(new Font("Futura", Font.BOLD, 30));
        minesLeft.setBounds(15, 5, 500, 50);

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

        // This while loop causes the program to fail the restart
        while (!startMatch) {
            this.repaint();
            rowsNumber.setText("Larghezza: " + selectRows.getValue());
            columnsNumber.setText("Altezza: " + selectColumns.getValue());
            minesLeft.setText("Percentuale mine: " + selectMines.getValue() + "%");
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
        double ratio = (double)width / (double)height;
        if (ratio < 3) {
            boardFontSize = (squareSize - 1) * 8 / 9;
        } else if (ratio < 6){
            boardFontSize = (squareSize - 1) * 5 / 9;
        } else {
            boardFontSize = (squareSize - 1) * 2 / 9;
        }
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

        // Setting the mines counter display JLabel
        minesPanel = new JPanel();
        minesPanel.setBounds(1300, 25, 300, 50);
        minesPanel.setBackground(backgroundTheme);
        minesLeftLabel = new JLabel("Mine rimanenti: " + mineCounter);
        minesLeftLabel.setForeground(Color.WHITE);
        minesLeftLabel.setFont(buttonsFont);
        minesPanel.add(minesLeftLabel);
        this.add(minesPanel);

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

    public int findAroundFlags(int i, int j) {
        int aroundFlags = 0;
        for (int r = -1; r < 2; r++) {
            for (int c = -1; c < 2; c++) {
                try {
                    if (playBoard[i+r][j+c].getIcon() == flag) {
                        aroundFlags++;
                    }
                } catch (IndexOutOfBoundsException e) {}
            }
        }
        return aroundFlags;
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

    public void checkWin() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (playBoard[i][j].getText() == "" && numberBoard[i][j] > 0) {
                    return;
                }
            }
        }
        endGame = true;
        chronometer.stop();

        // Write the record inside the scores file
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter dateType = DateTimeFormatter.ofPattern("dd-MM-yyyy, HH:mm.ss");
        date.format(dateType);
        String newScore = "Campo: " + width + "x" + height + ", mine: " + mines + "% - Tempo: " + minutes + " m, " + seconds + " s; Data: " + date.format(dateType);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("punteggi.txt"));
            for (int i = 0; i < scores.size(); i++) {
                writer.write(scores.get(i) + "\n");
            }
            writer.write(newScore);
            writer.close();
        } catch (IOException e) {}
        setScore();

        String[] choices = {"Nuova partita", "Esci", "Guarda il campo"};
        int endChoice = JOptionPane.showOptionDialog(null, "Hai vinto!", "Vittoria", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, choices, 0);
        if (endChoice == 0) {
            restart();
        } else if (endChoice == 1) {
            this.dispose();
        } else {
            watchBoard();
        }
    }

    public void gameOver() {
        chronometer.stop();
        endGame = true;
        String[] choices = {"Nuova partita", "Esci", "Guarda il campo"};
        int endChoice = JOptionPane.showOptionDialog(null, "Hai preso una mina!", "Fine partita", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, choices, 0);
        if (endChoice == 0) {
            restart();
        } else if (endChoice == 1) {
            this.dispose();
        } else {
            watchBoard();
        }
    }

    public void watchBoard() {
        for (int s = 0; s < height; s++) {
            for (int t = 0; t < width; t++) {
                if (numberBoard[s][t] != -3 && numberBoard[s][t] != 0) {
                    if (numberBoard[s][t] == -1) {
                        if (playBoard[s][t].getIcon() != flag) {
                            playBoard[s][t].setIcon(mineIcon);
                            playBoard[s][t].setBackground(Color.DARK_GRAY);
                        } else {
                            playBoard[s][t].setBackground(new Color(0x014B00));
                        }
                    } else {
                        if (playBoard[s][t].getIcon() == flag) {
                            playBoard[s][t].setBackground(new Color(0x5E0300));
                        } else if (playBoard[s][t].getText() == "" && numberBoard[s][t] > 0){
                            playBoard[s][t].setText("" + numberBoard[s][t]);
                            playBoard[s][t].setBackground(backgroundNotFoundCells);
                        }
                    }
                } else {
                    if (numberBoard[s][t] != -1 && playBoard[s][t].getIcon() == flag) {
                        playBoard[s][t].setBackground(new Color(0x5E0300));
                    } else if (playBoard[s][t].getText() == "" && numberBoard[s][t] > 0){
                        playBoard[s][t].setText("" + numberBoard[s][t]);
                        playBoard[s][t].setBackground(backgroundNotFoundCells);
                    } else if (numberBoard[s][t] == 0) {
                        playBoard[s][t].setBackground(backgroundNotFoundCells);
                    }
                }
            }
        }
    }

    public void uncoverNearNumbers(int i, int j) {
        for (int r = -1; r < 2; r++) {
            for (int c = -1; c < 2; c++) {
                try {
                    if (numberBoard[i+r][j+c] > 0 && playBoard[i+r][j+c].getIcon() != flag && playBoard[i+r][j+c].getIcon() != notSure) {
                        playBoard[i+r][j+c].setText("" + numberBoard[i+r][j+c]);
                        playBoard[i+r][j+c].setBackground(backgroundCellFound);
                    } else if (numberBoard[i+r][j+c] == -1 && playBoard[i+r][j+c].getIcon() != flag && playBoard[i+r][j+c].getIcon() != notSure) {
                        gameOver();
                    } else if (numberBoard[i+r][j+c] == 0) {
                        ifZero(i+r, j+c);
                    }
                } catch (IndexOutOfBoundsException e) {}
            }
        }
    }

    public void restart() {
        CompletableFuture.runAsync(() -> {
            this.dispose();
            new MineSweeper();
        });
    }

    public void setScore() {
        scoresFile = new File("punteggi.txt");
        scores = new ArrayList<>();
        try {
            // If the file doesn't exist it creates one
            if (!scoresFile.createNewFile() || !reader.hasNextLine()) {
                scoreExist = true;
            }
            reader = new Scanner(scoresFile);
            while (reader.hasNextLine()) {
                scores.add(reader.nextLine());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Nessun punteggio rilevato.", "Errore punteggi", JOptionPane.INFORMATION_MESSAGE);
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
        } else if (e.getSource() == rulesItem) {
            String rules = "Campo Minato:\nOgni quadrato viene ripulito, o scoperto, cliccando su di esso. Molti quadrati contengono mine: quando viene cliccato un quadrato con una mina,\nessa esploderà e farà terminare il gioco. Se verrà scoperto un quadrato non contenente una mina, verrà visualizzato all'interno di esso il\nnumero di mine attorno a quel quadrato. L'obiettivo del gioco è ripulire completamente il campo, lasciando coperti oppure contrassegnando\ncon una bandiera, solo i quadrati contenenti le mine.";
            JOptionPane.showMessageDialog(null, rules, "Regole di gioco", JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == exitItem) {
            this.dispose();
            System.exit(0);
        } else if (e.getSource() == restartItem) {
            restart();
        } else if (e.getSource() == scoresItem) {
            int scoreChoice = 0;
            // iterator is the number of scores to skip when viewing (used to scroll through plenty of scores)
            int iterator = 0;
            // loopCounter is used because the program mustn't print more than 15 scores per iteration
            int loopCounter;
            // Used to close the score view, ending the while loop as well
            boolean continueCycle = true;

            String[] choices = {"Indietro", "Avanti", "Chiudi"};
            String scoresToDisplay;

            // If the scores file is empty/doesn't exist it will be displayed a warning telling there is no score recorded
            if (scores.size() != 0) {
                while (continueCycle) {
                    loopCounter = 0;
                    scoresToDisplay = "Verranno mostrati 15 punteggi per pagina:\n\n";

                    for (int i = iterator; loopCounter < 15 && i < scores.size(); i++) {
                        scoresToDisplay = scoresToDisplay + (i + 1) + ":\n";
                        scoresToDisplay = scoresToDisplay + scores.get(i) + "\n\n";
                        loopCounter++;
                    }
                    while (true) {
                        scoreChoice = JOptionPane.showOptionDialog(null, scoresToDisplay, "Punteggi", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, choices, 0);
                        if (scoreChoice == 0) {
                            if (iterator > 14) {
                                iterator -= 15;
                                break;
                            }
                        } else if (scoreChoice == 1) {
                            if (scores.size() > (iterator + 15)) {
                                iterator += 15;
                                break;
                            }
                        } else {
                            continueCycle = false;
                            break;
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Nessun punteggio rilevato.", "Punteggi", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (e.getSource() == resetScore) {
            try {
                FileWriter writer = new FileWriter("punteggi.txt");
                writer.write("");
            } catch (IOException f) {}
            JOptionPane.showMessageDialog(null, "Punteggi cancellati!", "Rimozione punteggi", JOptionPane.INFORMATION_MESSAGE);
            setScore();
        }
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        if ((e.getModifiers() & e.BUTTON1_MASK) != 0 && !endGame) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (e.getSource() == playBoard[i][j] && numberBoard[i][j] > 0 && playBoard[i][j].getText().equals("" + numberBoard[i][j])) {
                        int nearMines = findAroundFlags(i, j);
                        if (nearMines == numberBoard[i][j]) {
                            uncoverNearNumbers(i, j);
                            checkWin();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        if ((e.getModifiers() & e.BUTTON1_MASK) != 0 && !endGame) { // Left mouse
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (e.getSource() == playBoard[i][j]) {
                        if (!firstInput) {
                            firstInput = true;
                            chronometer.start();
                            numberGeneration(i, j);
                        }
                        if (playBoard[i][j].getIcon() != flag && playBoard[i][j].getIcon() != notSure) {
                            if (numberBoard[i][j] == 0) {
                                ifZero(i, j);
                                checkWin();
                            } else if (numberBoard[i][j] > 0) {
                                playBoard[i][j].setText("" + numberBoard[i][j]);
                                playBoard[i][j].setBackground(backgroundCellFound);
                                checkWin();
                            } else if (numberBoard[i][j] == -1){
                                gameOver();
                            }
                        }
                    }
                }
            }
        } else if ((e.getModifiers() & e.BUTTON3_MASK) != 0 && !endGame) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (e.getSource() == playBoard[i][j]) {
                        if (playBoard[i][j].getText().equals("") && playBoard[i][j].getIcon() != flag && playBoard[i][j].getIcon() != notSure && numberBoard[i][j] != -3 && mineCounter != 0) {
                            playBoard[i][j].setIcon(flag);
                            mineCounter--;
                            minesLeftLabel.setText("Mine rimanenti: " + mineCounter);
                            playBoard[i][j].setBackground(backgroundFlag);
                        } else if (playBoard[i][j].getIcon() == flag) {
                            playBoard[i][j].setIcon(notSure);
                        } else if (playBoard[i][j].getIcon() == notSure){
                            playBoard[i][j].setIcon(null);
                            mineCounter++;
                            minesLeftLabel.setText("Mine rimanenti: " + mineCounter);
                            playBoard[i][j].setBackground(backgroundTheme);
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
