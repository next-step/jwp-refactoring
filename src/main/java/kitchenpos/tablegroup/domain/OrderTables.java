package kitchenpos.tablegroup.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableResponse;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    public OrderTables() {
        this.orderTables = new ArrayList<>();
    }

    public List<OrderTableResponse> toResponses() {
        return this.orderTables.stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    public void add(OrderTable orderTable) {
        this.orderTables.add(orderTable);
    }
}
