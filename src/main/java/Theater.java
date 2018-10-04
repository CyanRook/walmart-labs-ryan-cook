import javafx.util.Pair;

import java.util.ArrayList;

import static java.lang.Integer.max;

public class Theater {
    private ArrayList<ArrayList<Seat>> seats;
    private Integer seatsRemaining;

    public Theater(ArrayList<Integer> rowSeatCount){
        seats = new ArrayList<>();
        seatsRemaining = 0;
        for (Integer rowCount: rowSeatCount) {
            ArrayList<Seat> row = new ArrayList<>(rowCount);
            for (int i = 0; i < rowCount; i++){
                row.add(Seat.Available);
            }
            seats.add(row);
            seatsRemaining += rowCount;
        }
    }

    public Integer getSeatsRemaining() {
        return seatsRemaining;
    }

    public Integer getSeatsOfType(Seat seat){
        Integer count = 0;
        for(ArrayList<Seat> row : seats){
            for (Seat col : row){
                if (col.equals(seat)){
                    count += 1;
                }
            }
        }
        return count;
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

    // TODO: Implement grabbing contiguous seats when possible
    public ArrayList<Pair<Integer, Integer>> findContigousSeats(Integer count) {
        /* Identify ideal seats
           Criteria Used
           1. Contiguous Seats
           2. Towards the Front
           3. Towards the Center
        */
        ArrayList<Pair<Integer, Integer>> seatList = null;
        return null;
    }

    public ArrayList<Pair<Integer, Integer>> findSeats(Integer count) {
        /* Identify ideal seats
           Criteria Used
           1. Towards the Front
           2. Towards the Center
        */
        ArrayList<Pair<Integer, Integer>> seatList = new ArrayList<>();
        Integer rowId = -1;
        // Check each Row starting at the front
        for (ArrayList row : seats) {
            rowId += 1;
            Integer leftSearch = row.size() / 2;
            Integer rightSearch = (row.size() + 1) / 2;
            while ((leftSearch >= 0) && (rightSearch <= row.size()-1)){
                if (row.get(leftSearch) == Seat.Available){
                    seatList.add(new Pair<>(rowId, leftSearch));
                }
                if (seatList.size() == count){
                    break;
                }
                if ((row.get(rightSearch) == Seat.Available) && (leftSearch != rightSearch)){
                    seatList.add(new Pair<>(rowId, rightSearch));
                }
                if (seatList.size() == count){
                    break;
                }
                leftSearch -= 1;
                rightSearch += 1;
            }
            if (seatList.size() == count){
                break;
            }
        }
        if (seatList.size() == count) {
            return seatList;
        }
        else {
            return null;
        }
    }

    public void holdSeats(ArrayList<Pair<Integer, Integer>> seatList) throws Exception{
        for (Pair seat : seatList){
            if (getSeat(seat) != Seat.Available) {
                throw new Exception("Trying to Reserve an Unavailable Seat");
            }
            setSeat(seat, Seat.Held);
        }
    }

    public void reserveSeats(ArrayList<Pair<Integer, Integer>> seatList) throws Exception{
        for (Pair seat : seatList){
            if (getSeat(seat) != Seat.Available) {
                throw new Exception("Trying to Reserve an Unavailable Seat");
            }
            setSeat(seat, Seat.Reserved);
        }
    }
}
