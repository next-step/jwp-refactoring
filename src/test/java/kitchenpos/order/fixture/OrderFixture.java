package kitchenpos.order.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.common.domain.OrderStatus;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequestDto;
import kitchenpos.order.dto.OrderLineItemResponseDto;
import kitchenpos.order.dto.OrderRequestDto;
import kitchenpos.order.dto.OrderResponseDto;

public class OrderFixture {

    public static Order 주문_데이터_생성(Long id, Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> requestOrderLineItems) {
        return new Order(id, orderTableId, orderStatus, requestOrderLineItems);
    }

    public static OrderRequestDto 주문_요청_데이터_생성(List<OrderLineItemRequestDto> orderLineItems) {
        return new OrderRequestDto(1L, orderLineItems);
    }

    public static OrderResponseDto 주문_응답_데이터_생성(Long id, OrderStatus status, LocalDateTime orderedAt, List<OrderLineItemResponseDto> orderLineItems) {
        return new OrderResponseDto(id, status, 1L, orderedAt, orderLineItems);
    }

}
