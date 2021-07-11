package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DisplayName("주문 관련 기능 테스트")
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

    private OrderTable 주문테이블;
    private OrderLineItem 짜장면_메뉴;
    private OrderLineItem 짬뽕_메뉴;
    private Order 주문;

    @BeforeEach
    void setUp() {
        주문테이블 = new OrderTable();
        주문테이블.setId(1L);
        주문테이블.setEmpty(false);
        주문테이블.setNumberOfGuests(2);

        짜장면_메뉴 = new OrderLineItem();
        짜장면_메뉴.setMenuId(1L);
        짜장면_메뉴.setQuantity(1L);

        짬뽕_메뉴 = new OrderLineItem();
        짬뽕_메뉴.setMenuId(2L);
        짬뽕_메뉴.setQuantity(1L);

        주문 = new Order();
        주문.setOrderTableId(1L);
    }

    @Test
    void 주문_생성_기능() {
        주문.setOrderLineItems(Arrays.asList(짜장면_메뉴, 짬뽕_메뉴));
        when(menuDao.countByIdIn(Arrays.asList(1L, 2L))).thenReturn(2L);
        when(orderTableDao.findById(주문.getOrderTableId())).thenReturn(Optional.of(주문테이블));
        주문.setId(1L);
        when(orderDao.save(주문)).thenReturn(주문);
        when(orderLineItemDao.save(짜장면_메뉴)).thenReturn(짜장면_메뉴);
        when(orderLineItemDao.save(짬뽕_메뉴)).thenReturn(짬뽕_메뉴);

        Order expected = orderService.create(주문);
        assertThat(expected.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(expected.getOrderTableId()).isEqualTo(주문테이블.getId());
        assertThat(expected.getOrderLineItems().size()).isEqualTo(2);
        Set<Long> orderIds = expected.getOrderLineItems().stream().map(OrderLineItem::getOrderId).collect(Collectors.toSet());
        assertThat(orderIds.size()).isEqualTo(1);
        assertThat(orderIds).contains(expected.getId());
    }

    @Test
    void 주문_아이템이_존재하지않을경우_에러발생() {
        assertThatThrownBy(() -> orderService.create(주문)).isInstanceOf(IllegalArgumentException.class);

        주문.setOrderLineItems(Collections.emptyList());
        assertThatThrownBy(() -> orderService.create(주문)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문한_아이템의_갯수와_실제_조회한_아이템의_갯수가_일치하지않을때_에러발생() {
        주문.setOrderLineItems(Arrays.asList(짜장면_메뉴, 짬뽕_메뉴));
        when(menuDao.countByIdIn(Arrays.asList(1L, 2L))).thenReturn(1L);
        assertThatThrownBy(() -> orderService.create(주문)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블_정보가_존재하지않을시_에러발생() {
        주문.setOrderLineItems(Arrays.asList(짜장면_메뉴, 짬뽕_메뉴));
        when(menuDao.countByIdIn(Arrays.asList(1L, 2L))).thenReturn(2L);
        when(orderTableDao.findById(주문.getOrderTableId())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> orderService.create(주문)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_비어있는_상태이면_에러발생() {
        주문테이블.setEmpty(true);
        주문.setOrderLineItems(Arrays.asList(짜장면_메뉴, 짬뽕_메뉴));
        when(menuDao.countByIdIn(Arrays.asList(1L, 2L))).thenReturn(2L);
        when(orderTableDao.findById(주문.getOrderTableId())).thenReturn(Optional.of(주문테이블));
        assertThatThrownBy(() -> orderService.create(주문)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_리스트_조회() {
        주문.setId(1L);
        주문.setOrderStatus("COOKING");

        when(orderDao.findAll()).thenReturn(Arrays.asList(주문));
        when(orderLineItemDao.findAllByOrderId(주문.getId())).thenReturn(Arrays.asList(짜장면_메뉴, 짬뽕_메뉴));

        List<Order> orders = orderService.list();
        assertThat(orders.size()).isEqualTo(1);
        Order expected = orders.get(0);

        assertThat(expected.getOrderTableId()).isEqualTo(주문테이블.getId());
        assertThat(expected.getOrderStatus()).isEqualTo("COOKING");
        assertThat(expected.getOrderLineItems().size()).isEqualTo(2);
    }

    @Test
    void 주문_상태_변경_식사() {
        주문.setId(1L);
        주문.setOrderStatus("MEAL");

        when(orderDao.findById(주문.getId())).thenReturn(Optional.of(주문));
        when(orderDao.save(주문)).thenReturn(주문);
        when(orderLineItemDao.findAllByOrderId(주문.getId())).thenReturn(Arrays.asList(짜장면_메뉴, 짬뽕_메뉴));

        Order expected = orderService.changeOrderStatus(주문.getId(), 주문);

        assertThat(expected.getOrderStatus()).isEqualTo("MEAL");
        assertThat(expected.getOrderLineItems().size()).isEqualTo(2);
    }

    @Test
    void 존재하지않는_주문의_상태_변경_시_에러발생() {
        주문.setId(99L);
        주문.setOrderStatus("MEAL");
        when(orderDao.findById(주문.getId())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 주문)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 완료된_주문의_상태를_변경_시_에러발생() {
        Order order = new Order();
        order.setId(99L);
        order.setOrderStatus("COMPLETION");

        주문.setId(99L);
        주문.setOrderStatus("MEAL");
        when(orderDao.findById(주문.getId())).thenReturn(Optional.of(order));
        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 주문)).isInstanceOf(IllegalArgumentException.class);
    }
}
