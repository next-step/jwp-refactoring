package order.fixture;

import common.domain.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import order.domain.Order;
import order.domain.OrderLineItem;
import order.dto.OrderLineItemRequestDto;
import order.dto.OrderLineItemResponseDto;
import order.dto.OrderRequestDto;
import order.dto.OrderResponseDto;

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
