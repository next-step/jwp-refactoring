package kitchenpos.order.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.dto.OrderDto;
import kitchenpos.order.dto.OrderLineItemDto;
import kitchenpos.order.domain.OrdersValidator;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.MenuId;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrdersRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTableId;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrdersRepository orderRepository;

    @Mock
    private OrdersValidator ordersValidator;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문이 저장된다.")
    @Test
    void create_order() {
        // given
        OrderLineItem 치킨_주문항목 = OrderLineItem.of(MenuId.of(1L), 1L);
        Orders 치킨주문 = Orders.of(OrderTableId.of(1L), OrderStatus.COOKING);
        치킨_주문항목.acceptOrder(치킨주문);

        when(ordersValidator.getValidatedOrdersForCreate(any(OrderDto.class))).thenReturn(치킨주문);
        when(orderRepository.save(any(Orders.class))).thenReturn(치킨주문);

        OrderDto 주문_요청전문 = OrderDto.of(1L, List.of(OrderLineItemDto.of(1L, 1L)));

        // when
        OrderDto savedOrder = orderService.create(주문_요청전문);

        // then
        assertAll(
            () -> Assertions.assertThat(savedOrder.getOrderLineItems().size()).isEqualTo(1),
            () -> Assertions.assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
        );
    }

    @DisplayName("주문이 조회된다.")
    @Test
    void search_order() {
        // given
        OrderLineItem 치킨_주문항목 = OrderLineItem.of(MenuId.of(1L), 1L);
        Orders 치킨주문 = Orders.of(OrderTableId.of(1L), OrderStatus.MEAL);
        치킨_주문항목.acceptOrder(치킨주문);

        when(orderRepository.findAll()).thenReturn(List.of(치킨주문));

        // when
        List<OrderDto> orders = orderService.list();

        // then
        Assertions.assertThat(orders).isEqualTo(List.of(OrderDto.of(치킨주문)));
    }

    @DisplayName("주문의 상태가 변경된다.")
    @Test
    void update_orderStatus() {
        // given
        OrderLineItem 치킨_주문항목 = OrderLineItem.of(MenuId.of(1L), 1L);
        Orders 치킨주문 = Orders.of(OrderTableId.of(1L), OrderStatus.COOKING);
        치킨_주문항목.acceptOrder(치킨주문);

        when(ordersValidator.getValidatedOrdersForChangeOrderStatus(치킨주문.getId())).thenReturn(치킨주문);
        
        OrderDto 상태변경_요청전문 = OrderDto.of(OrderStatus.MEAL.name());

        // when
        OrderDto chagedOrder = orderService.changeOrderStatus(치킨주문.getId(), 상태변경_요청전문);

        // then
        assertAll(
            () -> Assertions.assertThat(chagedOrder).isEqualTo(OrderDto.of(치킨주문)),
            () -> Assertions.assertThat(chagedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name())
        );
    }
}
