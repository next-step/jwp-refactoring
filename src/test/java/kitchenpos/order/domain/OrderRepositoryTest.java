package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;

    private MenuGroup menuGroup;
    private Menu menu;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        menuGroup = menuGroupRepository.save(new MenuGroup("a"));
        menu = menuRepository.save(new Menu("A", BigDecimal.valueOf(10000.00), menuGroup.getId()));
        orderTable = orderTableRepository.save(new OrderTable(4, false));
    }

    @TestFactory
    @DisplayName("기본 저장 확인")
    List<DynamicTest> save() {
        // given
        Order order = new Order(LocalDateTime.now(), orderTable.getId());
        OrderLineItem orderLineItem1 = new OrderLineItem(order, menu.getId(), 3L);
        OrderLineItem orderLineItem2 = new OrderLineItem(order, menu.getId(), 2L);
        order.addOrderLineItem(orderLineItem1);
        order.addOrderLineItem(orderLineItem2);

        // when
        Order saveOrder = orderRepository.save(order);

        // then
        return Arrays.asList(
                dynamicTest("ID 생성 확인.", () -> assertThat(saveOrder.getId()).isNotNull()),
                dynamicTest("초기 주문상태 확인", () -> assertThat(saveOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING)),
                dynamicTest("주문 항목 개수 확인", () -> assertThat(saveOrder.getOrderLineItems().size()).isEqualTo(2)),
                dynamicTest("주문 항목 ID 생성 확인", () -> assertThat(saveOrder.getOrderLineItems().isExistsAllIds()).isTrue())
        );
    }

    @Test
    @DisplayName("특정 주문상태를 가진 하나의 주문 테이블이 존재하는지 확인")
    void existsByOrderTableIdAndOrderStatusIn() {
        // given
        Order order = new Order(LocalDateTime.now(), orderTable.getId());
        OrderLineItem orderLineItem1 = new OrderLineItem(order, menu.getId(), 3L);
        OrderLineItem orderLineItem2 = new OrderLineItem(order, menu.getId(), 2L);
        order.addOrderLineItem(orderLineItem1);
        order.addOrderLineItem(orderLineItem2);
        orderRepository.save(order);

        // when
        boolean exists = orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), Arrays.asList(OrderStatus.COOKING));

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("특정 주문상태를 가진 다수의 주문 테이블이 존재하는지 확인")
    void existsByOrderTableIdInAndOrderStatusIn() {
        // given
        Order order = new Order(LocalDateTime.now(), orderTable.getId());
        OrderLineItem orderLineItem1 = new OrderLineItem(order, menu.getId(), 3L);
        OrderLineItem orderLineItem2 = new OrderLineItem(order, menu.getId(), 2L);
        order.addOrderLineItem(orderLineItem1);
        order.addOrderLineItem(orderLineItem2);
        orderRepository.save(order);

        // when
        boolean exists = orderRepository.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(orderTable.getId()),
                Arrays.asList(OrderStatus.COOKING));

        // then
        assertThat(exists).isTrue();
    }
}
