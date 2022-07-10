package kitchenpos.table.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderTables {
    private static final int MINIMUM_TABLE_SIZE = 2;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        this.orderTables = orderTables;
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MINIMUM_TABLE_SIZE) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
