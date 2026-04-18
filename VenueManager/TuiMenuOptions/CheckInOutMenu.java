package VenueManager.TuiMenuOptions;

import VenueManager.VenueManager;
import VenueManager.InteractionClasses.TerminalInterface;

public class CheckInOutMenu {
    private final VenueManager vm;
    private final TerminalInterface tui;

    public CheckInOutMenu(VenueManager vm, TerminalInterface tui) {
        this.vm = vm;
        this.tui = tui;
    }

    public void show() {
        Integer choice = tui.renderMultiChoicePrompt("Check In/Out",
                "Venue organiser use case: manage arrivals and departures.",
                new String[] { "Check In Seat", "Check Out Seat", "View Occupancy Snapshot" });

        if (choice == 1) {
            placeholder("TODO: Implement seat check-in workflow.");
        } else if (choice == 2) {
            placeholder("TODO: Implement seat check-out workflow.");
        } else if (choice == 3) {
            placeholder("TODO: Implement occupancy summary for current event.");
        }
    }

    private void placeholder(String message) {
        tui.renderDisplayPrompt("Notice", message + "\nVenue: " + vm.venue.name);
    }
}
