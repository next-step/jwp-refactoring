package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void findAllByOrderTableIdIn() {
        // given
        final OrderTable orderTable = new OrderTable(new TableGroup(), 1);
        final Menu menu = new Menu("name", BigDecimal.ONE, new MenuGroup("name").getId());
        final OrderLineItem orderLineItem = new OrderLineItem(menu, 1);
        final List<OrderLineItem> orderLineItemList = Collections.singletonList(orderLineItem);
        final OrderLineItems orderLineItems = new OrderLineItems(orderLineItemList);
        final Order order = orderRepository.save(new Order(orderTable, orderLineItems));

        // when
        final List<Order> actual = orderRepository.findAllByOrderTable_IdIn(
            Collections.singletonList(order.getOrderTableId()));

        // then
        assertThat(actual).containsExactly(order);
    }
}
