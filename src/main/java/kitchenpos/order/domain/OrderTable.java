package kitchenpos.order.domain;

import kitchenpos.exception.OrderTableError;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;

    private int numberOfGuests;

    private boolean empty;

    protected OrderTable() {

    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.tableGroup = null;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void checkOrderTableIsEmpty() {
        if (!empty) {
            throw new IllegalArgumentException(OrderTableError.IS_NOT_EMPTY);
        }
    }

    public void updateTableGroup(TableGroup tableGroup) {
        if (this.tableGroup != tableGroup) {
            this.tableGroup = tableGroup;
            tableGroup.addOrderTable(this);
        }
    }

    public void changeEmpty(boolean empty) {
        if (Objects.nonNull(this.tableGroup)) {
            throw new IllegalArgumentException(OrderTableError.HAS_GROUP);
        }

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
        this.tableGroup = null;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
