package kitchenpos.order.application;

import java.math.BigDecimal;
import kitchenpos.common.Price;
import kitchenpos.exception.KitchenposException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
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

import static kitchenpos.exception.KitchenposExceptionMessage.ALREADY_COMPLETION_ORDER;
import static kitchenpos.exception.KitchenposExceptionMessage.NOT_FOUND_ORDER;
import static kitchenpos.exception.KitchenposExceptionMessage.NOT_FOUND_ORDER_TABLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문 생성 테스트")
    @Test
    void createTest() {
        // given
        Menu 메뉴 = Menu.Builder.of("메뉴1", Price.of(BigDecimal.valueOf(2000L)))
                              .menuGroup(1L)
                              .menuProducts(Arrays.asList(new MenuProduct(1l, 5)))
                              .build();
        List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(new OrderLineItemRequest(1L, 3),
                                                                         new OrderLineItemRequest(2L, 1));
        OrderLineItem orderLineItem = new OrderLineItem(1L, 3);
        Order order = new Order(1L, OrderStatus.COOKING, Arrays.asList(orderLineItem));
        OrderRequest orderRequest = new OrderRequest(1l, orderLineItemRequests);

        Mockito.when(orderTableRepository.findById(1L))
               .thenReturn(Optional.of(new OrderTable(3, false)));
        Mockito.when(menuRepository.findById(1L)).thenReturn(Optional.of(메뉴));
        Mockito.when(menuRepository.findById(2L)).thenReturn(Optional.of(메뉴));
        Mockito.when(orderRepository.save(any())).thenReturn(order);

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
            .isInstanceOf(KitchenposException.class)
            .hasMessageContaining(NOT_FOUND_ORDER_TABLE.getMessage());
    }

    @DisplayName("주문 대상인 테이블이 없을 경우 테스트")
    @Test
    void createTestWithoutOrderTable() {
        // given
        List<OrderLineItemRequest> orderLineItemRequests = Arrays
            .asList(new OrderLineItemRequest(1L, 3),
                    new OrderLineItemRequest(2L, 1));
        OrderRequest orderRequest = new OrderRequest(1l, orderLineItemRequests);

        Mockito.when(orderTableRepository.findById(1L)).thenReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> orderService.create(orderRequest))
            .isInstanceOf(KitchenposException.class)
            .hasMessageContaining(NOT_FOUND_ORDER_TABLE.getMessage());
    }

    @DisplayName("전체 주문 테이블 리스트 조회 테스트")
    @Test
    void listTest() {
        // given
        Order order1 = new Order(1L, null, Collections.emptyList());
        Order order2 = new Order(2L, null, Collections.emptyList());

        Mockito.when(orderRepository.findAll()).thenReturn(Arrays.asList(order1, order2));

        // when
        List<OrderResponse> actual = orderService.list();

        // then
        assertThat(actual).isNotEmpty().hasSize(2);
    }

    @DisplayName("주문 상태 변경 테스트")
    @Test
    void changeOrderStatusTest() {
        // given
        Order order = new Order(1L, null, Collections.emptyList());

        Mockito.when(orderRepository.findById(any())).thenReturn(Optional.of(order));

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
            .isInstanceOf(KitchenposException.class)
            .hasMessageContaining(NOT_FOUND_ORDER.getMessage());
    }

    @DisplayName("주문이 이미 완료가 된 경우 테스트")
    @Test
    void changeOrderStatusAlreadyCompleteTest() {
        // given
        Order order = new Order(1L, OrderStatus.COMPLETION, Collections.emptyList());

        Mockito.when(orderRepository.findById(any())).thenReturn(Optional.of(order));

        // when
        assertThatThrownBy(() -> orderService.changeOrderStatus(1l,
                                                                new OrderStatusRequest(OrderStatus.COOKING)))
            .isInstanceOf(KitchenposException.class)
            .hasMessageContaining(ALREADY_COMPLETION_ORDER.getMessage());
    }

}
