package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.table.dto.response.OrderTableResponse;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<OrderTable> orderTables = new ArrayList<>();

    public OrderTables() {
    }

    private OrderTables(List<OrderTable> orderTables) {
        this.orderTables.addAll(orderTables);
    }

    public static OrderTables of(List<OrderTable> orderTables){
        return new OrderTables(orderTables);
    }

    public List<Long> getOrderTableIds() {
        return this.orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public List<OrderTableResponse> getOrderTables(){
        return this.orderTables.stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    public void ungroup() {
        this.orderTables.forEach(OrderTable::ungroup);
    }
}
