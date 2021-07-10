package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    private Order order;

    @BeforeEach
    public void setup() {
        List<OrderLineItem> orderLineItemList = new ArrayList<>();
        OrderTable orderTable = orderTableRepository.save(new OrderTable(0, TableStatus.ORDER));
        Menu menu = new Menu(3L, "반반치킨", new BigDecimal(16000), new MenuGroup("한마리메뉴"));
        String orderStatus = OrderStatus.COOKING.name();
        orderLineItemList.add(new OrderLineItem(menu.id(), 1));

        order = orderRepository.save(new Order(orderTable, orderStatus, LocalDateTime.now()));
        orderLineItemList.forEach(orderLineItem -> orderLineItem.mappingOrder(order.id()));

        OrderLineItems orderLineItems = new OrderLineItems(orderLineItemList);
        orderLineItems.mappingOrder(order.id());
        order.mappingOrderLineItems(orderLineItems);
    }

    @Test
    @DisplayName("생성한 주문을 저장 한다")
    @Transactional
    public void createOrder() {
        // when
        Order saveOrder = orderRepository.save(order);

        // then
        assertThat(saveOrder).isEqualTo(order);
    }

    @Test
    @DisplayName("주문 상태를 변경한다")
    @Transactional
    public void modifyOrder() {
        // given
        order.changeOrderStatus(OrderStatus.MEAL.name());

        // when
        Order saveOrder = orderRepository.save(order);

        // then
        assertThat(saveOrder.orderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    @DisplayName("주문 리스트를 가져온다")
    @Transactional
    public void selectOrderList() {
        // when
        List<Order> orders = orderRepository.findAll();

        // then
        for (Order selectOrder : orders) {
            assertThat(selectOrder.id()).isNotNull();
            assertThat(selectOrder.orderTable()).isNotNull();
            assertThat(selectOrder.getOrderLineItems()).isNotEmpty();
        }
    }
}
