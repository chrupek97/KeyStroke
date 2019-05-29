package keystroke;

public class CharAsNumber {

    public static int asNumber(String text) {
        int number = -1;

        switch (text.toUpperCase()) {
            case "A": number = 0; break;

            case "B": number = 1; break;

            case "C": number = 2; break;

            case "D": number = 3; break;

            case "E": number = 4; break;

            case "F": number = 5; break;

            case "G": number = 6; break;

            case "H": number = 7; break;

            case "I": number = 8; break;

            case "J": number = 9; break;

            case "K": number = 10; break;

            case "L": number = 11; break;

            case "M": number = 12; break;

            case "N": number = 13; break;

            case "O": number = 14; break;

            case "P": number = 15; break;

            case "Q": number = 16; break;

            case "R": number = 17; break;

            case "S": number = 18; break;

            case "T": number = 19; break;

            case "U": number = 20; break;

            case "V": number = 21; break;

            case "W": number = 22; break;

            case "X": number = 23; break;

            case "Y": number = 24; break;

            case "Z": number = 25; break;
            
            case " ": number = 26; break;
            
        }
        return number;
    }
}
