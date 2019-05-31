package keystroke;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.control.TextField;

public class File {

    public void saveToCsv(long[] alphabet, TextField name) throws IOException {
        FileWriter csvWriter = new FileWriter("dane.csv", true);
        csvWriter.append(name.getText() + ";");

        for (int i = 0; i < alphabet.length; i++) {
            csvWriter.append(alphabet[i] + ";");
        }
        csvWriter.append("\n");
        csvWriter.flush();
        csvWriter.close();
    }

    public void loadFromCsv(ArrayList<User> users) throws FileNotFoundException, IOException {
        String row;
        BufferedReader csvReader = new BufferedReader(new FileReader("dane.csv"));
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(";");
            String name = data[0];
            long[] alphabet = new long[27];
            long[] flightTime = new long[27];

            for (int i = 1; i < data.length; i++) {
                alphabet[i - 1] = Long.parseLong(data[i]);
            }
            users.add(new User(name, alphabet));
        }
        loadFlightTimeFromCsv(users);
        csvReader.close();
    }

    public void loadFlightTimeFromCsv(ArrayList<User> users) throws FileNotFoundException, IOException {
        String row;
        BufferedReader csvReader = new BufferedReader(new FileReader("flightTime.csv"));
        int index = 0;
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(";");
            String name = data[0];
            long[] flightTime = new long[27];

            for (int i = 1; i < data.length; i++) {
                flightTime[i - 1] = Long.parseLong(data[i]);
            }
            users.get(index++).setFlightTimes(flightTime);
        }
        csvReader.close();
    }

    void saveFlightTimeToCsv(long[] alphabet, String name) throws IOException {
        FileWriter csvWriter = new FileWriter("flightTime.csv", true);
        csvWriter.append(name + ";");
        for (int i = 0; i < alphabet.length; i++) {
            csvWriter.append(alphabet[i] + ";");
        }
        csvWriter.append("\n");
        csvWriter.flush();
        csvWriter.close();
    }

}
