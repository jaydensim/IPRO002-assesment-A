package VenueManager.BaseClasses;

public class Booking {

    private String id;
    private String eventId;
    private String[] seatIds;

    private String customerName;

    Booking(String id, String eventId, String[] seatIds, String customerName) {
        this.id = id;
        this.eventId = eventId;
        this.seatIds = seatIds;
        this.customerName = customerName;
    }

    public String toString() {
        String seatIdsStr = String.join(", ", seatIds);
        return "Booking {id='" + id + "', eventId='" + eventId + "', seatIds='" + seatIdsStr + "', customerName='"
                + customerName + "'}";
    }

}
