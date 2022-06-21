package kitchenpos.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
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
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
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

    private Menu 메뉴1;
    private MenuGroup 메뉴그룹1;
    private MenuProduct 메뉴상품1;
    private MenuProduct 메뉴상품2;
    private Product 상품1;
    private Product 상품2;
    private Order 주문1;
    private Order 주문2;
    private OrderTable 주문_테이블1;
    private OrderTable 빈주문_테이블;
    private OrderLineItem 주문항목1;

    @BeforeEach
    void before() {
        메뉴그룹1 = MenuGroupFixtureFactory.create(1L, "메뉴그룹1");
        메뉴1 = MenuFixtureFactory.create(1L, "메뉴1", BigDecimal.valueOf(3000), 메뉴그룹1.getId());

        상품1 = ProductFixtureFactory.create(1L, "상품1", BigDecimal.valueOf(1000));
        상품2 = ProductFixtureFactory.create(2L, "상품2", BigDecimal.valueOf(2000));

        메뉴상품1 = MenuProductFixtureFactory.create(1L, 메뉴1.getId(), 상품1.getId(), 3);
        메뉴상품2 = MenuProductFixtureFactory.create(2L, 메뉴1.getId(), 상품2.getId(), 1);

        메뉴1.setMenuProducts(Arrays.asList(메뉴상품1, 메뉴상품2));

        주문_테이블1 = OrderTableFixtureFactory.create(1L, false);
        빈주문_테이블 = OrderTableFixtureFactory.create(1L, true);

        주문1 = OrderFixtureFactory.create(1L, 주문_테이블1.getId());
        주문2 = OrderFixtureFactory.create(2L, 주문_테이블1.getId());
        주문항목1 = OrderLineItemFixtureFactory.create(1L, 주문1.getId(), 메뉴1.getId(), 1);

        주문1.setOrderLineItems(Arrays.asList(주문항목1));
        주문2.setOrderLineItems(Arrays.asList(주문항목1));
    }

    @Test
    @DisplayName("생성하려는 주문에서 주문 항목이 비어있으면 주문을 생성 할 수 없다.")
    void createFailTest01() {
        //given
        Order order = new Order(1L, 주문_테이블1.getId());
        order.setOrderLineItems(Collections.emptyList());

        //when & then
        assertThatThrownBy(
                () -> orderService.create(order)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("생성하려는 주문에서 주문 항목의 메뉴가 시스템에 등록 되어 있지 않으면 주문을 생성 할 수 없다.")
    void createFailTest02() {

        //given
        Order order = new Order(1L, 주문_테이블1.getId());
        order.setOrderLineItems(Arrays.asList(주문항목1));

        given(menuDao.countByIdIn(anyList())).willReturn(0L);

        //when & then
        assertThatThrownBy(
                () -> orderService.create(order)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("생성하려는 주문에서 주문 테이블이 시스템에 등록 되어 있지 않으면 주문을 생성 할 수 없다.")
    void createFailTest03() {
        //given
        Order order = new Order(1L, 주문_테이블1.getId());
        order.setOrderLineItems(Arrays.asList(주문항목1));

        given(menuDao.countByIdIn(Arrays.asList(메뉴1.getId()))).willReturn(1L);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(
                () -> orderService.create(order)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("생성하려는 주문에서 주문 테이블이 빈주문 테이블이면 생성 할 수 없다.")
    void createFailTest04() {
        //given
        Order order = new Order(1L, 빈주문_테이블.getId());
        order.setOrderLineItems(Arrays.asList(주문항목1));

        given(menuDao.countByIdIn(Arrays.asList(메뉴1.getId()))).willReturn(1L);
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(빈주문_테이블));

        //when & then
        assertThatThrownBy(
                () -> orderService.create(order)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 생성 할 수 있다.")
    void createTest01() {
        //given
        given(menuDao.countByIdIn(Arrays.asList(메뉴1.getId()))).willReturn(1L);
        given(orderTableDao.findById(주문_테이블1.getId())).willReturn(Optional.of(주문_테이블1));
        given(orderDao.save(any(Order.class))).willReturn(주문1);
        given(orderLineItemDao.save(any(OrderLineItem.class))).willReturn(주문항목1);

        //when
        Order order = orderService.create(주문1);

        //then
        assertThat(order).isEqualTo(주문1);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.toString());
    }

    @Test
    @DisplayName("주문 목록을 조회 할 수 있다.")
    void listTest01() {
        //given
        given(orderDao.findAll()).willReturn(Arrays.asList(주문1));

        //when
        List<Order> orders = orderService.list();

        //then
        assertThat(orders).containsExactly(주문1);
    }

    @Test
    @DisplayName("주문이 시스템에 등록 되어 있지 않으면 변경 할 수 없다.")
    void changeOrderStatusFailTest01() {
        //given
        given(orderDao.findById(주문1.getId())).willThrow(IllegalArgumentException.class);

        //when & then
        assertThatThrownBy(
                () -> orderService.changeOrderStatus(주문1.getId(), 주문1)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문이 완료 상태이면 변경 할 수 없다.")
    void changeOrderStatusFailTest02() {
        //given
        주문1.setOrderStatus(String.valueOf(OrderStatus.COMPLETION));
        given(orderDao.findById(주문1.getId())).willReturn(Optional.of(주문1));

        //when & then
        assertThatThrownBy(
                () -> orderService.changeOrderStatus(주문1.getId(), 주문2)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문의 상태를 변경 할 수 있다.")
    void changeOrderStatusTest01() {

        //given
        주문1.setOrderStatus(String.valueOf(OrderStatus.COOKING));
        주문2.setOrderStatus(String.valueOf(OrderStatus.MEAL));
        given(orderDao.findById(주문1.getId())).willReturn(Optional.of(주문1));

        //when
        Order changedOrder = orderService.changeOrderStatus(주문1.getId(), 주문2);

        //then
        assertThat(changedOrder).isEqualTo(주문1);
    }
}
