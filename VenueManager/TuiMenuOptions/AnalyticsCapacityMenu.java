package VenueManager.TuiMenuOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import VenueManager.VenueManager;
import VenueManager.BaseClasses.Seat;
import VenueManager.BaseClasses.SeatingArea;
import VenueManager.BaseClasses.Venue;
import VenueManager.InteractionClasses.TerminalInterface;

public class AnalyticsCapacityMenu {
    private final VenueManager vm;
    private final TerminalInterface tui;

    public AnalyticsCapacityMenu(VenueManager vm, TerminalInterface tui) {
        this.vm = vm;
        this.tui = tui;
    }

    public void show() {
        boolean inMenu = true;
        while (inMenu) {
            Integer choice = tui.renderMultiChoicePrompt("Analytics and Capacity",
                    "Venue management use case: inspect capacity and usage data.",
                    new String[] { "Capacity by Seating Area", "Booking Summary", "Attendance Summary" });

            if (choice == 0) {
                inMenu = false;
            } else if (choice == 1) {
                flowCapacityByArea();
            } else if (choice == 2) {
                flowBookingSummary();
            } else if (choice == 3) {
                flowAttendanceSummary();
            }
        }
    }

    private void flowCapacityByArea() {
        if (vm.venue.seatingAreas == null || vm.venue.seatingAreas.length == 0) {
            placeholder("No seating areas defined in this venue.");
            return;
        }

        StringBuilder output = new StringBuilder();
        output.append("Venue: ").append(vm.venue.name).append("\n\n");

        int totalCapacity = 0;
        int totalAvailable = 0;
        int totalBooked = 0;
        int totalCheckedIn = 0;

        for (SeatingArea area : vm.venue.seatingAreas) {
            if (area == null) {
                continue;
            }

            int areaCapacity = Venue.countSeatsInArea(area.id);
            int areaAvailable = 0;
            int areaBooked = 0;
            int areaCheckedIn = 0;

            if (Venue.seats != null) {
                for (String seatKey : Venue.seats.keySet()) {
                    if (seatKey != null && seatKey.startsWith(area.id + "-")) {
                        Seat seat = Venue.seats.get(seatKey);
                        if (seat != null) {
                            if (seat.isBookable() && (seat.getBookingId() == null || seat.getBookingId().isEmpty())) {
                                areaAvailable++;
                            } else if (!seat.isBookable()
                                    && (seat.getBookingId() != null && !seat.getBookingId().isEmpty())) {
                                areaCheckedIn++;
                            } else if (seat.getBookingId() != null && !seat.getBookingId().isEmpty()) {
                                areaBooked++;
                            }
                        }
                    }
                }
            }

            output.append(area.id).append(" - ").append(area.name).append("\n");
            output.append("  Total Capacity: ").append(areaCapacity).append("\n");
            output.append("  Available: ").append(areaAvailable).append("\n");
            output.append("  Booked: ").append(areaBooked).append("\n");
            output.append("  Checked In: ").append(areaCheckedIn).append("\n\n");

            totalCapacity += areaCapacity;
            totalAvailable += areaAvailable;
            totalBooked += areaBooked;
            totalCheckedIn += areaCheckedIn;
        }

        output.append("TOTALS\n");
        output.append("  Total Capacity: ").append(totalCapacity).append("\n");
        output.append("  Available: ").append(totalAvailable).append("\n");
        output.append("  Booked: ").append(totalBooked).append("\n");
        output.append("  Checked In: ").append(totalCheckedIn).append("\n");

        tui.renderDisplayPrompt("Capacity by Seating Area", output.toString().trim());
    }

    private void flowBookingSummary() {
        StringBuilder output = new StringBuilder();
        output.append("Venue: ").append(vm.venue.name).append("\n\n");

        if (vm.venue.bookings == null || vm.venue.bookings.isEmpty()) {
            output.append("No bookings found.");
            tui.renderDisplayPrompt("Booking Summary", output.toString().trim());
            return;
        }

        int totalBookings = vm.venue.bookings.size();
        output.append("Total Bookings: ").append(totalBookings).append("\n\n");

        if (vm.venue.events != null && !vm.venue.events.isEmpty()) {
            output.append("Bookings by Event:\n");
            for (String eventId : vm.venue.events.keySet()) {
                int eventBookingCount = 0;
                for (String bookingId : vm.venue.bookings.keySet()) {
                    if (vm.venue.bookings.get(bookingId).getEventId().equals(eventId)) {
                        eventBookingCount++;
                    }
                }
                output.append("  ").append(eventId).append(": ").append(eventBookingCount).append(" bookings\n");
            }
        }

        output.append("\nRecent Bookings:\n");
        List<String> bookingIds = new ArrayList<>(vm.venue.bookings.keySet());
        Collections.reverse(bookingIds);

        int displayCount = Math.min(5, bookingIds.size());
        for (int i = 0; i < displayCount; i++) {
            String bookingId = bookingIds.get(i);
            output.append("  - ").append(bookingId.substring(0, Math.min(8, bookingId.length())))
                    .append("...: ").append(vm.venue.bookings.get(bookingId).getCustomerName()).append("\n");
        }

        tui.renderDisplayPrompt("Booking Summary", output.toString().trim());
    }

    private void flowAttendanceSummary() {
        StringBuilder output = new StringBuilder();
        output.append("Venue: ").append(vm.venue.name).append("\n\n");

        if (Venue.seats == null || Venue.seats.isEmpty()) {
            output.append("No seats found in this venue.");
            tui.renderDisplayPrompt("Attendance Summary", output.toString().trim());
            return;
        }

        int totalSeats = Venue.seats.size();
        int checkedInSeats = 0;
        int checkedOutSeats = 0;

        for (String seatKey : Venue.seats.keySet()) {
            Seat seat = Venue.seats.get(seatKey);
            if (seat != null) {
                if (!seat.isBookable()) {
                    checkedInSeats++;
                } else {
                    checkedOutSeats++;
                }
            }
        }

        double occupancyRate = (totalSeats > 0) ? (double) checkedInSeats / totalSeats * 100 : 0;

        output.append("Total Seats: ").append(totalSeats).append("\n");
        output.append("Checked In: ").append(checkedInSeats).append("\n");
        output.append("Checked Out: ").append(checkedOutSeats).append("\n");
        output.append("Occupancy Rate: ").append(String.format("%.1f", occupancyRate)).append("%\n");

        tui.renderDisplayPrompt("Attendance Summary", output.toString().trim());
    }

    private void placeholder(String message) {
        tui.renderDisplayPrompt("Notice", message + "\nVenue: " + vm.venue.name);
    }
}
