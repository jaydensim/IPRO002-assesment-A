package VenueManager.TuiMenuOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
            placeholder("TODO: Implement create seating area.");
        } else if (choice == 2) {
            placeholder("TODO: Implement remove seating area.");
        } else if (choice == 3) {
            listSeatingAreas();
        }
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

    private void listSeats() {
        StringBuilder output = new StringBuilder();
        output.append("\n");
        output.append("--------------------------------------------------\n");

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
