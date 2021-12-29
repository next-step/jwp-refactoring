package kitchenpos.order.dto;

import kitchenpos.order.domain.MenuAdapter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class OrderRequest {

    @Positive
    @NotNull
    private Long orderTableId;

    @Size(min = 1)
    private List<OrderLineItemRequest> orderLineItems = new ArrayList<>();

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public List<Long> getMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(toList());
    }

    public OrderLineItemRequest find(MenuAdapter menuAdapter) {
        return this.orderLineItems.stream()
                .filter(o -> o.match(menuAdapter))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다."));
    }
}
