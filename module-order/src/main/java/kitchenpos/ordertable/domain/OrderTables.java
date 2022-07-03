package kitchenpos.ordertable.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderTable> orderTables;

    protected OrderTables() {
    }

    private OrderTables(List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        this.orderTables = orderTables;
    }

    public static OrderTables from(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    private static void validateOrderTables(List<OrderTable> orderTables) {
        if (Objects.isNull(orderTables)) {
            throw new IllegalArgumentException("주문 테이블들이 필요합니다.");
        }
    }

    public List<OrderTable> readOnlyOrderTables() {
        return Collections.unmodifiableList(this.orderTables);
    }

    public void reserve(Long tableGroupId) {
        orderTables.forEach(orderTable -> orderTable.reserve(tableGroupId));
    }

    public boolean isNotEqualSize(int size) {
        return orderTables.size() != size;
    }
}
