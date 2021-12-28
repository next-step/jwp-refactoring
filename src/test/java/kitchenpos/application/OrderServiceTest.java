package kitchenpos.application;

import kitchenpos.dao.*;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 테스트")
class OrderServiceTest {

    private final MenuDao menuDao = new FakeMenuDao();
    private final OrderDao orderDao = new FakeOrderDao();
    private final OrderLineItemDao orderLineItemDao = new FakeOrderLineItemDao();
    private final OrderTableDao orderTableDao = new FakeOrderTableDao();
    private final OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

    private Menu 소고기메뉴;

    @BeforeEach
    void setUp() {
        Menu menu = Menu.of("소고기세트", 70000, 1L,
                Arrays.asList(
                        MenuProduct.of(1L, 2),
                        MenuProduct.of(2L, 1)
                )
        );
        소고기메뉴 = menuDao.save(menu);
    }

    @DisplayName("주문 항목이 없으면 예외 발생한다")
    @Test
    void notExistsOrderLineItems() {
        OrderTable orderTable = orderTableDao.save(OrderTable.of(10, false));
        Order order = Order.of(orderTable.getId(), Collections.emptyList());

        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("주문 테이블이 없으면 예외 발생한다.")
    @Test
    void notExistsOrderTable() {
        OrderTable orderTable = orderTableDao.save(OrderTable.of(10, false));
        Order order = Order.of(2L,
                Arrays.asList(
                        OrderLineItem.of(소고기메뉴.getId(), 10)
                )
        );

        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("주문 테이블이 공석이면 예외가 발생한다")
    @Test
    void empty() {
        OrderTable orderTable = orderTableDao.save(OrderTable.of(10, true));
        Order order = Order.of(orderTable.getId(),
                Arrays.asList(
                        OrderLineItem.of(소고기메뉴.getId(), 10)
                )
        );

        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("주문 저장 성공")
    @Test
    void success() {
        OrderTable orderTable = orderTableDao.save(OrderTable.of(10, false));
        Order order = Order.of(orderTable.getId(),
                Arrays.asList(
                        OrderLineItem.of(소고기메뉴.getId(), 10)
                )
        );

        Order result = orderService.create(order);
        assertAll(
                () -> assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(result.getOrderTableId()).isEqualTo(orderTable.getId()),
                () -> equalOrderLineItem(result, order)
        );
    }

    @DisplayName("모든 주문 조회")
    @Test
    void list() {
        OrderTable orderTable = orderTableDao.save(OrderTable.of(10, false));
        Order order1 = Order.of(orderTable.getId(),
                Arrays.asList(
                        OrderLineItem.of(소고기메뉴.getId(), 10)
                )
        );
        OrderTable orderTable2 = orderTableDao.save(OrderTable.of(15, false));
        Order order2 = Order.of(orderTable2.getId(),
                Arrays.asList(
                        OrderLineItem.of(소고기메뉴.getId(), 20)
                )
        );

        Order resultOrder1 = orderService.create(order1);
        Order resultOrder2 = orderService.create(order2);

        List<Order> list = orderService.list();

        long count = getOrderLineItemCount(list);
        assertAll(
                () -> assertThat(list.size()).isEqualTo(2),
                () -> assertThat(count).isEqualTo(resultOrder1.getOrderLineItems().size() + resultOrder2.getOrderLineItems().size())
        );
    }

    @DisplayName("주문이 없으면 예외가 발생한다.")
    @Test
    void changeStatus() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(33L, Order.of(OrderStatus.COMPLETION.name())));
    }

    @DisplayName("주문 상태가 COMPLETION 이면 예외가 발생한다.")
    @Test
    void orderStatusComplete() {
        Order order = new Order(1L,
                1L,
                OrderStatus.COMPLETION.name(),
                LocalDateTime.now(),
                Arrays.asList(OrderLineItem.of(소고기메뉴.getId(), 20))
        );
        orderDao.save(order);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(order.getId(), Order.of(OrderStatus.COMPLETION.name())));
    }

    @DisplayName("주문 상태 변경 성공")
    @Test
    void successChangeStatus() {
        OrderTable orderTable = orderTableDao.save(OrderTable.of(10, false));
        Order order = Order.of(orderTable.getId(),
                Arrays.asList(
                        OrderLineItem.of(소고기메뉴.getId(), 10)
                )
        );
        Order result = orderService.create(order);
        Order resultOrder = orderService.changeOrderStatus(result.getId(), Order.of(OrderStatus.COMPLETION.name()));

        assertAll(
                () -> assertThat(resultOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name()),
                () -> equalOrderLineItem(result, order)
        );
    }

    private long getOrderLineItemCount(List<Order> list) {
        long count = list.stream()
                .map(order -> order.getOrderLineItems())
                .flatMap(orderLineItems -> orderLineItems.stream())
                .count();
        return count;
    }

    private void equalOrderLineItem(Order result, Order order) {
        List<OrderLineItem> resultOrderLineItems = result.getOrderLineItems();
        List<OrderLineItem> orderLineItems = order.getOrderLineItems();
        for (int i = 0; i < resultOrderLineItems.size(); i++) {
            OrderLineItem resultOrderLineItem = resultOrderLineItems.get(i);
            OrderLineItem orderLineItem = orderLineItems.get(i);
            assertThat(resultOrderLineItem.getMenuId()).isEqualTo(orderLineItem.getMenuId());
            assertThat(resultOrderLineItem.getOrderId()).isEqualTo(result.getId());
            assertThat(resultOrderLineItem.getQuantity()).isEqualTo(orderLineItem.getQuantity());
        }
    }

}
