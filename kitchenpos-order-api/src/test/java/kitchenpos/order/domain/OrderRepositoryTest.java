package kitchenpos.order.domain;

import kitchenpos.tablegroup.domain.OrderTable;
import kitchenpos.tablegroup.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("OrderRepository 테스트")
@DataJpaTest
class OrderRepositoryTest {
    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;

    @BeforeEach
    void setUp() {
        주문테이블1 = orderTableRepository.save(new OrderTable(2, false));
        주문테이블2 = orderTableRepository.save(new OrderTable(2, false));

        orderRepository.save(new Order(주문테이블1.getId(), OrderStatus.COMPLETION));
        orderRepository.save(new Order(주문테이블1.getId(), OrderStatus.MEAL));
        orderRepository.save(new Order(주문테이블1.getId(), OrderStatus.COOKING));

        orderRepository.save(new Order(주문테이블2.getId(), OrderStatus.COMPLETION));
        orderRepository.save(new Order(주문테이블2.getId(), OrderStatus.MEAL));
    }

    @Test
    void 주문_테이블_ID로_주문_목록_검색() {
        List<Order> orders = orderRepository.findAllByOrderTableId(주문테이블1.getId());

        assertThat(orders).hasSize(3);
    }

    @Test
    void 주문_테이블_ID_목록으로_주문_목록_검색() {
        List<Order> orders = orderRepository.findAllByOrderTableIds(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()));

        assertThat(orders).hasSize(5);
    }
}