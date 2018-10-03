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

    @Test
    public void getSeatsRemaining() {
        Theater theater = init();
        Integer seatCount = 0;
        ArrayList<ArrayList<Seat>> seatLists = theater.getSeats();
        for (ArrayList<Seat> rowList : seatLists){
            seatCount += rowList.size();
        }
        assertEquals(seatCount, theater.getSeatsRemaining());
    }
}