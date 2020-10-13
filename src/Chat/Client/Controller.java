package Chat.Client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public HBox hb_sendMess;
    @FXML
    private TextArea ta_mainField;
    @FXML
    private TextField tf_message;
    @FXML
    private MenuItem mi_close;
    @FXML
    private Button b_sent;
    @FXML
    private HBox hb_authPanel;
    @FXML
    private TextField tf_login;
    @FXML
    private PasswordField pf_password;
    @FXML
    private HBox hb_mainPanel;


    Socket socket;
    DataInputStream in;
    DataOutputStream out;
    final String IP_ADDRESS = "localhost";
    final int Port = 8189;

    private boolean authentif;

    private String nick;
    private Stage stage;

    public void setAuthentif (boolean authentif){
        this.authentif = authentif;
        hb_authPanel.setVisible(!authentif);
        hb_authPanel.setManaged(!authentif);
        hb_mainPanel.setVisible(authentif);
        hb_mainPanel.setManaged(authentif);
        hb_sendMess.setVisible(authentif);
        hb_sendMess.setManaged(authentif);

        if (!authentif){
            nick = "";
            setTittle("Чат пупсиков");
        } else {
            setTittle(String.format("Чат пупсиков - [ %s ]", nick));
        }

        ta_mainField.clear();
    }

    public void clickSent(ActionEvent actionEvent) {
        if (tf_message.getText().trim().length() ==0) {
            return;
        }try {
                out.writeUTF(tf_message.getText()  + "\n");
                tf_message.clear();
                tf_message.requestFocus();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void mi_close(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            Stage stage = (Stage) b_sent.getScene().getWindow();
            stage.close();
        });
    }
    public void connect (){
        try {
            socket = new Socket(IP_ADDRESS, Port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(()->{
                try {
                    // authentication step
                    while (true) {
                        String mess = in.readUTF();
                        if (mess.startsWith("/auth_ok")){
                            nick = mess.split("\\s")[1];
                            setAuthentif(true);
                            break;
                        }
//                        ta_mainField.appendText(mess);
                    }
                    // work step
                    while (true) {
                        String mess = in.readUTF();
                        if (mess.startsWith("/exit")){
                            break;
                        }
                        ta_mainField.appendText(mess);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    setAuthentif(false);
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(()-> {
            stage = ((Stage) ta_mainField.getScene().getWindow());

        });
        setAuthentif(false);
    }

    public void tryToEnter(ActionEvent actionEvent) {
        if (socket == null || socket.isClosed()) {
            connect();
        }
            String mesServ = String.format("/auth %s %s", tf_login.getText().trim(), pf_password.getText().trim());
            try {
                out.writeUTF(mesServ);
                pf_password.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
private void setTittle (String tittle){
        Platform.runLater(()-> {
            stage.setTitle(tittle);
        });
}

}
