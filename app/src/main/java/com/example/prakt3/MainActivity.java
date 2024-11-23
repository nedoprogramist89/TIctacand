package com.example.prakt3;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private GridLayout gridLayout;
    private Button[][] buttons = new Button[3][3];
    private boolean playerXTurn = true;
    private int moveCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getSharedPreferences("TicTacToePreferences", MODE_PRIVATE);
        boolean isDarkTheme = sharedPreferences.getBoolean("DarkTheme", false);
        setTheme(isDarkTheme ? R.style.DarkTheme : R.style.LightTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridLayout = findViewById(R.id.gridLayout);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onButtonClick((Button) v);
                    }
                });
            }
        }

        Button themeButton = findViewById(R.id.themeButton);
        themeButton.setOnClickListener(v -> changeTheme());
    }

    private void onButtonClick(Button button) {
        if (button.getText().toString().equals("")) {
            button.setText(playerXTurn ? "X" : "O");
            moveCount++;

            if (checkForWin()) {
                updateStatistics(playerXTurn ? "X" : "O");
                Toast.makeText(this, (playerXTurn ? "Player X" : "Player O") + " wins!", Toast.LENGTH_SHORT).show();
                resetBoard();
            } else if (moveCount == 9) {
                updateStatistics("Draw");
                Toast.makeText(this, "It's a draw!", Toast.LENGTH_SHORT).show();
                resetBoard();
            } else {
                playerXTurn = !playerXTurn;
            }

        }
    }

    private boolean checkForWin() {
        for (int i = 0; i < 3; i++) {

            if (buttons[i][0].getText().toString().equals(buttons[i][1].getText().toString()) &&
                    buttons[i][1].getText().toString().equals(buttons[i][2].getText().toString()) &&
                    !buttons[i][0].getText().toString().equals("")) {
                return true;
            }
            if (buttons[0][i].getText().toString().equals(buttons[1][i].getText().toString()) &&
                    buttons[1][i].getText().toString().equals(buttons[2][i].getText().toString()) &&
                    !buttons[0][i].getText().toString().equals("")) {
                return true;
            }
        }

        if (buttons[0][0].getText().toString().equals(buttons[1][1].getText().toString()) &&
                buttons[1][1].getText().toString().equals(buttons[2][2].getText().toString()) &&
                !buttons[0][0].getText().toString().equals("")) {
            return true;
        }
        if (buttons[0][2].getText().toString().equals(buttons[1][1].getText().toString()) &&
                buttons[1][1].getText().toString().equals(buttons[2][0].getText().toString()) &&
                !buttons[0][2].getText().toString().equals("")) {
            return true;
        }
        return false;
    }

    private void resetBoard() {
        moveCount = 0;
        playerXTurn = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
    }

    private void changeTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences("TicTacToePreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean isDarkTheme = sharedPreferences.getBoolean("DarkTheme", false);
        editor.putBoolean("DarkTheme", !isDarkTheme);
        editor.apply();
        setTheme(!isDarkTheme ? R.style.DarkTheme : R.style.LightTheme);
        recreate();
    }
    private void updateStatistics(String result) {
        SharedPreferences sharedPreferences = getSharedPreferences("GameStatistics", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        int winsX = sharedPreferences.getInt("winsX", 0);
        int winsO = sharedPreferences.getInt("winsO", 0);
        int draws = sharedPreferences.getInt("draws", 0);


        if (result.equals("X")) {
            winsX++;
        } else if (result.equals("O")) {
            winsO++;
        } else if (result.equals("Draw")) {
            draws++;
        }


        editor.putInt("winsX", winsX);
        editor.putInt("winsO", winsO);
        editor.putInt("draws", draws);
        editor.apply();


        TextView statsTextView = findViewById(R.id.statsTextView);
        statsTextView.setText("Победы X: " + winsX + " Победы O: " + winsO + " Ничья: " + draws);
    }

    @Override
    protected void onResume() {
        super.onResume();


        SharedPreferences sharedPreferences = getSharedPreferences("GameStatistics", MODE_PRIVATE);
        int winsX = sharedPreferences.getInt("winsX", 0);
        int winsO = sharedPreferences.getInt("winsO", 0);
        int draws = sharedPreferences.getInt("draws", 0);


        TextView statsTextView = findViewById(R.id.statsTextView);
        statsTextView.setText("Победы X: " + winsX + " Победы O: " + winsO + " Ничья: " + draws);
    }




}
