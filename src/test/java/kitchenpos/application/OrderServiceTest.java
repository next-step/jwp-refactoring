package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    public static final LocalDateTime TEST_CREATED_AT = LocalDateTime.of(2021, 12, 1, 12, 0);
    @Mock
    private OrderFactory orderFactory;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문 생성")
    @Nested
    class CreateTest {
        @DisplayName("주문을 생성한다")
        @Test
        void testCreate() {
            // given
            OrderTable orderTable = new OrderTable(1L, null, 4, false);

            OrderLineItemRequest orderLineItemRequest1 = new OrderLineItemRequest(1L, 1);
            OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(2L, 1);
            List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(orderLineItemRequest1, orderLineItemRequest2);
            OrderRequest orderRequest = new OrderRequest(orderTable.getId(), orderLineItemRequests);

            Order expectedOrder = new Order(orderTable, new ArrayList<>());

            given(orderFactory.create(any(OrderRequest.class))).willReturn(expectedOrder);
            given(orderRepository.save(any(Order.class))).willReturn(expectedOrder);

            // when
            OrderResponse order = orderService.create(orderRequest);

            // then
            assertThat(order).isEqualTo(OrderResponse.of(expectedOrder));
        }
    }

    @DisplayName("주문상태 변경")
    @Nested
    class ChangeStatusTest {
        @DisplayName("주문 상태를 변경한다")
        @Test
        void testChangeOrderStatus() {
            // given
            OrderRequest requestOrder = new OrderRequest(OrderStatus.COMPLETION);
            Order savedOrder = new Order(1L, new OrderTable(), OrderStatus.COOKING, TEST_CREATED_AT, Collections.emptyList());

            given(orderRepository.findById(anyLong())).willReturn(Optional.of(savedOrder));

            // when
            OrderResponse order = orderService.changeOrderStatus(savedOrder.getId(), requestOrder);

            // then
            assertThat(order.getOrderStatus().name()).isEqualTo(requestOrder.getOrderStatus());
        }

        @DisplayName("생성된 주문이 있어야 한다")
        @Test
        void hasSavedOrder() {
            // given
            OrderRequest requestOrder = new OrderRequest(OrderStatus.COMPLETION);
            Order savedOrder = new Order(1L, new OrderTable(), OrderStatus.COOKING, TEST_CREATED_AT, Collections.emptyList());

            given(orderRepository.findById(anyLong())).willReturn(Optional.empty());

            // when
            ThrowableAssert.ThrowingCallable callable = () -> orderService.changeOrderStatus(savedOrder.getId(), requestOrder);

            // then
            assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("계산완료 상태에선 변경할 수 없다")
        @Test
        void canNotChangeWhenCompleteStatus() {
            // given
            OrderRequest requestOrder = new OrderRequest(OrderStatus.COMPLETION);
            given(orderRepository.findById(anyLong())).willReturn(Optional.empty());

            // when
            ThrowableAssert.ThrowingCallable callable = () -> orderService.changeOrderStatus(anyLong(), requestOrder);

            // then
            assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("모든 주문을 조회한다")
    @Test
    void testList() {
        // given
        List<OrderLineItem> orderLineItems = Collections.emptyList();
        Order order = new Order(1L, new OrderTable(), OrderStatus.COOKING, TEST_CREATED_AT, orderLineItems);
        List<Order> expectedOrders = Arrays.asList(order);

        given(orderRepository.findAll()).willReturn(expectedOrders);

        // when
        List<OrderResponse> orders = orderService.list();

        // then
        assertThat(orders).isEqualTo(OrderResponse.ofList(expectedOrders));
    }
}
