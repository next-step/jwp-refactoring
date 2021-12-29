package kitchenpos.table.dto;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import kitchenpos.table.domain.OrderTable;

public class OrderTableResponses {
    private final List<OrderTableResponse> orderTableResponses;

    private OrderTableResponses(List<OrderTableResponse> orderTableResponses) {
        this.orderTableResponses = orderTableResponses;
    }

    public static OrderTableResponses from(List<OrderTable> orderTables) {
        return new OrderTableResponses(orderTables.stream()
            .map(OrderTableResponse::from)
            .collect(Collectors.toList()));
    }

    public List<OrderTableResponse> getOrderTableResponses() {
        return Collections.unmodifiableList(orderTableResponses);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof OrderTableResponses))
            return false;
        OrderTableResponses that = (OrderTableResponses)o;
        return Objects.equals(orderTableResponses, that.orderTableResponses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTableResponses);
    }
}
