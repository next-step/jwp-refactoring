package kitchenpos.table.domain;

import common.domain.Empty;
import kitchenpos.table.exception.NegativeNumberOfGuestsException;
import kitchenpos.table.exception.NotEmptyOrderTableGroupException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableGroupId;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Embedded
    private Empty empty;

    protected OrderTable() {}

    private OrderTable(final Long id, final Long tableGroupId, final NumberOfGuests numberOfGuests, final Empty empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }


    public static OrderTable of(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        return new OrderTable(id, tableGroupId, NumberOfGuests.from(numberOfGuests), Empty.from(empty));
    }

    public static OrderTable of(final Long id, final int numberOfGuests, final boolean empty) {
        return new OrderTable(id, null, NumberOfGuests.from(numberOfGuests), Empty.from(empty));
    }

    public static OrderTable of(final int numberOfGuests, final boolean empty) {
        return new OrderTable(null, null, NumberOfGuests.from(numberOfGuests), Empty.from(empty));
    }

    public void changeEmptyStatus(final boolean empty) {
        if (Objects.nonNull(tableGroupId)) {
            throw new NotEmptyOrderTableGroupException();
        }
        this.empty = Empty.from(empty);
    }

    public void changeNumberOfGuests(final int numbers) {
        if (this.getEmpty().isEmpty()) {
            throw new NegativeNumberOfGuestsException();
        }
        this.numberOfGuests = NumberOfGuests.from(numbers);
    }

    public void unGroup() {
        this.tableGroupId = null;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public Empty getEmpty() {
        return empty;
    }

    public boolean isEmpty() {
        return empty.isEmpty();
    }

    public void group(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
