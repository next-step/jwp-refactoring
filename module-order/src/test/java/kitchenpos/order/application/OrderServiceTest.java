package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.mapper.OrderMapper;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.validator.OrderLineItemsSizeAndMenuCountValidator;
import kitchenpos.order.validator.OrderValidatorsImpl;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.ordertable.validator.OrderTableExistAndEmptyValidator;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private OrderService orderService;
    @Mock
    private OrderValidatorsImpl orderValidatorImpl;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderLineItemRepository orderLineItemRepository;
    @Mock
    private Order order = new Order(1L,
            Collections.singletonList(new OrderLineItem(1L, 1l, "메뉴명", new BigDecimal(16000))));
    @Mock
    private Menu menu;
    private OrderMapper orderMapper;
    private OrderValidatorsImpl orderValidators;

    @BeforeEach
    void setUp() {
        orderMapper = new OrderMapper(menuRepository);
        orderValidators = new OrderValidatorsImpl(new OrderLineItemsSizeAndMenuCountValidator(menuRepository),
                new OrderTableExistAndEmptyValidator(orderTableRepository));
        orderService = new OrderService(orderRepository, orderLineItemRepository, orderValidatorImpl, orderMapper);
    }

    @Test
    void 주문을_등록할_수_있다() {
        List<OrderLineItemRequest> orderLineItems = Arrays
                .asList(new OrderLineItemRequest(1L, 1l),
                        new OrderLineItemRequest(2L, 1l));
        OrderRequest orderRequest = new OrderRequest(1L, orderLineItems);
        BDDMockito.given(menuRepository.findAllById(ArgumentMatchers.any()))
                .willReturn(Collections.singletonList(menu));
        BDDMockito.given(menu.getId()).willReturn(1L);
        BDDMockito.given(menu.getName()).willReturn("메뉴");
        BDDMockito.given(menu.getPrice()).willReturn(new BigDecimal(16000));
        BDDMockito.given(orderRepository.save(ArgumentMatchers.any())).willReturn(order);

        Order order = orderService.create(orderRequest);

        assertThat(order).isEqualTo(order);
    }

    @Test
    void 수량이_남은_메뉴만_주문할_수_있다() {
        OrderRequest orderRequest = new OrderRequest(1L, Collections.emptyList());

        ThrowingCallable 수량이_남지_않은_메뉴_주문시도 = () -> orderService.create(orderRequest);

        Assertions.assertThatIllegalArgumentException().isThrownBy(수량이_남지_않은_메뉴_주문시도);
    }

    @Test
    void 등록_된_메뉴만_지정할_수_있다() {
        List<OrderLineItemRequest> orderLineItems = Arrays
                .asList(new OrderLineItemRequest(1L, 1l),
                        new OrderLineItemRequest(2L, 1l));
        OrderRequest orderRequest = new OrderRequest(1L, orderLineItems);
        BDDMockito.given(menuRepository.findAllById(ArgumentMatchers.any()))
                .willReturn(Collections.singletonList(menu));
        BDDMockito.given(menu.getId()).willReturn(1L);
        BDDMockito.given(menu.getName()).willReturn("메뉴");
        BDDMockito.given(menu.getPrice()).willReturn(new BigDecimal(16000));
        BDDMockito.given(orderTableRepository.findById(ArgumentMatchers.any()))
                .willReturn(Optional.of(new OrderTable(1, false)));
        orderService = new OrderService(orderRepository, orderLineItemRepository, orderValidators, orderMapper);

        ThrowingCallable 없는_메뉴가_포함된_주문시도 = () -> orderService.create(orderRequest);

        Assertions.assertThatIllegalArgumentException().isThrownBy(없는_메뉴가_포함된_주문시도);
    }

    @Test
    void 등록_된_주문_테이블만_지정할_수_있다() {
        List<OrderLineItemRequest> orderLineItems = Arrays
                .asList(new OrderLineItemRequest(1L, 1l),
                        new OrderLineItemRequest(2L, 1l));
        OrderRequest orderRequest = new OrderRequest(1L, orderLineItems);
        BDDMockito.given(menuRepository.findAllById(ArgumentMatchers.any()))
                .willReturn(Collections.singletonList(menu));
        BDDMockito.given(menu.getId()).willReturn(1L);
        BDDMockito.given(menu.getName()).willReturn("메뉴");
        BDDMockito.given(menu.getPrice()).willReturn(new BigDecimal(16000));
        BDDMockito.given(orderTableRepository.findById(ArgumentMatchers.any()))
                .willThrow(IllegalArgumentException.class);
        orderService = new OrderService(orderRepository, orderLineItemRepository, orderValidators, orderMapper);

        ThrowingCallable 등록_되지_않은_주문_테이블_지정 = () -> orderService.create(orderRequest);

        Assertions.assertThatIllegalArgumentException().isThrownBy(등록_되지_않은_주문_테이블_지정);
    }

    @Test
    void 주문_테이블은_비어있으면_안된다() {
        List<OrderLineItemRequest> orderLineItems = Arrays
                .asList(new OrderLineItemRequest(1L, 1l),
                        new OrderLineItemRequest(2L, 1l));
        OrderRequest orderRequest = new OrderRequest(1L, orderLineItems);
        BDDMockito.given(menuRepository.findAllById(ArgumentMatchers.any()))
                .willReturn(Collections.singletonList(menu));
        BDDMockito.given(menu.getId()).willReturn(1L);
        BDDMockito.given(menu.getName()).willReturn("메뉴");
        BDDMockito.given(menu.getPrice()).willReturn(new BigDecimal(16000));
        BDDMockito.given(orderTableRepository.findById(ArgumentMatchers.any()))
                .willReturn(Optional.of(new OrderTable(1, true)));
        orderService = new OrderService(orderRepository, orderLineItemRepository, orderValidators, orderMapper);

        ThrowingCallable 빈_주문_테이블일_경우 = () -> orderService.create(orderRequest);

        Assertions.assertThatIllegalArgumentException().isThrownBy(빈_주문_테이블일_경우);
    }

    @Test
    void 주문_목록을_조회할_수_있다() {
        BDDMockito.given(orderRepository.findAllWithOrderLineItems()).willReturn(Collections.singletonList(order));

        List<Order> orders = orderService.list();

        assertAll(
                () -> Assertions.assertThat(orders).hasSize(1),
                () -> Assertions.assertThat(orders).containsExactly(order)
        );
    }

    @Test
    void 주문_상태를_변경할_수_있다() {
        BDDMockito.given(orderRepository.findById(ArgumentMatchers.any())).willReturn(Optional.of(order));

        Order saveOrder = orderService.changeOrderStatus(1L, new OrderStatusRequest(OrderStatus.COOKING.name()));

        assertThat(saveOrder).isEqualTo(order);
    }

    @Test
    void 등록_된_주문의_상태만_변경할_수_있다() {
        BDDMockito.given(orderRepository.findById(ArgumentMatchers.any())).willThrow(IllegalArgumentException.class);

        ThrowingCallable 등록되지_않은_주문의_상태변경 = () -> orderService
                .changeOrderStatus(1L, new OrderStatusRequest(OrderStatus.COOKING.name()));

        Assertions.assertThatIllegalArgumentException().isThrownBy(등록되지_않은_주문의_상태변경);
    }

    @Test
    void 이미_완료된_주문의_상태는_변경할_수_없다() {
        BDDMockito.given(orderRepository.findById(ArgumentMatchers.any())).willReturn(Optional.of(order));
        Mockito.doThrow(IllegalArgumentException.class).when(order).changeStatus(ArgumentMatchers.any());

        ThrowingCallable 이미_완료된_주문의_상태_변경 = () -> orderService
                .changeOrderStatus(1L, new OrderStatusRequest(OrderStatus.COOKING.name()));

        Assertions.assertThatIllegalArgumentException().isThrownBy(이미_완료된_주문의_상태_변경);
    }
}
