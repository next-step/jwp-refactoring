package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 관리")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    private OrderService orderService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    private List<OrderLineItem> orderLineItems;
    private OrderLineItem orderLineItem1;
    private OrderLineItem orderLineItem2;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
        orderLineItem1 = new OrderLineItem();
        orderLineItem1.setMenuId(1L);
        orderLineItem1.setQuantity(2);
        orderLineItem2 = new OrderLineItem();
        orderLineItem2.setMenuId(2L);
        orderLineItem2.setQuantity(3);
        orderLineItems = Lists.list(orderLineItem1, orderLineItem2);
        orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setEmpty(false);
    }

    @DisplayName("주문을 추가한다.")
    @Test
    void create() {
        //given
        Order order = new Order();
        order.setOrderLineItems(orderLineItems);

        //and
        given(menuDao.countByIdIn(any())).willReturn(2L);
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
        given(orderDao.save(any())).willReturn(order);

        //when
        Order actual = orderService.create(order);

        //then
        assertThat(actual).isEqualTo(order);
    }

    @DisplayName("주문은 메뉴를 지정해야한다.")
    @Test
    void createOrderExceptionIfMenuIsNull() {
        //given
        Order order = new Order();
        order.setOrderLineItems(orderLineItems);

        //and
        given(menuDao.countByIdIn(any())).willReturn(0L); //메뉴를 지정하지 않음

        //when
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class); //then
    }

    @DisplayName("주문은 메뉴 수량을 지정해야한다.")
    @Test
    void createOrderExceptionIfMenuQuantityIsNull() {
        //TODO: 추가 기능 개발
    }

    @DisplayName("주문은 주문 테이블을 지정해야한다.")
    @Test
    void createOrderExceptionIfOrderTableIsNull() {
        //given
        Order order = new Order();
        order.setOrderLineItems(orderLineItems);
        orderTable.setEmpty(true);

        //and
        given(menuDao.countByIdIn(any())).willReturn(2L);
        given(orderTableDao.findById(any())).willReturn(Optional.empty()); // 주문테이블 없음

        //when
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class); //then
    }

    @DisplayName("주문 불가능 상태일 경우 주문할 수 없다.")
    @Test
    void createOrderExceptionIfTableEmptyIsTrue() {
        //given
        Order order = new Order();
        order.setOrderLineItems(orderLineItems);
        orderTable.setEmpty(true);

        //and
        given(menuDao.countByIdIn(any())).willReturn(2L);
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));

        //when
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class); //then
    }

    @DisplayName("주문을 모두 조회한다.")
    @Test
    void list() {
        //given
        Order order1 = new Order();
        order1.setId(1L);
        Order order2 = new Order();
        order2.setId(2L);
        List<Order> orders = Lists.list(order1, order2);

        //and
        given(orderDao.findAll()).willReturn(orders);

        //when
        List<Order> actual = orderService.list();

        //then
        assertThat(actual).isEqualTo(orders);
        assertThat(actual.get(0).getId()).isEqualTo(order1.getId());
        assertThat(actual.get(1).getId()).isEqualTo(order2.getId());
    }

    @DisplayName("특정 주문의 상태는 조리, 식사, 계산완료 순서로 진행된다.")
    @Test
    void changeOrderStatus() {
        //given
        Order order = new Order();
        order.setId(1L);
        order.setOrderStatus(OrderStatus.COOKING.name());
        Order mealOrder = new Order();
        mealOrder.setOrderStatus(OrderStatus.MEAL.name());
        Order completionOrder = new Order();
        completionOrder.setOrderStatus(OrderStatus.COMPLETION.name());

        //and
        given(orderDao.findById(any())).willReturn(Optional.of(order));

        //when
        Order actual1 = orderService.changeOrderStatus(order.getId(), mealOrder);
        //then
        assertThat(actual1.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());

        //when
        Order actual2 = orderService.changeOrderStatus(order.getId(), completionOrder);
        //then
        assertThat(actual2.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @DisplayName("주문 완료 상태인 경우 변경할 수 없다.")
    @Test
    void changeOrderStatusExceptionIfSameStatusBefore() {
        //given
        Order order = new Order();
        order.setId(1L);
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        Order changeOrder = new Order();
        changeOrder.setOrderStatus(OrderStatus.COMPLETION.name());

        //and
        given(orderDao.findById(any())).willReturn(Optional.of(order));

        //when
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), changeOrder))
                .isInstanceOf(IllegalArgumentException.class); //then
    }
}
