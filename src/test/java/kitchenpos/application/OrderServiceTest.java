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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

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

    private List<OrderLineItem> 주문_상품_내역;
    private Order 신규_주문_등록_요청;
    private OrderTable 주문_테이블;

    @BeforeEach
    void setUp() {
        this.주문_상품_내역 = Arrays.asList(
                new OrderLineItem(1L, 1L),
                new OrderLineItem(2L, 1L)
        );
        this.신규_주문_등록_요청 = new Order(1L, 주문_상품_내역);
        this.주문_테이블 = new OrderTable(1, false);
    }

    @Test
    void 주문_등록시_등록에_성공하고_주문_정보를_반환한다() {
        // given
        given(menuDao.countByIdIn(any())).willReturn((long) 주문_상품_내역.size());
        given(orderTableDao.findById(any())).willReturn(Optional.of(주문_테이블));
        given(orderDao.save(any())).willReturn(신규_주문_등록_요청);
        for (OrderLineItem orderLineItem : 주문_상품_내역) {
            given(orderLineItemDao.save(orderLineItem)).willReturn(orderLineItem);
        }

        // when
        Order 신규_주문 = orderService.create(신규_주문_등록_요청);

        // then
        주문_등록됨(신규_주문, 신규_주문_등록_요청);
    }

    @Test
    void 주문_등록시_주문_상품이_미동록된_경우_예외처리되어_등록에_실패한다() {
        // given
        신규_주문_등록_요청.setOrderLineItems(new ArrayList<>());

        // when & then
        assertThatThrownBy(() -> orderService.create(신규_주문_등록_요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_등록시_동록된_주문_상품의_개수가_다른경우_예외처리되어_등록에_실패한다() {
        // given
        given(menuDao.countByIdIn(any())).willReturn((long) 주문_상품_내역.size() + 1);

        // when
        assertThatThrownBy(() -> orderService.create(신규_주문_등록_요청))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        주문_등록_실패함();
    }

    @Test
    void 주문_등록시_주문_테이블이_미동록된_경우_예외처리되어_등록에_실패한다() {
        // given
        given(menuDao.countByIdIn(any())).willReturn((long) 주문_상품_내역.size());
        given(orderTableDao.findById(any())).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> orderService.create(신규_주문_등록_요청))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        then(menuDao).should(times(1)).countByIdIn(any());
        then(orderTableDao).should(times(1)).findById(any());
    }

    @Test
    void 주문_등록시_주문_테이블이_사용중인_경우_예외처리되어_등록에_실패한다() {
        // given
        given(menuDao.countByIdIn(any())).willReturn((long) 주문_상품_내역.size());
        주문_테이블.setEmpty(true);
        given(orderTableDao.findById(any())).willReturn(Optional.of(주문_테이블));

        // when
        assertThatThrownBy(() -> orderService.create(신규_주문_등록_요청))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        then(menuDao).should(times(1)).countByIdIn(any());
        then(orderTableDao).should(times(1)).findById(any());
    }

    @Test
    void 주문_목록_조회시_주문_목록을_반환한다() {
        // given
        List<Order> 기존_주문_목록 = Arrays.asList(
                new Order(1L, 주문_상품_내역),
                new Order(2L, 주문_상품_내역),
                new Order(3L, 주문_상품_내역)
        );
        given(orderDao.findAll()).willReturn(기존_주문_목록);
        given(orderLineItemDao.findAllByOrderId(any())).willReturn(주문_상품_내역);

        // when
        List<Order> 주문_목록 = orderService.list();

        // then
        주문_목록_조회됨(주문_목록, 기존_주문_목록);
    }

    @Test
    void 주문_상태_변경시_변경에_성공한다() {
        Order 주문_상태_변경_요청 = new Order(OrderStatus.COMPLETION);
        given(orderDao.findById(any())).willReturn(Optional.ofNullable(신규_주문_등록_요청));
        given(orderDao.save(any())).willReturn(신규_주문_등록_요청);
        given(orderLineItemDao.findAllByOrderId(any())).willReturn(주문_상품_내역);

        // when
        Order 상태_변경된_주문 = orderService.changeOrderStatus(신규_주문_등록_요청.getId(), 주문_상태_변경_요청);

        // then
        주문_상태_변경됨(상태_변경된_주문, 주문_상태_변경_요청);
    }

    @Test
    void 주문_상태_변경시_주문이_미등록된경우_예외처리되어_변경에_실패한다() {
        Order 주문_상태_변경_요청 = new Order(OrderStatus.COMPLETION);
        given(orderDao.findById(any())).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> orderService.changeOrderStatus(신규_주문_등록_요청.getId(), 주문_상태_변경_요청))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        주문_상태_변경_실패함();
    }

    @Test
    void 주문_상태_변경시_주문이_이미_완료된경우_예외처리되어_변경에_실패한다() {
        Order 주문_상태_변경_요청 = new Order(OrderStatus.COMPLETION);
        신규_주문_등록_요청.setOrderStatus(OrderStatus.COMPLETION.name());
        given(orderDao.findById(any())).willReturn(Optional.ofNullable(신규_주문_등록_요청));

        // when
        assertThatThrownBy(() -> orderService.changeOrderStatus(신규_주문_등록_요청.getId(), 주문_상태_변경_요청))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        주문_상태_변경_실패함();
    }


    private void 주문_등록됨(Order order, Order expectedOrder) {
        then(menuDao).should(times(1)).countByIdIn(any());
        then(orderTableDao).should(times(1)).findById(any());
        then(orderDao).should(times(1)).save(any());
        then(orderLineItemDao).should(times(주문_상품_내역.size())).save(any());

        List<Long> orderLineMenuIds = order.getOrderLineItems().stream().map(OrderLineItem::getMenuId).collect(Collectors.toList());
        List<Long> expectedOrderLineMenuIds = expectedOrder.getOrderLineItems().stream().map(OrderLineItem::getMenuId).collect(Collectors.toList());
        assertAll(
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(orderLineMenuIds).containsAll(expectedOrderLineMenuIds)
        );
    }

    private void 주문_등록_실패함() {
        then(menuDao).should(times(1)).countByIdIn(any());
    }

    private void 주문_목록_조회됨(List<Order> orders, List<Order> expectedOrders) {
        then(orderDao).should(times(1)).findAll();
        then(orderLineItemDao).should(times(expectedOrders.size())).findAllByOrderId(any());

        List<Long> orderTableIds = orders.stream().map(Order::getOrderTableId).collect(Collectors.toList());
        List<Long> expectedOrderTableIds = expectedOrders.stream().map(Order::getOrderTableId).collect(Collectors.toList());
        assertAll(
                () -> assertThat(orders).hasSize(expectedOrders.size()),
                () -> assertThat(orderTableIds).containsAll(expectedOrderTableIds)
        );
    }

    private void 주문_상태_변경됨(Order order, Order expectedOrder) {
        assertThat(order.getOrderStatus()).isEqualTo(expectedOrder.getOrderStatus());
    }

    private void 주문_상태_변경_실패함() {
        then(orderDao).should(times(1)).findById(any());
    }
}
