package VenueManager.TuiMenuOptions;

import VenueManager.VenueManager;
import VenueManager.InteractionClasses.TerminalInterface;

public class BookSeatsMenu {
    private final VenueManager vm;
    private final TerminalInterface tui;

    public BookSeatsMenu(VenueManager vm, TerminalInterface tui) {
        this.vm = vm;
        this.tui = tui;
    }

    public void show() {
        Integer choice = tui.renderMultiChoicePrompt("Book Seats",
                "Concertgoer use case: allocate or review seat bookings.",
                new String[] { "Allocate Seat to Booking", "View Existing Booking", "Cancel Booking" });

        if (choice == 1) {
            placeholder("TODO: Implement seat allocation logic.");
        } else if (choice == 2) {
            placeholder("TODO: Implement booking lookup by event/seat/customer.");
        } else if (choice == 3) {
            placeholder("TODO: Implement booking cancellation flow.");
        }
    }

    private void placeholder(String message) {
        tui.renderDisplayPrompt("Notice", message + "\nVenue: " + vm.venue.name);
    }
}
