package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.validator.OrderValidator;
import kitchenpos.ordertable.domain.OrderTable;
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

import static java.util.Collections.singletonList;
import static kitchenpos.menu.fixture.MenuTestFixture.*;
import static kitchenpos.order.fixture.OrderLineItemTestFixture.주문정보목록;
import static kitchenpos.order.fixture.OrderLineItemTestFixture.주문정보요청;
import static kitchenpos.order.fixture.OrderTestFixture.주문;
import static kitchenpos.ordertable.fixture.OrderTableTestFixture.*;
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
    private OrderValidator orderValidator;

    @InjectMocks
    private OrderService orderService;

    private MenuRequest 짜장면_탕수육_1인_메뉴_세트_요청;
    private MenuRequest 짬뽕_탕수육_1인_메뉴_세트_요청;
    private Menu 짜장면_탕수육_1인_메뉴_세트;
    private Menu 짬뽕_탕수육_1인_메뉴_세트;
    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;
    private OrderLineItemRequest 짜장면_탕수육_1인_메뉴_세트주문;
    private OrderLineItemRequest 짬뽕_탕수육_1인_메뉴_세트주문;
    private OrderRequest 주문1_요청;
    private OrderRequest 주문2_요청;
    private Order 주문1;
    private Order 주문2;

    @BeforeEach
    public void setUp() {
        짜장면_탕수육_1인_메뉴_세트_요청 = 짜장면_탕수육_1인_메뉴_세트_요청();
        짬뽕_탕수육_1인_메뉴_세트_요청 = 짬뽕_탕수육_1인_메뉴_세트_요청();
        짜장면_탕수육_1인_메뉴_세트 = 메뉴세트(짜장면_탕수육_1인_메뉴_세트_요청, 1L);
        짬뽕_탕수육_1인_메뉴_세트 = 메뉴세트(짬뽕_탕수육_1인_메뉴_세트_요청, 2L);
        주문테이블1 = 그룹_없는_주문테이블_생성(주문테이블1_요청());
        주문테이블2 = 그룹_없는_주문테이블_생성(주문테이블2_요청());
        짜장면_탕수육_1인_메뉴_세트주문 = 주문정보요청(1L, 1);
        짬뽕_탕수육_1인_메뉴_세트주문 = 주문정보요청(2L, 1);
        주문1_요청 = 주문(주문테이블1.getId(), null, null, Arrays.asList(짜장면_탕수육_1인_메뉴_세트주문, 짬뽕_탕수육_1인_메뉴_세트주문));
        주문2_요청 = 주문(주문테이블2.getId(), null, null, singletonList(짜장면_탕수육_1인_메뉴_세트주문));
        주문1 = Order.of(1L, 주문정보목록(주문1_요청.getOrderLineItemsRequest()));
        주문2 = Order.of(2L, 주문정보목록(주문2_요청.getOrderLineItemsRequest()));
    }

    @DisplayName("주문 생성 작업을 성공한다.")
    @Test
    void create() {
        // given
        when(orderRepository.save(any())).thenReturn(주문1);

        // when
        OrderResponse order = orderService.create(주문1_요청);

        // then
        assertThat(order).isNotNull();
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
        주문2.changeOrderStatus(OrderStatus.MEAL);
        when(orderRepository.findById(주문2.getId())).thenReturn(Optional.of(주문2));

        // when
        OrderResponse order = orderService.changeOrderStatus(주문2.getId(), 주문2_요청);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(주문2.getOrderStatus());
    }

    @DisplayName("주문 상태를 변경할 때, 주문이 등록되어 있지 않으면 IllegalArgumentException을 반환한다.")
    @Test
    void changeOrderWithException1() {
        // given
        OrderRequest 미등록_주문_요청 = 주문(null, null, null, singletonList(짜장면_탕수육_1인_메뉴_세트주문));
        Order 미등록_주문 = Order.of(1L, 주문정보목록(미등록_주문_요청.getOrderLineItemsRequest()));
        when(orderRepository.findById(미등록_주문.getId())).thenReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(미등록_주문.getId(), 미등록_주문_요청));
    }

    @DisplayName("주문 상태를 변경할 때, 주문이 이미 완료되어 있으면 IllegalArgumentException을 반환한다.")
    @Test
    void changeOrderWithException2() {
        // given
        when(orderRepository.findById(주문2.getId())).thenReturn(Optional.of(주문2));
        주문2.changeOrderStatus(OrderStatus.COMPLETION);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(주문2.getId(), 주문2_요청));
    }
}
