package kitchenpos.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    MenuRepository menuRepository;

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderLineItemRepository orderLineItemRepository;

    @Mock
    OrderTableRepository orderTableRepository;

    @InjectMocks
    OrderService orderService;

    @DisplayName("주문을 신청한다.")
    @Test
    void create1() {
        //given
        OrderRequest newOrder = new OrderRequest();
        List<OrderLineItemRequest> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItemRequest(1L, 1));
        orderLineItems.add(new OrderLineItemRequest(2L, 1));
        newOrder.setOrderLineItems(orderLineItems);

        given(orderRepository.save(any()))
                .willReturn(new Order());
        given(menuRepository.countByIdIn(any()))
                .willReturn(2L);
        given(orderTableRepository.findById(any()))
                .willReturn(Optional.of(new OrderTable()));
        given(orderLineItemRepository.save(any()))
                .willReturn(new OrderLineItem());
        given(orderRepository.save(any()))
                .willReturn(new Order(1L, OrderStatus.MEAL, LocalDateTime.now()));

        //when

        OrderResponse createOrder = orderService.create(newOrder);

        //then
        assertThat(createOrder.getId()).isEqualTo(1L);
        assertThat(createOrder.getOrderTableId()).isEqualTo(2L);
        assertThat(createOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }


    @DisplayName("주문을 신청한다 - 주문 항목에 아무것도 없으면 주문할 수 없다.")
    @Test
    void create2() {
        //given
        OrderRequest newOrder = new OrderRequest();
        newOrder.setOrderLineItems(Collections.EMPTY_LIST);
        //when
        //then
        assertThatThrownBy(() -> orderService.create(newOrder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목이 비어있습니다.");
    }

    @DisplayName("주문을 신청한다 - 주문 항목에 등록하지 않은 메뉴가 있다면 주문할 수 없다.")
    @Test
    void create3() {
        //given
        given(menuRepository.countByIdIn(any())).willReturn(1L);

        List<OrderLineItemRequest> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItemRequest(1L, 3));
        orderLineItems.add(new OrderLineItemRequest(2L, 3));

        OrderRequest newOrder = new OrderRequest();
        newOrder.setOrderLineItems(orderLineItems);

        //when
        //then
        assertThatThrownBy(() -> orderService.create(newOrder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목에 등록하지 않은 메뉴가 있습니다.");
    }

    @DisplayName("주문을 신청한다 - 주문 테이블이 없거니 비어있으면 주문할 수 없다.")
    @Test
    void create4() {
        //given
        given(menuRepository.countByIdIn(any())).willReturn(2L);
        given(orderTableRepository.findById(any()))
                .willReturn(Optional.of(new OrderTable(1L, null, 0, true)));

        List<OrderLineItemRequest> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItemRequest(1L, 3));
        orderLineItems.add(new OrderLineItemRequest(2L, 3));

        OrderRequest newOrder = new OrderRequest();
        newOrder.setOrderLineItems(orderLineItems);
        //when
        //then
        assertThatThrownBy(() -> orderService.create(newOrder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비어있는 주문 테이블입니다.");
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void list() {
        //given
        given(orderRepository.findAll())
                .willReturn(
                        Arrays.asList(
                                new Order(1L, OrderStatus.COOKING, LocalDateTime.now()),
                                new Order(2L, OrderStatus.MEAL, LocalDateTime.now())
                        )
                );
        //when
        List<OrderResponse> orders = orderService.list();

        //then
        assertThat(orders.size()).isEqualTo(2);

        assertThat(orders.get(0).getId()).isEqualTo(1L);
        assertThat(orders.get(0).getOrderTableId()).isEqualTo(2L);
        assertThat(orders.get(0).getOrderStatus()).isEqualTo(OrderStatus.COOKING);

        assertThat(orders.get(1).getId()).isEqualTo(2L);
        assertThat(orders.get(1).getOrderTableId()).isEqualTo(3L);
        assertThat(orders.get(1).getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus1() {
        //given
        given(orderRepository.findById(any()))
                .willReturn(
                        Optional.of(new Order(1L, OrderStatus.COOKING, LocalDateTime.now()))
                );

        OrderRequest changeOrder = new OrderRequest();
        changeOrder.setOrderStatus(OrderStatus.MEAL);

        //when
        OrderResponse changedOrder = orderService.changeOrderStatus(1L, changeOrder);

        //then
        assertThat(changedOrder.getId()).isEqualTo(1L);
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("주문 상태를 변경한다. - 신청하지 않은 주문은 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatus2() {
        //given
        given(orderRepository.findById(any()))
                .willReturn(Optional.empty());

        //when
        //then
        OrderRequest changeOrder = new OrderRequest();
        changeOrder.setOrderStatus(OrderStatus.MEAL);
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, changeOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태를 변경한다. - 이미 왼료상태인 주문은 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatus3() {
        //given
        given(orderRepository.findById(any()))
                .willReturn(
                        Optional.of(new Order(1L, OrderStatus.COMPLETION, LocalDateTime.now()))
                );

        OrderRequest changeOrder = new OrderRequest();
        changeOrder.setOrderStatus(OrderStatus.MEAL);

        //when
        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, changeOrder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 왼료상태인 주문은 상태 변경이 불가합니다.");
    }
}
