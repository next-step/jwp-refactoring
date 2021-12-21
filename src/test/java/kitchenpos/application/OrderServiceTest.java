package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
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
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문 생성")
    @Test
    void 주문_생성() {
        // given
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 1L, 1);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, 1L, 2L, 1);
        List<OrderLineItem> orderLineItems = Lists.newArrayList(orderLineItem1, orderLineItem2);
        Order order = new Order(1L, 1L, "COOKING", null, orderLineItems);
        OrderTable orderTable = new OrderTable(1L, null, 2, false);

        given(menuDao.countByIdIn(anyList())).willReturn((long) orderLineItems.size());
        given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));
        given(orderDao.save(any())).willReturn(order);

        // when
        Order result = orderService.create(order);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getOrderTableId()).isEqualTo(1L);
        assertThat(result.getOrderStatus()).isEqualTo("COOKING");
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

        Order order1 = new Order(1L, 1L, "COOKING", LocalDateTime.of(2021, 12, 19, 18, 30), null);
        Order order2 = new Order(2L, 2L, "MEAL", LocalDateTime.of(2021, 12, 19, 17, 0), null);

        given(orderDao.findAll()).willReturn(Lists.newArrayList(order1, order2));
        given(orderLineItemDao.findAllByOrderId(any())).willReturn(orderLineItems1,
            orderLineItems2);

        // when
        List<Order> result = orderService.list();

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getOrderTableId()).isEqualTo(1L);
        assertThat(result.get(0).getOrderStatus()).isEqualTo("COOKING");
        assertThat(result.get(0).getOrderLineItems()).hasSize(2);
        assertThat(result.get(0).getOrderLineItems().get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getOrderLineItems().get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getOrderTableId()).isEqualTo(2L);
        assertThat(result.get(1).getOrderStatus()).isEqualTo("MEAL");
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
        Order order = new Order(1L, 1L, "COOKING", null, orderLineItems);
        Order orderForUpdate = new Order(1L, 1L, "MEAL", null, orderLineItems);

        given(orderDao.findById(any())).willReturn(Optional.of(order));
        given(orderLineItemDao.findAllByOrderId(1L)).willReturn(orderLineItems);

        // when
        Order result = orderService.changeOrderStatus(1L, orderForUpdate);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getOrderStatus()).isEqualTo("MEAL");

    }

    @DisplayName("완료 상태일 때 주문 상태 변경시 예외 발생")
    @Test
    void 주문상태_변경_예외() {
        // given
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 1L, 1);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, 1L, 2L, 1);
        List<OrderLineItem> orderLineItems = Lists.newArrayList(orderLineItem1, orderLineItem2);
        Order order = new Order(1L, 1L, "COMPLETION", null, orderLineItems);
        Order orderForUpdate = new Order(1L, 1L, "MEAL", null, orderLineItems);

        given(orderDao.findById(any())).willReturn(Optional.of(order));

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> orderService.changeOrderStatus(1L, orderForUpdate)
        );
    }
}