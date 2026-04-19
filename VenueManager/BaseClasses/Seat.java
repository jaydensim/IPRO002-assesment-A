package VenueManager.BaseClasses;

public class Seat {
    public String id;

    private String posArea;

    private String displayName;

    private String eventId;
    private String bookingId;
    private Boolean isBookable;

    public String toString() {
        return "Seat {id='" + id + "', posArea='" + posArea + "', displayName='" + displayName + "', eventId='"
                + eventId + "', bookingId='" + bookingId + "', isBookable='" + isBookable
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

    public String getEventId() {
        return eventId;
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

    public void assignBooking(String bookingId, String eventId) {
        this.bookingId = bookingId;
        this.eventId = eventId;
    }

    public void clearBooking() {
        this.bookingId = null;
        this.eventId = null;
    }

    public void setBookable(boolean isBookable) {
        this.isBookable = isBookable;
    }

}
