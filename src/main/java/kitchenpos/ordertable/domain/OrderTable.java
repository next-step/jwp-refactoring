package kitchenpos.ordertable.domain;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.order.domain.Order;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private NumberOfGuests numberOfGuests;
    private boolean empty;
    private Long tableGroupId;

    protected OrderTable() {}

    public OrderTable(NumberOfGuests numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Long id, NumberOfGuests numberOfGuests, boolean empty) {
        this(numberOfGuests, empty);
        this.id = id;
    }

    public void updateEmpty(boolean empty, List<Order> orders) {
        validateHasTableGroup();
        orders.forEach(Order::validateOrderStatusShouldComplete);

        this.empty = empty;
    }

    public void updateNumberOfGuest(NumberOfGuests numberOfGuests) {
        validateShouldNotEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    private void validateShouldNotEmpty() {
        if (isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.ORDER_TABLE_IS_EMPTY.getMessage());
        }
    }

    private void validateHasTableGroup() {
        if (tableGroupId != null) {
            throw new IllegalArgumentException(ErrorCode.ALREADY_TABLE_GROUP.getMessage());
        }
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    public boolean hasTableGroup() {
        return tableGroupId != null;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.value();
    }

    public boolean isEmpty() {
        return empty;
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
