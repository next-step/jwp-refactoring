package kitchenpos.tablegroup.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.ordertable.domain.OrderTable;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public void addAllOrderTables(List<OrderTable> savedOrderTables) {
        orderTables.addAll(savedOrderTables);
    }

    public void validateOrderTableEmptyOrNonNull(List<OrderTable> savedOrderTables) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty()) {
                throw new IllegalArgumentException(
                        "비어있지 않은 주문 테이블은 단체 지정을 할 수 없습니다[orderTable" + savedOrderTable + "]");
            }
            if (Objects.nonNull(savedOrderTable.getTableGroup())) {
                throw new IllegalArgumentException("이미 단체 지정이 된 주문 테이블이 포함되어 있습니다[orderTable:" + savedOrderTable + "]");
            }
        }
    }

    public void validateOrderTablesSize() {
        if (orderTables.size() < 2) {
            throw new IllegalArgumentException("최소 2개 이상의 주문 테이블들에 대해서만 단체 지정이 가능합니다");
        }
    }

    public void validateOrderStatus(List<String> orderStatuses) {
        orderTables.forEach(orderTable -> orderTable.validateOrderStatus(orderStatuses));
    }

    public void unGroup() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.changeTableGroup(null);
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
