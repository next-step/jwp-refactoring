package kitchenpos.table.application;

import java.util.Collections;
import javax.transaction.Transactional;
import kitchenpos.menu.application.MenuTestFixture;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.util.dto.SaveMenuDto;
import org.springframework.stereotype.Component;

@Component
public class OrderTableTestFixture {

    private final OrderRepository orderRepository;
    private final MenuTestFixture menuTestFixture;

    public OrderTableTestFixture(OrderRepository orderRepository, MenuTestFixture menuTestFixture) {
        this.orderRepository = orderRepository;
        this.menuTestFixture = menuTestFixture;
    }

    @Transactional
    public OrderTable 메뉴_만들고_주문하기(SaveMenuDto saveMenuDto, int quantity, OrderTable orderTable) {
        Menu menu = menuTestFixture.메뉴_만들기(saveMenuDto);
        OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), quantity);

        Order order = new Order(orderTable.getId(), Collections.singletonList(orderLineItem));

        orderRepository.save(order);
        return orderTable;
    }

}
