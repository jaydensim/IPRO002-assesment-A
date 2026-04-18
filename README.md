# IPRO002 *Assesment A*
**Topic: Concert**

---

## Business Case:
> [!TIP]
> *Changed as of 13 April 2026 for simplicity.*
A venue management company that maintains and operates stadiuyms and thearters primarily for concerts has determined that no existing solutions on the market fit their needs. They would like to build a new Venue Management System that suits their needs. It should be able to handle seat allocation, concert groups and times, arrival and departures and associated check-ins. 

## 4-5 use cases
1. **For Concertgoers:** Handle seat allocation based on event times and seating tiers.
2. **For Concert Organisers:** Handle the booking of timeslots for concerts.
3. **For Venue Organisers:** Handle check-ins and departures.
4. **For Venue Management** Provide analytics data and capacity information.

---

## Structure:

There will be 3 atomic base classes: the `Seat`, the `Event` and the `Venue`.

1. `Seat`
    - Represents a single seat.
    - A `Seat` can be assigned to a `Booking`.

2. `Event`
    - Represents a timeslot in which an event will happen. Such as a concert.
    - An `Event` contains a timeslot and a name and a description.

3. `Venue`
    - Represents a venue which contains Arrays of Seats, and an Array of Events.
    - To handle things such as different seating areas, the `Event` will contain several Arrays of `Seat`s - I think we should create a `SeatingArea` class for this. 


### System Overview

#### VenueManager
Initalises and holds lookup tables for `Venue`s `Seat`s, `Event`s. Venue holds information about the seating class and so on.


### Class Definitions

#### `Seat`
Each `Seat` should define:
- The class type of seat (predefined or not, i'm not sure. maybe we define in venue management or something but this should be a numeric value to allow for sorting)
- A few IDs.
    - A seat UUID.
    - A numerical Area/Row/Seat number
- A seat Display Name.
- A linked booking ID, if it has one.