package kitchenpos.ordertable.domain;

import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Embedded
    private Empty empty;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
        this.empty = Empty.from(empty);
    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
        this.empty = Empty.from(empty);
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
        this.empty = Empty.from(empty);
    }

    public void ungroup() {
        this.tableGroup = null;
    }

    public boolean isEmpty() {
        return empty.isEmpty();
    }

    public boolean isNotNullTableGroup() {
        return tableGroup != null;
    }

    public void updateTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = Empty.IS_NOT_EMPTY;
    }

    public void changeEmpty(boolean isEmpty, List<Order> orders) {
        validateHasTableGroup();
        orders.forEach(Order::validateNotCompleteOrder);
        this.empty = Empty.from(isEmpty);
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
        if(tableGroup == null) {
            return null;
        }
        return tableGroup.getId();
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
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
