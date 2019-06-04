package keystroke;

public class User {

    private String name;
    private long alphabet[] = new long[27];
    private long[] flightTimes = new long[27];

    public User(String name, long alphabet[], long flightTimes[]) {
        this.name = name;
        this.alphabet = alphabet;
        this.flightTimes = flightTimes;
    }

    public User(String name, long alphabet[]) {
        this.name = name;
        this.alphabet = alphabet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long[] getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(long[] alphabet) {
        this.alphabet = alphabet;
    }

    public long[] getFlightTimes() {
        return flightTimes;
    }

    public void setFlightTimes(long[] flightTimes) {
        this.flightTimes = flightTimes;
    }

}
