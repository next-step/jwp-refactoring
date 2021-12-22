package kitchenpos.application;

import kitchenpos.common.fixtrue.MenuFixture;
import kitchenpos.common.fixtrue.MenuGroupFixture;
import kitchenpos.common.fixtrue.MenuProductFixture;
import kitchenpos.common.fixtrue.OrderFixture;
import kitchenpos.common.fixtrue.OrderLineItemFixture;
import kitchenpos.common.fixtrue.OrderTableFixture;
import kitchenpos.common.fixtrue.ProductFixture;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    MenuDao menuDao;

    @Mock
    OrderDao orderDao;

    @Mock
    OrderLineItemDao orderLineItemDao;

    @Mock
    OrderTableDao orderTableDao;

    @InjectMocks
    OrderService orderService;

    Order 주문;
    OrderTable 주문_테이블;
    OrderLineItem 주문_상품;

    @BeforeEach
    void setUp() {
        Product 후라이드치킨 = ProductFixture.of(1L, "후라이드치킨", BigDecimal.valueOf(16000));
        MenuGroup 두마리치킨 = MenuGroupFixture.of(1L, "두마리치킨");
        Menu 후라이드_후라이드 = MenuFixture.of(
                1L,
                "후라이드+후라이드",
                BigDecimal.valueOf(31000),
                두마리치킨.getId(),
                MenuProductFixture.of(1L, 1L, 후라이드치킨.getId(), 2));

        주문_테이블 = OrderTableFixture.of(1L, null, 4, false);
        주문_상품 = OrderLineItemFixture.of(1L, 1L, 후라이드_후라이드.getId(), 1L);
        주문 = OrderFixture.of(
                1L,
                주문_테이블.getId(),
                OrderStatus.COOKING.name(),
                주문_상품);
    }

    @Test
    void 주문_발생() {
        // given
        List<Long> menuIds = 주문.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        given(menuDao.countByIdIn(menuIds)).willReturn(1L);
        given(orderTableDao.findById(주문.getOrderTableId())).willReturn(Optional.of(주문_테이블));
        given(orderDao.save(any())).willReturn(주문);

        // when
        Order actual = orderService.create(주문);

        // then
        assertAll(() -> {
            assertThat(actual).isEqualTo(주문);
            assertThat(actual.getOrderStatus()).isEqualTo(주문.getOrderStatus());
        });
    }

    @Test
    void 주문_발생_시_주문_상품은_반드시_존재해야_한다() {
        // given
        주문.setOrderLineItems(new ArrayList<>());

        // when
        ThrowingCallable throwingCallable = () -> orderService.create(주문);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    void 주문_발생_시_주문_상품은_반드시_메뉴에_존재해야_한다() {
        // given
        given(menuDao.countByIdIn(any())).willReturn(0L);

        // when
        ThrowingCallable throwingCallable = () -> orderService.create(주문);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    void 주문_발생_시_주문_테이블이_존재해야_한다() {
        // given
        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(orderTableDao.findById(주문.getOrderTableId())).willReturn(Optional.empty());

        // when
        ThrowingCallable throwingCallable = () -> orderService.create(주문);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    void 주문_발생_시_주문_테이블이_빈_테이블이면_주문할_수_없다() {
        // given
        given(menuDao.countByIdIn(any())).willReturn(1L);
        주문_테이블.setEmpty(true);
        given(orderTableDao.findById(주문.getOrderTableId())).willReturn(Optional.of(주문_테이블));

        // when
        ThrowingCallable throwingCallable = () -> orderService.create(주문);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    void 주문_조회() {
        // given
        List<Order> orders = Collections.singletonList(주문);
        given(orderDao.findAll()).willReturn(orders);
        given(orderLineItemDao.findAllByOrderId(주문.getId())).willReturn(Collections.singletonList(주문_상품));

        // when
        List<Order> actual = orderService.list();

        // then
        assertAll(() -> {
            assertThat(actual).hasSize(1);
            assertThat(actual).containsExactlyElementsOf(Collections.singletonList(주문));
        });
    }

    @Test
    void 주문_상태_변경() {
        // given
        주문.setOrderStatus(OrderStatus.MEAL.name());
        given(orderDao.findById(주문.getId())).willReturn(Optional.of(주문));
        given(orderDao.save(any())).willReturn(주문);

        // when
        Order actual = orderService.changeOrderStatus(주문.getId(), 주문);

        // then
        assertAll(() -> {
            assertThat(actual).isEqualTo(주문);
            assertThat(actual.getOrderStatus()).isEqualTo(주문.getOrderStatus());
        });
    }

    @Test
    void 주문_상태_변경_시_주문이_반드시_존재해야_한다() {
        // given
        given(orderDao.findById(주문.getId())).willReturn(Optional.empty());

        // when
        ThrowingCallable throwingCallable = () -> orderService.changeOrderStatus(주문.getId(), 주문);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    void 주문_상태_변경_시_상태가_계산이면_변경할_수_없다() {
        // given
        주문.setOrderStatus(OrderStatus.COMPLETION.name());
        given(orderDao.findById(주문.getId())).willReturn(Optional.of(주문));

        // when
        ThrowingCallable throwingCallable = () -> orderService.changeOrderStatus(주문.getId(), 주문);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }
}
