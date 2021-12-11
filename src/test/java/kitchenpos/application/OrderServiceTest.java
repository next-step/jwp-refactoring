package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    private MenuGroup 고기_메뉴그룹;
    private Product 돼지고기;
    private Product 공기밥;
    private Menu 불고기;
    private MenuProduct 불고기_돼지고기;
    private MenuProduct 불고기_공기밥;
    private OrderTable 주문_개인테이블;
    private OrderTable 빈_개인테이블;
    private Order 불고기_주문;
    private Order 불고기_식사중_주문;
    private OrderLineItem 불고기_주문항목;

    @BeforeEach
    void setUp() {
        고기_메뉴그룹 = MenuGroupFixtureFactory.create(1L, "고기 메뉴그룹");
        돼지고기 = ProductFixtureFactory.create(1L, "돼지고기", 9_000);
        공기밥 = ProductFixtureFactory.create(2L, "공기밥", 1_000);
        불고기 = MenuFixtureFactory.create(1L, "불고기", 10_000, 고기_메뉴그룹.getId());

        불고기_돼지고기 = MenuProductFixtureFactory.create(1L, 불고기.getId(), 돼지고기.getId(), 1L);
        불고기_공기밥 = MenuProductFixtureFactory.create(2L, 불고기.getId(), 공기밥.getId(), 1L);
        불고기.setMenuProducts(Arrays.asList(불고기_돼지고기, 불고기_공기밥));

        주문_개인테이블 = OrderTableFixtureFactory.create(1L, false);
        빈_개인테이블 = OrderTableFixtureFactory.create(2L, true);
        불고기_주문 = OrderFixtureFactory.create(1L, 주문_개인테이블.getId(), OrderStatus.COOKING);
        불고기_식사중_주문 = OrderFixtureFactory.create(1L, 주문_개인테이블.getId(), OrderStatus.MEAL);

        불고기_주문항목 = OrderLineItemFixtureFactory.create(1L, 불고기_주문.getId(), 불고기.getId(), 1L);
        불고기_주문.setOrderLineItems(Arrays.asList(불고기_주문항목));
        불고기_식사중_주문.setOrderLineItems(Arrays.asList(불고기_주문항목));
    }

    @DisplayName("Order 를 등록한다.")
    @Test
    void create1() {
        // given
        Order order = new Order();
        order.setOrderTableId(주문_개인테이블.getId());
        order.setOrderLineItems(Arrays.asList(불고기_주문항목));

        given(menuDao.countByIdIn(Arrays.asList(불고기.getId()))).willReturn(1L);
        given(orderTableDao.findById(주문_개인테이블.getId())).willReturn(Optional.ofNullable(주문_개인테이블));
        given(orderDao.save(any(Order.class))).willReturn(불고기_주문);
        given(orderLineItemDao.save(불고기_주문항목)).willReturn(불고기_주문항목);

        // when
        Order savedOrder = orderService.create(order);

        // then
        assertThat(savedOrder).isEqualTo(불고기_주문);
    }

    @DisplayName("Order 를 등록 시, OrderLineItem 이 없으면 예외가 발생한다.")
    @Test
    void create2() {
        // given
        Order order = new Order();
        order.setOrderTableId(주문_개인테이블.getId());
        order.setOrderLineItems(Collections.emptyList());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("Order 를 등록 시, OrderLineItem 의 Menu 가 메뉴에 존재하지 않으면 예외가 발생한다.")
    @Test
    void create3() {
        // given
        Order order = new Order();
        order.setOrderTableId(주문_개인테이블.getId());
        order.setOrderLineItems(Arrays.asList(불고기_주문항목));

        given(menuDao.countByIdIn(Arrays.asList(불고기.getId()))).willReturn(0L);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("Order 를 등록 시, 주문을 한 OrderTable 이 존재하지 않으면 예외가 발생한다.")
    @Test
    void create4() {
        // given
        Order order = new Order();
        order.setOrderTableId(주문_개인테이블.getId());
        order.setOrderLineItems(Arrays.asList(불고기_주문항목));

        given(menuDao.countByIdIn(Arrays.asList(불고기.getId()))).willReturn(1L);
        given(orderTableDao.findById(주문_개인테이블.getId())).willReturn(Optional.ofNullable(주문_개인테이블));
        given(orderTableDao.findById(주문_개인테이블.getId())).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("Order 를 등록 시, OrderTable 이 빈(empty) 상태면 예외가 발생한다.")
    @Test
    void create5() {
        // given
        Order order = new Order();
        order.setOrderTableId(빈_개인테이블.getId());
        order.setOrderLineItems(Arrays.asList(불고기_주문항목));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("Order 목록을 조회할 수 있다.")
    @Test
    void findList() {
        // given
        given(orderDao.findAll()).willReturn(Arrays.asList(불고기_주문));

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders).containsExactly(불고기_주문);
    }

    @DisplayName("Order 의 상태를 변경한다.")
    @Test
    void update1() {
        // given
        불고기_주문.setOrderStatus(OrderStatus.COOKING.name());
        given(orderDao.findById(불고기_주문.getId())).willReturn(Optional.of(불고기_주문));

        // when
        Order changedStatusOrder = orderService.changeOrderStatus(불고기_주문.getId(), 불고기_식사중_주문);

        // then
        assertThat(changedStatusOrder).isEqualTo(불고기_주문);
        assertThat(changedStatusOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("완료된 Order 의 상태를 변경하면 예외가 발생한다.")
    @Test
    void update2() {
        // given
        불고기_주문.setOrderStatus(OrderStatus.COMPLETION.name());
        given(orderDao.findById(불고기_주문.getId())).willReturn(Optional.of(불고기_주문));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(불고기_주문.getId(),
                                                                                             불고기_식사중_주문));
    }
}