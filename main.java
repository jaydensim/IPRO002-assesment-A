import VenueManager.VenueManager;
import VenueManager.BaseClasses.Venue;
import VenueManager.InteractionClasses.TerminalInterface;
import VenueManager.TuiMenuOptions.AnalyticsCapacityMenu;
import VenueManager.TuiMenuOptions.BookSeatsMenu;
import VenueManager.TuiMenuOptions.CheckInOutMenu;
import VenueManager.TuiMenuOptions.ManageEventsMenu;
import VenueManager.TuiMenuOptions.ManageVenueLayoutMenu;

import java.util.UUID;

public class main {

    public static VenueManager vm;
    private static TerminalInterface tui;
    private static BookSeatsMenu bookSeatsMenu;
    private static CheckInOutMenu checkInOutMenu;
    private static ManageEventsMenu manageEventsMenu;
    private static AnalyticsCapacityMenu analyticsCapacityMenu;
    private static ManageVenueLayoutMenu manageVenueLayoutMenu;

    public static void main(String[] args) {
        stdout_proginfo();
        tui = new TerminalInterface("Venue Manager");
        tui.setHeaderText("Venue Manager - IPRO002 Assesment A");

        menu_welcome();

        if (vm == null) {
            System.out.println("No venue loaded. Exiting program.");
            return;
        }

        tui.setHeaderText(
                "Venue Manager - IPRO002 Assesment A\nVenue: " + vm.venue.name + " (" + vm.venue.id + ")");

        bookSeatsMenu = new BookSeatsMenu(vm, tui);
        checkInOutMenu = new CheckInOutMenu(vm, tui);
        manageEventsMenu = new ManageEventsMenu(vm, tui);
        analyticsCapacityMenu = new AnalyticsCapacityMenu(vm, tui);
        manageVenueLayoutMenu = new ManageVenueLayoutMenu(vm, tui);

        menu_main();
    }

    private static void menu_welcome() {
        String choice = tui.renderYNPrompt("Create New Venue",
                "No currently loaded Venue exists and the functionality for loading Venues is currently unimplemented. Create a new Venue?");

        if (choice.equals("Y")) {
            main.vm = new VenueManager(
                    new Venue(UUID.randomUUID().toString(), "Sample Venue", "This is a sample venue.", null));
            main.vm.putSampleSeatingData();
        } else {
            tui.render();
        }

    }

    private static void menu_main() {
        boolean running = true;
        while (running) {
            Integer choice = tui.renderMultiChoicePrompt("Main Menu", "Choose a program section to enter.",
                    new String[] {
                            "Book Seats",
                            "Check In/Out",
                            "Manage Events",
                            "Analytics/Capacity",
                            "Manage Venue Layout"
                    });

            if (choice == 0) {
                running = false;
            } else if (choice == 1) {
                bookSeatsMenu.show();
            } else if (choice == 2) {
                checkInOutMenu.show();
            } else if (choice == 3) {
                manageEventsMenu.show();
            } else if (choice == 4) {
                analyticsCapacityMenu.show();
            } else if (choice == 5) {
                manageVenueLayoutMenu.show();
            }
        }
    }

    private static void stdout_proginfo() {
        System.out.println("Venue Manager - IPRO002 Assesment A");
        System.out.println("Jayden Sim 14744876 / Andrew");
    }
}