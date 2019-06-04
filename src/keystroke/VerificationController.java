package keystroke;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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

public class VerificationController implements Initializable {

    @FXML
    private AnchorPane pane;
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
    private long alphabetWithFlightTimes[] = new long[27];
    private int countOfFlightTimes[] = new int[27];
    private Instant flightTime;
    private int flightIndex;
    private boolean flightTimeStart = true;
    private long flightTimeDuration;
    @FXML
    private TextField name;

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
            Logger.getLogger(VerificationController.class.getName()).log(Level.SEVERE, null, ex);
        }

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
            System.out.println(Arrays.toString(alphabetWithAvgTime));
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
            System.out.println(Arrays.toString(alphabetWithAvgTime));
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
        return sortEuclidesMetrics;
    }

    /* funkcja do identyfikacji użytkownika */
    public void verificationFunction(String choiceMetric) {
        int kParameter = 3;
        long[] average = new long[27];
        for (int i = 0; i < 27; i++) {
            if (countOfFlightTimes[i] != 0) {
                average[i] = alphabetWithFlightTimes[i] / countOfFlightTimes[i];
                System.out.println(Arrays.toString(average));
            }
        }
        User user = new User("", alphabetWithAvgTime, average);  //użytkownik do weryfikacji
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
                    sum += Math.pow((u.getFlightTimes()[i] - user.getFlightTimes()[i]), 2);
                }
                euclides.add(new Metrics(u, Math.sqrt(sum)));
            }
            sortEuclides = sort(euclides);

            for (int i = 0; i < kParameter; i++) {
                userNames.add(sortEuclides.get(i).getUser().getName());
            }
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
                System.out.println("Tak, jesteś " + name.getText());
            } else {
                System.out.println("Nie jesteś " + name.getText());
            }

        } else if (choiceMetric.equals("Manhattan")) {
            for (User u : users) {
                double sum = 0.0;
                for (int i = 0; i < 27; i++) {
                    /* sum += pierwiastek(potega(i-ta litera nowego uzytkownika + i-ta litera przykladowego uzytkownika))*/
                    sum += Math.abs((u.getAlphabet()[i] - user.getAlphabet()[i]));
                    sum += Math.abs((u.getFlightTimes()[i] - user.getFlightTimes()[i]));
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
            for (int i = 0; i < kParameter; i++) {
                howMany = Collections.frequency(userNames, sortEuclides.get(i).getUser().getName());
                if ((howMany >= max) && (sortEuclides.get(i).getSum() <= minSum)) {
                    max = howMany;
                    minSum = sortEuclides.get(i).getSum();
                    nameOfUser = sortEuclides.get(i).getUser().getName();
                }
            }
            if (nameOfUser.equals(name.getText())) {
                System.out.println("Tak, jesteś " + name.getText());
            } else {
                System.out.println("Nie jesteś" + name.getText());
            }
        } else if (choiceMetric.equals("Czebyszewa")) {
            ArrayList<Double> absValues;

            euclides = new ArrayList<>();
            for (User u : users) {
                absValues = new ArrayList<>();
                for (int i = 0; i < 27; i++) {
                    /* sum += pierwiastek(potega(i-ta litera nowego uzytkownika + i-ta litera przykladowego uzytkownika))*/
                    absValues.add((double) (Math.abs((u.getAlphabet()[i] - user.getAlphabet()[i]))));
                    absValues.add((double) (Math.abs((u.getFlightTimes()[i] - user.getFlightTimes()[i]))));
                }
                double maxAbs = Collections.max(absValues);
                euclides.add(new Metrics(u, maxAbs));
            }
            sortEuclides = sort(euclides);

            for (int i = 0; i < kParameter; i++) {
                userNames.add(sortEuclides.get(i).getUser().getName());
            }
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
                System.out.println("Tak, jesteś " + name.getText());
            } else {
                System.out.println("Nie jesteś" + name.getText());
            }
        }

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

    @FXML
    private void verification(ActionEvent event) {
        verificationFunction(metrics.getSelectionModel().getSelectedItem().toString());
    }
}
