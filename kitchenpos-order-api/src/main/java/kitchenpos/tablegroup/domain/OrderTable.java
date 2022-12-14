package kitchenpos.tablegroup.domain;

import kitchenpos.tablegroup.exception.OrderTableExceptionCode;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;

    @Embedded
    private TableGuests numberOfGuests = new TableGuests();

    @Embedded
    private TableEmpty empty = new TableEmpty();

    protected OrderTable() {}

    public OrderTable(int numberOfGuests, boolean empty) {
        this.tableGroup = null;
        this.numberOfGuests = new TableGuests(numberOfGuests);
        this.empty = new TableEmpty(empty);
    }

    void updateTableGroup(TableGroup tableGroup) {
        if (this.tableGroup != tableGroup) {
            this.tableGroup = tableGroup;
            tableGroup.addOrderTable(this);
        }
    }

    public boolean hasTableGroup() {
        return Objects.nonNull(tableGroup);
    }

    public void changeEmpty(boolean request) {
        this.empty = new TableEmpty(request);
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = this.numberOfGuests.changeNumberOfGuests(numberOfGuests, isEmpty());
    }

    public void group() {
        checkForTableGrouping();
        changeEmpty(false);
    }

    private void checkForTableGrouping() {
        if (hasTableGroup()) {
            throw new IllegalArgumentException(OrderTableExceptionCode.ALREADY_INCLUDED_IN_ANOTHER_TABLE_GROUP.getMessage());
        }

        if (!isEmpty()) {
            throw new IllegalArgumentException(
                    OrderTableExceptionCode.NON_EMPTY_ORDER_TABLE_CANNOT_BE_INCLUDED_IN_TABLE_GROUP.getMessage());
        }
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
        return numberOfGuests.getNumberOfGuests();
    }

    public boolean isEmpty() {
        return empty.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
