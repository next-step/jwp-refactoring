package kitchenpos.table.dto;


import kitchenpos.table.OrderTable;

public class TableAddRequest {

    private int numberOfGuests;
    private boolean empty;

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

}
