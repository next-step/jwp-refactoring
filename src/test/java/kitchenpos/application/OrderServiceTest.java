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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 관련 기능 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    private Order order;
    private OrderLineItem orderLineItem;
    private List<OrderLineItem> orderLineItemList = new ArrayList<>();
    private List<Long> menuIds;
    private OrderTable orderTable;

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

    @BeforeEach
    void setUp() {
        orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setOrderId(1L);
        orderLineItem.setQuantity(1L);
        orderLineItem.setSeq(1L);
        orderLineItemList.add(orderLineItem);

        orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setEmpty(false);
        orderTable.setTableGroupId(1L);
        orderTable.setNumberOfGuests(0);

        order = new Order();
        order.setId(1L);
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderLineItems(orderLineItemList);
        //order.setOrderTableId(orderTable.getId());

        menuIds = orderLineItemList.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    @DisplayName("주문하려는 메뉴 목록의 수량이 한개 이상이어야 한다.")
    @Test
    void create_메뉴목록_수량_0개_예외() {
        // given
        orderLineItemList.remove(0);

        assertThatThrownBy(() ->orderService.create(this.order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록은 중복 될 수 없다.")
    @Test
    void create_메뉴_목록_중복_예외() {
        orderLineItemList.add(orderLineItem);

        assertThatThrownBy(() ->orderService.create(this.order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블이 비어있을경우 예외 발생")
    @Test
    void create_주문테이블_null_예외() {
        orderTable.setEmpty(true);

        given(menuDao.countByIdIn(menuIds)).willReturn((long)orderLineItemList.size());
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));

        assertThatThrownBy(() ->orderService.create(this.order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        given(menuDao.countByIdIn(menuIds)).willReturn((long)orderLineItemList.size());
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));
        given(orderDao.save(order)).willReturn(order);
        given(orderLineItemDao.save(orderLineItem)).willReturn(orderLineItem);

        Order createOrder = orderService.create(this.order);

        assertThat(createOrder.getId()).isEqualTo(order.getId());
        assertThat(createOrder.getOrderStatus()).isEqualTo(order.getOrderStatus());
        assertThat(createOrder.getOrderTableId()).isEqualTo(order.getOrderTableId());
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        given(orderDao.findAll()).willReturn(Collections.singletonList(order));
        given(orderLineItemDao.findAllByOrderId(order.getId())).willReturn(orderLineItemList);

        List<Order> orders = orderService.list();

        assertThat(orders).isNotEmpty();
        assertThat(orders.get(0).getId()).isEqualTo(order.getId());
        assertThat(orders.get(0).getOrderStatus()).isEqualTo(order.getOrderStatus());
        assertThat(orders.get(0).getOrderLineItems()).isEqualTo(order.getOrderLineItems());
    }

    @DisplayName("주주문상태가 완료가 되면 변경 불가능하다.")
    @Test
    void changeOrderStatus_완료시_변경_불가() {
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        given(orderDao.findById(order.getId())).willReturn(Optional.of(order));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(this.order.getId(), this.order));
    }

    @DisplayName("주문상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        order.setOrderStatus(OrderStatus.MEAL.name());
        given(orderDao.findById(order.getId())).willReturn(Optional.of(order));

        orderService.changeOrderStatus(this.order.getId(), this.order);
    }
}