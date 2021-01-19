package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private MenuDao menuDao;
    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderService orderService;

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        // given
        Menu menu1 = menuDao.findById(1L).get();
        Menu menu2 = menuDao.findById(2L).get();
        OrderTable orderTable = orderTableDao.findById(1L).get();
        List<OrderLineItem> orderLineItems = Arrays.asList(
            new OrderLineItem(menu1.getId(), 1),
            new OrderLineItem(menu2.getId(), 1)
        );
        Order order = new Order(orderTable.getId());
        order.setOrderLineItems(orderLineItems);

        // when
        Order savedOrder = orderService.create(order);

        // then
        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getOrderLineItems().size()).isEqualTo(2);
    }

    @DisplayName("주문 등록 예외 - 주문하고자 하는 메뉴가 등록되어 있어야 한다.")
    @Test
    void create_exception1() {
        // given
        Menu menu1 = menuDao.findById(1L).get();
        OrderTable orderTable = orderTableDao.findById(1L).get();
        List<OrderLineItem> orderLineItems = Arrays.asList(
            new OrderLineItem(menu1.getId(), 1),
            new OrderLineItem(999999L, 1)
        );
        Order order = new Order(orderTable.getId());
        order.setOrderLineItems(orderLineItems);

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> orderService.create(order))
            .withMessage("주문하고자 하는 메뉴가 등록되어 있어야 한다.");
    }

    @DisplayName("주문 등록 예외 - 주문하고자 하는 테이블이 등록되어 있어야 한다.")
    @Test
    void create_exception2() {
        // given
        Menu menu1 = menuDao.findById(1L).get();
        Menu menu2 = menuDao.findById(2L).get();
        List<OrderLineItem> orderLineItems = Arrays.asList(
            new OrderLineItem(menu1.getId(), 1),
            new OrderLineItem(menu2.getId(), 1)
        );
        Order order = new Order(999999L);
        order.setOrderLineItems(orderLineItems);

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> orderService.create(order))
            .withMessage("주문하고자 하는 테이블이 등록되어 있어야 한다.");
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        // given
        Menu menu1 = menuDao.findById(1L).get();
        Menu menu2 = menuDao.findById(2L).get();
        OrderTable orderTable = orderTableDao.findById(1L).get();
        List<OrderLineItem> orderLineItems = Arrays.asList(
            new OrderLineItem(menu1.getId(), 1),
            new OrderLineItem(menu2.getId(), 1)
        );
        Order order = new Order(orderTable.getId());
        order.setOrderLineItems(orderLineItems);
        Order savedOrder = orderService.create(order);

        // when
        List<Order> list = orderService.list();

        // then
        assertThat(list).extracting("id").contains(savedOrder.getId());
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        Order order = orderDao.findById(1L).get();
        order.setOrderStatus(OrderStatus.MEAL.name());

        // when
        Order updatedOrder = orderService.changeOrderStatus(order.getId(), order);

        // then
        assertThat(updatedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문 상태 변경 예외 - 이미 완료 상태는 변경할 수 없다.")
    @Test
    void changeOrderStatus_exception1() {
        // given
        Order order = orderDao.findById(2L).get();

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> orderService.changeOrderStatus(order.getId(), order))
            .withMessage("이미 완료 상태는 변경할 수 없다.");

    }
}
