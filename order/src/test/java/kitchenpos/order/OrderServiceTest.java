package kitchenpos.order;

import kitchenpos.order.application.OrderMenuService;
import kitchenpos.order.application.OrderMenuServiceImpl;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.OrderValidator;
import kitchenpos.order.domain.MenuAdapter;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderMenuServiceImpl orderMenuService;

    @Mock
    private TableService tableService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderValidator orderValidator;

    @DisplayName("주문한다.")
    @Test
    void order() {

        //given
        final OrderRequest orderRequest = new OrderRequest();

        final OrderLineItemRequest orderLineItemRequestA = new OrderLineItemRequest();
        ReflectionTestUtils.setField(orderLineItemRequestA, "menuId", 1L);
        ReflectionTestUtils.setField(orderLineItemRequestA, "quantity", 1L);

        final OrderLineItemRequest orderLineItemRequestB = new OrderLineItemRequest();
        ReflectionTestUtils.setField(orderLineItemRequestB, "menuId", 2L);
        ReflectionTestUtils.setField(orderLineItemRequestB, "quantity", 1L);

        ReflectionTestUtils.setField(orderRequest, "orderLineItems", Arrays.asList(orderLineItemRequestA, orderLineItemRequestB));

        final OrderTable orderTable = OrderTable.setting(10, false);
        ReflectionTestUtils.setField(orderTable, "id", 1L);

        final Order order = Order.create(orderTable);
        ReflectionTestUtils.setField(order, "id", 1L);

        final MenuAdapter menuA = new MenuAdapter(orderLineItemRequestA.getMenuId(), "후라이드세트", new BigDecimal("17000"));
        final MenuAdapter menuB = new MenuAdapter(orderLineItemRequestB.getMenuId(), "햄버거세트", new BigDecimal("10000"));

        ReflectionTestUtils.setField(orderRequest, "orderTableId", orderTable.getId());
        when(orderMenuService.findAllByIds(anyList())).thenReturn(Arrays.asList(menuA, menuB));
        when(tableService.findById(anyLong())).thenReturn(orderTable);
        doNothing().when(orderValidator).validate(any());
        when(orderRepository.save(any())).thenReturn(order);

        //when
        final OrderResponse savedOrder = orderService.create(orderRequest);

        //then
        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getId()).isGreaterThan(0L);
    }

    @DisplayName("주문 리스트를 조회한다.")
    @Test
    void getOrders() {

        //given
        final int numberOfGuest = 10;
        final boolean isEmpty = false;
        OrderTable orderTable = OrderTable.setting(numberOfGuest, isEmpty);

        Order orderA = Order.create(orderTable);
        ReflectionTestUtils.setField(orderA, "id", 1L);

        Order orderB = Order.create(orderTable);
        ReflectionTestUtils.setField(orderB, "id", 2L);

        Order orderC = Order.create(orderTable);
        ReflectionTestUtils.setField(orderC, "id", 3L);

        List<Order> orders = Arrays.asList(orderA, orderB, orderC);

        when(orderRepository.findAll()).thenReturn(orders);

        //when
        List<OrderResponse> findOrders = orderService.list();

        //then
        Assertions.assertThat(findOrders).isNotEmpty();
        Assertions.assertThat(findOrders).extracting(OrderResponse::getId)
                .contains(orderA.getId(), orderB.getId(), orderC.getId());
    }

    @DisplayName("주문 상태를 수정한다.")
    @Test
    void changeOrderStatus() {

        //given
        final int numberOfGuests = 10;
        final OrderTable orderTable = OrderTable.setting(numberOfGuests);
        final Order order = Order.create(orderTable);
        ReflectionTestUtils.setField(order, "id", 1L);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(order));

        //when
        OrderResponse changeOrder = orderService.changeOrderStatus(order.getId());

        //then
        assertThat(changeOrder).isNotNull();
        assertThat(changeOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문 상태를 수정할 시 주문 상태가 Completion 일 경우")
    @Test
    void changeOrderStatusByOrderStatusCompletion() {

        //given
        final int numberOfGuests = 10;
        OrderTable orderTable = OrderTable.setting(numberOfGuests);
        Order order = Order.create(orderTable);
        ReflectionTestUtils.setField(order, "id", 1L);
        order.completion();

        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(order));

        //when
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId()))
                .isInstanceOf(IllegalStateException.class);

    }

}
