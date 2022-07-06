package kitchenpos.table.domain;

import kitchenpos.table.exception.TableException;
import kitchenpos.table.exception.TableExceptionType;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "table_group_id")
    private Long tableGroupId;
    @Column(name = "number_of_guests")
    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(final int numberOfGuests, final boolean empty) {
        return new OrderTable(null, null, numberOfGuests, empty);
    }

    public void updateTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void updateEmptyTable(final OrderTable updateTable, final boolean enabled) {
        emptyValidate(enabled);
        this.empty = updateTable.isEmpty();
    }

    private void emptyValidate(final boolean enabled) {
        enabledOrderCancelStatus(enabled);

        if (Objects.nonNull(tableGroupId)) {
            throw new TableException(TableExceptionType.EXIST_TABLE_GROUP);
        }
    }

    private void enabledOrderCancelStatus(final boolean enabled) {
        if (!enabled) {
            throw new TableException(TableExceptionType.IMPOSSIBLE_ORDER_STATUS);
        }
    }

    public void changeNumberOfGuest(final OrderTable updateTable) {
        changeNumberValidate();
        this.numberOfGuests = updateTable.numberOfGuests;
    }

    private void changeNumberValidate() {
        if (empty) {
            throw new TableException(TableExceptionType.TABLE_EMPTY);
        }

        if(numberOfGuests < 0) {
            throw  new TableException(TableExceptionType.NUMBER_OF_GUESTS_ERROR);
        }
    }

    public void updateEmpty(final boolean empty) {
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

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public String toString() {
        return "OrderTable{" +
                "id=" + id +
                ", tableGroupId=" + tableGroupId +
                ", numberOfGuests=" + numberOfGuests +
                ", empty=" + empty +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderTable that = (OrderTable) o;
        return numberOfGuests == that.numberOfGuests && empty == that.empty && Objects.equals(id, that.id) && Objects.equals(tableGroupId, that.tableGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroupId, numberOfGuests, empty);
    }

}
