package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
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

    @DisplayName("주문을 등록할 수 있다. ")
    @Test
    void 주문_등록() {
        // given
        Long orderId = 1L;
        Long orderTableId = 1L;
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, orderId, 1L, 1);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, orderId, 2L, 1);
        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem1, orderLineItem2);
        OrderTable orderTable = new OrderTable(orderTableId, 2);
        Order expected = new Order(orderId, orderTableId, orderLineItems);

        given(menuDao.countByIdIn(anyList()))
            .willReturn((long) orderLineItems.size());
        given(orderTableDao.findById(orderTableId))
            .willReturn(Optional.of(orderTable));
        given(orderDao.save(any()))
            .willReturn(expected);

        // when
        Order actual = orderService.create(expected);

        // then
        assertThat(expected).isEqualTo(actual);
    }

    @DisplayName("주문 항목이 비어 있으면 주문을 등록할 수 없다.")
    @Test
    void 주문_등록_예외_주문_항목_없음() {
        // given
        List<OrderLineItem> orderLineItems = Lists.emptyList();
        Order expected = new Order(1L, 1L, orderLineItems);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> orderService.create(expected)
        );
    }

    @DisplayName("등록되어 있는 주문 테이블이 아니면 주문을 등록할 수 없다.")
    @Test
    void 주문_등록_예외_주문_테이블_없음() {
        // given
        Long orderId = 1L;
        Long orderTableId = 1L;
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, orderId, 1L, 1);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, orderId, 2L, 1);
        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem1, orderLineItem2);
        Order expected = new Order(orderId, orderTableId, orderLineItems);

        given(menuDao.countByIdIn(anyList()))
            .willReturn((long) orderLineItems.size());
        given(orderTableDao.findById(orderTableId))
            .willReturn(Optional.empty());

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> orderService.create(expected)
        );
    }

    @DisplayName("주문 테이블이 빈 테이블이면 등록할 수 없다.")
    @Test
    void 주문_등록_예외_빈_테이블() {
        // given
        Long orderId = 1L;
        Long orderTableId = 1L;
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, orderId, 1L, 1);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, orderId, 2L, 1);
        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem1, orderLineItem2);
        OrderTable emptyTable = new OrderTable(orderTableId, 2, true);
        Order expected = new Order(orderId, orderTableId, orderLineItems);

        given(menuDao.countByIdIn(anyList()))
            .willReturn((long) orderLineItems.size());
        given(orderTableDao.findById(orderTableId))
            .willReturn(Optional.of(emptyTable));

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> orderService.create(expected)
        );
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void 주문목록_조회() {
        // given
        Long orderId = 1L;
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, orderId, 1L, 1);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, orderId, 2L, 1);
        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem1, orderLineItem2);

        Order order1 = new Order(1L, 1L, orderLineItems);
        Order order2 = new Order(2L, 2L, orderLineItems);
        List<Order> expected = Arrays.asList(order1, order2);
        given(orderDao.findAll())
            .willReturn(expected);
        given(orderLineItemDao.findAllByOrderId(any()))
            .willReturn(orderLineItems);

        // when
        List<Order> actual = orderService.list();

        // then
        assertThat(expected).isEqualTo(actual);
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void 주문상태_변경() {
        // given
        Long orderId = 1L;
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, orderId, 1L, 1);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, orderId, 2L, 1);
        List<OrderLineItem> orderLineItems = Lists.newArrayList(orderLineItem1, orderLineItem2);
        Order order = new Order(orderId, 1L, orderLineItems);
        Order expected = new Order(orderId, 1L, OrderStatus.MEAL.name(), orderLineItems);


        given(orderDao.findById(any()))
            .willReturn(Optional.of(order));
        given(orderLineItemDao.findAllByOrderId(orderId))
            .willReturn(orderLineItems);

        // when
        Order actual = orderService.changeOrderStatus(orderId, expected);

        // then
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getOrderTableId()).isEqualTo(expected.getOrderTableId());
        assertThat(actual.getOrderLineItems()).containsAll(expected.getOrderLineItems());
    }

    @DisplayName("주문이 COMPLETION 상태일 때는 주문 변경을 할 수 없다.")
    @Test
    void 주문상태_변경_예외() {
        // given
        Long orderId = 1L;
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, orderId, 1L, 1);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, orderId, 2L, 1);
        List<OrderLineItem> orderLineItems = Lists.newArrayList(orderLineItem1, orderLineItem2);
        Order order = new Order(orderId, 1L, OrderStatus.COMPLETION.name(), orderLineItems);
        Order updateOrder = new Order(orderId, 1L, OrderStatus.MEAL.name(), orderLineItems);

        given(orderDao.findById(any()))
            .willReturn(Optional.of(order));

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> orderService.changeOrderStatus(orderId, updateOrder)
        );
    }
}
