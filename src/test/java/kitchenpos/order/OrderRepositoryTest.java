package kitchenpos.order;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @DisplayName("주문하다")
    @Test
    void createOrder() {

        //given
        final int numberOfGuests = 10;
        final boolean isEmpty = false;
        final OrderTable orderTable = OrderTable.setting(numberOfGuests, isEmpty);
        final Order order = Order.create(orderTable);
        orderTableRepository.save(orderTable);

        //when
        Order savedOrder = orderRepository.save(order);

        //then
        assertThat(savedOrder.getId()).isGreaterThan(0L);
    }

    @DisplayName("주문 리스트 조회하기")
    @Test
    void getOrders() {

        //given
        final int numberOfGuests = 10;
        final boolean isEmpty = false;

        final OrderTable orderTable = OrderTable.setting(numberOfGuests, isEmpty);
        orderTableRepository.save(orderTable);
        final Order order = Order.create(orderTable);
        orderRepository.save(order);

        //when
        List<Order> orders = orderRepository.findAll();

        //then
        assertThat(orders).isNotEmpty();
    }
}
