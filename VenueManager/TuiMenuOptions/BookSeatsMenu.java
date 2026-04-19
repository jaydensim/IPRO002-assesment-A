package VenueManager.TuiMenuOptions;

import VenueManager.VenueManager;
import VenueManager.BaseClasses.Booking;
import VenueManager.BaseClasses.Seat;
import VenueManager.InteractionClasses.TerminalInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BookSeatsMenu {
    private VenueManager vm;
    private TerminalInterface tui;

    public BookSeatsMenu(VenueManager vm, TerminalInterface tui) {
        this.vm = vm;
        this.tui = tui;
    }

    public void show() {
        Integer choice = tui.renderMultiChoicePrompt("Book Seats",
                "Concertgoer use case: allocate or review seat bookings by event.",
                new String[] { "Book Seat", "View Existing Booking", "Cancel Booking" });

        if (choice == 1) {
            flowCreateBooking();
        } else if (choice == 2) {
            flowViewBooking();
        } else if (choice == 3) {
            flowCancelBooking();
        }
    }

    private void flowCreateBooking() {
        if (vm.venue.events == null || vm.venue.events.isEmpty()) {
            placeholder("No events exist yet. Create an event first.");
            return;
        }

        if (vm.venue.seats == null || vm.venue.seats.isEmpty()) {
            placeholder("No seats exist in this venue yet.");
            return;
        }

        String eventId = chooseEvent();
        if (eventId == null) {
            return;
        }

        String customerName = tui.renderTextPrompt(
                "Book Seat - Step 1/3",
                "Enter customer name for this booking.",
                "Customer name",
                false).trim();

        String availableSeatList = buildAvailableSeatList();
        if (availableSeatList.isEmpty()) {
            placeholder("No available seats at the moment.");
            return;
        }

        String seatId = tui.renderTextPrompt(
                "Book Seat - Step 2/3",
                "Choose a seat ID from the list below:\n\n" + availableSeatList,
                "Seat ID",
                false).trim();

        Seat selectedSeat = vm.venue.seats.get(seatId);
        if (selectedSeat == null) {
            placeholder("Seat ID not found: " + seatId);
            return;
        }

        if (!selectedSeat.isAvailable()) {
            placeholder("Seat is not available: " + seatId);
            return;
        }

        String confirmation = tui.renderYNPrompt(
                "Book Seat - Step 3/3",
                "Confirm booking?\nCustomer: " + customerName + "\nEvent: " + eventId + "\nSeat: " + seatId);

        if (!confirmation.equals("Y")) {
            placeholder("Booking cancelled by user.");
            return;
        }

        if (vm.venue.bookings == null) {
            vm.venue.bookings = new HashMap<String, Booking>();
        }

        String bookingId = UUID.randomUUID().toString();
        Booking booking = new Booking(bookingId, eventId, new String[] { seatId }, customerName);
        vm.venue.bookings.put(bookingId, booking);
        selectedSeat.assignBooking(bookingId, eventId);

        tui.renderDisplayPrompt("Booking Created",
                "Booking ID: " + bookingId + "\nCustomer: " + customerName + "\nEvent: " + eventId
                        + "\nSeat: " + seatId);
    }

    private void flowViewBooking() {
        if (vm.venue.bookings == null || vm.venue.bookings.isEmpty()) {
            placeholder("No bookings found.");
            return;
        }

        String bookingId = tui.renderTextPrompt(
                "View Booking",
                "Enter booking UUID to view.",
                "Booking ID",
                false).trim();

        Booking booking = vm.venue.bookings.get(bookingId);
        if (booking == null) {
            placeholder("Booking not found: " + bookingId);
            return;
        }

        tui.renderDisplayPrompt("Booking Details", formatBookingDetails(booking));
    }

    private void flowCancelBooking() {
        if (vm.venue.bookings == null || vm.venue.bookings.isEmpty()) {
            placeholder("No bookings found.");
            return;
        }

        String bookingId = tui.renderTextPrompt(
                "Cancel Booking",
                "Enter booking UUID to cancel.",
                "Booking ID",
                false).trim();

        Booking booking = vm.venue.bookings.get(bookingId);
        if (booking == null) {
            placeholder("Booking not found: " + bookingId);
            return;
        }

        String confirmation = tui.renderYNPrompt(
                "Cancel Booking",
                "Cancel this booking?\n" + formatBookingDetails(booking));

        if (!confirmation.equals("Y")) {
            placeholder("Cancellation aborted.");
            return;
        }

        String[] seatIds = booking.getSeatIds();
        if (seatIds != null) {
            for (String seatId : seatIds) {
                Seat seat = vm.venue.seats.get(seatId);
                if (seat != null && bookingId.equals(seat.getBookingId())) {
                    seat.clearBooking();
                }
            }
        }

        vm.venue.bookings.remove(bookingId);
        tui.renderDisplayPrompt("Booking Cancelled", "Removed booking: " + bookingId);
    }

    private String chooseEvent() {
        List<String> eventIds = new ArrayList<>(vm.venue.events.keySet());
        java.util.Collections.sort(eventIds);

        String[] options = new String[eventIds.size()];
        for (int i = 0; i < eventIds.size(); i++) {
            options[i] = eventIds.get(i) + ": " + vm.venue.events.get(eventIds.get(i)).getName();
        }

        Integer choice = tui.renderMultiChoicePrompt("Select Event",
                "Choose the event this seat booking is for.", options);

        if (choice == 0) {
            return null;
        }

        return eventIds.get(choice - 1);
    }

    private String buildAvailableSeatList() {
        List<String> seatIds = new ArrayList<>(vm.venue.seats.keySet());
        java.util.Collections.sort(seatIds);

        StringBuilder output = new StringBuilder();
        for (String seatId : seatIds) {
            Seat seat = vm.venue.seats.get(seatId);
            if (seat != null && seat.isAvailable()) {
                output.append(seatId).append("\n");
            }
        }

        return output.toString().trim();
    }

    private String formatBookingDetails(Booking booking) {
        String seatText = booking.getSeatIds() == null ? "" : String.join(", ", booking.getSeatIds());

        return "Booking ID: " + booking.getId() + "\nCustomer: " + booking.getCustomerName() + "\nEvent: "
                + booking.getEventId() + "\nSeats: " + seatText;
    }

    private void placeholder(String message) {
        tui.renderDisplayPrompt("Notice", message + "\nVenue: " + vm.venue.name);
    }
}
// 45a4b34c-3aa3-437f-a431-cd95e11df09e