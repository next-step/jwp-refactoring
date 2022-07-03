package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
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
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;

    private List<OrderLineItemRequest> orderLineItems;

    @BeforeEach
    void setUp() {
        OrderLineItemRequest orderLineItem1 = new OrderLineItemRequest(1L, 3);
        OrderLineItemRequest orderLineItem2 = new OrderLineItemRequest(1L, 3);
        orderLineItems = Arrays.asList(orderLineItem1, orderLineItem2);
    }

    @Test
    @DisplayName("주문은 주문항목이 없으면 주문을 할 수 없다")
    void orderItemEmptyCreateOrder() {
        //given
        OrderRequest order = new OrderRequest(1L, new ArrayList<>());
        given(orderTableRepository.findByIdAndEmptyIsFalse(1L)).willReturn(Optional.of(new OrderTable(3, false)));

        //when & then
        assertThatIllegalArgumentException().isThrownBy(() ->
                orderService.create(order)
        );
    }

    @Test
    @DisplayName("등록되지 않은 메뉴를 주문하면 주문을 할 수 없다.")
    void registerNotMenuOrder() {
        //given
        OrderRequest order = new OrderRequest(1L, orderLineItems);
        given(orderTableRepository.findByIdAndEmptyIsFalse(1L)).willReturn(Optional.of(new OrderTable(3, false)));
        given(menuRepository.countByIdIn(anyList())).willReturn(1L);

        //orderService.create(order);
        //when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(NoSuchElementException.class);
    }
//
    @Test
    @DisplayName("존재하지 않은 테이블에 주문 할 수 없다.")
    void existNotOrderTable() {
        //gvien
        OrderRequest order = new OrderRequest(1L, orderLineItems);
        given(orderTableRepository.findByIdAndEmptyIsFalse(1L)).willReturn(Optional.of(new OrderTable(3, false)));
        given(menuRepository.countByIdIn(anyList())).willReturn(2L);

        //when & then
        assertThatIllegalArgumentException().isThrownBy(() ->
                orderService.create(order)
        );
    }

    @Test
    @DisplayName("주문 받을 수 있는 주문 테이블이 없다면 주문을 할 수 없다.")
    void emptyOrderTable() {
        //gvien
        OrderRequest order = new OrderRequest(1L, orderLineItems);
        given(orderTableRepository.findByIdAndEmptyIsFalse(1L)).willReturn(Optional.of(new OrderTable(3, false)));
        given(menuRepository.countByIdIn(anyList())).willReturn(2L);

        //when & then
        assertThatIllegalArgumentException().isThrownBy(() ->
                orderService.create(order)
        );
    }
//
    @Test
    @DisplayName("주문을 한다.")
    void createOrder() {
        //gvien
        Order order = new Order(1L, 1L, OrderStatus.COOKING, LocalDateTime.now(), OrderLineItems.of(orderLineItems));
        OrderRequest orderRequest = new OrderRequest(1L, orderLineItems);
        given(orderTableRepository.findByIdAndEmptyIsFalse(1L)).willReturn(Optional.of(new OrderTable(1L, 1L,3, false)));
        given(menuRepository.countByIdIn(anyList())).willReturn(2L);
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
        Order order = new Order(1L, 1L, OrderStatus.COOKING, LocalDateTime.now(), OrderLineItems.of(orderLineItems));
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
        Order order = new Order(1L, 1L, OrderStatus.COMPLETION, LocalDateTime.now(), OrderLineItems.of(orderLineItems));
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
        Order order = new Order(1L, 1L, OrderStatus.COOKING, LocalDateTime.now(), OrderLineItems.of(orderLineItems));
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        //when
        final OrderResponse changeStatusOrder = orderService.changeOrderStatus(1L, orderRequest);
        //then
        assertThat(changeStatusOrder.getOrderStatus()).isEqualTo( OrderStatus.MEAL.name());
    }

}