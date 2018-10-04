import javafx.util.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

public class TheaterTest {

    private Integer minRows = 5;
    private Integer maxRows = 20;
    private Integer minColumns = 5;
    private Integer maxColumns = 50;

    private Theater init() {
        Random rand = new Random();
        ArrayList<Integer> rowSeats = new ArrayList<>();
        Integer numRows = rand.nextInt(maxRows - minRows) + minRows;
        for (int i = 0; i < numRows; i++) {
            rowSeats.add(rand.nextInt(maxColumns - minColumns) + minColumns);
        }
        return new Theater(rowSeats);
    }

    private Theater setSize(Integer rows, Integer columns){
        ArrayList<Integer> rowSeats = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            rowSeats.add(columns);
        }
        return new Theater(rowSeats);
    }

    @Test
    public void getSeatsRemainingRandom() {
        Theater theater = init();
        Integer seatCount = 0;
        ArrayList<ArrayList<Seat>> seatLists = theater.getSeats();
        for (ArrayList<Seat> rowList : seatLists){
            seatCount += rowList.size();
        }
        assertEquals(seatCount, theater.getSeatsRemaining());
        assertEquals(theater.getSeatsOfType(Seat.Available), theater.getSeatsRemaining());
        assertEquals(theater.getSeatsOfType(Seat.Held), new Integer(0));
        assertEquals(theater.getSeatsOfType(Seat.Reserved), new Integer(0));
    }

    @Test
    public void getSeatsRemainingSquare() {
        Theater theater = setSize(10, 10);
        assertEquals(new Integer(100), theater.getSeatsRemaining());
        assertEquals(theater.getSeatsOfType(Seat.Available), theater.getSeatsRemaining());
        assertEquals(theater.getSeatsOfType(Seat.Held), new Integer(0));
        assertEquals(theater.getSeatsOfType(Seat.Reserved), new Integer(0));
    }

    @Test
    public void getContiguousSeats() {
        Theater theater = setSize(10, 10);
        ArrayList<Pair<Integer, Integer>> seats = theater.findSeats(10);
        assertNotEquals(seats, null);
        assertEquals(seats.size(), 10);
        for (int i = 0; i < 10; i++){
            assertEquals(seats.get(i).getKey(), new Integer(0));
        }
    }
}