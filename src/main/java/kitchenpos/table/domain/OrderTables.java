package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import kitchenpos.order.domain.Orders;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    public OrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    protected OrderTables() {
    }

    public List<Long> ids() {
        return orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
    }

    public void ungroup(final Orders orders) {
        validateOrderStatus(orders);

        orderTables.forEach(OrderTable::ungroup);
    }

    private void validateOrderStatus(final Orders orders) {
        orderTables.stream()
            .filter(it -> orders.isNotCompleted(it.getId()))
            .findFirst()
            .ifPresent(orderTable -> {
                throw new IllegalArgumentException("사용 중인 테이블은 그룹을 해제할 수 없습니다.");
            });
    }

    public List<OrderTable> toList() {
        return Collections.unmodifiableList(orderTables);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final OrderTables that = (OrderTables)o;
        return Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTables);
    }
}
