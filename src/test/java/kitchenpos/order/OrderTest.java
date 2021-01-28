package kitchenpos.order;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class OrderTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    @DisplayName("주문을 등록합니다.")
    void save() {
        // when
        Order persistOrder = 주문_등록_요청(OrderStatus.COOKING);

        // then
        assertThat(persistOrder.getId()).isNotNull();
        assertThat(persistOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    private Order 주문_등록_요청(OrderStatus orderStatus) {
        Order order = new Order();
        order.setOrderStatus(orderStatus.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTable(this.orderTableRepository.save(new OrderTable(4, false)));

        return this.orderRepository.save(order);
    }


    @Test
    @DisplayName("특정 주문을 조회합니다.")
    void findById() {
        // given
        Order persistOrder = 주문_등록_요청(OrderStatus.COOKING);

        // when
        Order foundOrder = this.orderRepository.findById(persistOrder.getId()).get();

        // then
        assertThat(foundOrder.getId()).isEqualTo(persistOrder.getId());
        assertThat(foundOrder.getOrderedTime()).isEqualTo(persistOrder.getOrderedTime());
    }


    @Test
    @DisplayName("전체 주문을 조회합니다.")
    void findAll() {
        // given
        주문_등록_요청(OrderStatus.COOKING);
        주문_등록_요청(OrderStatus.COOKING);
        주문_등록_요청(OrderStatus.COOKING);

        // when
        List<Order> foundOrders = this.orderRepository.findAll();

        // then
        assertThat(foundOrders).hasSize(3);
    }

    @Test
    @DisplayName("테이블ID와 주문상태로 주문이 있는지 확인합니다.")
    void existsByOrderTableIdAndOrderStatusIn() {
        // given
        Order order = 주문_등록_요청(OrderStatus.COOKING);
        주문_등록_요청(OrderStatus.MEAL);

        // when
        boolean existsByOrderTableIdAndOrderStatusIn
                = this.orderRepository.existsByOrderTableIdAndOrderStatusIn(order.getOrderTable().getId()
                        , Arrays.asList(new String[]{OrderStatus.COOKING.name(), OrderStatus.MEAL.name()}));

        // then
        assertTrue(existsByOrderTableIdAndOrderStatusIn);
    }


    @Test
    @DisplayName("여러개의 테이블ID와 주문상태로 주문이 있는지 확인합니다.")
    void existsByOrderTableIdInAndOrderStatusIn() {
        // given
        Order order1 = 주문_등록_요청(OrderStatus.COOKING);
        Order order2 = 주문_등록_요청(OrderStatus.MEAL);

        // when
        boolean existsByOrderTableIdAndOrderStatusIn
                = this.orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                        Arrays.asList(new Long[]{order1.getOrderTable().getId(), order2.getOrderTable().getId()})
                        , Arrays.asList(new String[]{OrderStatus.COOKING.name(), OrderStatus.MEAL.name()}));

        // then
        assertTrue(existsByOrderTableIdAndOrderStatusIn);
    }
}
