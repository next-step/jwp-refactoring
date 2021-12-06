package kitchenpos.application;

import kitchenpos.dao.*;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.fixture.MenuFixture.메뉴;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * - 주문을 등록할 수 있다
 * - 주문 항목 목록이 올바르지 않으면 주문을 등록할 수 없다
 *     - 주문 항목 목록은 비어 있을 수 없다
 * - 주문 항목 개수과 존재하는 메뉴 개수가 일치하지 않으면 등록할 수 없다
 * - 주문 테이블이 존재하지 않으면 등록할 수 없다
 * - 주문 테이블이 올바르지 않으면 주문을 등록할 수 없다
 *     - 주문 테이블은 빈 주문 테이블이 아니어야 한다
 * - 주문 목록을 조회할 수 있다
 * - 주문 상태를 변경할 수 있다
 * - 주문 상태 올바르지 않으면 주문 상태를 변경할 수 없다
 *     - 주문 상태가 완료가 아니어야 한다
 * - 주문이 존재하지 않으면 등록할 수 없다
 */
class OrderServiceTest {

    private static final int 수량 = 1;
    private static final Menu 메뉴 = 메뉴(1L, "후라이드", new BigDecimal(17_000), 메뉴_상품(1L, 1));
    private static final OrderTable 주문_테이블 = 주문_테이블(false, 2);

    private MenuDao menuDao;
    private OrderDao orderDao;
    private OrderLineItemDao orderLineItemDao;
    private OrderTableDao orderTableDao;
    private OrderService orderService;
    private Menu 저장된_메뉴;
    private OrderTable 저장된_주문_테이블;

    @BeforeEach
    void setUp() {
        menuDao = new InMemoryMenuDao();
        orderDao = new InMemoryOrderDao();
        orderLineItemDao = new InMemoryOrderLineItemDao();
        orderTableDao = new InMemoryOrderTableDao();
        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

        저장된_메뉴 = menuDao.save(메뉴);
        저장된_주문_테이블 = orderTableDao.save(주문_테이블);
    }

    @Test
    void create_주문을_등록할_수_있다() {
        Order savedOrder = orderService.create(주문(저장된_주문_테이블, 주문_항목(저장된_메뉴, 수량)));
        assertAll(
                () -> assertThat(savedOrder.getOrderTableId()).isEqualTo(저장된_주문_테이블.getId()),
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(savedOrder.getOrderedTime()).isNotNull(),
                () -> assertThat(savedOrder.getOrderLineItems().size()).isEqualTo(1),
                () -> assertThat(savedOrder.getOrderLineItems().get(0).getOrderId()).isEqualTo(savedOrder.getId()),
                () -> assertThat(savedOrder.getOrderLineItems().get(0).getMenuId()).isEqualTo(저장된_메뉴.getId()),
                () -> assertThat(savedOrder.getOrderLineItems().get(0).getQuantity()).isEqualTo(수량)
        );
    }

    @Test
    void create_주문_항목_목록이_올바르지_않으면_주문을_등록할_수_없다() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.create(주문(저장된_주문_테이블, Collections.emptyList())));
    }

    @Test
    void create_주문_항목_개수과_존재하는_메뉴_개수가_일치하지_않으면_등록할_수_없다() {
        List<OrderLineItem> 존재하는_메뉴_개수_이상의_주문_항목 = Arrays.asList(주문_항목(저장된_메뉴, 수량), 주문_항목(저장된_메뉴, 수량));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.create(주문(저장된_주문_테이블, 존재하는_메뉴_개수_이상의_주문_항목)));
    }

    @ParameterizedTest
    @ValueSource(longs = {0L})
    void create_주문_테이블이_존재하지_않으면_등록할_수_없다(Long 존재하지_않는_주문_테이블_아이디) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.create(주문(존재하지_않는_주문_테이블_아이디, 주문_항목(저장된_메뉴, 수량))));
    }

    @Test
    void create_주문_테이블이_올바르지_않으면_주문을_등록할_수_없다() {
        OrderTable 비어있는_주문_테이블 = orderTableDao.save(주문_테이블(true, 2));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.create(주문(비어있는_주문_테이블, 주문_항목(저장된_메뉴, 수량))));
    }

    @Test
    void list_주문_목록을_조회할_수_있다() {
        Order 저장된_주문 = orderService.create(주문(저장된_주문_테이블, 주문_항목(저장된_메뉴, 수량)));
        List<Order> orders = orderService.list();
        assertAll(
                () -> assertThat(orders.size()).isEqualTo(1),
                () -> assertThat(orders.get(0).getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(orders.get(0).getOrderTableId()).isEqualTo(저장된_주문_테이블.getId()),
                () -> assertThat(orders.get(0).getOrderedTime()).isNotNull(),
                () -> assertThat(orders.get(0).getOrderLineItems().size()).isEqualTo(1),
                () -> assertThat(orders.get(0).getOrderLineItems().get(0).getOrderId()).isEqualTo(저장된_주문.getId()),
                () -> assertThat(orders.get(0).getOrderLineItems().get(0).getMenuId()).isEqualTo(저장된_메뉴.getId()),
                () -> assertThat(orders.get(0).getOrderLineItems().get(0).getQuantity()).isEqualTo(수량)
        );
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"MEAL"})
    void changeOrderStatus_주문_상태를_변경할_수_있다(OrderStatus 변경될_주문_상태) {
        Order 저장된_주문 = orderService.create(주문(저장된_주문_테이블, 주문_항목(저장된_메뉴, 수량)));
        Order 변경될_주문 = 주문(저장된_주문_테이블, 주문_항목(저장된_메뉴, 수량), 변경될_주문_상태);

        Order 변경된_주문 = orderService.changeOrderStatus(저장된_주문.getId(), 변경될_주문);

        assertThat(변경된_주문.getOrderStatus()).isEqualTo(변경될_주문_상태.name());
    }

    private static OrderTable 주문_테이블(boolean empty, int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }

    private static Order 주문(Long orderTableId, OrderLineItem orderLineItem) {
        return 주문(orderTableId, Arrays.asList(orderLineItem), OrderStatus.COOKING);
    }

    private static Order 주문(Long orderTableId, List<OrderLineItem> orderLineItems, OrderStatus orderStatus) {
        Order order = new Order();
        order.setOrderStatus(orderStatus.name());
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    private static Order 주문(OrderTable orderTable, OrderLineItem orderLineItem, OrderStatus orderStatus) {
        return 주문(orderTable.getId(), Arrays.asList(orderLineItem), orderStatus);
    }

    private static Order 주문(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return 주문(orderTable.getId(), orderLineItems, OrderStatus.COOKING);
    }

    private static Order 주문(OrderTable orderTable, OrderLineItem orderLineItem) {
        return 주문(orderTable.getId(), Arrays.asList(orderLineItem), OrderStatus.COOKING);
    }

    private static OrderLineItem 주문_항목(Long menuId, int quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    private static OrderLineItem 주문_항목(Menu menu, int quantity) {
        return 주문_항목(menu.getId(), quantity);
    }
}