package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.common.domain.MustHaveName;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("주문 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private final OrderRequest orderRequest = new OrderRequest(1L,
        Arrays.asList(new OrderLineItemRequest(1L, 1L)));
    private final OrderLineItem orderLineItem = OrderLineItem.of(OrderMenu.of(1L,
        MustHaveName.valueOf("후라이드치킨"),
        Price.fromInteger(10000)), 2L);
    private final Order order = orderRequest.toEntity( Arrays.asList(orderLineItem));
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderValidator orderValidator;
    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("주문을 등록한다.")
    void create() {
        Menu menu = Menu.of("후라이드치킨", 10000, MenuGroup.from("치킨"));
        when(menuRepository.findById(anyLong()))
            .thenReturn(Optional.of(menu));
        when(orderRepository.save(any(Order.class)))
            .thenReturn(order);

        ReflectionTestUtils.setField(menu, "id", 1L);

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