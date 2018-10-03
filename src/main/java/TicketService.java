import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class TicketService {

    private Theater theater;
    private HashMap<String, SeatHold> seatHoldMap;
    private HashMap<String, SeatHold> seatReserveMap;

    public TicketService(ArrayList<Integer> rowSeatCount){
        theater = new Theater(rowSeatCount);
        seatHoldMap = new HashMap<>();
        seatReserveMap = new HashMap<>();
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
        return 0;
    }

    /** * Find and hold the best available seats for a customer
     *
     * @param numSeats the number of seats to find and hold
     * @param customerEmail unique identifier for the customer
     * @return a SeatHold object identifying the specific seats and related information
     */
    SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
        return new SeatHold();
    }

    /** * Commit seats held for a specific customer
     *
     * @param seatHoldId the seat hold identifier
     * @param customerEmail the email address of the customer to which the seat hold is assigned
     * @return a reservation confirmation code */
    String reserveSeats(int seatHoldId, String customerEmail) {
        SeatHold seatHold = seatHoldMap.get(seatHoldId);
        for (Pair location : seatHold.getSeatList()) {
            theater.setSeat(location, Seat.Reserved);
        }
        return new String();
    }
}
