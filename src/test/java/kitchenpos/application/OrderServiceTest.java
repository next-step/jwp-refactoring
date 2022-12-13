package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private OrderService orderService;

    @DisplayName("빈 주문항목목록으로 주문을 등록할 수 없다")
    @Test
    void order1() {
        assertThatThrownBy(() -> orderService.create(new Order(null, Collections.emptyList())))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 메뉴만 주문 등록할 수 있다")
    @Test
    void order2() {
        List<OrderLineItem> orderLineItems = Collections.singletonList(new OrderLineItem(1L, 10L));
        when(menuRepository.countByIdIn(any())).thenReturn(0L);

        assertThatThrownBy(() -> orderService.create(new Order(null, orderLineItems)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 주문테이블만 주문 등록할 수 있다")
    @Test
    void order3() {
        List<OrderLineItem> orderLineItems = Collections.singletonList(new OrderLineItem(1L, 10L));
        when(menuRepository.countByIdIn(any())).thenReturn(Long.valueOf(orderLineItems.size()));
        when(orderTableRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(new Order(1L, orderLineItems)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 주문테이블은 주문 등록할 수 없다")
    @Test
    void order4() {
        List<OrderLineItem> orderLineItems = Collections.singletonList(new OrderLineItem(1L, 10L));
        when(menuRepository.countByIdIn(any())).thenReturn(Long.valueOf(orderLineItems.size()));
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(new OrderTable(1L, 0, true)));

        assertThatThrownBy(() -> orderService.create(new Order(1L, orderLineItems)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 주문만 상태변경할 수 있다")
    @Test
    void order5() {
        when(orderRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new Order()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문상태가 완료인 경우 상태변경할 수 없다")
    @Test
    void order6() {
        when(orderRepository.findById(any())).thenReturn(Optional.of(new Order(OrderStatus.COMPLETION.name())));

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new Order()))
            .isInstanceOf(IllegalArgumentException.class);
    }

}
