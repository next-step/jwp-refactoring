package kitchenpos.order.domain;

import kitchenpos.order.dto.OrderResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Orders {
    private List<Order> value = new ArrayList<>();

    public Orders(List<Order> value) {
        this.value.addAll(value);
    }

    public List<OrderResponse> toResponse() {
        return this.value.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public List<Order> getValue() {
        return value;
    }
}
