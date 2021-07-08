package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderLineItemRepository orderLineItemRepository;
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문 생성 테스트")
    @Test
    void createTest() {
        // given
        List<OrderLineItemRequest> orderLineItemRequests = Arrays
            .asList(new OrderLineItemRequest(1L, 3),
                    new OrderLineItemRequest(2L, 1));
        OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 3);
        Order order = new Order(1L, OrderStatus.COOKING.name());
        OrderTable orderTable = new OrderTable(1L, 5);
        OrderRequest orderRequest = new OrderRequest(1l, orderLineItemRequests);

        Mockito.when(menuRepository.countByIdIn(any())).thenReturn(2L);
        Mockito.when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));
        Mockito.when(orderRepository.save(any())).thenReturn(order);
        Mockito.when(orderLineItemRepository.save(any())).thenReturn(orderLineItem);

        // when
        OrderResponse actual = orderService.create(orderRequest);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @DisplayName("주문할 메뉴가 비었을 경우 테스트")
    @Test
    void createTestMenuEmpty() {
        // given
        OrderRequest orderRequest = new OrderRequest(1l, Collections.emptyList());

        // when
        assertThatThrownBy(() -> orderService.create(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문할 메뉴가 없을 경우 테스트")
    @Test
    void createTestWithoutMenu() {
        // given
        List<OrderLineItemRequest> orderLineItemRequests = Arrays
            .asList(new OrderLineItemRequest(1L, 3),
                    new OrderLineItemRequest(2L, 1));
        OrderRequest orderRequest = new OrderRequest(1l, orderLineItemRequests);

        Mockito.when(menuRepository.countByIdIn(any())).thenReturn(1L);

        // when
        assertThatThrownBy(() -> orderService.create(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 대상인 테이블이 없을 경우 테스트")
    @Test
    void createTestWithoutOrderTable() {
        // given
        List<OrderLineItemRequest> orderLineItemRequests = Arrays
            .asList(new OrderLineItemRequest(1L, 3),
                    new OrderLineItemRequest(2L, 1));
        OrderRequest orderRequest = new OrderRequest(1l, orderLineItemRequests);

        Mockito.when(menuRepository.countByIdIn(any())).thenReturn(2L);
        Mockito.when(orderTableRepository.findById(1L)).thenReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> orderService.create(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 주문 테이블 리스트 조회 테스트")
    @Test
    void listTest() {
        // given
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 3);
        OrderLineItem orderLineItem2 = new OrderLineItem(1L, 2L, 1);
        Order order1 = new Order(1L, null);
        Order order2 = new Order(1L, null);

        Mockito.when(orderRepository.findAll()).thenReturn(Arrays.asList(order1, order2));
        Mockito.when(orderLineItemRepository.findAllByOrderId(any()))
               .thenReturn(Arrays.asList(orderLineItem1, orderLineItem2));

        // when
        List<OrderResponse> actual = orderService.list();

        // then
        assertThat(actual).isNotEmpty().hasSize(2);
    }

    @DisplayName("주문 상태 변경 테스트")
    @Test
    void changeOrderStatusTest() {
        // given
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 3);
        OrderLineItem orderLineItem2 = new OrderLineItem(1L, 2L, 1);
        Order order = new Order(1L, null);

        Mockito.when(orderRepository.findById(any())).thenReturn(Optional.of(order));
        Mockito.when(orderLineItemRepository.findAllByOrderId(any()))
               .thenReturn(Arrays.asList(orderLineItem1, orderLineItem2));

        // when
        OrderResponse actual = orderService.changeOrderStatus(1l, new OrderStatusRequest(OrderStatus.COOKING));

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @DisplayName("주문 대상이 없는 경우 테스트")
    @Test
    void changeOrderStatusWithoutOrderTest() {
        // given
        Mockito.when(orderRepository.findById(any())).thenReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> orderService.changeOrderStatus(1l,
                                                                new OrderStatusRequest(OrderStatus.COOKING)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문이 이미 완료가 된 경우 테스트")
    @Test
    void changeOrderStatusAlreadyCompleteTest() {
        // given
        Order order = new Order(1L, OrderStatus.COMPLETION.name());

        Mockito.when(orderRepository.findById(any())).thenReturn(Optional.of(order));

        // when
        assertThatThrownBy(() -> orderService.changeOrderStatus(1l,
                                                                new OrderStatusRequest(OrderStatus.COOKING)))
            .isInstanceOf(IllegalArgumentException.class);
    }

}
