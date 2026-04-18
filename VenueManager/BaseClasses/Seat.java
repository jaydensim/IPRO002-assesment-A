package VenueManager.BaseClasses;

public class Seat {
    public String id;

    private String posArea;

    private String displayName;

    private String bookingId;
    private Boolean isBookable;

    public String toString() {
        return "Seat {id='" + id + "', posArea='" + posArea + "', displayName='" + displayName + "', bookingId='"
                + bookingId + "', isBookable='" + isBookable
                + "'}";
    }

    public Seat(String id, String posArea, String displayName, String bookingId, Boolean isBookable) {
        this.id = id;
        this.posArea = posArea;
        this.displayName = displayName;
        this.bookingId = bookingId;
        this.isBookable = isBookable;

    }

}
