package kitchenpos.ordertable.domain;

import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.common.domain.Empty;
import kitchenpos.common.domain.NumberOfGuests;
import kitchenpos.order.domain.Order;
import kitchenpos.tablegroup.domain.TableGroup;

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

    protected OrderTable() {
    }

    private OrderTable(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
        this.empty = Empty.from(empty);
    }

    public static OrderTable of(Long id, int numberOfGuests, boolean empty) {
        return new OrderTable(id, numberOfGuests, empty);
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(null, numberOfGuests, empty);
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    public boolean isEmpty() {
        return empty.isEmpty();
    }

    public boolean hasTableGroup() {
        return tableGroupId != null;
    }

    public boolean isNotNullTableGroup() {
        return tableGroupId != null;
    }

    public void registerTableGroup(Long tableGroupId) {
        validateHasTableGroup();
        this.tableGroupId = tableGroupId;
        this.empty = Empty.IS_NOT_EMPTY;
    }


    public void changeEmpty(boolean isEmpty, List<Order> orders) {
        validateNotCompleteOrders(orders);
        validateHasTableGroup();
        this.empty = Empty.from(isEmpty);
    }

    private void validateNotCompleteOrders(List<Order> orders) {
        orders.forEach(Order::validateNotCompleteOrder);
    }

    private void validateHasTableGroup() {
        if (isNotNullTableGroup()) {
            throw new IllegalArgumentException(ErrorCode.HAS_TABLE_GROUP.getErrorMessage());
        }
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateOrderTableNotEmpty();
        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
    }

    private void validateOrderTableNotEmpty() {
        if(empty.isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.ORDER_TABLE_NOT_EMPTY.getErrorMessage());
        }
    }

    public Long findTableGroupId() {
        if(tableGroupId == null) {
            return null;
        }
        return tableGroupId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderTable that = (OrderTable) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
