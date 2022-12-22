package kitchenpos.table.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    public OrderTables() {

    }

    public OrderTables(List<OrderTable> orderTableList) {
        this.orderTables = orderTableList;
    }

    public void addList(List<OrderTable> orderTableList) {
        orderTableList.stream()
                .forEach(orderTables::add);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
