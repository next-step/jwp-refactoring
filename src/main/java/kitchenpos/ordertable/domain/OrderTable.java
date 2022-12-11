package kitchenpos.ordertable.domain;

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

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
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
            throw new IllegalArgumentException("주문 테이블이 빈 상태입니다.");
        }
    }

    private void validateHasTableGroup() {
        if (tableGroup != null) {
            throw new IllegalArgumentException("이미 단체그룹으로 지정되어 있습니다.");
        }
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
