package duke;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

/**
 * Handles user interaction in the JavaFX chatbot window.
 */
public class MainWindow {
    private static final String DATA_FILE_PATH = "data/duke.txt";

    private final Duke duke = new Duke(DATA_FILE_PATH);

    @FXML
    private TextArea dialogArea;

    @FXML
    private TextField inputField;

    @FXML
    private Button sendButton;

    /**
     * Initializes the FXML controls after they are loaded.
     */
    @FXML
    public void initialize() {
        dialogArea.setText(duke.getGreeting());
        dialogArea.setFont(Font.font("Monospaced", 13));
        inputField.setMaxWidth(Double.MAX_VALUE);
        sendButton.setDefaultButton(true);
    }

    /**
     * Moves keyboard focus to the command input field.
     */
    public void focusInput() {
        inputField.requestFocus();
    }

    /**
     * Sends the user's command to the chatbot and appends the response.
     */
    @FXML
    private void handleUserInput() {
        String command = inputField.getText().trim();
        if (command.isEmpty()) {
            return;
        }

        String response = duke.getResponse(command);
        dialogArea.appendText("\n\n" + command + "\n" + response);
        inputField.clear();

        if (command.equals("bye")) {
            inputField.setDisable(true);
            sendButton.setDisable(true);
        }
    }
}
