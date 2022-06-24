package kitchenpos.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequestDto;
import kitchenpos.dto.OrderLineItemResponseDto;
import kitchenpos.dto.OrderRequestDto;
import kitchenpos.dto.OrderResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderFixture {

    public static Order 주문_데이터_생성(Long id, OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> requestOrderLineItems) {
        return new Order(id, orderTable, orderStatus, requestOrderLineItems);
    }

    public static OrderRequestDto 주문_요청_데이터_생성(List<OrderLineItemRequestDto> orderLineItems) {
        return new OrderRequestDto(1L, orderLineItems);
    }

    public static OrderResponseDto 주문_응답_데이터_생성(Long id, OrderStatus status, LocalDateTime orderedAt, List<OrderLineItemResponseDto> orderLineItems) {
        return new OrderResponseDto(id, status, 1L, orderedAt, orderLineItems);
    }

}
