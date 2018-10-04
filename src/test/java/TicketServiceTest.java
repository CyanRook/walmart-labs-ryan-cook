import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

public class TicketServiceTest {

    private Integer minRows = 5;
    private Integer maxRows = 20;
    private Integer minColumns = 5;
    private Integer maxColumns = 50;

    private TicketService init() {
        Random rand = new Random();
        ArrayList<Integer> rowSeats = new ArrayList<>();
        Integer numRows = rand.nextInt(maxRows - minRows) + minRows;
        for (int i = 0; i < numRows; i++){
            rowSeats.add(rand.nextInt(maxColumns - minColumns)+minColumns);
        }
        return new TicketService(rowSeats);
    }

    @Test
    public void numSeatsAvailableConstruction() {
        TicketService ticketService = init();
        Theater theater = ticketService.getTheater();
        assertEquals(new Integer(ticketService.numSeatsAvailable()), theater.getSeatsRemaining());
    }

    @Test
    public void findAndHoldSeatsOnce() throws Exception {
        Random rand = new Random();
        String customerEmail = "test@test.test";
        TicketService ticketService = init();
        Integer ticketsTotal = ticketService.numSeatsAvailable();
        Integer ticketsHeld = rand.nextInt(ticketsTotal);
        SeatHold seatHold = ticketService.findAndHoldSeats(ticketsHeld, customerEmail);
        assertNotEquals(seatHold, null);
        assertEquals(seatHold.getCustomerEmail(), customerEmail);
        assertEquals(ticketsTotal-ticketsHeld, ticketService.numSeatsAvailable());
        assertEquals(ticketsHeld, ticketService.getTheater().getSeatsOfType(Seat.Held));
        assertEquals(new Integer(0), ticketService.getTheater().getSeatsOfType(Seat.Reserved));
    }

    @Test
    public void findAndHoldSeatsTwice() throws Exception {
        Random rand = new Random();
        String customerEmail = "test@test.test";
        TicketService ticketService = init();
        Integer ticketsTotal = ticketService.numSeatsAvailable();
        Integer ticketsReservedOnce = rand.nextInt(ticketsTotal);
        SeatHold seatHoldOne = ticketService.findAndHoldSeats(ticketsReservedOnce, customerEmail);
        assertNotEquals(seatHoldOne, null);
        assertEquals(seatHoldOne.getCustomerEmail(), customerEmail);
        assertEquals(ticketsTotal-ticketsReservedOnce, ticketService.numSeatsAvailable());
        Integer ticketsReservedTwice = rand.nextInt(ticketsTotal-ticketsReservedOnce);
        SeatHold seatHoldTwo = ticketService.findAndHoldSeats(ticketsReservedTwice, customerEmail);
        assertNotEquals(seatHoldTwo, null);
        assertEquals(seatHoldTwo.getCustomerEmail(), customerEmail);
        assertEquals(ticketsTotal-ticketsReservedTwice-ticketsReservedOnce, ticketService.numSeatsAvailable());
        assertEquals(new Integer(0), ticketService.getTheater().getSeatsOfType(Seat.Reserved));
    }

    @Test
    public void reserveSeats() throws Exception{
        // Set It up
        Random rand = new Random();
        String customerEmail = "test@test.test";
        TicketService ticketService = init();

        // Get Total Seats
        Integer ticketsTotal = ticketService.numSeatsAvailable();

        // Select Some to Hold
        Integer ticketsReservedOnce = rand.nextInt(ticketsTotal);
        SeatHold seatHoldOne = ticketService.findAndHoldSeats(ticketsReservedOnce, customerEmail);

        // Confirm First Reservation Worked
        assertNotEquals(seatHoldOne, null);
        assertEquals(seatHoldOne.getCustomerEmail(), customerEmail);
        assertEquals(ticketsTotal-ticketsReservedOnce, ticketService.numSeatsAvailable());

        // Hold A Second Set
        Integer ticketsReservedTwice = rand.nextInt(ticketsTotal-ticketsReservedOnce);
        SeatHold seatHoldTwo = ticketService.findAndHoldSeats(ticketsReservedTwice, customerEmail);

        // Confirm it got held
        assertNotEquals(seatHoldTwo, null);
        assertEquals(seatHoldTwo.getCustomerEmail(), customerEmail);
        assertEquals(ticketsTotal-ticketsReservedTwice-ticketsReservedOnce, ticketService.numSeatsAvailable());

        // Reserve Some Seats
        ticketService.reserveSeats(seatHoldTwo.getHoldId(), seatHoldTwo.getCustomerEmail());

        // Confirm it was reserved
        assertEquals(ticketsTotal-ticketsReservedTwice-ticketsReservedOnce, ticketService.numSeatsAvailable());
        assertEquals(ticketsReservedOnce, ticketService.getTheater().getSeatsOfType(Seat.Held));
        assertEquals(ticketsReservedTwice, ticketService.getTheater().getSeatsOfType(Seat.Reserved));
    }
}