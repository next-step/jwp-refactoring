package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.product.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("주문 관련")
@SpringBootTest
class OrderServiceTest {
    @Autowired
    OrderService orderService;
    @MockBean
    OrderRepository orderRepository;
    @MockBean
    OrderValidator orderValidator;

    @DisplayName("주문을 생성할 수 있다")
    @Test
    void create() {
        // given
        OrderRequest given = new OrderRequest(1L);
        given.setOrderLineItems(singletonList(new OrderLineItemRequest(1L, 1)));
        List<OrderMenu> orderMenus = singletonList(new OrderMenu(1L, "메뉴", Price.valueOf(10000)));
        when(orderValidator.checkItems(given)).thenReturn(orderMenus);
        when(orderRepository.save(any(Order.class))).thenReturn(given.toEntity(orderMenus));

        // when
        OrderResponse actual = orderService.create(given);

        // then
        verify(orderValidator).checkOrderTable(given);
        assertThat(actual.getOrderTableId()).isEqualTo(given.getOrderTableId());
    }

    @DisplayName("주문의 목록을 조회할 수 있다")
    @Test
    void list() {
        // when
        orderService.list();

        // then
        verify(orderRepository).findAllWithItem();
    }

    @DisplayName("변경하려는 주문은 존재해야 한다")
    @Test
    void orderIsExists() {
        // given
        Long notExistsOrderId = 1000L;

        // when then
        assertThatThrownBy(() -> orderService.changeOrderStatus(notExistsOrderId, OrderStatus.MEAL))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
