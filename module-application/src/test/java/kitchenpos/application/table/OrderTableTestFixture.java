package kitchenpos.application.table;

import java.util.Collections;
import javax.transaction.Transactional;
import kitchenpos.application.menu.MenuTestFixture;
import kitchenpos.application.util.dto.SaveMenuDto;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderMenu;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.table.OrderTable;
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
        OrderLineItem orderLineItem = new OrderLineItem(OrderMenu.of(menu), quantity);

        Order order = new Order(orderTable.getId(), Collections.singletonList(orderLineItem));

        orderRepository.save(order);
        return orderTable;
    }

}
