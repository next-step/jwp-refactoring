package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문을 관리한다")
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private TableService tableService;

    @DisplayName("주문들을 조회할수있다.")
    @Test
    void listTest() {
        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders).isNotNull();
    }

    @DisplayName("주문을 등록할수 있다.")
    @Test
    void createTest() {
        // given
        Menu menu = menuService.list().get(0);
        OrderTable orderTable = tableService.create(
            new OrderTable(1, false)
        );

        Order order = new Order(orderTable, OrderStatus.COOKING);
        order.appendOrderLineItems(new OrderLineItem(order, menu, 1L));

        // when
        Order actualOrder = orderService.create(order);

        // then
        assertThat(actualOrder).isNotNull();
    }

    @DisplayName("주문 등록시, 주문 항목들이 존재해야 한다.")
    @Test
    void createExceptionTest1() {
        // given
        OrderTable orderTable = tableService.create(
            new OrderTable(1, false)
        );

        Order order = new Order(orderTable, OrderStatus.COOKING);

        // when
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("주문항목이 존재하지 않습니다.");
    }

    @DisplayName("주문 등록시, 주문한 메뉴가 존재하는 메뉴여야 한다.")
    @Test
    void createExceptionTest2() {
        // given
        Menu noneMenu = new Menu();
        OrderTable orderTable = tableService.create(
            new OrderTable(1, false)
        );

        Order order = new Order(orderTable, OrderStatus.COOKING);
        order.appendOrderLineItems(new OrderLineItem(order, noneMenu, 1L));

        // when
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("메뉴가 존재하지 않습니다.");
    }

    @DisplayName("주문 등록시, 주문 테이블이 존재해야 한다.")
    @Test
    void createExceptionTest3() {
        // given
        Menu menu = menuService.list().get(0);
        OrderTable orderTable = new OrderTable(TestUtils.getRandomId(), 1, false);

        Order order = new Order(TestUtils.getRandomId(), orderTable, OrderStatus.COOKING);
        order.appendOrderLineItems(new OrderLineItem(order, menu, 1L));

        // when
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("테이블이 존재하지 않습니다.");
    }

    @DisplayName("주문 등록시, 주문 테이블은 비어있지 않아야 한다.")
    @Test
    void createExceptionTest4() {
        // given
        Menu menu = menuService.list().get(0);
        OrderTable orderTable = tableService.create(
            new OrderTable(1, true)
        );

        Order order = new Order(orderTable, OrderStatus.COOKING);
        order.appendOrderLineItems(new OrderLineItem(order, menu, 1L));

        // when
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("빈 테이블에서는 주문을 할수가 없습니다.");
    }

    @DisplayName("주문상태를 수정할수 있다.")
    @Test
    void changeOrderStatusTest() {
        // given
        Menu menu = menuService.list().get(0);
        OrderTable orderTable = tableService.create(
            new OrderTable(1, false)
        );

        Order order = new Order(orderTable, OrderStatus.COOKING);
        order.appendOrderLineItems(new OrderLineItem(order, menu, 1L));
        order = orderService.create(order);

        // when
        order.chaangeOrderStatus(OrderStatus.MEAL);
        Order changedOrder = orderService.changeOrderStatus(order.getId(), order);

        // then
        assertThat(changedOrder).isNotNull();
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("주문상태를 수정시, 존재하는 주문만 상태를 변경 가능하다.")
    @Test
    void changeOrderStatusExceptionTest1() {
        // given
        Menu menu = menuService.list().get(0);
        OrderTable orderTable = tableService.create(
            new OrderTable(1, false)
        );

        Order order = new Order(TestUtils.getRandomId(), orderTable, OrderStatus.COOKING);
        order.appendOrderLineItems(new OrderLineItem(order, menu, 1L));

        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), order))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("해당 주문은 존재하지 않습니다.");
    }

    @DisplayName("주문상태를 수정시, 계산 완료 상태의 주문은 상태 변경이 불가하다.")
    @Test
    void changeOrderStatusExceptionTest2() {
        // given
        Menu menu = menuService.list().get(0);
        OrderTable orderTable = tableService.create(
            new OrderTable(1, false)
        );

        Order order = new Order(orderTable, OrderStatus.COMPLETION);
        order.appendOrderLineItems(new OrderLineItem(order, menu, 1L));
        Order createdOrder = orderService.create(order);

        // when
        createdOrder.chaangeOrderStatus(OrderStatus.COMPLETION);
        Order changedOrder = orderService.changeOrderStatus(createdOrder.getId(), createdOrder);

        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(changedOrder.getId(), changedOrder))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("상태는 변경 불가능");
    }
}