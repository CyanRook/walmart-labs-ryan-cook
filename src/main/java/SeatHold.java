import javafx.util.Pair;

import java.util.ArrayList;

public class SeatHold {
    private ArrayList<Pair<Integer, Integer>> seatList;
    private String customerEmail;
    private String holdId;

    public ArrayList<Pair<Integer, Integer>> getSeatList() {
        return seatList;
    }

    public void setSeatList(ArrayList<Pair<Integer, Integer>> seatList) {
        this.seatList = seatList;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getHoldId() {
        return customerEmail;
    }

    public void setHoldId(String holdId) {
        this.holdId = holdId;
    }
}
