import javafx.util.Pair;

import java.time.Instant;
import java.util.*;

public class TicketService {

    private Theater theater;
    private HashMap<String, SeatHold> seatHoldMap;
    private HashMap<String, SeatHold> seatReserveMap;
    private HashMap<String, HashSet<String>> emailToHoldId;
    private HashMap<String, HashSet<String>> emailToReserveId;
    private ArrayList<Pair<String, Long>> holdExpirationList;
    private Integer holdLifetime;
    private Timer timer;
    private TimerTask task;

    public TicketService(ArrayList<Integer> rowSeatCount){
        theater = new Theater(rowSeatCount);
        seatHoldMap = new HashMap<>();
        seatReserveMap = new HashMap<>();
        emailToHoldId = new HashMap<>();
        emailToReserveId = new HashMap<>();
        holdExpirationList = new ArrayList<>();
        holdLifetime = 60;
        // Set up timer to expire the holds
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                int cutoff = -1;
                for (int i = 0; i < holdExpirationList.size(); i++){
                    Pair<String, Long> expiration = holdExpirationList.get(i);
                    if (Instant.now().getEpochSecond() - expiration.getValue() > holdLifetime){
                        SeatHold seatHold = seatHoldMap.get(expiration.getKey());
                        // Clear Seats
                        theater.releaseSeats(seatHold.getSeatList());
                        // Remove HoldID from Email look up as it is no longer in held
                        emailToHoldId.get(seatHold.getCustomerEmail()).remove(seatHold.getHoldId());
                        // Remove the SeatHold object from the map
                        seatHoldMap.remove(seatHold.getHoldId());
                        cutoff = i;
                    }
                    else{
                        break;
                    }
                }
                for (int i = 0; i <= cutoff; i++) {
                    holdExpirationList.remove(i);
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1);
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
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Requested ");
            stringBuilder.append(numSeats);
            stringBuilder.append(" - Available ");
            stringBuilder.append(theater.getSeatsRemaining());
            stringBuilder.append(" - Not Enough Seats");
            throw new Exception(stringBuilder.toString());
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
        holdExpirationList.add(new Pair<>(holdId, Instant.now().getEpochSecond()));
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
        theater.reserveSeats(seatHold.getSeatList());
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
        emailToHoldId.get(customerEmail).remove(seatHoldId);
        // Remove the SeatHold object from the map
        seatHoldMap.remove(seatHoldId);
        return confirmationCode;
    }
}
