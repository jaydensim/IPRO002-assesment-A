package VenueManager.TuiMenuOptions;

import VenueManager.VenueManager;
import VenueManager.InteractionClasses.TerminalInterface;

public class ManageEventsMenu {
    private final VenueManager vm;
    private final TerminalInterface tui;

    public ManageEventsMenu(VenueManager vm, TerminalInterface tui) {
        this.vm = vm;
        this.tui = tui;
    }

    public void show() {
        Integer choice = tui.renderMultiChoicePrompt("Manage Events",
                "Concert organiser use case: manage concert timeslots and details.",
                new String[] { "Create Event", "Delete Event", "List Events" });

        if (choice == 1) {
            placeholder("TODO: Implement create event flow (name, timeslot, description).");
        } else if (choice == 2) {
            placeholder("TODO: Implement event deletion flow.");
        } else if (choice == 3) {
            placeholder("TODO: Implement list events with schedule summary.");
        }
    }

    private void placeholder(String message) {
        tui.renderDisplayPrompt("Notice", message + "\nVenue: " + vm.venue.name);
    }
}
