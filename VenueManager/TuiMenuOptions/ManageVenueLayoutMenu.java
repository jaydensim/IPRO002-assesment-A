package VenueManager.TuiMenuOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import VenueManager.VenueManager;
import VenueManager.BaseClasses.Seat;
import VenueManager.BaseClasses.SeatingArea;
import VenueManager.BaseClasses.Venue;
import VenueManager.InteractionClasses.TerminalInterface;

public class ManageVenueLayoutMenu {
    private final VenueManager vm;
    private final TerminalInterface tui;

    public ManageVenueLayoutMenu(VenueManager vm, TerminalInterface tui) {
        this.vm = vm;
        this.tui = tui;
    }

    public void show() {
        boolean inMenu = true;
        while (inMenu) {
            Integer choice = tui.renderMultiChoicePrompt("Manage Venue", "Choose a section to manage.",
                    new String[] { "Venue Details", "Seating Areas", "Seats" });

            if (choice == 0) {
                inMenu = false;
            } else if (choice == 1) {
                menuVenueDetails();
            } else if (choice == 2) {
                menuSeatingAreas();
            } else if (choice == 3) {
                listSeats();
            }
        }
    }

    private void menuVenueDetails() {
        tui.renderDisplayPrompt("Venue Details", this.vm.venue.toString());

    }

    private void menuSeatingAreas() {
        Integer choice = tui.renderMultiChoicePrompt("Seating Areas",
                "Maintain seating areas within the venue.",
                new String[] { "Create Seating Area", "Delete Seating Area", "List Seating Areas" });

        if (choice == 1) {
            flowCreateSeatingArea();
        } else if (choice == 2) {
            flowDeleteSeatingArea();
        } else if (choice == 3) {
            listSeatingAreas();
        }
    }

    private void flowCreateSeatingArea() {
        String areaId = tui.renderTextPrompt(
                "Create Seating Area - Step 1/4",
                "Enter seating area ID (example: C).",
                "Area ID",
                false).trim();

        if (areaId.isEmpty()) {
            placeholder("Area ID cannot be empty.");
            return;
        }

        if (findSeatingAreaById(areaId) != null) {
            placeholder("A seating area with that ID already exists: " + areaId);
            return;
        }

        String areaName = tui.renderTextPrompt(
                "Create Seating Area - Step 2/4",
                "Enter seating area display name.",
                "Area name",
                false).trim();

        String seatCountInput = tui.renderTextPrompt(
                "Create Seating Area - Step 3/4",
                "Enter the number of seats to generate for this area.",
                "Seat count",
                false).trim();

        int seatCount;
        try {
            seatCount = Integer.parseInt(seatCountInput);
        } catch (NumberFormatException e) {
            placeholder("Seat count must be a whole number.");
            return;
        }

        if (seatCount <= 0) {
            placeholder("Seat count must be greater than 0.");
            return;
        }

        String confirmation = tui.renderYNPrompt(
                "Create Seating Area - Step 4/4",
                "Create area " + areaId + " (" + areaName + ") with " + seatCount + " seats?");

        if (!confirmation.equals("Y")) {
            placeholder("Creation cancelled.");
            return;
        }

        if (vm.venue.seats == null) {
            vm.venue.seats = new HashMap<String, Seat>();
        }

        String[] areaSeatIds = new String[seatCount];
        for (int i = 0; i < seatCount; i++) {
            String seatKey = areaId + "-" + (i + 1);
            areaSeatIds[i] = seatKey;

            Seat seat = new Seat(UUID.randomUUID().toString(), areaId, "Seat " + areaId + (i + 1), null, true);
            vm.venue.seats.put(seatKey, seat);
        }

        SeatingArea newArea = new SeatingArea(areaId, areaName, areaSeatIds);
        if (vm.venue.seatingAreas == null) {
            vm.venue.seatingAreas = new SeatingArea[] { newArea };
        } else {
            SeatingArea[] next = new SeatingArea[vm.venue.seatingAreas.length + 1];
            for (int i = 0; i < vm.venue.seatingAreas.length; i++) {
                next[i] = vm.venue.seatingAreas[i];
            }
            next[next.length - 1] = newArea;
            vm.venue.seatingAreas = next;
        }

        tui.renderDisplayPrompt("Seating Area Created",
                "Created area " + areaId + " (" + areaName + ") with " + seatCount + " seats.");
    }

    private void flowDeleteSeatingArea() {
        if (vm.venue.seatingAreas == null || vm.venue.seatingAreas.length == 0) {
            placeholder("No seating areas to delete.");
            return;
        }

        String areaId = tui.renderTextPrompt(
                "Delete Seating Area - Step 1/2",
                "Enter the seating area ID to delete.",
                "Area ID",
                false).trim();

        SeatingArea target = findSeatingAreaById(areaId);
        if (target == null) {
            placeholder("Seating area not found: " + areaId);
            return;
        }

        if (hasBookedSeatsInArea(areaId)) {
            placeholder("Cannot delete area " + areaId + " because it has booked seats.");
            return;
        }

        String confirmation = tui.renderYNPrompt(
                "Delete Seating Area - Step 2/2",
                "Delete area " + target.id + " (" + target.name + ") and all of its seats?");

        if (!confirmation.equals("Y")) {
            placeholder("Deletion cancelled.");
            return;
        }

        removeSeatsForArea(areaId);
        removeSeatingArea(areaId);

        tui.renderDisplayPrompt("Seating Area Deleted",
                "Deleted area " + areaId + " and removed its seats.");
    }

    private void listSeatingAreas() {
        StringBuilder output = new StringBuilder();
        output.append("\n");

        SeatingArea[] areas = vm.venue.seatingAreas;
        if (areas == null || areas.length == 0) {
            output.append("No seating areas found.\n");
            tui.renderDisplayPrompt("Seating Areas", output.toString());
            return;
        }

        for (int i = 0; i < areas.length; i++) {
            SeatingArea area = areas[i];
            if (area == null) {
                continue;
            }

            int seatCount = Venue.countSeatsInArea(area.id);
            output.append((i + 1) + ". " + area.id + " - " + area.name + " (" + seatCount + " seats)\n");
        }

        tui.renderDisplayPrompt("Seating Areas", output.toString());
    }

    private SeatingArea findSeatingAreaById(String areaId) {
        if (vm.venue.seatingAreas == null) {
            return null;
        }

        for (SeatingArea area : vm.venue.seatingAreas) {
            if (area != null && area.id.equals(areaId)) {
                return area;
            }
        }

        return null;
    }

    private boolean hasBookedSeatsInArea(String areaId) {
        if (vm.venue.seats == null || vm.venue.seats.isEmpty()) {
            return false;
        }

        for (String seatKey : vm.venue.seats.keySet()) {
            if (seatKey.startsWith(areaId + "-")) {
                Seat seat = vm.venue.seats.get(seatKey);
                if (seat != null && seat.getBookingId() != null && !seat.getBookingId().isEmpty()) {
                    return true;
                }
            }
        }

        return false;
    }

    private void removeSeatsForArea(String areaId) {
        if (vm.venue.seats == null || vm.venue.seats.isEmpty()) {
            return;
        }

        List<String> toRemove = new ArrayList<>();
        for (String seatKey : vm.venue.seats.keySet()) {
            if (seatKey.startsWith(areaId + "-")) {
                toRemove.add(seatKey);
            }
        }

        for (String seatKey : toRemove) {
            vm.venue.seats.remove(seatKey);
        }
    }

    private void removeSeatingArea(String areaId) {
        if (vm.venue.seatingAreas == null || vm.venue.seatingAreas.length == 0) {
            return;
        }

        List<SeatingArea> keptAreas = new ArrayList<>();
        for (SeatingArea area : vm.venue.seatingAreas) {
            if (area == null) {
                continue;
            }
            if (!area.id.equals(areaId)) {
                keptAreas.add(area);
            }
        }

        vm.venue.seatingAreas = keptAreas.toArray(new SeatingArea[0]);
    }

    private void listSeats() {
        StringBuilder output = new StringBuilder();
        output.append("\n");

        if (vm.venue.seats == null || vm.venue.seats.isEmpty()) {
            output.append("No seats found.\n");
            tui.renderDisplayPrompt("Seats", output.toString());
            return;
        }

        List<String> seatKeys = new ArrayList<>(vm.venue.seats.keySet());
        Collections.sort(seatKeys);

        for (String seatKey : seatKeys) {
            Seat seat = vm.venue.seats.get(seatKey);
            if (seat == null) {
                continue;
            }
            output.append(seatKey + ": " + seat.toString() + "\n");
        }

        tui.renderDisplayPrompt("Seats", output.toString());
    }

    private void placeholder(String message) {
        tui.renderDisplayPrompt("Notice", message + "\nVenue: " + vm.venue.name);
    }
}
