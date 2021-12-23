package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * packageName : kitchenpos.dto
 * fileName : OrderLineItemResponse
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
//FIXME 생성자 제한하기
public class OrderLineItemResponse {
    private Long id;
    private Long orderId;
    private Long menuId;
    private Long quantity;

    public OrderLineItemResponse() {
    }

    public OrderLineItemResponse(Long id, Long orderId, Long menuId, Long quantity) {
        this.id = id;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getId(), orderLineItem.getOrder().getId(), orderLineItem.getMenu().getId(), orderLineItem.getQuantity().value());
    }

    public static List<OrderLineItemResponse> ofList(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItemResponse::of)
                .collect(toList());
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
