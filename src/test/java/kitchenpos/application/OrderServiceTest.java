package kitchenpos.application;

import kitchenpos.dao.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.exception.NoOrderLineItemException;
import kitchenpos.exception.NoOrderTableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        OrderRequest orderRequest = new OrderRequest(1L, OrderStatus.COOKING, null);

        // when
        OrderService orderService = new OrderService(orderRepository, tableService, menuService);

        // then
        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(NoOrderLineItemException.class);
    }

    @DisplayName("주문 테이블 없이 주문을 생성한다")
    @Test
    void creteWithoutOrderTableTest() {
        OrderRequest orderRequest = new OrderRequest(null, OrderStatus.COOKING, Collections.singletonList(new OrderLineItemRequest(1L, 1L)));

        // when
        OrderService orderService = new OrderService(orderRepository, tableService, menuService);

        // then
        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(NoOrderTableException.class);
    }
}
