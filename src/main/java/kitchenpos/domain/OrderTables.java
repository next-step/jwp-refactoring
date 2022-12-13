package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    public OrderTables() {
    }

    public void addAllOrderTables(List<OrderTable> savedOrderTables) {
        orderTables.addAll(savedOrderTables);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void validateOrderTableEmptyOrNonNull(List<OrderTable> savedOrderTables) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
                throw new IllegalArgumentException();
            }
        }
    }

    public void validateOrderTablesSize() {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public void validateOrderStatus(List<String> orderStatuses) {
        orderTables.forEach(orderTable -> orderTable.findByInOrderStatus(orderStatuses));
    }

    public void unGroup() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.changeTableGroup(null);
        }
    }
}
