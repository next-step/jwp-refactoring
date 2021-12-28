package kitchenpos.table.domain;

import kitchenpos.common.exception.IsEmptyTableException;
import kitchenpos.common.exception.IsNotNullTableGroupException;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "order_table")
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Embedded
    private EmptyStatus empty;

    protected OrderTable() {
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this(tableGroupId, numberOfGuests, empty);
        this.id = id;
    }

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this(numberOfGuests, empty);
        this.tableGroupId = tableGroupId;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = new EmptyStatus(empty);
    }

    public static OrderTable ofEmptyTable() {
        return new OrderTable(0, true);
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumber();
    }

    public void changeNumberOfGuests(final NumberOfGuests numberOfGuests) {
        checkEmptyOrderTable();
        this.numberOfGuests = numberOfGuests;
    }

    private void checkEmptyOrderTable() {
        if (isEmpty()) {
            throw new IsEmptyTableException();
        }
    }

    public boolean isEmpty() {
        return empty.getStatus();
    }

    public void changeEmpty(final boolean empty) {
        checkTableGroupIsNull();
        this.empty = new EmptyStatus(empty);
    }

    private void checkTableGroupIsNull() {
        if (isNotNullTableGroup()) {
            throw new IsNotNullTableGroupException();
        }
    }

    public boolean isNotNullTableGroup() {
        return !Objects.isNull(tableGroupId);
    }

    public void initTableGroup(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        this.empty = EmptyStatus.ofFalse();
    }

    public void unTableGroup() {
        tableGroupId = null;
    }

    public boolean isNotEmptyOrNonNullTableGroup() {
        return !isEmpty() || Objects.nonNull(tableGroupId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.id)
                && Objects.equals(tableGroupId, that.tableGroupId)
                && Objects.equals(numberOfGuests, that.numberOfGuests)
                && Objects.equals(empty, that.empty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroupId, numberOfGuests, empty);
    }
}
