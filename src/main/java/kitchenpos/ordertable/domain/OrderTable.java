package kitchenpos.ordertable.domain;

import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.error.ErrorEnum;
import kitchenpos.order.domain.Order;
import kitchenpos.tablegroup.domain.TableGroup;
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

    public void updateNumberOfGuest(NumberOfGuests numberOfGuests) {
        validateShouldNotEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    private void validateShouldNotEmpty() {
        if (isEmpty()) {
            throw new IllegalArgumentException(ErrorEnum.ORDER_TABLE_IS_EMPTY.message());
        }
    }

    private void validateHasTableGroup() {
        if (tableGroup != null) {
            throw new IllegalArgumentException(ErrorEnum.ALREADY_GROUP.message());
        }
    }

    public void updateEmpty(boolean empty, List<Order> orders) {
        validateHasTableGroup();
        orders.forEach(Order::validateOrderStatusShouldComplete);
        this.empty = empty;
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

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void ungroup() {
        this.tableGroup = null;
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
