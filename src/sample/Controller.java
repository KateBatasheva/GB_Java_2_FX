package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Controller {
    @FXML
    public TextArea ta_mainField;
    @FXML
    public TextField tf_message;
    @FXML
    public MenuItem mi_close;
    @FXML
    public Button b_sent;

    public void clickSent(ActionEvent actionEvent) {
        ta_mainField.appendText(tf_message.getText() + "\n");
        tf_message.requestFocus();
        tf_message.clear();
    }

    public void mi_close(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            Stage stage = (Stage) b_sent.getScene().getWindow();
            stage.close();
        });
    }


}
