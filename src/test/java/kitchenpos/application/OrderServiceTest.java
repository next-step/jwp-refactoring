package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.fixture.MenuFixtureFactory;
import kitchenpos.application.fixture.MenuGroupFixtureFactory;
import kitchenpos.application.fixture.MenuProductFixtureFactory;
import kitchenpos.application.fixture.OrderFixtureFactory;
import kitchenpos.application.fixture.OrderLineItemFixtureFactory;
import kitchenpos.application.fixture.OrderTableFixtureFactory;
import kitchenpos.application.fixture.ProductFixtureFactory;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.product.domain.Product;
import org.assertj.core.util.Lists;
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
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;


    private MenuGroup 초밥_메뉴그룹;
    private Product 우아한_초밥_1;
    private Product 우아한_초밥_2;
    private MenuProduct A_우아한_초밥_1;
    private MenuProduct A_우아한_초밥_2;
    private Menu A;
    private OrderTable 주문_테이블;
    private OrderTable 빈_테이블;
    private Order A_주문;
    private Order A_주문_식사중;
    private OrderLineItem A_주문항목;

    @BeforeEach
    void setUp() {
        초밥_메뉴그룹 = MenuGroupFixtureFactory.create(1L, "초밥_메뉴그룹");
        우아한_초밥_1 = ProductFixtureFactory.create(1L, "우아한_초밥_1", BigDecimal.valueOf(10_000));
        우아한_초밥_2 = ProductFixtureFactory.create(2L, "우아한_초밥_2", BigDecimal.valueOf(20_000));
        A = MenuFixtureFactory.create("A", BigDecimal.valueOf(30_000), 초밥_메뉴그룹.getId());

        A_우아한_초밥_1 = MenuProductFixtureFactory.create(1L, A.getId(), 우아한_초밥_1.getId(), 1);
        A_우아한_초밥_2 = MenuProductFixtureFactory.create(2L, A.getId(), 우아한_초밥_2.getId(), 2);

        A.setMenuProducts(Lists.newArrayList(A_우아한_초밥_1, A_우아한_초밥_2));

        주문_테이블 = OrderTableFixtureFactory.create(1L, false);
        빈_테이블 = OrderTableFixtureFactory.create(2L, true);

        A_주문 = OrderFixtureFactory.create(1L, 주문_테이블.getId(), OrderStatus.COOKING);
        A_주문_식사중 = OrderFixtureFactory.create(2L, 주문_테이블.getId(), OrderStatus.MEAL);

        A_주문항목 = OrderLineItemFixtureFactory.create(1L, A_주문.getId(), A.getId(), 1);
        A_주문.setOrderLineItems(Lists.newArrayList(A_주문항목));
        A_주문_식사중.setOrderLineItems(Lists.newArrayList(A_주문항목));
    }

    @DisplayName("주문을 할 수 있다.")
    @Test
    void create01() {
        // given
        Order order = new Order();
        order.setId(1L);
        order.setOrderTableId(주문_테이블.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(Lists.newArrayList(A_주문항목));

        given(menuDao.countByIdIn(Lists.newArrayList(A.getId()))).willReturn(1L);
        given(orderTableDao.findById(주문_테이블.getId())).willReturn(Optional.ofNullable(주문_테이블));
        given(orderDao.save(any(Order.class))).willReturn(A_주문);
        given(orderLineItemDao.save(any(OrderLineItem.class))).willReturn(A_주문항목);

        // when
        Order createdOrder = orderService.create(order);

        // then
        assertThat(createdOrder).isEqualTo(A_주문);
    }

    @DisplayName("주문항목은 1건 이상이어야 한다.")
    @Test
    void create02() {
        // given
        Order order = new Order();
        order.setId(1L);
        order.setOrderTableId(주문_테이블.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(Collections.emptyList());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("주문항목에 메뉴가 존재하지 않으면 주문을 등록할 수 없다.")
    @Test
    void create03() {
        // given
        Order order = new Order();
        order.setOrderTableId(주문_테이블.getId());
        order.setOrderLineItems(Lists.newArrayList(A_주문항목));

        given(menuDao.countByIdIn(Lists.newArrayList(A.getId()))).willReturn(0L);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("주문 테이블이 없이는 주문을 등록할 수 없다.")
    @Test
    void create04() {
        // given
        Order order = new Order();
        order.setOrderTableId(주문_테이블.getId());
        order.setOrderLineItems(Lists.newArrayList(A_주문항목));

        given(menuDao.countByIdIn(Lists.newArrayList(A.getId()))).willReturn(1L);
        given(orderTableDao.findById(주문_테이블.getId())).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void find01() {
        // given
        given(orderDao.findAll()).willReturn(Lists.newArrayList(A_주문));

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders).containsExactly(A_주문);
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void change01() {
        // given
        A_주문.setOrderStatus(OrderStatus.COOKING.name());
        given(orderDao.findById(A_주문.getId())).willReturn(Optional.of(A_주문));

        // when
        Order changedOrder = orderService.changeOrderStatus(A_주문.getId(), A_주문_식사중);

        // then
        assertAll(
                () -> assertThat(changedOrder).isEqualTo(A_주문),
                () -> assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name())
        );
    }

    @DisplayName("주문 상태가 \"COMPLETION\"인 경우 주문 상태를 변경할 수 없다.")
    @Test
    void change02() {
        // given
        A_주문.setOrderStatus(OrderStatus.COMPLETION.name());
        given(orderDao.findById(A_주문.getId())).willReturn(Optional.of(A_주문));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(A_주문.getId(), A_주문_식사중));
    }

}