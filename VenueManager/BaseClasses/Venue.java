package VenueManager.BaseClasses;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Venue {

    public String id;
    public String name;
    public String description;

    public SeatingArea[] seatingAreas;
    public static HashMap<String, Seat> seats;

    private Event[] events;

    public Venue(String id, String name, String description, SeatingArea[] seatingAreas) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.seatingAreas = seatingAreas;
        this.events = new Event[0];
    }

    public String toString() {
        return "Venue {id='" + id + "', name='" + name + "', description='" + description + "', seatingAreas='"
                + seatingAreas + "', events='" + events + "'}";
    }

    public static int countSeatsInArea(String areaId) {
        if (seats == null || seats.isEmpty()) {
            return 0;
        }

        int count = 0;
        for (Map.Entry<String, Seat> entry : seats.entrySet()) {
            if (entry.getKey() != null && entry.getKey().startsWith(areaId + "-")) {
                count++;
            }
        }

        return count;
    }
}
