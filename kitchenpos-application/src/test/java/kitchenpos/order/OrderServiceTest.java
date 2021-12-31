package kitchenpos.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.MenuRepository;
import kitchenpos.table.OrderTable;
import kitchenpos.table.OrderTableRepository;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.order.OrderStatusRequest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @DisplayName("주문 생성")
    @Test
    void 주문_생성() {
        // given
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 1L, 1);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, 1L, 2L, 1);
        List<OrderLineItem> orderLineItems = Lists.newArrayList(orderLineItem1, orderLineItem2);
        Order order = new Order(1L, 1L, OrderStatus.COOKING, null, orderLineItems);
        OrderTable orderTable = new OrderTable(1L, null, 2, false);

        given(menuRepository.countByIdIn(anyList())).willReturn(orderLineItems.size());
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));
        given(orderRepository.save(any())).willReturn(order);
        given(orderLineItemRepository.saveAll(anyList())).willReturn(orderLineItems);

        // when
        OrderResponse result = orderService.create(order);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getOrderTableId()).isEqualTo(1L);
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(result.getOrderedTime()).isNotNull();
        assertThat(result.getOrderLineItems()).hasSize(2);

    }

    @DisplayName("주문 목록 조회")
    @Test
    void 주문목록_조회() {
        // given
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 1L, 1);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, 2L, 2L, 1);
        List<OrderLineItem> orderLineItems1 = Lists.newArrayList(orderLineItem1, orderLineItem2);

        OrderLineItem orderLineItem3 = new OrderLineItem(3L, 3L, 1L, 1);
        OrderLineItem orderLineItem4 = new OrderLineItem(4L, 4L, 2L, 2);
        List<OrderLineItem> orderLineItems2 = Lists.newArrayList(orderLineItem3, orderLineItem4);

        Order order1 = new Order(1L, 1L, OrderStatus.COOKING,
            LocalDateTime.of(2021, 12, 19, 18, 30), orderLineItems1);
        Order order2 = new Order(2L, 2L, OrderStatus.MEAL, LocalDateTime.of(2021, 12, 19, 17, 0),
            orderLineItems2);

        given(orderRepository.findAll()).willReturn(Lists.newArrayList(order1, order2));

        // when
        List<OrderResponse> result = orderService.list();

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getOrderTableId()).isEqualTo(1L);
        assertThat(result.get(0).getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(result.get(0).getOrderLineItems()).hasSize(2);
        assertThat(result.get(0).getOrderLineItems().get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getOrderLineItems().get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getOrderTableId()).isEqualTo(2L);
        assertThat(result.get(1).getOrderStatus()).isEqualTo(OrderStatus.MEAL);
        assertThat(result.get(1).getOrderLineItems()).hasSize(2);
        assertThat(result.get(1).getOrderLineItems().get(0).getId()).isEqualTo(3L);
        assertThat(result.get(1).getOrderLineItems().get(1).getId()).isEqualTo(4L);

    }

    @DisplayName("주문 상태 변경")
    @Test
    void 주문상태_변경() {
        // given
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 1L, 1);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, 1L, 2L, 1);
        List<OrderLineItem> orderLineItems = Lists.newArrayList(orderLineItem1, orderLineItem2);
        Order order = new Order(1L, 1L, OrderStatus.COOKING, null, orderLineItems);
        OrderStatusRequest orderForUpdate = new OrderStatusRequest(OrderStatus.MEAL);

        given(orderRepository.findById(any())).willReturn(Optional.of(order));

        // when
        OrderResponse result = orderService.changeOrderStatus(1L, orderForUpdate);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL);

    }

    @DisplayName("완료 상태일 때 주문 상태 변경시 예외 발생")
    @Test
    void 주문상태_변경_예외() {
        // given
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 1L, 1);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, 1L, 2L, 1);
        List<OrderLineItem> orderLineItems = Lists.newArrayList(orderLineItem1, orderLineItem2);
        Order order = new Order(1L, 1L, OrderStatus.COMPLETION, null, orderLineItems);
        OrderStatusRequest orderForUpdate = new OrderStatusRequest(OrderStatus.MEAL);

        given(orderRepository.findById(any())).willReturn(Optional.of(order));

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> orderService.changeOrderStatus(1L, orderForUpdate)
        );
    }
}