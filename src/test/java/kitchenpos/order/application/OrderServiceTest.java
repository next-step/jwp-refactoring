package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.menu.exception.MenuNotFoundException;
import kitchenpos.order.exception.OrderNotFoundException;
import kitchenpos.order.exception.OrderTableNotFoundException;
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
    private OrderRepository orderRepository;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
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

            Menu 커플세트 = new Menu(1L, "커플세트", 16000, new MenuGroup(), Collections.emptyList());
            Menu 혼밥세트 = new Menu(2L, "혼밥세트", 16000, new MenuGroup(), Collections.emptyList());
            OrderLineItemRequest orderLineItemRequest1 = new OrderLineItemRequest(커플세트.getId(), 1);
            OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(혼밥세트.getId(), 1);
            List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(orderLineItemRequest1, orderLineItemRequest2);
            OrderRequest orderRequest = new OrderRequest(orderTable.getId(), orderLineItemRequests);

            List<OrderLineItem> orderLineItems = new ArrayList<>();
            Order expectedOrder = new Order(1L, orderTable, OrderStatus.COOKING, orderLineItems);
            orderLineItems.add(new OrderLineItem(expectedOrder, 커플세트, orderLineItemRequest1.getQuantity()));
            orderLineItems.add(new OrderLineItem(expectedOrder, 혼밥세트, orderLineItemRequest2.getQuantity()));

            given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(orderTable));
            given(menuRepository.findById(anyLong())).willReturn(Optional.of(커플세트), Optional.of(혼밥세트));
            given(orderRepository.save(any(Order.class))).willReturn(expectedOrder);

            // when
            OrderResponse order = orderService.create(orderRequest);

            // then
            assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
            assertThat(order.getOrderLineItems()).map(OrderLineItemResponse::getMenuId)
                    .contains(커플세트.getId(), 혼밥세트.getId());
        }

        @DisplayName("주문 항목이 있어야 한다")
        @Test
        void requiredOrderItem() {
            // given
            OrderRequest orderRequest = new OrderRequest(1L, Collections.emptyList());

            // when
            ThrowableAssert.ThrowingCallable callable = () -> orderService.create(orderRequest);

            // then
            assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 항목에 메뉴에 있는 메뉴만 있어야 한다")
        @Test
        void validateMenu() {
            // given
            OrderTable orderTable = new OrderTable(1L, null, 4, false);

            OrderLineItemRequest orderLineItemRequest1 = new OrderLineItemRequest(1L, 1);
            OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(2L, 1);
            List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(orderLineItemRequest1, orderLineItemRequest2);
            OrderRequest orderRequest = new OrderRequest(orderTable.getId(), orderLineItemRequests);

            given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(orderTable));
            given(menuRepository.findById(anyLong())).willReturn(Optional.empty());

            // when
            ThrowableAssert.ThrowingCallable callable = () -> orderService.create(orderRequest);

            // then
            assertThatThrownBy(callable).isInstanceOf(MenuNotFoundException.class);
        }

        @DisplayName("주문 테이블이 있어야 한다")
        @Test
        void validateTable() {
            OrderTable orderTable = new OrderTable(1L, null, 4, false);

            OrderLineItemRequest orderLineItemRequest1 = new OrderLineItemRequest(1L, 1);
            OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(2L, 1);
            List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(orderLineItemRequest1, orderLineItemRequest2);
            OrderRequest orderRequest = new OrderRequest(orderTable.getId(), orderLineItemRequests);

            given(orderTableRepository.findById(anyLong())).willReturn(Optional.empty());

            // when
            ThrowableAssert.ThrowingCallable callable = () -> orderService.create(orderRequest);

            // then
            assertThatThrownBy(callable).isInstanceOf(OrderTableNotFoundException.class);
        }

        @DisplayName("주문 테이블이 비어있지 않아야 한다")
        @Test
        void notEmptyTable() {
            // given
            OrderTable orderTable = new OrderTable(1L, null, 4, true);

            OrderLineItemRequest orderLineItemRequest1 = new OrderLineItemRequest(1L, 1);
            OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(2L, 1);
            List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(orderLineItemRequest1, orderLineItemRequest2);
            OrderRequest orderRequest = new OrderRequest(orderTable.getId(), orderLineItemRequests);

            given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(orderTable));

            // when
            ThrowableAssert.ThrowingCallable callable = () -> orderService.create(orderRequest);

            // then
            assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
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
            assertThatThrownBy(callable).isInstanceOf(OrderNotFoundException.class);
        }

        @DisplayName("계산완료 상태에선 변경할 수 없다")
        @Test
        void canNotChangeWhenCompleteStatus() {
            // given
            OrderRequest requestOrder = new OrderRequest(OrderStatus.MEAL);
            Order savedOrder = new Order(1L, new OrderTable(), OrderStatus.COMPLETION, TEST_CREATED_AT, Collections.emptyList());
            given(orderRepository.findById(anyLong())).willReturn(Optional.of(savedOrder));

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
