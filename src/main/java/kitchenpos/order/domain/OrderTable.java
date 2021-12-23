package kitchenpos.order.domain;

import kitchenpos.order.exceptions.InputTableDataErrorCode;
import kitchenpos.order.exceptions.InputTableDataException;

public class OrderTable {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this(tableGroupId, numberOfGuests, empty);
        this.id = id;
    }

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        validateWrongNumberOfGuest(numberOfGuests);
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void seatNumberOfGuests(int numberOfGuests) {
        validateWrongNumberOfGuest(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void allocateTableGroupId(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void emptyTableGroupId() {
        this.tableGroupId = null;
    }

    public void enterGuest() {
        this.empty = false;
    }

    public void leaveGuest() {
        this.empty = true;
    }

    private void validateWrongNumberOfGuest(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new InputTableDataException(InputTableDataErrorCode.THE_NUMBER_OF_GUESTS_IS_NOT_LESS_THAN_ZERO);
        }
    }
}
