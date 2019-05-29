/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keystroke;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Mateusz
 */
public class IdentificationController implements Initializable {

    @FXML
    private AnchorPane pane;
    private File file = new File();
    private ArrayList<User> users = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void addToDatabase(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("fxml/AddToDataBase.fxml"));
        Scene scene = new Scene(parent);
        Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        newStage.setScene(scene);
        newStage.setResizable(false);
        newStage.show();

    }

    @FXML
    private void identification(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("fxml/Identification.fxml"));
        Scene scene = new Scene(parent);
        Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        newStage.setScene(scene);
        newStage.setResizable(false);
        newStage.show();
    }

    @FXML
    private void verification(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("fxml/Verification.fxml"));
        Scene scene = new Scene(parent);
        Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        newStage.setScene(scene);
        newStage.setResizable(false);
        newStage.show();
    }
}
