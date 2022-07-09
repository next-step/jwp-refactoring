package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.domain.Price;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderValidator orderValidator;

    @InjectMocks
    private OrderService orderService;

    private List<OrderLineItem> orderLineItemList;
    private List<OrderLineItemRequest> orderLineItems;
    private OrderLineItem orderLineItems1;
    private OrderLineItem orderLineItems2;

    @BeforeEach
    void setUp() {
        OrderLineItemRequest orderLineItemRequest1 = new OrderLineItemRequest(1L, 3);
        OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(1L, 3);


        orderLineItems = Arrays.asList(orderLineItemRequest1, orderLineItemRequest2);

        orderLineItems1 = OrderLineItem.of(1L, Price.from(100).value(), "1L", 10);
        orderLineItems2 = OrderLineItem.of(1L, Price.from(100).value(), "2B", 10);
        orderLineItemList = Arrays.asList(orderLineItems1, orderLineItems2);
    }


    @Test
    @DisplayName("주문을 한다.")
    void createOrder() {

        Order order = new Order(1L, 1L, OrderStatus.COOKING, LocalDateTime.now(), OrderLineItems.from(Arrays.asList(orderLineItems1, orderLineItems2)));
        OrderRequest orderRequest = new OrderRequest(1L, orderLineItems);

        given(orderValidator.createValidation(any())).willReturn(order);
        given(orderRepository.save(any())).willReturn(order);

        //when
        OrderResponse saveOrder = orderService.create(orderRequest);

        //then
        assertAll(
                () -> assertThat(saveOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(saveOrder.getOrderLineItems()).hasSize(2)
        );
    }

    @Test
    @DisplayName("주문 목록을 조회한다.")
    void orderList() {
        //gvien
        Order order = new Order(1L, 1L, OrderStatus.COOKING, LocalDateTime.now(), OrderLineItems.from(Arrays.asList(orderLineItems1, orderLineItems2)));
        given(orderRepository.findAll()).willReturn(Collections.singletonList(order));


        //when
        List<OrderResponse> orders = orderService.list();

        //then
        assertAll(
                () -> assertThat(orders).hasSize(1),
                () -> assertThat(orders.get(0).getId()).isEqualTo(order.getId())
        );
    }
//
    @Test
    @DisplayName("주문이 존재하지 않으면 변경할 수 없다.")
    void orderIsNotExistNotChange() {
        //given
        OrderRequest orderRequest = new OrderRequest(OrderStatus.COOKING.name());
        given(orderRepository.findById(any())).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, orderRequest))
                .isInstanceOf(NoSuchElementException.class);
    }
//
    @Test
    @DisplayName("주문이 완료가 되었으면 경우에는 주문을 변경 할 수 없다.")
    void orderStatusCompleteNotChange() {
        //given
        OrderRequest orderRequest = new OrderRequest(OrderStatus.COOKING.name());
        Order order = new Order(1L, OrderStatus.COMPLETION, LocalDateTime.now(), OrderLineItems.from(orderLineItemList));
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        //when & then
        assertThatIllegalArgumentException().isThrownBy(() ->
                orderService.changeOrderStatus(1L, orderRequest)
        );
    }
//
    @Test
    @DisplayName("주문이 상태가 변경이 된다.")
    void orderStatusChange() {
        //given
        OrderRequest orderRequest = new OrderRequest(OrderStatus.MEAL.name());
        Order order = new Order(1L, 1L, OrderStatus.COOKING, LocalDateTime.now(), OrderLineItems.from(orderLineItemList));
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        //when
        final OrderResponse changeStatusOrder = orderService.changeOrderStatus(1L, orderRequest);
        //then
        assertThat(changeStatusOrder.getOrderStatus()).isEqualTo( OrderStatus.MEAL.name());
    }

}
