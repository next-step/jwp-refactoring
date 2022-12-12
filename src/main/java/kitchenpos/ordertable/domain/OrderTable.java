package kitchenpos.ordertable.domain;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.order.domain.Order;
import kitchenpos.tablegroup.domain.TableGroup;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    protected OrderTable() {}

    public OrderTable(Long id, NumberOfGuests numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(NumberOfGuests numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
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
        if (tableGroup != null) {
            throw new IllegalArgumentException(ErrorCode.ALREADY_TABLE_GROUP.getMessage());
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

    public void setTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.value();
    }

    public boolean isEmpty() {
        return empty;
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
