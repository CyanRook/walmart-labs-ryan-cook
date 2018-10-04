import javafx.util.Pair;

import java.util.*;

public class TicketService {

    private Theater theater;
    private HashMap<String, SeatHold> seatHoldMap;
    private HashMap<String, SeatHold> seatReserveMap;
    private HashMap<String, HashSet<String>> emailToHoldId;
    private HashMap<String, HashSet<String>> emailToReserveId;

    public TicketService(ArrayList<Integer> rowSeatCount){
        theater = new Theater(rowSeatCount);
        seatHoldMap = new HashMap<>();
        seatReserveMap = new HashMap<>();
        emailToHoldId = new HashMap<>();
        emailToReserveId = new HashMap<>();
    }

    public Theater getTheater() {
        return theater;
    }

    public HashMap<String, SeatHold> getSeatHoldMap() {
        return seatHoldMap;
    }

    public HashMap<String, SeatHold> getSeatReserveMap() {
        return seatReserveMap;
    }

    /** * The number of seats in the venue that are neither held nor reserved
     *
     * @return the number of tickets available in the venue
     */
    int numSeatsAvailable() {
        return theater.getSeatsRemaining();
    }

    /** * Find and hold the best available seats for a customer
     *
     * @param numSeats the number of seats to find and hold
     * @param customerEmail unique identifier for the customer
     * @return a SeatHold object identifying the specific seats and related information
     */
    SeatHold findAndHoldSeats(int numSeats, String customerEmail) throws Exception {
        /* Identify ideal seats
           Criteria Used
           1. Contiguous Seats
           2. Towards the Front
           3. Towards the Center
        */
        ArrayList<Pair<Integer, Integer>> seats;
        seats = theater.findContigousSeats(numSeats);
        if (seats == null){
            seats = theater.findSeats(numSeats);
        }
        if (seats == null){
            throw new Exception("Not Enough Seats");
        }
        theater.holdSeats(seats);
        // Generate a new Hold ID
        String holdId = UUID.randomUUID().toString();
        SeatHold seatHold = new SeatHold();
        seatHold.setSeatList(seats);
        seatHold.setCustomerEmail(customerEmail);
        seatHold.setHoldId(holdId);
        seatHoldMap.put(holdId, seatHold);
        if (emailToHoldId.containsKey(customerEmail)) {
            emailToHoldId.get(customerEmail).add(holdId);
        }
        else {
            emailToHoldId.put(customerEmail, new HashSet<>(Arrays.asList(holdId)));
        }
        return seatHold;
    }

    /** * Commit seats held for a specific customer
     *
     * @param seatHoldId the seat hold identifier
     * @param customerEmail the email address of the customer to which the seat hold is assigned
     * @return a reservation confirmation code */
    String reserveSeats(String seatHoldId, String customerEmail) throws Exception {
        // Get the SeatHold Object
        SeatHold seatHold = seatHoldMap.get(seatHoldId);
        if (seatHold == null){
            throw new Exception("Hold ID Not Found");
        }
        // Turn the Seats from Held to Reserved
        for (Pair location : seatHold.getSeatList()) {
            theater.setSeat(location, Seat.Reserved);
        }
        // Generate a new Confirmation Code
        String confirmationCode = UUID.randomUUID().toString();
        // Add the SeatHold object to a mpa with the confirmation code to enable seat lookup by confirmation code
        seatReserveMap.put(confirmationCode, seatHold);
        // Allow Reservation lookup by email
        if (emailToReserveId.containsKey(customerEmail)) {
            emailToReserveId.get(customerEmail).add(confirmationCode);
        }
        else {
            emailToReserveId.put(customerEmail, new HashSet<>(Arrays.asList(confirmationCode)));
        }
        // Remove HoldID from Email look up as it is no longer in held
        emailToHoldId.get(customerEmail).remove(confirmationCode);
        // Remove the SeatHold object from the map
        seatHoldMap.remove(seatHoldId);
        return confirmationCode;
    }
}
