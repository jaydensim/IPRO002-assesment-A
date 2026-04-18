package VenueManager.TuiMenuOptions;

import VenueManager.VenueManager;
import VenueManager.InteractionClasses.TerminalInterface;

public class AnalyticsCapacityMenu {
    private final VenueManager vm;
    private final TerminalInterface tui;

    public AnalyticsCapacityMenu(VenueManager vm, TerminalInterface tui) {
        this.vm = vm;
        this.tui = tui;
    }

    public void show() {
        Integer choice = tui.renderMultiChoicePrompt("Analytics and Capacity",
                "Venue management use case: inspect capacity and usage data.",
                new String[] { "Capacity by Seating Area", "Booking Summary", "Attendance Summary" });

        if (choice == 1) {
            placeholder("TODO: Implement seating-area capacity report.");
        } else if (choice == 2) {
            placeholder("TODO: Implement booking totals report.");
        } else if (choice == 3) {
            placeholder("TODO: Implement attendance/check-in report.");
        }
    }

    private void placeholder(String message) {
        tui.renderDisplayPrompt("Notice", message + "\nVenue: " + vm.venue.name);
    }
}
