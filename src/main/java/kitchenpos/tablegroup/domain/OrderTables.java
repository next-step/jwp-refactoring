package kitchenpos.tablegroup.domain;

import kitchenpos.ordertable.domain.OrderTable;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class OrderTables {

    public static final String MESSAGE_VALIDATE_CHANGABLE = "주문 테이블이 변경 가능해야 합니다.";

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.PERSIST)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void ungroup() {
        orderTables.forEach(this::validateChangable);
        orderTables.forEach(OrderTable::ungroup);
    }

    void add(OrderTable orderTable) {
        orderTables.add(orderTable);
    }

    private void validateChangable(OrderTable orderTable) {
        if (!orderTable.isChangable()) {
            throw new IllegalArgumentException(MESSAGE_VALIDATE_CHANGABLE);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTables that = (OrderTables) o;
        return Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTables);
    }
}
