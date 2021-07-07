package kitchenpos.application.unit;

import kitchenpos.application.OrderService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
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

    @DisplayName("주문 등록")
    @Test
    public void 주문_등록_확인() throws Exception {
        //given
        OrderLineItem orderLineItem1 = 주문항목_생성(1L, 1L, 1L, 4L);
        OrderLineItem orderLineItem2 = 주문항목_생성(2L, 2L, 1L, 2L);
        OrderTable orderTable = null;
        Order order = 주문_등록됨(1L, 1L, OrderStatus.COOKING.name(),
                Arrays.asList(orderLineItem1, orderLineItem2), LocalDateTime.now());
        given(menuDao.countByIdIn(Arrays.asList(1L, 2L))).willReturn(2L);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));
        Order createOrder = 주문_생성(1L, OrderStatus.COOKING.name(), Arrays.asList(orderLineItem1, orderLineItem2),
                LocalDateTime.now());
        given(orderDao.save(createOrder)).willReturn(order);
        given(orderLineItemDao.save(orderLineItem1)).willReturn(orderLineItem1);
        given(orderLineItemDao.save(orderLineItem2)).willReturn(orderLineItem2);

        //when
        Order saveOrder = orderService.create(createOrder);

        //then
        assertThat(saveOrder.getId()).isNotNull();
    }

    @DisplayName("주문 등록 예외 - 주문항목이 없을 경우")
    @Test
    public void 주문항목이없는경우_주문_등록_예외() throws Exception {
        //given
        Order createOrder = 주문_생성(1L, OrderStatus.COOKING.name(), Arrays.asList(), LocalDateTime.now());

        //when
        //then
        assertThatThrownBy(() -> orderService.create(createOrder)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 예외 - 주문항목 목록에 같은 메뉴가 있는 경우")
    @Test
    public void 주문항목목록에같은메뉴가있는경우_주문_등록_예외() throws Exception {
        //given
        OrderLineItem orderLineItem1 = 주문항목_생성(1L, 1L, 1L, 4L);
        OrderLineItem orderLineItem2 = 주문항목_생성(2L, 1L, 1L, 2L);
        given(menuDao.countByIdIn(Arrays.asList(1L, 1L))).willReturn(1L);
        Order createOrder = 주문_생성(1L, OrderStatus.COOKING.name(), Arrays.asList(orderLineItem1, orderLineItem2),
                LocalDateTime.now());

        //when
        //then
        assertThatThrownBy(() -> orderService.create(createOrder)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 예외 - 주문테이블이 없는 경우")
    @Test
    public void 주문테이블이없는경우_주문_등록_예외() throws Exception {
        //given
        OrderLineItem orderLineItem1 = 주문항목_생성(1L, 1L, 1L, 4L);
        OrderLineItem orderLineItem2 = 주문항목_생성(2L, 2L, 1L, 2L);
        given(menuDao.countByIdIn(Arrays.asList(1L, 2L))).willReturn(2L);
        given(orderTableDao.findById(1L)).willThrow(IllegalArgumentException.class);
        Order createOrder = 주문_생성(1L, OrderStatus.COOKING.name(), Arrays.asList(orderLineItem1, orderLineItem2),
                LocalDateTime.now());

        //when
        //then
        assertThatThrownBy(() -> orderService.create(createOrder)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 예외 - 주문테이블이 빈테이블인 경우")
    @Test
    public void 주문테이블이빈테이블인경우_주문_등록_예외() throws Exception {
        //given
        OrderLineItem orderLineItem1 = 주문항목_생성(1L, 1L, 1L, 4L);
        OrderLineItem orderLineItem2 = 주문항목_생성(2L, 2L, 1L, 2L);
        OrderTable orderTable = null;
        given(menuDao.countByIdIn(Arrays.asList(1L, 2L))).willReturn(2L);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));
        Order createOrder = 주문_생성(1L, OrderStatus.COOKING.name(), Arrays.asList(orderLineItem1, orderLineItem2),
                LocalDateTime.now());

        //when
        //then
        assertThatThrownBy(() -> orderService.create(createOrder)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록 조회")
    @Test
    public void 주문목록_조회_확인() throws Exception {
        //given
        OrderLineItem orderLineItem1 = 주문항목_생성(1L, 1L, 1L, 4L);
        OrderLineItem orderLineItem2 = 주문항목_생성(2L, 2L, 1L, 2L);
        Order order1 = 주문_등록됨(1L, 1L, OrderStatus.COOKING.name(),
                Arrays.asList(orderLineItem1, orderLineItem2), LocalDateTime.now());
        Order order2 = 주문_등록됨(2L, 2L, OrderStatus.COOKING.name(),
                Arrays.asList(orderLineItem1, orderLineItem2), LocalDateTime.now());
        Order order3 = 주문_등록됨(3L, 3L, OrderStatus.COOKING.name(),
                Arrays.asList(orderLineItem1, orderLineItem2), LocalDateTime.now());
        given(orderDao.findAll()).willReturn(Arrays.asList(order1, order2, order3));
        given(orderLineItemDao.findAllByOrderId(1L)).willReturn(Arrays.asList(orderLineItem1, orderLineItem2));
        given(orderLineItemDao.findAllByOrderId(2L)).willReturn(Arrays.asList(orderLineItem1, orderLineItem2));
        given(orderLineItemDao.findAllByOrderId(3L)).willReturn(Arrays.asList(orderLineItem1, orderLineItem2));

        //when
        List<Order> orders = orderService.list();

        //then
        assertThat(orders.size()).isEqualTo(3);
    }

    @DisplayName("주문상태 변경")
    @Test
    public void 주문상태_변경_확인() throws Exception {
        //given
        OrderLineItem orderLineItem1 = 주문항목_생성(1L, 1L, 1L, 4L);
        OrderLineItem orderLineItem2 = 주문항목_생성(2L, 2L, 1L, 2L);
        Order order = 주문_등록됨(1L, 1L, OrderStatus.COOKING.name(),
                Arrays.asList(orderLineItem1, orderLineItem2), LocalDateTime.now());
        given(orderDao.findById(1L)).willReturn(Optional.of(order));
        order.setOrderStatus(OrderStatus.MEAL);
        given(orderDao.save(any(Order.class))).willReturn(order);
        given(orderLineItemDao.findAllByOrderId(1L)).willReturn(Arrays.asList(orderLineItem1, orderLineItem2));

        //when
        Order changeOrder = 주문_생성(1L, OrderStatus.MEAL.name(),
                Arrays.asList(orderLineItem1, orderLineItem2), LocalDateTime.now());
        Order saveOrder = orderService.changeOrderStatus(1L, changeOrder);

        //then
        assertThat(saveOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문상태 변경 예외 - 주문이 없는 경우")
    @Test
    public void 주문이없는경우_주문상태_변경_예외() throws Exception {
        //given
        OrderLineItem orderLineItem1 = 주문항목_생성(1L, 1L, 1L, 4L);
        OrderLineItem orderLineItem2 = 주문항목_생성(2L, 2L, 1L, 2L);
        given(orderDao.findById(1L)).willThrow(IllegalArgumentException.class);
        Order changeOrder = 주문_생성(1L, OrderStatus.MEAL.name(),
                Arrays.asList(orderLineItem1, orderLineItem2), LocalDateTime.now());

        //when
        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, changeOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문상태 변경 예외 - 주문상태가 계산완료인 경우")
    @Test
    public void 주문상태가계산완료인경우_주문상태_변경_예외() throws Exception {
        //given
        OrderLineItem orderLineItem1 = 주문항목_생성(1L, 1L, 1L, 4L);
        OrderLineItem orderLineItem2 = 주문항목_생성(2L, 2L, 1L, 2L);
        Order order = 주문_등록됨(1L, 1L, OrderStatus.COOKING.name(),
                Arrays.asList(orderLineItem1, orderLineItem2), LocalDateTime.now());
        given(orderDao.findById(1L)).willReturn(Optional.of(order));
        order.setOrderStatus(OrderStatus.COMPLETION);
        Order changeOrder = 주문_생성(1L, OrderStatus.MEAL.name(),
                Arrays.asList(orderLineItem1, orderLineItem2), LocalDateTime.now());

        //when
        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, changeOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    public Order 주문_등록됨(Long id, Long orderTableId, String orderStatus, List<OrderLineItem> orderLineItems,
                        LocalDateTime orderedTime) {
        Order order = 주문_생성(orderTableId, orderStatus, orderLineItems, orderedTime);
        order.setId(id);
        return order;
    }

    public Order 주문_생성(Long orderTableId, String orderStatus, List<OrderLineItem> orderLineItems,
                       LocalDateTime orderedTime) {
        /*Order order = new Order();
        order.setOrderTableId(orderTableId);
        //order.setOrderStatus(orderStatus);
        order.setOrderLineItems(orderLineItems);
        order.setOrderedTime(orderedTime);*/
        return null;
    }

    public OrderLineItem 주문항목_생성(Long seq, Long menuId, Long orderId, Long quantity) {
        /*OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(seq);
        orderLineItem.setMenuId(menuId);
        //orderLineItem.setOrderId(orderId);
        orderLineItem.setQuantity(quantity);*/
        return null;
    }
}
