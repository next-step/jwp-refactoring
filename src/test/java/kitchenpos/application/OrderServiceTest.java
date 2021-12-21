package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.menu.infra.MenuRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.application.TableServiceTest.getOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderLineItemDao orderLineItemDao;
    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문테이블의 아이디, 주문 항목 목록을 통해 주문을 생성 할 수 있다.")
    @Test
    void create() {
        // given
        final OrderTable orderTable = getOrderTable(1L, false, 7);
        final OrderLineItem orderLineItem1 = getOrderLineItem(1L, 3);
        final OrderLineItem orderLineItem2 = getOrderLineItem(2L, 4);
        final Order createRequest = getCreateRequest(orderTable.getId(), Arrays.asList(
                orderLineItem1,
                orderLineItem2)
        );
        final Order order = getOrder(1L, createRequest);

        given(menuRepository.countByIdIn(any())).willReturn(2L);
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
        given(orderDao.save(any())).willReturn(order);
        given(orderLineItemDao.save(orderLineItem1)).willReturn(orderLineItem1);
        given(orderLineItemDao.save(orderLineItem2)).willReturn(orderLineItem2);

        // when
        Order actual = orderService.create(createRequest);
        // then
        assertThat(order).isEqualTo(actual);
    }


    @DisplayName("주문을 할 수 없는 경우")
    @Nested
    class CreateFailTest {

        @DisplayName("주문 항목 목록은 비어 있을 수 없다.")
        @Test
        void orderByEmptyOrderLines() {
            // given
            final OrderTable orderTable = getOrderTable(1L, false, 7);
            final Order createRequest = getCreateRequest(orderTable.getId(), Collections.emptyList());
            // when
            ThrowableAssert.ThrowingCallable callable = () -> orderService.create(createRequest);
            // then
            assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 항목 개수에 따른 존재하는 메뉴 개수가 서로 일치하지 않으면 생성할 수 없다.")
        @Test
        void orderByNotEqualsMenuIds() {
            // given
            final OrderTable orderTable = getOrderTable(1L, false, 7);
            final OrderLineItem orderLineItem1 = getOrderLineItem(1L, 3);
            final OrderLineItem orderLineItem2 = getOrderLineItem(2L, 4);
            final Order createRequest = getCreateRequest(orderTable.getId(), Arrays.asList(
                    orderLineItem1,
                    orderLineItem2)
            );
            given(menuRepository.countByIdIn(any())).willReturn(1L);
            // when
            ThrowableAssert.ThrowingCallable callable = () -> orderService.create(createRequest);
            // then
            assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블의 아이디를 따른 주문테이블이 존재해야 한다.")
        @Test
        void orderByNotExistOrderTableId() {
            // given
            final OrderLineItem orderLineItem1 = getOrderLineItem(1L, 3);
            final OrderLineItem orderLineItem2 = getOrderLineItem(2L, 4);
            final Order createRequest = getCreateRequest(null, Arrays.asList(
                    orderLineItem1,
                    orderLineItem2)
            );

            given(menuRepository.countByIdIn(any())).willReturn(2L);
            given(orderTableDao.findById(any())).willReturn(Optional.empty());
            // when
            ThrowableAssert.ThrowingCallable callable = () -> orderService.create(createRequest);
            // then
            assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 빈 테이블가 아니어야 한다.")
        @Test
        void orderByNotExistOrderTable() {
            // given
            final OrderTable orderTable = getOrderTable(1L, true, 7);
            final OrderLineItem orderLineItem1 = getOrderLineItem(1L, 3);
            final OrderLineItem orderLineItem2 = getOrderLineItem(2L, 4);
            final Order createRequest = getCreateRequest(1L, Arrays.asList(
                    orderLineItem1,
                    orderLineItem2)
            );

            given(menuRepository.countByIdIn(any())).willReturn(2L);
            given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));

            // when
            ThrowableAssert.ThrowingCallable callable = () -> orderService.create(createRequest);
            // then
            assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
        }

    }


    @DisplayName("주문 목록을 조회 할 수 있다.")
    @Test
    void list() {
        // given
        final Order order = getOrder(1L, 1L, Arrays.asList(
                getOrderLineItem(1L, 3),
                getOrderLineItem(2L, 4)
        ));
        final Order order2 = getOrder(2L, 2L, Arrays.asList(
                getOrderLineItem(3L, 3),
                getOrderLineItem(4L, 4)
        ));
        final List<Order> expected = Arrays.asList(order, order2);

        given(orderDao.findAll()).willReturn(expected);
        // when
        List<Order> list = orderService.list();
        // then
        assertThat(list).containsExactlyElementsOf(expected);
    }

    @DisplayName("주문 아이디를 통해 주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        // given
        Order changeRequest = getChangeRequest(1L, OrderStatus.COMPLETION.name());
        final Order expected = getChangeExpectedResult(OrderStatus.COOKING.name());

        given(orderDao.findById(anyLong())).willReturn(Optional.of(expected));
        // when
        final Order actual = orderService.changeOrderStatus(changeRequest.getId(), changeRequest);
        // then
        assertThat(actual.getOrderStatus()).isEqualTo(expected.getOrderStatus());
    }

    @DisplayName("주문 상태를 변경할 수 없는 경우")
    @Nested
    class ChangeOrderStatusFail {
        @DisplayName("주문 아이디를 따른 주문이 존재하지 않는 경우")
        @Test
        void changeOrderStatusByEmptyOrder() {
            // given
            Order changeRequest = getChangeRequest(1L, OrderStatus.COOKING.name());
            given(orderDao.findById(anyLong())).willReturn(Optional.empty());
            // when
            ThrowableAssert.ThrowingCallable callable = () -> orderService.changeOrderStatus(changeRequest.getId(), changeRequest);
            // then
            assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 상태가 계산완료인 경우")
        @Test
        void changeOrderStatusByCompletionStatus() {
            // given
            final Order changeRequest = getChangeRequest(1L, OrderStatus.COMPLETION.name());
            final Order expected = getChangeExpectedResult(OrderStatus.COMPLETION.name());
            given(orderDao.findById(anyLong())).willReturn(Optional.of(expected));

            // when
            ThrowableAssert.ThrowingCallable callable = () -> orderService.changeOrderStatus(changeRequest.getId(), changeRequest);
            // then
            assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
        }
    }


    private Order getChangeExpectedResult(String status) {
        final Order expected = new Order();
        expected.setOrderStatus(status);
        return expected;
    }

    private Order getChangeRequest(Long id, String status) {
        Order request = new Order();
        request.setId(id);
        request.setOrderStatus(status);
        return request;
    }

    private Order getOrder(long id, Order createRequest) {
        final Order order = new Order();
        order.setId(id);
        order.setOrderTableId(createRequest.getOrderTableId());
        order.setOrderLineItems(createRequest.getOrderLineItems());
        return order;
    }

    private Order getOrder(long id, long orderTableId, List<OrderLineItem> orderLineItems) {
        final Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    private Order getCreateRequest(Long orderTableId, List<OrderLineItem> orderLineItems) {
        final Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    private OrderLineItem getOrderLineItem(long seq, int quantity) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(quantity);
        orderLineItem.setSeq(seq);
        return orderLineItem;
    }
}
