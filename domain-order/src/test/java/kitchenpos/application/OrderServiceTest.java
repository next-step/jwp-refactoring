package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.ProductFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.ThrowingCallable;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    MenuService menuService;

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderService orderService;

    OrderTable 빈_테이블;
    OrderTable 주문_테이블;
    OrderLineItem 주문_상품;

    Menu 후라이드_후라이드;
    OrderLineItemRequest 주문_항목_요청;
    OrderRequest 주문_요청;
    Order 주문;
    OrderResponse 주문_응답;

    @BeforeEach
    void setUp() {
        Product 후라이드치킨 = ProductFixture.of("후라이드치킨", BigDecimal.valueOf(16000));
        MenuGroup 두마리치킨 = MenuGroupFixture.from("두마리치킨");
        MenuProduct 후라이드_메뉴_상품 = MenuProductFixture.of(후라이드치킨.getId(), 2);
        후라이드_후라이드 = MenuFixture.of(
                "후라이드+후라이드",
                BigDecimal.valueOf(31000),
                두마리치킨,
                MenuProducts.from(Collections.singletonList(후라이드_메뉴_상품)));

        주문_테이블 = OrderTableFixture.of(4, false);
        빈_테이블 = OrderTableFixture.of(0, true);
        주문_상품 = OrderLineItemFixture.of(후라이드_후라이드, 1L);

        주문_항목_요청 = OrderLineItemRequest.of(후라이드_후라이드.getId(), 1L);
        주문_요청 = OrderRequest.of(1L, Arrays.asList(주문_항목_요청));
        주문 = Order.of(1L, OrderLineItems.from(Collections.singletonList(주문_상품)));
        주문_응답 = OrderResponse.from(주문);
    }

    @Test
    void 주문_발생() {
        // given
        List<Long> menuIds = 주문_요청.getOrderLineItems()
                .stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        BDDMockito.given(menuService.countByIdIn(menuIds)).willReturn(1L);
        BDDMockito.given(menuService.findMenuById(ArgumentMatchers.any())).willReturn(후라이드_후라이드);
        BDDMockito.given(orderRepository.save(ArgumentMatchers.any())).willReturn(주문);

        // when
        OrderResponse actual = orderService.create(주문_요청);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    void 주문_발생_시_주문_상품은_반드시_메뉴에_존재해야_한다() {
        // given
        BDDMockito.given(menuService.countByIdIn(ArgumentMatchers.any())).willReturn(0L);

        // when
        ThrowingCallable throwingCallable = () -> orderService.create(주문_요청);

        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    void 주문_조회() {
        // given
        List<Order> orders = Collections.singletonList(주문);
        BDDMockito.given(orderRepository.findAll()).willReturn(orders);

        // when
        List<OrderResponse> actual = orderService.list();

        // then
        assertThat(actual).hasSize(1);
    }

    @Test
    void 주문_상태_변경() {
        // given
        OrderRequest 주문_상태_변경_요청 = OrderRequest.from(OrderStatus.MEAL);
        BDDMockito.given(orderRepository.findById(주문.getId())).willReturn(Optional.of(주문));

        // when
        OrderResponse actual = orderService.changeOrderStatus(주문.getId(), 주문_상태_변경_요청);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(주문.getOrderStatus().name());
    }

    @Test
    void 주문_상태_변경_시_주문이_반드시_존재해야_한다() {
        // given
        OrderRequest 주문_상태_변경_요청 = OrderRequest.from(OrderStatus.MEAL);
        BDDMockito.given(orderRepository.findById(주문.getId())).willReturn(Optional.empty());

        // when
        ThrowingCallable throwingCallable = () -> orderService.changeOrderStatus(주문.getId(), 주문_상태_변경_요청);

        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    void 주문_상태_변경_시_상태가_계산이면_변경할_수_없다() {
        // given
        OrderRequest 주문_상태_변경_요청 = OrderRequest.from(OrderStatus.MEAL);
        주문.changeOrderStatus(OrderStatus.COMPLETION);
        BDDMockito.given(orderRepository.findById(주문.getId())).willReturn(Optional.of(주문));

        // when
        ThrowingCallable throwingCallable = () -> orderService.changeOrderStatus(주문.getId(), 주문_상태_변경_요청);

        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }
}
