package kitchenpos.unit;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuPrice;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.type.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.port.OrderTablePort;
import kitchenpos.order.validator.OrderValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {

    @Mock
    private OrderTablePort orderTablePort;

    @InjectMocks
    private OrderValidator orderValidator;

    @Test
    @DisplayName("주문 항목이 비어 있을 수 없다.")
    void createOrderLineItemEmpty() {
        Menu menu = new Menu("스테이크_파스타_빅세트", new MenuPrice(BigDecimal.valueOf(45_000)), 1L, new MenuProducts(Arrays.asList()));

        given(orderTablePort.findById(any())).willReturn(any());

        assertThatThrownBy(() ->
                orderValidator.validOrder(new OrderRequest(1L, null), Arrays.asList(menu))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문항목과 메뉴의 크기는 같아야한다.")
    void validCheckMenuSize() {
        Menu menu = new Menu("스테이크_파스타_빅세트", new MenuPrice(BigDecimal.valueOf(45_000)), 1L, new MenuProducts(Arrays.asList()));
        List<OrderLineItemRequest> requests = Arrays.asList(new OrderLineItemRequest(1L, 1L), new OrderLineItemRequest(2L, 1L));
        given(orderTablePort.findById(any())).willReturn(any());

        assertThatThrownBy(() ->
                orderValidator.validOrder(new OrderRequest(1L, requests), Arrays.asList(menu))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문의 상태가 completion이면 오류가 발생한다")
    void validChangeOrderStatus() {
        Order 주문 = new Order(1L, 1L, OrderStatus.COMPLETION, null);

        assertThatThrownBy(() ->
                orderValidator.validChangeOrderStatus(주문)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}