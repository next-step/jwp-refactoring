package kitchenpos.order.dto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {

    private Long id;
    private MenuResponse menu;
    private long quantity;

    private OrderLineItemResponse() {
    }

    public OrderLineItemResponse(Long seq, MenuResponse menu, long quantity) {
        this.id = seq;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static List<OrderLineItemResponse> fromList(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
            .map(OrderLineItemResponse::from)
            .collect(Collectors.toList());
    }


    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getId(),
            MenuResponse.from(orderLineItem.getMenu()),
            orderLineItem.getQuantityVal());
    }

    public Long getId() {
        return id;
    }

    public MenuResponse getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderLineItemResponse that = (OrderLineItemResponse) o;
        return getQuantity() == that.getQuantity() && Objects.equals(getId(), that.getId())
            && Objects.equals(getMenu(), that.getMenu());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getMenu(), getQuantity());
    }
}
