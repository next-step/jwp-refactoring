package kitchenpos.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.exception.NoOrderLineItemException;
import kitchenpos.table.exception.NoOrderTableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private TableService tableService;

    @Mock
    private MenuService menuService;

    @DisplayName("주문항목 없이 주문을 생성한다")
    @Test
    void creteWithoutOrderLineItemTest() {
        OrderRequest noOrderLineItemOrderRequest
                = new OrderRequest(1L, OrderStatus.COOKING, null);

        // when
        OrderService orderService = new OrderService(orderRepository, tableService, menuService);
        when(tableService.findById(1L)).thenReturn(new OrderTable());

        // then
        assertThatThrownBy(() -> orderService.create(noOrderLineItemOrderRequest))
                .isInstanceOf(NoOrderLineItemException.class);
    }

    @DisplayName("주문 테이블 없이 주문을 생성한다")
    @Test
    void creteWithoutOrderTableTest() {
        OrderRequest noOrderTableOrderRequest = new OrderRequest(null, OrderStatus.COOKING,
                Collections.singletonList(new OrderLineItemRequest(1L, 1L)));

        // when
        OrderService orderService = new OrderService(orderRepository, tableService, menuService);

        // then
        assertThatThrownBy(() -> orderService.create(noOrderTableOrderRequest))
                .isInstanceOf(NoOrderTableException.class);
    }
}
