package com.example.task_4_tic_tac_toe;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Button[][] buttons = new Button[3][3];
    private Button singlePlayerBtn, multiPlayerBtn, exitButton;
    private TextView scoreText;
    private GridLayout gameBoard;
    private LinearLayout modeButtons;
    private boolean isPlayerXTurn = true;
    private int playerXScore = 0;
    private int playerOScore = 0;
    private boolean isSinglePlayerMode = true;
    private boolean gameActive = false;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupButtonListeners();
        hideGameElements();
    }

    private void initializeViews() {
        scoreText = findViewById(R.id.scoreText);
        singlePlayerBtn = findViewById(R.id.singlePlayerBtn);
        multiPlayerBtn = findViewById(R.id.multiPlayerBtn);
        exitButton = findViewById(R.id.exitButton);
        gameBoard = findViewById(R.id.buttonGrid);
        modeButtons = findViewById(R.id.modeButtons);

        // Initialize game board buttons
        buttons[0][0] = findViewById(R.id.btn00);
        buttons[0][1] = findViewById(R.id.btn01);
        buttons[0][2] = findViewById(R.id.btn02);
        buttons[1][0] = findViewById(R.id.btn10);
        buttons[1][1] = findViewById(R.id.btn11);
        buttons[1][2] = findViewById(R.id.btn12);
        buttons[2][0] = findViewById(R.id.btn20);
        buttons[2][1] = findViewById(R.id.btn21);
        buttons[2][2] = findViewById(R.id.btn22);
    }

    private void hideGameElements() {
        gameBoard.setVisibility(View.GONE);
        scoreText.setVisibility(View.GONE);
        exitButton.setVisibility(View.GONE);
        modeButtons.setVisibility(View.VISIBLE);
        findViewById(R.id.gameBoard).setVisibility(View.GONE);
    }

    private void showGameElements() {
        gameBoard.setVisibility(View.VISIBLE);
        scoreText.setVisibility(View.VISIBLE);
        exitButton.setVisibility(View.VISIBLE);
        modeButtons.setVisibility(View.GONE);
        findViewById(R.id.gameBoard).setVisibility(View.VISIBLE);
    }

    private void setupButtonListeners() {
        // Mode selection buttons
        singlePlayerBtn.setOnClickListener(v -> {
            isSinglePlayerMode = true;
            showGameElements();
            resetGame();
            gameActive = true;
            updateScoreText();
        });

        multiPlayerBtn.setOnClickListener(v -> {
            isSinglePlayerMode = false;
            showGameElements();
            resetGame();
            gameActive = true;
            updateScoreText();
        });

        // Game board buttons
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int row = i;
                final int col = j;
                buttons[i][j].setOnClickListener(v -> onGameButtonClick(row, col));
            }
        }

        // Exit button
        exitButton.setOnClickListener(v -> showExitConfirmation());
    }

    private void onGameButtonClick(int row, int col) {
        if (!gameActive || !buttons[row][col].getText().toString().isEmpty()) {
            return;
        }

        String symbol = isPlayerXTurn ? "X" : "O";
        buttons[row][col].setText(symbol);

        if (checkWin()) {
            updateScore(isPlayerXTurn);
            showWinDialog(isPlayerXTurn ? "Player X" : "Player O");
            return;
        }

        if (isBoardFull()) {
            showDrawDialog();
            return;
        }

        isPlayerXTurn = !isPlayerXTurn;

        // Computer's turn in single player mode
        if (isSinglePlayerMode && !isPlayerXTurn) {
            makeComputerMove();
        }
    }

    private void makeComputerMove() {
        // Simple AI: randomly select an empty cell
        while (true) {
            int row = random.nextInt(3);
            int col = random.nextInt(3);
            if (buttons[row][col].getText().toString().isEmpty()) {
                buttons[row][col].setText("O");
                if (checkWin()) {
                    updateScore(false);
                    showWinDialog("Computer");
                } else if (isBoardFull()) {
                    showDrawDialog();
                }
                isPlayerXTurn = true;
                break;
            }
        }
    }

    private boolean checkWin() {
        String[][] field = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        // Check rows
        for (int i = 0; i < 3; i++) {
            if (!field[i][0].isEmpty() && field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2])) {
                return true;
            }
        }

        // Check columns
        for (int i = 0; i < 3; i++) {
            if (!field[0][i].isEmpty() && field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i])) {
                return true;
            }
        }

        // Check diagonals
        if (!field[0][0].isEmpty() && field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2])) {
            return true;
        }
        return !field[0][2].isEmpty() && field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]);
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().toString().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void updateScore(boolean isPlayerX) {
        if (isPlayerX) {
            playerXScore++;
        } else {
            playerOScore++;
        }
        updateScoreText();
    }

    private void updateScoreText() {
        String scoreString = String.format("Score: Player X: %d | Player %s: %d",
                playerXScore, isSinglePlayerMode ? "Computer" : "O", playerOScore);
        scoreText.setText(scoreString);
    }

    private void resetGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        isPlayerXTurn = true;
        updateScoreText();
    }

    private void showWinDialog(String winner) {
        new AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage(winner + " wins!")
                .setPositiveButton("Play Again", (dialog, which) -> resetGame())
                .setCancelable(false)
                .show();
    }

    private void showDrawDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage("It's a draw!")
                .setPositiveButton("Play Again", (dialog, which) -> resetGame())
                .setCancelable(false)
                .show();
    }

    private void showExitConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Exit Game")
                .setMessage("Are you sure you want to exit? Your scores will be reset.")
                .setPositiveButton("Yes", (dialog, which) -> {
                    playerXScore = 0;
                    playerOScore = 0;
                    resetGame();
                    gameActive = false;
                    hideGameElements();
                })
                .setNegativeButton("No", null)
                .show();
    }
}