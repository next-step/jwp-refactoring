package kitchenpos.menu.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderGeneratedEvent;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderEventListenerTest {
    @Mock
    private MenuService menuService;

    OrderEventListener orderEventListener;

    @BeforeEach
    void setUp() {
        orderEventListener = new OrderEventListener(menuService);
    }

    @DisplayName("주문 저장시 주문항목 갯수와 메뉴의 갯수가 다르게 주어지면 예외를 던진다.")
    @Test
    void create_with_different_menu_size() {
        OrderLineItem orderLineItem = OrderLineItem.builder()
                .menuId(1L)
                .quantity(2L)
                .build();
        OrderLineItem orderLineItem2 = OrderLineItem.builder()
                .menuId(2L)
                .quantity(3L)
                .build();

        OrderTable givenOrderTable = new OrderTable(1L, 1L, 5, false);
        Order order = Order.of(1L, Arrays.asList(orderLineItem, orderLineItem2));
        OrderGeneratedEvent orderGeneratedEvent = new OrderGeneratedEvent(order);

        when(menuService.getMenuExistCount(anyList()))
                .thenReturn(1);

        assertThatThrownBy(() -> orderEventListener.generateOrder(orderGeneratedEvent))
                .isInstanceOf(MenuNotMatchException.class);
    }
}
