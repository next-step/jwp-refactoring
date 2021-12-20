package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
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
import kitchenpos.fixture.OrderTableFixture;
import org.assertj.core.api.ThrowableAssert;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

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

    @DisplayName("주문을 요청할 수 있다.")
    @Test
    void create() {
        // given
        final OrderTable table = OrderTableFixture.of(1L, null, 4, false);
        final OrderLineItem item = createOrderLineItem(1L, 1L, 1);
        final Order order = createOrder(
            1L,
            table.getId(),
            OrderStatus.COOKING.name(),
            LocalDateTime.now(),
            Arrays.asList(item)
        );

        final OrderLineItem itemRequest = new OrderLineItem();
        itemRequest.setMenuId(1L);
        itemRequest.setQuantity(1);

        final Order orderRequest = new Order();
        orderRequest.setOrderTableId(1L);
        orderRequest.setOrderLineItems(Collections.singletonList(itemRequest));

        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(table));
        given(orderDao.save(any(Order.class))).willReturn(order);
        given(orderLineItemDao.save(any(OrderLineItem.class))).willReturn(item);

        // when
        final Order actual = orderService.create(orderRequest);

        // then
        assertThat(actual).isEqualTo(order);
    }

    @DisplayName("주문을 요청할 수 없다.")
    @Nested
    class CreateFailTest {

        @DisplayName("주문 항목이 없는 경우")
        @Test
        void emptyOrderLineItems() {
            // given
            final Order request = new Order();
            request.setOrderLineItems(Collections.emptyList());

            // when
            ThrowableAssert.ThrowingCallable actual = () -> orderService.create(request);

            // then
            assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 항목 수량이 메뉴의 주문 항목 수량과 일치하지 않는 경우")
        @Test
        void invalidNumberOfOrderLineItems() {
            // given
            final OrderLineItem item = new OrderLineItem();
            item.setMenuId(1L);
            item.setQuantity(1);

            final Order request = new Order();
            request.setOrderLineItems(Arrays.asList(item, item));

            // when
            ThrowableAssert.ThrowingCallable actual = () -> orderService.create(request);

            // then
            assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 등록되어 있지 않은 경우")
        @Test
        void noSuchOrderTable() {
            // given
            final OrderLineItem item = new OrderLineItem();
            item.setMenuId(1L);
            item.setQuantity(1);

            final Order request = new Order();
            request.setOrderLineItems(Arrays.asList(item));

            given(menuDao.countByIdIn(anyList())).willReturn(1L);
            given(orderTableDao.findById(any())).willReturn(Optional.empty());

            // when
            ThrowableAssert.ThrowingCallable actual = () -> orderService.create(request);

            // then
            assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 비어 있는 경우")
        @Test
        void emptyOrderTable() {
            // given
            final OrderLineItem item = new OrderLineItem();
            item.setMenuId(1L);
            item.setQuantity(1);

            final Order request = new Order();
            request.setOrderLineItems(Arrays.asList(item));

            given(menuDao.countByIdIn(anyList())).willReturn(1L);

            final OrderTable emptyTable = OrderTableFixture.of(1L, null, 4, true);
            given(orderTableDao.findById(any())).willReturn(Optional.of(emptyTable));

            // when
            ThrowableAssert.ThrowingCallable actual = () -> orderService.create(request);

            // then
            assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final OrderTable table = OrderTableFixture.of(1L, null, 4, false);
        final OrderLineItem item1 = createOrderLineItem(1L, 1L, 1);
        final Order order1 = createOrder(
            1L,
            table.getId(),
            OrderStatus.COOKING.name(),
            LocalDateTime.now(),
            Arrays.asList(item1)
        );
        final OrderLineItem item2 = createOrderLineItem(2L, 2L, 1);
        final Order order2 = createOrder(
            1L,
            table.getId(),
            OrderStatus.COOKING.name(),
            LocalDateTime.now(),
            Arrays.asList(item1)
        );
        final List<Order> expected = Arrays.asList(order1, order2);

        given(orderDao.findAll()).willReturn(expected);

        // when
        final List<Order> actual = orderService.list();

        // then
        assertThat(actual).containsExactlyElementsOf(expected);
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        // given
        final OrderTable table = OrderTableFixture.of(1L, null, 4, false);
        final OrderLineItem item = createOrderLineItem(1L, 1L, 1);
        final Order before = createOrder(
            1L,
            table.getId(),
            OrderStatus.COOKING.name(),
            LocalDateTime.now(),
            Collections.singletonList(item)
        );

        final Order request = new Order();
        request.setOrderStatus(OrderStatus.MEAL.name());

        final Order after = createOrder(
            before.getId(),
            before.getOrderTableId(),
            request.getOrderStatus(),
            before.getOrderedTime(),
            before.getOrderLineItems()
        );

        given(orderDao.findById(any())).willReturn(Optional.of(before));
        given(orderDao.save(any())).willReturn(after);
        given(orderLineItemDao.findAllByOrderId(any())).willReturn(
            Collections.singletonList(item));

        // when
        final Order actual = orderService.changeOrderStatus(1L, request);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(request.getOrderStatus());
    }

    @DisplayName("주문 상태를 변경할 수 없다.")
    @Nested
    class ChangeOrderStatusFailTest {

        @DisplayName("주문이 등록되어 있지 않은 경우")
        @Test
        void changeOrderStatus_fail_noSuchOrder() {
            // given
            final Order request = new Order();
            request.setOrderStatus(OrderStatus.COMPLETION.name());

            given(orderDao.findById(any())).willReturn(Optional.empty());

            // when
            ThrowingCallable actual = () -> orderService.changeOrderStatus(1L, request);

            // then
            assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문이 이미 완료된 경우")
        @Test
        void changeOrderStatus_fail_orderAlreadyCompleted() {
            // given
            final OrderTable table = OrderTableFixture.of(1L, null, 4, false);
            final OrderLineItem item = createOrderLineItem(1L, 1L, 1);
            final Order completedOrder = createOrder(
                1L,
                table.getId(),
                OrderStatus.COMPLETION.name(),
                LocalDateTime.now(),
                Collections.singletonList(item)
            );

            final Order request = new Order();
            request.setOrderStatus(OrderStatus.COMPLETION.name());

            given(orderDao.findById(any())).willReturn(Optional.of(completedOrder));

            // when
            ThrowingCallable actual = () -> orderService.changeOrderStatus(1L, request);

            // then
            assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
        }
    }

    private OrderLineItem createOrderLineItem(
        final Long id,
        final Long menuId,
        final long quantity
    ) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(id);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    private Order createOrder(
        final Long id,
        final Long orderTableId,
        final String orderStatus,
        final LocalDateTime orderedTime,
        final List<OrderLineItem> orderLineItems
    ) {
        final Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(orderedTime);
        order.setOrderLineItems(orderLineItems);
        return order;
    }
}
