import javafx.util.Pair;

import java.util.ArrayList;

public class SeatHold {
    public ArrayList<Pair<Integer, Integer>> getSeatList() {
        return seatList;
    }

    public void setSeatList(ArrayList<Pair<Integer, Integer>> seatList) {
        this.seatList = seatList;
    }

    private ArrayList<Pair<Integer, Integer>> seatList;
}
