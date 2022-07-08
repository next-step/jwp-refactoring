package kitchenpos.table.domain;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "table_group", fetch = FetchType.LAZY)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public List<Long> orderTableIds() {
        return orderTables.stream()
                            .map(OrderTable::getId)
                            .collect(Collectors.toList());
    }

    public void unGroup() {
        orderTables.forEach(OrderTable::unGroup);
    }
}
