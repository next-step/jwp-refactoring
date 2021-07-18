package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    void findAllByOrderTableIdIn() {
        // given
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        final OrderTable orderTable = new OrderTable(tableGroup.getId(), 1);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        final Order order = orderRepository.save(new Order(savedOrderTable.getId()));

        // when
        final List<Order> actual = orderRepository.findAllByOrderTableIdIn(
            Collections.singletonList(order.getOrderTableId()));

        // then
        Assertions.assertThat(actual).containsExactly(order);
    }

    @Test
    void save() {
        // given
        final Order order = new Order(1L);

        // when
        final Order save = orderRepository.save(order);

        // then
        assertThat(save.getId()).isNotNull();
        assertThat(save.getOrderTableId()).isNotNull();
    }
}
