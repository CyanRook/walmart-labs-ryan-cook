import javafx.util.Pair;

import java.util.ArrayList;

public class Theater {
    private ArrayList<ArrayList<Seat>> seats;
    private Integer seatsRemaining;

    public Theater(ArrayList<Integer> rowSeatCount){
        seats = new ArrayList<>();
        seatsRemaining = 0;
        for (Integer rowCount: rowSeatCount) {
            ArrayList<Seat> row = new ArrayList<>(rowCount);
            seats.add(row);
            seatsRemaining += rowCount;
        }
    }

    public Integer getSeatsRemaining() {
        return seatsRemaining;
    }

    public ArrayList<ArrayList<Seat>> getSeats() {
        return seats;
    }

    public Seat getSeat(Integer row, Integer column){
        return seats.get(row).get(column);
    }

    public void setSeat(Integer row, Integer column, Seat type){
        if ((seats.get(row).get(column) == Seat.Available) && (type != Seat.Available))
            seatsRemaining -= 1;
        if ((seats.get(row).get(column) != Seat.Available) && (type == Seat.Available))
            seatsRemaining += 1;
        seats.get(row).set(column, type);
    }

    public Seat getSeat(Pair<Integer, Integer> location){
        return getSeat(location.getKey(),location.getValue());
    }

    public void setSeat(Pair<Integer, Integer> location, Seat type){
        setSeat(location.getKey(),location.getValue(), type);
    }
}
