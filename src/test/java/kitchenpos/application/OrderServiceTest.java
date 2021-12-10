package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.application.TableServiceTest.orderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("주문 관리")
class OrderServiceTest {

//    private MenuDao menuDao;
//    private OrderDao orderDao;
//    private OrderLineItemDao orderLineItemDao;
//    private OrderTableDao orderTableDao;
//    private OrderService orderService;
//    private Menu 등록된_메뉴;
//
//    public static Order order(Long id, Long orderTableId, String orderStatus) {
//        Order order = new Order();
//        order.setId(id);
//        order.setOrderTableId(orderTableId);
//        order.setOrderStatus(orderStatus);
//        order.setOrderedTime(LocalDateTime.now());
//        return order;
//    }
//
//    public static OrderLineItem orderLineItem(Long seq, Order order, Long menuId, long quantity) {
//        OrderLineItem orderLineItem = new OrderLineItem();
//        orderLineItem.setSeq(seq);
//        orderLineItem.setOrderId(order.getId());
//        orderLineItem.setMenuId(menuId);
//        orderLineItem.setQuantity(quantity);
//        return orderLineItem;
//    }
//
//    @BeforeEach
//    void setUp() {
//        this.menuDao = mock(MenuDao.class);
//        this.orderDao = mock(OrderDao.class);
//        this.orderLineItemDao = mock(OrderLineItemDao.class);
//        this.orderTableDao = mock(OrderTableDao.class);
//        this.orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
//        this.등록된_메뉴 = menu(1L, "후라이드치킨", BigDecimal.valueOf(15000), 1L);
//    }
//
//    private void setMockOrderData(Order order, OrderLineItem orderLineItem, OrderTable orderTable, List<Menu> menus) {
//        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
//        when(orderDao.save(any(Order.class))).thenReturn(order);
//        when(orderLineItemDao.save(any(OrderLineItem.class))).thenReturn(orderLineItem);
//        when(menuDao.countByIdIn(anyList())).thenReturn(Long.valueOf(menus.size()));
//    }
//
//    @Test
//    @DisplayName("주문 내역 조회")
//    void list() {
//        // given
//        OrderTable orderTable = orderTable(1L, null, 0, false);
//        Order order1 = order(1L, orderTable.getId(), OrderStatus.COOKING.name());
//        Order order2 = order(2L, orderTable.getId(), OrderStatus.COOKING.name());
//        when(orderDao.findAll()).thenReturn(new ArrayList<>(Arrays.asList(order1, order2)));
//
//        // when
//        List<Order> actual = orderService.list();
//
//        // then
//        assertAll(
//                () -> assertThat(actual).containsAll(Arrays.asList(order1, order2)),
//                () -> assertThat(actual).hasSize(2)
//        );
//    }
//
//    @Nested
//    @DisplayName("주문 생성")
//    class CreateOrder {
//        @Test
//        @DisplayName("성공")
//        void createSuccess() {
//            // given
//            OrderTable orderTable = orderTable(1L, null, 0, false);
//            Order order = order(1L, orderTable.getId(), OrderStatus.COOKING.name());
//            OrderLineItem orderLineItem = orderLineItem(1L, order, 1L, 2);
//            order.setOrderLineItems(new ArrayList<>(Arrays.asList(orderLineItem)));
//            setMockOrderData(order, orderLineItem, orderTable, Arrays.asList(등록된_메뉴));
//
//            // when
//            Order actual = orderService.create(order);
//
//            // then
//            assertAll(
//                    () -> assertThat(actual).isEqualTo(order),
//                    () -> assertThat(actual.getOrderLineItems()).contains(orderLineItem)
//            );
//        }
//
//        @Test
//        @DisplayName("실패 - 등록되지 않은 메뉴 존재.")
//        void createFailNotExistsMenu() {
//            // given
//            OrderTable orderTable = orderTable(1L, null, 0, false);
//            Order order = order(1L, orderTable.getId(), OrderStatus.COOKING.name());
//            OrderLineItem orderLineItem = orderLineItem(1L, order, 1L, 2);
//            order.setOrderLineItems(new ArrayList<>(Arrays.asList(orderLineItem)));
//            setMockOrderData(order, orderLineItem, orderTable, Arrays.asList());
//
//            // when
//            assertThatThrownBy(() -> {
//                Order actual = orderService.create(order);
//            }).isInstanceOf(IllegalArgumentException.class);
//        }
//
//        @Test
//        @DisplayName("실패 - 주문 테이블 찾을 수 없음.")
//        void createFailNotExistsOrderTable() {
//            // given
//            OrderTable orderTable = orderTable(1L, null, 0, false);
//            Order order = order(1L, orderTable.getId(), OrderStatus.COOKING.name());
//            OrderLineItem orderLineItem = orderLineItem(1L, order, 1L, 2);
//            order.setOrderLineItems(new ArrayList<>(Arrays.asList(orderLineItem)));
//            setMockOrderData(order, orderLineItem, orderTable, Arrays.asList(등록된_메뉴));
//
//            when(orderTableDao.findById(anyLong())).thenReturn(Optional.empty());
//
//            // when
//            assertThatThrownBy(() -> {
//                Order actual = orderService.create(order);
//            }).isInstanceOf(IllegalArgumentException.class);
//        }
//    }
//
//    @Nested
//    @DisplayName("주문 수정 테스트")
//    class OrderStatusChange {
//        @Test
//        @DisplayName("성공")
//        void changeOrderStatusSuccess() {
//            // given
//            OrderTable orderTable = orderTable(1L, null, 0, false);
//            Order order = order(1L, orderTable.getId(), OrderStatus.MEAL.name());
//            OrderLineItem orderLineItem = orderLineItem(1L, order, 1L, 2);
//            order.setOrderLineItems(new ArrayList<>(Arrays.asList(orderLineItem)));
//            setMockOrderData(order, orderLineItem, orderTable, Arrays.asList(등록된_메뉴));
//            when(orderDao.findById(anyLong())).thenReturn(Optional.of(order));
//
//            // when
//            Order actual = orderService.changeOrderStatus(order.getId(), order);
//
//            // then
//            assertAll(
//                    () -> assertThat(actual).isEqualTo(order),
//                    () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name())
//            );
//        }
//
//        @Test
//        @DisplayName("실패 - 완료된 주문 건")
//        void changeOrderStatusFailOrderComplete() {
//            // given
//            OrderTable orderTable = orderTable(1L, null, 0, false);
//            Order order = order(1L, orderTable.getId(), OrderStatus.COMPLETION.name());
//            OrderLineItem orderLineItem = orderLineItem(1L, order, 1L, 2);
//            order.setOrderLineItems(new ArrayList<>(Arrays.asList(orderLineItem)));
//            setMockOrderData(order, orderLineItem, orderTable, Arrays.asList(등록된_메뉴));
//            when(orderDao.findById(anyLong())).thenReturn(Optional.of(order));
//
//            // when
//            assertThatThrownBy(() -> {
//                Order actual = orderService.changeOrderStatus(order.getId(), order);
//            }).isInstanceOf(IllegalArgumentException.class);
//        }
//    }

}
