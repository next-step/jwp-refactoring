package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderTables {

    private static final int TABLE_MIN_SIZE = 2;

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    private OrderTables(List<OrderTable> orderTables) {
        validateSize(orderTables);
        validateEmpty(orderTables);
        validateTableGroup(orderTables);
        this.orderTables = orderTables;
    }

    public static OrderTables from(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    public void group(TableGroup tableGroup) {
        orderTables.forEach(orderTable -> orderTable.group(tableGroup));
    }

    public List<OrderTable> getOrderTables() {
        return Collections.unmodifiableList(orderTables);
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
    }

    private void validateSize(List<OrderTable> orderTables) {
        if (orderTables.size() < TABLE_MIN_SIZE) {
            throw new IllegalArgumentException("단체 지정 시 주문 테이블은 두 테이블 이상이어야 합니다.");
        }
    }

    private void validateEmpty(List<OrderTable> orderTables) {
        boolean notEmpty =  orderTables.stream()
                .anyMatch(orderTable -> !orderTable.isEmpty());

        if (notEmpty) {
            throw new IllegalArgumentException("단체 지정 시 주문 테이블은 빈 테이블 이어야 합니다.");
        }
    }

    private void validateTableGroup(List<OrderTable> orderTables) {
        boolean isGrouped = orderTables.stream()
                .anyMatch(OrderTable::isGrouped);
        if (isGrouped) {
            throw new IllegalArgumentException("이미 주문 테이블이 단체 지정이 되어있으므로 지정할 수 없습니다.");
        }
    }
}
