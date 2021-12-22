package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private final OrderRequest orderRequest = new OrderRequest(1L,
        Arrays.asList(new OrderLineItemRequest(1L, 1L)));
    private final Menu menu = Menu.of("후라이드치킨", 10000, MenuGroup.from("치킨"));
    private final OrderTable orderTable = OrderTable.of(2, false);
    private final Order order = orderRequest
        .toEntity(orderTable, Arrays.asList(OrderLineItem.of(menu, 2L)));
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("주문을 등록한다.")
    void create() {
        when(menuRepository.findById(anyLong()))
            .thenReturn(Optional.of(menu));
        when(orderTableRepository.findById(anyLong()))
            .thenReturn(Optional.of(orderTable));
        when(orderRepository.save(any(Order.class)))
            .thenReturn(order);

        OrderResponse saved = orderService.create(orderRequest);

        assertAll(
            () -> assertNotNull(saved.getOrderedTime()),
            () -> assertThat(saved.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
        );
    }

    @Test
    @DisplayName("주문 목록을 조회한다.")
    void list() {

        when(orderRepository.findAll())
            .thenReturn(Arrays.asList(order));

        List<OrderResponse> orders = orderService.list();

        assertAll(
            () -> assertThat(orders.size()).isEqualTo(1),
            () -> assertThat(orders.get(0).getOrderLineItems())
                .extracting(OrderLineItemResponse::getQuantity).containsExactly(2L)
        );
    }

    @Test
    @DisplayName("주문의 상태를 관리한다")
    void changeOrderStatus() {

        assertTrue(order.isOnGoing());

        when(orderRepository.findById(anyLong()))
            .thenReturn(Optional.of(order));

        OrderResponse changed = orderService
            .changeOrderStatus(1L, new OrderStatusRequest(OrderStatus.COMPLETION.name()));

        assertFalse(order.isOnGoing());
        assertThat(changed.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

}