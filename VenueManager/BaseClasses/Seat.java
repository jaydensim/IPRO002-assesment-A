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

    public String getPosArea() {
        return posArea;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBookingId() {
        return bookingId;
    }

    public boolean isBookable() {
        return Boolean.TRUE.equals(isBookable);
    }

    public boolean isAvailable() {
        return isBookable() && (bookingId == null || bookingId.isEmpty());
    }

    public void assignBooking(String bookingId) {
        this.bookingId = bookingId;
    }

    public void clearBooking() {
        this.bookingId = null;
    }

}
