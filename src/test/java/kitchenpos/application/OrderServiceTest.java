package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static kitchenpos.fixture.MenuTestFixture.*;
import static kitchenpos.fixture.OrderLineItemTestFixture.createOrderLineItem;
import static kitchenpos.fixture.OrderTableTestFixture.*;
import static kitchenpos.fixture.OrderTestFixture.createOrder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("주문 관련 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderLineItemRepository orderLineItemRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;

    private MenuRequest 짜장면_탕수육_1인_메뉴_세트_요청;
    private MenuRequest 짬뽕_탕수육_1인_메뉴_세트_요청;
    private Menu 짜장면_탕수육_1인_메뉴_세트;
    private Menu 짬뽕_탕수육_1인_메뉴_세트;
    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;
    private OrderLineItem 짜장면_탕수육_1인_메뉴_세트주문;
    private OrderLineItem 짬뽕_탕수육_1인_메뉴_세트주문;
    private OrderRequest 주문1_요청;
    private OrderRequest 주문2_요청;
    private Order 주문1;
    private Order 주문2;

    @BeforeEach
    public void setUp() {
        짜장면_탕수육_1인_메뉴_세트_요청 = 짜장면_탕수육_1인_메뉴_세트_요청();
        짬뽕_탕수육_1인_메뉴_세트_요청 = 짬뽕_탕수육_1인_메뉴_세트_요청();
        짜장면_탕수육_1인_메뉴_세트 = 메뉴_세트_생성(짜장면_탕수육_1인_메뉴_세트_요청);
        짬뽕_탕수육_1인_메뉴_세트 = 메뉴_세트_생성(짬뽕_탕수육_1인_메뉴_세트_요청);
        주문테이블1 = 그룹_없는_주문테이블_생성(주문테이블1_요청());
        주문테이블2 = 그룹_없는_주문테이블_생성(주문테이블2_요청());
        짜장면_탕수육_1인_메뉴_세트주문 = createOrderLineItem(1L, null, 짜장면_탕수육_1인_메뉴_세트.getId(), 1);
        짬뽕_탕수육_1인_메뉴_세트주문 = createOrderLineItem(2L, null, 짬뽕_탕수육_1인_메뉴_세트.getId(), 1);
        주문1_요청 = createOrder(주문테이블1.getId(), null, null, Arrays.asList(짜장면_탕수육_1인_메뉴_세트주문, 짬뽕_탕수육_1인_메뉴_세트주문));
        주문2_요청 = createOrder(주문테이블2.getId(), null, null, singletonList(짜장면_탕수육_1인_메뉴_세트주문));
        주문1 = Order.of(주문테이블1, 주문1_요청.getOrderLineItems());
        주문2 = Order.of(주문테이블2, 주문2_요청.getOrderLineItems());
    }

    @DisplayName("주문 생성 작업을 성공한다.")
    @Test
    void create() {
        // given
        List<Long> menuIds = 주문1.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        when(menuRepository.countByIdIn(menuIds)).thenReturn((long) menuIds.size());
        when(orderTableRepository.findById(주문1.getOrderTable().getId())).thenReturn(Optional.of(주문테이블1));
        when(orderRepository.save(any())).thenReturn(주문1);

        // when
        OrderResponse order = orderService.create(주문1_요청);

        // then
        assertThat(order).isNotNull();
    }

    @DisplayName("주문을 생성할 떄, 주문 항목이 비어있으면 IllegalArgumentException을 반환한다.")
    @Test
    void createWithException1() {
        // given
        OrderRequest order = createOrder(주문테이블1.getId(), null, null, null);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("주문을 생성할 때, 주문 항목 내에 등록되지 않은 메뉴가 있다면 IllegalArgumentException을 반환한다.")
    @Test
    void createWithException2() {
        // given
//        when(menuRepository.countByIdIn(any())).thenReturn(0L);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(주문1_요청));
    }

    @DisplayName("주문을 생성할 때, 주문 테이블이 등록되어 있지 않으면 IllegalArgumentException을 반환한다.")
    @Test
    void createWithException3() {
        // given
        List<Long> menuIds = 주문1.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
//        when(menuRepository.countByIdIn(menuIds)).thenReturn((long) menuIds.size());
        when(orderTableRepository.findById(주문1.getOrderTable().getId())).thenReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(주문1_요청));
    }

    @DisplayName("주문 목록 조회 작업을 성공한다.")
    @Test
    void list() {
        // given
        List<Order> orders = Arrays.asList(주문1, 주문2);
        when(orderRepository.findAll()).thenReturn(orders);

        // when
        List<OrderResponse> findOrders = orderService.list();

        // then
        assertAll(
                () -> assertThat(findOrders).hasSize(orders.size()),
                () -> assertThat(findOrders).containsExactly(OrderResponse.from(주문1), OrderResponse.from(주문2))
        );
    }

    @DisplayName("주문 상태 변경을 성공한다.")
    @Test
    void changeOrderStatus() {
        // given
        주문2.changeOrderStatus(OrderStatus.MEAL.name());
        when(orderRepository.findById(주문2.getId())).thenReturn(Optional.of(주문2));

        // when
        OrderResponse order = orderService.changeOrderStatus(주문2.getId(), 주문2);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(주문2.getOrderStatus());
    }

    @DisplayName("주문 상태를 변경할 때, 주문이 등록되어 있지 않으면 IllegalArgumentException을 반환한다.")
    @Test
    void changeOrderWithException1() {
        // given
        OrderRequest 미등록_주문_요청 = createOrder(10L, null, null, singletonList(짜장면_탕수육_1인_메뉴_세트주문));
        Order 미등록_주문 = Order.of(OrderTable.of(10L, null, 10, false), 미등록_주문_요청.getOrderLineItems());
        when(orderRepository.findById(미등록_주문.getId())).thenReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(미등록_주문.getId(), 미등록_주문));
    }

    @DisplayName("주문 상태를 변경할 때, 주문이 이미 완료되어 있으면 IllegalArgumentException을 반환한다.")
    @Test
    void changeOrderWithException2() {
        // given
        주문2.changeOrderStatus(OrderStatus.COMPLETION.name());
        when(orderRepository.findById(주문2.getId())).thenReturn(Optional.of(주문2));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(주문2.getId(), 주문2));
    }
}
