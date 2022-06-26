package kitchenpos.order.application;

import kitchenpos.application.OrderService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import kitchenpos.menu.service.MenuServiceTest;
import kitchenpos.table.application.TableServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    private OrderService orderService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderListItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuDao, orderDao, orderListItemDao, orderTableDao);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        when(menuDao.countByIdIn(any())).thenReturn(1L);
        OrderTable orderTable = new OrderTable(1L, 3, false);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));
        when(orderDao.save(any())).thenReturn(createOrder());
        when(orderListItemDao.save(any())).thenReturn(createOrderListItem());

        // when
        Order created = orderService.create(createOrder());

        // then
        assertThat(created).isNotNull();
    }

    @DisplayName("[예외] 주문 항목이 없는 주문을 생성한다.")
    @Test
    void createOrder_without_order_list_item() {
        // when, then
        assertThatThrownBy(() -> {
            orderService.create(createOrderWithoutOrderListItem());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 메뉴와 메뉴 항목이 일치하지 않는 주문을 생성한다.")
    @Test
    void createOrder_menu_and_order_list_item_not_matching() {
        when(menuDao.countByIdIn(any())).thenReturn(2L);

        // when, then
        assertThatThrownBy(() -> {
            orderService.create(createOrder());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 주문 테이블 없는 주문을 생성한다.")
    @Test
    void createOrder_without_order_table() {
        when(menuDao.countByIdIn(any())).thenReturn(1L);
        when(orderTableDao.findById(any())).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> {
            orderService.create(createOrder());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 비어 있는 주문 테이블에서 주문을 생성한다.")
    @Test
    void createOrder_with_empty_order_table() {
        when(menuDao.countByIdIn(any())).thenReturn(1L);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(new OrderTable(true)));

        // when, then
        assertThatThrownBy(() -> {
            orderService.create(createOrder());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        when(orderDao.findAll()).thenReturn(createOrderList());

        // when
        List<Order> list = orderService.list();

        // then
        assertThat(list).isNotNull();
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        when(orderDao.findById(any())).thenReturn(Optional.of(createOrder()));
        when(orderDao.save(any())).thenReturn(createOrder());
        when(orderListItemDao.findAllByOrderId(any())).thenReturn(Arrays.asList(createOrderListItem()));

        // when
        Order changeOrder = new Order(OrderStatus.COMPLETION.name());
        Order updated = orderService.changeOrderStatus(1L, changeOrder);

        // then
        assertThat(updated).isNotNull();
        assertThat(updated.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @DisplayName("[예외] 저장되지 않은 주문의 상태를 변경한다.")
    @Test
    void changeOrderStatus_with_not_saved_order() {
        when(orderDao.findById(any())).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> {
            Order changeOrder = new Order(OrderStatus.COMPLETION.name());
            orderService.changeOrderStatus(1L, changeOrder);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 계산 완료 상태에서 추가로 주문 상태를 변경한다.")
    @Test
    void changeOrderStatus_with_completion_state() {
        when(orderDao.findById(any())).thenReturn(Optional.of(createOrderWithCompletion()));

        // when, then
        assertThatThrownBy(() -> {
            Order changeOrder = new Order(OrderStatus.COMPLETION.name());
            orderService.changeOrderStatus(1L, changeOrder);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    public static Order createOrder() {
        OrderLineItem orderListItem = createOrderListItem();
        OrderTable orderTable = new OrderTable(1L, 3, false);
        return new Order(1L, orderTable.getId(), Arrays.asList(orderListItem));
    }

    public static Order createOrderWithoutOrderListItem() {
        OrderTable orderTable = new OrderTable(1L, 3, false);
        return new Order(1L, orderTable.getId(), null);
    }

    public static Order createOrderWithCompletion() {
        OrderLineItem orderListItem = createOrderListItem();
        OrderTable orderTable = new OrderTable(1L, 3, false);
        return new Order(1L, orderTable.getId(), OrderStatus.COMPLETION.name(), Arrays.asList(orderListItem));
    }

    public static OrderLineItem createOrderListItem() {
        Menu menu = MenuServiceTest.createMenu01();
        return new OrderLineItem(1L, 1L, menu.getId(), 1);
    }

    public static List<Order> createOrderList() {
        return Collections.singletonList(createOrder());
    }

}
