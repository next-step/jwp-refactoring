package kitchenpos.table.application;

import java.util.Collections;
import javax.transaction.Transactional;
import kitchenpos.menu.application.MenuTestFixture;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class OrderTableTestFixture {

    private final MenuTestFixture menuServiceTestSupport;
    private final OrderRepository orderRepository;

    public OrderTableTestFixture(MenuTestFixture menuServiceTestSupport,
        OrderRepository orderRepository) {
        this.menuServiceTestSupport = menuServiceTestSupport;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderTable 후라이드_양념_세트_주문하기(OrderTable orderTable) {
        Menu 후라이드_양념_세트 = menuServiceTestSupport.후라이드_양념_세트_가져오기();
        OrderLineItem 후라이드_양념_세트_주문 = new OrderLineItem(후라이드_양념_세트.getId(), 1);

        Order 주문 = new Order(orderTable.getId(), Collections.singletonList(후라이드_양념_세트_주문));

        orderRepository.save(주문);
        return orderTable;
    }

}
