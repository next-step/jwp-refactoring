package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderStatusRequest;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.OrderLineItemV2;
import kitchenpos.order.domain.OrderStatusV2;
import kitchenpos.order.domain.OrdersV2;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTableV2;
import kitchenpos.table.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    private OrderService orderService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderLineItemRepository orderLineItemRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuRepository, orderRepository, orderLineItemRepository, orderTableRepository);
    }

    @Test
    @DisplayName("주문을 생성한다.")
    void createOrder() {
        // given
        final OrdersV2 order = new OrdersV2(null, 1L, OrderStatusV2.COOKING, null, null);
        final OrderLineItemV2 orderLineItemV2 = new OrderLineItemV2(1L, order, 1L, 1L);
        when(menuRepository.countByIdIn(any())).thenReturn(1L);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(new OrderTableV2(1L, null, 3, false)));
        when(orderRepository.save(any())).thenReturn(order);
        when(orderLineItemRepository.save(any())).thenReturn(orderLineItemV2);
        // when
        final OrderResponse actual = orderService.create(new OrderRequest(1L, Arrays.asList(1L)));
        // then
        assertAll(
                () -> assertThat(actual.getOrderLineItems()).hasSize(1),
                () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(actual.getOrderTable().getId()).isEqualTo(1L)
        );
    }

    @Test
    @DisplayName("주문 할 메뉴가 없으면 에러 발생")
    void invalidZeroOfOrderLineItem() {
        // given
        final OrderRequest invalidOrderRequest = new OrderRequest(1L, Collections.emptyList());

        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(invalidOrderRequest));
    }

    @Test
    @DisplayName("등록되지 않은 메뉴 주문시 에러 발생")
    void orderNotExistMenu() {
        // given
        final OrderRequest notExistMenuOrderRequest = new OrderRequest(1L, Arrays.asList(1L));

        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(notExistMenuOrderRequest));
    }

    @Test
    @DisplayName("주문 테이블이 존재하지 않으면 에러 발생")
    void notExistOrderTable() {
        // given
        when(menuRepository.countByIdIn(any())).thenReturn(1L);
        when(orderTableRepository.findById(any())).thenReturn(Optional.empty());

        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(new OrderRequest(1L, Arrays.asList(1L))));
    }

    @Test
    @DisplayName("주문 내역을 조회할 수 있다.")
    void searchOrders() {
        // given
        final OrdersV2 order = new OrdersV2(1L, 1L, OrderStatusV2.COOKING, LocalDateTime.now(), null);
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order));
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(new OrderTableV2(1L, null, 3, false)));
        // when
        final List<OrderResponse> actual = orderService.list();
        // then
        assertThat(actual).hasSize(1);
    }

    @Test
    @DisplayName("주문의 상태를 변경할 수 있다.")
    void changeOrderStatus() {
        // given
        final OrdersV2 order = new OrdersV2(1L, 1L, OrderStatusV2.COOKING, LocalDateTime.now(), null);
        when(orderRepository.findById(any())).thenReturn(Optional.of(order));
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(new OrderTableV2(1L, null, 3, false)));
        // when
        final OrderResponse actual = orderService.changeOrderStatus(1L, new OrderStatusRequest(OrderStatusV2.COOKING));
        // then
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    @DisplayName("주문 내역이 존재하지 않은 주문 상태 변경시 에러 발생")
    void changeOrderStatusNotExistOrder() {
        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(1L, new OrderStatusRequest(OrderStatusV2.COOKING)));
    }

    @Test
    @DisplayName("완료된 주문을 주문 상태 변경시 에러 발생")
    void changeOrderStatusCompletionOrder() {
        // given
        final OrdersV2 order = new OrdersV2(1L, 1L, OrderStatusV2.COMPLETION, LocalDateTime.now(), null);
        when(orderRepository.findById(any())).thenReturn(Optional.of(order));

        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(1L, new OrderStatusRequest(OrderStatusV2.COOKING)));
    }
}
