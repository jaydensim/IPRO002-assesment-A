package VenueManager.TuiMenuOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import VenueManager.VenueManager;
import VenueManager.BaseClasses.Seat;
import VenueManager.BaseClasses.Venue;
import VenueManager.InteractionClasses.TerminalInterface;

public class CheckInOutMenu {
    private final VenueManager vm;
    private final TerminalInterface tui;

    public CheckInOutMenu(VenueManager vm, TerminalInterface tui) {
        this.vm = vm;
        this.tui = tui;
    }

    public void show() {
        boolean inMenu = true;
        while (inMenu) {
            Integer choice = tui.renderMultiChoicePrompt("Check In/Out",
                    "Venue organiser use case: manage arrivals and departures.",
                    new String[] { "Check In Seat", "Check Out Seat", "View Occupancy Snapshot" });

            if (choice == 0) {
                inMenu = false;
            } else if (choice == 1) {
                flowCheckInSeat();
            } else if (choice == 2) {
                flowCheckOutSeat();
            } else if (choice == 3) {
                flowViewOccupancySnapshot();
            }
        }
    }

    private void flowCheckInSeat() {
        if (Venue.seats == null || Venue.seats.isEmpty()) {
            placeholder("No seats exist in this venue yet.");
            return;
        }

        String seatId = tui.renderTextPrompt(
                "Check In Seat",
                "Enter seat ID to check in. Checked-in seats are not bookable.",
                "Seat ID",
                false).trim();

        Seat seat = Venue.seats.get(seatId);
        if (seat == null) {
            placeholder("Seat ID not found: " + seatId);
            return;
        }

        String bookingId = seat.getBookingId();
        if (bookingId == null || bookingId.trim().isEmpty()) {
            placeholder("Cannot check in unbooked seat: " + seatId);
            return;
        }

        if (!seat.isBookable()) {
            placeholder("Seat is already checked in: " + seatId);
            return;
        }

        seat.setBookable(false);
        tui.renderDisplayPrompt("Seat Checked In",
                "Seat " + seatId + " has been checked in and is now unavailable for booking.");
    }

    private void flowCheckOutSeat() {
        if (Venue.seats == null || Venue.seats.isEmpty()) {
            placeholder("No seats exist in this venue yet.");
            return;
        }

        String seatId = tui.renderTextPrompt(
                "Check Out Seat",
                "Enter seat ID to check out. Checked-out seats become bookable again.",
                "Seat ID",
                false).trim();

        Seat seat = Venue.seats.get(seatId);
        if (seat == null) {
            placeholder("Seat ID not found: " + seatId);
            return;
        }

        if (seat.isBookable()) {
            placeholder("Seat is already checked out and bookable: " + seatId);
            return;
        }

        seat.setBookable(true);
        tui.renderDisplayPrompt("Seat Checked Out",
                "Seat " + seatId + " has been checked out and is now bookable.");
    }

    private void flowViewOccupancySnapshot() {
        if (Venue.seats == null || Venue.seats.isEmpty()) {
            placeholder("No seats exist in this venue yet.");
            return;
        }

        int totalSeats = 0;
        int checkedInSeats = 0;
        List<String> checkedInSeatIds = new ArrayList<>();

        List<String> seatIds = new ArrayList<>(Venue.seats.keySet());
        Collections.sort(seatIds);

        for (String seatId : seatIds) {
            Seat seat = Venue.seats.get(seatId);
            if (seat == null) {
                continue;
            }

            totalSeats++;
            if (!seat.isBookable()) {
                checkedInSeats++;
                checkedInSeatIds.add(seatId);
            }
        }

        StringBuilder output = new StringBuilder();
        output.append("Venue: ").append(vm.venue.name).append("\n");
        output.append("Total seats: ").append(totalSeats).append("\n");
        output.append("Checked in (not bookable): ").append(checkedInSeats).append("\n");
        output.append("Checked out (bookable): ").append(totalSeats - checkedInSeats).append("\n");

        if (!checkedInSeatIds.isEmpty()) {
            output.append("\nChecked-in seat IDs:\n");
            for (String seatId : checkedInSeatIds) {
                output.append("- ").append(seatId).append("\n");
            }
        }

        tui.renderDisplayPrompt("Occupancy Snapshot", output.toString().trim());
    }

    private void placeholder(String message) {
        tui.renderDisplayPrompt("Notice", message + "\nVenue: " + vm.venue.name);
    }
}
