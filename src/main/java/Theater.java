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

    public ArrayList<Pair<Integer, Integer>> findContigousSeats(Integer count){
        /* Identify ideal seats
           Criteria Used
           1. Contiguous Seats
           2. Towards the Front
           3. Towards the Center
        */
        ArrayList<Pair<Integer, Integer>> seatList = null;
        Integer rowId = -1;
        // Check each Row starting at the front
        for (ArrayList row : seats){
            rowId += 1;
            //  If cancellations occur, then the center might not be filled contiguously, must search over whole row.
            Integer leftSearch = row.size()/2;
            Integer rightSearch = (row.size()+1)/2;
            // Search to the left
            while (leftSearch > 0){
                // Find First Available Seats to the left
                while ((leftSearch > 0) && (row.get(leftSearch) != Seat.Available)){
                    leftSearch -= 1;
                }
                // Jump out if we don't have any available seats to the left
                if (row.get(leftSearch) != Seat.Available){
                    break;
                }
                Integer leftBound = leftSearch;
                Integer rightBound = leftSearch;
                // Search For More to the left
                while ((leftBound > 0) && (row.get(leftBound) == Seat.Available)){
                    leftBound -= 1;
                }
                // Have to check to the right on the first pass
                while ((rightBound < row.size()-1) && (row.get(rightBound) == Seat.Available)){
                    rightBound += 1;
                }
                // If we don't have available, move back
                if (row.get(leftBound) != Seat.Available){
                    leftBound += 1;
                }
                // If we don't have available, move back
                if (row.get(rightBound) != Seat.Available){
                    rightBound -= 1;
                }
                // Update overall search bounds
                leftSearch = leftBound;
                rightSearch = max(rightBound, rightSearch);
                if (leftBound - rightBound + 1 >= count){
                    if (leftBound + (count/2) < row.size() / 2){
                        seatList = new ArrayList<>();
                        for (int i = leftBound; i < leftBound + count; i++){
                            seatList.add(new Pair<>(rowId, i));
                        }
                    }
                    else if ((row.size()+1)/2 < rightBound - (count/2)){
                        seatList = new ArrayList<>();
                        for (int i = rightBound; i > rightBound - count; i--){
                            seatList.add(new Pair<>(rowId, i));
                        }
                    }
                    else{
                        seatList = new ArrayList<>();
                        for (int i = 0; i < count; i--){
                            seatList.add(new Pair<>(rowId, (row.size()/2)-(count/2)+i));
                        }
                    }
                    break;
                }
            }
            // Search to the right
            while (rightSearch < row.size()-1){
                while ((rightSearch < row.size()-1) && (row.get(rightSearch) != Seat.Available)){
                    rightSearch += 1;
                }
                // Jump out if we don't have any available seats to the left
                if (row.get(leftSearch) != Seat.Available){
                    break;
                }
                Integer leftBound = leftSearch;
                Integer rightBound = leftSearch;
                // Search For More to the left
                while ((leftBound > 0) && (row.get(leftBound) == Seat.Available)){
                    leftBound -= 1;
                }
                // Have to check to the right on the first pass
                while ((rightBound < row.size()-1) && (row.get(rightBound) == Seat.Available)){
                    rightBound += 1;
                }
                // If we don't have available, move back
                if (row.get(leftBound) != Seat.Available){
                    leftBound += 1;
                }
                // If we don't have available, move back
                if (row.get(rightBound) != Seat.Available){
                    rightBound -= 1;
                }
                // Update overall search bounds
                leftSearch = leftBound;
                rightSearch = max(rightBound, rightSearch);
                if (leftBound - rightBound + 1 >= count){
                    if (leftBound + (count/2) < row.size() / 2){
                        seatList = new ArrayList<>();
                        for (int i = leftBound; i < leftBound + count; i++){
                            seatList.add(new Pair<>(rowId, i));
                        }
                    }
                    else if ((row.size()+1)/2 < rightBound - (count/2)){
                        seatList = new ArrayList<>();
                        for (int i = rightBound; i > rightBound - count; i--){
                            seatList.add(new Pair<>(rowId, i));
                        }
                    }
                    else{
                        seatList = new ArrayList<>();
                        for (int i = 0; i < count; i--){
                            seatList.add(new Pair<>(rowId, (row.size()/2)-(count/2)+i));
                        }
                    }
                    break;
                }
            }
            if (seatList != null){
                break;
            }
        }
        return seatList;
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
                if (row.get(rightSearch) == Seat.Available){
                    seatList.add(new Pair<>(rowId, rightSearch));
                }
                if (seatList.size() == count){
                    break;
                }
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
}
