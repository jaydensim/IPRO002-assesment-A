package VenueManager.TuiMenuOptions;

import java.util.UUID;

import VenueManager.VenueManager;
import VenueManager.BaseClasses.Booking;
import VenueManager.BaseClasses.Event;
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
                "Concert organiser use case: manage event UUIDs.",
                new String[] { "Create Event", "Delete Event", "List Events" });

        if (choice == 1) {
            flowCreateEvent();
        } else if (choice == 2) {
            flowDeleteEvent();
        } else if (choice == 3) {
            flowListEvents();
        }
    }

    private void flowCreateEvent() {
        String eventName = tui.renderTextPrompt(
                "Event Name",
                "Enter the name of the new event.",
                "Event Name",
                false).trim();

        if (eventName.isEmpty()) {
            placeholder("Creation cancelled.");
            return;
        }

        if (vm.venue.events == null) {
            vm.venue.events = new java.util.HashMap<String, Event>();
        }

        String eventId = UUID.randomUUID().toString();
        Event event = new Event(eventId, eventName);
        vm.venue.events.put(eventId, event);

        tui.renderDisplayPrompt("Event Created",
                "Created event '" + eventName + "' with UUID: " + eventId);
    }

    private void flowDeleteEvent() {
        if (vm.venue.events == null || vm.venue.events.isEmpty()) {
            placeholder("No events to delete.");
            return;
        }

        String eventId = tui.renderTextPrompt(
                "Delete Event",
                "Enter the event UUID to delete.",
                "Event UUID",
                false).trim();

        Event event = vm.venue.events.get(eventId);
        if (event == null) {
            placeholder("Event not found: " + eventId);
            return;
        }

        if (hasBookingsForEvent(eventId)) {
            placeholder("Cannot delete event because bookings already exist for it.");
            return;
        }

        String confirmation = tui.renderYNPrompt(
                "Delete Event",
                "Delete event?\n" + event.toString());

        if (!confirmation.equals("Y")) {
            placeholder("Deletion cancelled.");
            return;
        }

        vm.venue.events.remove(eventId);
        tui.renderDisplayPrompt("Event Deleted", "Removed event: " + eventId);
    }

    private void flowListEvents() {
        StringBuilder output = new StringBuilder();
        output.append("\n");

        if (vm.venue.events == null || vm.venue.events.isEmpty()) {
            output.append("No events found.\n");
            tui.renderDisplayPrompt("Events", output.toString());
            return;
        }

        java.util.List<String> eventIds = new java.util.ArrayList<>(vm.venue.events.keySet());
        java.util.Collections.sort(eventIds);

        for (String eventId : eventIds) {
            if (vm.venue.events.get(eventId) == null) {
                continue;
            }
            output.append("- ").append(eventId + ": " + vm.venue.events.get(eventId).getName()).append("\n");
        }

        tui.renderDisplayPrompt("Events", output.toString());
    }

    private boolean hasBookingsForEvent(String eventId) {
        if (vm.venue.bookings == null || vm.venue.bookings.isEmpty()) {
            return false;
        }

        for (Booking booking : vm.venue.bookings.values()) {
            if (booking != null && eventId.equals(booking.getEventId())) {
                return true;
            }
        }

        return false;
    }

    private void placeholder(String message) {
        tui.renderDisplayPrompt("Notice", message + "\nVenue: " + vm.venue.name);
    }
}
