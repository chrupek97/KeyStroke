/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keystroke;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AddToDatabaseController implements Initializable {

    @FXML
    private AnchorPane pane;
    @FXML
    private TextField name;
    @FXML
    private TextField inputText;
    @FXML
    private TextField inputText1;
    @FXML
    private TextField inputText2;

    private Instant time[] = new Instant[2];
    private long alphabetWithAvgTime[] = new long[27];
    private long alphabetWithFlightTimes[] = new long[27];
    private int countOfFlightTimes[] = new int[27];
    private Instant flightTime;
    private int flightIndex;
    private boolean flightTimeStart = true;
    private long flightTimeDuration;
    private File file = new File();
    private ArrayList<User> users = new ArrayList<>();

    public long dwellTime(Instant start, Instant stop) {
        long timeElapsed = Duration.between(start, stop).toMillis();
        return timeElapsed;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inputText.setOnKeyPressed((event) -> {
            time[0] = Instant.now();
            if (flightTimeStart) {
                flightTime = time[0];
                flightTimeStart = false;
            } else {
                flightTimeFunction();
            }
        });

        inputText.setOnKeyReleased((event) -> {
            time[1] = Instant.now();
            int index = CharAsNumber.asNumber(event.getText());
            flightIndex = index;
            if (index != -1) {
                alphabetWithAvgTime[index] = (alphabetWithAvgTime[index] + dwellTime(time[0], time[1])) / 2;
            }
//            System.out.println(Arrays.toString(alphabetWithAvgTime));
        });

        inputText1.setOnKeyPressed((event) -> {
            time[0] = Instant.now();
            flightTimeFunction();
        });

        inputText1.setOnKeyReleased((event) -> {
            time[1] = Instant.now();
            int index = CharAsNumber.asNumber(event.getText());
            flightIndex = index;
            if (index != -1) {
                alphabetWithAvgTime[index] = (alphabetWithAvgTime[index] + dwellTime(time[0], time[1])) / 2;
            }
//            System.out.println(Arrays.toString(alphabetWithAvgTime));
        });
        inputText2.setOnKeyPressed((event) -> {
            time[0] = Instant.now();
            flightTimeFunction();
        });

        inputText2.setOnKeyReleased((event) -> {
            time[1] = Instant.now();
            int index = CharAsNumber.asNumber(event.getText());
            flightIndex = index;
            if (index != -1) {
                alphabetWithAvgTime[index] = (alphabetWithAvgTime[index] + dwellTime(time[0], time[1])) / 2;
            }
//            System.out.println(Arrays.toString(alphabetWithAvgTime));
        });

    }

    @FXML
    private void saveDataToCSV(ActionEvent event) throws IOException {
        file.saveToCsv(alphabetWithAvgTime, name);
        long[] average = new long[27];
        for (int i = 0; i < 27; i++) {
            if (countOfFlightTimes[i] != 0) {
                average[i] = alphabetWithFlightTimes[i] / countOfFlightTimes[i];
                System.out.println(Arrays.toString(average));
            }
        }
        file.saveFlightTimeToCsv(average, name.getText());
    }

    @FXML
    private void backToMenu(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/FXMLDocument.fxml"));
        Scene scene = new Scene(parent);
        Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        newStage.setScene(scene);
        newStage.setResizable(false);
        newStage.show();
    }

    private void flightTimeFunction() {
        flightTimeDuration = dwellTime(flightTime, time[0]);
        if ((flightTimeDuration < 1500) && (flightIndex > -1)) {
            countOfFlightTimes[flightIndex]++;
            alphabetWithFlightTimes[flightIndex] += flightTimeDuration;
            System.out.println(Arrays.toString(alphabetWithFlightTimes));
            System.out.println(flightTimeDuration);
            System.out.println(flightIndex);
            System.out.println("-----------------");
        }
        flightTime = time[0];
    }

}
