package keystroke;

public class Metrics {

    private User user;
    private double sum;

    public Metrics(User user, double sum) {
        this.user = user;
        this.sum = sum;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

}
