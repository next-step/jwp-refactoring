package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    @TestFactory
    @DisplayName("기본 저장 확인")
    List<DynamicTest> save() {
        // given
        Order order = new Order(LocalDateTime.now(), 1L);
        OrderLineItem orderLineItem1 = new OrderLineItem(order, 1L, 3L);
        OrderLineItem orderLineItem2 = new OrderLineItem(order, 1L, 2L);
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
        Order order = new Order(LocalDateTime.now(), 1L);
        OrderLineItem orderLineItem1 = new OrderLineItem(order, 1L, 3L);
        OrderLineItem orderLineItem2 = new OrderLineItem(order, 1L, 2L);
        order.addOrderLineItem(orderLineItem1);
        order.addOrderLineItem(orderLineItem2);
        orderRepository.save(order);

        // when
        boolean exists = orderRepository.existsByOrderTableIdAndOrderStatusIn(1L, Arrays.asList(OrderStatus.COOKING));

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("특정 주문상태를 가진 다수의 주문 테이블이 존재하는지 확인")
    void existsByOrderTableIdInAndOrderStatusIn() {
        // given
        Order order = new Order(LocalDateTime.now(), 1L);
        OrderLineItem orderLineItem1 = new OrderLineItem(order, 1L, 3L);
        OrderLineItem orderLineItem2 = new OrderLineItem(order, 1L, 2L);
        order.addOrderLineItem(orderLineItem1);
        order.addOrderLineItem(orderLineItem2);
        orderRepository.save(order);

        // when
        boolean exists = orderRepository.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L),
                Arrays.asList(OrderStatus.COOKING));

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("여러 테이블 ID 기준으로 주문 찾기")
    void find_by_orderIds() {
        // given
        Order order1 = new Order(LocalDateTime.now(), 1L);
        Order order2 = new Order(LocalDateTime.now(), 2L);
        orderRepository.saveAll(Arrays.asList(order1, order2));

        // when
        List<Order> orders = orderRepository.findByOrderTableIdIn(Arrays.asList(1L, 2L));

        // then
        assertThat(orders).size().isEqualTo(2);
    }

    @Test
    @DisplayName("단일 테이블 ID 기준으로 주문 찾기")
    void find_by_orderId() {
        // given
        Order order = new Order(LocalDateTime.now(), 1L);
        orderRepository.save(order);

        // when
        Optional<Order> orders = orderRepository.findByOrderTableId(order.getId());

        // then
        assertThat(order).isNotNull();
    }
}
