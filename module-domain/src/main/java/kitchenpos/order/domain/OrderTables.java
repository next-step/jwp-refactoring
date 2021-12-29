package kitchenpos.order.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroupId", cascade = {PERSIST, MERGE}, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables.addAll(orderTables);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
