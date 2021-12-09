package kitchenpos.application;

import static kitchenpos.application.fixture.MenuFixture.*;
import static kitchenpos.application.fixture.OrderFixture.*;
import static kitchenpos.application.fixture.OrderTableFixture.*;
import static kitchenpos.application.fixture.ProductFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import kitchenpos.application.fixture.OrderFixture;
import kitchenpos.application.fixture.OrderTableFixture;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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

    @BeforeEach
    void setUp() {
        when(menuDao.countByIdIn(Arrays.asList(불고기.getId()))).thenReturn(1L);
        when(orderTableDao.findById(주문_개인테이블.getId())).thenReturn(Optional.ofNullable(주문_개인테이블));
        when(orderDao.save(any(Order.class))).thenReturn(불고기_주문);
        when(orderLineItemDao.save(불고기_주문항목)).thenReturn(불고기_주문항목);
    }

    @AfterEach
    void setDown() {
        OrderFixture.init();
    }

    @DisplayName("Order 를 등록한다.")
    @Test
    void create1() {
        // given
        Order order = new Order();
        order.setOrderTableId(주문_개인테이블.getId());
        order.setOrderLineItems(Arrays.asList(불고기_주문항목));

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

        when(menuDao.countByIdIn(Arrays.asList(불고기.getId()))).thenReturn(0L);

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

        when(orderTableDao.findById(주문_개인테이블.getId())).thenReturn(Optional.empty());

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
        when(orderDao.findAll()).thenReturn(Arrays.asList(불고기_주문));

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
        when(orderDao.findById(불고기_주문.getId())).thenReturn(Optional.of(불고기_주문));

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
        when(orderDao.findById(불고기_주문.getId())).thenReturn(Optional.of(불고기_주문));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(불고기_주문.getId(),
                                                                                             불고기_식사중_주문));
    }
}