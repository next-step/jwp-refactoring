package kitchenpos.table.domain;

import kitchenpos.table.exception.EmptyException;
import kitchenpos.table.exception.IllegalNumberOfGuestsException;
import kitchenpos.table.exception.NullGroupException;
import kitchenpos.tablegroup.domain.TableGroup;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private TableGroup tableGroup;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this(null, tableGroup, numberOfGuests, empty);
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
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

    public void changeEmpty(final boolean empty) {
        validateGroup();
        this.empty = empty;
    }

    private void validateGroup() {
        if (Objects.nonNull(tableGroup)) {
            throw new NullGroupException();
        }
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        validateEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    private void validateEmpty() {
        if (empty) {
            throw new EmptyException();
        }
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalNumberOfGuestsException();
        }
    }

    public void checkEmptyTable() {
        if (empty) {
            throw new IllegalArgumentException();
        }
    }

    public void upgroup() {
        this.tableGroup = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTable that = (OrderTable) o;
        return numberOfGuests == that.numberOfGuests &&
                empty == that.empty &&
                Objects.equals(id, that.id) &&
                Objects.equals(tableGroup, that.tableGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroup, numberOfGuests, empty);
    }
}
