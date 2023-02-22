import javax.swing.*;
import java.util.ArrayList;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.MouseListener;
import java.util.Random;

public class App extends JFrame implements MouseListener {

    int w = 16, h = 16;
    JPanel[][] sfondoCampo;
    JLabel[][] etic;
    JPanel game;
    Random rand = new Random();
    ImageIcon bandiera;

    String[][] campo = new String[w][h];

    App() {
        this.setLayout(new FlowLayout());
        this.setResizable(true);
        this.setSize(1200, 700);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        this.getContentPane().setBackground(Color.DARK_GRAY);

        game = new JPanel();
        game.setLayout(new GridLayout(h, w));
        game.setBackground(Color.LIGHT_GRAY);
        game.setPreferredSize(new Dimension(600, 605)); // Da tenere sempre in rapporto tra larghezza e altezza

        bandiera = new ImageIcon("red-flag-icon-18.jpg");
        etic = new JLabel[w][h];
        sfondoCampo = new JPanel[w][h];

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (rand.nextInt(0, 13) > 9) {
                    campo[i][j] = "X";
                } else {
                    campo[i][j] = "0";
                }
                sfondoCampo[i][j] = new JPanel();
                etic[i][j] = new JLabel();
                sfondoCampo[i][j].setBorder(BorderFactory.createLineBorder(Color.black, 2));
                etic[i][j].setFont(new Font("Calibri", Font.BOLD, 30));
                etic[i][j].setForeground(Color.LIGHT_GRAY);
                etic[i][j].setHorizontalTextPosition(JLabel.CENTER);
                etic[i][j].setVerticalTextPosition(JLabel.CENTER);
                sfondoCampo[i][j].add(etic[i][j]);
                game.add(sfondoCampo[i][j]);
                sfondoCampo[i][j].setSize(100, 100);
                sfondoCampo[i][j].setLayout(new FlowLayout());
                etic[i][j].setSize(90, 90);
                etic[i][j].addMouseListener(this);
                etic[i][j].setVerticalAlignment(JLabel.CENTER);
                etic[i][j].setHorizontalAlignment(JLabel.CENTER);
            }
        }

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (campo[i][j] != "X") {
                    etic[i][j].setForeground(Color.RED);
                    int num = 0;
                    for (int r = -1; r < 2; r++) { // Controlla tutte le caselle attorno il numero
                        for (int c = -1; c < 2; c++) {
                            try {
                                if (campo[i+r][j+c] == "X") {
                                    num++;
                                }
                            } catch (IndexOutOfBoundsException e) {}
                        }
                    }
                    campo[i][j] = String.valueOf(num);
                }
            }
        }

        this.add(game);
        this.setVisible(true);
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {

    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        if ((e.getModifiers() & e.BUTTON1_MASK) != 0) { // Tasto sinistro
            for (int i = 0; i < w; i++) {
                for (int j = 0; j < h; j++) {
                    if (e.getSource() == etic[i][j]) {
                        if (etic[i][j].getText() == "!" || etic[i][j].getText() == "?") {
                            break;
                        } else if (campo[i][j].equals("X")) {
                            String[] scelte = {"Nuova partita", "Esci", "Guarda il campo"};
                            int fine = JOptionPane.showOptionDialog(null, "Hai preso una mina!", "Fine partita", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, scelte, 0);
                            if (fine == 0) {
                                new App();
                                this.dispose();
                            } else if (fine == 1) {
                                this.dispose();
                            } else {
                                for (int s = 0; s < w; s++) {
                                    for (int t = 0; t < h; t++) {
                                        if (!campo[s][t].equals("!") && !campo[s][t].equals("0")) {
                                            etic[s][t].setText(campo[s][t]);
                                            coloraNumeri(s, t);
                                        }
                                    }
                                }
                            }
                        } else if (campo[i][j].equals("0")) {
                            seZero(i, j);
                        } else if (campo[i][j].equals("!")) {

                        } else {
                            etic[i][j].setText(campo[i][j]);
                            coloraNumeri(i, j);
                        }
                    }
                }
            }
        } else if ((e.getModifiers() & e.BUTTON3_MASK) != 0) {
            for (int i = 0; i < w; i++) {
                for (int j = 0; j < h; j++) {
                    if (e.getSource() == etic[i][j]) {
                        if (etic[i][j].getText() == "") {
                            etic[i][j].setText("!");
                            etic[i][j].setForeground(Color.WHITE);
                        } else if (etic[i][j].getText() == "!") {
                            etic[i][j].setText("?");
                            etic[i][j].setForeground(Color.GRAY);
                        } else if (etic[i][j].getText() == "?") {
                            etic[i][j].setText("");
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
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (e.getSource() == etic[i][j]) {
                    etic[i][j].setBackground(Color.BLACK);
                }
            }
        }
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (e.getSource() == etic[i][j]) {
                    etic[i][j].setBackground(Color.GRAY);
                }
            }
        }
    }

    public String[] trovaLati(int i, int j) {
        String[] ris = new String[9];
        int k = 0;
        for (int r = -1; r < 2; r++) {
            for (int c = -1; c < 2; c++) {
                try {
                    ris[k] = campo[i+r][j+c];
                    k++;
                } catch (IndexOutOfBoundsException e) {
                    ris[k] = "-1";
                    k++;
                }
            }
        }
        return ris;
    }

    public void seZero(int i, int j) {
        etic[i][j].setText("!");
        campo[i][j] = ("!");
        String[] lati = trovaLati(i, j);
        ArrayList<Integer> zeri = new ArrayList<Integer>();
        for (int k = 0; k < lati.length; k++) {
            if (lati[k].equals("1") || lati[k].equals("2") || lati[k].equals("3") || lati[k].equals("4") ||
                lati[k].equals("5") || lati[k].equals("6") || lati[k].equals("7") || lati[k].equals("8")) {
                switch (k) {
                    case 0:
                        etic[i - 1][j - 1].setText(campo[i - 1][j - 1]);
                        coloraNumeri(i - 1, j - 1);
                        break;
                    case 1:
                        etic[i - 1][j].setText(campo[i - 1][j]);
                        coloraNumeri(i - 1, j);
                        break;
                    case 2:
                        etic[i - 1][j + 1].setText(campo[i - 1][j + 1]);
                        coloraNumeri(i - 1, j + 1);
                        break;
                    case 3:
                        etic[i][j - 1].setText(campo[i][j - 1]);
                        coloraNumeri(i, j - 1);
                        break;
                    case 5:
                        etic[i][j + 1].setText(campo[i][j + 1]);
                        coloraNumeri(i, j + 1);
                        break;
                    case 6:
                        etic[i + 1][j - 1].setText(campo[i + 1][j - 1]);
                        coloraNumeri(i + 1, j - 1);
                        break;
                    case 7:
                        etic[i + 1][j].setText(campo[i + 1][j]);
                        coloraNumeri(i + 1, j);
                        break;
                    case 8:
                        etic[i + 1][j + 1].setText(campo[i + 1][j + 1]);
                        coloraNumeri(i + 1, j + 1);
                        break;
                }
            } else if (lati[k].equals("0")) {
                switch (k) {
                    case 0:
                        etic[i - 1][j - 1].setText("!");
                        campo[i - 1][j - 1] = "!";
                        break;
                    case 1:
                        etic[i - 1][j].setText("!");
                        campo[i - 1][j] = "!";
                        break;
                    case 2:
                        etic[i - 1][j + 1].setText("!");
                        campo[i - 1][j + 1] = "!";
                        break;
                    case 3:
                        etic[i][j - 1].setText("!");
                        campo[i][j - 1] = "!";
                        break;
                    case 5:
                        etic[i][j + 1].setText("!");
                        campo[i][j + 1] = "!";
                        break;
                    case 6:
                        etic[i + 1][j - 1].setText("!");
                        campo[i + 1][j - 1] = "!";
                        break;
                    case 7:
                        etic[i + 1][j].setText("!");
                        campo[i + 1][j] = "!";
                        break;
                    case 8:
                        etic[i + 1][j + 1].setText("!");
                        campo[i + 1][j + 1] = "!";
                        break;
                }
                zeri.add(k);
            }
        }
        for (int k = 0; k < zeri.size(); k++) {
            switch (zeri.get(k)) {
                case 0:
                    seZero(i-1, j-1);
                    break;
                case 1:
                    seZero(i-1, j);
                    break;
                case 2:
                    seZero(i-1, j+1);
                    break;
                case 3:
                    seZero(i, j-1);
                    break;
                case 5:
                    seZero(i, j+1);
                    break;
                case 6:
                    seZero(i+1, j-1);
                    break;
                case 7:
                    seZero(i+1, j);
                    break;
                case 8:
                    seZero(i+1, j+1);
                    break;
            }
        }
        etic[i][j].setText("");
    }

    public void controllaVitt() {
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (etic[i][j].getText() == "" && (campo[i][j] != "!" || campo[i][j] != "0")) {
                    return;
                }
            }
        }
        String[] scelte = {"Nuova partita", "Esci", "Guarda il campo"};
        int fine = JOptionPane.showOptionDialog(null, "Hai vinto!", "Vittoria", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, scelte, 0);
    }

    public void coloraNumeri(int i, int j) {
        switch(etic[i][j].getText()) {
            case "1":
                etic[i][j].setForeground(new Color(0,0,255,255));
                break;
            case "2":
                etic[i][j].setForeground(new Color(0,128,0,255));
                break;
            case "3":
                etic[i][j].setForeground(new Color(255,0,0,255));
                break;
            case "4":
                etic[i][j].setForeground(new Color(0,0,128,255));
                break;
            case "5":
                etic[i][j].setForeground(new Color(128,0,0,255));
                break;
            case "6":
                etic[i][j].setForeground(new Color(0,128,128,255));
                break;
            case "7":
                etic[i][j].setForeground(new Color(0,0,0,255));
                break;
            case "8":
                etic[i][j].setForeground(new Color(128,128,128,255));
                break;
        }
    }
}