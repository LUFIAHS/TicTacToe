import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Random;

public class TicTacToe extends JFrame {

    private final JButton[] buttons = new JButton[9];
    private JLabel statusLabel, scoreLabel, timerLabel;
    private int xScore = 0, oScore = 0;
    private boolean xTurn = true, gameOver = false, vsBot = true;
    private int seconds = 0;
    private Timer gameTimer;
    private final Random random = new Random();

    private final Color BACKGROUND = new Color(25, 25, 35);
    private final Color BUTTON_COLOR = new Color(45, 45, 60);
    private final Color X_COLOR = new Color(80, 180, 255);
    private final Color O_COLOR = new Color(255, 100, 100);
    private final Color WIN_COLOR = new Color(70, 180, 100);

    private static final int[][] WINS = {
        {0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}
    };

    public TicTacToe() {
        setTitle("Tic-Tac-Toe");
        setSize(500, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        createUI();
        startTimer();
        setVisible(true);
    }

    private void createUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(BACKGROUND);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        statusLabel = new JLabel("X's Turn");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 28));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        scoreLabel = new JLabel("X: 0    O: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setForeground(Color.LIGHT_GRAY);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        timerLabel = new JLabel("Time: 00:00");
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        timerLabel.setForeground(Color.LIGHT_GRAY);
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(Box.createVerticalStrut(15));
        topPanel.add(statusLabel);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(scoreLabel);
        topPanel.add(Box.createVerticalStrut(5));
        topPanel.add(timerLabel);
        topPanel.add(Box.createVerticalStrut(15));

        JPanel boardPanel = new JPanel(new GridLayout(3, 3, 8, 8));
        boardPanel.setBackground(BACKGROUND);
        boardPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            buttons[i].setFont(new Font("Arial", Font.BOLD, 60));
            buttons[i].setFocusPainted(false);
            buttons[i].setBackground(BUTTON_COLOR);
            buttons[i].setForeground(Color.WHITE);
            final int index = i;
            buttons[i].addActionListener(e -> handleMove(index));
            boardPanel.add(buttons[i]);
        }

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(BACKGROUND);

        JButton newGameButton = new JButton("New Game");
        JButton resetScoreButton = new JButton("Reset Score");
        JButton modeButton = new JButton("Mode: Bot");
        for (JButton b : new JButton[]{newGameButton, resetScoreButton, modeButton})
            b.setFont(new Font("Arial", Font.BOLD, 16));

        newGameButton.addActionListener(e -> newGame());
        resetScoreButton.addActionListener(e -> {
            xScore = 0; oScore = 0;
            updateScore();
            newGame();
        });
        modeButton.addActionListener(e -> {
            vsBot = !vsBot;
            modeButton.setText(vsBot ? "Mode: Bot" : "Mode: PvP");
            newGame();
        });

        bottomPanel.add(newGameButton);
        bottomPanel.add(resetScoreButton);
        bottomPanel.add(modeButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void handleMove(int index) {
        if (gameOver) return;
        if (!buttons[index].getText().isEmpty()) return;
        if (vsBot && !xTurn) return;

        makeMove(index);
        if (checkGameEnd()) return;

        if (vsBot && !xTurn) {
            statusLabel.setText("Bot is thinking...");
            Timer botTimer = new Timer(400, e -> {
                ((Timer) e.getSource()).stop();
                botMove();
            });
            botTimer.setRepeats(false);
            botTimer.start();
        }
    }

    private void makeMove(int index) {
        if (xTurn) {
            buttons[index].setText("X");
            buttons[index].setForeground(X_COLOR);
        } else {
            buttons[index].setText("O");
            buttons[index].setForeground(O_COLOR);
        }
        playSound();
        xTurn = !xTurn;
        updateStatus();
    }

    private void botMove() {
        if (gameOver) return;
        int move = findBestMove();
        if (move != -1) {
            makeMove(move);
            checkGameEnd();
        }
    }

    private int findBestMove() {
        for (String p : new String[]{"O", "X"}) {   // win first, then block
            for (int i = 0; i < 9; i++) {
                if (buttons[i].getText().isEmpty()) {
                    buttons[i].setText(p);
                    boolean w = hasWinner(p);
                    buttons[i].setText("");
                    if (w) return i;
                }
            }
        }
        if (buttons[4].getText().isEmpty()) return 4;
        for (int c : new int[]{0, 2, 6, 8})
            if (buttons[c].getText().isEmpty()) return c;

        int[] available = new int[9];
        int count = 0;
        for (int i = 0; i < 9; i++)
            if (buttons[i].getText().isEmpty()) available[count++] = i;
        return count > 0 ? available[random.nextInt(count)] : -1;
    }

    private boolean checkGameEnd() {
        if (hasWinner("X")) {
            xScore++; updateScore(); gameOver = true;
            statusLabel.setText("🎉 X Wins!");
            highlightWinner("X"); stopTimer();
            return true;
        }
        if (hasWinner("O")) {
            oScore++; updateScore(); gameOver = true;
            statusLabel.setText("🎉 O Wins!");
            highlightWinner("O"); stopTimer();
            return true;
        }
        if (isDraw()) {
            gameOver = true;
            statusLabel.setText("It's a Draw!");
            stopTimer();
            return true;
        }
        return false;
    }

    private boolean isLine(int[] c, String p) {
        return buttons[c[0]].getText().equals(p)
            && buttons[c[1]].getText().equals(p)
            && buttons[c[2]].getText().equals(p);
    }

    private boolean hasWinner(String player) {
        for (int[] c : WINS) if (isLine(c, player)) return true;
        return false;
    }

    private void highlightWinner(String player) {
        for (int[] c : WINS) {
            if (isLine(c, player)) {
                for (int i : c) buttons[i].setBackground(WIN_COLOR);
                break;
            }
        }
    }

    private boolean isDraw() {
        for (JButton b : buttons) if (b.getText().isEmpty()) return false;
        return true;
    }

    private void newGame() {
        for (JButton b : buttons) {
            b.setText("");
            b.setBackground(BUTTON_COLOR);
            b.setForeground(Color.WHITE);
        }
        xTurn = true;
        gameOver = false;
        seconds = 0;
        statusLabel.setText("X's Turn");
        timerLabel.setText("Time: 00:00");
        startTimer();
    }

    private void updateStatus() {
        if (gameOver) return;
        if (xTurn) statusLabel.setText("X's Turn");
        else statusLabel.setText(vsBot ? "Bot's Turn" : "O's Turn");
    }

    private void updateScore() {
        scoreLabel.setText("X: " + xScore + "    O: " + oScore);
    }

    private void startTimer() {
        if (gameTimer != null) gameTimer.stop();
        gameTimer = new Timer(1000, e -> {
            seconds++;
            timerLabel.setText(String.format("Time: %02d:%02d", seconds / 60, seconds % 60));
        });
        gameTimer.start();
    }

    private void stopTimer() {
        if (gameTimer != null) gameTimer.stop();
    }

    private void playSound() {
        File soundFile = new File("move.wav");   // optional
        if (!soundFile.exists()) return;
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (Exception ignored) { }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToe::new);
    }
}
