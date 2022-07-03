package kitchenpos.table.domain;

import kitchenpos.table.dto.OrderTableResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class OrderTables {
    private List<OrderTable> value = new ArrayList<>();

    public OrderTables(Collection<OrderTable> orderTables) {
        this.value.addAll(orderTables);
    }

    public List<OrderTableResponse> toResponse() {
        return this.value.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    public List<OrderTable> getValue() {
        return value;
    }
}
