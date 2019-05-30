/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keystroke;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
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
    @FXML
    private TextField name;
    @FXML
    private ComboBox<String> metrics;
    @FXML
    private TextField inputText;
    @FXML
    private TextField inputText1;
    @FXML
    private TextField inputText2;

    private Instant time[] = new Instant[2];
    private long alphabetWithAvgTime[] = new long[27];
    private File file = new File();
    private ArrayList<User> users = new ArrayList<>();

    public long dwellTime(Instant start, Instant stop) {
        long timeElapsed = Duration.between(start, stop).toMillis();
        return timeElapsed;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        metrics.getItems().add("Euklidesowa");
        metrics.getItems().add("Manhattan");
        metrics.getItems().add("Czebyszewa");

        try {
            file.loadFromCsv(users);
        } catch (IOException ex) {
            Logger.getLogger(IdentificationController.class.getName()).log(Level.SEVERE, null, ex);
        }

        inputText.setOnKeyPressed((event) -> {
            time[0] = Instant.now();
        });

        inputText.setOnKeyReleased((event) -> {
            time[1] = Instant.now();
            int index = CharAsNumber.asNumber(event.getText());
            alphabetWithAvgTime[index] = (alphabetWithAvgTime[index] + dwellTime(time[0], time[1])) / 2;
            System.out.println(Arrays.toString(alphabetWithAvgTime));
        });

        inputText1.setOnKeyPressed((event) -> {
            time[0] = Instant.now();
        });

        inputText1.setOnKeyReleased((event) -> {
            time[1] = Instant.now();
            int index = CharAsNumber.asNumber(event.getText());
            alphabetWithAvgTime[index] = (alphabetWithAvgTime[index] + dwellTime(time[0], time[1])) / 2;
            System.out.println(Arrays.toString(alphabetWithAvgTime));
        });
        inputText2.setOnKeyPressed((event) -> {
            time[0] = Instant.now();
        });

        inputText2.setOnKeyReleased((event) -> {
            time[1] = Instant.now();
            int index = CharAsNumber.asNumber(event.getText());
            alphabetWithAvgTime[index] = (alphabetWithAvgTime[index] + dwellTime(time[0], time[1])) / 2;
            System.out.println(Arrays.toString(alphabetWithAvgTime));
        });
    }

    /* sortowanie listy euklidesowej*/
    public ArrayList<Metrics> sort(ArrayList<Metrics> em) {
        ArrayList<Double> sumToSort = new ArrayList<>();
        ArrayList<Metrics> sortEuclidesMetrics = new ArrayList<>();

        for (Metrics e : em) {
            sumToSort.add(e.getSum());
        }
        Collections.sort(sumToSort);
        System.out.println(sumToSort);
        User user = null;
        for (Double d : sumToSort) {
            for (Metrics metric : em) {
                if (metric.getSum() == d) {
                    user = metric.getUser();
                    break;
                }
            }
            sortEuclidesMetrics.add(new Metrics(user, d));
        }
        System.out.println("P");
        System.out.println(sortEuclidesMetrics.get(0).getUser().getName());
        System.out.println(sortEuclidesMetrics.get(1).getUser().getName());
        System.out.println(sortEuclidesMetrics.get(2).getUser().getName());
        return sortEuclidesMetrics;
    }

    /* funkcja do identyfikacji użytkownika */
    public void identifyFunction(String choiceMetric) {
        int kParameter = 3;
        User user = new User(name.getText(), alphabetWithAvgTime);  //użytkownik do weryfikacji
        ArrayList<Metrics> euclides = new ArrayList<>();    //lista zawierajaca uzytkownika i czas liczony wedlug wzoru Euclidesa
        ArrayList<Metrics> sortEuclides;        //lista posortowana
        ArrayList<String> userNames = new ArrayList<>();    //nazwy użytkowników
        choiceMetric = metrics.getSelectionModel().getSelectedItem().toString(); //wybor metryki

        if (choiceMetric.equals("Euklidesowa")) {
            for (User u : users) {
                double sum = 0.0;
                for (int i = 0; i < 27; i++) {
                    /* sum += pierwiastek(potega(i-ta litera nowego uzytkownika + i-ta litera przykladowego uzytkownika))*/
                    sum += Math.pow((u.getAlphabet()[i] - user.getAlphabet()[i]), 2);
                }
                euclides.add(new Metrics(u, Math.sqrt(sum)));
            }
            sortEuclides = sort(euclides);

            for (int i = 0; i < kParameter; i++) {
                userNames.add(sortEuclides.get(i).getUser().getName());
            }
            System.out.println(userNames.get(0));
            System.out.println(userNames.get(1));
            System.out.println(userNames.get(2));
            int max = 0;        //maksymalna liczba wystapien opbiektu
            double minSum = sortEuclides.get(0).getSum();      //najmniejsza droga
            int howMany = 0;    //liczba wystapien danego obiektu
            String nameOfUser = null;
            for (int i = 0; i < kParameter; i++) {
                howMany = Collections.frequency(userNames, sortEuclides.get(i).getUser().getName());
                if ((howMany >= max) && (sortEuclides.get(i).getSum() <= minSum)) {
                    max = howMany;
                    minSum = sortEuclides.get(i).getSum();
                    nameOfUser = sortEuclides.get(i).getUser().getName();
                }
            }
            if (nameOfUser.equals(name.getText())) {
                System.out.println("Dokładnie tak, jesteś " + name.getText());
            } else {
                System.out.println("Nie jesteś " + name.getText());
            }
        } else if (choiceMetric.equals("Manhattan")) {
            for (User u : users) {
                double sum = 0.0;
                for (int i = 0; i < 27; i++) {
                    /* sum += pierwiastek(potega(i-ta litera nowego uzytkownika + i-ta litera przykladowego uzytkownika))*/
                    sum += Math.abs(u.getAlphabet()[i] - user.getAlphabet()[i]);
                }
                euclides.add(new Metrics(u, sum));
            }
            sortEuclides = sort(euclides);

            for (int i = 0; i < kParameter; i++) {
                userNames.add(sortEuclides.get(i).getUser().getName());
            }
            int max = 0;        //maksymalna liczba wystapien opbiektu
            double minSum = sortEuclides.get(0).getSum();      //najmniejsza droga
            int howMany = 0;    //liczba wystapien danego obiektu
            String nameOfUser = null;
            System.out.println(sortEuclides.get(0).getUser().getName());
            System.out.println(sortEuclides.get(1).getUser().getName());
            System.out.println(sortEuclides.get(2).getUser().getName());
            
            for (int i = 0; i < kParameter; i++) {
                howMany = Collections.frequency(userNames, sortEuclides.get(i).getUser().getName());
                if ((howMany >= max) && (sortEuclides.get(i).getSum() <= minSum)) {
                    max = howMany;
                    minSum = sortEuclides.get(i).getSum();
                    nameOfUser = sortEuclides.get(i).getUser().getName();
                }
            }

            if (nameOfUser.equals(name.getText())) {
                System.out.println("Dokładnie tak, jesteś " + name.getText());
            } else {
                System.out.println("Nie jesteś " + name.getText() + " Jesteś " + nameOfUser);
            }
        } else if (choiceMetric.equals("Czebyszewa")) {
        }

    }

    @FXML
    private void identify(ActionEvent event) {
        identifyFunction(metrics.getSelectionModel().getSelectedItem().toString());
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

}
