package kitchenpos.order.domain;

import kitchenpos.exception.OrderTableError;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableGroupId;

    private int numberOfGuests;

    private boolean empty;

    protected OrderTable() {

    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void checkHasTableGroup() {
        if (hasTableGroup()) {
            throw new IllegalArgumentException(OrderTableError.HAS_GROUP);
        }
    }

    public void updateTableGroup(Long tableGroupId) {
        if (!isEmpty()) {
            throw new IllegalArgumentException(OrderTableError.IS_NOT_EMPTY);
        }
        changeEmpty(false);
        this.tableGroupId = tableGroupId;
    }

    public boolean hasTableGroup() {
        return Objects.nonNull(tableGroupId);
    }

    public void changeEmpty(boolean empty) {
        checkHasTableGroup();
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (empty) {
            throw new IllegalArgumentException(OrderTableError.IS_EMPTY);
        }
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException(OrderTableError.INVALID_NUMBER_OF_GUESTS);
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void ungroup() {
        this.tableGroupId = null;
        changeEmpty(true);
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

    public boolean isEmpty() {
        return empty;
    }
}
