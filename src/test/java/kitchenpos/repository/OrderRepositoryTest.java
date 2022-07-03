package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
            OrderLineItemRepository orderLineItemRepository;

    Order order;
    Order savedOrder;
    OrderLineItem orderLineItem;

    @BeforeEach
    void setUp() {
        order = new Order();
        orderLineItem = new OrderLineItem(1L, order, new Menu(), 1);
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.add(orderLineItem);

        order.setOrderLineItems(orderLineItems);
    }

    @Test
    @DisplayName("Order 저장 시 orderLineItem cascade 저장 확인")
    public void saveCascade() {
        savedOrder = orderRepository.save(order);

        assertThat(savedOrder.getOrderLineItems()).isNotNull();
        assertThat(savedOrder.getOrderLineItems().getOrderLineItems()).contains(orderLineItem);
    }
}