package keystroke;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import static java.util.Arrays.sort;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class QualityController implements Initializable {

    @FXML
    private AnchorPane pane;
    @FXML
    private ComboBox<String> metrics;
    private File file = new File();
    private ArrayList<User> users = new ArrayList<>();
    private int counter = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        metrics.getItems().add("Euklidesowa");
        metrics.getItems().add("Manhattan");
        metrics.getItems().add("Czebyszewa");
        try {
            file.loadFromCsv(users);
        } catch (IOException ex) {
            Logger.getLogger(QualityController.class.getName()).log(Level.SEVERE, null, ex);
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

    /* sortowanie listy euklidesowej*/
    private ArrayList<Metrics> sort(ArrayList<Metrics> em) {
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
        System.out.println(sortEuclidesMetrics.size());
        return sortEuclidesMetrics;
    }

    private double checkQualityFunction(String metric) {
        int kParameter = 3;
        ArrayList<Metrics> euclides = null;    //lista zawierajaca uzytkownika i czas liczony wedlug wzoru Euclidesa
        ArrayList<Metrics> sortEuclides;        //lista posortowana
        ArrayList<String> userNames = new ArrayList<>();    //nazwy użytkowników
        metric = metrics.getSelectionModel().getSelectedItem().toString(); //wybor metryki

        if (metric.equals("Euklidesowa")) {
            for (User us : users) {
                euclides = new ArrayList<>();
                for (User u : users) {
                    double sum = 0.0;
                    for (int i = 0; i < 27; i++) {
                        /* sum += pierwiastek(potega(i-ta litera nowego uzytkownika + i-ta litera przykladowego uzytkownika))*/
                        sum += Math.pow((u.getAlphabet()[i] - us.getAlphabet()[i]), 2);
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
                if (nameOfUser.equals(us.getName())) {
                    counter++;
                }
            }
        } else if (metric.equals("Manhattan")) {
            for (User us : users) {
                euclides = new ArrayList<>();
                for (User u : users) {
                    double sum = 0.0;
                    for (int i = 0; i < 27; i++) {
                        /* sum += pierwiastek(potega(i-ta litera nowego uzytkownika + i-ta litera przykladowego uzytkownika))*/
                        sum += Math.abs((u.getAlphabet()[i] - us.getAlphabet()[i]));
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
                if (nameOfUser.equals(us.getName())) {
                    counter++;
                }
            }
        }
        return ((double) counter / (double) users.size()) * 100.00;
    }

    @FXML
    private void checkQuality(ActionEvent event
    ) {
        System.out.println(checkQualityFunction(metrics.getSelectionModel().getSelectedItem().toString()));
    }

}
