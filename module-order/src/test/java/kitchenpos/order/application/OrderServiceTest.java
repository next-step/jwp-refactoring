package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.EmptyOrderLineItemsException;
import kitchenpos.order.fixture.OrderFixtureFactory;
import kitchenpos.order.fixture.OrderLineItemFixtureFactory;
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

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private OrderService orderService;

    private OrderLineItem 주문항목_1;
    private OrderLineItem 주문항목_2;
    private Order 주문;

    @BeforeEach
    void setUp() {
        주문항목_1 = OrderLineItemFixtureFactory.create(1L, 1L);
        주문항목_2 = OrderLineItemFixtureFactory.create(2L, 2L);
        주문 = OrderFixtureFactory.create(1L, OrderStatus.COOKING, Lists.newArrayList(주문항목_1, 주문항목_2));
    }

    @DisplayName("주문을 할 수 있다")
    @Test
    void create01() {
        // given
        OrderRequest orderRequest = OrderRequest.of(1L,
                OrderStatus.COOKING,
                Lists.newArrayList(OrderLineItemRequest.of(주문항목_1.getMenuId(), 주문항목_1.findQuantity()),
                        OrderLineItemRequest.of(주문항목_2.getMenuId(), 주문항목_2.findQuantity())));

        given(orderRepository.save(any(Order.class))).willReturn(주문);

        // when
        OrderResponse response = orderService.create(orderRequest);

        // then
        assertThat(response).isEqualTo(OrderResponse.from(주문));
    }

    @DisplayName("주문항목은 1건 이상이어야 한다.")
    @Test
    void create02() {
        // given
        OrderRequest orderRequest = OrderRequest.of(1L,
                OrderStatus.COOKING,
                Collections.emptyList());

        // when & then
        assertThatExceptionOfType(EmptyOrderLineItemsException.class)
                .isThrownBy(() -> orderService.create(orderRequest));
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void find01() {
        // given
        given(orderRepository.findAll()).willReturn(Lists.newArrayList(주문));

        // when
        List<OrderResponse> orders = orderService.list();

        // then
        assertThat(orders).containsExactly(OrderResponse.from(주문));
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void change01() {
        // given
        주문.changeOrderStatus(OrderStatus.MEAL);
        OrderRequest orderRequest = OrderRequest.of(1L,
                OrderStatus.MEAL,
                Lists.newArrayList(OrderLineItemRequest.of(주문항목_1.getMenuId(), 주문항목_2.findQuantity())));

        given(orderRepository.findById(주문.getId())).willReturn(Optional.of(주문));

        // when
        OrderResponse changedOrder = orderService.changeOrderStatus(주문.getId(), orderRequest);

        // then
        assertAll(
                () -> assertThat(changedOrder).isEqualTo(OrderResponse.from(주문)),
                () -> assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL)
        );
    }

    @DisplayName("주문 상태가 \"COMPLETION\"인 경우 주문 상태를 변경할 수 없다.")
    @Test
    void change02() {
        // given
        주문.changeOrderStatus(OrderStatus.COMPLETION);
        OrderRequest orderRequest = OrderRequest.of(1L,
                OrderStatus.COOKING,
                Lists.newArrayList(OrderLineItemRequest.of(주문항목_1.getMenuId(), 주문항목_2.findQuantity())));

        given(orderRepository.findById(주문.getId())).willReturn(Optional.of(주문));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(주문.getId(), orderRequest));
    }
}
