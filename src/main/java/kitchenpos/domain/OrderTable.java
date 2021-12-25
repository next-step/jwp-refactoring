package kitchenpos.domain;

import kitchenpos.exception.CannotChangeEmptyException;
import kitchenpos.exception.CannotChangeNumberOfGuestsException;
import kitchenpos.exception.CannotUngroupException;
import kitchenpos.exception.NegativeNumberOfGuestsException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    public static final int ZERO = 0;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup = new TableGroup();
    private int numberOfGuests;
    private boolean empty;
    @Embedded
    private Orders orders;

    public OrderTable() {
    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.orders = new Orders();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void changeTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        if (this.empty) {
            throw new CannotChangeNumberOfGuestsException();
        }
        if (numberOfGuests < ZERO) {
            throw new NegativeNumberOfGuestsException();
        }
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(final boolean empty) {
        validateChangeEmpty();
        this.empty = empty;
    }

    private void validateChangeEmpty() {
        if (Objects.nonNull(getTableGroup())) {
            throw new CannotChangeEmptyException();
        }
        if (this.orders.canUngroupOrChange()) {
            throw new CannotChangeEmptyException();
        }
    }

    public void ungroup() {
        if (!orders.canUngroupOrChange()) {
            throw new CannotUngroupException();
        }
        this.tableGroup = null;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }
}
