package VenueManager.Utis;

public class Logging {
    private String compName;

    public Logging(String compName) {
        this.compName = compName;
    }

    public void log(String message) {
        System.out.println("[" + compName + "] " + message);
    }
}
