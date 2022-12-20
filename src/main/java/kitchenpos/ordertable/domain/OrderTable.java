package kitchenpos.ordertable.domain;

import javax.persistence.*;
import java.util.Objects;

import static kitchenpos.common.ErrorMessage.*;

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

    private OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException(INVALID_CUSTOMER_NUMBER.getMessage());
        }

        if (isEmpty()) {
            throw new IllegalArgumentException(EMPTY_ORDER_TABLE.getMessage());
        }

        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(final boolean empty) {
        validateTableGroup();
        this.empty = empty;
    }

    private void validateTableGroup() {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException(ALREADY_TABLE_GROUP.getMessage());
        }
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void unGroup() {
        tableGroupId = null;
    }

    public void group(final Long tableGroupId) {
        validateTableGroup();
        this.tableGroupId = tableGroupId;
    }
}
