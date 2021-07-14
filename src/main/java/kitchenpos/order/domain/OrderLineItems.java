package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import kitchenpos.order.dto.OrderLineItemResponse;

@Embeddable
public class OrderLineItems {
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private final List<OrderLineItem> orderLineItems;

    public OrderLineItems() {
        this.orderLineItems = new ArrayList<>();
    }

    public void add(OrderLineItem orderLineItem) {
        this.orderLineItems.add(orderLineItem);
    }

    public List<OrderLineItemResponse> toResponses() {
        return this.orderLineItems.stream()
                .map(OrderLineItemResponse::of)
                .collect(Collectors.toList());
    }

    public int size() {
        return this.orderLineItems.size();
    }

    public boolean isExistsAllIds() {
        return this.orderLineItems.stream()
                .noneMatch(orderLineItem -> Objects.isNull(orderLineItem.getSeq()));
    }
}
