package kitchenpos.domain.order;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroupId")
    private List<OrderTable> orderTables = new ArrayList<>();

    public static OrderTables of(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}