package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.ChangeOrderStatusRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.orderTable.application.OrderTableService;
import kitchenpos.orderTable.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.menu.domain.MenuTest.메뉴_생성;
import static kitchenpos.order.domain.OrderLineItemTest.주문_항목_생성;
import static kitchenpos.order.domain.OrderTest.주문_생성;
import static kitchenpos.orderTable.domain.OrderTableTest.주문_태이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableService orderTableService;
    @InjectMocks
    private OrderService orderService;

    private OrderTable orderTable;
    private Menu menu1;
    private Menu menu2;
    private OrderLineItem orderLineItem1;
    private OrderLineItem orderLineItem2;
    private List<OrderLineItem> orderLineItems;

    @BeforeEach
    void setup() {
        orderTable = 주문_태이블_생성(1L, null, 1, false);
        menu1 = 메뉴_생성(1L, "menu", 1_000, 1L, null);
        menu2 = 메뉴_생성(2L, "menu", 1_000, 1L, null);
        orderLineItem1 = 주문_항목_생성(null, menu1, 1);
        orderLineItem2 = 주문_항목_생성(null, menu2, 1);
        orderLineItems = Arrays.asList(orderLineItem1, orderLineItem2);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrder() {
        // given
        OrderTable orderTable = 주문_태이블_생성(1L, null, 1, false);
        OrderRequest.MenuInfo menuInfo1 = new OrderRequest.MenuInfo(menu1.getId(), 1);
        OrderRequest.MenuInfo menuInfo2 = new OrderRequest.MenuInfo(menu1.getId(), 1);
        OrderRequest request = new OrderRequest(orderTable.getId(), Arrays.asList(menuInfo1, menuInfo2));
        Order order = 주문_생성(orderTable.getId(), OrderStatus.COOKING, orderLineItems);

        when(menuRepository.findAllByIdIn(any())).thenReturn(Arrays.asList(menu1, menu2));
        when(orderTableService.findByIdAndAndEmpty(any(), anyBoolean())).thenReturn(orderTable);
        when(orderRepository.save(any())).thenReturn(주문_생성(1L, orderTable.getId(), OrderStatus.COOKING, orderLineItems));

        // when
        OrderResponse orderResponse = orderService.create(request);

        // then
        assertAll(
                () -> assertThat(orderResponse.getOrderId()).isNotNull(),
                () -> assertThat(orderResponse.getOrderTableId()).isEqualTo(order.getOrderTableId()),
                () -> assertThat(orderResponse.getOrderStatus()).isEqualTo(order.getOrderStatus()),
                () -> assertThat(orderResponse.getMenuIds()).hasSize(order.getOrderLineItems().size())
        );

    }

    @DisplayName("주문항목이 1개 이상이여야 한다.")
    @Test
    void createOrder1() {
        // given
        OrderRequest request = new OrderRequest(orderTable.getId(), Collections.emptyList());

        // when, then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문항목과 메뉴의 갯수가 일치해야한다.")
    @Test
    void createOrder2() {
        // given
        OrderTable orderTable = 주문_태이블_생성(1L, null, 1, false);
        OrderRequest.MenuInfo menuInfo1 = new OrderRequest.MenuInfo(menu1.getId(), 1);
        OrderRequest.MenuInfo menuInfo2 = new OrderRequest.MenuInfo(menu1.getId(), 1);
        OrderRequest request = new OrderRequest(orderTable.getId(), Arrays.asList(menuInfo1, menuInfo2));

        when(menuRepository.findAllByIdIn(any())).thenReturn(Collections.singletonList(menu1));

        // when, then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재해야한다.")
    @Test
    void createOrder3() {
        // given
        OrderTable orderTable = 주문_태이블_생성(1L, null, 1, false);
        OrderRequest.MenuInfo menuInfo1 = new OrderRequest.MenuInfo(menu1.getId(), 1);
        OrderRequest.MenuInfo menuInfo2 = new OrderRequest.MenuInfo(menu1.getId(), 1);
        OrderRequest request = new OrderRequest(orderTable.getId(), Arrays.asList(menuInfo1, menuInfo2));

        when(menuRepository.findAllByIdIn(any())).thenReturn(Arrays.asList(menu1, menu2));
        when(orderTableService.findByIdAndAndEmpty(any(), anyBoolean())).thenThrow(IllegalArgumentException.class);

        // when, then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비여있으면 안된다.")
    @Test
    void createOrder4() {
        // given
        OrderTable orderTable = 주문_태이블_생성(1L, null, 1, false);
        OrderRequest.MenuInfo menuInfo1 = new OrderRequest.MenuInfo(menu1.getId(), 1);
        OrderRequest.MenuInfo menuInfo2 = new OrderRequest.MenuInfo(menu1.getId(), 1);
        OrderRequest request = new OrderRequest(orderTable.getId(), Arrays.asList(menuInfo1, menuInfo2));

        when(menuRepository.findAllByIdIn(any())).thenReturn(Arrays.asList(menu1, menu2));
        when(orderTableService.findByIdAndAndEmpty(any(), anyBoolean())).thenThrow(IllegalArgumentException.class);

        // when, then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        // given
        Order order = 주문_생성(orderTable.getId(), OrderStatus.COOKING, orderLineItems);

        when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));

        // when
        List<OrderResponse> list = orderService.list();

        // then
        assertThat(list).hasSize(1);
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        ChangeOrderStatusRequest request = new ChangeOrderStatusRequest(OrderStatus.MEAL);
        Order order = 주문_생성(orderTable.getId(), OrderStatus.COOKING, orderLineItems);

        when(orderRepository.findById(any())).thenReturn(Optional.of(order));

        // when, then
        orderService.changeOrderStatus(1L, request);
    }

    @DisplayName("주문의 현재 상태가 '조리' 또는 '식사' 이여만 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus2() {
        // given
        ChangeOrderStatusRequest request = new ChangeOrderStatusRequest(OrderStatus.MEAL);
        Order order = 주문_생성(orderTable.getId(), OrderStatus.COMPLETION, orderLineItems);

        when(orderRepository.findById(any())).thenReturn(Optional.of(order));

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
