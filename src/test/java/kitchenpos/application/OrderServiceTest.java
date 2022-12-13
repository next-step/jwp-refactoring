package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderStatusRequest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private Order order = new Order(new OrderTable(1L, 1L, 1, false));
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderLineItemRepository orderLineItemRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuRepository, orderRepository, orderLineItemRepository, orderTableRepository);
    }

    @Test
    void 주문을_등록할_수_있다() {
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(new OrderLineItemRequest(1L, 1l),
                new OrderLineItemRequest(2L, 1l));
        OrderRequest orderRequest = new OrderRequest(1L, orderLineItems);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(new OrderTable(1L, 1L, 2, false)));
        given(menuRepository.countByIdIn(any())).willReturn(2L);
        given(orderRepository.save(any())).willReturn(order);

        Order createOrder = orderService.create(orderRequest);

        assertThat(createOrder).isEqualTo(order);
    }

    @Test
    void 수량이_남은_메뉴만_주문할_수_있다() {
        OrderRequest orderRequest = new OrderRequest(1L, Collections.emptyList());
        given(orderTableRepository.findById(any())).willReturn(Optional.of(new OrderTable(1L, 1L, 2, false)));

        ThrowingCallable 수량이_남지_않은_메뉴_주문시도 = () -> orderService.create(orderRequest);

        assertThatIllegalArgumentException().isThrownBy(수량이_남지_않은_메뉴_주문시도);
    }

    @Test
    void 등록_된_메뉴만_지정할_수_있다() {
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(new OrderLineItemRequest(1L, 1l),
                new OrderLineItemRequest(2L, 1l));
        OrderRequest orderRequest = new OrderRequest(1L, orderLineItems);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(new OrderTable(1L, 1L, 2, false)));
        given(menuRepository.countByIdIn(any())).willReturn(1L);

        ThrowingCallable 없는_메뉴가_포함된_주문시도 = () -> orderService.create(orderRequest);

        assertThatIllegalArgumentException().isThrownBy(없는_메뉴가_포함된_주문시도);
    }

    @Test
    void 등록_된_주문_테이블만_지정할_수_있다() {
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(new OrderLineItemRequest(1L, 1l),
                new OrderLineItemRequest(2L, 1l));
        OrderRequest orderRequest = new OrderRequest(1L, orderLineItems);
        given(orderTableRepository.findById(any())).willThrow(IllegalArgumentException.class);

        ThrowingCallable 등록_되지_않은_주문_테이블_지정 = () -> orderService.create(orderRequest);

        assertThatIllegalArgumentException().isThrownBy(등록_되지_않은_주문_테이블_지정);
    }

    @Test
    void 주문_테이블은_비어있으면_안된다() {
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(new OrderLineItemRequest(1L, 1l),
                new OrderLineItemRequest(2L, 1l));
        OrderRequest orderRequest = new OrderRequest(1L, orderLineItems);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(new OrderTable(1L, 1L, 2, true)));

        ThrowingCallable 빈_주문_테이블일_경우 = () -> orderService.create(orderRequest);

        assertThatIllegalArgumentException().isThrownBy(빈_주문_테이블일_경우);
    }

    @Test
    void 주문_목록을_조회할_수_있다() {
        given(orderRepository.findAll()).willReturn(Collections.singletonList(order));

        List<Order> orders = orderService.list();

        assertAll(
                () -> assertThat(orders).hasSize(1),
                () -> assertThat(orders).containsExactly(order)
        );
    }

    @Test
    void 주문_상태를_변경할_수_있다() {
        given(orderRepository.findById(any())).willReturn(Optional.of(order));

        Order saveOrder = orderService.changeOrderStatus(1L, new OrderStatusRequest(OrderStatus.COOKING.name()));

        assertThat(saveOrder).isEqualTo(order);
    }

    @Test
    void 등록_된_주문의_상태만_변경할_수_있다() {
        given(orderRepository.findById(any())).willThrow(IllegalArgumentException.class);

        ThrowingCallable 등록되지_않은_주문의_상태변경 = () -> orderService
                .changeOrderStatus(1L, new OrderStatusRequest(OrderStatus.COOKING.name()));

        assertThatIllegalArgumentException().isThrownBy(등록되지_않은_주문의_상태변경);
    }

    @Test
    void 이미_완료된_주문의_상태는_변경할_수_없다() {
        given(orderRepository.findById(any())).willReturn(Optional.of(order));
        given(order.changeStatus(any())).willThrow(IllegalArgumentException.class);

        ThrowingCallable 이미_완료된_주문의_상태_변경 = () -> orderService
                .changeOrderStatus(1L, new OrderStatusRequest(OrderStatus.COOKING.name()));

        assertThatIllegalArgumentException().isThrownBy(이미_완료된_주문의_상태_변경);
    }
}
