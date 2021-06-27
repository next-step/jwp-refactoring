package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
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
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 기능 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
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

    private Order order;
    private OrderLineItem orderLineItem;
    private List<OrderLineItem> orderLineItems;
    private List<Long> menuIds;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderLineItems = new ArrayList<>();

        orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setOrderId(1L);
        orderLineItem.setQuantity(1L);
        orderLineItem.setSeq(1L);

        orderLineItems.add(orderLineItem);

        orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setEmpty(false);
        orderTable.setTableGroupId(1L);
        orderTable.setNumberOfGuests(0);

        order = new Order();
        order.setId(1L);
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderLineItems(orderLineItems);
        order.setOrderTableId(orderTable.getId());

        menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    @Test
    @DisplayName("1 개 이상의 등록된 메뉴로 주문을 등록할 수 있다.")
    public void create() throws Exception {
        // given
        given(menuDao.countByIdIn(menuIds)).willReturn((long)orderLineItems.size());
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));
        given(orderDao.save(order)).willReturn(order);
        given(orderLineItemDao.save(orderLineItem)).willReturn(orderLineItem);

        // when
        Order order = orderService.create(this.order);

        // then
        assertThat(order.getId()).isEqualTo(this.order.getId());
    }

    @Test
    @DisplayName("빈 테이블에는 주문을 등록할 수 없다.")
    public void createFail() throws Exception {
        // given
        orderTable.setEmpty(true);
        given(menuDao.countByIdIn(menuIds)).willReturn((long)orderLineItems.size());
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));

        // when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(this.order));
    }

    @Test
    @DisplayName("주문의 목록을 조회할 수 있다.")
    public void list() throws Exception {
        // given
        given(orderDao.findAll()).willReturn(Collections.singletonList(order));
        given(orderLineItemDao.findAllByOrderId(order.getId())).willReturn(orderLineItems);

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders).isNotEmpty();
        assertThat(orders.get(0).getId()).isEqualTo(order.getId());
        assertThat(orders.get(0).getOrderStatus()).isEqualTo(order.getOrderStatus());
        assertThat(orders.get(0).getOrderLineItems()).isEqualTo(order.getOrderLineItems());
    }

    @Test
    @DisplayName("주문 상태를 변경할 수 있다.")
    public void changeOrderStatus() throws Exception {
        // given
        given(orderDao.findById(order.getId())).willReturn(Optional.of(order));
        given(orderLineItemDao.findAllByOrderId(order.getId())).willReturn(orderLineItems);

        // when
        Order order = orderService.changeOrderStatus(this.order.getId(), this.order);

        // then
        assertThat(order.getId()).isEqualTo(this.order.getId());
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    @DisplayName("주문 상태가 계산 완료인 경우 변경할 수 없다.")
    public void changeOrderStatusFail() throws Exception {
        // given
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        given(orderDao.findById(order.getId())).willReturn(Optional.of(order));

        // when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(this.order.getId(), this.order));
    }
}