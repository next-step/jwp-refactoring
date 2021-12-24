package kitchenpos.order.dto;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OrderRequest {

    private Long orderTableId;
    private List<OrderLineRequest> orderLineItems;

    public OrderRequest(Long orderTableId, List<OrderLineRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public OrderRequest() {
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public int getOrderItemSize() {
        return orderLineItems.size();
    }

    public <R> List<R> convert(Function<OrderLineRequest, R> mapper) {
        return orderLineItems.stream()
            .map(mapper)
            .collect(Collectors.toList());
    }
}
