package kitchenpos.order.application;

import kitchenpos.fixture.*;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderValidator orderValidator;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private TableService tableService;
    @Mock
    private MenuService menuService;

    @InjectMocks
    private OrderService orderService;

    private Menu 후라이드두마리세트;
    private OrderTable 테이블1번;
    private OrderLineItem 후라이드두마리세트_2개_주문함;
    private OrderLineItemRequest 후라이드두마리세트_2개_주문_Request;
    private Order 총주문;
    private OrderRequest 총주문Request;

    @BeforeEach
    void setUp() {
        후라이드두마리세트 = MenuFixture.생성("후라이드두마리세트", new BigDecimal("10000"), MenuGroupFixture.치킨류());
        후라이드두마리세트.addMenuProducts(Arrays.asList(MenuProductFixture.후라이드두마리()));

        테이블1번 = OrderTableFixture.생성(0,true);

        후라이드두마리세트_2개_주문함 = OrderLineItemFixture.생성(후라이드두마리세트.getId(), 2L);

        총주문 = OrderFixture.생성(1L);
        총주문.addLineItems(Arrays.asList(후라이드두마리세트_2개_주문함));

        후라이드두마리세트_2개_주문_Request = OrderLineItemRequestFixture.생성_Request(1L, 2L);
        총주문Request = OrderRequestFixture.request생성(1L,Arrays.asList(후라이드두마리세트_2개_주문_Request));
    }

    @DisplayName("주문 생성")
    @Test
    void create() {
        given(tableService.findOrderTable(any())).willReturn(테이블1번);
        given(orderRepository.save(any())).willReturn(총주문);
        given(menuService.findById(any())).willReturn(후라이드두마리세트);
        OrderResponse createOrder = orderService.create(총주문Request);

        assertAll(
                () -> assertThat(createOrder).isNotNull(),
                () -> assertThat(createOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(createOrder.getOrderLineItems().get(0).getQuantity()).isEqualTo(2)
        );

    }

    @DisplayName("주문 목록 조회")
    @Test
    void list() {
        given(orderRepository.findAll()).willReturn(Arrays.asList(총주문));

        List<OrderResponse> orders = orderService.list();

        assertThat(orders.size()).isEqualTo(1);
    }

    @DisplayName("주문 상태를 식사로 변경 할 수 있다.")
    @Test
    void changeMealStatus() {
        Order 주문 = OrderFixture.생성(테이블1번);
        Order 식사_상태_주문 = OrderFixture.생성(테이블1번);
        식사_상태_주문.updateOrderStatus(OrderStatus.MEAL);
        given(orderRepository.findById(any())).willReturn(java.util.Optional.ofNullable(주문));

        orderService.changeOrderStatus(2L, 식사_상태_주문);

        assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("주문 상태를 계산 완료로 변경 할 수 있다.")
    @Test
    void changeCompletionStatus() {
        Order 주문 = OrderFixture.생성(테이블1번);
        Order 계산_완료_주문 = OrderFixture.생성(테이블1번);
        계산_완료_주문.updateOrderStatus(OrderStatus.COMPLETION);
        given(orderRepository.findById(any())).willReturn(java.util.Optional.ofNullable(주문));

        orderService.changeOrderStatus(2L, 계산_완료_주문);

        assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @DisplayName("주문 완료 상태가 아닌 주문만 변경 가능하다.")
    @Test
    void changeStatusError() {
        Order 주문 = OrderFixture.생성(테이블1번);

        주문.updateOrderStatus(OrderStatus.COMPLETION);
        Order 계산_완료_주문 = OrderFixture.생성(테이블1번);
        계산_완료_주문.updateOrderStatus(OrderStatus.COMPLETION);
        given(orderRepository.findById(any())).willReturn(java.util.Optional.ofNullable(주문));

        assertThatThrownBy(
                () -> orderService.changeOrderStatus(2L, 계산_완료_주문)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
