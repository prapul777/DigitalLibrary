import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class NumberGuessingGame {
    private static final int MIN_RANGE = 1;
    private static final int MAX_RANGE = 100;
    private static final int MAX_ATTEMPTS = 10;
    private static final int MAX_ROUNDS = 3;

    private int currentRound = 1;
    private int attemptsLeft = MAX_ATTEMPTS; vc
    private int totalScore = 0;
    private int randomNumber;
    private Random random = new Random();

    private JFrame frame;
    private JTextField guessField;
    private JTextArea messageArea;
    private JLabel roundLabel, attemptsLabel, scoreLabel;

    public NumberGuessingGame() {
        randomNumber = random.nextInt(MAX_RANGE) + MIN_RANGE;
        createGUI();
    }

    private void createGUI() {
        frame = new JFrame("Number Guessing Game");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new GridLayout(2, 2));
        roundLabel = new JLabel("Round: " + currentRound);
        attemptsLabel = new JLabel("Attempts Left: " + attemptsLeft);
        scoreLabel = new JLabel("Total Score: " + totalScore);

        topPanel.add(roundLabel);
        topPanel.add(attemptsLabel);
        topPanel.add(scoreLabel);

        messageArea = new JTextArea(6, 30);
        messageArea.setEditable(false);
        messageArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        guessField = new JTextField(10);
        JButton guessButton = new JButton("Submit Guess");

        guessButton.addActionListener(e -> processGuess());

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Enter your guess:"));
        inputPanel.add(guessField);
        inputPanel.add(guessButton);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(messageArea), BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void processGuess() {
        try {
            int guess = Integer.parseInt(guessField.getText().trim());

            if (guess < MIN_RANGE || guess > MAX_RANGE) {
                messageArea.setText("Please enter a number between " + MIN_RANGE + " and " + MAX_RANGE);
                return;
            }

            attemptsLeft--;

            if (guess == randomNumber) {
                int roundScore = MAX_ATTEMPTS - attemptsLeft;
                totalScore += roundScore;
                messageArea.setText("🎉 Correct! Number guessed in " + roundScore + " attempts.");
                nextRound();
            } else if (guess < randomNumber) {
                messageArea.setText("🔼 The number is greater than " + guess + ". Attempts Left: " + attemptsLeft);
            } else {
                messageArea.setText("🔽 The number is less than " + guess + ". Attempts Left: " + attemptsLeft);
            }

            attemptsLabel.setText("Attempts Left: " + attemptsLeft);

            if (attemptsLeft == 0) {
                messageArea.setText("😔 Out of attempts! The number was: " + randomNumber);
                nextRound();
            }
        } catch (NumberFormatException ex) {
            messageArea.setText("⚠ Please enter a valid number.");
        }

        guessField.setText("");
    }

    private void nextRound() {
        currentRound++;
        if (currentRound > MAX_ROUNDS) {
            messageArea.setText("🎮 Game Over! Total Score: " + totalScore);
            guessField.setEnabled(false);
        } else {
            attemptsLeft = MAX_ATTEMPTS;
            randomNumber = random.nextInt(MAX_RANGE) + MIN_RANGE;
            roundLabel.setText("Round: " + currentRound);
            attemptsLabel.setText("Attempts Left: " + attemptsLeft);
            scoreLabel.setText("Total Score: " + totalScore);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NumberGuessingGame::new);
    }
}
