//package kitchenpos.application;
//
//import kitchenpos.application.order.OrderService;
//import kitchenpos.dao.MenuDao;
//import kitchenpos.dao.OrderDao;
//import kitchenpos.dao.OrderLineItemDao;
//import kitchenpos.dao.OrderTableDao;
//import kitchenpos.domain.order.Order;
//import kitchenpos.domain.order.OrderLineItem;
//import kitchenpos.domain.order.OrderStatus;
//import kitchenpos.domain.order.OrderTable;
//import org.assertj.core.util.Lists;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//
//@DisplayName("주문 관리")
//@ExtendWith(MockitoExtension.class)
//class OrderServiceTest {
//    private OrderService orderService;
//
//    @Mock
//    private MenuDao menuDao;
//
//    @Mock
//    private OrderDao orderDao;
//
//    @Mock
//    private OrderLineItemDao orderLineItemDao;
//
//    @Mock
//    private OrderTableDao orderTableDao;
//
//    private List<OrderLineItem> orderLineItems;
//    private OrderLineItem orderLineItem1;
//    private OrderLineItem orderLineItem2;
//    private OrderTable orderTable;
//
//    @BeforeEach
//    void setUp() {
//        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
//        orderLineItem1 = OrderLineItem.of(null, null, 1L, 2);
//        orderLineItem2 = OrderLineItem.of(null, null, 2L, 3);
//        orderLineItems = Lists.list(orderLineItem1, orderLineItem2);
//        orderTable = OrderTable.of(1L, null, 0, false);
//    }
//
//    @DisplayName("주문을 추가한다.")
//    @Test
//    void create() {
//        //given
//        Order order = Order.of(null, null, null, null, orderLineItems);
//
//        //and
//        given(menuDao.countByIdIn(any())).willReturn(2L);
//        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
//        given(orderDao.save(any())).willReturn(order);
//
//        //when
//        Order actual = orderService.create(order);
//
//        //then
//        assertThat(actual).isEqualTo(order);
//    }
//
//    @DisplayName("주문은 메뉴를 지정해야한다.")
//    @Test
//    void createOrderExceptionIfMenuIsNull() {
//        //given
//        Order order = Order.of(null, null, null, null, orderLineItems);
//
//        //and
//        given(menuDao.countByIdIn(any())).willReturn(0L); //메뉴를 지정하지 않음
//
//        //when
//        assertThatThrownBy(() -> orderService.create(order))
//                .isInstanceOf(IllegalArgumentException.class); //then
//    }
//
//    @DisplayName("주문은 메뉴 수량을 지정해야한다.")
//    @Test
//    void createOrderExceptionIfMenuQuantityIsNull() {
//        //TODO: 추가 기능 개발
//    }
//
//    @DisplayName("주문은 주문 테이블을 지정해야한다.")
//    @Test
//    void createOrderExceptionIfOrderTableIsNull() {
//        //given
//        Order order = Order.of(null, null, null, null, orderLineItems);
//        orderTable.setEmpty(true);
//
//        //and
//        given(menuDao.countByIdIn(any())).willReturn(2L);
//        given(orderTableDao.findById(any())).willReturn(Optional.empty()); // 주문테이블 없음
//
//        //when
//        assertThatThrownBy(() -> orderService.create(order))
//                .isInstanceOf(IllegalArgumentException.class); //then
//    }
//
//    @DisplayName("주문 불가능 상태일 경우 주문할 수 없다.")
//    @Test
//    void createOrderExceptionIfTableEmptyIsTrue() {
//        //given
//        Order order = Order.of(null, null, null, null, orderLineItems);
//        orderTable.setEmpty(true);
//
//        //and
//        given(menuDao.countByIdIn(any())).willReturn(2L);
//        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
//
//        //when
//        assertThatThrownBy(() -> orderService.create(order))
//                .isInstanceOf(IllegalArgumentException.class); //then
//    }
//
//    @DisplayName("주문을 모두 조회한다.")
//    @Test
//    void list() {
//        //given
//        Order order1 = Order.of(1L, null, null, null, null);
//        Order order2 = Order.of(2L, null, null, null, null);
//        List<Order> orders = Lists.list(order1, order2);
//
//        //and
//        given(orderDao.findAll()).willReturn(orders);
//
//        //when
//        List<Order> actual = orderService.list();
//
//        //then
//        assertThat(actual).isEqualTo(orders);
//        assertThat(actual.get(0).getId()).isEqualTo(order1.getId());
//        assertThat(actual.get(1).getId()).isEqualTo(order2.getId());
//    }
//
//    @DisplayName("특정 주문의 상태는 조리, 식사, 계산완료 순서로 진행된다.")
//    @Test
//    void changeOrderStatus() {
//        //given
//        Order order = Order.of(1L, null, OrderStatus.COOKING.name(), null, null);
//        Order mealOrder = Order.of(1L, null, OrderStatus.MEAL.name(), null, null);
//        Order completionOrder = Order.of(1L, null, OrderStatus.COMPLETION.name(), null, null);
//
//        //and
//        given(orderDao.findById(any())).willReturn(Optional.of(order));
//
//        //when
//        Order actual1 = orderService.changeOrderStatus(order.getId(), mealOrder);
//        //then
//        assertThat(actual1.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
//
//        //when
//        Order actual2 = orderService.changeOrderStatus(order.getId(), completionOrder);
//        //then
//        assertThat(actual2.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
//    }
//
//    @DisplayName("주문 완료 상태인 경우 변경할 수 없다.")
//    @Test
//    void changeOrderStatusExceptionIfSameStatusBefore() {
//        //given
//        Order order = Order.of(1L, null, OrderStatus.COMPLETION.name(), null, null);
//        Order changeOrder = Order.of(1L, null, OrderStatus.COMPLETION.name(), null, null);
//
//        //and
//        given(orderDao.findById(any())).willReturn(Optional.of(order));
//
//        //when
//        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), changeOrder))
//                .isInstanceOf(IllegalArgumentException.class); //then
//    }
//}
