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

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 관련 기능 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
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
        orderLineItem = 주문_수량_생성(1L, 1L, 1L, 1L);

        orderTable = 주문테이블_생성(1L, 1L, 0, false);
    }

    @DisplayName("주문하려는 메뉴 목록의 수량이 한개 이상이어야 한다.")
    @Test
    void create_메뉴목록_수량_0개_예외() {
        Order order = 수량이_0개인_주문_생성();

        수량이_0개인_주문_생성_예외_발생함(order);
    }

    @DisplayName("메뉴에 등록되지 않은 주문 항목은 주문 할 수 없다.")
    @Test
    void create_메뉴_등록되지_않은_주문_메뉴_예외() {
        Order order = 메뉴에_등록되지_않은_주문_항목_생성();

        메뉴에_등록되지_않은_주문_항목_생성시_예외_발생함(order);
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        Order order = 주문_생성(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now());
        주문_수량_설정함(order);

        주문_등록전_설정(order);

        Order createOrder = 주문_생성_요청(order);

        주문_생성됨(createOrder, order);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        Order order = 주문_생성(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now());
        주문_수량_설정함(order);

        주문_목록_조회전_설정(order);

        List<Order> orders = 주문_목록_요청();

        주문_목록_확인됨(orders, order);
    }

    @DisplayName("주문상태가 완료가 되면 변경 불가능하다.")
    @Test
    void changeOrderStatus_완료시_변경_불가() {
        Order order = 주문_생성(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now());
        주문_수량_설정함(order);

        주문_상태_변경전_설정(order);

        주문상태_완료일경우_변경시_예외발생함(order);
    }

    @DisplayName("주문상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        Order order = 주문_생성(1L, 1L, OrderStatus.MEAL.name(), LocalDateTime.now());
        주문_수량_설정함(order);

        주문_상태_변경전_설정(order);

        주문_상태_변경_요청(order);

        주문_상태_변경됨(order);
    }

    public static Order 주문_생성(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime) {
        return new Order(id, orderTableId, orderStatus, orderedTime);
    }

    private OrderLineItem 주문_수량_생성(Long seq, Long orderId, Long menuId, Long quantity) {
        return new OrderLineItem(1L, 1L, 1L, 1L);
    }

    private OrderTable 주문테이블_생성(long id, long tableGroupId, int numberOfGuests, boolean isEmpty) {
        return new OrderTable(id, tableGroupId, numberOfGuests, isEmpty);
    }

    private Order 수량이_0개인_주문_생성() {
        LocalDateTime now = LocalDateTime.now();
        return new Order(1L, orderTable.getId(), OrderStatus.COOKING.name(), now);
    }

    private void 수량이_0개인_주문_생성_예외_발생함(Order order) {
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private void 메뉴에_등록되지_않은_주문_항목_생성시_예외_발생함(Order order) {
        assertThatThrownBy(() ->orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Order 메뉴에_등록되지_않은_주문_항목_생성() {
        Order order =  주문_생성(1L, 1L, OrderStatus.MEAL.name(), LocalDateTime.now());
        orderLineItemList.add(orderLineItem);
        order.updateOrderLineItems(orderLineItemList);
        return order;
    }

    private void 주문_등록전_설정(Order order) {
        메뉴_목록_등록();
        given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));
        given(orderDao.save(order)).willReturn(order);
        given(orderLineItemDao.save(orderLineItem)).willReturn(orderLineItem);
    }

    private void 주문_생성됨(Order createOrder, Order order) {
        assertThat(createOrder.getId()).isEqualTo(order.getId());
        assertThat(createOrder.getOrderStatus()).isEqualTo(order.getOrderStatus());
        assertThat(createOrder.getOrderTableId()).isEqualTo(order.getOrderTableId());
    }

    private void 주문_수량_설정함(Order order) {
        orderLineItemList.add(orderLineItem);
        order.updateOrderLineItems(orderLineItemList);
    }

    private Order 주문_생성_요청(Order order) {
        return orderService.create(order);
    }

    private List<Order> 주문_목록_요청() {
        return orderService.list();
    }

    private void 메뉴_목록_등록() {
        menuIds = orderLineItemList.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        given(menuDao.countByIdIn(menuIds)).willReturn((long)orderLineItemList.size());
    }

    private void 주문_목록_조회전_설정(Order order) {
        given(orderDao.findAll()).willReturn(Collections.singletonList(order));
        given(orderLineItemDao.findAllByOrderId(order.getId())).willReturn(orderLineItemList);
    }

    private void 주문_목록_확인됨(List<Order> orders, Order order) {
        assertThat(orders).isNotEmpty();
        assertThat(orders.get(0).getId()).isEqualTo(order.getId());
        assertThat(orders.get(0).getOrderStatus()).isEqualTo(order.getOrderStatus());
        assertThat(orders.get(0).getOrderLineItems()).isEqualTo(order.getOrderLineItems());
    }

    private void 주문상태_완료일경우_변경시_예외발생함(Order order) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(order.getId(), order));
    }

    private void 주문_상태_변경전_설정(Order order) {
        given(orderDao.findById(order.getId())).willReturn(Optional.of(order));
    }

    private void 주문_상태_변경됨(Order order) {
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    private void 주문_상태_변경_요청(Order order) {
        order.updateOrderStatus(OrderStatus.COOKING.name());
        orderService.changeOrderStatus(order.getId(), order);
    }
}