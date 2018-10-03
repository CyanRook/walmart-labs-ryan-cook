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

    }

    @Test
    public void findAndHoldSeats() {
    }

    @Test
    public void reserveSeats() {
    }
}