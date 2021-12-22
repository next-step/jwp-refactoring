package kitchenpos.order.application;

import kitchenpos.order.domain.MenuCountOrderValidator;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.infra.OrderRepository;
import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.stream.Collectors;

import static kitchenpos.order.application.OrderServiceFixture.*;
import static kitchenpos.ordertable.application.OrderTableServiceTest.getOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@DisplayName("주문 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderTableService orderTableService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MenuCountOrderValidator menuCountOrderValidator;
    @InjectMocks
    private OrderService orderService;

    private OrderTable 빈_주문_테이블;
    private OrderTable 주문_테이블;


    @BeforeEach
    void setUp() {
        주문_테이블 = getOrderTable(1L, false, 7);
        빈_주문_테이블 = getOrderTable(1L, true, 7);
    }

    @DisplayName("주문테이블의 아이디, 주문 항목 목록을 통해 주문을 생성 할 수 있다.")
    @Test
    void create() {
        // given
        final OrderLineItem orderLineItem1 = getOrderLineItem(1L, 3);
        final OrderLineItem orderLineItem2 = getOrderLineItem(2L, 4);
        final Order expected = Order.of(1L,
                getOrderLineItems(orderLineItem1, orderLineItem2));
        final OrderRequest createRequest = getCreateRequest(주문_테이블.getId(),
                getOrderLineRequests(orderLineItem1, orderLineItem2));

        given(orderTableService.getOrderTable(anyLong())).willReturn(주문_테이블);
        doNothing().when(menuCountOrderValidator).validate(any());
        given(orderRepository.save(any())).willReturn(expected);

        // when
        OrderResponse actual = orderService.create(createRequest);
        // then
        assertThat(actual).isEqualTo(OrderResponse.of(expected));
    }


    @DisplayName("주문을 할 수 없는 경우")
    @Nested
    class CreateFailTest {

        @DisplayName("주문 항목 목록은 비어 있을 수 없다.")
        @Test
        void orderByEmptyOrderLines() {
            // given
            final OrderTable orderTable = getOrderTable(1L, false, 7);
            final OrderRequest createRequest = getCreateRequest(orderTable.getId(), Collections.emptyList());
            given(orderTableService.getOrderTable(anyLong())).willReturn(주문_테이블);
            // when
            ThrowableAssert.ThrowingCallable callable = () -> orderService.create(createRequest);
            // then
            assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 항목 개수에 따른 존재하는 메뉴 개수가 서로 일치하지 않으면 생성할 수 없다.")
        @Test
        void orderByNotEqualsMenuIds() {
            // given
            final OrderLineItem orderLineItem1 = getOrderLineItem(1L, 3);
            final OrderLineItem orderLineItem2 = getOrderLineItem(2L, 4);
            final OrderRequest createRequest = getCreateRequest(주문_테이블.getId(),
                    getOrderLineRequests(orderLineItem1, orderLineItem2));

            doThrow(new IllegalArgumentException()).when(menuCountOrderValidator).validate(anyList());
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
            final OrderRequest createRequest = getCreateRequest(주문_테이블.getId(),
                    getOrderLineRequests(orderLineItem1, orderLineItem2));

            doThrow(new IllegalArgumentException()).when(orderTableService).getOrderTable(anyLong());
            // when
            ThrowableAssert.ThrowingCallable callable = () -> orderService.create(createRequest);
            // then
            assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 빈 테이블가 아니어야 한다.")
        @Test
        void orderByNotExistOrderTable() {
            // given
            final OrderLineItem orderLineItem1 = getOrderLineItem(1L, 3);
            final OrderLineItem orderLineItem2 = getOrderLineItem(2L, 4);
            final OrderRequest createRequest = getCreateRequest(빈_주문_테이블.getId(),
                    getOrderLineRequests(orderLineItem1, orderLineItem2));

            given(orderTableService.getOrderTable(anyLong())).willReturn(빈_주문_테이블);
            doNothing().when(menuCountOrderValidator).validate(any());
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
        final Order order = getOrder(1L, 1L, getOrderLineItems(
                getOrderLineItem(1L, 3), getOrderLineItem(2L, 4))
        );
        final Order order2 = getOrder(2L, 2L, getOrderLineItems(
                getOrderLineItem(3L, 3), getOrderLineItem(4L, 4))
        );
        final List<Order> expected = Arrays.asList(order, order2);

        given(orderRepository.findAll()).willReturn(expected);
        // when
        List<OrderResponse> list = orderService.list();
        // then
        assertThat(list).containsExactlyElementsOf(Arrays.asList(OrderResponse.of(order), OrderResponse.of(order2)));
    }

}
